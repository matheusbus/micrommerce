package com.esw.authservice.dto;

public record ErrorResponse(
    int statusCode,
    String message
) {
}
