package com.esw.productservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoOffensiveLanguageValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoOffensiveLanguage {
    String message() default "The content contains aggressive or inappropriate language.";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default{};
}
