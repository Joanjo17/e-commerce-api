package com.joanlica.ecommerce.auth.service.implementation;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.joanlica.ecommerce.auth.dto.user.AuthTokensDTO;
import com.joanlica.ecommerce.auth.dto.user.LoginUserRequestDTO;
import com.joanlica.ecommerce.auth.dto.user.RegisterUserRequestDTO;
import com.joanlica.ecommerce.auth.dto.user.UserAuthResponseDTO;
import com.joanlica.ecommerce.auth.model.UserAuth;
import com.joanlica.ecommerce.auth.repository.UserAuthRepository;
import com.joanlica.ecommerce.auth.service.RoleService;
import com.joanlica.ecommerce.auth.service.UserAuthService;
import com.joanlica.ecommerce.common.exception.UserAlreadyExistsException;
import com.joanlica.ecommerce.config.util.JwtUtils;
import com.joanlica.ecommerce.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RequiredArgsConstructor
@Service
@Transactional
public class UserAuthServiceImpl implements UserAuthService {

    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final RoleService roleService;
    private final UserProfileService userProfileService;

    @Override
    public AuthTokensDTO register(RegisterUserRequestDTO registerUserRequestDTO) {
        if (userAuthRepository.existsByEmail(registerUserRequestDTO.email())) {
            throw new UserAlreadyExistsException("Email is already in use");
        }

        var roles = Set.of(roleService.getEntityByName("USER"));

        var userAuth = new UserAuth();
        userAuth.setEmail(registerUserRequestDTO.email());
        userAuth.setPassword(passwordEncoder.encode(registerUserRequestDTO.password()));
        userAuth.setRolesList(roles);
        UserAuth savedUser = userAuthRepository.save(userAuth);

        userProfileService.createProfile(savedUser.getId(), registerUserRequestDTO.firstName(), "");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        savedUser.getEmail(),
                        registerUserRequestDTO.password()));

        String accessToken = jwtUtils.createAccessToken(authentication);
        String refreshToken = jwtUtils.createRefreshToken(authentication);

        return new AuthTokensDTO(
                accessToken,
                refreshToken,
                new UserAuthResponseDTO(savedUser.getId(), savedUser.getEmail()));
    }

    @Override
    public AuthTokensDTO login(LoginUserRequestDTO loginUserRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserRequestDTO.email(),
                        loginUserRequestDTO.password()));

        UserAuth user = userAuthRepository.findByEmail(loginUserRequestDTO.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken = jwtUtils.createAccessToken(authentication);
        String refreshToken = jwtUtils.createRefreshToken(authentication);

        return new AuthTokensDTO(
                accessToken,
                refreshToken,
                new UserAuthResponseDTO(user.getId(), loginUserRequestDTO.email()));
    }

    @Override
    public String refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadCredentialsException("Refresh token missing");
        }

        DecodedJWT decodedJWT = jwtUtils.validateToken(refreshToken);

        if (!jwtUtils.isRefreshToken(decodedJWT)) {
            throw new JWTVerificationException("Invalid refresh token");
        }

        String username = jwtUtils.extractUsername(decodedJWT);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities());

        return jwtUtils.createAccessToken(authentication);
    }
}
