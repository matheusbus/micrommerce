package com.esw.productservice.validation;

import jakarta.validation.ConstraintValidator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// See: https://medium.com/@himani.prasad016/validations-in-spring-boot-e9948aa6286b
// Implementation reference to validation for abuse products and categories names and descriptions
public class NoOffensiveLanguageValidator implements ConstraintValidator<NoOffensiveLanguage, String> {

    private Pattern pattern;

    public void initialize(NoOffensiveLanguage constraintAnnotation) {
        try (InputStream is = getClass().getResourceAsStream("/badwords.txt")) {
            if (is != null) {
                List<String> badWords = new BufferedReader(new InputStreamReader(is))
                        .lines()
                        .map(String::trim)
                        .filter(s -> !s.isBlank())
                        .map(Pattern::quote)
                        .toList();

                String joinedBadWords = String.join("|", badWords);
                pattern = Pattern.compile("\\b(" + joinedBadWords + ")\\b", Pattern.CASE_INSENSITIVE);
            } else {
                pattern = Pattern.compile("");
            }
        } catch (Exception e) {
            pattern = Pattern.compile("");
        }
    }

    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        if (value == null) return true;
        Matcher matcher = pattern.matcher(value);
        return !matcher.find();
    }
}
