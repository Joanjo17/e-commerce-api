package com.joanlica.ecommerce.auth.controller;

import com.joanlica.ecommerce.auth.dto.user.AuthTokensDTO;
import com.joanlica.ecommerce.auth.dto.user.LoginUserRequestDTO;
import com.joanlica.ecommerce.auth.dto.user.RegisterUserRequestDTO;
import com.joanlica.ecommerce.auth.dto.user.UserAuthResponseDTO;
import com.joanlica.ecommerce.auth.service.UserAuthService;
import com.joanlica.ecommerce.config.util.AuthCookieFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(
        name = "Authentication",
        description = "Endpoints for register, login, refresh token, and logout using HttpOnly cookies."
)
@Slf4j
public class UserAuthController {

    private final UserAuthService userAuthService;
    private final AuthCookieFactory authCookieFactory;

    @Operation(
            summary = "Register user",
            description = """
                    Registers a new user and its associated profile.

                    If registration succeeds, the API returns the public user data and sets two HttpOnly cookies:
                    - access_token: access JWT
                    - refresh_token: refresh JWT

                    Tokens are not returned in the response body.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Registration payload",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterUserRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User registered successfully. Cookies access_token and refresh_token set.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserAuthResponseDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "409", description = "Email already in use", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
            }
    )
    @PostMapping("/register")
    public ResponseEntity<UserAuthResponseDTO> register(@Valid @RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
        log.info("Register user: {}", registerUserRequestDTO.email());
        AuthTokensDTO auth = userAuthService.register(registerUserRequestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(auth.user().id())
                .toUri();

        return ResponseEntity
                .created(location)
                .header(HttpHeaders.SET_COOKIE, authCookieFactory.createAccessCookie(auth.accessToken()).toString())
                .header(HttpHeaders.SET_COOKIE, authCookieFactory.createRefreshCookie(auth.refreshToken()).toString())
                .body(auth.user());
    }

    @Operation(
            summary = "Login",
            description = """
                    Authenticates an existing user.

                    If credentials are valid, the API returns the public user data and sets two HttpOnly cookies:
                    - access_token: access JWT
                    - refresh_token: refresh JWT

                    Tokens are not returned in the response body.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Login payload",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginUserRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User logged in successfully. Cookies access_token and refresh_token set.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserAuthResponseDTO.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
            }
    )
    @PostMapping("/login")
    public ResponseEntity<UserAuthResponseDTO> login(@Valid @RequestBody LoginUserRequestDTO loginUserRequestDTO) {
        log.info("Login user: {}", loginUserRequestDTO.email());
        AuthTokensDTO auth = userAuthService.login(loginUserRequestDTO);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, authCookieFactory.createAccessCookie(auth.accessToken()).toString())
                .header(HttpHeaders.SET_COOKIE, authCookieFactory.createRefreshCookie(auth.refreshToken()).toString())
                .body(auth.user());
    }

    @Operation(
            summary = "Refresh access token",
            description = """
                    Refreshes the access token using the HttpOnly refresh_token cookie.

                    If the refresh token is valid, the API sets a new access_token cookie.
                    Tokens are not returned in the response body.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Access token refreshed successfully"),
                    @ApiResponse(responseCode = "401", description = "Refresh token missing, expired, or invalid", content = @Content)
            }
    )
    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        String newAccessToken = userAuthService.refresh(refreshToken);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, authCookieFactory.createAccessCookie(newAccessToken).toString())
                .build();
    }

    @Operation(
            summary = "Get CSRF token",
            description = """
                    Returns the CSRF token required to perform protected requests when using HttpOnly cookies.

                    Send this value in the X-XSRF-TOKEN header for protected operations.
                    """,
            security = {},
            responses = @ApiResponse(responseCode = "200", description = "CSRF token returned successfully")
    )
    @GetMapping("/csrf")
    public ResponseEntity<CsrfToken> csrf(CsrfToken csrfToken) {
        return ResponseEntity.ok(csrfToken);
    }

    @Operation(
            summary = "Logout",
            description = """
                    Deletes the access_token and refresh_token cookies from the browser.

                    This endpoint does not invalidate tokens server-side unless you implement refresh token persistence
                    or a token blacklist.
                    """,
            responses = @ApiResponse(responseCode = "200", description = "Logged out successfully. Cookies deleted.")
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, authCookieFactory.deleteAccessCookie().toString())
                .header(HttpHeaders.SET_COOKIE, authCookieFactory.deleteRefreshCookie().toString())
                .build();
    }
}