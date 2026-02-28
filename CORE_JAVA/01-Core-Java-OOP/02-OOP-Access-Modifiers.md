# Access Modifiers — Complete Guide

## The 4 Access Levels

```
┌──────────────────────────────────────────────────────────────┐
│              ACCESS MODIFIER VISIBILITY                        │
├──────────────┬──────────┬─────────┬──────────┬───────────────┤
│  Modifier    │ Same     │ Same    │ Subclass │ Everywhere    │
│              │ Class    │ Package │ (diff pkg)│              │
├──────────────┼──────────┼─────────┼──────────┼───────────────┤
│  public      │    ✅    │   ✅    │    ✅    │     ✅        │
│  protected   │    ✅    │   ✅    │    ✅    │     ❌        │
│  (default)   │    ✅    │   ✅    │    ❌    │     ❌        │
│  private     │    ✅    │   ❌    │    ❌    │     ❌        │
└──────────────┴──────────┴─────────┴──────────┴───────────────┘
```

---

## 1. public — Accessible Everywhere
```java
public class User {
    public String name;           // Visible everywhere
    public void greet() { }      // Visible everywhere
}

// Any class, any package can access
User user = new User();
user.name = "John";
user.greet();
```

**Top-level class rule:** Only ONE public class per file, and filename must match.

---

## 2. private — Same Class Only
```java
public class BankAccount {
    private double balance;       // Only this class
    private String pin;           // Only this class
    
    public double getBalance() {  // Controlled access
        return balance;
    }
    
    private boolean validatePin(String input) {  // Internal only
        return pin.equals(input);
    }
}

// From outside:
BankAccount acc = new BankAccount();
// acc.balance;         ❌ COMPILE ERROR
// acc.validatePin(""); ❌ COMPILE ERROR
acc.getBalance();       // ✅ Via public getter
```

**Key:** private members ARE accessible from another instance of the SAME class:
```java
public class Point {
    private int x, y;
    
    public double distanceTo(Point other) {
        int dx = this.x - other.x;  // ✅ Can access other's private fields!
        int dy = this.y - other.y;  // ✅ Same class, so allowed
        return Math.sqrt(dx * dx + dy * dy);
    }
}
```

---

## 3. default (Package-Private) — Same Package Only

No keyword — just don't write any modifier.

```java
// File: com/myapp/internal/Helper.java
class Helper {                   // default class — same package only
    int count;                   // default field
    void process() { }           // default method
}

// File: com/myapp/internal/Service.java
class Service {
    void use() {
        Helper h = new Helper(); // ✅ Same package
        h.count = 5;             // ✅ Same package
    }
}

// File: com/myapp/external/Client.java
// Helper h = new Helper();     ❌ Different package — not visible!
```

**Common trap:** Many developers forget this modifier exists and accidentally use it.

---

## 4. protected — Same Package + Subclasses

### The Tricky One

```java
// File: com/animals/Animal.java
package com.animals;

public class Animal {
    protected String name;
    protected void eat() { System.out.println("eating"); }
}
```

```java
// File: com/pets/Dog.java
package com.pets;  // DIFFERENT package

import com.animals.Animal;

public class Dog extends Animal {
    void test() {
        this.name = "Rex";     // ✅ Subclass accessing inherited member
        this.eat();            // ✅ Through inheritance
        
        Dog d = new Dog();
        d.name = "Max";       // ✅ Same class
        d.eat();              // ✅ Same class
        
        Animal a = new Animal();
        // a.name = "Buddy";  // ❌ NOT through inheritance — different package!
        // a.eat();           // ❌ Can't access via parent reference from different package
    }
}
```

### The Protected Access Trap
```java
package com.pets;

public class Cat extends Animal {
    void test() {
        Dog d = new Dog();
        // d.name = "test";   // ❌ COMPILE ERROR!
        // Cat can't access Dog's protected members even though both extend Animal
        // Protected access is through inheritance of YOUR OWN type only
    }
}
```

### Protected Summary
```
Same package  → acts like default (accessible to all classes in package)
Diff package  → only accessible through inheritance (this.member or super.member)
               NOT through a parent-type reference variable
```

---

## Applied To What?

| Modifier | Class (top-level) | Class (nested) | Field | Method | Constructor |
|----------|-------------------|----------------|-------|--------|-------------|
| public | ✅ | ✅ | ✅ | ✅ | ✅ |
| protected | ❌ | ✅ | ✅ | ✅ | ✅ |
| default | ✅ | ✅ | ✅ | ✅ | ✅ |
| private | ❌ | ✅ | ✅ | ✅ | ✅ |

**Top-level classes** can only be `public` or `default`. NOT private or protected.

**Private constructor** = Singleton, utility class, factory pattern:
```java
public class Singleton {
    private Singleton() { }  // Can't instantiate from outside
    
    private static final Singleton INSTANCE = new Singleton();
    public static Singleton getInstance() { return INSTANCE; }
}
```

---

## Access Modifiers in Inheritance

### Overriding Rules: Can Only Widen Access
```java
class Parent {
    protected void display() { }
}

class Child extends Parent {
    // public void display() { }     ✅ Wider (protected → public)
    // protected void display() { }  ✅ Same
    // void display() { }            ❌ Narrower (protected → default)
    // private void display() { }    ❌ Narrower (protected → private)
}
```

**Rule:** Overridden method must have SAME or WIDER access. Never narrower.

**Why?** Liskov Substitution — if parent promises a method is accessible, child can't revoke it.

```
Widening order: private → default → protected → public
```

---

## Interview Questions

**Q: What are the 4 access modifiers in Java?**
A: `public` (everywhere), `protected` (package + subclasses), default/package-private (package only), `private` (class only).

**Q: Can a top-level class be private or protected?**
A: No. Only `public` or `default`. Nested classes can use all four.

**Q: Can private members be accessed from another instance of the same class?**
A: Yes. Private is per-class, not per-instance. `this.x` and `other.x` both work within the same class.

**Q: What is the default access modifier?**
A: Package-private (no keyword). Visible to all classes in the same package, invisible outside.

**Q: Can we access protected members through a parent-type reference from a different package?**
A: No. Protected access from a different package only works through inheritance (`this.member` or `super.member`), not through a parent-type variable.

**Q: Can we narrow access when overriding?**
A: No. Overriding can only widen access (e.g., protected → public). Narrowing causes a compile error.

**Q: Why can't we narrow access when overriding?**
A: Liskov Substitution Principle. If parent declares a method as protected, all code expecting the parent type can call it. Child can't revoke that access.

**Q: What access modifier should fields generally have?**
A: `private`. Expose via public getters/setters if needed. This is encapsulation.

**Q: What is the difference between default and protected?**
A: Default = same package only. Protected = same package + subclasses in other packages. Protected is wider.
