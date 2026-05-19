package com.joanlica.ecommerce.user.controller;

import com.joanlica.ecommerce.user.dto.EditUserProfileDTO;
import com.joanlica.ecommerce.user.dto.UserProfileDTO;
import com.joanlica.ecommerce.user.service.UserProfileService;
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

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/v1/profile")
@Tag(
        name = "User Profile",
        description = "Endpoints for managing user profiles, allowing users to view and edit their personal information."
)
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Operation(
            summary = "Get current user profile",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile returned successfully",
                            content = @Content(schema = @Schema(implementation = UserProfileDTO.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Authentication required", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<UserProfileDTO> getUserProfile(Principal principal) {
        log.info("Get user profile for user {}", principal.getName());
        UserProfileDTO user = userProfileService.getUserProfile(principal.getName());
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Update current user profile",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = EditUserProfileDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile updated successfully",
                            content = @Content(schema = @Schema(implementation = UserProfileDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Authentication required", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content)
            }
    )
    @PutMapping
    public ResponseEntity<UserProfileDTO> editUserProfile(Principal principal, @RequestBody @Valid EditUserProfileDTO userProfileDTO) {
        log.info("Edit user profile {}", userProfileDTO);
        UserProfileDTO updatedUser = userProfileService.editUserProfile(principal.getName(), userProfileDTO);
        return ResponseEntity.ok(updatedUser);
    }
}
