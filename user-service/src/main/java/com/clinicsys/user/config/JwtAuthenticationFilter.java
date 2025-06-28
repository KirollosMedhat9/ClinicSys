package com.clinicsys.user.config;

import org.springframework.stereotype.Component;

/**
 * User Service JWT Authentication Filter
 * Extends the common JWT filter for consistent authentication across services
 */
@Component
public class JwtAuthenticationFilter extends com.clinicsys.common.security.JwtAuthenticationFilter {
    
    // Inherits all functionality from the common filter
    // Can override methods here if user-service specific behavior is needed
    
    @Override
    protected boolean shouldNotFilter(jakarta.servlet.http.HttpServletRequest request) throws jakarta.servlet.ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/actuator/") || 
               path.equals("/health") || 
               path.equals("/error") ||
               path.startsWith("/public/") ||
               path.startsWith("/api/user/public/"); // User service specific public endpoints
    }
} 