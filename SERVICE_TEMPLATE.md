# Microservice Authentication Template

## Cookie-Based JWT Authentication Implementation

This template shows how to implement cookie-based JWT authentication in new microservices.

### 1. Dependencies

Add the common-lib dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.clinicsys</groupId>
    <artifactId>common-lib</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. JWT Configuration

Add JWT configuration to your `application.yml`:

```yaml
# JWT Configuration (must match auth-service)
jwt:
  secret: 404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
  expiration: 3600000  # 1 hour in milliseconds
  refresh-expiration: 86400000  # 24 hours in milliseconds
```

### 3. JWT Authentication Filter

Create a JWT filter that extends the common filter:

```java
package com.clinicsys.yourservice.config;

import org.springframework.stereotype.Component;

/**
 * Your Service JWT Authentication Filter
 * Extends the common JWT filter for consistent authentication
 */
@Component
public class JwtAuthenticationFilter extends com.clinicsys.common.security.JwtAuthenticationFilter {
    
    @Override
    protected boolean shouldNotFilter(jakarta.servlet.http.HttpServletRequest request) throws jakarta.servlet.ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/actuator/") || 
               path.equals("/health") || 
               path.equals("/error") ||
               path.startsWith("/public/") ||
               path.startsWith("/api/yourservice/public/"); // Your service specific public endpoints
    }
}
```

### 4. Security Configuration

Configure Spring Security to use the JWT filter:

```java
package com.clinicsys.yourservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/actuator/**", "/health", "/error", "/public/**").permitAll()
                .requestMatchers("/api/yourservice/**").authenticated()
                .anyRequest().authenticated()
            );
        
        return http.build();
    }
}
```

### 5. Testing with Cookies

Use curl with cookie handling for testing:

```bash
# Login and save cookies
curl -X POST "http://localhost:8081/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email": "user@example.com", "password": "password"}' \
  -c cookies.txt

# Use cookies for authenticated requests
curl -X GET "http://localhost:8082/api/yourservice/endpoint" \
  -H "Content-Type: application/json" \
  -b cookies.txt
```

### 6. Authentication Flow

1. **Login**: User logs in via auth-service, receives JWT cookies
2. **Requests**: Browser automatically sends cookies with requests
3. **Validation**: Service validates JWT from cookies
4. **Authorization**: Service checks user roles for access control

### 7. Benefits

- **Automatic**: Browser handles cookie transmission
- **Secure**: HttpOnly cookies prevent XSS attacks
- **Consistent**: Same authentication across all services
- **Fallback**: Still supports Authorization header for API clients

### 8. Security Considerations

- Set `Secure=true` in production (HTTPS)
- Use `SameSite=Strict` for CSRF protection
- Implement proper logout by clearing cookies
- Consider token refresh mechanisms 