@echo off
echo ========================================
echo Testing Database Connection and Registration
echo ========================================
echo.

set BASE_URL=http://localhost:8081

echo [1] Testing health endpoint...
echo [REQUEST] GET %BASE_URL%/api/auth/health
echo [RESPONSE]
curl -s -X GET "%BASE_URL%/api/auth/health"
echo.
echo.

echo [2] Testing valid registration...
echo [REQUEST] POST %BASE_URL%/api/auth/register
echo [BODY] {"firstName": "Test", "lastName": "User", "email": "test@example.com", "password": "Test123!@#", "phoneNumber": "+1234567890", "role": "PATIENT"}
echo [RESPONSE]
curl -s -X POST "%BASE_URL%/api/auth/register" ^
  -H "Content-Type: application/json" ^
  -d "{\"firstName\": \"Test\", \"lastName\": \"User\", \"email\": \"test@example.com\", \"password\": \"Test123!@#\", \"phoneNumber\": \"+1234567890\", \"role\": \"PATIENT\"}"
echo.
echo.

echo [3] Checking database for new user...
echo [DATABASE CHECK]
docker exec postgres-auth psql -U postgres -d auth_db -c "SELECT id, email, first_name, last_name, role FROM users WHERE email = 'test@example.com';"
echo.
echo.

echo [4] Testing login with the registered user...
echo [REQUEST] POST %BASE_URL%/api/auth/login
echo [BODY] {"email": "test@example.com", "password": "Test123!@#"}
echo [RESPONSE]
curl -s -X POST "%BASE_URL%/api/auth/login" ^
  -H "Content-Type: application/json" ^
  -d "{\"email\": \"test@example.com\", \"password\": \"Test123!@#\"}"
echo.
echo.

echo [5] Final database count...
echo [DATABASE COUNT]
docker exec postgres-auth psql -U postgres -d auth_db -c "SELECT COUNT(*) as total_users FROM users;"
echo.
echo.

echo Testing Completed!
echo ========================================
pause 