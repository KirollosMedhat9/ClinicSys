@echo off
echo Testing Frontend-Backend Integration
echo ====================================

echo.
echo 1. Testing API Gateway health...
curl -X GET http://localhost:8080/actuator/health
echo.

echo.
echo 2. Testing Auth Service health...
curl -X GET http://localhost:8081/api/auth/health
echo.

echo.
echo 3. Testing CORS preflight request...
curl -X OPTIONS http://localhost:8080/api/auth/login ^
  -H "Origin: http://localhost:3000" ^
  -H "Access-Control-Request-Method: POST" ^
  -H "Access-Control-Request-Headers: Content-Type" ^
  -v
echo.

echo.
echo 4. Testing registration endpoint...
curl -X POST http://localhost:8080/api/auth/register ^
  -H "Content-Type: application/json" ^
  -H "Origin: http://localhost:3000" ^
  -d "{\"firstName\":\"Test\",\"lastName\":\"User\",\"email\":\"test@example.com\",\"password\":\"password123\",\"phoneNumber\":\"1234567890\",\"role\":\"USER\"}"
echo.

echo.
echo Integration test completed!
pause 