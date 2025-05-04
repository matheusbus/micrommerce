package com.esw.authservice.controller;

import com.esw.authservice.dto.AuthRequest;
import com.esw.authservice.dto.AuthResponse;
import com.esw.authservice.dto.RegisterRequest;
import com.esw.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        String token = authService.authenticate(request);
        return ResponseEntity.ok(new AuthResponse("User logged in successfully", token));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        String token = authService.register(request);
        return ResponseEntity.ok(new AuthResponse("User registered successfully", token));
    }
}
