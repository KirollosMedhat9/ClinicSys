package com.clinicsys.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

/**
 * Reusable JWT Authentication Filter for all microservices
 * Supports both cookie-based and header-based JWT authentication
 */
public abstract class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret:defaultSecretKeyForDevelopmentOnly}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String token = extractTokenFromRequest(request);
        
        if (token != null) {
            try {
                // Use Base64 decoding like auth service
                SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                
                String userId = claims.getSubject();
                String role = claims.get("role", String.class);
                
                // If role is not in claims, try to extract from authorities
                if (role == null) {
                    List<String> authorities = claims.get("authorities", List.class);
                    if (authorities != null && !authorities.isEmpty()) {
                        // Extract role from "ROLE_ADMIN" format
                        String authority = authorities.get(0);
                        if (authority.startsWith("ROLE_")) {
                            role = authority.substring(5); // Remove "ROLE_" prefix
                        }
                    }
                }
                
                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Ensure role has ROLE_ prefix for Spring Security
                    String roleWithPrefix = role != null && !role.startsWith("ROLE_") ? "ROLE_" + role : role;
                    
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, 
                        null, 
                        List.of(new SimpleGrantedAuthority(roleWithPrefix))
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    logger.debug("JWT authentication successful for user: " + userId + " with role: " + roleWithPrefix);
                }
                
            } catch (Exception e) {
                logger.error("JWT token validation failed: " + e.getMessage(), e);
            }
        }
        
        filterChain.doFilter(request, response);
    }

    /**
     * Extract JWT token from request (cookie first, then header)
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        // First, try to get token from cookie (primary method)
        String token = extractTokenFromCookie(request);
        
        // If no token in cookie, try Authorization header (fallback)
        if (token == null) {
            token = extractTokenFromHeader(request);
        }
        
        return token;
    }

    /**
     * Extract JWT token from cookie
     */
    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt_token".equals(cookie.getName())) {
                    logger.debug("JWT token found in cookie");
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Extract JWT token from Authorization header
     */
    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            logger.debug("JWT token found in Authorization header");
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * Override this method to customize which paths should be excluded from authentication
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/actuator/") || 
               path.equals("/health") || 
               path.equals("/error") ||
               path.startsWith("/public/");
    }
} 