package com.esw.productservice.service;

import com.esw.productservice.exception.NotFoundException;
import com.esw.productservice.model.Product;
import com.esw.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldReturnProductWhenIdExists() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Notebook");
        product.setDescription("Tecnologia");
        product.setPrice(new BigDecimal("3500.00"));
        product.setActive(true);

        Mockito.when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(product));

        Product result = productService.findById(1L);

        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        Mockito.when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Assertions.assertThrows (NotFoundException.class, () -> productService.findById(999L));
    }

}
