package com.joanlica.ecommerce.user.service.implementation;

import com.joanlica.ecommerce.auth.model.UserAuth;
import com.joanlica.ecommerce.common.exception.ProfileNotFoundException;
import com.joanlica.ecommerce.user.dto.EditUserProfileDTO;
import com.joanlica.ecommerce.user.dto.UserProfileDTO;
import com.joanlica.ecommerce.user.mapper.UserProfileMapper;
import com.joanlica.ecommerce.user.model.UserProfile;
import com.joanlica.ecommerce.user.repository.UserProfileRepository;
import com.joanlica.ecommerce.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    private UserProfile findUserProfileByEmail(String email) {
        return userProfileRepository.findByUserAuthEmail(email)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found for email: " + email));
    }

    @Override
    public UserProfileDTO getUserProfile(String email) {
        return UserProfileMapper.toDTO(this.findUserProfileByEmail(email));
    }

    @Override
    @Transactional
    public UserProfileDTO editUserProfile(String email, EditUserProfileDTO userProfileDTO) {
        var userProfile = this.findUserProfileByEmail(email);
        userProfile.setFirstName(userProfileDTO.firstName());
        userProfile.setLastName(userProfileDTO.lastName());
        return UserProfileMapper.toDTO(userProfileRepository.save(userProfile));
    }

    @Override
    @Transactional
    public UserProfileDTO createProfile(UUID userAuthId, String firstName, String lastName) {
        var userAuth = new UserAuth();
        userAuth.setId(userAuthId);

        var userProfile = new UserProfile();
        userProfile.setUserAuth(userAuth);
        userProfile.setFirstName(firstName);
        userProfile.setLastName(lastName);
        return UserProfileMapper.toDTO(userProfileRepository.save(userProfile));
    }
}