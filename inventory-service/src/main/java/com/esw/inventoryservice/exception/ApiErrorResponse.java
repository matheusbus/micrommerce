package com.esw.inventoryservice.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ApiErrorResponse {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final List<FieldError> fieldErrors;

    public ApiErrorResponse(int status, String error, List<FieldError> fieldErrors) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.fieldErrors = fieldErrors;
    }

    public static class FieldError {
        private final String field;
        private final String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}
