package com.esw.authservice.controller;

import com.esw.authservice.config.SecurityTestConfig;
import com.esw.authservice.dto.AuthRequest;
import com.esw.authservice.dto.AuthResponse;
import com.esw.authservice.dto.RegisterRequest;
import com.esw.authservice.dto.UserDTO;
import com.esw.authservice.model.User;
import com.esw.authservice.model.UserType;
import com.esw.authservice.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@WebMvcTest(AuthController.class)
@Import({SecurityTestConfig.class})
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/auth/login - Deve autenticar usuário com sucesso")
    void shouldAuthenticateUserSuccessfully() throws Exception {
        AuthRequest request = new AuthRequest("user@email.com", "123456");

        User user = new User();
        user.setId(1L);
        user.setEmail(request.email());
        user.setUsername("userbuyer");
        user.setPassword(request.password());
        user.setType(UserType.BUYER);
        UserDTO userDTO = new UserDTO(user);

        AuthResponse response = new AuthResponse("User logged in successfully", userDTO, "fake-jwt-token");

        when(authService.authenticate(request)).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    @DisplayName("POST /api/auth/register - Deve registrar novo usuário com sucesso")
    void shouldRegisterUserSuccessfully() throws Exception {
        RegisterRequest request = new RegisterRequest("userbuyer", "user@email.com", "123456", (short) 1);

        User user = new User();
        user.setId(1L);
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setPassword(request.password());
        user.setType(UserType.valueOf(request.type()));
        UserDTO userDTO = new UserDTO(user);

        AuthResponse response = new AuthResponse("User registered successfully", userDTO, "registered-jwt-token");

        when(authService.register(request)).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("registered-jwt-token"));
    }

    @Test
    @WithMockUser(username = "user@email.com")
    @DisplayName("GET /api/auth/me - Deve retornar e-mail do usuário autenticado")
    void shouldReturnAuthenticatedUsername() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isOk())
                .andExpect(content().string("Authenticated user: user@email.com"));
    }
}
