package com.esw.productservice.controller;

import com.esw.productservice.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.esw.productservice.dto.product.ProductCreateRequest;
import com.esw.productservice.dto.product.ProductUpdateRequest;
import com.esw.productservice.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product createSampleProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setPublicId(UUID.randomUUID());
        product.setName("Produto Teste");
        product.setDescription("Descrição Teste");
        product.setPrice(new BigDecimal("99.99"));
        product.setActive(true);
        product.setCreatedAt(new Date());
        return product;
    }

    @Test
    @DisplayName("GET /api/products - Deve retornar lista de produtos")
    void shouldReturnListOfProducts() throws Exception {
        List<Product> products = List.of(createSampleProduct());
        Mockito.when(productService.findAll()).thenReturn(products);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(products.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(products.get(0).getName()));
    }

    @Test
    @DisplayName("GET /api/products/{id} - Deve retornar produto pelo id")
    void shouldReturnProductById() throws Exception {
        Product product = createSampleProduct();
        Mockito.when(productService.findById(anyLong())).thenReturn(product);

        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()));
    }

    @Test
    @DisplayName("GET /api/products?categoryId=1 - Deve retornar lista de produtos por categoria")
    void shouldReturnProductsByCategoryId() throws Exception {
        List<Product> products = List.of(createSampleProduct());
        Mockito.when(productService.findAllByCategoryId(anyLong())).thenReturn(products);

        mockMvc.perform(get("/api/products").param("categoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(products.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(products.get(0).getName()));
    }

    @Test
    @DisplayName("POST /api/products - Deve criar um novo produto")
    void shouldCreateNewProduct() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest(
                "Novo Produto", "Descrição do novo produto", new BigDecimal("50.00"), 1L);

        Product createdProduct = createSampleProduct();
        createdProduct.setName(request.name());
        createdProduct.setDescription(request.description());
        createdProduct.setPrice(request.price());

        Mockito.when(productService.save(any(ProductCreateRequest.class))).thenReturn(createdProduct);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.description").value(request.description()));
    }

    @Test
    @DisplayName("PATCH /api/products/{id} - Deve atualizar produto existente")
    void shouldUpdateProduct() throws Exception {
        ProductUpdateRequest request = new ProductUpdateRequest(
                "Produto Atualizado", "Descrição atualizada", new BigDecimal("75.00"), 1L);

        Product updatedProduct = createSampleProduct();
        updatedProduct.setName(request.name());
        updatedProduct.setDescription(request.description());
        updatedProduct.setPrice(request.price());

        Mockito.when(productService.update(anyLong(), any(ProductUpdateRequest.class))).thenReturn(updatedProduct);

        mockMvc.perform(patch("/api/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.description").value(request.description()));
    }

    @Test
    @DisplayName("DELETE /api/products/{id} - Deve deletar produto")
    void shouldDeleteProduct() throws Exception {
        Mockito.doNothing().when(productService).delete(anyLong());

        mockMvc.perform(delete("/api/products/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}