package com.joanlica.ecommerce.auth.service;

import com.joanlica.ecommerce.auth.dto.user.AuthTokensDTO;
import com.joanlica.ecommerce.auth.dto.user.LoginUserRequestDTO;
import com.joanlica.ecommerce.auth.dto.user.RegisterUserRequestDTO;

public interface UserAuthService {
    // Registers a new user
    AuthTokensDTO register(RegisterUserRequestDTO registerUserRequestDTO);

    // Logs in a user
    AuthTokensDTO login(LoginUserRequestDTO loginUserRequestDTO);

    // Refreshes access token
    String refresh(String refreshToken);
}
