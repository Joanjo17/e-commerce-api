package com.joanlica.ecommerce.auth.repository;

import com.joanlica.ecommerce.auth.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, UUID> {
    boolean existsByEmail(String email);

    Optional<UserAuth> findByEmail(String email);
}