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
String info =