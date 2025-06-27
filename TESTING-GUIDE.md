# ClinicSys Auth Service Testing Guide

## 🚀 Quick Start Testing

### Prerequisites
- Java 17+ installed
- Maven 3.6+ installed
- PostgreSQL running (via Docker or local installation)
- Service Registry (Eureka) running on port 8761
- Config Server running on port 8888

### 1. Start Infrastructure Services

```bash
# Start Service Registry
mvn spring-boot:run -pl service-registry

# Start Config Server (in another terminal)
mvn spring-boot:run -pl config-server

# Start API Gateway (in another terminal)
mvn spring-boot:run -pl api-gateway
```

### 2. Start Auth Service

```bash
# Build the project first
mvn clean install

# Start Auth Service
mvn spring-boot:run -pl auth-service
```

### 3. Verify Services are Running

- **Service Registry**: http://localhost:8761
- **Config Server**: http://localhost:8888/actuator/health
- **API Gateway**: http://localhost:8080/actuator/health
- **Auth Service**: http://localhost:8081/actuator/health

---

## 🧪 Manual Testing

### Test 1: Health Check

```bash
curl -X GET http://localhost:8081/api/auth/health
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Auth service is running",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

### Test 2: User Registration

```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "Test123!@#",
    "phoneNumber": "+1234567890",
    "role": "PATIENT"
  }'
```

**Expected Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600000,
    "user": {
      "id": 1,
      "email": "john.doe@example.com",
      "firstName": "John",
      "lastName": "Doe",
      "phoneNumber": "+1234567890",
      "role": "PATIENT",
      "isVerified": false
    }
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

### Test 3: User Login

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "Test123!@#"
  }'
```

### Test 4: Token Refresh

```bash
# Replace YOUR_REFRESH_TOKEN with the actual refresh token
curl -X POST http://localhost:8081/api/auth/refresh \
  -H "Authorization: Bearer YOUR_REFRESH_TOKEN"
```

### Test 5: Invalid Login

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "WrongPassword123!@#"
  }'
```

**Expected Response:**
```json
{
  "success": false,
  "message": "Invalid credentials",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

### Test 6: Duplicate Registration

```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "Test123!@#",
    "phoneNumber": "+1234567890",
    "role": "PATIENT"
  }'
```

**Expected Response:**
```json
{
  "success": false,
  "message": "User with this email already exists",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

---

## 🔧 Automated Testing

### Using the Test Scripts

#### Linux/Mac:
```bash
# Make script executable
chmod +x scripts/test-auth-service.sh

# Run the test script
./scripts/test-auth-service.sh
```

#### Windows:
```bash
# Run the batch file
scripts/test-auth-service.bat
```

---

## 📊 Testing Different User Roles

### Admin Registration
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Admin",
    "lastName": "User",
    "email": "admin@clinicsys.com",
    "password": "Admin123!@#",
    "phoneNumber": "+1987654321",
    "role": "ADMIN"
  }'
```

### Staff Registration
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Staff",
    "lastName": "Member",
    "email": "staff@clinicsys.com",
    "password": "Staff123!@#",
    "phoneNumber": "+1555555555",
    "role": "STAFF"
  }'
```

---

## 🚨 Validation Testing

### Test Invalid Email Format
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Test",
    "lastName": "User",
    "email": "invalid-email",
    "password": "Test123!@#",
    "role": "PATIENT"
  }'
```

### Test Weak Password
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Test",
    "lastName": "User",
    "email": "test@example.com",
    "password": "weak",
    "role": "PATIENT"
  }'
```

### Test Missing Required Fields
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Test",
    "lastName": "User",
    "email": "test@example.com"
  }'
```

---

## 🔍 Database Verification

### Check User Table
```sql
-- Connect to PostgreSQL
psql -h localhost -p 5432 -U postgres -d auth_db

-- View all users
SELECT id, email, first_name, last_name, role, is_active, is_verified, created_at 
FROM users;

-- Check specific user
SELECT * FROM users WHERE email = 'john.doe@example.com';
```

---

## 🐛 Troubleshooting

### Common Issues

#### 1. Service Won't Start
**Error**: `Port already in use`
**Solution**: 
```bash
# Find process using port 8081
lsof -i :8081
# Kill the process
kill -9 <PID>
```

#### 2. Database Connection Failed
**Error**: `Connection refused`
**Solution**:
```bash
# Check if PostgreSQL is running
docker ps | grep postgres
# Start PostgreSQL if needed
docker-compose up postgres-auth -d
```

#### 3. JWT Token Issues
**Error**: `Invalid token`
**Solution**:
- Check JWT secret in `application.yml`
- Ensure token is not expired
- Verify token format: `Bearer <token>`

#### 4. Service Discovery Issues
**Error**: `Service not found`
**Solution**:
- Ensure Eureka is running on port 8761
- Check service registration in Eureka dashboard
- Verify service name in `application.yml`

### Log Analysis

```bash
# View auth service logs
tail -f logs/auth-service.log

# Check for specific errors
grep "ERROR" logs/auth-service.log
```

---

## 📈 Performance Testing

### Load Testing with Apache Bench

```bash
# Test registration endpoint
ab -n 100 -c 10 -p register.json -T application/json http://localhost:8081/api/auth/register

# Test login endpoint
ab -n 100 -c 10 -p login.json -T application/json http://localhost:8081/api/auth/login
```

### Create test data files:

**register.json:**
```json
{
  "firstName": "Test",
  "lastName": "User",
  "email": "test@example.com",
  "password": "Test123!@#",
  "role": "PATIENT"
}
```

**login.json:**
```json
{
  "email": "test@example.com",
  "password": "Test123!@#"
}
```

---

## ✅ Test Checklist

- [ ] Health check endpoint responds
- [ ] User registration works
- [ ] User login works
- [ ] Token refresh works
- [ ] Invalid credentials are rejected
- [ ] Duplicate registration is prevented
- [ ] Password validation works
- [ ] Email validation works
- [ ] Role assignment works
- [ ] JWT tokens are valid
- [ ] Database records are created
- [ ] Service is registered with Eureka

---

## 🎯 Next Steps

After successful auth service testing:

1. **Implement User Service** - Patient profile management
2. **Test Service Integration** - Auth service with other services
3. **Implement Security** - Role-based access control
4. **Add Monitoring** - Metrics and health checks
5. **Performance Optimization** - Caching and optimization

---

## 📞 Support

If you encounter issues:
1. Check the logs for error messages
2. Verify all services are running
3. Check database connectivity
4. Ensure proper configuration
5. Review the troubleshooting section above 