# User Service

The User Service is responsible for managing patient profiles and user information in the ClinicSys platform. It provides comprehensive profile management capabilities with authentication and authorization integration.

## Features

### Profile Management
- **Complete Profile Creation**: Users can create comprehensive profiles with personal, medical, and preference information
- **Section-based Updates**: Update specific sections of the profile (personal info, medical info, preferences, etc.)
- **Profile Completion Tracking**: Automatic calculation of profile completion percentage
- **Treatment Progress Tracking**: Monitor treatment sessions, progress notes, and scheduling

### Authentication & Security
- **JWT Integration**: Works with the Auth Service for secure authentication
- **Role-based Access**: Different access levels for patients, staff, and admins
- **API Gateway Integration**: Routes through the API Gateway for unified access

### Profile Sections

#### Personal Information
- Basic details (name, email, phone)
- Address information
- Emergency contact details
- Date of birth and gender

#### Medical Information
- Medical conditions and allergies
- Current medications
- Skin type and sensitivity
- Previous treatments
- Treatment goals

#### Treatment Progress
- Current treatment plan
- Sessions completed vs. planned
- Last and next session dates
- Progress notes

#### Preferences
- Preferred clinic and staff
- Appointment time preferences
- Communication preferences
- Language and timezone settings

#### Privacy & Consents
- Marketing consent
- Data processing consent
- Photo consent

## API Endpoints

### Patient Endpoints (Authenticated)
- `GET /api/user/profiles/me` - Get current user's profile
- `POST /api/user/profiles/me` - Create profile for current user
- `PUT /api/user/profiles/me` - Update current user's profile
- `PATCH /api/user/profiles/me/personal-info` - Update personal information
- `PATCH /api/user/profiles/me/medical-info` - Update medical information
- `PATCH /api/user/profiles/me/preferences` - Update preferences
- `PATCH /api/user/profiles/me/emergency-contact` - Update emergency contact
- `PATCH /api/user/profiles/me/treatment-progress` - Update treatment progress
- `PATCH /api/user/profiles/me/consents` - Update consents
- `GET /api/user/profiles/me/completion` - Get profile completion status

### Admin Endpoints (Admin Role Required)
- `GET /api/user/profiles/{id}` - Get specific user profile
- `GET /api/user/profiles` - Get all profiles (paginated)
- `PUT /api/user/profiles/{id}` - Update specific user profile
- `DELETE /api/user/profiles/{id}` - Delete user profile
- `GET /api/user/profiles/search` - Search profiles

## Usage Flow

### 1. User Registration/Login
```bash
# Register new user
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "alice.smith", "email": "alice@example.com", "password": "password123", "role": "PATIENT"}'

# Login to get JWT token
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "alice.smith", "password": "password123"}'
```

### 2. Create Profile
```bash
# Create comprehensive profile
curl -X POST http://localhost:8080/api/user/profiles/me \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "firstName": "Alice",
    "lastName": "Smith",
    "email": "alice@example.com",
    "phoneNumber": "+1234567890",
    "dateOfBirth": "1990-05-15",
    "gender": "FEMALE",
    "skinType": "Type III",
    "treatmentGoals": "Smooth skin, hair reduction"
  }'
```

### 3. Update Profile Sections
```bash
# Update personal information
curl -X PATCH http://localhost:8080/api/user/profiles/me/personal-info \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"phoneNumber": "+1234567899", "address": "456 Oak Avenue"}'

# Update medical information
curl -X PATCH http://localhost:8080/api/user/profiles/me/medical-info \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"skinType": "Type IV", "treatmentGoals": "Complete hair removal"}'
```

### 4. Track Treatment Progress
```bash
# Update treatment progress
curl -X PATCH http://localhost:8080/api/user/profiles/me/treatment-progress \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "currentTreatmentPlan": "Full Body Laser Treatment",
    "sessionsCompleted": 2,
    "totalSessionsPlanned": 8,
    "lastSessionDate": "2024-01-15",
    "nextSessionDate": "2024-02-15"
  }'
```

## Configuration

### Application Properties
```yaml
server:
  port: 8082

spring:
  application:
    name: user-service
  datasource:
    url: jdbc:postgresql://localhost:5432/userdb
    username: user
    password: password
  jpa:
    hibernate:
      ddl-auto: update

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

jwt:
  secret: your-secret-key-here
  expiration: 86400000
```

## Database Schema

The service uses a comprehensive `user_profiles` table with the following key fields:

- **Basic Info**: id, user_id, first_name, last_name, email, phone_number
- **Personal**: date_of_birth, gender, address, city, state, zip_code, country
- **Emergency Contact**: emergency_contact_name, emergency_contact_phone, emergency_contact_relationship
- **Medical**: medical_conditions, allergies, medications, skin_type, skin_sensitivity, previous_treatments, treatment_goals
- **Treatment Progress**: current_treatment_plan, sessions_completed, total_sessions_planned, last_session_date, next_session_date, treatment_progress_notes
- **Preferences**: preferred_clinic_id, preferred_staff_id, preferred_appointment_time, preferred_days, communication_preferences, language_preference, timezone
- **Consents**: marketing_consent, data_processing_consent, photo_consent
- **Status**: is_active, profile_completion_percentage, created_at, updated_at

## Testing

Use the provided test scripts:
- `scripts/test-user-service.sh` (Linux/Mac)
- `scripts/test-user-service.bat` (Windows)

These scripts demonstrate the complete flow from registration to profile management.

## Integration Points

- **Auth Service**: JWT token validation and user authentication
- **API Gateway**: Unified routing and security
- **Service Registry**: Service discovery via Eureka
- **Config Server**: Centralized configuration management

## Security Considerations

- All endpoints require valid JWT tokens (except health checks)
- Role-based access control for admin endpoints
- Input validation and sanitization
- CORS configuration for frontend integration
- Secure password handling (delegated to Auth Service)

## Future Enhancements

- Profile photo upload functionality
- Integration with medical history service
- Advanced search and filtering capabilities
- Profile export/import functionality
- Multi-language support for profile fields
- Integration with notification service for profile updates 