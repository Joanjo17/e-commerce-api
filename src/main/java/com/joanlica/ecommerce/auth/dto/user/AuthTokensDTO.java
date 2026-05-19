package com.joanlica.ecommerce.auth.dto.user;

public record AuthTokensDTO(
        String accessToken,
        String refreshToken,
        UserAuthResponseDTO user
) {}