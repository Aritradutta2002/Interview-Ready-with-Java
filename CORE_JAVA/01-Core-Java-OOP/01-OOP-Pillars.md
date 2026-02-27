# OOP Pillars - Quick Reference

## 1. Encapsulation
**Definition:** Bundling data + methods, restricting direct access via access modifiers.

```java
public class BankAccount {
    private double balance;  // Hidden state
    
    public void deposit(double amount) {
        if (amount > 0) balance += amount;
    }
    
    public double getBalance() { return balance; }
}
```

**Key Points:**
- `private` fields + `public` getters/setters
- Data hiding, validation control
- JavaBeans convention

---

## 2. Abstraction
**Definition:** Hiding implementation, showing only functionality.

```java
abstract class Vehicle {
    abstract void start();  // What, not how
}

class Car extends Vehicle {
    void start() { System.out.println("Key start"); }
}
```

**Abstraction vs Encapsulation:**
| Abstraction | Encapsulation |
|-------------|---------------|
| Design level | Implementation level |
| Hides complexity | Hides data |
| Abstract classes/interfaces | Getters/setters |

---

## 3. Inheritance
**Definition:** Acquiring properties of parent class.

```java
class Animal { void eat() {} }
class Dog extends Animal { void bark() {} }  // IS-A relationship
```

**Types:**
- Single, Multilevel, Hierarchical
- Multiple inheritance NOT supported (to avoid diamond problem)
- Use interfaces for multiple inheritance of type

---

## 4. Polymorphism
**Definition:** Same entity, different behaviors.

### Compile-time (Static) - Method Overloading
```java
void add(int a, int b) {}
void add(int a, int b, int c) {}
void add(double a, double b) {}
```

### Runtime (Dynamic) - Method Overriding
```java
class Animal { void sound() { System.out.println("Animal"); } }
class Dog extends Animal { 
    @Override void sound() { System.out.println("Bark"); } 
}

Animal a = new Dog();
a.sound();  // Outputs: Bark (runtime polymorphism)
```

**Overloading vs Overriding:**
| Overloading | Overriding |
|-------------|------------|
| Same class | Parent-child |
| Different parameters | Same parameters |
| Compile-time | Runtime |
| Return type can differ | Covariant return allowed |

---

## Quick Interview Q&A

**Q: Can we override static method?**
A: No. Static methods are hidden, not overridden.

**Q: Can we overload main method?**
A: Yes, but JVM calls `main(String[] args)`.

**Q: Why multiple inheritance not in Java?**
A: Diamond problem - ambiguity when same method in multiple parents.

**Q: What is covariant return type?**
A: Overridden method can return subtype of parent's return type.
```java
class Parent { Animal get() { return new Animal(); } }
class Child extends Parent { 
    @Override Dog get() { return new Dog(); }  // Covariant
}
```
