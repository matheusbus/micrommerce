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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException(request.email()));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        String token = jwtService.generateToken(authentication);

        return new AuthResponse("User logged in successfully", new UserDTO(user), token);
    }

    public AuthResponse register(RegisterRequest request) {
        addNewUser(request);

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException(request.email()));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        String token = jwtService.generateToken(authentication);

        return new AuthResponse("User registered successfully", new UserDTO(user), token);
    }

    public Jwt validateToken(String token) {
        return jwtService.validateToken(token);
    }

    private boolean validateIfUserExists(String userEmail) {
        return userRepository.findByEmail(userEmail).isPresent();
    }

    private void addNewUser(RegisterRequest request) {
        if(validateIfUserExists(request.email())) {
            throw new UserAlreadyExistsException(request.email());
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setActive(true);
        user.setRole("USER");
        user.setType(UserType.valueOf(request.type()));
        user.setCreatedAt(Date.from(java.time.Instant.now()));
        userRepository.save(user);
    }
}
