package com.esw.productservice.service;

import com.esw.productservice.dto.product.ProductCreateRequest;
import com.esw.productservice.dto.product.ProductUpdateRequest;
import com.esw.productservice.exception.AlreadyExistsException;
import com.esw.productservice.exception.NotFoundException;
import com.esw.productservice.model.Category;
import com.esw.productservice.model.Product;
import com.esw.productservice.repository.ProductRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.esw.productservice.factory.ProductFactory;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final Validator validator;
    private final ProductFactory productFactory;


    public ProductService(ProductRepository productRepository, CategoryService categoryService, Validator validator, ProductFactory productFactory) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.validator = validator;
        this.productFactory = productFactory;
    }

    public Product findById(Long id) {
        return getProductOrThrows(id);
    }

    public List<Product> findAll() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            throw new NotFoundException("No products found");
        }

        return productRepository.findAll();
    }

    public List<Product> findAllByCategoryId(Long categoryId) {
        Category category = categoryService.findById(categoryId);

        Optional<List<Product>> products = productRepository.findByCategory(category);

        if (products.isEmpty()) {
            throw new NotFoundException("No products found");
        }

        return products.get();
    }

    @Transactional
    public Product save(ProductCreateRequest request) {
        validateIfProductExistsByName(request.name());
        Product product = productFactory.createProduct(request);
        //Product product = new Product();
        //product = getProduct(product, request.name(), request.price(), request.description(), request.categoryId());

        return productRepository.save(product);
    }

    @Transactional
    public Product update(Long id, ProductUpdateRequest request) {
        Product product = getProductOrThrows(id);
        product = getProduct(product, request.name(), request.price(), request.description(), request.categoryId());

        return productRepository.save(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = getProductOrThrows(id);
        productRepository.deleteById(product.getId());
    }

    private Product getProductOrThrows(Long id) throws NotFoundException {
        Optional<Product> product = productRepository.findById(id);

        if(product.isEmpty()) {
            throw new NotFoundException(Product.class, "id", id.toString());
        }

        return product.get();
    }

    private Product getProduct(Product product, String name, BigDecimal price, String description, Long categoryId) {
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setActive(true);
        product.setCategory(categoryService.findById(categoryId));

        validate(product);

        if(product.getCreatedAt() == null) {
            product.setCreatedAt(new Date());
        }

        return product;
    }

    private void validateIfProductExistsByName(String name) {
        if (productRepository.findByName(name).isPresent()) {
            throw new AlreadyExistsException(Product.class, "name", name);
        }
    }

    private void validate(Product product) {
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Validation failed", violations);
        }
    }

    private boolean existsWithPublicId(UUID value) {
        return this.productRepository.findByPublicId(value).isPresent();
    }
}
