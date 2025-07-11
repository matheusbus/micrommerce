package com.esw.productservice.repository;

import com.esw.productservice.model.Category;
import com.esw.productservice.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Deve salvar categoria no banco com sucesso")
    void shouldSaveCategory() {
        Category category = new Category();
        category.setName("Utensílios");

        Category saved = categoryRepository.save(category);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Utensílios");
    }

    @Test
    @DisplayName("Deve buscar categoria por ID")
    void shouldFindCategoryById() {
        Category category = new Category();
        category.setName("Periféricos");
        Category saved = categoryRepository.save(category);

        Optional<Category> found = categoryRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Periféricos");
    }

    @Test
    @DisplayName("Deve buscar todas as categorias salvas")
    void shouldFindAllCategories() {
        categoryRepository.save(new Category(null, "Placas", null, null));
        categoryRepository.save(new Category(null, "Memórias", null, null));

        List<Category> all = categoryRepository.findAll();

        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve associar produtos à categoria e persistir relacionamento")
    void shouldPersistCategoryWithProducts() {
        Category category = new Category();
        category.setName("Monitores");

        Product p1 = new Product();
        p1.setName("Monitor LG");
        p1.setDescription("23'' Full HD");
        p1.setPrice(new BigDecimal("899.99"));
        p1.setActive(true);
        p1.setCategory(category);

        Product p2 = new Product();
        p2.setName("Monitor Samsung");
        p2.setDescription("24'' Curvo");
        p2.setPrice(new BigDecimal("999.99"));
        p2.setActive(true);
        p2.setCategory(category);

        category.setProducts(List.of(p1, p2));

        Category savedCategory = categoryRepository.save(category);

        assertThat(savedCategory.getId()).isNotNull();

        List<Product> products = productRepository.findByCategory(savedCategory).orElseThrow();
        assertThat(products).hasSize(2);
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar categoria inexistente")
    void shouldReturnEmptyWhenCategoryNotFound() {
        Optional<Category> result = categoryRepository.findById(999L);
        assertThat(result).isEmpty();
    }
}