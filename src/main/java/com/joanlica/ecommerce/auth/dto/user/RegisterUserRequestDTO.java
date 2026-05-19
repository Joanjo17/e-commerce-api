package com.joanlica.ecommerce.auth.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserRequestDTO(
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
                pattern = "^(?=.*[A-Za-z])(?=.*\\d).+$",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 255, message = "Email must be between 6 and 255 characters")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
                message = "Password must contain at least one letter and one number")
        String password,

        @Schema(
                description = "User first name.",
                example = "Raul",
                minLength = 2,
                maxLength = 100,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
        @Pattern(regexp = "^\\p{L}+(?:[ '-]\\p{L}+)*$",
                message = "First name may only contain letters, spaces, apostrophes, and hyphens")
        String firstName
) {
}