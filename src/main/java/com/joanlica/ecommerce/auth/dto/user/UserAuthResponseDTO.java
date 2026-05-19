package com.joanlica.ecommerce.auth.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record UserAuthResponseDTO(
        @Schema(
                description = "Unique user ID in the database.",
                example = "123e4567-e89b-12d3-a456-426614174000"
        )
        UUID id,
        @Schema(
                description = "User email.",
                example = "email@example.com"
        )
        String email

) {
}
