package com.joanlica.ecommerce.auth.service.implementation;

import com.joanlica.ecommerce.auth.dto.admin.UpdateUserRolesDTO;
import com.joanlica.ecommerce.auth.dto.admin.UserRolesResponseDTO;
import com.joanlica.ecommerce.auth.model.Role;
import com.joanlica.ecommerce.auth.repository.UserAuthRepository;
import com.joanlica.ecommerce.auth.service.AdminAuthService;
import com.joanlica.ecommerce.auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAuthServiceImpl implements AdminAuthService {

    private final UserAuthRepository userAuthRepository;
    private final RoleService roleService;

    @Override
    public UserRolesResponseDTO updateRoleList(String email, UpdateUserRolesDTO updateUserRolesDTO) {
        var userAuth = userAuthRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var newRoles = updateUserRolesDTO.rolesList().stream()
                .map(roleService::getEntityByName)
                .collect(Collectors.toSet());

        userAuth.setRolesList(newRoles);
        userAuthRepository.save(userAuth);

        return new UserRolesResponseDTO(userAuth.getId(),
                userAuth.getEmail(),
                newRoles.stream()
                        .map(Role::getRoleName)
                        .collect(Collectors.toSet()));
    }
}