package com.joanlica.ecommerce.brands.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Brand response payload.")
public record BrandDTO(
        @Schema(description = "Unique brand ID.", example = "1")
        Long id,

        @Schema(description = "Brand name.", example = "Apple")
        String name,

        @Schema(description = "Brand description.", example = "Technology company known for innovative consumer electronics.")
        String description,

        @Schema(description = "URL of the brand logo.", example = "https://example.com/logos/apple.png")
        String logoUrl
) {
}
