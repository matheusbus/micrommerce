package com.esw.inventoryservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Inventory {

    @Id
    @Column(name = "inventory_id", unique = true, nullable = false)
    private Long productId;

    @Column(name = "available", nullable = false)
    private Integer available;

    @Column(name = "reserved", nullable = false)
    private Integer reserved;

    public Inventory() {
    }

    public Inventory(Long productId, Integer available, Integer reserved) {
        this.productId = productId;
        this.available = available;
        this.reserved = reserved;
    }

    public boolean reserve(int quantity) {
        if (available >= quantity) {
            available -= quantity;
            reserved += quantity;
            return true;
        }
        return false;
    }

    public void release(int quantity) {
        reserved = Math.max(0, reserved - quantity);
        available += quantity;
    }

    public void reset() {
        reserved = 0;
        available = 0;
    }

    public void confirmReservation(int quantity) {
        reserved = Math.max(0, reserved - quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getReserved() {
        return reserved;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Inventory inventory = (Inventory) o;
        return Objects.equals(productId, inventory.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productId);
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "productId='" + productId + '\'' +
                ", available=" + available +
                ", reserved=" + reserved +
                '}';
    }
}