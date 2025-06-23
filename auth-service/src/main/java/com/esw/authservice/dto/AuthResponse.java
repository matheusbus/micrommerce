package com.esw.authservice.dto;

public record AuthResponse(
    String message,
    UserDTO user,
    String token
) {}
