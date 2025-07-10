package com.esw.authservice.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private static Validator validator;

    @BeforeAll
    public static void init(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testUserCreationAndGetters() {
        Date now = new Date();
        User user = new User(
                1L,
                "matheusb",
                "matheus@email.com",
                "123456",
                "ADMIN",
                UserType.SELLER,
                true,
                now
        );

        assertEquals(1L, user.getId());
        assertEquals("matheusb", user.getUsername());
        assertEquals("matheus@email.com", user.getEmail());
        assertEquals("123456", user.getPassword());
        assertEquals("ADMIN", user.getRole());
        assertEquals(UserType.SELLER, user.getType());
        assertTrue(user.isActive());
        assertEquals(now, user.getCreatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(1L);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testValidation_validUser_noViolations() {
        User user = new User();
        user.setUsername("matheusb");
        user.setEmail("matheus@email.com");
        user.setPassword("123456");
        user.setRole("USER");
        user.setActive(true);
        user.setType(UserType.BUYER);
        user.setCreatedAt(new Date());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidation_invalidUser_violationsPresent() {
        User user = new User();
        user.setUsername("ab");
        user.setEmail("email");
        user.setPassword("");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(3, violations.size());
    }
}
