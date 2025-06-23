package com.esw.paymentservice.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Class<?> entityClass, String attribute, String value) {
        super(String.format("%s not found with %s = '%s'.",
                entityClass.getSimpleName(), attribute, value));
    }

    public NotFoundException(String message) {
        super(message);
    }
}