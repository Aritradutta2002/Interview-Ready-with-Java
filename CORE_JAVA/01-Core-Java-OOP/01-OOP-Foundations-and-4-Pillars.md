# OOP Foundations and the 4 Pillars

## Table of Contents
1. [What is OOP?](#what-is-oop)
2. [Class vs Object](#class-vs-object)
3. [The 4 Pillars](#the-4-pillars)
4. [OOP vs Procedural](#oop-vs-procedural)
5. [Java's OOP Building Blocks](#javas-oop-building-blocks)
6. [Interview Questions](#interview-questions)

---

## What is OOP?

Object-Oriented Programming organizes code around **objects** (data + behavior) rather than functions and logic.

```
┌─────────────────────────────────────────────────────────────┐
│               THE 4 PILLARS OF OOP                           │
├───────────────┬───────────────┬──────────────┬──────────────┤
│ Encapsulation │  Abstraction  │ Inheritance  │ Polymorphism │
├───────────────┼───────────────┼──────────────┼──────────────┤
│ Protect data  │ Hide          │ Reuse code   │ Same method  │
│ via access    │ complexity,   │ from parent  │ different    │
│ modifiers     │ show only     │ classes      │ behavior     │
│               │ essentials    │              │              │
├───────────────┼───────────────┼──────────────┼──────────────┤
│ private fields│ abstract class│ extends      │ Overloading  │
│ getters/      │ interfaces    │ implements   │ Overriding   │
│ setters       │               │              │              │
└───────────────┴───────────────┴──────────────┴──────────────┘
```

---

## Class vs Object

### Class — The Blueprint
```java
public class Car {
    // State (what it HAS)
    private String brand;
    private int speed;
    private boolean engineOn;
    
    // Behavior (what it DOES)
    public void start() {
        engineOn = true;
        System.out.println(brand + " started");
    }
    
    public void accelerate(int increment) {
        if (engineOn) speed += increment;
    }
    
    public Car(String brand) {
        this.brand = brand;
        this.speed = 0;
        this.engineOn = false;
    }
}
```

### Object — The Instance
```java
Car myCar = new Car("Toyota");    // Object 1
Car yourCar = new Car("Honda");   // Object 2

myCar.start();           // Toyota started
myCar.accelerate(60);    // myCar speed = 60
// yourCar is unaffected — separate object with its own state
```

### Memory View
```
Stack                          Heap
┌──────────────┐              ┌──────────────────┐
│ myCar ───────┼──────────────▶│ Car Object       │
│              │              │ brand = "Toyota"  │
│ yourCar ─────┼────────┐    │ speed = 60        │
│              │        │    └──────────────────┘
└──────────────┘        │    ┌──────────────────┐
                        └───▶│ Car Object       │
                             │ brand = "Honda"   │
                             │ speed = 0         │
                             └──────────────────┘
```

| Class | Object |
|-------|--------|
| Blueprint/Template | Instance of class |
| Defined once | Created many times |
| No memory for data | Allocated on heap |
| Declared with `class` | Created with `new` |
| Compile-time concept | Runtime concept |

---

## The 4 Pillars

### 1. Encapsulation — "Protect the Data"

```java
public class BankAccount {
    private double balance;       // ← Hidden state
    
    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        balance += amount;        // ← Controlled modification with validation
    }
    
    public void withdraw(double amount) {
        if (amount > balance) throw new IllegalStateException("Insufficient funds");
        balance -= amount;
    }
    
    public double getBalance() {
        return balance;           // ← Read-only access
    }
    // No setBalance() — can't arbitrarily set balance
}

BankAccount acc = new BankAccount();
acc.deposit(1000);          // ✅ Goes through validation
// acc.balance = 1000000;   // ❌ COMPILE ERROR — private field
```

**Why it matters:** Prevents invalid state, internal changes don't break external code, easier debugging.

> **Deep dive → [03-Encapsulation-vs-Abstraction.md](03-OOP-Encapsulation-vs-Abstraction.md)**

---

### 2. Abstraction — "Hide the Complexity"

```java
// User sees simple interface:
interface PaymentProcessor {
    boolean pay(double amount);
    boolean refund(String transactionId);
}

// Complex implementation hidden:
class StripeProcessor implements PaymentProcessor {
    public boolean pay(double amount) {
        // 1. Validate merchant account
        // 2. Encrypt card details
        // 3. Connect to Stripe API
        // 4. Handle 3D Secure authentication
        // 5. Process payment
        return true;  // User just sees "pay succeeded"
    }
    public boolean refund(String id) { return true; }
}

PaymentProcessor processor = new StripeProcessor();
processor.pay(99.99);  // Simple! Complexity hidden.
```

```
Encapsulation: HOW to protect data    → private fields, getters/setters
Abstraction:   WHAT to show the user  → interfaces, abstract classes
```

> **Deep dive → [03-Encapsulation-vs-Abstraction.md](03-OOP-Encapsulation-vs-Abstraction.md)**

---

### 3. Inheritance — "Reuse and Extend"

```java
class Animal {
    protected String name;
    void eat() { System.out.println(name + " is eating"); }
}

class Dog extends Animal {
    void bark() { System.out.println(name + " says Woof!"); }
}

Dog dog = new Dog();
dog.name = "Rex";
dog.eat();   // ✅ Inherited from Animal
dog.bark();  // ✅ Own method
```

```
            ┌──────────┐
            │  Animal   │  eat()
            └────┬─────┘
         ┌───────┴───────┐
         ▼               ▼
    ┌─────────┐    ┌─────────┐
    │   Dog   │    │   Cat   │
    │ bark()  │    │ purr()  │
    └─────────┘    └─────────┘
```

**Supported:** Single ✅, Multilevel ✅, Hierarchical ✅, Multiple via interfaces ✅
**NOT supported:** Multiple via classes ❌ (diamond problem)

> **Deep dive → [04-Inheritance-Types-and-Diamond-Problem.md](04-OOP-Inheritance-Types-and-Diamond-Problem.md)**

---

### 4. Polymorphism — "One Interface, Many Forms"

#### Compile-time (Method Overloading)
```java
class Calculator {
    int add(int a, int b)          { return a + b; }
    double add(double a, double b) { return a + b; }
    int add(int a, int b, int c)   { return a + b + c; }
}
// Compiler decides which method at compile time
```

#### Runtime (Method Overriding)
```java
class Animal { void sound() { System.out.println("Some sound"); } }
class Dog extends Animal { @Override void sound() { System.out.println("Bark!"); } }
class Cat extends Animal { @Override void sound() { System.out.println("Meow!"); } }

Animal[] animals = { new Dog(), new Cat(), new Dog() };
for (Animal a : animals) {
    a.sound();  // Bark! → Meow! → Bark!
    // JVM decides at runtime based on actual object type
}
```

**Overloading vs Overriding:**

| Aspect | Overloading | Overriding |
|--------|-------------|------------|
| Where | Same class | Parent → Child |
| Parameters | Must differ | Must be same |
| Return type | Can differ | Same or covariant |
| Binding | Compile-time | Runtime |
| static methods | Can overload | Cannot override (hiding) |
| Access | No restriction | Cannot narrow (can widen) |

> **Deep dive → [05-Polymorphism-Deep-Dive.md](05-OOP-Polymorphism-Deep-Dive.md)**

---

## OOP vs Procedural

```java
// ❌ Procedural style
double[] balances = {1000, 2000, 500};
void withdraw(int index, double amount) {
    if (balances[index] >= amount) balances[index] -= amount;
}

// ✅ OOP style
class Account {
    private String name;
    private double balance;
    void withdraw(double amount) {
        if (balance >= amount) balance -= amount;
    }
}
Account john = new Account("John", 1000);
john.withdraw(500);  // Data and behavior together
```

| Procedural | OOP |
|-----------|-----|
| Organized around functions | Organized around objects |
| Data separate from functions | Data bundled with methods |
| No access control | Access modifiers |
| Reuse via copy-paste | Reuse via inheritance/composition |
| Hard to scale | Scales well |

---

## Java's OOP Building Blocks

### Constructor
```java
class Person {
    private String name;
    private int age;
    
    Person() { this("Unknown", 0); }       // Default → chains to parameterized
    
    Person(String name, int age) {          // Parameterized
        this.name = name;
        this.age = age;
    }
}
// Rules:
// - Same name as class, no return type
// - Java provides default constructor only if you write NONE
// - super() implicitly called as first statement
```

### this and super Keywords
```java
class Animal {
    String name;
    Animal(String name) { this.name = name; }
    void display() { System.out.println("Animal: " + name); }
}

class Dog extends Animal {
    String breed;
    Dog(String name, String breed) {
        super(name);            // Call parent constructor (must be first line)
        this.breed = breed;     // this = current instance
    }
    @Override
    void display() {
        super.display();        // Call parent method
        System.out.println("Breed: " + breed);
    }
}
```

### Object Class — Root of All
```java
class MyClass { }  // Implicitly: class MyClass extends Object { }

// Object provides 11 methods to ALL classes:
// toString(), equals(), hashCode(), getClass(),
// clone(), finalize() (deprecated),
// wait(), wait(long), wait(long, int), notify(), notifyAll()
```

---

## Interview Questions

**Q: What are the 4 pillars of OOP?**
A: Encapsulation (data protection), Abstraction (hide complexity), Inheritance (code reuse), Polymorphism (same method, different behavior).

**Q: Difference between class and object?**
A: Class is a blueprint defined at compile time. Object is a runtime instance created with `new`, allocated on heap.

**Q: Can we override a static method?**
A: No. Static methods are hidden, not overridden — resolved by reference type at compile time.

**Q: Can we overload main method?**
A: Yes, but JVM always calls `public static void main(String[] args)` as the entry point.

**Q: Why no multiple inheritance with classes?**
A: Diamond problem — ambiguity when two parents have the same method. Use interfaces instead.

**Q: What is covariant return type?**
A: Overridden method can return a subtype of parent's return type (Java 5+).
```java
class Parent { Animal get() { return new Animal(); } }
class Child extends Parent {
    @Override Dog get() { return new Dog(); }  // Dog IS-A Animal
}
```

**Q: Can a constructor be private?**
A: Yes. Used in Singleton, factory methods, utility classes.

**Q: What happens if you define no constructor?**
A: Compiler provides a no-arg default. If you write ANY constructor, the default is NOT generated.

**Q: Can this() and super() both be in the same constructor?**
A: No. Both must be the first statement, so only one is allowed.

**Q: Overloading vs Overriding?**
A: Overloading = same name, different params, same class, compile-time. Overriding = same signature, parent-child, runtime.

**Q: Is Java 100% object-oriented?**
A: No. Primitives (int, char, boolean) are not objects. Static members and null also break pure OOP.
