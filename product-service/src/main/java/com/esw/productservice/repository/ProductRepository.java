package com.esw.productservice.repository;

import com.esw.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<List<Product>> findByCategory_Id(Long categoryId);
    Optional<Product> findByName(String name);
    Optional<Product> findByPublicId(UUID publicId);
}
