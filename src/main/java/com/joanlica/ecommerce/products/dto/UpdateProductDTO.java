package com.joanlica.ecommerce.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Set;

public record UpdateProductDTO(
        @Schema(
                description = "The name of the product",
                example = "Smartphone XYZ",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Name is required")
        @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
        String name,
        @Schema(
                description = "The description of the product",
                example = "A high-end smartphone with a powerful processor and a stunning display.",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Description is required")
        @Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters")
        String description,
        @Schema(
                description = "The price of the product",
                example = "499.99",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be a positive number")
        BigDecimal price,
        @Schema(
                description = "The stock quantity of the product",
                example = "100",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Positive(message = "Stock must be a positive integer")
        @NotNull(message = "Stock is required")
        Integer stock,
        @Schema(
                description = "The active status of the product",
                example = "true",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        @Nullable
        Boolean active,
        @Schema(
                description = "The URL of the product image",
                example = "https://example.com/images/product-xyz.jpg",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        @Nullable
        String image,
        @Schema(
                description = "The brand ID associated with the product",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Brand ID is required")
        @Positive(message = "Brand ID must be a positive number")
        Long brandId,
        @Schema(
                description = "The categories IDs associated with the product",
                example = "[1, 2, 3]",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        @Size(min = 1, message = "At least one category is required")
        Set<Long> categoriesId
) {
}