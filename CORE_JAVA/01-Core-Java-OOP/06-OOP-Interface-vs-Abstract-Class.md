# Interface vs Abstract Class

## Key Differences

| Feature | Interface | Abstract Class |
|---------|-----------|----------------|
| Multiple inheritance | ✅ Yes | ❌ No |
| State (instance variables) | ❌ Only constants | ✅ Yes |
| Constructors | ❌ No | ✅ Yes |
| Method types | Abstract, default, static | Any |
| Default access modifier | public | Can be any |
| Speed | Slightly slower | Faster |
| Purpose | Define contract | Share code |

---

## Interface (Java 8+)

```java
interface Flyable {
    int MAX_HEIGHT = 1000;  // public static final
    
    void fly();  // public abstract
    
    // Java 8: default method
    default void glide() {
        System.out.println("Gliding...");
    }
    
    // Java 8: static method
    static void checkFuel() {
        System.out.println("Checking fuel");
    }
}

// Multiple interfaces
class Bird implements Flyable, Runnable {
    public void fly() { }
    public void run() { }
}
```

---

## Abstract Class

```java
abstract class Vehicle {
    protected String brand;  // Can have state
    private int speed;
    
    public Vehicle(String brand) {  // Constructor
        this.brand = brand;
    }
    
    abstract void start();  // Abstract method
    
    void stop() {  // Concrete method
        System.out.println("Stopping...");
    }
}

class Car extends Vehicle {
    public Car(String brand) { super(brand); }
    void start() { System.out.println("Key start"); }
}
```

---

## When to Use What?

### Use Interface when:
- Need multiple inheritance
- Unrelated classes need same behavior
- Defining API contract
- Need to specify behavior without implementation

### Use Abstract Class when:
- Need to share code among closely related classes
- Need instance variables/state
- Need non-public members
- Need constructors

---

## Marker Interface

**Definition:** Interface with no methods, used to indicate special behavior.

```java
// Built-in marker interfaces
Serializable    // Object can be serialized
Cloneable       // Object can be cloned
Remote          // Object can be used remotely

// Custom marker interface
interface Auditable {}

class Transaction implements Auditable {
    // Framework can check: if (obj instanceof Auditable)
}
```

**Why use?** 
- Signals to JVM/framework to treat object specially
- Alternative: Use annotations (`@Auditable`)

---

## Interview Q&A

**Q: Can interface extend another interface?**
A: Yes. `interface B extends A { }`

**Q: Can abstract class implement interface?**
A: Yes. Can implement some/all methods or leave abstract.

**Q: Can abstract class be final?**
A: No. Abstract needs inheritance, final prevents it.

**Q: What happens if class implements interface but doesn't implement all methods?**
A: Class must be declared abstract.

**Q: Can interface have private methods?**
A: Yes, Java 9+. Used by default methods internally.
