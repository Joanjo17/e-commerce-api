package com.joanlica.ecommerce.auth.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateRoleRequestDTO(
        @Schema(
                description = "New unique role name stored in the database (e.g. ADMIN, USER).",
                example = "CUSTOMER",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Role name must not be blank")
        @Pattern(regexp = "^[A-Z_]+$", message = "Role name may only contain uppercase letters and underscores")
        String roleName
) {
}
