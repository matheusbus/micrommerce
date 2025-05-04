package com.esw.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    String email,

    @NotBlank(message = "Password cannot be blank")
    String password
) {}
