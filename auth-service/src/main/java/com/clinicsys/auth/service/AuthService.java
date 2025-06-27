package com.clinicsys.auth.service;

import com.clinicsys.auth.dto.AuthResponse;
import com.clinicsys.auth.dto.LoginRequest;
import com.clinicsys.auth.dto.RegisterRequest;
import com.clinicsys.auth.model.User;
import com.clinicsys.auth.repository.UserRepository;
import com.clinicsys.common.exception.BadRequestException;
import com.clinicsys.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Lazy
    private final AuthenticationManager authenticationManager;
    
    public AuthResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("User with this email already exists");
        }
        
        // Create new user
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
        user.setActive(true);
        user.setVerified(false); // Will be verified via email later
        
        User savedUser = userRepository.save(user);
        
        // Generate tokens
        String token = jwtService.generateToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);
        
        return createAuthResponse(savedUser, token, refreshToken);
    }
    
    public AuthResponse login(LoginRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        // Load user details
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        
        if (!user.isActive()) {
            throw new UnauthorizedException("Account is deactivated");
        }
        
        // Generate tokens
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        
        return createAuthResponse(user, token, refreshToken);
    }
    
    public AuthResponse refreshToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));
        
        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new UnauthorizedException("Invalid refresh token");
        }
        
        // Generate new tokens
        String newToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        
        return createAuthResponse(user, newToken, newRefreshToken);
    }
    
    private AuthResponse createAuthResponse(User user, String token, String refreshToken) {
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getRole().name(),
                user.isVerified()
        );
        
        return new AuthResponse(
                token,
                refreshToken,
                "Bearer",
                3600000L, // 1 hour in milliseconds
                userInfo
        );
    }
} 