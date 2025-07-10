package com.esw.authservice.service;

import com.esw.authservice.dto.AuthRequest;
import com.esw.authservice.dto.AuthResponse;
import com.esw.authservice.dto.RegisterRequest;
import com.esw.authservice.dto.UserDTO;
import com.esw.authservice.exception.UserAlreadyExistsException;
import com.esw.authservice.exception.UserNotFoundException;
import com.esw.authservice.model.User;
import com.esw.authservice.model.UserType;
import com.esw.authservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve autenticar usuário com credenciais válidas")
    void shouldAuthenticateValidUser() {
        AuthRequest request = new AuthRequest("user@email.com", "123456");
        User user = getMockUser();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtService.generateToken(authentication)).thenReturn("mock-token");

        AuthResponse response = authService.authenticate(request);

        assertEquals("User logged in successfully", response.message());
        assertEquals("mock-token", response.token());
        assertEquals(user.getEmail(), response.user().getEmail());
    }

    @Test
    @DisplayName("Deve lançar exceção se usuário não encontrado ao autenticar")
    void shouldThrowWhenUserNotFoundOnAuthenticate() {
        AuthRequest request = new AuthRequest("notfound@email.com", "123456");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.authenticate(request));
    }

    @Test
    @DisplayName("Deve registrar novo usuário com sucesso")
    void shouldRegisterNewUserSuccessfully() {
        RegisterRequest request = new RegisterRequest("newuser", "user@email.com", "123456", (short) 1);

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(getMockUser()));

        when(passwordEncoder.encode(request.password())).thenReturn("encoded-pass");
        when(userRepository.save(any(User.class))).thenReturn(getMockUser());
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtService.generateToken(authentication)).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertEquals("User registered successfully", response.message());
        assertEquals("jwt-token", response.token());
        assertEquals("user@email.com", response.user().getEmail());
    }

    @Test
    @DisplayName("Deve lançar exceção se usuário já existir ao registrar")
    void shouldThrowIfUserAlreadyExists() {
        RegisterRequest request = new RegisterRequest("user", "user@email.com", "123456", (short) 1);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(getMockUser()));

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
    }

    @Test
    @DisplayName("Deve validar token e retornar Jwt")
    void shouldValidateToken() {
        Jwt mockJwt = mock(Jwt.class);
        when(jwtService.validateToken("valid-token")).thenReturn(mockJwt);

        Jwt result = authService.validateToken("valid-token");

        assertEquals(mockJwt, result);
    }

    @Test
    @DisplayName("Deve buscar usuário a partir do token Jwt")
    void shouldGetUserFromJwtToken() {
        User user = getMockUser();
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDTO dto = authService.getUserFromToken(jwt);

        assertEquals(user.getEmail(), dto.getEmail());
    }

    @Test
    @DisplayName("Deve lançar exceção se usuário não encontrado ao buscar por token")
    void shouldThrowIfUserNotFoundFromToken() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("notfound@email.com");
        when(userRepository.findByEmail("notfound@email.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.getUserFromToken(jwt));
    }

    private User getMockUser() {
        return new User(
                1L,
                "user",
                "user@email.com",
                "123456",
                "USER",
                UserType.BUYER,
                true,
                new Date()
        );
    }
}