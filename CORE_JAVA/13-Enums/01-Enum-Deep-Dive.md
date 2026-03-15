# Enum in Java - Complete Interview Guide

## What is an Enum?
A special class that represents a **fixed set of constants**. Enums are implicitly `final` and extend `java.lang.Enum`.

```java
public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

// Usage
Day today = Day.MONDAY;
System.out.println(today);  // "MONDAY"
```

---

## Enum Internals — What Compiler Generates

```java
// What you write:
enum Color { RED, GREEN, BLUE }

// What compiler generates (simplified):
public final class Color extends Enum<Color> {
    public static final Color RED = new Color("RED", 0);
    public static final Color GREEN = new Color("GREEN", 1);
    public static final Color BLUE = new Color("BLUE", 2);
    
    private static final Color[] VALUES = { RED, GREEN, BLUE };
    
    private Color(String name, int ordinal) {
        super(name, ordinal);
    }
    
    public static Color[] values() { return VALUES.clone(); }
    public static Color valueOf(String name) { /* lookup */ }
}
```

**Key facts:**
- Enum constants are `public static final` instances
- Enum class is implicitly `final` — cannot be extended
- Constructor is implicitly `private` — cannot instantiate with `new`
- Each constant is created **once** when class is loaded (Singleton per constant)

---

## Enum with Fields, Methods, and Constructor

```java
public enum Planet {
    MERCURY(3.303e+23, 2.4397e6),
    VENUS(4.869e+24, 6.0518e6),
    EARTH(5.976e+24, 6.37814e6),
    MARS(6.421e+23, 3.3972e6);
    
    private final double mass;     // in kg
    private final double radius;   // in meters
    
    // Constructor — MUST be private or package-private
    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
    }
    
    // Methods
    public double getMass() { return mass; }
    public double getRadius() { return radius; }
    
    public double surfaceGravity() {
        final double G = 6.67300E-11;
        return G * mass / (radius * radius);
    }
    
    public double surfaceWeight(double otherMass) {
        return otherMass * surfaceGravity();
    }
}

// Usage
double earthWeight = 75.0;
double marsWeight = Planet.MARS.surfaceWeight(earthWeight);
```

---

## Enum with Abstract Methods

```java
public enum Operation {
    ADD {
        @Override
        public double apply(double a, double b) { return a + b; }
    },
    SUBTRACT {
        @Override
        public double apply(double a, double b) { return a - b; }
    },
    MULTIPLY {
        @Override
        public double apply(double a, double b) { return a * b; }
    },
    DIVIDE {
        @Override
        public double apply(double a, double b) {
            if (b == 0) throw new ArithmeticException("Division by zero");
            return a / b;
        }
    };
    
    // Abstract method — each constant MUST implement
    public abstract double apply(double a, double b);
}

// Usage
double result = Operation.ADD.apply(10, 5);  // 15.0
```

---

## Enum Implementing Interface

```java
interface Printable {
    void print();
}

enum Status implements Printable {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended");
    
    private final String label;
    
    Status(String label) { this.label = label; }
    
    @Override
    public void print() {
        System.out.println("Status: " + label);
    }
}

// Polymorphism with enum
Printable p = Status.ACTIVE;
p.print();  // "Status: Active"
```

---

## Built-in Enum Methods

```java
enum Season { SPRING, SUMMER, FALL, WINTER }

// name() — Returns exact name as declared
Season.SPRING.name();        // "SPRING"

// ordinal() — Returns position (0-based)
Season.SPRING.ordinal();     // 0
Season.WINTER.ordinal();     // 3

// values() — Returns array of all constants
Season[] all = Season.values();  // [SPRING, SUMMER, FALL, WINTER]

// valueOf() — Returns constant by name
Season s = Season.valueOf("SUMMER");  // Season.SUMMER
// Season.valueOf("summer");  // ❌ IllegalArgumentException (case-sensitive!)

// compareTo() — Compares by ordinal
Season.SPRING.compareTo(Season.WINTER);  // < 0 (SPRING before WINTER)

// toString() — Same as name() by default (can be overridden)
Season.SPRING.toString();    // "SPRING"
```

---

## Enum in Switch

```java
enum TrafficLight { RED, YELLOW, GREEN }

TrafficLight light = TrafficLight.RED;

// Classic switch
switch (light) {
    case RED:    System.out.println("Stop"); break;
    case YELLOW: System.out.println("Caution"); break;
    case GREEN:  System.out.println("Go"); break;
}

// Enhanced switch (Java 14+)
String action = switch (light) {
    case RED -> "Stop";
    case YELLOW -> "Caution";
    case GREEN -> "Go";
};
// No default needed — compiler ensures all constants handled
```

---

## EnumSet and EnumMap

### EnumSet — Efficient Set for Enum Constants
```java
// Uses bit vector internally — extremely fast and memory efficient
EnumSet<Day> weekend = EnumSet.of(Day.SATURDAY, Day.SUNDAY);
EnumSet<Day> weekdays = EnumSet.complementOf(weekend);
EnumSet<Day> allDays = EnumSet.allOf(Day.class);
EnumSet<Day> noDay = EnumSet.noneOf(Day.class);
EnumSet<Day> range = EnumSet.range(Day.MONDAY, Day.FRIDAY);

weekend.contains(Day.SATURDAY);  // true — O(1)
weekend.add(Day.FRIDAY);         // Add to set
```

### EnumMap — Efficient Map with Enum Keys
```java
// Uses array internally — faster than HashMap for enum keys
EnumMap<Day, String> schedule = new EnumMap<>(Day.class);
schedule.put(Day.MONDAY, "Work");
schedule.put(Day.SATURDAY, "Rest");

String plan = schedule.get(Day.MONDAY);  // "Work" — O(1)
```

### Why Use EnumSet/EnumMap?
| Feature | EnumSet/EnumMap | HashSet/HashMap |
|---------|-----------------|-----------------|
| Performance | Faster (bit vector / array) | Slower (hashing) |
| Memory | Compact | More overhead |
| Null | No null in EnumSet | Allowed |
| Order | Enum declaration order | No guaranteed order |

---

## Singleton via Enum (Best Practice)

```java
// Thread-safe, serialization-safe, reflection-safe Singleton
public enum DatabaseConnection {
    INSTANCE;
    
    private Connection connection;
    
    DatabaseConnection() {
        // Initialize connection
        connection = createConnection();
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    private Connection createConnection() {
        // Create and return database connection
        return null;
    }
}

// Usage
Connection conn = DatabaseConnection.INSTANCE.getConnection();
```

**Why Enum Singleton is Best:**
- JVM guarantees single instance
- Thread-safe by default
- Prevents reflection attacks (`Cannot reflectively create enum objects`)
- Handles serialization automatically (no `readResolve()` needed)

---

## Enum Gotchas

```java
// ❌ Cannot instantiate enum
Day d = new Day();  // COMPILE ERROR

// ❌ Cannot extend enum
class MyDay extends Day { }  // COMPILE ERROR

// ❌ valueOf is case-sensitive
Day.valueOf("monday");  // IllegalArgumentException

// ✅ Safe valueOf with fallback
public static Day fromString(String name) {
    try {
        return Day.valueOf(name.toUpperCase());
    } catch (IllegalArgumentException e) {
        return null;  // or default value
    }
}

// ⚠️ Don't rely on ordinal() for persistence
// If enum order changes, stored ordinals break!
// Use name() instead for serialization
```

---

## Interview Q&A

**Q: Can enum extend a class?**
A: No. Enum implicitly extends `java.lang.Enum`. Java doesn't support multiple inheritance.

**Q: Can enum implement an interface?**
A: Yes. Each constant can provide its own implementation.

**Q: Why is enum Singleton the best approach?**
A: Thread-safe, reflection-safe, serialization-safe — all guaranteed by JVM.

**Q: What is the difference between name() and toString()?**
A: `name()` always returns exact declared name (final, cannot override). `toString()` returns same by default but can be overridden.

**Q: Can we use == to compare enums?**
A: Yes! `==` is preferred over `equals()` for enums because each constant is a singleton. Null-safe too.

**Q: What is EnumSet and why use it?**
A: Bit-vector based Set implementation for enums. Extremely fast (O(1)) and memory efficient.

**Q: Can enum have constructors?**
A: Yes, but only private or package-private. Called once per constant during class loading.

**Q: What happens if you add a new enum constant?**
A: All switch statements without `default` will get compiler warnings. `ordinal()` values may shift.

**Q: Can enum be serialized?**
A: Yes. Serialization uses `name()`, not ordinal. Deserialization calls `valueOf()` — guaranteed single instance.
