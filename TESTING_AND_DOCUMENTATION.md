# DNF Backend - Security Implementation Testing & Documentation

## Overview
This document covers all security enhancements implemented in Punto 2 and Punto 3:
- **Punto 2.1-2.3**: Input validation, error handling, account security
- **Punto 2.4**: Login lockout mechanism (max 5 attempts in 15 minutes)
- **Punto 2.5**: Login audit trail logging
- **Punto 3.1-3.6**: Authorization, API response standardization, CORS, audit logging

---

## Architecture Changes

### New Entities & Tables

#### 1. `LoginAttempt` Table
**Purpose**: Tracks all login attempts (successful and failed)  
**Auto-created by Hibernate** when service starts

```
login_attempts {
  id (BIGSERIAL PRIMARY KEY)
  usuario_id (BIGINT NOT NULL, FK to usuarios)
  intento_exitoso (BOOLEAN DEFAULT false)
  fecha_intento (TIMESTAMP DEFAULT CURRENT_TIMESTAMP)
  direccion_ip (VARCHAR 45)
  razon_fallo (VARCHAR 255)
}
```

#### 2. Modified `Usuario` Table
**New Fields Added**:
- `activo` (BOOLEAN DEFAULT true) - Account lockout flag
- `intentos_fallidos` (INTEGER DEFAULT 0) - Failed attempt counter

---

## API Endpoints

### Authentication Endpoints (Public)

#### 1. **Register User**
```
POST /api/usuarios
Content-Type: application/json

Request Body:
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePass123!",
  "rol": "USER",
  "avatar": "https://avatar-url.jpg"
}

Response (201 Created):
{
  "success": true,
  "code": 201,
  "message": "Usuario registrado exitosamente",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "rol": "USER",
    "avatar": "https://avatar-url.jpg"
  },
  "timestamp": "2026-06-09T12:00:00"
}

Error Cases:
- 400 Bad Request: Email already exists (ConflictException)
- 400 Bad Request: Weak password
- 400 Bad Request: Validation errors (invalid email format, missing fields, etc.)
```

**Password Requirements**:
- Minimum 8 characters
- At least one uppercase letter (A-Z)
- At least one lowercase letter (a-z)
- At least one digit (0-9)
- At least one special character (@$!%*?&)

Example valid passwords:
- `SecurePass123!`
- `MyApp@2024`
- `TestPassword#99`

#### 2. **Login User**
```
POST /api/usuarios/login
Content-Type: application/json

Request Body:
{
  "email": "john@example.com",
  "password": "SecurePass123!"
}

Response (200 OK):
{
  "success": true,
  "code": 200,
  "message": "Login exitoso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "usuario": {
      "id": 1,
      "name": "John Doe",
      "email": "john@example.com",
      "rol": "USER",
      "avatar": "https://avatar-url.jpg"
    }
  },
  "timestamp": "2026-06-09T12:00:00"
}

Error Cases:
- 401 Unauthorized: Invalid credentials
- 423 Locked: Account locked due to too many failed attempts
  Response: {
    "success": false,
    "code": 423,
    "message": "Cuenta bloqueada por múltiples intentos fallidos (Desbloqueo disponible en 12 minutos)"
  }
- 404 Not Found: User with email not found
```

**Lockout Mechanism**:
- Triggered after 5 failed login attempts within 15 minutes
- Automatic temporary lockout for 15 minutes
- Automatically unlocks after 15 minutes
- Each successful login resets the failure counter

---

### User Management Endpoints (Protected)

#### 3. **List All Users**
```
GET /api/usuarios
Authorization: Bearer <JWT_TOKEN>

Response (200 OK):
{
  "success": true,
  "code": 200,
  "message": "Usuarios obtenidos exitosamente",
  "data": [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john@example.com",
      "rol": "USER",
      "avatar": "https://avatar-url.jpg"
    },
    ...
  ],
  "timestamp": "2026-06-09T12:00:00"
}

Access Control:
- @PreAuthorize("isAuthenticated()") - Any authenticated user
```

#### 4. **Get User by ID**
```
GET /api/usuarios/{userId}
Authorization: Bearer <JWT_TOKEN>

Response (200 OK):
{
  "success": true,
  "code": 200,
  "message": "Usuario obtenido exitosamente",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "rol": "USER",
    "avatar": "https://avatar-url.jpg"
  },
  "timestamp": "2026-06-09T12:00:00"
}

Error Cases:
- 401 Unauthorized: Missing or invalid token
- 404 Not Found: User not found

Access Control:
- @PreAuthorize("isAuthenticated()") - Any authenticated user
```

#### 5. **Get Login Audit History**
```
GET /api/usuarios/{userId}/login-audit?limit=50
Authorization: Bearer <JWT_TOKEN>

Query Parameters:
- limit (optional, default: 50) - Maximum number of records to return

Response (200 OK):
{
  "success": true,
  "code": 200,
  "message": "Historial de login obtenido exitosamente",
  "data": [
    {
      "id": 101,
      "usuarioId": 1,
      "intentoExitoso": false,
      "fechaIntento": "2026-06-09T11:55:30",
      "direccionIp": "192.168.1.100",
      "razonFallo": "Contraseña incorrecta"
    },
    {
      "id": 102,
      "usuarioId": 1,
      "intentoExitoso": false,
      "fechaIntento": "2026-06-09T11:56:15",
      "direccionIp": "192.168.1.100",
      "razonFallo": "Contraseña incorrecta"
    },
    {
      "id": 103,
      "usuarioId": 1,
      "intentoExitoso": true,
      "fechaIntento": "2026-06-09T12:00:00",
      "direccionIp": "192.168.1.100",
      "razonFallo": null
    }
  ],
  "timestamp": "2026-06-09T12:00:00"
}

Access Control:
- @PreAuthorize("isAuthenticated()") - Any authenticated user
- Note: Authorization check should be added to restrict access to own history + ADMIN override
```

#### 6. **Delete User**
```
DELETE /api/usuarios/{userId}
Authorization: Bearer <JWT_TOKEN>

Response (200 OK):
{
  "success": true,
  "code": 200,
  "message": "Usuario eliminado exitosamente",
  "data": null,
  "timestamp": "2026-06-09T12:00:00"
}

Error Cases:
- 401 Unauthorized: Missing or invalid token
- 403 Forbidden: User is not ADMIN
- 404 Not Found: User not found

Access Control:
- @PreAuthorize("hasRole('ADMIN')") - Only administrators
```

---

## Error Handling

### Standard Error Response Format
```json
{
  "success": false,
  "code": 400,
  "message": "Error description",
  "data": null,
  "timestamp": "2026-06-09T12:00:00"
}
```

### HTTP Status Codes & Exception Mapping

| HTTP Code | Exception | Scenario |
|-----------|-----------|----------|
| 400 | BadRequest | Validation errors, malformed request |
| 401 | Unauthorized | Invalid credentials, invalid token |
| 403 | Forbidden | Insufficient permissions/role |
| 404 | NotFound | Resource not found |
| 409 | Conflict | Email already registered |
| 423 | Locked | Account locked due to failed attempts |
| 500 | InternalServerError | Unexpected server error |

### Exception Classes
1. **UsuarioException** (400) - General user-related errors
2. **UnauthorizedException** (401) - Authentication failures
3. **ResourceNotFoundException** (404) - Entity not found
4. **ConflictException** (409) - Duplicate resources
5. **AccountLockedException** (423) - Account locked temporarily
6. **MethodArgumentNotValidException** (400) - Validation errors (auto-handled by Spring)

---

## Security Features Implemented

### 1. Input Validation (@Valid)
All DTOs use Jakarta Bean Validation:
- `@NotBlank` - Required fields
- `@Email` - Email format validation
- `@Size` - Length constraints
- `@Pattern` - Custom regex patterns
- `@EmailUnique` - Custom validator (database lookup)
- `@StrongPassword` - Custom validator (complexity rules)

### 2. Password Encryption
- Passwords encoded using `BCryptPasswordEncoder`
- Never returned in API responses
- Salt/hash automatically applied

### 3. JWT Authentication
- Algorithm: HS256
- Secret: From environment variable `JWT_SECRET`
- Expiration: 10 hours
- Claims: Email, issued time, expiration
- Used via `Authorization: Bearer <token>` header

### 4. Authorization (@PreAuthorize)
Spring Security method-level authorization:
```java
@PreAuthorize("isAuthenticated()")        // Any authenticated user
@PreAuthorize("hasRole('ADMIN')")         // Only ADMIN role
@PreAuthorize("hasRole('MODERATOR')")     // Only MODERATOR role
@PreAuthorize("hasAnyRole('ADMIN','MODERATOR')") // Multiple roles
```

### 5. Account Lockout (Brute Force Protection)
- **Threshold**: 5 failed attempts
- **Time Window**: 15 minutes
- **Lockout Duration**: 15 minutes (automatic)
- **Auto-unlock**: After 15 minutes have passed
- **Tracking**: All attempts logged with IP address

### 6. Login Audit Trail
- Captures: Timestamp, IP address, success/failure status, failure reason
- Accessible via: `GET /api/usuarios/{userId}/login-audit`
- Retention: Indefinite (database records)
- Usage: Security analysis, forensics, compliance

### 7. CORS Configuration
```
Allowed Origins:
  - http://localhost:3000 (Frontend development)
  - http://localhost:8080 (Internal)

Allowed Methods: GET, POST, PUT, DELETE, OPTIONS, PATCH

Allowed Headers: * (all headers)

Credentials: true (cookies/auth headers allowed)

Max Age: 3600 seconds (1 hour)
```

### 8. Audit Interceptor
Request/response logging with:
- HTTP method, path, parameters
- Authenticated user (or ANONYMOUS)
- Client IP address
- Response status
- Request duration (milliseconds)
- Exception details (if any)

---

## Testing Guide

### Prerequisites
1. Database running: `docker compose -f docker-compose-db.yml up`
2. Ms-usuario service running locally or in Docker
3. JWT_SECRET environment variable set
4. Tools: curl, Postman, or similar HTTP client

### Test Scenarios

#### Scenario 1: User Registration
```bash
# Valid registration
curl -X POST http://localhost:8082/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "testuser@example.com",
    "password": "SecurePass123!",
    "rol": "USER",
    "avatar": "https://example.com/avatar.jpg"
  }'

# Expected: 201 Created

# Duplicate email
curl -X POST http://localhost:8082/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Another User",
    "email": "testuser@example.com",
    "password": "AnotherPass123!",
    "rol": "USER",
    "avatar": "https://example.com/avatar2.jpg"
  }'

# Expected: 409 Conflict

# Weak password (missing special char)
curl -X POST http://localhost:8082/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User 2",
    "email": "test2@example.com",
    "password": "WeakPass123",
    "rol": "USER",
    "avatar": ""
  }'

# Expected: 400 Bad Request
```

#### Scenario 2: Successful Login
```bash
curl -X POST http://localhost:8082/api/usuarios/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testuser@example.com",
    "password": "SecurePass123!"
  }'

# Expected: 200 OK with JWT token in response
# Save the token for subsequent requests:
# TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Scenario 3: Failed Login with Lockout
```bash
# Attempt 1 - Wrong password
curl -X POST http://localhost:8082/api/usuarios/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testuser@example.com",
    "password": "WrongPassword123!"
  }'
# Expected: 401 Unauthorized

# Repeat 4 more times (attempts 2-5)
# On attempt 5, response should still be 401

# Attempt 6+ within 15 minutes
curl -X POST http://localhost:8082/api/usuarios/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testuser@example.com",
    "password": "SecurePass123!"
  }'
# Expected: 423 Locked with lockout message
```

#### Scenario 4: Protected Endpoint Access
```bash
# Without token (should fail)
curl http://localhost:8082/api/usuarios

# Expected: 401 Unauthorized

# With token (should succeed)
TOKEN="<JWT_TOKEN_FROM_LOGIN>"
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8082/api/usuarios

# Expected: 200 OK with list of users
```

#### Scenario 5: Login Audit History
```bash
TOKEN="<JWT_TOKEN_FROM_LOGIN>"
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8082/api/usuarios/1/login-audit?limit=10"

# Expected: 200 OK with login attempt history
# Shows successful login at the end (contador reset)
```

---

## Configuration Files

### Environment Variables (.env)
```
JWT_SECRET=your-secret-key-change-this-in-production
DB_USERNAME=admin
DB_PASSWORD=adminpassword
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=app-specific-password
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
```

### Database Connection (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/dnf_db
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
```

### JWT Configuration
- Algorithm: HS256
- Secret: ${JWT_SECRET} (environment variable)
- Expiration: 10 hours (36,000,000 ms)
- Token format: `Authorization: Bearer <token>`

---

## Deployment Checklist

- [ ] All services compile successfully
- [ ] Database migrations applied
- [ ] Environment variables configured (.env)
- [ ] JWT_SECRET is strong and unique
- [ ] CORS origins configured for production domain
- [ ] HTTPS enabled in production
- [ ] Database credentials changed from defaults
- [ ] Audit logs monitored for suspicious activity
- [ ] Test scenarios executed successfully
- [ ] Load testing completed for lockout mechanism
- [ ] Monitoring/alerting set up for failed login attempts

---

## Known Limitations & Future Enhancements

### Current Limitations
1. Lockout is temporary only (no permanent suspension)
2. Email-based account recovery not implemented
3. Two-factor authentication (2FA) not implemented
4. Password reset/change endpoint not implemented
5. Rate limiting at API gateway not implemented
6. IP blacklist/whitelist not implemented

### Recommended Future Enhancements
1. Implement email-based password recovery
2. Add 2FA support (TOTP/SMS)
3. Implement rate limiting at Gateway level
4. Add IP reputation checking
5. Implement role-based access control (RBAC) policies
6. Add audit log retention policies
7. Implement user session management
8. Add login notifications/alerts to users
9. Implement API key authentication for service-to-service calls
10. Add encryption for sensitive fields in database

---

## Support & Troubleshooting

### Common Issues

**Issue**: 401 Unauthorized on protected endpoints  
**Solution**: 
- Verify JWT token is included in Authorization header
- Check token hasn't expired (10 hours)
- Verify JWT_SECRET is same on server

**Issue**: 409 Conflict on registration  
**Solution**: 
- Email already exists in database
- Use a unique email address

**Issue**: 423 Account Locked  
**Solution**: 
- Wait 15 minutes for automatic unlock
- Or use correct password to prevent future failures
- Check `login-audit` endpoint to see failure history

**Issue**: Validation errors (400 Bad Request)  
**Solution**: 
- Check password meets complexity requirements
- Verify email format is valid
- Ensure all required fields are provided
- Check request Content-Type is application/json

---

## File References

### Ms-usuario Security Implementation

| Component | File | Line | Purpose |
|-----------|------|------|---------|
| DTO Validation | `dto/RegistroRequest.java` | — | Registration input validation |
| Password Validation | `validator/StrongPasswordValidator.java` | — | Custom password strength check |
| Email Uniqueness | `validator/EmailUniqueValidator.java` | — | Database email uniqueness |
| Exception Handling | `exception/GlobalExceptionHandler.java` | — | Centralized error responses |
| Account Lockout | `service/AccountLockService.java` | — | Brute force protection |
| Login Tracking | `model/LoginAttempt.java` | — | Audit trail entity |
| Security Config | `security/SecurityConfig.java` | — | JWT + CORS + @PreAuthorize |
| Auth Filter | `security/AuthenticationFilter.java` | — | JWT token validation |
| Audit Logging | `interceptor/AuditInterceptor.java` | — | Request/response logging |

---

## Document Version
- **Version**: 1.0
- **Last Updated**: 2026-06-09
- **Status**: Initial implementation complete
