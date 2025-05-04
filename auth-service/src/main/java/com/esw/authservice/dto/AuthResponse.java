package com.esw.authservice.dto;

public record AuthResponse(
    String message,
    String token
) {}
