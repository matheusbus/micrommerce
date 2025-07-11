package com.esw.inventoryservice.repository;

import com.esw.inventoryservice.model.Inventory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    @DisplayName("findByProductId(): Deve retornar inventário existente")
    void shouldFindInventoryByProductId() {
        // Arrange
        Inventory inventory = new Inventory(1L, 10, 2);
        inventoryRepository.save(inventory);

        // Act
        Optional<Inventory> found = inventoryRepository.findByProductId(1L);

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getProductId()).isEqualTo(1L);
        assertThat(found.get().getAvailable()).isEqualTo(10);
        assertThat(found.get().getReserved()).isEqualTo(2);
    }

    @Test
    @DisplayName("findByProductId(): Deve retornar vazio quando inventário não existir")
    void shouldReturnEmptyWhenInventoryNotFound() {
        Optional<Inventory> found = inventoryRepository.findByProductId(99L);

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("save(): Deve persistir inventário corretamente")
    void shouldSaveInventoryCorrectly() {
        Inventory inventory = new Inventory(2L, 15, 5);
        Inventory saved = inventoryRepository.save(inventory);

        assertThat(saved.getProductId()).isEqualTo(2L);
        assertThat(saved.getAvailable()).isEqualTo(15);
        assertThat(saved.getReserved()).isEqualTo(5);
    }
}