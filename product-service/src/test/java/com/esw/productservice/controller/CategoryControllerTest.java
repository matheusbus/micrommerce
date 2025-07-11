package com.esw.productservice.controller;

import com.esw.productservice.dto.category.CategoryCreateRequest;
import com.esw.productservice.dto.category.CategoryUpdateRequest;
import com.esw.productservice.model.Category;
import com.esw.productservice.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/categories - Deve retornar todas as categorias")
    void shouldGetAllCategories() throws Exception {
        List<Category> categories = List.of(
                new Category(1L, "Periféricos", new Date(), List.of()),
                new Category(2L, "Componentes", new Date(), List.of())
        );

        when(categoryService.findAll()).thenReturn(categories);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Periféricos"))
                .andExpect(jsonPath("$[1].name").value("Componentes"));
    }

    @Test
    @DisplayName("GET /api/categories/{id} - Deve retornar categoria por ID")
    void shouldGetCategoryById() throws Exception {
        Category category = new Category(1L, "Monitores", new Date(), List.of());

        when(categoryService.findById(1L)).thenReturn(category);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Monitores"));
    }

    @Test
    @DisplayName("POST /api/categories - Deve criar nova categoria")
    void shouldCreateCategory() throws Exception {
        CategoryCreateRequest request = new CategoryCreateRequest("Gaming");
        Category category = new Category(1L, "Gaming", new Date(), List.of());

        when(categoryService.save(any())).thenReturn(category);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gaming"));
    }

    @Test
    @DisplayName("POST /api/categories - Deve falhar com nome inválido")
    void shouldFailToCreateWithInvalidName() throws Exception {
        CategoryCreateRequest request = new CategoryCreateRequest("A"); // muito curto

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PATCH /api/categories/{id} - Deve atualizar categoria")
    void shouldUpdateCategory() throws Exception {
        CategoryUpdateRequest request = new CategoryUpdateRequest("Atualizado");
        Category category = new Category(1L, "Atualizado", new Date(), List.of());

        when(categoryService.update(eq(1L), any())).thenReturn(category);

        mockMvc.perform(patch("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Atualizado"));
    }

    @Test
    @DisplayName("DELETE /api/categories/{id} - Deve deletar categoria com sucesso")
    void shouldDeleteCategory() throws Exception {
        doNothing().when(categoryService).delete(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).delete(1L);
    }
}