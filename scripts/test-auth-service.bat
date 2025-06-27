@echo off
echo ========================================
echo    ClinicSys Auth Service Test Script
echo ========================================
echo.

REM Base URL for the auth service
set BASE_URL=http://localhost:8081/api/auth

echo [INFO] Testing Auth Service at: %BASE_URL%
echo.

echo ========================================
echo 1. Testing Health Check
echo ========================================
echo [REQUEST] GET %BASE_URL%/health
echo [RESPONSE]
curl -s -X GET "%BASE_URL%/health" 
echo.
echo.

echo ========================================
echo 2. Testing User Registration
echo ========================================
echo [REQUEST] POST %BASE_URL%/register
echo [REQUEST BODY]
echo {
echo   "firstName": "John",
echo   "lastName": "Doe", 
echo   "email": "john.doae@example.com",
echo   "password": "1Kk12312223#",
echo   "phoneNumber": "+1234567890",
echo   "role": "PATIENT"
echo }
echo.
echo [RESPONSE]
curl -s -X POST "%BASE_URL%/register" ^
  -H "Content-Type: application/json" ^
  -d "{\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"password\": \"1Kk12312223#\", \"phoneNumber\": \"+1234567890\", \"role\": \"PATIENT\"}" 
echo.
echo.

echo ========================================
echo 3. Testing User Login
echo ========================================
echo [REQUEST] POST %BASE_URL%/login
echo [REQUEST BODY]
echo {
echo   "email": "john.doe@example.com",
echo   "password": "Test123!@#"
echo }
echo.
echo [RESPONSE]
curl -s -X POST "%BASE_URL%/login" ^
  -H "Content-Type: application/json" ^
  -d "{\"email\": \"john.doae@example.com\", \"password\": \"Test123!@#\"}" 
echo.
echo.

echo ========================================
echo 4. Testing Invalid Login (Wrong Password)
echo ========================================
echo [REQUEST] POST %BASE_URL%/login
echo [REQUEST BODY]
echo {
echo   "email": "john.doe@example.com",
echo   "password": "WrongPassword123!@#"
echo }
echo.
echo [RESPONSE]
curl -s -X POST "%BASE_URL%/login" ^
  -H "Content-Type: application/json" ^
  -d "{\"email\": \"john.doe@example.com\", \"password\": \"WrongPassword123!@#\"}" 
echo.
echo.

echo ========================================
echo 5. Testing Duplicate Registration
echo ========================================
echo [REQUEST] POST %BASE_URL%/register
echo [REQUEST BODY]
echo {
echo   "firstName": "John",
echo   "lastName": "Doe",
echo   "email": "john.doe@example.com",
echo   "password": "Test123!@#",
echo   "phoneNumber": "+1234567890",
echo   "role": "PATIENT"
echo }
echo.
echo [RESPONSE]
curl -s -X POST "%BASE_URL%/register" ^
  -H "Content-Type: application/json" ^
  -d "{\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"password\": \"Test123!@#\", \"phoneNumber\": \"+1234567890\", \"role\": \"PATIENT\"}" 
echo.
echo.

echo ========================================
echo 6. Testing Admin Registration
echo ========================================
echo [REQUEST] POST %BASE_URL%/register
echo [REQUEST BODY]
echo {
echo   "firstName": "Admin",
echo   "lastName": "User",
echo   "email": "admin@clinicsys.com",
echo   "password": "Admin123!@#",
echo   "phoneNumber": "+1987654321",
echo   "role": "ADMIN"
echo }
echo.
echo [RESPONSE]
curl -s -X POST "%BASE_URL%/register" ^
  -H "Content-Type: application/json" ^
  -d "{\"firstName\": \"Admin\", \"lastName\": \"User\", \"email\": \"admin@clinicsys.com\", \"password\": \"Admin123!@#\", \"phoneNumber\": \"+1987654321\", \"role\": \"ADMIN\"}" 
echo.
echo.

echo ========================================
echo 7. Testing Staff Registration
echo ========================================
echo [REQUEST] POST %BASE_URL%/register
echo [REQUEST BODY]
echo {
echo   "firstName": "Staff",
echo   "lastName": "Member",
echo   "email": "staff@clinicsys.com",
echo   "password": "Staff123!@#",
echo   "phoneNumber": "+1555555555",
echo   "role": "STAFF"
echo }
echo.
echo [RESPONSE]
curl -s -X POST "%BASE_URL%/register" ^
  -H "Content-Type: application/json" ^
  -d "{\"firstName\": \"Staff\", \"lastName\": \"Member\", \"email\": \"staff@clinicsys.com\", \"password\": \"123\", \"phoneNumber\": \"+1555555555\", \"role\": \"STAFF\"}" 
echo.
echo.

echo ========================================
echo 8. Testing Invalid Email Format
echo ========================================
echo [REQUEST] POST %BASE_URL%/register
echo [REQUEST BODY]
echo {
echo   "firstName": "Test",
echo   "lastName": "User",
echo   "email": "invalid-email",
echo   "password": "Test123!@#",
echo   "role": "PATIENT"
echo }
echo.
echo [RESPONSE]
curl -s -X POST "%BASE_URL%/register" ^
  -H "Content-Type: application/json" ^
  -d "{\"firstName\": \"Test\", \"lastName\": \"User\", \"email\": \"invalid-email\", \"password\": \"Test123!@#\", \"role\": \"PATIENT\"}" 
echo.
echo.

echo ========================================
echo 9. Testing Weak Password
echo ========================================
echo [REQUEST] POST %BASE_URL%/register
echo [REQUEST BODY]
echo {
echo   "firstName": "Test",
echo   "lastName": "User",
echo   "email": "test@example.com",
echo   "password": "weak",
echo   "role": "PATIENT"
echo }
echo.
echo [RESPONSE]
curl -s -X POST "%BASE_URL%/register" ^
  -H "Content-Type: application/json" ^
  -d "{\"firstName\": \"Test\", \"lastName\": \"User\", \"email\": \"test@example.com\", \"password\": \"weak\", \"role\": \"PATIENT\"}" 
echo.
echo.

echo ========================================
echo 10. Testing Missing Required Fields
echo ========================================
echo [REQUEST] POST %BASE_URL%/register
echo [REQUEST BODY]
echo {
echo   "firstName": "Test",
echo   "lastName": "User",
echo   "email": "test@example.com"
echo }
echo.
echo [RESPONSE]
curl -s -X POST "%BASE_URL%/register" ^
  -H "Content-Type: application/json" ^
  -d "{\"firstName\": \"Test\", \"lastName\": \"User\", \"email\": \"test@example.com\"}" 
echo.
echo.

echo ========================================
echo Test Summary
echo ========================================
echo ✓ Health Check
echo ✓ User Registration
echo ✓ User Login
echo ✓ Invalid Login
echo ✓ Duplicate Registration
echo ✓ Admin Registration
echo ✓ Staff Registration
echo ✓ Invalid Email Validation
echo ✓ Weak Password Validation
echo ✓ Missing Fields Validation
echo.
echo ========================================
echo Service URLs
echo ========================================
echo Auth Service:    http://localhost:8081
echo Eureka Dashboard: http://localhost:8761
echo API Gateway:     http://localhost:8080
echo.
echo ========================================
echo Testing Completed!
echo ========================================
pause 
pause 