# AGENTS.md - Developer Guidelines for E-commerce Project

## Project Overview
- **Type**: Spring Boot 4.0.1 Java 17 Maven Application
- **Stack**: JPA/Hibernate, Spring Security, Lombok, MariaDB/MySQL, JWT
- **Package Structure**:
  - `controller/` - REST endpoints
  - `service/` - Service interfaces
  - `impl/` - Service implementations
  - `repository/` - Data access layer
  - `model/` - JPA entities
  - `request/` - Request DTOs
  - `response/` - Response DTOs
  - `config/` - Configuration classes
  - `domain/` - Enums
  - `exceptions/` - Custom exceptions

---

## Build & Test Commands

### Maven Wrapper (Recommended)
```bash
./mvnw clean install      # Build project
./mvnw test               # Run all tests
./mvnw test -Dtest=EcommerceApplicationTests#contextLoads  # Single test method
./mvnw test -Dtest=EcommerceApplicationTests              # Single test class
./mvnw spring-boot:run   # Run application
./mvnw clean              # Clean target directory
```

### Standard Maven
```bash
mvn clean install
mvn test
mvn test -Dtest=TestClassName#testMethod
mvn spring-boot:run
```

### IDE Integration
- Tests run via JUnit 5 in IDE (IntelliJ/Eclipse)
- Use Spring Boot DevTools for hot reload during development

---

## Code Style Guidelines

### Naming Conventions
- **Classes/Interfaces**: PascalCase (e.g., `ProductService`, `UserController`)
- **Methods**: camelCase (e.g., `findProductById`, `createProduct`)
- **Variables**: camelCase (e.g., `productId`, `sellerRepository`)
- **Packages**: lowercase with underscores avoided (e.g., `com.penguinshop.model`)
- **Enums**: SCREAMING_SNAKE_CASE (e.g., `USER_ROLE`, `ORDER_STATUS`, `PAYMENT_STATUS`)
- **Constants**: SCREAMING_SNAKE_CASE

### Project Structure
```
src/
├── main/java/com/penguinshop/
│   ├── controller/   # @RestController classes
│   ├── service/    # Service interfaces
│   ├── impl/       # @Service implementations
│   ├── repository/ # JpaRepository interfaces
│   ├── model/      # @Entity classes
│   ├── request/    # Request DTOs
│   ├── response/   # Response DTOs
│   ├── config/     # @Configuration classes
│   ├── domain/     # Enums
│   └── exceptions/ # Custom exception classes
└── test/java/      # Test classes
```

### Service Layer Pattern
Follow the interface + implementation pattern:
- Interface in `service/` package (e.g., `ProductService.java`)
- Implementation in `impl/` package with `@Service` (e.g., `ProductServiceImpl.java`)
- Use constructor injection via `@RequiredArgsConstructor` (Lombok)

### Lombok Usage
Required annotations:
```java
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ClassName { }
```

Service classes:
```java
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
}
```

### Imports Organization
Order imports by category (no static import ordering enforced):
1. Java standard library (`java.*`)
2. Spring framework (`org.springframework.*`)
3. Third-party libraries (`com.fasterxml.*`, `jakarta.*`, `io.jsonwebtoken.*`)
4. Project imports (`com.penguinshop.*`)

### Entity/Model Conventions
- Use `@Id` with `@GeneratedValue(strategy = GenerationType.AUTO)`
- Use `@Column(unique = true, nullable = false)` for constrained fields
- Use `@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)` for sensitive fields
- Use `@JsonIgnore` to prevent circular references
- Initialize collections: `private Set<Address> addresses = new HashSet<>();`
- Default values set in field declaration: `private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;`

### Exception Handling
Custom exceptions extend `RuntimeException`:
```java
public class ProductException extends RuntimeException {
    public ProductException(String message) {
        super(message);
    }
}
```

Use `@ControllerAdvice` for global exception handling:
```java
@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorDetails> productExceptionHandler(ProductException se, WebRequest req) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError(se.getMessage());
        errorDetails.setDetails(req.getDescription(false));
        errorDetails.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
```

Throw exceptions with meaningful messages:
```java
return productRepository.findById(productId)
    .orElseThrow(() -> new ProductException("Product not found with id " + productId));
```

### REST Controller Conventions
```java
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req) throws Exception {
        AuthResponse res = authService.signIn(req);
        return ResponseEntity.ok(res);
    }
}
```

### DTO Conventions
- Request DTOs in `request/` package
- Response DTOs in `response/` package
- Use Lombok annotations for getters/setters
- Validate inputs using Jakarta Validation (`@NotNull`, `@Email`, etc.)

### Testing
- Test classes in `src/test/java/com/penguinshop/`
- Follow naming: `ClassNameTests.java`
- Use `@SpringBootTest` for integration tests
- Use `@Test` from JUnit 5 (`org.junit.jupiter.api.Test`)

---

## Configuration

### Database Configuration
Database credentials stored in `.env` (not committed to version control):
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/ecommerce
spring.datasource.username=root
spring.datasource.password=your_password
```

### JWT Configuration
JWT constants in `config/JWT_CONSTANT.java`
JWT utilities in `config/JwtProvider.java`

---

## Notes
- No existing linting tools configured
- Follow existing code patterns for consistency
- Use comments to explain business logic (existing codebase has extensive inline comments)
