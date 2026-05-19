package com.joanlica.ecommerce.config;

import com.joanlica.ecommerce.auth.model.Role;
import com.joanlica.ecommerce.auth.model.UserAuth;
import com.joanlica.ecommerce.auth.repository.UserAuthRepository;
import com.joanlica.ecommerce.auth.service.RoleService;
import com.joanlica.ecommerce.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
// Para cuando estemos en dev o en prod:
@Profile({"dev", "prod"})
@RequiredArgsConstructor
@Slf4j
public class AdminUserInitializer implements CommandLineRunner {

    private final UserAuthRepository userAuthRepository;
    private final RoleService roleService;
    private final UserProfileService userProfileService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    @Transactional
    public void run(String... args) {

        if (userAuthRepository.findByEmail(adminUsername).isPresent()) {
            return;
        }
        log.info("Creating admin user");
        Role roleAdmin = roleService.findOrCreate("ADMIN");
        Role roleUser = roleService.findOrCreate("USER");

        UserAuth adminUser = new UserAuth();
        adminUser.setEmail(adminUsername);
        adminUser.setPassword(passwordEncoder.encode(adminPassword));
        adminUser.setRolesList(Set.of(roleAdmin, roleUser));

        UserAuth saved = userAuthRepository.save(adminUser);

        userProfileService.createProfile(saved.getId(), "Admin", "User");
        log.info("Admin user created");
    }
}