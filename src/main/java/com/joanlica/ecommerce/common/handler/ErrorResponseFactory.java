package com.joanlica.ecommerce.common.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ErrorResponseFactory {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public Map<String, Object> payload(String error,
                                       String message,
                                       String path,
                                       Map<String, Object> details) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("error", error);
        payload.put("message", message);
        payload.put("path", path);
        payload.put("timestamp", OffsetDateTime.now().toString());
        if (details != null && !details.isEmpty()) payload.put("details", details);
        return payload;
    }

    public ResponseEntity<Object> entity(HttpStatus status,
                                         String error,
                                         String message,
                                         String path,
                                         Map<String, Object> details) {
        return ResponseEntity.status(status).body(payload(error, message, path, details));
    }

    public void write(HttpServletResponse res,
                      int status,
                      String error,
                      String message,
                      String path,
                      Map<String, Object> details) throws IOException {
        if (res.isCommitted()) return;
        res.setStatus(status);
        res.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(res.getWriter(), payload(error, message, path, details));
    }
}
