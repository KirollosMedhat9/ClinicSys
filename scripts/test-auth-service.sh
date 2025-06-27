#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Base URL for the auth service
BASE_URL="http://localhost:8081/api/auth"

echo -e "${YELLOW}=== ClinicSys Auth Service Test Script ===${NC}"
echo ""

# Test 1: Health Check
echo -e "${YELLOW}1. Testing Health Check...${NC}"
curl -s -X GET "$BASE_URL/health" | jq .
echo ""

# Test 2: User Registration
echo -e "${YELLOW}2. Testing User Registration...${NC}"
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/register" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "Test123!@#",
    "phoneNumber": "+1234567890",
    "role": "PATIENT"
  }')

echo "Registration Response:"
echo "$REGISTER_RESPONSE" | jq .

# Extract token from registration response
TOKEN=$(echo "$REGISTER_RESPONSE" | jq -r '.data.token')
REFRESH_TOKEN=$(echo "$REGISTER_RESPONSE" | jq -r '.data.refreshToken')

echo ""
echo -e "${GREEN}✓ Registration completed${NC}"
echo ""

# Test 3: User Login
echo -e "${YELLOW}3. Testing User Login...${NC}"
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "Test123!@#"
  }')

echo "Login Response:"
echo "$LOGIN_RESPONSE" | jq .

# Extract new token from login response
NEW_TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.data.token')
NEW_REFRESH_TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.data.refreshToken')

echo ""
echo -e "${GREEN}✓ Login completed${NC}"
echo ""

# Test 4: Token Refresh
echo -e "${YELLOW}4. Testing Token Refresh...${NC}"
REFRESH_RESPONSE=$(curl -s -X POST "$BASE_URL/refresh" \
  -H "Authorization: Bearer $NEW_REFRESH_TOKEN")

echo "Token Refresh Response:"
echo "$REFRESH_RESPONSE" | jq .

echo ""
echo -e "${GREEN}✓ Token refresh completed${NC}"
echo ""

# Test 5: Invalid Login (Wrong Password)
echo -e "${YELLOW}5. Testing Invalid Login (Wrong Password)...${NC}"
INVALID_LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "WrongPassword123!@#"
  }')

echo "Invalid Login Response:"
echo "$INVALID_LOGIN_RESPONSE" | jq .

echo ""
echo -e "${GREEN}✓ Invalid login test completed${NC}"
echo ""

# Test 6: Duplicate Registration
echo -e "${YELLOW}6. Testing Duplicate Registration...${NC}"
DUPLICATE_RESPONSE=$(curl -s -X POST "$BASE_URL/register" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "Test123!@#",
    "phoneNumber": "+1234567890",
    "role": "PATIENT"
  }')

echo "Duplicate Registration Response:"
echo "$DUPLICATE_RESPONSE" | jq .

echo ""
echo -e "${GREEN}✓ Duplicate registration test completed${NC}"
echo ""

# Test 7: Admin Registration
echo -e "${YELLOW}7. Testing Admin Registration...${NC}"
ADMIN_RESPONSE=$(curl -s -X POST "$BASE_URL/register" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Admin",
    "lastName": "User",
    "email": "admin@clinicsys.com",
    "password": "Admin123!@#",
    "phoneNumber": "+1987654321",
    "role": "ADMIN"
  }')

echo "Admin Registration Response:"
echo "$ADMIN_RESPONSE" | jq .

echo ""
echo -e "${GREEN}✓ Admin registration completed${NC}"
echo ""

echo -e "${GREEN}=== All Auth Service Tests Completed ===${NC}"
echo ""
echo -e "${YELLOW}Test Summary:${NC}"
echo "✓ Health Check"
echo "✓ User Registration"
echo "✓ User Login"
echo "✓ Token Refresh"
echo "✓ Invalid Login"
echo "✓ Duplicate Registration"
echo "✓ Admin Registration"
echo ""
echo -e "${YELLOW}Service URLs:${NC}"
echo "Auth Service: http://localhost:8081"
echo "Eureka Dashboard: http://localhost:8761"
echo "API Gateway: http://localhost:8080" 