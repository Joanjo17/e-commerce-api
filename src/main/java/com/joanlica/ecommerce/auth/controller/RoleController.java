package com.joanlica.ecommerce.auth.controller;

import com.joanlica.ecommerce.auth.dto.role.CreateRoleRequestDTO;
import com.joanlica.ecommerce.auth.dto.role.RoleResponseDTO;
import com.joanlica.ecommerce.auth.dto.role.UpdateRoleRequestDTO;
import com.joanlica.ecommerce.auth.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Internal role management endpoints. All endpoints require the ADMIN role.")
@Slf4j
public class RoleController {

    private final RoleService roleService;

    @Operation(
            summary = "List all roles (ADMIN)",
            description = "Returns a list of all roles available in the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Role list",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoleResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Access denied (requires ADMIN)", content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
        log.info("Listing all roles");
        List<RoleResponseDTO> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    @Operation(
            summary = "Get role by ID (ADMIN)",
            description = "Returns a role by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Role found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoleResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Role not found", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Access denied (requires ADMIN)", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Long id) {
        log.info("Getting role by id {}", id);
        RoleResponseDTO role = roleService.findById(id);
        return ResponseEntity.ok(role);
    }

    @Operation(
            summary = "Get role by name (ADMIN)",
            description = "Returns a role by its name (e.g. 'USER').",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Role found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoleResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Role not found", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Access denied (requires ADMIN)", content = @Content)
            }
    )
    @GetMapping("/name/{roleName}")
    public ResponseEntity<RoleResponseDTO> getRoleByName(@PathVariable String roleName) {
        log.info("Getting role by name {}", roleName);
        RoleResponseDTO role = roleService.findByName(roleName);
        return ResponseEntity.ok(role);
    }

    @Operation(
            summary = "Create role (ADMIN)",
            description = "Creates a new role in the database.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Role payload",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateRoleRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Role created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoleResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Role name already exists", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Access denied (requires ADMIN)", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(@Valid @RequestBody CreateRoleRequestDTO role) {
        log.info("Creating role: {}", role);
        RoleResponseDTO newRole = roleService.save(role);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newRole.id())
                .toUri();
        return ResponseEntity.created(location).body(newRole);
    }

    @Operation(
            summary = "Update role (ADMIN)",
            description = "Updates an existing role name.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Updated role payload",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateRoleRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Role updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoleResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Role not found", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Role name already exists", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Access denied (requires ADMIN)", content = @Content)
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> updateRole(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateRoleRequestDTO role) {
        log.info("Updating role id {}: {}", id, role);
        RoleResponseDTO newRole = roleService.update(id, role);
        return ResponseEntity.ok(newRole);
    }
}