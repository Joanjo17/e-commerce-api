package com.joanlica.ecommerce.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EditUserProfileDTO(
        @Schema(
                description = "User first name.",
                example = "Juan Jose",
                minLength = 2,
                maxLength = 100,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
        @Pattern(regexp = "^\\p{L}+(?:[ '-]\\p{L}+)*$",
                message = "First name may only contain letters, spaces, apostrophes, and hyphens")
        String firstName,
        @Schema(
                description = "User last name.",
                example = "Perez",
                minLength = 2,
                maxLength = 100,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
        @Pattern(regexp = "^\\p{L}+(?:[ '-]\\p{L}+)*$",
                message = "Last name may only contain letters, spaces, apostrophes, and hyphens")
        String lastName
) {
}
