# Frontend-Backend Integration Guide

This guide explains how to integrate the React frontend with the Spring Boot microservices backend.

## Issues Fixed

### 1. CORS Configuration (FIXED)
- **Problem**: Frontend couldn't communicate with backend due to CORS restrictions
- **Root Cause**: Multiple CORS configurations causing duplicate `Access-Control-Allow-Origin` headers
- **Solution**: Centralized CORS handling in API Gateway only, removed from individual services

### 2. API Endpoint Mismatches
- **Problem**: Frontend was calling incorrect endpoints
- **Solution**: Updated frontend constants to match backend routes:
  - `/auth-service/auth/login` → `/api/auth/login`
  - `/auth-service/auth/register` → `/api/auth/register`

### 3. Missing Backend Endpoints
- **Problem**: Frontend expected `/logout` and `/validate` endpoints that didn't exist
- **Solution**: Added missing endpoints to AuthController

### 4. Response Structure Mismatch
- **Problem**: Frontend expected direct response, but backend wraps responses in `ApiResponse<T>`
- **Solution**: Updated frontend types and service to handle wrapped responses

## Backend Changes Made

### 1. API Gateway SecurityConfig (CORS Fix)
```java
@Bean
public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ENABLED CORS
        .authorizeExchange(authz -> authz
            .pathMatchers("/api/auth/**").permitAll()
            .pathMatchers("/actuator/**").permitAll()
            .anyExchange().authenticated()
        );
    return http.build();
}

@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.setAllowedOriginPatterns(Arrays.asList("*"));
    corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    corsConfig.setAllowedHeaders(Arrays.asList("*"));
    corsConfig.setAllowCredentials(true);
    corsConfig.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfig);

    return source;
}
```

### 2. Auth Service SecurityConfig (Removed CORS)
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        // CORS removed - handled by API Gateway
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/health", "/actuator/**", "/error").permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
}
```

### 3. AuthController - Added Missing Endpoints
```java
@PostMapping("/logout")
public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String authHeader) {
    return ResponseEntity.ok(ApiResponse.success("Logout successful"));
}

@GetMapping("/validate")
public ResponseEntity<ApiResponse<Map<String, Object>>> validateToken(@RequestHeader("Authorization") String authHeader) {
    // Token validation logic
}
```

### 4. AuthService - Added validateToken Method
```java
public Map<String, Object> validateToken(String token) {
    // Token validation and user info extraction
}
```

## Frontend Changes Made

### 1. Updated API Endpoints (constants.ts)
```typescript
export const ENDPOINTS = {
  AUTH: {
    LOGIN: '/api/auth/login',
    SIGNUP: '/api/auth/register',
    REFRESH: '/api/auth/refresh',
    LOGOUT: '/api/auth/logout',
    VALIDATE: '/api/auth/validate',
  },
  // ...
};
```

### 2. Updated Types (auth.ts)
```typescript
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data?: T;
  error?: any;
}

export interface AuthResponse {
  token: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  user: UserInfo;
}
```

### 3. Updated AuthService
- Handle wrapped API responses
- Proper error handling
- Correct token refresh logic

## Testing the Integration

### 1. Start Backend Services
```bash
# Start all services using docker-compose
docker-compose up -d

# Or start individually:
# 1. Service Registry (port 8761)
# 2. Config Server (port 8888)
# 3. API Gateway (port 8080)
# 4. Auth Service (port 8081)
```

### 2. Test CORS Fix
```bash
# Run the CORS test script
scripts/test-cors-fix.bat

# Or test manually:
curl -X OPTIONS http://localhost:8080/api/auth/login \
  -H "Origin: http://localhost:3000" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type, Authorization" \
  -v
```

### 3. Test Backend Endpoints
```bash
# Run the integration test script
scripts/test-frontend-integration.bat

# Or test manually:
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Test","lastName":"User","email":"test@example.com","password":"password123","phoneNumber":"1234567890","role":"USER"}'
```

### 4. Start Frontend
```bash
cd temp-frontend
npm install
npm start
```

### 5. Test Frontend-Backend Communication
1. Open browser to `http://localhost:3000`
2. Try to register a new user
3. Try to login with existing credentials
4. Check browser console for any errors

## Environment Configuration

### Frontend Environment Variables
Create `.env` file in frontend root:
```env
REACT_APP_API_GATEWAY_URL=http://localhost:8080
REACT_APP_AUTH_SERVICE_URL=http://localhost:8081
REACT_APP_DEBUG=true
```

### Backend Configuration
All backend services are configured to run on:
- Service Registry: `http://localhost:8761`
- Config Server: `http://localhost:8888`
- API Gateway: `http://localhost:8080`
- Auth Service: `http://localhost:8081`

## Common Issues and Solutions

### 1. CORS Errors (FIXED)
- **Symptom**: Browser shows CORS errors in console
- **Root Cause**: Multiple CORS configurations causing duplicate headers
- **Solution**: Centralized CORS in API Gateway only

### 2. 404 Not Found
- **Symptom**: API calls return 404
- **Solution**: Check that endpoints match between frontend and backend

### 3. 401 Unauthorized
- **Symptom**: Login works but subsequent requests fail
- **Solution**: Check token format and refresh token logic

### 4. Network Errors
- **Symptom**: Can't connect to backend
- **Solution**: Ensure all backend services are running and accessible

## Debugging Tips

### 1. Check Service Health
```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8081/api/auth/health
```

### 2. Monitor Logs
```bash
# Check service logs
docker-compose logs -f api-gateway
docker-compose logs -f auth-service
```

### 3. Browser Developer Tools
- Check Network tab for failed requests
- Check Console for JavaScript errors
- Check Application tab for stored tokens

### 4. Test Individual Services
```bash
# Test auth service directly
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'
```

## CORS Architecture

### Before (Problematic)
```
Frontend → API Gateway (CORS disabled) → Auth Service (CORS enabled)
Result: Duplicate CORS headers
```

### After (Fixed)
```
Frontend → API Gateway (CORS enabled) → Auth Service (CORS disabled)
Result: Single CORS header from Gateway
```

## Next Steps

1. **Add More Services**: Implement other microservices (user-service, clinic-service, etc.)
2. **Error Handling**: Improve error handling and user feedback
3. **Security**: Add rate limiting, input validation, and security headers
4. **Testing**: Add unit and integration tests
5. **Deployment**: Prepare for production deployment 