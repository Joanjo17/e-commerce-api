package com.joanlica.ecommerce.auth.service;

import com.joanlica.ecommerce.auth.dto.role.CreateRoleRequestDTO;
import com.joanlica.ecommerce.auth.dto.role.RoleResponseDTO;
import com.joanlica.ecommerce.auth.dto.role.UpdateRoleRequestDTO;
import com.joanlica.ecommerce.auth.model.Role;

import java.util.List;

public interface RoleService {
    List<RoleResponseDTO> findAll();

    RoleResponseDTO findById(Long id);

    RoleResponseDTO findByName(String name);

    RoleResponseDTO save(CreateRoleRequestDTO newRole);

    void deleteById(Long id);

    RoleResponseDTO update(Long id, UpdateRoleRequestDTO roleUpdate);

    /**
     * Returns the Role entity by name.
     * Used internally by other services (e.g. UserAuthService) to resolve role references.
     */
    Role getEntityByName(String name);

    /**
     * Finds a role by name or creates it if it doesn't exist.
     * Used by bootstrap initializers.
     */
    Role findOrCreate(String name);
}