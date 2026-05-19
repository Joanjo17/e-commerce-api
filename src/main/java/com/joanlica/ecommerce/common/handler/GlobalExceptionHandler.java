package com.joanlica.ecommerce.common.handler;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.joanlica.ecommerce.common.exception.BrandNotFoundException;
import com.joanlica.ecommerce.common.exception.CategoryNotFoundException;
import com.joanlica.ecommerce.common.exception.ProductNotFoundException;
import com.joanlica.ecommerce.common.exception.ProfileNotFoundException;
import com.joanlica.ecommerce.common.exception.RoleNotFoundException;
import com.joanlica.ecommerce.common.exception.UserAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorResponseFactory errorFactory;

    /*
     * 401 - Authentication failures (invalid credentials or unknown user)
     */
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<Object> handleAuthFailures(RuntimeException ex, HttpServletRequest req) {
        return errorFactory.entity(HttpStatus.UNAUTHORIZED, "unauthorized",
                "Invalid username or password", req.getRequestURI(), Map.of());
    }

    /*
     * 401 - Disabled user
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Object> handleDisabled(DisabledException ex, HttpServletRequest req) {
        return errorFactory.entity(HttpStatus.UNAUTHORIZED, "disabled",
                "User account is disabled", req.getRequestURI(), Map.of());
    }

    /*
     * 423 - Locked account
     */
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<Object> handleLocked(LockedException ex, HttpServletRequest req) {
        return errorFactory.entity(HttpStatus.LOCKED, "locked",
                "Account locked", req.getRequestURI(), Map.of());
    }

    /*
     * 401 - Expired credentials
     */
    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<Object> handleCredsExpired(CredentialsExpiredException ex, HttpServletRequest req) {
        return errorFactory.entity(HttpStatus.UNAUTHORIZED, "credentials_expired",
                "Credentials expired", req.getRequestURI(), Map.of());
    }

    /*
     * 403 - URL access denied
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex,
                                                     HttpServletRequest req) {
        return errorFactory.entity(HttpStatus.FORBIDDEN, "forbidden",
                "Access denied", req.getRequestURI(), Map.of());
    }

    /*
     * 403 - Authorization denied (e.g. @PreAuthorize)
     */
    @ExceptionHandler(org.springframework.security.authorization.AuthorizationDeniedException.class)
    public ResponseEntity<Object> handleAuthorizationDeniedException(
            org.springframework.security.authorization.AuthorizationDeniedException ex,
            HttpServletRequest req) {
        return errorFactory.entity(HttpStatus.FORBIDDEN, "forbidden",
                "Access denied", req.getRequestURI(), Map.of());
    }

    /*
     * 400 - Malformed JSON in @RequestBody
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  @Nullable HttpHeaders headers,
                                                                  @Nullable HttpStatusCode status,
                                                                  @Nullable WebRequest request) {
        String path = (request instanceof ServletWebRequest sw) ? sw.getRequest().getRequestURI() : "";
        log.debug("Malformed JSON at {}: {}", path, ex.getMessage());
        return errorFactory.entity(HttpStatus.BAD_REQUEST, "bad_request",
                "Malformed JSON request", path, Map.of());
    }

    /*
     * 400 - Bean validation failed for @RequestBody
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @Nullable HttpHeaders headers,
                                                                  @Nullable HttpStatusCode status,
                                                                  @Nullable WebRequest request) {
        Map<String, String> fields = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> fields.put(fe.getField(), fe.getDefaultMessage()));
        String path = (request instanceof ServletWebRequest sw) ? sw.getRequest().getRequestURI() : "";
        log.debug("Validation failed at {}: {}", path, fields);
        return errorFactory.entity(HttpStatus.BAD_REQUEST, "bad_request",
                "Validation failed", path, Map.of("fields", fields));
    }

    /*
     * 400 - Bean validation failed for @PathVariable/@RequestParam
     */
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(jakarta.validation.ConstraintViolationException ex,
                                                           HttpServletRequest req) {
        Map<String, String> fields = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(v ->
                fields.put(v.getPropertyPath().toString(), v.getMessage()));
        return errorFactory.entity(HttpStatus.BAD_REQUEST, "bad_request",
                "Validation failed", req.getRequestURI(), Map.of("fields", fields));
    }

    /*
     * 409 - Integrity violation (unique, FK, etc.)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        log.debug("DataIntegrityViolation at {}: {}", req.getRequestURI(), ex.getMessage());
        return errorFactory.entity(HttpStatus.CONFLICT, "conflict",
                "Data integrity violation", req.getRequestURI(), Map.of());
    }

    /*
     * 409 - User already exists (register)
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExists(UserAlreadyExistsException ex, HttpServletRequest req) {
        return errorFactory.entity(HttpStatus.CONFLICT, "user_already_exists",
                ex.getMessage(), req.getRequestURI(), Map.of());
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Object> handleRoleNotFoundException(RoleNotFoundException ex, HttpServletRequest req) {
        return errorFactory.entity(HttpStatus.NOT_FOUND, "role_not_found",
                ex.getMessage(), req.getRequestURI(), Map.of());
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<Object> handleProfileNotFoundException(ProfileNotFoundException ex, HttpServletRequest req) {
        return errorFactory.entity(HttpStatus.NOT_FOUND, "profile_not_found",
                ex.getMessage(), req.getRequestURI(), Map.of());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> handleCategoryNotFoundException(CategoryNotFoundException ex, HttpServletRequest req) {
        return errorFactory.entity(HttpStatus.NOT_FOUND, "category_not_found",
                ex.getMessage(), req.getRequestURI(), Map.of());
    }

    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<Object> handleBrandNotFoundException(BrandNotFoundException ex, HttpServletRequest req) {
        return errorFactory.entity(HttpStatus.NOT_FOUND, "brand_not_found",
                ex.getMessage(), req.getRequestURI(), Map.of());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex, HttpServletRequest req) {
        return errorFactory.entity(HttpStatus.NOT_FOUND, "product_not_found",
                ex.getMessage(), req.getRequestURI(), Map.of());
    }

    @ExceptionHandler({JWTDecodeException.class, JWTVerificationException.class})
    public ResponseEntity<Object> handleJWTException(RuntimeException ex, HttpServletRequest req) {
        return errorFactory.entity(HttpStatus.UNAUTHORIZED, "invalid_token",
                "Invalid JWT token", req.getRequestURI(), Map.of());
    }

    /*
     * 500 - Fallback for unexpected runtime errors
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleAll(RuntimeException ex, HttpServletRequest req) {
        log.error("Unhandled error at {}:", req.getRequestURI(), ex);
        return errorFactory.entity(HttpStatus.INTERNAL_SERVER_ERROR, "internal_error",
                "Unexpected error", req.getRequestURI(), Map.of());
    }
}
