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

### Basic Level

**Q: Why must we override both equals() and hashCode()?**
A: The contract requires equal objects to have equal hash codes. HashMap/HashSet use hashCode() to find the bucket, then equals() to match. Breaking this makes collections malfunction.

**Q: What happens if we only override equals()?**
A: Two equal objects may end up in different HashMap buckets (different hashCodes), so `map.get()` with an equal key returns null.

**Q: What happens if we only override hashCode()?**
A: Equal objects get same bucket, but equals() uses reference check (Object default), so duplicates appear in HashSet.

**Q: What is the default equals() behavior?**
A: `Object.equals()` uses `==` (reference comparison). Two distinct objects with same data are NOT equal unless you override equals().

**Q: What is the default hashCode() behavior?**
A: Based on memory address (or an internal identifier). Different objects get different hash codes even with same data.

**Q: Can two non-equal objects have the same hashCode()?**
A: Yes. This is called a hash collision. hashCode equality is necessary but not sufficient for object equality.

### Intermediate Level

**Q: Why use getClass() instead of instanceof in equals()?**
A: `instanceof` breaks symmetry when subclasses are involved. `e.equals(m)` may be true but `m.equals(e)` false.

**Q: How does HashMap use equals() and hashCode() together?**
A: `hashCode()` determines the bucket. `equals()` resolves collisions within the same bucket. Both are needed for correct behavior.

**Q: Should you use mutable fields in equals()/hashCode()?**
A: Avoid it. If a field changes after the object is in a HashMap, the object becomes "lost" — it's in the wrong bucket and can't be found.

**Q: How do Records handle equals/hashCode/toString?**
A: Records auto-generate all three based on all components. Two records with same component values are equal.

**Q: What are the 5 properties of equals()?**
A: Reflexive (a.equals(a) = true), Symmetric (a.equals(b) = b.equals(a)), Transitive (a=b, b=c → a=c), Consistent (same result for unchanged objects), Null-safe (a.equals(null) = false).

**Q: Why is 31 used as a multiplier in hashCode()?**
A: 31 is an odd prime that gives good distribution. JVM can optimize `31 * i` to `(i << 5) - i` (fast bitshift operation).

**Q: What's the difference between == and equals()?**
A: `==` compares references (memory addresses). `equals()` compares logical equality (content). For primitives, `==` compares values.

### Advanced Level

**Q: What happens if you violate the equals-hashCode contract?**
A: HashMap/HashSet malfunction. Equal objects may be stored as duplicates, or lookups may fail even when the key exists.

**Q: Can you override equals() without overriding hashCode() in a class that will never be used in collections?**
A: Technically yes, but it's bad practice. Future code might use it in collections, and it violates the general contract documented in Object class.

**Q: How do you handle inheritance in equals()?**
A: Use `getClass()` for strict type checking (no subclass equality). Use `instanceof` only if you're okay with subclass instances being equal to parent instances (risky for symmetry).

**Q: What's the performance impact of a poor hashCode() implementation?**
A: If all objects return the same hashCode, HashMap degrades to O(n) instead of O(1) because all entries end up in the same bucket (linked list or tree).

**Q: How does String implement hashCode()?**
A: `s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]` where s[i] is the ith character. Cached after first calculation (String is immutable).

**Q: What is the difference between equals() and compareTo()?**
A: `equals()` checks equality. `compareTo()` establishes ordering. They should be consistent: `a.equals(b)` should imply `a.compareTo(b) == 0`, but it's not required (e.g., BigDecimal).

**Q: Can equals() be inconsistent with compareTo()?**
A: Yes, but it's discouraged. Example: `BigDecimal("1.0").equals(BigDecimal("1.00"))` is false, but `compareTo()` returns 0. This can cause issues in sorted collections.

### Scenario-Based Questions

**Q: What will this code print?**
```java
String s1 = "hello";
String s2 = "hello";
String s3 = new String("hello");
System.out.println(s1 == s2);
System.out.println(s1 == s3);
System.out.println(s1.equals(s3));
```
A: true, false, true. s1 and s2 point to the same string pool object. s3 is a new object in heap. equals() compares content.

**Q: What's wrong with this equals() implementation?**
```java
public boolean equals(Employee other) {
    return this.id == other.id;
}
```
A: Wrong signature - takes Employee instead of Object. This overloads equals(), doesn't override it. Collections will use Object.equals() (reference check).

**Q: What will happen with this code?**
```java
Set<Person> set = new HashSet<>();
Person p = new Person("John", 30);
set.add(p);
p.age = 31;  // Modify after adding
System.out.println(set.contains(p));
```
A: Prints false (likely). Modifying the age changes hashCode, so the object is now in the wrong bucket and can't be found.

**Q: What's the issue with this hashCode()?**
```java
public int hashCode() {
    return 42;  // Constant
}
```
A: Legal but terrible. All objects have the same hashCode, causing all HashMap entries to collide in one bucket, degrading performance to O(n).

**Q: What will this code print?**
```java
Integer a = 127;
Integer b = 127;
Integer c = 128;
Integer d = 128;
System.out.println(a == b);
System.out.println(c == d);
```
A: true, false. Integer caches values -128 to 127. a and b point to the same cached object. c and d are different objects. Always use equals() for wrapper classes.

### Tricky Interview Scenarios

**Q: What's wrong with this code?**
```java
class Parent {
    private int id;
    public boolean equals(Object o) {
        return ((Parent) o).id == this.id;
    }
}
class Child extends Parent {
    private String name;
    public boolean equals(Object o) {
        return super.equals(o) && ((Child) o).name.equals(this.name);
    }
}
```
A: Multiple issues: No null check, no type check (ClassCastException risk), no hashCode() override, accessing private field from parent (won't compile), symmetry broken between Parent and Child.

**Q: Why does this fail?**
```java
Map<double[], String> map = new HashMap<>();
double[] key1 = {1.0, 2.0};
double[] key2 = {1.0, 2.0};
map.put(key1, "value");
System.out.println(map.get(key2));
```
A: Prints null. Arrays use default Object.equals() (reference check) and Object.hashCode(). key1 and key2 are different objects. Use Arrays.equals() and Arrays.hashCode() or wrap in a custom class.

**Q: What's the output?**
```java
String s1 = "hello";
String s2 = new String("hello").intern();
System.out.println(s1 == s2);
```
A: true. intern() returns the string pool reference. Both s1 and s2 now point to the same pooled string.
