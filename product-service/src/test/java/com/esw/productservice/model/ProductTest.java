package com.esw.productservice.model;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class ProductTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve gerar publicId automaticamente ao persistir produto")
    void shouldGeneratePublicId() {
        Product product = new Product();
        product.setName("Valid name");
        product.setDescription("Valid desc");
        product.setPrice(new BigDecimal("99.99"));
        product.setCreatedAt(new Date());

        assertThat(product.getPublicId()).isNotNull();
    }

    @Test
    @DisplayName("Deve falhar na validação quando preço for zero ou negativo")
    void shouldFailValidationWhenPriceIsZero() {
        Product product = new Product();
        product.setName("Test");
        product.setDescription("Desc");
        product.setPrice(new BigDecimal("0.0"));

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("price"));
    }

    @Test
    @DisplayName("Deve passar na validação com dados válidos")
    void shouldPassValidationWithValidData() {
        Product product = new Product();
        product.setName("Produto legal");
        product.setDescription("Descrição bacana");
        product.setPrice(new BigDecimal("10.00"));
        product.setCreatedAt(new Date());

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve comparar produtos pela igualdade de IDs")
    void shouldCompareProductsById() {
        Product p1 = new Product();
        p1.setId(1L);
        Product p2 = new Product();
        p2.setId(1L);
        Product p3 = new Product();
        p3.setId(2L);

        assertThat(p1).isEqualTo(p2);
        assertThat(p1).isNotEqualTo(p3);
    }
}