# Java 8 Features - Lambda & Functional Interfaces

## Lambda Expressions

### Syntax
```java
(parameters) -> expression
(parameters) -> { statements; }
```

### Examples
```java
// No parameters
Runnable r = () -> System.out.println("Hello");

// Single parameter (parentheses optional)
Consumer<String> c = s -> System.out.println(s);
Consumer<String> c = (s) -> System.out.println(s);

// Multiple parameters
BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
BiFunction<Integer, Integer, Integer> add = (a, b) -> { return a + b; };

// Multiple statements
Consumer<String> printer = s -> {
    String upper = s.toUpperCase();
    System.out.println(upper);
};
```

### Lambda vs Anonymous Class
```java
// Anonymous class
Runnable r1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello");
    }
};

// Lambda (concise)
Runnable r2 = () -> System.out.println("Hello");
```

**Key Difference:**
- Lambda doesn't create new class file
- `this` in lambda refers to enclosing class
- Lambda can only implement functional interface

---

## Functional Interface

**Definition:** Interface with exactly one abstract method.

```java
@FunctionalInterface
interface Calculator {
    int calculate(int a, int b);
    
    // Can have default/static methods
    default void print() { }
    static void help() { }
}
```

### Built-in Functional Interfaces (java.util.function)

| Interface | Method | Purpose |
|-----------|--------|---------|
| `Predicate<T>` | `boolean test(T t)` | Test condition |
| `Function<T,R>` | `R apply(T t)` | Transform T to R |
| `Consumer<T>` | `void accept(T t)` | Consume T, no return |
| `Supplier<T>` | `T get()` | Supply T, no input |
| `UnaryOperator<T>` | `T apply(T t)` | Transform T to T |
| `BinaryOperator<T>` | `T apply(T t1, T t2)` | Combine two T's |
| `BiFunction<T,U,R>` | `R apply(T t, U u)` | Transform T,U to R |
| `BiPredicate<T,U>` | `boolean test(T t, U u)` | Test T,U |
| `BiConsumer<T,U>` | `void accept(T t, U u)` | Consume T,U |

### Predicate Examples
```java
Predicate<Integer> isEven = n -> n % 2 == 0;
Predicate<Integer> isPositive = n -> n > 0;

// Compose predicates
Predicate<Integer> isEvenAndPositive = isEven.and(isPositive);
Predicate<Integer> isEvenOrPositive = isEven.or(isPositive);
Predicate<Integer> isNotEven = isEven.negate();

// Test
isEven.test(4);  // true
isEvenAndPositive.test(4);  // true
```

### Function Examples
```java
Function<String, Integer> length = s -> s.length();
Function<Integer, Integer> doubleIt = n -> n * 2;

// Compose functions
Function<String, Integer> lengthThenDouble = length.andThen(doubleIt);
Function<String, Integer> doubleThenLength = doubleIt.compose(length);

lengthThenDouble.apply("Hello");  // 5 * 2 = 10
```

### Consumer Examples
```java
Consumer<String> print = s -> System.out.println(s);
Consumer<String> printUpper = s -> System.out.println(s.toUpperCase());

// Chain consumers
Consumer<String> chained = print.andThen(printUpper);
chained.accept("hello");  // prints: hello\n HELLO
```

### Supplier Examples
```java
Supplier<String> greeting = () -> "Hello World";
Supplier<Double> random = Math::random;
Supplier<List<String>> listFactory = ArrayList::new;

String s = greeting.get();  // "Hello World"
```

---

## Method References

### Types of Method References

| Type | Syntax | Example |
|------|--------|---------|
| Static method | `ClassName::staticMethod` | `Math::abs` |
| Instance method (particular) | `instance::method` | `System.out::println` |
| Instance method (arbitrary) | `ClassName::instanceMethod` | `String::toUpperCase` |
| Constructor | `ClassName::new` | `ArrayList::new` |

### Examples
```java
// Static method reference
Function<Integer, Integer> abs = Math::abs;
Function<Integer, Integer> abs = n -> Math.abs(n);

// Instance method (particular object)
Consumer<String> printer = System.out::println;
Consumer<String> printer = s -> System.out.println(s);

// Instance method (arbitrary object)
Function<String, String> upper = String::toUpperCase;
Function<String, String> upper = s -> s.toUpperCase();

// BiFunction with instance method
BiFunction<String, String, String> concat = String::concat;
// s1.concat(s2)

// Constructor reference
Supplier<List<String>> listSupplier = ArrayList::new;
Function<Integer, List<String>> listFactory = ArrayList::new;

// Array constructor
Function<Integer, int[]> arrayFactory = int[]::new;
```

---

## Default & Static Methods in Interface

### Default Method
```java
interface Vehicle {
    default void start() {
        System.out.println("Starting...");
    }
}

class Car implements Vehicle {
    // Can override or use default
}

// Multiple inheritance conflict
interface A { default void m() { } }
interface B { default void m() { } }
class C implements A, B {
    @Override
    public void m() {
        A.super.m();  // Must explicitly choose
    }
}
```

### Static Method
```java
interface Utility {
    static void log(String msg) {
        System.out.println(msg);
    }
}

Utility.log("Hello");  // Call via interface
// Not inherited by implementing classes
```

---

## Interview Q&A

**Q: What is a functional interface?**
A: Interface with exactly one abstract method. Can have multiple default/static methods.

**Q: Can lambda use local variable?**
A: Yes, but it must be effectively final (not reassigned).

**Q: Difference between lambda and anonymous class?**
A: Lambda doesn't create class file, `this` refers to enclosing class.

**Q: What is method reference?**
A: Shorthand for lambda when method already exists. Four types: static, instance, arbitrary, constructor.

**Q: Why default methods in interface?**
A: Enable interface evolution without breaking existing implementations.

**Q: What happens when two interfaces have same default method?**
A: Class must override and choose which to use: `InterfaceName.super.method()`.

**Q: Can we override static method of interface?**
A: No, static methods are not inherited.

**Q: What is effectively final?**
A: Variable that's not reassigned after initialization. Lambda can access such variables.
