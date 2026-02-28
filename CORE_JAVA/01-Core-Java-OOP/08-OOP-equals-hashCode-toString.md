# equals(), hashCode(), and toString() — The Contract

## Table of Contents
1. [The equals-hashCode Contract](#the-equals-hashcode-contract)
2. [Implementing equals() Correctly](#implementing-equals-correctly)
3. [Implementing hashCode() Correctly](#implementing-hashcode-correctly)
4. [What Happens When You Break the Contract](#what-happens-when-you-break-the-contract)
5. [toString() Best Practices](#tostring-best-practices)
6. [Interview Questions](#interview-questions)

---

## The equals-hashCode Contract

```
┌─────────────────────────────────────────────────────────────┐
│           THE CONTRACT (Must follow ALL rules)               │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  RULE 1: If a.equals(b) == true → a.hashCode() == b.hashCode()  │
│          (Equal objects MUST have equal hash codes)          │
│                                                             │
│  RULE 2: If a.hashCode() != b.hashCode() → a.equals(b) == false │
│          (Different hash codes → objects are NOT equal)      │
│                                                             │
│  RULE 3: If a.hashCode() == b.hashCode() → equals() may    │
│          be true OR false (hash collision is allowed)        │
│                                                             │
│  ⚠️ Override BOTH or NEITHER. Never just one.                │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Why This Matters
```java
// Without proper equals/hashCode:
Set<Person> set = new HashSet<>();
Person p1 = new Person("John", 30);
Person p2 = new Person("John", 30);

set.add(p1);
set.contains(p2);  // false! (without equals/hashCode override)
// HashSet uses hashCode() to find bucket, then equals() to match
```

---

## Implementing equals() Correctly

### The 5 Properties of equals()

| Property | Rule | Example |
|----------|------|---------|
| Reflexive | `a.equals(a)` must be `true` | Object equals itself |
| Symmetric | If `a.equals(b)` then `b.equals(a)` | Both ways must agree |
| Transitive | If `a.equals(b)` and `b.equals(c)` then `a.equals(c)` | Chain must hold |
| Consistent | Same result for same unchanged objects | Deterministic |
| Null-safe | `a.equals(null)` must be `false` | Never equal to null |

### Correct Implementation Pattern

```java
public class Employee {
    private String name;
    private int id;
    private double salary;
    
    @Override
    public boolean equals(Object obj) {
        // Step 1: Reference check (reflexive + performance)
        if (this == obj) return true;
        
        // Step 2: Null check + type check
        if (obj == null || getClass() != obj.getClass()) return false;
        
        // Step 3: Cast and compare fields
        Employee other = (Employee) obj;
        return this.id == other.id
            && Double.compare(this.salary, other.salary) == 0
            && Objects.equals(this.name, other.name);
    }
}
```

### Common Mistakes

```java
// ❌ Mistake 1: Wrong method signature (overloading instead of overriding!)
public boolean equals(Employee other) {  // ❌ Takes Employee, not Object!
    return this.id == other.id;
}
// This OVERLOADS equals, doesn't OVERRIDE it. Collections use Object version.

// ✅ Fix: Always use Object parameter
@Override
public boolean equals(Object obj) { ... }
```

```java
// ❌ Mistake 2: Using instanceof (breaks symmetry with subclasses)
@Override
public boolean equals(Object obj) {
    if (!(obj instanceof Employee)) return false;  // ⚠️ Problematic
    ...
}

class Manager extends Employee { ... }

Employee e = new Employee("John", 1);
Manager m = new Manager("John", 1);

e.equals(m);  // true  (Manager instanceof Employee)
m.equals(e);  // false (Employee NOT instanceof Manager) ← BREAKS SYMMETRY!

// ✅ Fix: Use getClass() instead of instanceof
if (obj == null || getClass() != obj.getClass()) return false;
```

```java
// ❌ Mistake 3: Using == for objects instead of equals()
return this.name == other.name;  // ❌ Compares references!

// ✅ Fix
return Objects.equals(this.name, other.name);  // Null-safe
```

```java
// ❌ Mistake 4: Using == for float/double (NaN, -0.0 issues)
return this.salary == other.salary;  // ❌ NaN != NaN, and -0.0 == 0.0

// ✅ Fix
return Double.compare(this.salary, other.salary) == 0;
return Float.compare(this.rate, other.rate) == 0;
```

---

## Implementing hashCode() Correctly

### Basic Rules
- Equal objects → equal hash codes (MUST)
- Use the SAME fields as equals()
- Good hash code = fewer collisions = better HashMap performance

### Using Objects.hash() (Simplest)
```java
@Override
public int hashCode() {
    return Objects.hash(name, id, salary);  // Uses same fields as equals()
}
```

### Manual Implementation (Better Performance)
```java
@Override
public int hashCode() {
    int result = 17;                        // Start with non-zero prime
    result = 31 * result + id;              // int field
    result = 31 * result + (name != null ? name.hashCode() : 0);  // Object field
    long temp = Double.doubleToLongBits(salary);
    result = 31 * result + (int)(temp ^ (temp >>> 32));  // double field
    return result;
}
```

### Why 31?
- 31 is an odd prime
- `31 * i` can be optimized by JVM to `(i << 5) - i` (fast bitshift)
- Gives good distribution of hash values

### Field Types Guide

| Field Type | Hash Contribution |
|-----------|-------------------|
| `boolean` | `(f ? 1 : 0)` |
| `byte/char/short/int` | `(int) f` |
| `long` | `(int)(f ^ (f >>> 32))` |
| `float` | `Float.floatToIntBits(f)` |
| `double` | `Double.doubleToLongBits(f)` then long treatment |
| Object | `(f == null ? 0 : f.hashCode())` |
| Array | `Arrays.hashCode(f)` |

---

## What Happens When You Break the Contract

### Only Override equals() Without hashCode()
```java
class Person {
    String name;
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person p) return name.equals(p.name);
        return false;
    }
    // ❌ No hashCode() override!
}

Map<Person, String> map = new HashMap<>();
Person p1 = new Person("John");
Person p2 = new Person("John");

map.put(p1, "Developer");
map.get(p2);  // null! ← Even though p1.equals(p2) is true

// Why? p1 and p2 have DIFFERENT hashCodes (default Object.hashCode)
// HashMap checks hashCode FIRST to find bucket
// Different hashCode → looks in wrong bucket → never finds it
```

### Only Override hashCode() Without equals()
```java
class Item {
    String code;
    
    @Override
    public int hashCode() { return code.hashCode(); }
    // ❌ No equals() override — uses Object.equals (reference check)
}

Set<Item> set = new HashSet<>();
Item i1 = new Item("A");
Item i2 = new Item("A");

set.add(i1);
set.add(i2);
set.size();  // 2! ← Both added because equals() says they're different objects
```

---

## toString() Best Practices

### Default Behavior
```java
Object obj = new Object();
System.out.println(obj);  // java.lang.Object@7852e922
// Format: ClassName@hexHashCode — not useful!
```

### Good toString()
```java
public class Employee {
    private int id;
    private String name;
    private double salary;
    
    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "', salary=" + salary + "}";
    }
    // Output: Employee{id=1, name='John', salary=50000.0}
}
```

### Using String.format() or StringBuilder
```java
@Override
public String toString() {
    return String.format("Employee{id=%d, name='%s', salary=%.2f}", id, name, salary);
}
```

### With Records (Java 16+)
```java
record Employee(int id, String name, double salary) { }
// toString() auto-generated: Employee[id=1, name=John, salary=50000.0]
```

### Tips
- Include all important fields
- Don't include sensitive data (passwords, tokens)
- Keep it concise — used in logs and debugging
- Consider using a library like Lombok `@ToString`

---

## Interview Questions

**Q: Why must we override both equals() and hashCode()?**
A: The contract requires equal objects to have equal hash codes. HashMap/HashSet use hashCode() to find the bucket, then equals() to match. Breaking this makes collections malfunction.

**Q: What happens if we only override equals()?**
A: Two equal objects may end up in different HashMap buckets (different hashCodes), so `map.get()` with an equal key returns null.

**Q: What happens if we only override hashCode()?**
A: Equal objects get same bucket, but equals() uses reference check (Object default), so duplicates appear in HashSet.

**Q: Why use getClass() instead of instanceof in equals()?**
A: `instanceof` breaks symmetry when subclasses are involved. `e.equals(m)` may be true but `m.equals(e)` false.

**Q: Can two non-equal objects have the same hashCode?**
A: Yes. This is called a hash collision. hashCode equality is necessary but not sufficient for object equality.

**Q: What is the default equals() behavior?**
A: `Object.equals()` uses `==` (reference comparison). Two distinct objects with same data are NOT equal unless you override equals().

**Q: What is the default hashCode() behavior?**
A: Based on memory address (or an internal identifier). Different objects get different hash codes even with same data.

**Q: How does HashMap use equals() and hashCode() together?**
A: `hashCode()` determines the bucket. `equals()` resolves collisions within the same bucket. Both are needed for correct behavior.

**Q: Should you use mutable fields in equals()/hashCode()?**
A: Avoid it. If a field changes after the object is in a HashMap, the object becomes "lost" — it's in the wrong bucket and can't be found.

**Q: How do Records handle equals/hashCode/toString?**
A: Records auto-generate all three based on all components. Two records with same component values are equal.
