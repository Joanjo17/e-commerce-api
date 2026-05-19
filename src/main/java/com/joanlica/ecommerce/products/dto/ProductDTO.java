package com.joanlica.ecommerce.products.dto;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record ProductDTO(
         UUID id,
         String name,
         String description,
         BigDecimal price,
         Integer stock,
         Boolean active,
         String image,
         Double rating,
         String brandName,
         Set<String> categories
) {
}