# Critical and Tricky OOP Interview Scenarios

> **The topics that separate good candidates from great candidates**

Advanced OOP interview questions frequently asked in senior-level and architecture interviews.

---

## Table of Contents

1. [Is Java 100% Object-Oriented?](#1-is-java-100-object-oriented)
2. [Polymorphism Without Inheritance](#2-polymorphism-without-inheritance)
3. [Null: The Billion-Dollar Mistake](#3-null-the-billion-dollar-mistake)
4. [The Problem with Getters and Setters](#4-the-problem-with-getters-and-setters)
5. [Singleton: Pattern or Anti-Pattern?](#5-singleton-pattern-or-anti-pattern)
6. [Object vs Class at Runtime](#6-object-vs-class-at-runtime)
7. [Anemic Domain Model](#7-anemic-domain-model)
8. [Early Binding vs Late Binding](#8-early-binding-vs-late-binding)
9. [Virtual Functions in Java](#9-virtual-functions-in-java)
10. [Destructor in Java](#10-destructor-in-java)
11. [Copy Constructor](#11-copy-constructor)
12. [Can Constructors Be Inherited?](#12-can-constructors-be-inherited)
13. [Can We Override Final Methods?](#13-can-we-override-final-methods)

---

## 1. Is Java 100% Object-Oriented?

### Short Answer: **NO**

### Why Java is NOT 100% OOP:

```
┌─────────────────────────────────────────────────────────────┐
│           Why Java Breaks Pure OOP                          │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. PRIMITIVE TYPES                                         │
│     int, char, boolean, double, etc.                        │
│     → Not objects, no methods, no inheritance             │
│                                                             │
│  2. STATIC MEMBERS                                          │
│     static methods/variables belong to class, not object    │
│     → No polymorphism, no overriding                      │
│                                                             │
│  3. NULL REFERENCES                                         │
│     null is not an object                                 │
│     → Breaks "everything is an object" rule               │
│                                                             │
│  4. WRAPPER AUTO-BOXING                                   │
│     int → Integer conversion                               │
│     → Primitives still exist underneath                    │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Detailed Arguments:

#### 1. Primitive Types
```java
// These are NOT objects
int x = 10;
char c = 'A';
boolean flag = true;

// You CANNOT do:
// x.toString();     // ❌ Compile error
// x.hashCode();     // ❌ Compile error
// x.getClass();     // ❌ Compile error

// Must use wrapper classes for object behavior
Integer objX = 10;  // Auto-boxing
objX.toString();      // ✅ Works
```

**Why it matters:** Pure OOP languages like Smalltalk have no primitives - everything is an object.

#### 2. Static Methods
```java
class Calculator {
    // Static method - belongs to class, not object
    public static int add(int a, int b) {
        return a + b;
    }
}

// Usage - no object needed!
int result = Calculator.add(5, 3);  // Direct class call

// This breaks OOP because:
// 1. No "this" reference
// 2. Cannot be overridden (method hiding instead)
// 3. No polymorphism
// 4. Procedural programming style
```

#### 3. Null References
```java
String s = null;
// null is not an object - it's a special reference value
// Calling any method on null throws NullPointerException
// s.length();  // ❌ NPE!
```

### Interview Q&A

**Q: If Java isn't 100% OOP, what percentage is it?**
A: It's a hybrid language - mostly OOP with procedural elements. The existence of primitives and static members makes it a practical compromise.

**Q: Which language IS 100% OOP?**
A: Smalltalk is considered pure OOP. Even Python has primitives (though they're objects).

**Q: Why did Java designers include primitives?**
A: **Performance**. Object overhead for every number would be too slow. Primitives are stack-allocated and faster.

---

## 2. Polymorphism Without Inheritance

### Short Answer: **YES, through Interfaces**

### How Java Achieves Polymorphism Without Inheritance:

```
┌─────────────────────────────────────────────────────────────┐
│     Polymorphism Through Interfaces                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   Interface: Drawable                                       │
│   ├── Circle implements Drawable                            │
│   ├── Rectangle implements Drawable                         │
│   └── Triangle implements Drawable                          │
│                                                             │
│   NO inheritance between Circle, Rectangle, Triangle        │
│   BUT they all respond to draw() polymorphically            │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Code Example:

```java
// Interface defines contract
interface Drawable {
    void draw();
}

// Unrelated classes - no inheritance between them
class Circle implements Drawable {
    @Override
    public void draw() {
        System.out.println("Drawing Circle");
    }
}

class Rectangle implements Drawable {
    @Override
    public void draw() {
        System.out.println("Drawing Rectangle");
    }
}

class Triangle implements Drawable {
    @Override
    public void draw() {
        System.out.println("Drawing Triangle");
    }
}

// Polymorphism WITHOUT inheritance!
public class PolymorphismDemo {
    public static void main(String[] args) {
        List<Drawable> shapes = Arrays.asList(
            new Circle(),
            new Rectangle(),
            new Triangle()
        );
        
        // Same method call, different behaviors
        for (Drawable shape : shapes) {
            shape.draw();  // Polymorphic behavior!
        }
    }
}
```

### Other Ways to Achieve Polymorphism Without Inheritance:

#### 1. Duck Typing (via Reflection)
```java
// If it walks like a duck and quacks like a duck...
class Duck {
    void quack() { System.out.println("Quack!"); }
}

class Person {
    void quack() { System.out.println("Person quacking!"); }
}

// Both can "quack" - polymorphic behavior without common inheritance
```

#### 2. Generics (Parametric Polymorphism)
```java
// Same method works with different types
public <T> void print(T item) {
    System.out.println(item);
}

print("Hello");     // Works with String
print(123);       // Works with Integer
print(3.14);      // Works with Double
```

#### 3. Composition
```java
// Behavior injected at runtime - no inheritance
interface PaymentStrategy {
    void pay(double amount);
}

class ShoppingCart {
    private PaymentStrategy paymentStrategy;  // Composition
    
    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }
    
    public void checkout(double amount) {
        paymentStrategy.pay(amount);  // Polymorphic behavior!
    }
}
```

### Interview Q&A

**Q: What's the difference between inheritance-based and interface-based polymorphism?**
A: Inheritance is "IS-A" relationship (tight coupling), interfaces are "CAN-DO" relationship (loose coupling).

**Q: Can you have polymorphism with neither inheritance nor interfaces?**
A: Yes - through generics (parametric polymorphism) or duck typing.

---

## 3. Null: The Billion-Dollar Mistake

### The Story

> **"I call it my billion-dollar mistake. It was the invention of the null reference in 1965."**
> — **Sir Tony Hoare**, inventor of null (2009)

### Why Null is a Problem:

```
┌─────────────────────────────────────────────────────────────┐
│           The Null Problem                                    │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. TYPE SAFETY VIOLATION                                   │
│     String s = null;                                        │
│     // Compiler thinks s is a String                        │
│     // But it's actually NOTHING                            │
│                                                             │
│  2. BILLION-DOLLAR COST                                     │
│     - NullPointerExceptions in production                   │
│     - Debugging time                                        │
│     - Defensive null checks everywhere                      │
│     - Security vulnerabilities                              │
│                                                             │
│  3. SEMANTIC AMBIGUITY                                      │
│     - "Not initialized yet"                                 │
│     - "No value found"                                      │
│     - "Error occurred"                                      │
│     - "Optional field"                                      │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### The Cost of Null:

```java
// Defensive programming - null checks everywhere!
public void processUser(User user) {
    if (user == null) return;  // ❌ Boilerplate!
    
    Address address = user.getAddress();
    if (address == null) return;  // ❌ More boilerplate!
    
    String city = address.getCity();
    if (city == null) return;  // ❌ Even more!
    
    // Finally do actual work
    System.out.println(city.toUpperCase());
}
```

### Java's Solution: Optional (Java 8+)

```java
// Before Java 8 - null hell
public String getCity(User user) {
    if (user != null) {
        Address address = user.getAddress();
        if (address != null) {
            return address.getCity();
        }
    }
    return "Unknown";
}

// After Java 8 - clean with Optional
public String getCity(User user) {
    return Optional.ofNullable(user)
        .map(User::getAddress)
        .map(Address::getCity)
        .orElse("Unknown");
}
```

### Better Alternatives:

```java
// 1. Null Object Pattern
interface Animal {
    void makeSound();
}

class Dog implements Animal {
    public void makeSound() { System.out.println("Woof!"); }
}

class NullAnimal implements Animal {
    public void makeSound() { /* Do nothing */ }  // Safe default
}

// 2. Factory that never returns null
class AnimalFactory {
    public static Animal create(String type) {
        switch (type) {
            case "dog": return new Dog();
            case "cat": return new Cat();
            default: return new NullAnimal();  // Never null!
        }
    }
}
```

### Interview Q&A

**Q: Why did Tony Hoare create null if it's so bad?**
A: It was a simple, easy implementation for "no value" - seemed like a good idea at the time.

**Q: How does Optional solve the null problem?**
A: It makes absence of value explicit in the type system - compiler forces you to handle it.

**Q: What's the Null Object Pattern?**
A: Instead of returning null, return a special object that does nothing/safe defaults.

---

## 4. The Problem with Getters and Setters

### The Argument: Getters/Setters are Procedural Code in OOP Clothing

```
┌─────────────────────────────────────────────────────────────┐
│     The Getters/Setters Problem                               │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ❌ BAD: Anemic Domain Model                                │
│  class BankAccount {                                        │
│      private double balance;                                │
│      public double getBalance() { return balance; }         │
│      public void setBalance(double b) { balance = b; }      │
│  }                                                          │
│                                                             │
│  // Client code - procedural style                          │
│  account.setBalance(account.getBalance() + 100);  // ❌     │
│                                                             │
│  ✅ GOOD: Rich Domain Model                                 │
│  class BankAccount {                                        │
│      private double balance;                                │
│      public void deposit(double amount) {                   │
│          if (amount > 0) balance += amount;               │
│      }                                                      │
│      public void withdraw(double amount) {                  │
│          if (amount <= balance) balance -= amount;          │
│      }                                                      │
│  }                                                          │
│                                                             │
│  // Client code - object-oriented                           │
│  account.deposit(100);  // ✅                               │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### The Problems:

#### 1. Encapsulation Leakage
```java
// ❌ Bad - exposes internal state
class Employee {
    private Date hireDate;
    
    public Date getHireDate() {
        return hireDate;  // ❌ Returns mutable reference!
    }
}

// Client can modify internal state!
Employee emp = new Employee();
Date date = emp.getHireDate();
date.setYear(2025);  // ❌ Modified employee's hire date!

// ✅ Good - defensive copy
public Date getHireDate() {
    return new Date(hireDate.getTime());  // Returns copy
}
```

#### 2. No Validation
```java
// ❌ Bad - direct setter
public void setAge(int age) {
    this.age = age;  // No validation!
}

// ✅ Good - validate in constructor/methods
public void setAge(int age) {
    if (age < 0 || age > 150) {
        throw new IllegalArgumentException("Invalid age");
    }
    this.age = age;
}
```

#### 3. Procedural Thinking
```java
// ❌ Procedural style with getters/setters
order.setStatus("SHIPPED");
order.setShippedDate(new Date());
order.setTrackingNumber("12345");

// ✅ Object-oriented - tell, don't ask
order.ship("12345");  // Method encapsulates all changes
```

### When ARE Getters/Setters Okay?

```java
// ✅ DTOs (Data Transfer Objects)
class UserDTO {
    private String name;
    private String email;
    // Getters/setters OK here - just data transport
}

// ✅ Configuration objects
class AppConfig {
    private String databaseUrl;
    // Getters/setters OK - just settings
}

// ✅ Framework requirements (Hibernate, Jackson, etc.)
@Entity
class User {
    // Getters/setters required by framework
}
```

### Interview Q&A

**Q: Should we never use getters/setters?**
A: Use them for DTOs and when frameworks require them. For domain objects, expose behavior instead.

**Q: What's "Tell, Don't Ask"?**
A: Instead of asking for data and making decisions, tell the object what to do. Let it handle the logic.

**Q: What's an Anemic Domain Model?**
A: Objects that are just data bags with no behavior - all logic is in service classes.

---

## 5. Singleton: Pattern or Anti-Pattern?

### The Debate

```
┌─────────────────────────────────────────────────────────────┐
│     Singleton: Pattern vs Anti-Pattern                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ✅ WHY IT'S A PATTERN                                      │
│  • Ensures single instance (DB connection, config)          │
│  • Global access point                                      │
│  • Lazy initialization                                      │
│  • Thread-safe with proper implementation                   │
│                                                             │
│  ❌ WHY IT'S AN ANTI-PATTERN                                │
│  • Hidden dependencies (hard to test)                       │
│  • Violates Single Responsibility Principle                   │
│  • Global state (hard to reason about)                      │
│  • Tight coupling                                           │
│  • Hard to mock in unit tests                               │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### The Problems:

#### 1. Hidden Dependencies
```java
// ❌ Bad - dependency hidden inside method
class OrderService {
    public void processOrder(Order order) {
        // Hidden dependency on Database singleton!
        Database.getInstance().save(order);  // ❌
    }
}

// ✅ Good - dependency injection
class OrderService {
    private final Database database;
    
    public OrderService(Database database) {  // Explicit dependency
        this.database = database;
    }
    
    public void processOrder(Order order) {
        database.save(order);  // ✅ Testable
    }
}
```

#### 2. Hard to Test
```java
// ❌ Bad - can't mock Singleton
@Test
void testOrderService() {
    OrderService service = new OrderService();
    service.processOrder(order);  // Uses real database!
}

// ✅ Good - inject mock
@Test
void testOrderService() {
    Database mockDb = mock(Database.class);
    OrderService service = new OrderService(mockDb);
    service.processOrder(order);  // Uses mock
    verify(mockDb).save(order);   // Verify behavior
}
```

### When Singleton IS Appropriate
- Logging frameworks
- Configuration management
- Connection pools (managed by frameworks)
- Use **Dependency Injection** (Spring) instead of manual Singleton

### Interview Q&A

**Q: How to break Singleton?**
A: Reflection, serialization, cloning. Enum Singleton prevents all three.

**Q: Why prefer enum Singleton?**
A: Thread-safe by default, prevents reflection attack, handles serialization automatically.

---

## 6. Object vs Class at Runtime

### How JVM Represents Objects

```
┌─────────────────────────────────────────────────────────────┐
│           Object in Memory                                    │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌──────────────┐                                          │
│  │ Object Header │  - Mark Word (hashCode, GC age, locks)  │
│  │  (12-16 bytes)│  - Klass Pointer (→ Class metadata)     │
│  ├──────────────┤                                          │
│  │ Instance Data │  - Fields (int, String, etc.)            │
│  │              │  - Ordered by size (longs, ints, shorts) │
│  ├──────────────┤                                          │
│  │   Padding    │  - Align to 8-byte boundary              │
│  └──────────────┘                                          │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

```java
// getClass() returns runtime class
Object obj = new String("Hello");
Class<?> clazz = obj.getClass();  // java.lang.String (runtime type)

// instanceof checks IS-A relationship
obj instanceof String;    // true
obj instanceof Object;    // true
obj instanceof Integer;   // false

// Class object is itself an Object
Class<?> c = String.class;
c instanceof Object;      // true
c instanceof Class;       // true
```

### Interview Q&A

**Q: Difference between getClass() and instanceof?**
A: `getClass()` returns exact runtime class. `instanceof` checks IS-A (includes parent types).

**Q: How many Class objects for one class?**
A: One per ClassLoader. Same class loaded by different ClassLoaders = different Class objects.

---

## 7. Anemic Domain Model

### What is it?
Objects that are just data containers with no behavior — all logic is in service classes.

```java
// ❌ Anemic Domain Model
class Employee {
    private String name;
    private double salary;
    // Only getters and setters — no behavior!
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
}

class EmployeeService {
    public void giveRaise(Employee emp, double percent) {
        double newSalary = emp.getSalary() * (1 + percent / 100);
        emp.setSalary(newSalary);
    }
}

// ✅ Rich Domain Model
class Employee {
    private String name;
    private double salary;
    
    public void giveRaise(double percent) {
        if (percent <= 0) throw new IllegalArgumentException("Raise must be positive");
        this.salary *= (1 + percent / 100);
    }
    
    public boolean isHighEarner(double threshold) {
        return salary > threshold;
    }
}
```

### Why Anemic is Bad
- Breaks encapsulation — logic outside the object
- Procedural code disguised as OOP
- Business rules scattered across services

---

## 8. Early Binding vs Late Binding

### Early Binding (Compile-time / Static Binding)
```java
// Resolved at COMPILE time
class Calculator {
    int add(int a, int b) { return a + b; }       // Overloaded
    double add(double a, double b) { return a + b; } // Overloaded
}

Calculator calc = new Calculator();
calc.add(1, 2);     // Compiler picks int version at compile time
calc.add(1.0, 2.0); // Compiler picks double version at compile time
```

**Early binding applies to:**
- Method overloading
- static methods
- final methods
- private methods

### Late Binding (Runtime / Dynamic Binding)
```java
// Resolved at RUNTIME
class Animal { void speak() { System.out.println("Animal"); } }
class Dog extends Animal { void speak() { System.out.println("Bark"); } }
class Cat extends Animal { void speak() { System.out.println("Meow"); } }

Animal a = new Dog();  // Reference: Animal, Object: Dog
a.speak();  // "Bark" — resolved at runtime based on OBJECT type
```

**Late binding applies to:**
- Method overriding (virtual method dispatch)
- Interface method calls

### Comparison

| Aspect | Early Binding | Late Binding |
|--------|--------------|-------------|
| When | Compile time | Runtime |
| What | Overloading | Overriding |
| Based on | Reference type | Object type |
| Performance | Faster | Slightly slower (vtable lookup) |
| Methods | static, final, private | Instance methods |

---

## 9. Virtual Functions in Java

### Are All Methods Virtual in Java?
**YES** — All non-static, non-final, non-private instance methods are virtual by default.

```java
class Parent {
    void display() {  // Virtual by default
        System.out.println("Parent");
    }
    
    final void show() {  // NOT virtual — cannot override
        System.out.println("Final");
    }
    
    private void helper() {  // NOT virtual — not visible to child
        System.out.println("Private");
    }
    
    static void staticMethod() {  // NOT virtual — belongs to class
        System.out.println("Static");
    }
}

class Child extends Parent {
    @Override
    void display() {  // Overrides virtual method
        System.out.println("Child");
    }
}

Parent p = new Child();
p.display();  // "Child" — virtual method dispatch
```

### How JVM Implements Virtual Methods
- **vtable (Virtual Method Table):** Each class has a vtable mapping method signatures to actual implementations.
- At runtime, JVM looks up the vtable of the **actual object type** to find the correct method.

```
Parent vtable:        Child vtable:
┌─────────────┐       ┌─────────────┐
│display→Parent│       │display→Child │  ← overridden
│show→Parent   │       │show→Parent   │  ← inherited (final)
└─────────────┘       └─────────────┘
```

---

## 10. Destructor in Java

### Short Answer: Java Has No Destructors

Unlike C++, Java uses **Garbage Collection**, not manual memory management.

```java
// C++ has destructors
~MyClass() { /* cleanup */ }

// Java had finalize() — DEPRECATED since Java 9
@Deprecated
protected void finalize() throws Throwable {
    // DON'T use this!
}
```

### Why finalize() is Bad
- Unpredictable when (or if) it runs
- Performance overhead
- Can resurrect objects (bad practice)
- Deprecated since Java 9

### Modern Alternatives
```java
// 1. try-with-resources (Java 7+) — PREFERRED
try (Connection conn = getConnection();
     PreparedStatement stmt = conn.prepareStatement(sql)) {
    // use resources
}  // Automatically closed

// 2. Cleaner API (Java 9+)
Cleaner cleaner = Cleaner.create();
class MyResource implements AutoCloseable {
    private final Cleaner.Cleanable cleanable;
    
    MyResource() {
        cleanable = cleaner.register(this, () -> {
            // Cleanup action
        });
    }
    
    @Override
    public void close() {
        cleanable.clean();
    }
}
```

---

## 11. Copy Constructor

### Java Doesn't Have Built-in Copy Constructor (Unlike C++)

```java
class Person {
    private String name;
    private List<String> hobbies;
    
    // Regular constructor
    public Person(String name, List<String> hobbies) {
        this.name = name;
        this.hobbies = hobbies;
    }
    
    // Copy constructor (manual)
    public Person(Person other) {
        this.name = other.name;                              // String is immutable — safe
        this.hobbies = new ArrayList<>(other.hobbies);       // Deep copy of mutable list
    }
}

// Usage
Person p1 = new Person("John", Arrays.asList("Chess", "Reading"));
Person p2 = new Person(p1);  // Copy
p2.getHobbies().add("Gaming");
// p1's hobbies unchanged — deep copy worked
```

### Copy Constructor vs clone()

| Aspect | Copy Constructor | clone() |
|--------|-----------------|--------|
| Explicit | Yes | No (magic method) |
| Type safety | Yes | Returns Object (needs cast) |
| Deep copy | Easy to implement | Must manually deep copy |
| Inheritance | Works naturally | Fragile with inheritance |
| Recommended | ✅ Yes | ❌ Avoid (Effective Java) |

---

## 12. Can Constructors Be Inherited?

### Short Answer: **NO**

```java
class Parent {
    public Parent(String name) {
        System.out.println("Parent: " + name);
    }
}

class Child extends Parent {
    // ❌ ERROR: No default constructor in Parent
    // Must explicitly call parent constructor
    
    public Child(String name) {
        super(name);  // Must be first line
    }
}

// new Child("John");
// Output: Parent: John
```

### Constructor Chaining Rules
1. `super()` is implicitly called if no explicit `super()` or `this()`
2. `super()` or `this()` must be **first statement**
3. If parent has no default constructor, child MUST call `super(args)`
4. Cannot call both `super()` and `this()` in same constructor

```java
class Animal {
    Animal() { System.out.println("Animal"); }
    Animal(String name) { System.out.println("Animal: " + name); }
}

class Dog extends Animal {
    Dog() {
        this("Unknown");  // Calls Dog(String)
    }
    
    Dog(String name) {
        super(name);  // Calls Animal(String)
        System.out.println("Dog: " + name);
    }
}

// new Dog();
// Output: Animal: Unknown
//         Dog: Unknown
```

---

## 13. Can We Override Final Methods?

### Short Answer: **NO**

```java
class Parent {
    final void display() {
        System.out.println("Parent");
    }
}

class Child extends Parent {
    // ❌ COMPILE ERROR: Cannot override final method
    // void display() { System.out.println("Child"); }
}
```

### Why Use final Methods?
1. **Security:** Prevent subclasses from changing critical behavior
2. **Performance:** JIT can inline final methods (no virtual dispatch needed)
3. **Design:** Signal that method is complete and shouldn't be modified

### final Keyword Summary

| Applied To | Effect |
|-----------|--------|
| Variable | Cannot reassign (constant) |
| Method | Cannot override |
| Class | Cannot extend |
| Parameter | Cannot reassign inside method |

```java
final class String { }       // Cannot extend String
final int MAX = 100;         // Constant

class Parent {
    final void critical() { } // Cannot override
}

void process(final int x) {
    // x = 10;  // ❌ Cannot reassign
}
```

---

## Quick Reference: Tricky OOP Questions

| Question | Answer |
|----------|--------|
| Is Java 100% OOP? | No (primitives, static, null) |
| Polymorphism without inheritance? | Yes (interfaces, generics) |
| All methods virtual in Java? | Yes (except static, final, private) |
| Java has destructors? | No (use try-with-resources) |
| Constructors inherited? | No (must call super()) |
| Override final method? | No (compile error) |
| Override private method? | No (not visible to child) |
| Override static method? | No (method hiding, not overriding) |
| Can abstract class have constructor? | Yes |
| Can interface have constructor? | No |
