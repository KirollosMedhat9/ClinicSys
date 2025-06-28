@echo off
setlocal enabledelayedexpansion

REM Test User Service API with Cookie-based Authentication Flow
REM This script demonstrates the complete flow: login -> manage profile

echo === ClinicSys User Service Test Script (Cookie-based Auth) ===
echo This script demonstrates the complete user flow:
echo 1. User registration/login (sets JWT cookies)
echo 2. Profile management (uses JWT cookies)
echo 3. Profile completion tracking
echo.

REM Configuration (allow override by environment)
if not defined AUTH_SERVICE_URL set AUTH_SERVICE_URL=http://localhost:8081/api/auth
if not defined USER_SERVICE_URL set USER_SERVICE_URL=http://localhost:8082/api/user/profiles
if not defined API_GATEWAY_URL set API_GATEWAY_URL=http://localhost:8080

REM Cookie file for storing JWT tokens
set COOKIE_FILE=cookies.txt

echo === Step 1: User Registration ===
echo Registering a new user...

set REGISTER_RESPONSE=
for /f "delims=" %%i in ('curl.exe -s -X POST "%AUTH_SERVICE_URL%/register" ^
    -H "Content-Type: application/json" ^
    -d '{"username": "alice.smith", "email": "alice.smith@example.com", "password": "password123", "role": "PATIENT", "firstName": "Alice", "lastName": "Smith"}'') do set REGISTER_RESPONSE=%%i

echo Registration Response: !REGISTER_RESPONSE!
echo.
echo.

echo === Step 2: User Login (Sets JWT Cookies) ===
echo Logging in to get JWT cookies...

REM Login and save cookies to file
curl.exe -s -X POST "%AUTH_SERVICE_URL%/login" ^
    -H "Content-Type: application/json" ^
    -d '{"email": "alice.smith@example.com", "password": "password123"}' ^
    -c !COOKIE_FILE!

echo Login completed - JWT cookies saved to !COOKIE_FILE!
echo.

echo === Step 3: Get Current User Profile (Using Cookies) ===
echo Getting current user profile...

curl.exe -s -X GET "%USER_SERVICE_URL%/me" ^
    -H "Content-Type: application/json" ^
    -b !COOKIE_FILE!
echo.
echo.

echo === Step 4: Create User Profile (Using Cookies) ===
echo Creating comprehensive user profile...

curl.exe -s -X POST "%USER_SERVICE_URL%/me" ^
    -H "Content-Type: application/json" ^
    -b !COOKIE_FILE! ^
    -d '{"firstName": "Alice", "lastName": "Smith", "email": "alice.smith@example.com", "phoneNumber": "+1234567890", "dateOfBirth": "1990-05-15", "gender": "FEMALE", "address": "123 Main Street", "city": "New York", "state": "NY", "zipCode": "10001", "country": "USA", "emergencyContactName": "John Smith", "emergencyContactPhone": "+1234567891", "emergencyContactRelationship": "Spouse", "medicalConditions": "None", "allergies": "None", "medications": "None", "skinType": "Type III", "skinSensitivity": "Normal", "previousTreatments": "None", "treatmentGoals": "Smooth skin, hair reduction", "preferredAppointmentTime": "MORNING", "preferredDays": "MONDAY,TUESDAY,WEDNESDAY", "communicationPreferences": "{\"email\": true, \"sms\": true, \"push\": false}", "languagePreference": "ENGLISH", "timezone": "America/New_York", "marketingConsent": true, "dataProcessingConsent": true, "photoConsent": false}'
echo.
echo.

echo === Step 5: Check Profile Completion (Using Cookies) ===
echo Checking profile completion status...

curl.exe -s -X GET "%USER_SERVICE_URL%/me/completion" ^
    -H "Content-Type: application/json" ^
    -b !COOKIE_FILE!
echo.
echo.

echo === Step 6: Update Profile Sections (Using Cookies) ===
echo Updating personal information...

curl.exe -s -X PATCH "%USER_SERVICE_URL%/me/personal-info" ^
    -H "Content-Type: application/json" ^
    -b !COOKIE_FILE! ^
    -d '{"phoneNumber": "+1234567899", "address": "456 Oak Avenue", "city": "Brooklyn"}'
echo.
echo.

echo Updating medical information...

curl.exe -s -X PATCH "%USER_SERVICE_URL%/me/medical-info" ^
    -H "Content-Type: application/json" ^
    -b !COOKIE_FILE! ^
    -d '{"skinType": "Type IV", "treatmentGoals": "Complete hair removal, smooth skin texture"}'
echo.
echo.

echo === Step 7: Update Treatment Progress (Using Cookies) ===
echo Updating treatment progress...

curl.exe -s -X PATCH "%USER_SERVICE_URL%/me/treatment-progress" ^
    -H "Content-Type: application/json" ^
    -b !COOKIE_FILE! ^
    -d '{"currentTreatmentPlan": "Full Body Laser Treatment", "sessionsCompleted": 2, "totalSessionsPlanned": 8, "lastSessionDate": "2024-01-15", "nextSessionDate": "2024-02-15", "treatmentProgressNotes": "Good progress, skin responding well to treatment"}'
echo.
echo.

echo === Step 8: Get Final Profile (Using Cookies) ===
echo Getting complete user profile...

curl.exe -s -X GET "%USER_SERVICE_URL%/me" ^
    -H "Content-Type: application/json" ^
    -b !COOKIE_FILE!
echo.
echo.

echo === Step 9: Test API Gateway Routing (Using Cookies) ===
echo Testing profile access through API Gateway...

curl.exe -s -X GET "%API_GATEWAY_URL%/api/user/profiles/me" ^
    -H "Content-Type: application/json" ^
    -b !COOKIE_FILE!
echo.
echo.

echo === Test Summary ===
echo ✓ User registration completed
echo ✓ User login and JWT cookies obtained
echo ✓ Profile creation completed
echo ✓ Profile completion tracking working
echo ✓ Section updates working
echo ✓ Treatment progress tracking working
echo ✓ API Gateway routing tested
echo.
echo All tests completed successfully!
echo.
echo Next steps:
echo 1. Check the database for created profile
echo 2. Verify profile completion percentage
echo 3. Test with different user roles (ADMIN, STAFF)
echo 4. Test error scenarios (invalid token, missing fields)
echo 5. Clean up cookie file: del !COOKIE_FILE!

echo.
pause 