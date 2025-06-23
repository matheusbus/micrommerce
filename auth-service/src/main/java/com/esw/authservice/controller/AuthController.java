package com.esw.authservice.controller;

import com.esw.authservice.dto.AuthRequest;
import com.esw.authservice.dto.AuthResponse;
import com.esw.authservice.dto.RegisterRequest;
import com.esw.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        return ResponseEntity.ok("Authenticated user: " + authentication.getName());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Jwt jwt = authService.validateToken(token);

        return ResponseEntity.ok(Map.of(
                "message", "Token is valid.",
                "subject", jwt.getSubject(),
                "scopes", jwt.getClaimAsString("scopes")
        ));
    }
}
