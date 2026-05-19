package com.joanlica.ecommerce.config.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtils {

    @Value("${app.security.jwt.secret-key}")
    private String secretKey;

    @Value("${app.security.jwt.generator}")
    private String userGenerator;

    @Value("${app.security.jwt.ttl-seconds}")
    private long ttlSeconds;

    @Value("${app.security.jwt.refresh-ttl-seconds}")
    private long refreshTtlSeconds;

    private String createToken(Authentication authentication, long expirationTimeSeconds, String tokenType) {
        Algorithm algorithm = Algorithm.HMAC256(this.secretKey);

        String username = authentication.getName();

        String[] authArray = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);

        Instant now = Instant.now();
        Date iat = Date.from(now);
        Date exp = Date.from(now.plusSeconds(expirationTimeSeconds));

        return JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withArrayClaim("authorities", authArray)
                .withClaim("type", tokenType)
                .withIssuedAt(iat)
                .withExpiresAt(exp)
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(iat)
                .sign(algorithm);
    }

    public String createAccessToken(Authentication authentication) {
        return createToken(authentication, this.ttlSeconds, "access");
    }

    public String createRefreshToken(Authentication authentication) {
        return createToken(authentication, this.refreshTtlSeconds, "refresh");
    }

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(userGenerator)
                    .build();
            return verifier.verify(token);
        } catch (IllegalArgumentException e) {
            throw new JWTVerificationException("Invalid token configuration", e);
        }
    }

    public boolean isAccessToken(DecodedJWT decodedJWT) {
        return "access".equals(decodedJWT.getClaim("type").asString());
    }

    public boolean isRefreshToken(DecodedJWT decodedJWT) {
        return "refresh".equals(decodedJWT.getClaim("type").asString());
    }

    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }
}
