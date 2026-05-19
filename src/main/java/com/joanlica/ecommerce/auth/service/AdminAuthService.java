package com.joanlica.ecommerce.auth.service;

import com.joanlica.ecommerce.auth.dto.admin.UpdateUserRolesDTO;
import com.joanlica.ecommerce.auth.dto.admin.UserRolesResponseDTO;

public interface AdminAuthService {

    UserRolesResponseDTO updateRoleList(String email, UpdateUserRolesDTO updateUserRolesDTO);
}