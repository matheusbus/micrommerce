package com.esw.productservice.validation;


import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class NoOffensiveLanguageValidatorTest {

    private NoOffensiveLanguageValidator validator;

    @BeforeEach
    void setup() {
        validator = new NoOffensiveLanguageValidator();
        validator.initialize(null);
    }

    @Test
    @DisplayName("Deve validar string sem linguagem ofensiva como válida")
    void shouldReturnTrueForCleanText() {
        boolean valid = validator.isValid("This is a clean description", mock(ConstraintValidatorContext.class));
        assertThat(valid).isTrue();
    }

    @Test
    @DisplayName("Deve invalidar string contendo linguagem ofensiva")
    void shouldReturnFalseForOffensiveText() {
        boolean valid = validator.isValid("This description contains peidao", mock(ConstraintValidatorContext.class));
        assertThat(valid).isFalse();
    }

    @Test
    @DisplayName("Deve considerar null como válido")
    void shouldReturnTrueForNull() {
        boolean valid = validator.isValid(null, mock(ConstraintValidatorContext.class));
        assertThat(valid).isTrue();
    }
}