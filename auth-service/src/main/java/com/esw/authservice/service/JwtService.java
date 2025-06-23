package com.esw.authservice.service;

import com.esw.authservice.exception.InvalidJwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public JwtService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 3600L;

        String scopes = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
            .issuer("auth-service")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiry))
            .subject(authentication.getName())
            .claim("scopes", scopes)
            .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Jwt validateToken(final String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            if (!"auth-service".equals(jwt.getIssuer().toString())) {
                throw new InvalidJwtException("Invalid token issuer.");
            }
            return jwt;
        } catch (JwtException ex) {
            throw new InvalidJwtException("Token is not valid or expired.", ex);
        }
    }
}
