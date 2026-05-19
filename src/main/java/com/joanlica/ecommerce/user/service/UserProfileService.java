package com.joanlica.ecommerce.user.service;

import com.joanlica.ecommerce.user.dto.EditUserProfileDTO;
import com.joanlica.ecommerce.user.dto.UserProfileDTO;

import java.util.UUID;

public interface UserProfileService {

    UserProfileDTO getUserProfile(String email);

    UserProfileDTO editUserProfile(String email, EditUserProfileDTO userProfileDTO);

    /**
     * Creates a new user profile associated with the given UserAuth ID.
     * Used by the registration flow.
     */
    UserProfileDTO createProfile(UUID userAuthId, String firstName, String lastName);
}