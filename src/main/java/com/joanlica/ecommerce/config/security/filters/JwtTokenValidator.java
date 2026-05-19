package com.joanlica.ecommerce.config.security.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.joanlica.ecommerce.config.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenValidator extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final AuthenticationEntryPoint authEntryPoint;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // If authentication is already present, do not re-validate.
        var existing = SecurityContextHolder.getContext().getAuthentication();
        if (existing == null || existing instanceof AnonymousAuthenticationToken) {
            // Resolve token from Authorization header or cookie.
            String jwtToken = resolveToken(request);

            if (jwtToken != null && !jwtToken.isBlank()) {
                try {
                    DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);

                    if (!jwtUtils.isAccessToken(decodedJWT)) {
                        throw new JWTVerificationException("Invalid token");
                    }

                    String username = jwtUtils.extractUsername(decodedJWT);
                    List<String> authoritiesClaim = jwtUtils
                            .getSpecificClaim(decodedJWT, "authorities")
                            .asList(String.class);

                    Collection<? extends GrantedAuthority> authoritiesList =
                            (authoritiesClaim == null ? List.<String>of() : authoritiesClaim).stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .toList();

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, authoritiesList);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set authentication into the SecurityContext.
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authentication);
                    SecurityContextHolder.setContext(context);

                } catch (JWTVerificationException ex) {
                    SecurityContextHolder.clearContext();
                    authEntryPoint.commence(
                            request, response,
                            new InsufficientAuthenticationException("Invalid token", ex)
                    );
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null) {
            String trimmed = header.trim();

            if (trimmed.length() >= 7 && trimmed.regionMatches(true, 0, "Bearer ", 0, 7)) {
                return trimmed.substring(7).trim();
            }
        }

        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("access_token"))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }
}
