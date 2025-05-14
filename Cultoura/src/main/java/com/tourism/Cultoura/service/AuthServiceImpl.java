package com.tourism.Cultoura.service;

import com.tourism.Cultoura.RequestModels.LoginRequest;
import com.tourism.Cultoura.RequestModels.RegistrationRequest;
import com.tourism.Cultoura.model.User;
import com.tourism.Cultoura.repository.UserRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
         this.userRepository = userRepository;
         this.passwordEncoder = passwordEncoder;
         this.jwtEncoder =  jwtEncoder;
    }

    @Override
    public void register(RegistrationRequest request) {
         if (userRepository.findByUsername(request.getUsername()).isPresent()) {
             throw new RuntimeException("Username is already taken");
         }

         User user = new User();
         user.setUsername(request.getUsername());
         user.setEmail(request.getEmail());
         user.setRole(request.getRole());
         // Encode the password before saving
         user.setPassword(passwordEncoder.encode(request.getPassword()));
         userRepository.save(user);
    }

    @Override
    public String authenticate(LoginRequest request) {
        // Retrieve the user from the repository
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify the provided password against the stored encoded password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Build the JWT claims
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(90, ChronoUnit.MINUTES))
                .subject(user.getUsername())
                // Include roles as a claim if needed
                .claim("roles","ROLE_" +user.getAuthorities()
                                     .stream()
                                     .map(GrantedAuthority::getAuthority)
                                     .collect(Collectors.joining(" ")))
                .build();

        // Generate the JWT token using the JwtEncoder bean
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
