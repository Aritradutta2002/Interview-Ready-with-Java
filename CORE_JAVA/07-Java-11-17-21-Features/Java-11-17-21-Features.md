# Java 11, 17, 21 Features

## Java 11 Features (LTS)

### 1. var in Lambda Parameters
```java
// Java 8
list.stream().map(s -> s.toUpperCase()).collect(toList());

// Java 11 - var with annotations
list.stream().map((@Nonnull var s) -> s.toUpperCase()).collect(toList());
```

### 2. String Methods
```java
// isBlank() - checks if empty or only whitespace
"  ".isBlank();  // true
"".isBlank();    // true
"Hello".isBlank();  // false

// lines() - returns Stream<String>
"Line1\nLine2\nLine3".lines().forEach(System.out::println);

// strip(), stripLeading(), stripTrailing()
"  Hello  ".strip();          // "Hello" (Unicode-aware)
"  Hello  ".stripLeading();   // "Hello  "
"  Hello  ".stripTrailing();  // "  Hello"

// repeat()
"Ab".repeat(3);  // "AbAbAb"
```

### 3. File Methods
```java
// Read file as string
String content = Files.readString(Path.of("file.txt"));

// Write string to file
Files.writeString(Path.of("output.txt"), "Hello World");
```

### 4. Collection to Array
```java
List<String> list = List.of("A", "B", "C");
String[] array = list.toArray(String[]::new);  // Already in Java 8
// Java 11: toArray without parameter returns Object[]
```

### 5. Running Java Files Directly
```bash
# No need to compile first
java HelloWorld.java

# With arguments
java HelloWorld.java arg1 arg2
```

---

## Java 17 Features (LTS)

### 1. Sealed Classes
```java
// Restrict which classes can extend/implement
public sealed class Shape permits Circle, Rectangle, Triangle {
    // Only Circle, Rectangle, Triangle can extend Shape
}

public final class Circle extends Shape {
    private final double radius;
}

public final class Rectangle extends Shape {
    private final double width, height;
}

// Non-sealed allows further extension
public non-sealed class Triangle extends Shape {
    // Can be extended by other classes
}

// Sealed interfaces
public sealed interface Vehicle permits Car, Bike, Truck { }
```

### 2. Pattern Matching for instanceof
```java
// Before Java 16
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}

// Java 16+ (Pattern matching)
if (obj instanceof String s) {
    System.out.println(s.length());
}

// With conditions
if (obj instanceof String s && s.length() > 5) {
    System.out.println(s.toUpperCase());
}
```

### 3. Records
```java
// Immutable data carrier
public record Person(String name, int age) {
    // Compiler generates:
    // - Constructor
    // - Getters: name(), age()
    // - equals(), hashCode(), toString()
}

// Usage
Person p = new Person("John", 30);
p.name();  // "John"
p.age();   // 30

// With validation
public record Person(String name, int age) {
    public Person {
        if (age < 0) throw new IllegalArgumentException("Age cannot be negative");
    }
}

// With additional methods
public record Person(String name, int age) {
    public boolean isAdult() {
        return age >= 18;
    }
    
    // Static factory
    public static Person anonymous() {
        return new Person("Anonymous", 0);
    }
}

// Record in pattern matching (Java 21)
if (obj instanceof Person(String name, int age)) {
    System.out.println(name + " is " + age);
}
```

### 4. Text Blocks
```java
// Multi-line strings
String json = """
    {
        "name": "John",
        "age": 30,
        "city": "NYC"
    }
    """;

String html = """
    <html>
        <body>
            <h1>Hello</h1>
        </body>
    </html>
    """;

// Escape sequences
String text = """
    Line 1\n\
    Line 2
    """;  // \ prevents newline

String formatted = """
    Name: %s
    Age: %d
    """.formatted("John", 30);
```

### 5. Switch Expressions (Enhanced)
```java
// Expression form (returns value)
String result = switch (day) {
    case MONDAY, FRIDAY -> "Work";
    case SATURDAY, SUNDAY -> "Weekend";
    default -> "Midweek";
};

// With yield (for blocks)
int numLetters = switch (day) {
    case MONDAY, FRIDAY, SUNDAY -> 6;
    case TUESDAY -> {
        System.out.println("Tuesday");
        yield 7;
    }
    case THURSDAY, SATURDAY -> 8;
    case WEDNESDAY -> 9;
};

// Multiple case labels
switch (status) {
    case PENDING, PROCESSING -> process();
    case COMPLETED -> complete();
    case FAILED -> retry();
}
```

### 6. Helpful NullPointerException
```java
// Java 8
Exception in thread "main" java.lang.NullPointerException

// Java 17+ (detailed message)
Exception in thread "main" java.lang.NullPointerException: 
    Cannot invoke "String.length()" because "str" is null

Exception in thread "main" java.lang.NullPointerException: 
    Cannot read field "age" because "person" is null
```

---

## Java 21 Features (LTS)

### 1. Virtual Threads (Project Loom)
```java
// Traditional platform threads (expensive)
ExecutorService executor = Executors.newFixedThreadPool(200);

// Virtual threads (lightweight, millions possible)
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    IntStream.range(0, 10000).forEach(i -> {
        executor.submit(() -> {
            Thread.sleep(Duration.ofSeconds(1));
            return i;
        });
    });
}

// Create virtual thread directly
Thread vThread = Thread.ofVirtual().start(() -> {
    System.out.println("Virtual thread");
});

// Factory
ThreadFactory factory = Thread.ofVirtual().factory();
Thread thread = factory.newThread(task);
```

**Benefits:**
- Lightweight (can create millions)
- No thread pool needed
- Blocking operations don't block OS thread
- Best for I/O-bound tasks

### 2. Pattern Matching for Switch
```java
// Pattern matching in switch
String formatted = switch (obj) {
    case Integer i -> "Integer: " + i;
    case Long l -> "Long: " + l;
    case Double d -> "Double: " + d;
    case String s -> "String: " + s;
    default -> "Unknown";
};

// With guards
String result = switch (obj) {
    case String s && s.length() > 5 -> "Long string: " + s;
    case String s -> "Short string: " + s;
    case Integer i && i > 0 -> "Positive: " + i;
    case Integer i -> "Non-positive: " + i;
    default -> "Unknown";
};

// Record patterns
switch (obj) {
    case Person(String name, int age) -> name + " is " + age;
    case Point(int x, int y) -> "Point at " + x + "," + y;
    default -> "Unknown";
}
```

### 3. Sequenced Collections
```java
// New interfaces with defined encounter order
interface SequencedCollection<E> extends Collection<E> {
    SequencedCollection<E> reversed();
    void addFirst(E e);
    void addLast(E e);
    E getFirst();
    E getLast();
    E removeFirst();
    E removeLast();
}

// Usage
List<String> list = new ArrayList<>();
list.addFirst("First");
list.addLast("Last");
String first = list.getFirst();
String last = list.getLast();
List<String> reversed = list.reversed();

// SequencedSet
LinkedHashSet<String> set = new LinkedHashSet<>();
set.addFirst("A");
set.addLast("Z");
```

### 4. Record Patterns
```java
// Deconstruct records
record Point(int x, int y) {}

if (obj instanceof Point(int x, int y)) {
    System.out.println("x=" + x + ", y=" + y);
}

// Nested record patterns
record Rectangle(Point upperLeft, Point lowerRight) {}

if (obj instanceof Rectangle(Point(int x1, int y1), Point(int x2, int y2))) {
    System.out.println("Rectangle from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")");
}
```

### 5. String Templates (Preview)
```java
// String interpolation (preview feature)
String name = "John";
int age = 30;

// Using STR template processor
String message = STR."Hello \{name}, you are \{age} years old";

// With expressions
String info = STR."Name: \{name.toUpperCase()}, Age: \{age + 1}";

// Multi-line
String html = STR."""
    <html>
        <body>
            <p>Hello \{name}</p>
        </body>
    </html>
    """;
```

**Note:** String Templates were a preview feature in Java 21 and may change in future releases.

### 6. Structured Concurrency (Incubator)
```java
// Manage multiple concurrent tasks as a single unit
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Subtask<String> user = scope.fork(() -> fetchUser(id));
    Subtask<Integer> order = scope.fork(() -> fetchOrder(id));
    
    scope.join();           // Wait for both
    scope.throwIfFailed();  // Propagate errors
    
    return new Response(user.get(), order.get());
}
// If one task fails, the other is cancelled automatically
```

---

## Version Comparison Summary

| Feature | Java 11 | Java 17 | Java 21 |
|---------|---------|---------|--------|
| var in lambda | ✅ | ✅ | ✅ |
| String methods | ✅ | ✅ | ✅ |
| Records | | ✅ | ✅ |
| Sealed classes | | ✅ | ✅ |
| Pattern matching instanceof | | ✅ | ✅ |
| Text blocks | | ✅ | ✅ |
| Switch expressions | | ✅ | ✅ |
| Virtual Threads | | | ✅ |
| Sequenced Collections | | | ✅ |
| Pattern matching switch | | | ✅ |
| Record patterns | | | ✅ |

---

## Interview Q&A

**Q: What is the difference between var and explicit types?**
A: `var` is type inference — compiler determines type. No runtime difference. Only for local variables.

**Q: When to use Records vs regular classes?**
A: Records for immutable data carriers. Use regular classes when you need mutability, custom equals/hashCode, or inheritance.

**Q: What are sealed classes used for?**
A: Restrict which classes can extend/implement a type. Enables exhaustive pattern matching in switch.

**Q: What is the benefit of Virtual Threads?**
A: Lightweight threads (millions possible). No thread pool needed. Blocking operations don't block OS threads. Best for I/O-bound tasks.

**Q: Virtual Threads vs Platform Threads?**
A: Platform threads map 1:1 to OS threads (expensive, ~1MB stack). Virtual threads are managed by JVM (cheap, ~1KB), multiplexed onto few OS threads.

**Q: What are Sequenced Collections?**
A: New interfaces (Java 21) that provide defined encounter order with `getFirst()`, `getLast()`, `reversed()` methods.

**Q: What is Pattern Matching for switch?**
A: Switch can match on types directly with guards: `case String s when s.length() > 5 -> ...`

**Q: Difference between Text Blocks and regular Strings?**
A: Text blocks preserve formatting, support multi-line without `\n`, auto-strip indentation. Enclosed in `"""`.

**Q: Can Records extend other classes?**
A: No. Records implicitly extend `java.lang.Record`. They can implement interfaces.

**Q: What is the difference between `strip()` and `trim()`?**
A: `strip()` (Java 11) is Unicode-aware, handles all whitespace characters. `trim()` only handles ASCII whitespace (<= U+0020).
