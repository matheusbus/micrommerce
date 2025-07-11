package com.esw.inventoryservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory(1L, 10, 0);
    }

    @Test
    @DisplayName("reserve(): Deve reservar quantidade se disponível for suficiente")
    void shouldReserveSuccessfullyWhenEnoughAvailable() {
        boolean result = inventory.reserve(5);
        assertThat(result).isTrue();
        assertThat(inventory.getAvailable()).isEqualTo(5);
        assertThat(inventory.getReserved()).isEqualTo(5);
    }

    @Test
    @DisplayName("reserve(): Não deve reservar se não houver estoque disponível")
    void shouldNotReserveWhenInsufficientAvailable() {
        boolean result = inventory.reserve(15);
        assertThat(result).isFalse();
        assertThat(inventory.getAvailable()).isEqualTo(10);
        assertThat(inventory.getReserved()).isEqualTo(0);
    }

    @Test
    @DisplayName("release(): Deve liberar corretamente quantidade reservada")
    void shouldReleaseReservedQuantity() {
        inventory.reserve(5);
        inventory.release(3);
        assertThat(inventory.getAvailable()).isEqualTo(8);
        assertThat(inventory.getReserved()).isEqualTo(2);
    }

    @Test
    @DisplayName("release(): Deve lidar com liberação maior do que reservado")
    void shouldHandleOverReleaseGracefully() {
        inventory.reserve(4);
        inventory.release(10); // maior que o reservado
        assertThat(inventory.getAvailable()).isEqualTo(10);
        assertThat(inventory.getReserved()).isEqualTo(0);
    }

    @Test
    @DisplayName("reset(): Deve zerar estoque disponível e reservado")
    void shouldResetInventory() {
        inventory.reserve(5);
        inventory.reset();
        assertThat(inventory.getAvailable()).isZero();
        assertThat(inventory.getReserved()).isZero();
    }

    @Test
    @DisplayName("confirmReservation(): Deve confirmar e reduzir valor reservado")
    void shouldConfirmReservation() {
        inventory.reserve(6);
        inventory.confirmReservation(4);
        assertThat(inventory.getReserved()).isEqualTo(2);
    }

    @Test
    @DisplayName("confirmReservation(): Deve lidar com confirmação maior que o reservado")
    void shouldHandleOverConfirmReservation() {
        inventory.reserve(3);
        inventory.confirmReservation(10);
        assertThat(inventory.getReserved()).isZero();
    }

    @Test
    @DisplayName("equals() e hashCode() devem funcionar com base no productId")
    void shouldCompareInventoriesByProductId() {
        Inventory inv1 = new Inventory(1L, 5, 2);
        Inventory inv2 = new Inventory(1L, 10, 0);
        Inventory inv3 = new Inventory(2L, 5, 2);

        assertThat(inv1).isEqualTo(inv2);
        assertThat(inv1).hasSameHashCodeAs(inv2);
        assertThat(inv1).isNotEqualTo(inv3);
    }
}