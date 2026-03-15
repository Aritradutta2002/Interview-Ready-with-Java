# SOLID Principles

## S - Single Responsibility Principle (SRP)

**A class should have only one reason to change.**

```java
// ❌ Bad: Multiple responsibilities
class User {
    private String name;
    private String email;
    
    public void save() { /* Save to database */ }
    public void sendEmail() { /* Send email */ }
    public void generateReport() { /* Generate report */ }
}

// ✅ Good: Single responsibility
class User { private String name, email; }

class UserRepository { public void save(User user) { } }

class EmailService { public void sendEmail(User user) { } }

class UserReport { public void generate(User user) { } }
```

---

## O - Open/Closed Principle (OCP)

**Open for extension, closed for modification.**

```java
// ❌ Bad: Modify for each new shape
class AreaCalculator {
    public double calculate(Object shape) {
        if (shape instanceof Circle) { /* ... */ }
        else if (shape instanceof Rectangle) { /* ... */ }
        return 0;
    }
}

// ✅ Good: Extension via interface
interface Shape { double area(); }

class Circle implements Shape {
    double radius;
    public double area() { return Math.PI * radius * radius; }
}

class Rectangle implements Shape {
    double width, height;
    public double area() { return width * height; }
}

class AreaCalculator {
    public double calculate(Shape shape) { return shape.area(); }
}
```

---

## L - Liskov Substitution Principle (LSP)

**Derived classes must be substitutable for base classes.**

```java
// ❌ Bad: Penguin can't fly but Bird interface requires it
class Bird { public void fly() { } }
class Penguin extends Bird {
    public void fly() { throw new UnsupportedOperationException(); }
}

// ✅ Good: Proper hierarchy
interface Bird { void move(); }
interface FlyingBird extends Bird { void fly(); }

class Sparrow implements FlyingBird {
    public void fly() { }
    public void move() { fly(); }
}

class Penguin implements Bird {
    public void move() { /* walk/swim */ }
}
```

### Classic Violation: Square-Rectangle
```java
// ❌ Bad: Square extends Rectangle
Rectangle r = new Square();
r.setWidth(5);
r.setHeight(4);
// Expected area: 20, Actual: 16 (Square sets both to 4)

// ✅ Good: Separate implementations
interface Shape { int getArea(); }
class Rectangle implements Shape { }
class Square implements Shape { }
```

---

## I - Interface Segregation Principle (ISP)

**Clients shouldn't depend on interfaces they don't use.**

```java
// ❌ Bad: Fat interface
interface Worker {
    void work();
    void eat();
    void sleep();
}

class Robot implements Worker {
    public void work() { }
    public void eat() { throw new UnsupportedOperationException(); }
    public void sleep() { throw new UnsupportedOperationException(); }
}

// ✅ Good: Segregated interfaces
interface Workable { void work(); }
interface Feedable { void eat(); }
interface Sleepable { void sleep(); }

class Human implements Workable, Feedable, Sleepable { }

class Robot implements Workable { }  // Only what it needs
```

---

## D - Dependency Inversion Principle (DIP)

**Depend on abstractions, not concretions.**

```java
// ❌ Bad: High-level depends on low-level
class EmailService {
    private SMTPMailer mailer = new SMTPMailer();  // Concrete dependency
    
    public void send(String message) {
        mailer.sendMail(message);
    }
}

// ✅ Good: Depend on abstraction
interface Mailer { void sendMail(String message); }

class SMTPMailer implements Mailer { }

class SendGridMailer implements Mailer { }

class EmailService {
    private Mailer mailer;  // Abstraction
    
    // Dependency injection
    public EmailService(Mailer mailer) {
        this.mailer = mailer;
    }
    
    public void send(String message) {
        mailer.sendMail(message);
    }
}

// Usage
EmailService service = new EmailService(new SMTPMailer());
EmailService service = new EmailService(new SendGridMailer());
```

---

## SOLID Summary Table

| Principle | Meaning | Violation Sign |
|-----------|---------|----------------|
| **S**RP | One reason to change | Class doing too many things |
| **O**CP | Extend, don't modify | if-else/switch for types |
| **L**SP | Substitutable subclasses | Exceptions in overrides |
| **I**SP | Small, focused interfaces | UnsupportedOperationException |
| **D**IP | Depend on abstractions | `new` in constructors |

---

## Real-World Examples

### SRP: Spring MVC
```java
// Controller - handles HTTP
@Controller
class UserController {
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
}

// Service - business logic
@Service
class UserService {
    public User findById(Long id) { return repository.findById(id); }
}

// Repository - data access
@Repository
interface UserRepository extends JpaRepository<User, Long> { }
```

### OCP: Payment Processing
```java
interface PaymentProcessor { void process(Payment payment); }

class CreditCardProcessor implements PaymentProcessor { }
class PayPalProcessor implements PaymentProcessor { }
class StripeProcessor implements PaymentProcessor { }

// Add new processor without modifying existing code
class ApplePayProcessor implements PaymentProcessor { }
```

### DIP: Spring DI
```java
@Service
class OrderService {
    private final PaymentProcessor processor;
    
    @Autowired  // Constructor injection
    public OrderService(PaymentProcessor processor) {
        this.processor = processor;
    }
}
```

---

## Interview Q&A

**Q: What is SRP?**
A: A class should have only one reason to change. Each class should focus on one responsibility.

**Q: How does OCP differ from inheritance?**
A: OCP allows extension via new code, not modifying existing. Inheritance is one way to achieve it.

**Q: What is LSP violation example?**
A: Square extending Rectangle - Square changes behavior of setWidth/setHeight unexpectedly.

**Q: Why is ISP important?**
A: Prevents clients from being forced to implement methods they don't use. Reduces coupling.

**Q: How does DIP relate to DI?**
A: DIP is the principle (depend on abstractions). DI is the technique to achieve it (inject dependencies).

**Q: Can you have too many small interfaces (ISP)?**
A: Yes, over-segmentation can lead to interface explosion. Balance cohesion with size.

**Q: How does Spring framework support SOLID?**
A: SRP: @Controller/@Service/@Repository. OCP: Interfaces. DIP: Dependency Injection.
