@echo off
echo Testing CORS Fix
echo ================

echo.
echo 1. Testing CORS preflight request to API Gateway...
curl -X OPTIONS http://localhost:8080/api/auth/login ^
  -H "Origin: http://localhost:3000" ^
  -H "Access-Control-Request-Method: POST" ^
  -H "Access-Control-Request-Headers: Content-Type, Authorization" ^
  -v
echo.

echo.
echo 2. Testing actual login request...
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -H "Origin: http://localhost:3000" ^
  -d "{\"email\":\"test@example.com\",\"password\":\"password123\"}" ^
  -v
echo.

echo.
echo 3. Testing registration request...
curl -X POST http://localhost:8080/api/auth/register ^
  -H "Content-Type: application/json" ^
  -H "Origin: http://localhost:3000" ^
  -d "{\"firstName\":\"Test\",\"lastName\":\"User\",\"email\":\"test2@example.com\",\"password\":\"password123\",\"phoneNumber\":\"1234567890\",\"role\":\"USER\"}" ^
  -v
echo.

echo.
echo CORS test completed!
echo Check that the Access-Control-Allow-Origin header appears only once in the response.
pause 