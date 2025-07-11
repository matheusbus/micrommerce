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
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Deve salvar e buscar produto por nome")
    void shouldFindByName() {
        Category category = new Category();
        category.setName("Periféricos");
        categoryRepository.save(category);

        Product product = new Product();
        product.setName("Teclado");
        product.setDescription("Teclado mecânico");
        product.setPrice(new BigDecimal("100.00"));
        product.setCategory(category);
        product.setActive(true);
        productRepository.save(product);

        Optional<Product> found = productRepository.findByName("Teclado");

        assertThat(found).isPresent();
        assertThat(found.get().getDescription()).isEqualTo("Teclado mecânico");
    }

    @Test
    @DisplayName("Deve buscar produtos por categoria")
    void shouldFindByCategoryId() {
        Category category = new Category();
        category.setName("Hardware");
        categoryRepository.save(category);

        Product p1 = new Product();
        p1.setName("Placa mãe");
        p1.setDescription("B450M");
        p1.setPrice(new BigDecimal("450.00"));
        p1.setCategory(category);
        p1.setActive(true);

        productRepository.save(p1);

        Optional<List<Product>> found = productRepository.findByCategory(category);
        assertThat(found).isPresent();
        assertThat(found.get()).hasSize(1);
    }

    @Test
    @DisplayName("Deve buscar produto por publicId")
    void shouldFindByPublicId() {
        UUID uuid = UUID.randomUUID();
        Category category = new Category();
        category.setName("Monitores");
        categoryRepository.save(category);

        Product product = new Product();
        product.setName("Monitor LG");
        product.setDescription("Full HD");
        product.setPrice(new BigDecimal("799.99"));
        product.setPublicId(uuid);
        product.setCategory(category);
        product.setActive(true);
        productRepository.save(product);

        Optional<Product> found = productRepository.findByPublicId(uuid);
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Monitor LG");
    }
}