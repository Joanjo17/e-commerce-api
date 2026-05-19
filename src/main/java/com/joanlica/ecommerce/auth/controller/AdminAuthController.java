package com.joanlica.ecommerce.auth.controller;

import com.joanlica.ecommerce.auth.dto.admin.UpdateUserRolesDTO;
import com.joanlica.ecommerce.auth.dto.admin.UserRolesResponseDTO;
import com.joanlica.ecommerce.auth.service.AdminAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@Tag(
        name = "Admin - User Management",
        description = "Endpoints restricted to administrators."
)
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @Operation(
            summary = "Update user roles",
            description = "Allows an ADMIN user to update the roles associated with an existing user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Roles updated successfully",
                            content = @Content(schema = @Schema(implementation = UserRolesResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
            }
    )
    @PutMapping("/user/{email}/roles")
    public ResponseEntity<UserRolesResponseDTO> updateUserRole(
            @Parameter(description = "User email to update.", example = "email@example.com")
            @PathVariable String email,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New role list",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateUserRolesDTO.class))
            )
            @Valid @RequestBody UpdateUserRolesDTO updateUserRolesDTO
    ) {
        UserRolesResponseDTO updatedUser = adminAuthService.updateRoleList(email, updateUserRolesDTO);
        return ResponseEntity.ok(updatedUser);
    }
}