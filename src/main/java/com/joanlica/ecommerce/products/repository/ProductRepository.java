package com.joanlica.ecommerce.products.repository;

import com.joanlica.ecommerce.products.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @EntityGraph(attributePaths = { "brand", "categories" })
    Optional<Product> findProductsById(UUID id);

}