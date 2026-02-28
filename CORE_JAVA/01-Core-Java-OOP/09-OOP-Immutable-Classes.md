# How to Create Immutable Classes in Java

## What is an Immutable Class?
A class whose instances cannot be modified after creation. Once created, the state never changes.

**Built-in Immutable Classes:** `String`, `Integer`, `Long`, `Double`, `BigDecimal`, `LocalDate`, `LocalDateTime`

---

## 5 Rules to Create Immutable Class

```
┌─────────────────────────────────────────────────────────────┐
│           5 Rules for Immutable Class                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. Declare class as FINAL (prevent subclassing)            │
│  2. Make all fields PRIVATE and FINAL                       │
│  3. NO setter methods                                        │
│  4. Initialize all fields via CONSTRUCTOR                    │
│  5. Return DEFENSIVE COPIES of mutable fields               │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Complete Example

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public final class Employee {                          // Rule 1: final class
    private final String name;                          // Rule 2: private final
    private final int age;                              // Rule 2: private final
    private final Date joinDate;                        // Rule 2: private final (mutable object!)
    private final List<String> skills;                  // Rule 2: private final (mutable collection!)
    
    // Rule 4: Initialize via constructor
    public Employee(String name, int age, Date joinDate, List<String> skills) {
        this.name = name;                               // String is immutable — safe
        this.age = age;                                 // Primitive — safe
        this.joinDate = new Date(joinDate.getTime());   // Rule 5: Defensive copy on input
        this.skills = new ArrayList<>(skills);           // Rule 5: Defensive copy on input
    }
    
    // Rule 3: Only getters, NO setters
    public String getName() { return name; }            // String is immutable — safe to return
    public int getAge() { return age; }                 // Primitive — safe to return
    
    // Rule 5: Return defensive copies of mutable fields
    public Date getJoinDate() {
        return new Date(joinDate.getTime());            // Return copy, not original
    }
    
    public List<String> getSkills() {
        return Collections.unmodifiableList(skills);    // Return unmodifiable view
    }
}
```

### Why Each Rule Matters

```java
// WITHOUT Rule 1 (final class): Subclass can break immutability
class MutableEmployee extends Employee {
    private String mutableField;
    public void setMutableField(String value) { mutableField = value; }
}

// WITHOUT Rule 5 (defensive copy): External code can modify state
Employee emp = new Employee("John", 30, date, skills);
Date d = emp.getJoinDate();
d.setYear(2000);           // ❌ Modifies internal state if no defensive copy!

List<String> s = emp.getSkills();
s.add("Hacking");          // ❌ Modifies internal list if no defensive copy!
```

---

## Defensive Copying Explained

### What is Defensive Copying?
Creating a new copy of mutable objects when accepting input AND returning output.

```java
// ❌ Bad: No defensive copy
public Employee(String name, Date joinDate) {
    this.joinDate = joinDate;  // Stores reference — caller can modify!
}

public Date getJoinDate() {
    return joinDate;           // Returns reference — caller can modify!
}

// ✅ Good: Defensive copy both ways
public Employee(String name, Date joinDate) {
    this.joinDate = new Date(joinDate.getTime());  // Copy on input
}

public Date getJoinDate() {
    return new Date(joinDate.getTime());           // Copy on output
}
```

### When NOT Needed
- Primitive types (`int`, `boolean`, etc.) — always copied by value
- Immutable objects (`String`, `Integer`, `LocalDate`) — safe to share reference

---

## Java Records as Immutable Data Carriers (Java 16+)

```java
// Records are automatically immutable (but NOT fully defensive)
public record Person(String name, int age) { }

// Compiler generates:
// - final class
// - private final fields
// - Constructor, getters (name(), age())
// - equals(), hashCode(), toString()

Person p = new Person("John", 30);
p.name();  // "John"
p.age();   // 30

// ⚠️ WARNING: Records with mutable fields are NOT truly immutable
public record Team(String name, List<String> members) { }

Team team = new Team("Dev", new ArrayList<>(List.of("Alice", "Bob")));
team.members().add("Charlie");  // ❌ Modifies internal list!

// Fix: Add defensive copy in compact constructor
public record Team(String name, List<String> members) {
    public Team {
        members = List.copyOf(members);  // Immutable copy
    }
}
```

---

## Benefits of Immutability

| Benefit | Explanation |
|---------|-------------|
| Thread safety | No synchronization needed — state never changes |
| Safe HashMap keys | hashCode never changes after creation |
| Simpler code | No need to track state changes |
| Cache-friendly | Can be freely shared and cached |
| Failure atomicity | Object is always in valid state |

---

## Common Mistakes

```java
// Mistake 1: Returning mutable field directly
public List<String> getItems() {
    return items;  // ❌ Caller can modify!
}
// Fix: return Collections.unmodifiableList(items);

// Mistake 2: Not copying mutable input
public MyClass(Date date) {
    this.date = date;  // ❌ Caller retains reference!
}
// Fix: this.date = new Date(date.getTime());

// Mistake 3: Forgetting to make class final
public class NotTrulyImmutable {  // ❌ Can be subclassed!
    private final int value;
}
// Fix: public final class TrulyImmutable { ... }

// Mistake 4: Using arrays (always mutable)
private final int[] data;
public int[] getData() { return data; }  // ❌ Caller can modify!
// Fix: return data.clone(); OR return Arrays.copyOf(data, data.length);
```

---

## Interview Q&A

**Q: How to create an immutable class in Java?**
A: Make class final, all fields private final, no setters, constructor initialization, defensive copies of mutable fields.

**Q: Why is String immutable?**
A: String Pool optimization, thread safety, security (class loading), hashCode caching.

**Q: Can an immutable class have mutable fields?**
A: Yes, but must use defensive copying on both input (constructor) and output (getters).

**Q: What is defensive copying?**
A: Creating a new copy of mutable objects when accepting/returning them, so external code can't modify internal state.

**Q: Are Records truly immutable?**
A: Only if all components are immutable. Mutable components (List, Date) require defensive copying in compact constructor.

**Q: Why make immutable class final?**
A: Prevents subclasses from adding mutable state or overriding methods to break immutability.

**Q: Can we modify an immutable object using reflection?**
A: Technically yes, but it violates the contract. Module system (Java 9+) restricts this further.

**Q: How does immutability help with thread safety?**
A: Immutable objects can't change state, so no race conditions, no synchronization needed.
