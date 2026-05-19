package com.joanlica.ecommerce.auth.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UpdateUserRolesDTO(
        @Schema(
                description = "List of roles assigned to the user (stored in the database).",
                example = "[\"ADMIN\",\"USER\"]"
        )
        @NotNull(message = "Role list must not be null")
        Set<String> rolesList
) {
}
