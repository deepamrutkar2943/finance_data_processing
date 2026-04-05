# Finance Data Processing and Access Control Backend

A role-based Finance Data Processing and Access Control Backend dashboard built with Java Spring Boot and MySQL. The system supports financial record management, user role management, dashboard analytics, and JWT-based authentication with full access control enforcement.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.2.5 |
| Database | MySQL |
| ORM | Spring Data JPA / Hibernate |
| Security | Spring Security + JWT |
| Mapping | MapStruct |
| Validation | Jakarta Bean Validation |
| Documentation | Springdoc OpenAPI (Swagger UI) |
| Testing | JUnit 5 + Mockito |
| Build Tool | Maven |

---

## Project Structure

```
com.example.fdp.
├── model                        # JPA entities and enums
├── dto
│   ├── request                  # Incoming request payloads
│   └── response                 # Outgoing response payloads
├── repository                   # Spring Data JPA interfaces
├── mapper                       # Entity to DTO mapping
├── service
│   ├── service                  # Service interfaces
│   └── serviceImpl              # Business logic implementations
├── controller                   # REST API controllers
├── security                     # JWT filter, config, user details
├── exceptions
│   ├── custom                   # Custom exception classes
│   ├── handler                  # Global exception handler
│   └── response                 # Structured error response
├── config                       # OpenAPI configuration
└── constants                    # Application-wide constants
```

---

## Setup Instructions

### Prerequisites
- Java 21
- Maven 3.9+
- MySQL 8.0+

### Step 1 — Clone Repository
```bash
git clone <repository-url>
cd finance_data_processing
```

### Step 2 — Create Database
```sql
CREATE DATABASE finance_db;
```

### Step 3 — Configure application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/finance_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update
app.jwt.secret=your_jwt_secret_key
app.jwt.expiration=86400000
```

### Step 4 — Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

### Step 5 — Insert Initial Admin User
```sql
INSERT INTO users (name, email, password, role, status, created_at, updated_at)
VALUES (
    'Admin User',
    'admin@gmail.com',
    '$2a$10$X3v2.P8E0wG.QDF0EiWLOewouqunTKdiLRQ/shelptJEWc5G4/p1O',
    'ADMIN',
    'ACTIVE',
    NOW(),
    NOW()
);
```
Default password is `password123`.

### Step 6 — Access Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```

---

## Data Model

### Users Table
| Field | Type | Description |
|---|---|---|
| id | BIGINT (PK) | Auto increment |
| name | VARCHAR | Full name |
| email | VARCHAR | Unique login email |
| password | VARCHAR | BCrypt hashed |
| role | ENUM | VIEWER, ANALYST, ADMIN |
| status | ENUM | ACTIVE, INACTIVE |
| created_at | TIMESTAMP | Auto set on creation |
| updated_at | TIMESTAMP | Auto set on update |

### Financial Records Table
| Field | Type | Description |
|---|---|---|
| id | BIGINT (PK) | Auto increment |
| amount | DECIMAL(15,2) | Must be positive |
| type | ENUM | INCOME, EXPENSE |
| category | VARCHAR | e.g. Salary, Rent |
| date | DATE | Transaction date |
| notes | TEXT | Optional description |
| created_by | BIGINT (FK) | References users.id |
| created_at | TIMESTAMP | Auto set on creation |
| updated_at | TIMESTAMP | Auto set on update |

---

## Role Based Access Control

| Action | VIEWER | ANALYST | ADMIN |
|---|:---:|:---:|:---:|
| Login | ✅ | ✅ | ✅ |
| View financial records | ✅ | ✅ | ✅ |
| Filter financial records | ✅ | ✅ | ✅ |
| View dashboard summary | ✅ | ✅ | ✅ |
| View dashboard insights | ❌ | ✅ | ✅ |
| Create financial records | ❌ | ❌ | ✅ |
| Update financial records | ❌ | ❌ | ✅ |
| Delete financial records | ❌ | ❌ | ✅ |
| Create users | ❌ | ❌ | ✅ |
| View all users | ❌ | ❌ | ✅ |
| Update user role | ❌ | ❌ | ✅ |
| Update user status | ❌ | ❌ | ✅ |

Access control is enforced at the controller level using Spring Security's `@PreAuthorize` annotation with method-level security enabled via `@EnableMethodSecurity`.

---

## API Endpoints

### Authentication
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | /api/auth/login | Login and get JWT token | Public |

**Login Request:**
```json
{
    "email": "admin@gmail.com",
    "password": "password123"
}
```

**Login Response:**
```json
{
    "token": "eyJhbGci...",
    "name": "Admin User",
    "email": "admin@gmail.com",
    "role": "ADMIN"
}
```

---

### User Management
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | /api/users | Create new user | ADMIN |
| GET | /api/users | Get all users | ADMIN |
| GET | /api/users/{id} | Get user by id | ADMIN |
| PUT | /api/users/{id}/role | Update user role | ADMIN |
| PUT | /api/users/{id}/status | Update user status | ADMIN |

**Create User Request:**
```json
{
    "name": "John Doe",
    "email": "john@gmail.com",
    "password": "password123",
    "role": "VIEWER"
}
```

**Update Role Request:**
```json
{
    "role": "ANALYST"
}
```

**Update Status Request:**
```json
{
    "status": "INACTIVE"
}
```

---

### Financial Records
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | /api/records | Create record | ADMIN |
| GET | /api/records | Get all records | ALL |
| GET | /api/records/{id} | Get record by id | ALL |
| PUT | /api/records/{id} | Update record | ADMIN |
| DELETE | /api/records/{id} | Delete record | ADMIN |
| GET | /api/records/filter | Filter records | ALL |

**Create Record Request:**
```json
{
    "amount": 50000.00,
    "type": "INCOME",
    "category": "Salary",
    "date": "2024-01-01",
    "notes": "Monthly salary"
}
```

**Filter Options:**
```
GET /api/records/filter?type=INCOME
GET /api/records/filter?category=Salary
GET /api/records/filter?startDate=2024-01-01&endDate=2024-12-31
GET /api/records/filter?type=INCOME&startDate=2024-01-01&endDate=2024-12-31
GET /api/records/filter?category=Salary&startDate=2024-01-01&endDate=2024-12-31
```

---

### Dashboard
| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | /api/dashboard/summary | Total income, expenses, net balance, recent records | ALL |
| GET | /api/dashboard/insights | Category wise totals, monthly trends | ANALYST, ADMIN |

**Summary Response:**
```json
{
    "totalIncome": 50000.00,
    "totalExpenses": 10000.00,
    "netBalance": 40000.00,
    "recentRecords": [...]
}
```

**Insights Response:**
```json
{
    "categoryWiseTotals": {
        "Salary": 50000.00,
        "Rent": 10000.00
    },
    "monthlyTrends": {
        "2024-01": 60000.00,
        "2024-02": 45000.00
    }
}
```

---

## Error Handling

All errors return a structured JSON response:

```json
{
    "status": 404,
    "error": "User Not Found",
    "message": "No user exists with id 99",
    "timestamp": "2024-01-01T10:00:00"
}
```

| HTTP Status | When |
|---|---|
| 400 | Invalid input or duplicate email |
| 401 | Invalid credentials or inactive user |
| 403 | Insufficient role permissions |
| 404 | Resource not found |
| 500 | Unexpected server error |

Validation errors return field level details:
```json
{
    "email": "Invalid email format",
    "amount": "Amount must be positive"
}
```

---

## Authentication Flow

```
POST /api/auth/login
    → validates credentials
    → checks user status (ACTIVE/INACTIVE)
    → generates JWT token (valid 24 hours)
    → returns token + user details

Every subsequent request:
    → Authorization: Bearer <token> header required
    → JwtAuthFilter validates token
    → loads user from DB
    → sets role in SecurityContext
    → @PreAuthorize enforces role restrictions
```

---

## Additional Implementations

### 1. Swagger UI / API Documentation
Interactive API documentation available at `http://localhost:8080/swagger-ui/index.html`. Supports JWT authorization directly from the UI via the Authorize button.

### 2. Structured Error Responses
All errors return a consistent JSON structure with status code, error type, message, and timestamp — making it easy for frontend clients to handle errors uniformly.

### 3. Inactive User Blocking
Users with `INACTIVE` status are blocked from logging in even if credentials are correct.

### 4. Unit Tests with Centralized Test Runner
Service layer is covered with unit tests using JUnit 5 and Mockito covering success cases, not found cases, validation cases, and role restriction cases. All tests can be run from a single `AllTestsSuite` class using `@Suite` and `@SelectPackages` — no need to run each test class individually.

### 5. Separation Of Concerns
Dedicated mapper classes (`UserMapper`, `FinancialRecordMapper`) handle all entity to DTO conversion, keeping service layer focused purely on business logic.

### 6. Constructor Based Dependency Injection
All dependencies are injected via constructor injection — the approach recommended by the Spring team — instead of field level `@Autowired`. This makes dependencies explicit, fields immutable, and classes easier to unit test.

---

## Assumptions and Tradeoffs

| Decision | Reason |
|---|---|
| Role stored directly on User entity | Fixed role model is sufficient for this use case. A separate role table would add complexity without benefit here. |
| ADMIN is the only role that can create records | Financial records in a real system should be controlled — only admins should be able to add or modify financial data. |
| JWT expiration set to 24 hours | Balances security and usability for a dashboard system. |
| `BigDecimal` used for amount | Avoids floating point precision issues common with `Double` — critical for financial data. |
| `LocalDate` used for transaction date | Transaction date is a business date, not a timestamp. `createdAt` is the system timestamp. |
| Soft delete not implemented | Hard delete is used for simplicity. Soft delete would be a recommended enhancement for production. |
| Amount must always be positive | Sign of the transaction is determined by `type` (INCOME/EXPENSE), not the amount value. |

---

## Running Tests

```bash
# Run all unit tests
mvn test

# Or right click AllTestsSuite.java in STS
# → Run As → JUnit Test
```

Test coverage includes:
- `AuthServiceImplTest` — login success, inactive user, bad credentials, user not found
- `UserServiceImplTest` — create user, duplicate email, get by id, update role, update status
- `FinancialRecordServiceImplTest` — create, read, update, delete, filter by type/category/date
- `DashboardServiceImplTest` — summary calculations, insights, empty data cases
