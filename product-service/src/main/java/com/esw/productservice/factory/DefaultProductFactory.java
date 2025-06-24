package com.esw.productservice.factory;

import com.esw.productservice.dto.product.ProductCreateRequest;
import com.esw.productservice.model.Product;
import com.esw.productservice.service.CategoryService;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class DefaultProductFactory implements ProductFactory {
    private final CategoryService categoryService;
    private final Validator validator;
    public DefaultProductFactory(CategoryService categoryService, Validator
            validator) {
        this.categoryService = categoryService;
        this.validator = validator;
    }
    @Override
    public Product createProduct(ProductCreateRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        product.setDescription(request.description());
        product.setActive(true);
        product.setCategory(categoryService.findById(request.categoryId()));
        product.setCreatedAt(new Date());
        return product;
    }
}
