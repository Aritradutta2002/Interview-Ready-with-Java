# Custom Serialization

## Table of Contents
1. [Why Custom Serialization?](#why-custom-serialization)
2. [writeObject and readObject Methods](#writeobject-and-readobject-methods)
3. [Externalizable Interface](#externalizable-interface)
4. [writeReplace and readResolve](#writereplace-and-readresolve)
5. [Serialization Proxy Pattern](#serialization-proxy-pattern)
6. [Interview Questions](#interview-questions)
7. [Common Traps](#common-traps)
8. [Related Topics](#related-topics)

---

## Why Custom Serialization?

Default serialization works for most cases, but custom serialization is needed when:

- **Security**: Encrypt sensitive fields before serialization
- **Performance**: Optimize what gets serialized
- **Validation**: Enforce invariants during deserialization
- **Compatibility**: Handle version changes gracefully
- **Special Logic**: Custom initialization after deserialization

```
Default Serialization          Custom Serialization
┌──────────────────┐          ┌──────────────────┐
│ Automatic        │          │ Full control     │
│ All fields       │          │ Choose fields    │
│ No validation    │          │ Add validation   │
│ No encryption    │          │ Encrypt data     │
└──────────────────┘          └──────────────────┘
```

---

## writeObject and readObject Methods

### Custom Serialization with Private Methods

Java looks for these **private** methods in your class:

```java
private void writeObject(ObjectOutputStream out) throws IOException {
    // Custom serialization logic
}

private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // Custom deserialization logic
}
```

### Example: Encrypting Sensitive Data

```java
import java.io.*;
import java.util.Base64;

class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;  // Sensitive field
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Custom serialization: encrypt password
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();  // Serialize non-transient fields first
        
        // Encrypt password before writing
        String encrypted = Base64.getEncoder().encodeToString(password.getBytes());
        out.writeObject(encrypted);
    }
    
    // Custom deserialization: decrypt password
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();  // Deserialize non-transient fields first
        
        // Decrypt password after reading
        String encrypted = (String) in.readObject();
        this.password = new String(Base64.getDecoder().decode(encrypted));
    }
    
    @Override
    public String toString() {
        return "User{username='" + username + "', password='" + password + "'}";
    }
}
```

**Usage:**
```java
User user = new User("john", "secret123");

// Serialize
try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("user.ser"))) {
    out.writeObject(user);
}

// Deserialize
try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("user.ser"))) {
    User deserializedUser = (User) in.readObject();
    System.out.println(deserializedUser);  // Password is decrypted
}
```

### Key Methods

```java
// In writeObject():
out.defaultWriteObject();     // Serialize all non-transient fields
out.writeInt(value);          // Write primitive
out.writeObject(obj);         // Write object
out.writeUTF(string);         // Write string

// In readObject():
in.defaultReadObject();       // Deserialize all non-transient fields
int value = in.readInt();     // Read primitive
Object obj = in.readObject(); // Read object
String str = in.readUTF();    // Read string
```

---

### Example: Validating Invariants

```java
class Range implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int min;
    private int max;
    
    public Range(int min, int max) {
        if (min > max) throw new IllegalArgumentException("min must be <= max");
        this.min = min;
        this.max = max;
    }
    
    // Validate invariants after deserialization
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        
        // Enforce invariant: min <= max
        if (min > max) {
            throw new InvalidObjectException("Invalid range: min > max");
        }
    }
}
```

**Why this matters**: Malicious code could modify the serialized byte stream to create invalid objects. Validation in `readObject()` prevents this.

---

### Example: Handling transient Fields

```java
class Cache implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Map<String, String> data;
    private transient Map<String, String> tempCache;  // Not serialized
    
    public Cache() {
        this.data = new HashMap<>();
        this.tempCache = new HashMap<>();
    }
    
    // Reinitialize transient fields after deserialization
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        
        // Rebuild transient cache
        this.tempCache = new HashMap<>();
        System.out.println("Cache reinitialized after deserialization");
    }
}
```

---

## Externalizable Interface

For **complete control** over serialization, implement `Externalizable`:

```java
public interface Externalizable extends Serializable {
    void writeExternal(ObjectOutput out) throws IOException;
    void readExternal(ObjectInput in) throws IOException, ClassNotFoundException;
}
```

### Example: Manual Serialization

```java
import java.io.*;

class Employee implements Externalizable {
    private String name;
    private int age;
    private double salary;
    
    // Required: public no-arg constructor
    public Employee() { }
    
    public Employee(String name, int age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }
    
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        // Manually write each field
        out.writeUTF(name);
        out.writeInt(age);
        // Don't serialize salary (sensitive)
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // Manually read each field in same order
        this.name = in.readUTF();
        this.age = in.readInt();
        this.salary = 0.0;  // Default value
    }
    
    @Override
    public String toString() {
        return "Employee{name='" + name + "', age=" + age + ", salary=" + salary + "}";
    }
}
```

### Serializable vs Externalizable

| Aspect | Serializable | Externalizable |
|--------|--------------|----------------|
| Methods | None (marker interface) | `writeExternal()`, `readExternal()` |
| Control | Automatic | Manual |
| Constructor | Not called during deserialization | **Must have public no-arg constructor** |
| Performance | Slower (reflection) | Faster (no reflection) |
| Ease of use | Easy | More complex |
| transient | Respected | Ignored (you control everything) |

**When to use Externalizable:**
- Performance-critical applications
- Need fine-grained control over serialization
- Want to serialize only specific fields
- Custom serialization logic is complex

---

## writeReplace and readResolve

### writeReplace — Replace Object Before Serialization

```java
class Employee implements Serializable {
    private String name;
    private transient String password;
    
    public Employee(String name, String password) {
        this.name = name;
        this.password = password;
    }
    
    // Replace with a proxy before serialization
    private Object writeReplace() throws ObjectStreamException {
        return new EmployeeProxy(name);  // Serialize proxy instead
    }
}

class EmployeeProxy implements Serializable {
    private String name;
    
    public EmployeeProxy(String name) {
        this.name = name;
    }
    
    // Reconstruct original object after deserialization
    private Object readResolve() throws ObjectStreamException {
        return new Employee(name, "default");  // Password not restored
    }
}
```

---

### readResolve — Replace Object After Deserialization

**Use case: Singleton Pattern**

```java
class Singleton implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Singleton INSTANCE = new Singleton();
    
    private Singleton() { }
    
    public static Singleton getInstance() {
        return INSTANCE;
    }
    
    // Ensure same instance after deserialization
    private Object readResolve() throws ObjectStreamException {
        return INSTANCE;  // Return existing instance, not new one
    }
}
```

**Without `readResolve()`:**
```java
Singleton s1 = Singleton.getInstance();
// Serialize and deserialize
Singleton s2 = // deserialized object
System.out.println(s1 == s2);  // false (different instances) ❌
```

**With `readResolve()`:**
```java
Singleton s1 = Singleton.getInstance();
// Serialize and deserialize
Singleton s2 = // deserialized object
System.out.println(s1 == s2);  // true (same instance) ✅
```

---

### Use Cases for writeReplace and readResolve

| Method | Use Case |
|--------|----------|
| `writeReplace()` | Replace object with proxy before serialization |
| `readResolve()` | Ensure Singleton, Enum, or immutable object integrity |

---

## Serialization Proxy Pattern

**Best practice** for secure serialization of complex objects.

### Problem: Direct Serialization Vulnerabilities

```java
class Period implements Serializable {
    private final Date start;
    private final Date end;
    
    public Period(Date start, Date end) {
        if (start.after(end)) throw new IllegalArgumentException();
        this.start = new Date(start.getTime());  // Defensive copy
        this.end = new Date(end.getTime());
    }
}

// Problem: Attacker can modify serialized bytes to create invalid Period
// (e.g., start > end)
```

### Solution: Serialization Proxy Pattern

```java
class Period implements Serializable {
    private final Date start;
    private final Date end;
    
    public Period(Date start, Date end) {
        if (start.after(end)) throw new IllegalArgumentException();
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
    }
    
    // Nested proxy class
    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 1L;
        private final Date start;
        private final Date end;
        
        SerializationProxy(Period period) {
            this.start = period.start;
            this.end = period.end;
        }
        
        // Reconstruct Period with validation
        private Object readResolve() {
            return new Period(start, end);  // Constructor validates invariants
        }
    }
    
    // Serialize proxy instead of Period
    private Object writeReplace() {
        return new SerializationProxy(this);
    }
    
    // Prevent direct deserialization of Period
    private void readObject(ObjectInputStream in) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
}
```

**Benefits:**
- Enforces invariants (constructor validation)
- Prevents byte stream manipulation attacks
- Cleaner separation of concerns

---

## Interview Questions

### Q1: What is the purpose of writeObject() and readObject()?

**Difficulty**: Medium

**Answer**:
Custom serialization methods that give you control over the serialization process. `writeObject()` customizes what gets written to the stream. `readObject()` customizes how the object is reconstructed, allowing validation, decryption, or reinitialization of transient fields.

**Must be private** and have exact signatures:
```java
private void writeObject(ObjectOutputStream out) throws IOException
private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
```

---

### Q2: Difference between Serializable and Externalizable?

**Difficulty**: Medium

**Answer**:

| Serializable | Externalizable |
|--------------|----------------|
| Marker interface | Has `writeExternal()` and `readExternal()` |
| Automatic | Manual control |
| No constructor called | **Requires public no-arg constructor** |
| Uses reflection (slower) | No reflection (faster) |
| Respects `transient` | Ignores `transient` |

---

### Q3: What is readResolve() used for?

**Difficulty**: Hard

**Answer**:
`readResolve()` replaces the deserialized object with another object. Used to:
- Maintain Singleton pattern (return existing instance)
- Ensure Enum integrity
- Implement Serialization Proxy Pattern

```java
private Object readResolve() throws ObjectStreamException {
    return INSTANCE;  // Return singleton instance
}
```

---

### Q4: What is writeReplace()?

**Difficulty**: Hard

**Answer**:
`writeReplace()` replaces the object with another object before serialization. Used in Serialization Proxy Pattern to serialize a proxy instead of the actual object.

```java
private Object writeReplace() throws ObjectStreamException {
    return new SerializationProxy(this);
}
```

---

### Q5: Why does Externalizable require a no-arg constructor?

**Difficulty**: Medium

**Answer**:
During deserialization, JVM creates an instance using the no-arg constructor, then calls `readExternal()` to populate fields. Without a no-arg constructor, deserialization fails with `InvalidClassException`.

---

### Q6: How to prevent serialization of a Serializable class?

**Difficulty**: Hard

**Answer**:
Throw `NotSerializableException` in `writeObject()`:

```java
private void writeObject(ObjectOutputStream out) throws IOException {
    throw new NotSerializableException("This class cannot be serialized");
}
```

---

### Q7: What is the Serialization Proxy Pattern?

**Difficulty**: Hard

**Answer**:
A design pattern that serializes a proxy object instead of the actual object. Benefits:
- Enforces invariants (proxy's `readResolve()` calls constructor)
- Prevents byte stream attacks
- Cleaner separation

**Implementation:**
1. Create nested `SerializationProxy` class
2. `writeReplace()` returns proxy
3. Proxy's `readResolve()` reconstructs original object
4. `readObject()` throws exception to prevent direct deserialization

---

### Q8: Can you serialize an inner class?

**Difficulty**: Hard

**Answer**:
**Non-static inner classes** have implicit reference to outer class. If outer class is not serializable, you get `NotSerializableException`.

**Solution:**
- Make outer class serializable
- Use static nested class (no outer reference)
- Mark inner class field as `transient`

---

## Common Traps

### ❌ Trap 1: Forgetting defaultWriteObject/defaultReadObject

**Why it's wrong**:
If you implement custom `writeObject()`/`readObject()` but forget to call `defaultWriteObject()`/`defaultReadObject()`, non-transient fields are NOT serialized.

**Incorrect Code**:
```java
private void writeObject(ObjectOutputStream out) throws IOException {
    // Missing: out.defaultWriteObject();
    out.writeInt(customField);
}

private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // Missing: in.defaultReadObject();
    customField = in.readInt();
}

// Result: All non-transient fields are lost!
```

**Correct Code**:
```java
private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();  // ← Serialize non-transient fields first
    out.writeInt(customField);
}

private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();  // ← Deserialize non-transient fields first
    customField = in.readInt();
}
```

**Interview Tip**:
Always call `defaultWriteObject()` and `defaultReadObject()` first, then add custom logic.

---

### ❌ Trap 2: Externalizable Without No-Arg Constructor

**Why it's wrong**:
`Externalizable` requires a **public no-arg constructor**. Without it, deserialization fails.

**Incorrect Code**:
```java
class Employee implements Externalizable {
    private String name;
    
    public Employee(String name) {  // ← Only parameterized constructor
        this.name = name;
    }
    
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(name);
    }
    
    public void readExternal(ObjectInput in) throws IOException {
        this.name = in.readUTF();
    }
}

// Deserialization throws InvalidClassException: no valid constructor
```

**Correct Code**:
```java
class Employee implements Externalizable {
    private String name;
    
    public Employee() { }  // ← Required no-arg constructor
    
    public Employee(String name) {
        this.name = name;
    }
    
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(name);
    }
    
    public void readExternal(ObjectInput in) throws IOException {
        this.name = in.readUTF();
    }
}
```

**Interview Tip**:
`Externalizable` always needs a public no-arg constructor. `Serializable` does not.

---

### ❌ Trap 3: Singleton Broken by Serialization

**Why it's wrong**:
Deserialization creates a new instance, breaking the Singleton pattern.

**Incorrect Code**:
```java
class Singleton implements Serializable {
    private static final Singleton INSTANCE = new Singleton();
    
    private Singleton() { }
    
    public static Singleton getInstance() {
        return INSTANCE;
    }
}

// Serialize and deserialize
Singleton s1 = Singleton.getInstance();
// ... serialize s1 ...
Singleton s2 = // ... deserialize ...
System.out.println(s1 == s2);  // false ❌ (different instances)
```

**Correct Code**:
```java
class Singleton implements Serializable {
    private static final Singleton INSTANCE = new Singleton();
    
    private Singleton() { }
    
    public static Singleton getInstance() {
        return INSTANCE;
    }
    
    // ← Ensure same instance after deserialization
    private Object readResolve() {
        return INSTANCE;
    }
}

// Now s1 == s2 is true ✅
```

**Interview Tip**:
Always implement `readResolve()` in Singleton classes to maintain uniqueness.

---

## Related Topics

- [Serialization Basics](./01-Serialization-Basics.md) - Fundamentals of serialization
- [Serialization Best Practices](./03-Serialization-Best-Practices.md) - Security and alternatives
- [Design Patterns](../09-Design-Patterns/01-Design-Patterns.md) - Singleton pattern
- [Enums](../13-Enums/01-Enum-Deep-Dive.md) - Enum serialization (built-in readResolve)
- [Exception Handling](../03-Exception-Handling/01-Exception-Handling.md) - InvalidObjectException

---

*Last Updated: February 2026*  
*Java Version: 8, 11, 17, 21*
