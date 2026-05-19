package com.joanlica.ecommerce.brands.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload for creating a new brand.")
public record CreateBrandDTO(
        @Schema(
                description = "Brand name.",
                example = "Apple",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Brand name is required")
        @Size(min = 1, max = 100, message = "Brand name must be between 1 and 100 characters")
        String name,

        @Schema(
                description = "Brand description.",
                example = "Technology company known for innovative consumer electronics.",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        @Nullable
        @Size(max = 500, message = "Description must be at most 500 characters")
        String description,

        @Schema(
                description = "URL of the brand logo.",
                example = "https://example.com/logos/apple.png",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        @Nullable
        @Size(max = 255, message = "Logo URL must be at most 255 characters")
        String logoUrl
) {
}
