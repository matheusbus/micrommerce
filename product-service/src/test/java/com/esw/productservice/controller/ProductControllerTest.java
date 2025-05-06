package com.esw.productservice.controller;

import com.esw.productservice.model.Category;
import com.esw.productservice.model.Product;
import com.esw.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    private final MockMvc mockMvc;

    private final ProductRepository productRepository;

    public ProductControllerTest(MockMvc mockMvc, ProductRepository productRepository) {
        this.mockMvc = mockMvc;
        this.productRepository = productRepository;
    }

    @BeforeEach
    void setup() {
        productRepository.deleteAll();

        Product product = new Product();
        product.setId(null);
        product.setName("Cadeira Gamer");
        product.setDescription("Uma cadeira gamer muito legal.");
        product.setPrice(new BigDecimal("1200.00"));
        product.setActive(true);
        product.setCategory(new Category(1L, "Móveis", new Date(), null));

        productRepository.save(product);
    }

    @Test
    void shouldReturnListOfProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Cadeira Gamer"));
    }
}
