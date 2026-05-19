package com.joanlica.ecommerce.auth.dto.role;

import com.joanlica.ecommerce.auth.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record RoleResponseDTO(
        @Schema(description = "Unique role ID.", example = "1")
        Long id,

        @Schema(description = "Role name as stored in the database.", example = "ADMIN")
        String roleName
) {
    public static RoleResponseDTO from(Role role) {
        return new RoleResponseDTO(role.getId(), role.getRoleName());
    }
}
