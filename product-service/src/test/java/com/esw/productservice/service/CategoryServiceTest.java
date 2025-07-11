package com.esw.productservice.service;

import com.esw.productservice.dto.category.CategoryCreateRequest;
import com.esw.productservice.dto.category.CategoryUpdateRequest;
import com.esw.productservice.exception.AlreadyExistsException;
import com.esw.productservice.exception.NotFoundException;
import com.esw.productservice.model.Category;
import com.esw.productservice.repository.CategoryRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    private CategoryRepository categoryRepository;
    private Validator validator;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        validator = mock(Validator.class);
        categoryService = new CategoryService(categoryRepository, validator);
    }

    @Test
    @DisplayName("Deve salvar nova categoria com sucesso")
    void shouldSaveCategorySuccessfully() {
        CategoryCreateRequest request = new CategoryCreateRequest("Periféricos");

        when(categoryRepository.findByName("Periféricos")).thenReturn(Optional.empty());
        when(validator.validate(any(Category.class))).thenReturn(Collections.emptySet());

        Category category = new Category();
        category.setId(1L);
        category.setName("Periféricos");

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.save(request);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Periféricos");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar categoria com nome duplicado")
    void shouldThrowAlreadyExistsException() {
        CategoryCreateRequest request = new CategoryCreateRequest("Monitores");

        when(categoryRepository.findByName("Monitores"))
                .thenReturn(Optional.of(new Category()));

        assertThatThrownBy(() -> categoryService.save(request))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("already exists with name");
    }

    @Test
    @DisplayName("Deve lançar exceção de validação ao salvar categoria inválida")
    void shouldThrowValidationExceptionOnInvalidCategory() {
        CategoryCreateRequest request = new CategoryCreateRequest("A");

        when(categoryRepository.findByName("A")).thenReturn(Optional.empty());

        Set<ConstraintViolation<Category>> violations = Set.of(mock(ConstraintViolation.class));
        when(validator.validate(any(Category.class))).thenReturn(violations);

        assertThatThrownBy(() -> categoryService.save(request))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Deve atualizar categoria existente com sucesso")
    void shouldUpdateCategorySuccessfully() {
        Long id = 1L;
        CategoryUpdateRequest request = new CategoryUpdateRequest("Novos Componentes");

        Category existing = new Category();
        existing.setId(id);
        existing.setName("Antigos");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));
        when(validator.validate(any(Category.class))).thenReturn(Collections.emptySet());

        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Category updated = categoryService.update(id, request);

        assertThat(updated.getName()).isEqualTo("Novos Componentes");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar categoria inexistente")
    void shouldThrowNotFoundExceptionOnUpdate() {
        Long id = 999L;
        CategoryUpdateRequest request = new CategoryUpdateRequest("Nova");

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.update(id, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("not found with id");
    }

    @Test
    @DisplayName("Deve excluir categoria existente")
    void shouldDeleteCategorySuccessfully() {
        Long id = 1L;
        Category category = new Category();
        category.setId(id);
        category.setName("Gamer");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        categoryService.delete(id);

        verify(categoryRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar excluir categoria inexistente")
    void shouldThrowNotFoundExceptionOnDelete() {
        Long id = 404L;

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.delete(id))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Deve retornar todas as categorias salvas")
    void shouldFindAllCategories() {
        Category c1 = new Category();
        c1.setId(1L);
        c1.setName("Monitores");

        Category c2 = new Category();
        c2.setId(2L);
        c2.setName("Placas");

        when(categoryRepository.findAll()).thenReturn(List.of(c1, c2));

        List<Category> result = categoryService.findAll();

        assertThat(result).hasSize(2).extracting(Category::getName)
                .containsExactly("Monitores", "Placas");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar todas e não encontrar nenhuma categoria")
    void shouldThrowNotFoundExceptionWhenNoCategoriesFound() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> categoryService.findAll())
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("No categories found");
    }

    @Test
    @DisplayName("Deve retornar categoria por ID existente")
    void shouldFindById() {
        Long id = 10L;
        Category category = new Category();
        category.setId(id);
        category.setName("Edição");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        Category result = categoryService.findById(id);

        assertThat(result).isNotNull().extracting(Category::getName).isEqualTo("Edição");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar categoria por ID inexistente")
    void shouldThrowNotFoundExceptionWhenCategoryNotFoundById() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.findById(500L))
                .isInstanceOf(NotFoundException.class);
    }
}