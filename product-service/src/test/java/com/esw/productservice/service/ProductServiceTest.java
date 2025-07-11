package com.esw.productservice.service;

import com.esw.productservice.dto.product.ProductCreateRequest;
import com.esw.productservice.dto.product.ProductUpdateRequest;
import com.esw.productservice.exception.NotFoundException;
import com.esw.productservice.factory.ProductFactory;
import com.esw.productservice.model.Product;
import com.esw.productservice.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Optional;

import com.esw.productservice.exception.AlreadyExistsException;
import com.esw.productservice.model.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private Validator validator;

    @Mock
    private ProductFactory productFactory;

    @InjectMocks
    private ProductService productService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Deve salvar um produto válido com sucesso")
    void shouldSaveValidProduct() {
        ProductCreateRequest request = new ProductCreateRequest(
                "Mouse Gamer",
                "Mouse com RGB",
                BigDecimal.valueOf(149.90),
                1L
        );

        Category category = new Category();
        Product expectedProduct = new Product();
        expectedProduct.setName(request.name());
        expectedProduct.setPrice(request.price());
        expectedProduct.setDescription(request.description());
        expectedProduct.setCategory(category);
        expectedProduct.setActive(true);
        expectedProduct.setCreatedAt(new Date());

        when(productRepository.findByName(request.name())).thenReturn(Optional.empty());
        when(productFactory.createProduct(request)).thenReturn(expectedProduct);
        when(productRepository.save(expectedProduct)).thenReturn(expectedProduct);

        Product saved = productService.save(request);

        assertThat(saved.getName()).isEqualTo("Mouse Gamer");
        verify(productRepository).save(expectedProduct);
    }

    @Test
    @DisplayName("Deve lançar AlreadyExistsException quando o nome do produto já existir")
    void shouldThrowAlreadyExistsExceptionWhenNameExists() {
        ProductCreateRequest request = new ProductCreateRequest(
                "Repetido",
                "Descrição",
                BigDecimal.TEN,
                1L
        );

        when(productRepository.findByName("Repetido"))
                .thenReturn(Optional.of(new Product()));

        assertThatThrownBy(() -> productService.save(request))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("already exists");

        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve encontrar um produto pelo seu ID")
    void shouldFindProductById() {
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.findById(1L);

        assertThat(result).isEqualTo(product);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando o produto com o ID informado não existir")
    void shouldThrowNotFoundWhenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    @DisplayName("Deve retornar todos os produtos")
    void shouldFindAllProducts() {
        List<Product> products = List.of(new Product(), new Product());

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando não existirem produtos cadastrados")
    void shouldThrowNotFoundWhenFindAllReturnsEmpty() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> productService.findAll())
                .isInstanceOf(NotFoundException.class)
                .hasMessage("No products found");
    }

    @Test
    @DisplayName("Deve retornar todos os produtos de uma categoria pelo ID da categoria")
    void shouldFindAllProductsByCategoryId() {
        Long categoryId = 10L;
        Category category = new Category();
        List<Product> products = List.of(new Product());

        when(categoryService.findById(categoryId)).thenReturn(category);
        when(productRepository.findByCategory(category)).thenReturn(Optional.of(products));

        List<Product> result = productService.findAllByCategoryId(categoryId);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando não existirem produtos para a categoria informada")
    void shouldThrowNotFoundWhenFindAllByCategoryReturnsEmpty() {
        Long categoryId = 10L;
        Category category = new Category();

        when(categoryService.findById(categoryId)).thenReturn(category);
        when(productRepository.findByCategory(category)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findAllByCategoryId(categoryId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("No products found");
    }

    @Test
    @DisplayName("Deve atualizar o produto corretamente")
    void shouldUpdateProductCorrectly() {
        Long productId = 1L;
        Product existing = new Product();
        existing.setId(productId);
        existing.setCreatedAt(new Date());

        Category category = new Category();
        when(productRepository.findById(productId)).thenReturn(Optional.of(existing));
        when(categoryService.findById(5L)).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(existing);

        ProductUpdateRequest update = new ProductUpdateRequest(
                "Novo nome",
                "Nova descrição",
                new BigDecimal("99.99"),
                5L
        );

        Product updated = productService.update(productId, update);

        assertThat(updated.getName()).isEqualTo("Novo nome");
        assertThat(updated.getPrice()).isEqualTo(new BigDecimal("99.99"));
    }

    @Test
    @DisplayName("Deve deletar o produto com sucesso")
    void shouldDeleteProductSuccessfully() {
        Product product = new Product();
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.delete(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao tentar deletar produto inexistente")
    void shouldThrowNotFoundWhenDeletingNonExistingProduct() {
        when(productRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.delete(42L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    @DisplayName("Deve lançar ConstraintViolationException ao validar produto inválido")
    void shouldThrowConstraintViolationWhenInvalidProduct() {
        Product product = new Product();
        Set<ConstraintViolation<Product>> violations = Set.of(mock(ConstraintViolation.class));

        when(validator.validate(product)).thenReturn(violations);

        assertThatThrownBy(() -> {
            ProductService service = new ProductService(productRepository, categoryService, validator, productFactory);
            var method = ProductService.class.getDeclaredMethod("validate", Product.class);
            method.setAccessible(true);
            method.invoke(service, product);
        }).hasCauseInstanceOf(ConstraintViolationException.class);
    }
}