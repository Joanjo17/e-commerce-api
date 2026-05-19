package com.joanlica.ecommerce.user.repository;

import com.joanlica.ecommerce.user.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserAuthId(UUID id);

    Optional<UserProfile> findByUserAuthEmail(String email);
}