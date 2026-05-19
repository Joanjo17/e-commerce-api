package com.joanlica.ecommerce.user.mapper;

import com.joanlica.ecommerce.user.dto.UserProfileDTO;
import com.joanlica.ecommerce.user.model.UserProfile;

public final class UserProfileMapper {

    private UserProfileMapper() {
    }

    public static UserProfileDTO toDTO(UserProfile userProfile) {
        return new UserProfileDTO(
                userProfile.getFirstName(),
                userProfile.getLastName()
        );
    }

}