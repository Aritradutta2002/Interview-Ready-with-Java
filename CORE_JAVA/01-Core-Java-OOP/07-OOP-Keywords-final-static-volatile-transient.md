# Important Keywords - Deep Dive

## 1. final Keyword

### final Variable
```java
final int MAX = 100;  // Constant, cannot reassign
final List<String> list = new ArrayList<>();
list.add("item");  // ✅ Can modify object
// list = new ArrayList<>();  // ❌ Cannot reassign reference
```

### final Method
```java
class Parent {
    final void display() {}  // Cannot be overridden
}
```

### final Class
```java
final class Immutable { }  // Cannot be extended
// String, Integer, wrapper classes are final
```

---

## 2. static Keyword

### static Variable (Class variable)
```java
class Counter {
    static int count = 0;  // Shared across all instances
    Counter() { count++; }
}
```

### static Method (Class method)
```java
class Utility {
    static int add(int a, int b) { return a + b; }
    // Cannot access instance members directly
    // Cannot use this, super
}
```

### static Block
```java
class Database {
    static Connection conn;
    static {
        conn = DriverManager.getConnection(url);  // Runs once on class load
    }
}
```

### static Nested Class
```java
class Outer {
    static class Nested {
        // Can access Outer's static members only
    }
}
```

---

## 3. transient Keyword

**Purpose:** Exclude field from serialization.

```java
class User implements Serializable {
    String username;
    transient String password;  // Not serialized
    transient int sessionId;     // Not serialized
    
    // On deserialization: password = null, sessionId = 0
}
```

**Use Cases:**
- Sensitive data (passwords, keys)
- Derived/cached data
- Non-serializable objects

---

## 4. volatile Keyword

**Purpose:** Ensure visibility across threads, prevent caching.

```java
class Flag {
    private volatile boolean running = true;
    
    // Thread 1
    void stop() { running = false; }
    
    // Thread 2
    void process() {
        while (running) { /* work */ }  // Always sees latest value
    }
}
```

**volatile vs synchronized:**
| volatile | synchronized |
|----------|--------------|
| Only visibility | Visibility + atomicity |
| No locking | Locking overhead |
| Single read/write | Compound operations |
| Doesn't block | Blocks other threads |

**Note:** volatile doesn't make `i++` atomic (use AtomicInteger)

---

## 5. Other Important Keywords

### default (Java 8+)
```java
interface MyInterface {
    default void log() {
        System.out.println("Default implementation");
    }
}
```

### synchronized
```java
synchronized void increment() { count++; }  // Method level

void process() {
    synchronized(this) {  // Block level
        // Critical section
    }
}
```

### native
```java
native void perform();  // Implemented in C/C++
// Used by JVM for platform-specific operations
```

### strictfp
```java
strictfp class Calculation {
    // Ensures consistent floating-point results across platforms
}
```

---

## Quick Reference Table

| Keyword | Applied To | Purpose |
|---------|------------|---------|
| final | Variable, Method, Class | Prevent modification/override/inheritance |
| static | Variable, Method, Block, Class | Class-level, shared |
| transient | Variable | Skip serialization |
| volatile | Variable | Thread visibility |
| synchronized | Method, Block | Thread safety |
| native | Method | Platform-specific code |
| default | Method (interface) | Default implementation |

---

## Interview Q&A

**Q: Can final method be overloaded?**
A: Yes. Overloading is different method signature.

**Q: Can we serialize transient variable?**
A: No, that's the purpose. Use writeObject() for custom serialization.

**Q: Is volatile replacement for synchronized?**
A: No. volatile only ensures visibility, not atomicity.

**Q: Can static methods be overridden?**
A: No. They can be hidden (shadowed) but not overridden.

**Q: When is static block executed?**
A: When class is loaded, before any instance creation.
