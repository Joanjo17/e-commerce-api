package com.joanlica.ecommerce.auth.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginUserRequestDTO(
        @Schema(
                description = "User email.",
                example = "email@example.com",
                minLength = 6,
                maxLength = 255,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Email is required")
        @Size(min = 6, max = 255, message = "Email must be between 6 and 255 characters")
        @Email(message = "Email must be valid")
        String email,

        @Schema(
                description = "User password.",
                example = "PasswOrd!123",
                minLength = 6,
                maxLength = 255,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 255, message = "Email must be between 6 and 255 characters")
        String password
) {
}