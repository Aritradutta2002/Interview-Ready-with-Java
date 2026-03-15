# String Internals - Complete Interview Guide

## Table of Contents
1. [String Immutability](#string-immutability)
2. [String Pool (String Constant Pool)](#string-pool)
3. [String vs StringBuilder vs StringBuffer](#string-vs-stringbuilder-vs-stringbuffer)
4. [String Methods](#important-string-methods)
5. [String Interning](#string-interning)
6. [Interview Questions](#interview-qa)

---

## String Immutability

### Why is String Immutable?

```
┌─────────────────────────────────────────────────────────────┐
│           Why String is Immutable                            │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. STRING POOL OPTIMIZATION                                │
│     Multiple references can safely share same String         │
│     → Memory savings                                        │
│                                                             │
│  2. THREAD SAFETY                                           │
│     Immutable objects are inherently thread-safe             │
│     → No synchronization needed                             │
│                                                             │
│  3. SECURITY                                                │
│     Cannot be modified after creation                        │
│     → Safe for passwords, URLs, class names                 │
│                                                             │
│  4. HASHCODE CACHING                                        │
│     hashCode computed once, cached                           │
│     → HashMap key performance                               │
│                                                             │
│  5. CLASS LOADING                                           │
│     Class names are Strings — if mutable, security risk      │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### How String is Made Immutable

```java
public final class String {          // final → cannot extend
    private final byte[] value;       // final → cannot reassign reference
    private final byte coder;         // Compact Strings (Java 9+)
    private int hash;                 // Cached hashCode (computed once)
    
    // No methods modify value[] — all return NEW String
}
```

### Proof of Immutability

```java
String s1 = "Hello";
String s2 = s1;

s1 = s1.concat(" World");  // Creates NEW String object

System.out.println(s1);  // "Hello World" (new object)
System.out.println(s2);  // "Hello" (original unchanged)

// What happened in memory:
// s1 → "Hello"          (original, still in pool)
// s2 → "Hello"          (same reference as original s1)
// s1 → "Hello World"    (NEW object, s1 re-pointed)
```

### String Operations Always Create New Objects

```java
String s = "Hello";
s.toUpperCase();           // Returns "HELLO", s is still "Hello"
s.substring(0, 3);         // Returns "Hel", s is still "Hello"
s.replace('l', 'r');       // Returns "Herro", s is still "Hello"
s.trim();                  // Returns trimmed, s is unchanged

// Must reassign to use result
s = s.toUpperCase();       // Now s points to "HELLO"
```

---

## String Pool

### How String Pool Works

```
┌─────────────────────────────────────────────────────────────┐
│                         HEAP                                 │
│                                                             │
│  ┌─────────────────────────────────────────────┐            │
│  │           STRING POOL                       │            │
│  │                                             │            │
│  │  "Hello"   "World"   "Java"   "abc"        │            │
│  │     ↑                                       │            │
│  │   s1, s2 (both point here)                  │            │
│  └─────────────────────────────────────────────┘            │
│                                                             │
│  ┌──────────────┐                                          │
│  │ String Object│  ← new String("Hello") lives HERE        │
│  │ value="Hello"│    NOT in the pool                       │
│  └──────────────┘                                          │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### String Creation: Literal vs new

```java
// 1. String Literal → Pool
String s1 = "Hello";        // Created in String Pool
String s2 = "Hello";        // Reuses same object from Pool
System.out.println(s1 == s2);  // true (same reference)

// 2. new String() → Heap (NOT pool)
String s3 = new String("Hello");  // New object in Heap
System.out.println(s1 == s3);     // false (different references)
System.out.println(s1.equals(s3)); // true (same content)

// 3. intern() → moves to Pool
String s4 = s3.intern();
System.out.println(s1 == s4);     // true (now same reference)
```

### How Many Objects Created?

```java
// Q: How many objects does this create?
String s = new String("Hello");

// A: UP TO 2 objects:
// 1. "Hello" literal in String Pool (if not already there)
// 2. new String object on Heap

// Q: How many objects?
String s1 = "Hello";          // 1 object (pool) - if "Hello" not already in pool
String s2 = "Hello";          // 0 objects (reuses from pool)
String s3 = new String("Hello"); // 1 object (heap) - "Hello" already in pool

// Q: How many objects?
String s = "Hello" + " World";   // Compiler optimizes to "Hello World" (1 pool object)

// Q: How many objects?
String a = "Hello";
String b = " World";
String c = a + b;             // Creates new StringBuilder internally → new String on heap
// c is NOT in the pool!
```

### == vs equals() for Strings

```java
String s1 = "Hello";
String s2 = "Hello";
String s3 = new String("Hello");
String s4 = new String("Hello");

s1 == s2        // true  (same pool reference)
s1 == s3        // false (pool vs heap)
s3 == s4        // false (different heap objects)

s1.equals(s2)   // true  (same content)
s1.equals(s3)   // true  (same content)
s3.equals(s4)   // true  (same content)

// RULE: Always use equals() for String comparison!
```

---

## String vs StringBuilder vs StringBuffer

### Comparison

| Feature | String | StringBuilder | StringBuffer |
|---------|--------|---------------|--------------|
| Mutability | Immutable | Mutable | Mutable |
| Thread-safe | Yes (immutable) | No | Yes (synchronized) |
| Performance | Slow (creates new objects) | Fast | Slower than StringBuilder |
| Storage | String Pool / Heap | Heap | Heap |
| Use case | Few modifications | Single-threaded concatenation | Multi-threaded concatenation |

### Why StringBuilder is Faster

```java
// ❌ Bad: String concatenation in loop (O(n²) — creates n objects)
String result = "";
for (int i = 0; i < 10000; i++) {
    result += i;  // Creates new String each iteration!
}

// ✅ Good: StringBuilder (O(n) — modifies same buffer)
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 10000; i++) {
    sb.append(i);  // Modifies internal char array
}
String result = sb.toString();

// Performance difference: StringBuilder is ~100x faster for large loops
```

### StringBuilder Methods

```java
StringBuilder sb = new StringBuilder("Hello");

sb.append(" World");          // "Hello World"
sb.insert(5, ",");            // "Hello, World"
sb.delete(5, 6);              // "Hello World"
sb.replace(6, 11, "Java");   // "Hello Java"
sb.reverse();                 // "avaJ olleH"
sb.charAt(0);                 // 'a'
sb.length();                  // 10
sb.capacity();                // Internal buffer capacity (default 16 + initial string length)
sb.setCharAt(0, 'A');         // "AvaJ olleH"
sb.deleteCharAt(0);           // "vaJ olleH"

String result = sb.toString(); // Convert to String
```

### StringBuffer (Thread-Safe StringBuilder)

```java
// Same API as StringBuilder but synchronized
StringBuffer buffer = new StringBuffer("Hello");

// Thread-safe operations
buffer.append(" World");      // synchronized method
buffer.insert(5, ",");        // synchronized method

// Use when multiple threads modify same string
// Otherwise, prefer StringBuilder (faster)
```

### String Concatenation Behind the Scenes

```java
// Compiler optimization (Java 9+: invokedynamic)
String s = "Hello" + " " + "World";
// Compiler optimizes to: "Hello World" (single constant)

// With variables (Java 9+ uses StringConcatFactory)
String name = "John";
String greeting = "Hello " + name + "!";
// Pre-Java 9: new StringBuilder().append("Hello ").append(name).append("!").toString()
// Java 9+: Uses invokedynamic → more efficient
```

---

## Important String Methods

```java
String s = "Hello World";

// Length & Chars
s.length();                  // 11
s.charAt(0);                 // 'H'
s.isEmpty();                 // false
s.isBlank();                 // false (Java 11+)

// Search
s.indexOf('o');              // 4 (first occurrence)
s.lastIndexOf('o');          // 7 (last occurrence)
s.contains("World");         // true
s.startsWith("Hello");       // true
s.endsWith("World");         // true

// Substring
s.substring(6);              // "World"
s.substring(0, 5);           // "Hello"

// Transform
s.toUpperCase();             // "HELLO WORLD"
s.toLowerCase();             // "hello world"
s.trim();                    // Remove leading/trailing whitespace
s.strip();                   // Unicode-aware trim (Java 11+)
s.replace('l', 'r');         // "Herro Worrd"
s.replaceAll("\\s+", "-");   // "Hello-World" (regex)

// Split & Join
String[] parts = "a,b,c".split(",");     // ["a", "b", "c"]
String joined = String.join("-", parts);  // "a-b-c"

// Compare
s.equals("Hello World");          // true
s.equalsIgnoreCase("hello world"); // true
s.compareTo("Apple");              // > 0 (lexicographic)

// Format
String formatted = String.format("Name: %s, Age: %d", "John", 30);
// Java 15+: "Name: %s, Age: %d".formatted("John", 30);

// Convert
String.valueOf(123);          // "123"
String.valueOf(true);         // "true"
char[] chars = s.toCharArray(); // Convert to char array

// Java 11+ Methods
"  ".isBlank();               // true
"Hello\nWorld".lines();       // Stream<String>
"Ab".repeat(3);               // "AbAbAb"
"  Hello  ".strip();          // "Hello"
"  Hello  ".stripLeading();   // "Hello  "
"  Hello  ".stripTrailing();  // "  Hello"
```

---

## String Interning

### What is intern()?

```java
// intern() returns canonical representation from String Pool
String s1 = new String("Hello");  // Heap object
String s2 = s1.intern();          // Returns pool reference
String s3 = "Hello";              // Pool reference

System.out.println(s2 == s3);     // true (both from pool)
System.out.println(s1 == s2);     // false (heap vs pool)
```

### When to Use intern()
- Reading many duplicate strings from external source (file, database)
- Reduces memory when same strings appear frequently
- Be cautious: interning too many unique strings wastes PermGen/Metaspace

---

## Interview Q&A

**Q: Why is String immutable in Java?**
A: For String Pool optimization, thread safety, security (class loading, network connections), and hashCode caching.

**Q: How many objects created by `new String("Hello")`?**
A: Up to 2: One in String Pool (literal "Hello") and one on Heap (new String object).

**Q: Difference between `==` and `equals()` for Strings?**
A: `==` compares references (memory address). `equals()` compares content. Always use `equals()` for String comparison.

**Q: When to use StringBuilder vs StringBuffer?**
A: StringBuilder for single-threaded (faster). StringBuffer for multi-threaded (synchronized). Both for string modification.

**Q: Why is String concatenation slow in loops?**
A: Each `+` creates a new String object. O(n²) total. Use StringBuilder for O(n).

**Q: What is String Pool and where is it located?**
A: A cache of String literals in Heap memory (moved from PermGen in Java 7). Enables reuse of identical strings.

**Q: What does `intern()` do?**
A: Returns canonical representation from String Pool. If string not in pool, adds it. If already there, returns existing reference.

**Q: Is String a primitive or object?**
A: Object. But Java gives it special treatment: literal syntax, Pool, `+` operator overloading.

**Q: Can you modify a String using reflection?**
A: Technically yes (access private `value` field), but breaks immutability contract. Never do this in production.

**Q: What is Compact Strings (Java 9+)?**
A: Strings that contain only Latin-1 characters use `byte[]` with LATIN1 encoding (1 byte/char) instead of UTF-16 (2 bytes/char). Saves ~50% memory for ASCII strings.
