package com.joanlica.ecommerce.config.security;

import com.joanlica.ecommerce.common.handler.ErrorResponseFactory;
import com.joanlica.ecommerce.config.security.filters.JwtTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final ErrorResponseFactory errorFactory;

    @Value("${app.cookies.secure}")
    private boolean secure;

    @Value("${app.cookies.same-site}")
    private String sameSite;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationEntryPoint jsonAuthenticationEntryPoint,
                                           AccessDeniedHandler jsonAccessDeniedHandler,
                                           JwtTokenValidator jwtTokenValidator) throws Exception {

        return http
                // 1) Apply CORS configuration first
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfTokenRepository())
                        .sessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy())
                        .ignoringRequestMatchers(
                                "/api/v1/auth/register",
                                "/api/v1/auth/login"
                        )
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(reg -> reg
                        .requestMatchers("/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html").permitAll()
                        // actuator
                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/info"
                        ).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // auth and roles
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/auth/register",
                                "/api/v1/auth/login",
                                "/api/v1/auth/refresh",
                                "/api/v1/auth/logout").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/auth/csrf").permitAll()
                        .requestMatchers("/api/v1/roles/**").hasAnyRole("ADMIN")
                        // user profile
                        .requestMatchers("/api/v1/profile/**").authenticated()
                        // admin actions
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        // categories
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
                        .requestMatchers("/api/v1/categories/**").hasRole("ADMIN")
                        // brands
                        .requestMatchers(HttpMethod.GET, "/api/v1/brands/**").permitAll()
                        .requestMatchers("/api/v1/brands/**").hasRole("ADMIN")
                        // products
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                        .requestMatchers("/api/v1/products/**").hasRole("ADMIN")
                        .anyRequest().denyAll()
                )
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint(jsonAuthenticationEntryPoint)
                        .accessDeniedHandler(jsonAccessDeniedHandler)
                )
                .addFilterBefore(jwtTokenValidator, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CookieCsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository =
                CookieCsrfTokenRepository.withHttpOnlyFalse();

        repository.setCookieCustomizer(cookie -> cookie
                .path("/")
                .secure(secure)
                .sameSite(sameSite)
        );

        return repository;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Configure UserDetailsService and PasswordEncoder.
        DaoAuthenticationProvider p = new DaoAuthenticationProvider(userDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    @Bean
    public AuthenticationEntryPoint jsonAuthenticationEntryPoint() {
        return (req, res, ex) -> {
            res.setHeader("WWW-Authenticate", "Bearer error=\"invalid_token\"");
            errorFactory.write(res, 401, "unauthorized", "Authentication required",
                    req.getRequestURI(), Map.of());
        };
    }

    @Bean
    public AccessDeniedHandler jsonAccessDeniedHandler() {
        return (req, res, ex) ->
                errorFactory.write(res, 403, "forbidden", "Access denied",
                        req.getRequestURI(), Map.of());
    }
}
