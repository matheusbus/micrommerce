package com.esw.productservice.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CategoryTest {

    @Test
    @DisplayName("Deve comparar categorias com base no ID")
    void shouldCompareCategoriesById() {
        Category c1 = new Category();
        c1.setId(1L);

        Category c2 = new Category();
        c2.setId(1L);

        Category c3 = new Category();
        c3.setId(2L);

        assertThat(c1).isEqualTo(c2);
        assertThat(c1).isNotEqualTo(c3);
    }

    @Test
    @DisplayName("Deve retornar hashCode baseado no ID")
    void shouldReturnConsistentHashCode() {
        Category c1 = new Category();
        c1.setId(10L);

        Category c2 = new Category();
        c2.setId(10L);

        assertThat(c1.hashCode()).isEqualTo(c2.hashCode());
    }

    @Test
    @DisplayName("Deve ter lista de produtos vazia por padrão")
    void shouldStartWithEmptyProductList() {
        Category category = new Category();
        assertThat(category.getProducts()).isEmpty();
    }

    @Test
    @DisplayName("Deve adicionar produto na lista da categoria")
    void shouldAddProductToCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Componentes");

        Product product = new Product();
        product.setId(100L);
        product.setName("Placa de vídeo");
        product.setCategory(category);

        category.setProducts(List.of(product));

        assertThat(category.getProducts()).contains(product);
        assertThat(product.getCategory()).isEqualTo(category);
    }

    @Test
    @DisplayName("Deve setar e recuperar valores corretamente")
    void shouldSetAndGetValuesCorrectly() {
        Date now = new Date();
        Category category = new Category();
        category.setId(3L);
        category.setName("Monitores");
        category.setCreatedAt(now);

        assertThat(category.getId()).isEqualTo(3L);
        assertThat(category.getName()).isEqualTo("Monitores");
        assertThat(category.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Deve gerar toString legível")
    void shouldGenerateToString() {
        Category category = new Category();
        category.setId(5L);
        category.setName("Teclados");

        String output = category.toString();

        assertThat(output).contains("Category");
        assertThat(output).contains("Teclados");
    }
}
