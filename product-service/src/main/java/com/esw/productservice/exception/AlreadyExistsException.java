package com.esw.productservice.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(Class<?> entityClass, String attribute, String value) {
        super(String.format("%s already exists with %s = '%s'.",
                entityClass.getSimpleName(), attribute, value));
    }

    public AlreadyExistsException(String message) {
        super(message);
    }
}
