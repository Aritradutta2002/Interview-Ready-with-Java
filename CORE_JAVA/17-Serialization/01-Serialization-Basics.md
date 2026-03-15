# Serialization Basics

## Table of Contents
1. [What is Serialization?](#what-is-serialization)
2. [Serializable Interface](#serializable-interface)
3. [ObjectOutputStream and ObjectInputStream](#objectoutputstream-and-objectinputstream)
4. [The Serialization Process](#the-serialization-process)
5. [transient and static Keywords](#transient-and-static-keywords)
6. [serialVersionUID](#serialversionuid)
7. [Interview Questions](#interview-questions)
8. [Common Traps](#common-traps)
9. [Related Topics](#related-topics)

---

## What is Serialization?

**Serialization** is the process of converting an object's state into a byte stream so it can be:
- Saved to a file
- Sent over a network
- Stored in a database
- Cached in memory

**Deserialization** is the reverse — reconstructing the object from the byte stream.

```
Object (Memory) ──serialize──▶ Byte Stream ──deserialize──▶ Object (Memory)
     ┌─────────┐                  [bytes]                    ┌─────────┐
     │ name    │                                             │ name    │
     │ age     │  ──────────────────────────────────────▶   │ age     │
     │ salary  │                                             │ salary  │
     └─────────┘                                             └─────────┘
```

### Why Serialization Matters

- **Persistence**: Save object state to disk
- **Network Communication**: Send objects between JVMs (RMI, EJB, distributed systems)
- **Caching**: Store objects in Redis, Memcached
- **Deep Cloning**: Create exact copies of objects
- **Session Management**: Store HTTP session data

---

## Serializable Interface

To make a class serializable, implement the `java.io.Serializable` interface.

```java
import java.io.Serializable;

public class Employee implements Serializable {
    private String name;
    private int age;
    private double salary;
    
    public Employee(String name, int age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }
    
    @Override
    public String toString() {
        return "Employee{name='" + name + "', age=" + age + ", salary=" + salary + "}";
    }
}
```

### Key Points

- `Serializable` is a **marker interface** (no methods to implement)
- Tells JVM: "This class can be serialized"
- All fields must be serializable (primitives, Strings, or other Serializable objects)
- If a field is not serializable, mark it `transient`

---

## ObjectOutputStream and ObjectInputStream

### Serialization (Writing)

```java
import java.io.*;

public class SerializationExample {
    public static void main(String[] args) {
        Employee emp = new Employee("John Doe", 30, 75000.0);
        
        try (FileOutputStream fileOut = new FileOutputStream("employee.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            
            out.writeObject(emp);
            System.out.println("Employee serialized to employee.ser");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### Deserialization (Reading)

```java
import java.io.*;

public class DeserializationExample {
    public static void main(String[] args) {
        try (FileInputStream fileIn = new FileInputStream("employee.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            
            Employee emp = (Employee) in.readObject();
            System.out.println("Deserialized Employee: " + emp);
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

**Output:**
```
Deserialized Employee: Employee{name='John Doe', age=30, salary=75000.0}
```

---

## The Serialization Process

### What Gets Serialized?

```
┌─────────────────────────────────────────────────────────┐
│                  Serialization Rules                     │
├─────────────────────────────────────────────────────────┤
│ ✅ Instance variables (non-transient, non-static)       │
│ ✅ Object's class metadata (class name, signature)      │
│ ✅ Entire object graph (referenced objects)             │
│ ❌ static variables (belong to class, not instance)     │
│ ❌ transient variables (explicitly excluded)            │
│ ❌ Methods (code is in .class file, not object state)   │
└─────────────────────────────────────────────────────────┘
```

### Object Graph Serialization

```java
class Address implements Serializable {
    String city;
    String country;
    
    public Address(String city, String country) {
        this.city = city;
        this.country = country;
    }
}

class Employee implements Serializable {
    String name;
    Address address;  // Referenced object
    
    public Employee(String name, Address address) {
        this.name = name;
        this.address = address;
    }
}

// Serializing Employee also serializes Address
Employee emp = new Employee("Alice", new Address("NYC", "USA"));
// Both Employee and Address objects are serialized
```

**Important:** If `Address` was NOT serializable, you'd get `NotSerializableException`.

---

## transient and static Keywords

### transient — Exclude from Serialization

```java
class User implements Serializable {
    private String username;
    private transient String password;  // ← NOT serialized
    private static int userCount = 0;   // ← NOT serialized (static)
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        userCount++;
    }
}

// Serialize
User user = new User("john", "secret123");
// ... serialize to file ...

// Deserialize
User deserializedUser = // ... read from file ...
System.out.println(deserializedUser.username);  // "john"
System.out.println(deserializedUser.password);  // null (transient)
System.out.println(User.userCount);             // 0 (static, not serialized)
```

### When to Use transient

- **Sensitive data**: passwords, credit card numbers
- **Derived fields**: calculated values that can be recomputed
- **Non-serializable fields**: database connections, file handles, threads
- **Large temporary data**: caches that can be rebuilt

```java
class Cache implements Serializable {
    private Map<String, String> data;
    private transient Map<String, String> tempCache;  // Don't serialize cache
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        tempCache = new HashMap<>();  // Rebuild cache after deserialization
    }
}
```

---

## serialVersionUID

### What is serialVersionUID?

A unique identifier for each Serializable class. Used during deserialization to verify that sender and receiver have compatible class versions.

```java
class Employee implements Serializable {
    private static final long serialVersionUID = 1L;  // ← Explicit UID
    
    private String name;
    private int age;
}
```

### Why It Matters

```
Serialization (Version 1)          Deserialization (Version 2)
┌──────────────────┐              ┌──────────────────┐
│ Employee         │              │ Employee         │
│ - name           │  ──bytes──▶  │ - name           │
│ - age            │              │ - age            │
│                  │              │ - salary (NEW!)  │
│ serialVersionUID │              │ serialVersionUID │
│ = 1L             │              │ = 1L             │
└──────────────────┘              └──────────────────┘
                                   ✅ Compatible (same UID)
```

### Without serialVersionUID

```java
// Version 1 (serialized)
class Employee implements Serializable {
    String name;
    int age;
}

// Version 2 (deserialize attempt)
class Employee implements Serializable {
    String name;
    int age;
    double salary;  // NEW FIELD
}

// Result: InvalidClassException (auto-generated UIDs don't match)
```

### Best Practice

```java
// ✅ Always declare serialVersionUID explicitly
private static final long serialVersionUID = 1L;

// ❌ Don't rely on auto-generated UID
// (changes with any class modification)
```

**How to generate:**
```bash
serialver Employee
# Output: Employee: static final long serialVersionUID = 8683452581122892189L;
```

---

## Interview Questions

### Q1: What is serialization in Java?

**Difficulty**: Easy

**Answer**:
Serialization is the process of converting an object into a byte stream for storage or transmission. Deserialization reconstructs the object from the byte stream. Used for persistence, network communication, caching, and deep cloning.

---

### Q2: What is the Serializable interface?

**Difficulty**: Easy

**Answer**:
`java.io.Serializable` is a marker interface (no methods) that tells the JVM a class can be serialized. Without it, attempting to serialize throws `NotSerializableException`.

---

### Q3: What happens if a parent class is Serializable but child is not?

**Difficulty**: Medium

**Answer**:
The child is automatically serializable (inheritance). However, if parent is NOT serializable but child is, the parent's fields are NOT serialized unless the parent has a no-arg constructor.

```java
// Parent NOT Serializable
class Parent {
    int x = 10;
    Parent() { }  // ← Must have no-arg constructor
}

class Child extends Parent implements Serializable {
    int y = 20;
}

// Serializing Child:
// - y is serialized (Child is Serializable)
// - x is NOT serialized (Parent is not Serializable)
// - On deserialization, Parent() constructor is called to initialize x
```

---

### Q4: What is transient keyword?

**Difficulty**: Easy

**Answer**:
`transient` marks a field to be excluded from serialization. After deserialization, transient fields have default values (null for objects, 0 for numbers, false for boolean).

**Use cases**: passwords, derived fields, non-serializable objects, temporary caches.

---

### Q5: What is serialVersionUID?

**Difficulty**: Medium

**Answer**:
A unique identifier for a Serializable class. During deserialization, JVM checks if the serialized object's UID matches the current class UID. Mismatch throws `InvalidClassException`.

**Best practice**: Always declare explicitly:
```java
private static final long serialVersionUID = 1L;
```

---

### Q6: Can static variables be serialized?

**Difficulty**: Easy

**Answer**:
No. Static variables belong to the class, not instances. Serialization saves instance state only. After deserialization, static variables have their current class values, not serialized values.

---

### Q7: What happens if a non-serializable object is referenced?

**Difficulty**: Medium

**Answer**:
`NotSerializableException` is thrown at runtime.

```java
class Address { }  // NOT Serializable

class Employee implements Serializable {
    Address address;  // ← Problem!
}

// Serializing Employee throws NotSerializableException
```

**Solution**: Make `Address` serializable or mark the field `transient`.

---

### Q8: How to serialize a Singleton class?

**Difficulty**: Hard

**Answer**:
Problem: Deserialization creates a new instance, breaking Singleton.

**Solution**: Implement `readResolve()`:
```java
class Singleton implements Serializable {
    private static final Singleton INSTANCE = new Singleton();
    
    private Singleton() { }
    
    public static Singleton getInstance() {
        return INSTANCE;
    }
    
    // ← Ensures same instance after deserialization
    protected Object readResolve() {
        return INSTANCE;
    }
}
```

---

### Q9: Difference between Serializable and Externalizable?

**Difficulty**: Medium

**Answer**:

| Serializable | Externalizable |
|--------------|----------------|
| Marker interface (no methods) | Has `writeExternal()` and `readExternal()` |
| Automatic serialization | Manual control over serialization |
| Serializes all non-transient fields | You choose what to serialize |
| Slower | Faster (less overhead) |
| Easy to use | More complex |

---

### Q10: Can you serialize a lambda expression?

**Difficulty**: Hard

**Answer**:
Yes, if the target type is serializable:

```java
Runnable r = (Runnable & Serializable) () -> System.out.println("Hello");
// Cast to intersection type: Runnable AND Serializable
```

Without the cast, lambda is NOT serializable by default.

---

## Common Traps

### ❌ Trap 1: Forgetting serialVersionUID

**Why it's wrong**:
Without explicit `serialVersionUID`, Java auto-generates one based on class structure. Any change (adding a field, method) changes the UID, breaking deserialization.

**Incorrect Code**:
```java
class Employee implements Serializable {
    // No serialVersionUID
    String name;
    int age;
}

// Later, add a field:
class Employee implements Serializable {
    String name;
    int age;
    double salary;  // NEW
}

// Deserializing old data throws InvalidClassException
```

**Correct Code**:
```java
class Employee implements Serializable {
    private static final long serialVersionUID = 1L;  // ← Explicit UID
    
    String name;
    int age;
    double salary;  // Adding field is now safe
}
```

**Interview Tip**:
Always declare `serialVersionUID` explicitly. Use `1L` for simplicity or generate with `serialver` tool.

---

### ❌ Trap 2: Serializing Sensitive Data

**Why it's wrong**:
Serialized data can be read by anyone with access to the file/stream. Passwords, keys, and tokens should NOT be serialized.

**Incorrect Code**:
```java
class User implements Serializable {
    String username;
    String password;  // ← Exposed in serialized form!
}
```

**Correct Code**:
```java
class User implements Serializable {
    String username;
    transient String password;  // ← Excluded from serialization
    
    // Re-authenticate after deserialization
}
```

**Interview Tip**:
Mark sensitive fields as `transient`. Consider encrypting serialized data.

---

### ❌ Trap 3: Serializing Non-Serializable Fields

**Why it's wrong**:
If a field's type is not serializable, you get `NotSerializableException` at runtime.

**Incorrect Code**:
```java
class Employee implements Serializable {
    String name;
    Thread workerThread;  // ← Thread is NOT Serializable!
}

// Throws NotSerializableException
```

**Correct Code**:
```java
class Employee implements Serializable {
    String name;
    transient Thread workerThread;  // ← Exclude from serialization
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        workerThread = new Thread();  // Recreate after deserialization
    }
}
```

**Interview Tip**:
Mark non-serializable fields (threads, streams, connections) as `transient` and reinitialize them in `readObject()`.

---

## Related Topics

- [I/O & NIO](../15-IO-NIO/01-IO-NIO.md) - File operations and streams
- [Reflection & Annotations](../14-Reflection-Annotations/01-Reflection-and-Annotations.md) - Runtime class inspection
- [Enums](../13-Enums/01-Enum-Deep-Dive.md) - Enum serialization (special handling)
- [Design Patterns](../09-Design-Patterns/01-Design-Patterns.md) - Singleton serialization
- [Exception Handling](../03-Exception-Handling/01-Exception-Handling.md) - Handling serialization exceptions

---

*Last Updated: February 2026*  
*Java Version: 8, 11, 17, 21*
