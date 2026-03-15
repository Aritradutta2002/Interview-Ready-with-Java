# Spring & Spring Boot Quick Reference

## 🔥 Most Asked Topics

| Topic | Frequency | Key Points |
|-------|-----------|------------|
| Dependency Injection | ⭐⭐⭐⭐⭐ | IoC container, @Autowired, constructor injection |
| Spring Boot Auto-config | ⭐⭐⭐⭐⭐ | @SpringBootApplication, @EnableAutoConfiguration |
| REST Controllers | ⭐⭐⭐⭐⭐ | @RestController, @RequestMapping, @PathVariable |
| Spring Data JPA | ⭐⭐⭐⭐⭐ | JpaRepository, query methods, @Transactional |
| Spring Security | ⭐⭐⭐⭐ | Authentication, Authorization, JWT, OAuth2 |
| AOP | ⭐⭐⭐⭐ | @Aspect, @Before, @After, cross-cutting concerns |
| Bean Scopes | ⭐⭐⭐ | Singleton, Prototype, Request, Session |

---

## 📊 Comparison Tables

### Dependency Injection Types

| Type | Annotation | Use Case | Pros | Cons |
|------|-----------|----------|------|------|
| Constructor | @Autowired (optional) | Mandatory dependencies | Immutable, testable | Verbose for many deps |
| Setter | @Autowired | Optional dependencies | Flexible | Mutable state |
| Field | @Autowired | Quick prototyping | Concise | Hard to test, breaks encapsulation |

### Bean Scopes

| Scope | Lifecycle | Use Case |
|-------|-----------|----------|
| Singleton | One instance per container | Stateless services |
| Prototype | New instance per request | Stateful objects |
| Request | One per HTTP request | Web request data |
| Session | One per HTTP session | User session data |

### Spring Boot vs Spring

| Aspect | Spring | Spring Boot |
|--------|--------|-------------|
| Configuration | XML or Java config | Auto-configuration |
| Dependencies | Manual management | Starter dependencies |
| Embedded Server | External server needed | Embedded Tomcat/Jetty |
| Production Ready | Manual setup | Actuator built-in |

---

## 🎯 Checklist

### Core Spring
- [ ] Understand IoC and DI
- [ ] Know bean lifecycle
- [ ] Understand AOP concepts
- [ ] Know different bean scopes

### Spring Boot
- [ ] Understand auto-configuration
- [ ] Know starter dependencies
- [ ] Understand application properties
- [ ] Know Spring Boot Actuator

### Spring MVC
- [ ] Build REST APIs
- [ ] Handle exceptions globally
- [ ] Validate request data
- [ ] Understand content negotiation

### Spring Data JPA
- [ ] Use JpaRepository
- [ ] Write custom queries
- [ ] Understand @Transactional
- [ ] Handle relationships

### Spring Security
- [ ] Configure authentication
- [ ] Implement authorization
- [ ] Use JWT tokens
- [ ] Understand OAuth2 flows

---

## ⚡ Code Snippets

### Basic REST Controller
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.save(user));
    }
}
```

### Spring Data JPA Repository
```java
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByLastName(String lastName);
    
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findByEmail(String email);
}
```

### Transaction Management
```java
@Service
public class UserService {
    
    @Transactional
    public void transferMoney(Long fromId, Long toId, BigDecimal amount) {
        // Both operations in same transaction
        accountRepository.debit(fromId, amount);
        accountRepository.credit(toId, amount);
    }
}
```

### Global Exception Handler
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(ex.getMessage()));
    }
}
```

---

## 📝 Common Interview Questions

1. **Q**: What is Dependency Injection?
   **A**: Design pattern where objects receive dependencies from external source rather than creating them

2. **Q**: Difference between @Component, @Service, @Repository?
   **A**: All create beans; @Service for business logic, @Repository for data access with exception translation

3. **Q**: How does Spring Boot auto-configuration work?
   **A**: Uses @Conditional annotations to configure beans based on classpath and properties

4. **Q**: What is @Transactional propagation?
   **A**: Defines how transactions relate to each other (REQUIRED, REQUIRES_NEW, NESTED, etc.)

5. **Q**: Difference between @RestController and @Controller?
   **A**: @RestController = @Controller + @ResponseBody, returns data directly

---

*Quick reference for last-minute review*
*Spring Boot 3.x | Spring Framework 6.x | Java 17+*
