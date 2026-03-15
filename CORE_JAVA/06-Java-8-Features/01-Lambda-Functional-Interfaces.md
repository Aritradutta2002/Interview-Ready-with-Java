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

### Basic Level

**Q: What is a functional interface?**
A: Interface with exactly one abstract method. Can have multiple default/static methods.

**Q: Can lambda use local variable?**
A: Yes, but it must be effectively final (not reassigned).

**Q: Difference between lambda and anonymous class?**
A: Lambda doesn't create class file, `this` refers to enclosing class, can only implement functional interface.

**Q: What is method reference?**
A: Shorthand for lambda when method already exists. Four types: static, instance (particular), instance (arbitrary), constructor.

**Q: Why default methods in interface?**
A: Enable interface evolution without breaking existing implementations.

**Q: What happens when two interfaces have same default method?**
A: Class must override and choose which to use: `InterfaceName.super.method()`.

**Q: Can we override static method of interface?**
A: No, static methods are not inherited.

**Q: What is effectively final?**
A: Variable that's not reassigned after initialization. Lambda can access such variables.

### Intermediate Level

**Q: What is the difference between Predicate and Function?**
A: Predicate tests a condition (returns boolean). Function transforms input to output (returns any type).

**Q: What is the difference between Consumer and Supplier?**
A: Consumer accepts input, returns nothing (void). Supplier takes no input, returns output.

**Q: What is the difference between Function and UnaryOperator?**
A: Function<T, R> transforms T to R (different types). UnaryOperator<T> transforms T to T (same type).

**Q: What is the difference between BiFunction and BinaryOperator?**
A: BiFunction<T, U, R> takes two inputs (T, U), returns R. BinaryOperator<T> takes two T's, returns T.

**Q: Can lambda throw checked exceptions?**
A: Not directly. Functional interfaces don't declare checked exceptions. Must wrap in try-catch or create custom functional interface.

**Q: What is the difference between andThen() and compose() in Function?**
A: `f.andThen(g)` = g(f(x)) (apply f first, then g). `f.compose(g)` = f(g(x)) (apply g first, then f).

**Q: Can lambda access instance variables?**
A: Yes, lambdas can access instance variables and modify them (unlike local variables which must be effectively final).

**Q: What is the difference between @FunctionalInterface and regular interface?**
A: @FunctionalInterface is optional but enforces single abstract method at compile time. Helps catch errors early.

### Advanced Level

**Q: How does JVM implement lambdas?**
A: Using invokedynamic bytecode instruction. Creates a hidden class at runtime (not anonymous inner class). More efficient than anonymous classes.

**Q: What is the difference between lambda and method reference in terms of performance?**
A: Method references are slightly more efficient (direct method handle). Lambdas may require additional wrapping. Difference is negligible in practice.

**Q: Can you serialize a lambda?**
A: Yes, if the target functional interface extends Serializable. But it's risky - implementation details may change.

**Q: What is the difference between lambda and closure?**
A: Lambda is the syntax. Closure is a lambda that captures variables from enclosing scope. All Java lambdas that use external variables are closures.

**Q: Why must captured variables be effectively final?**
A: To avoid concurrency issues and maintain functional programming principles. Lambda may execute in different thread or multiple times.

**Q: What is the difference between Predicate.and() and &&?**
A: Predicate.and() composes predicates (returns new Predicate). && is boolean operator. Use and() for predicate composition, && for boolean logic.

**Q: Can lambda have multiple statements?**
A: Yes, use curly braces: `(x) -> { statement1; statement2; return result; }`. Single expression doesn't need braces or return.

**Q: What is the difference between lambda and strategy pattern?**
A: Lambda is a concise way to implement strategy pattern. Before Java 8, you'd create separate classes. Now, pass lambda directly.

### Scenario-Based Questions

**Q: What will this code print?**
```java
List<String> list = Arrays.asList("a", "b", "c");
list.forEach(s -> System.out.println(s));
```
A: Prints "a", "b", "c" on separate lines. forEach is a Consumer that accepts each element.

**Q: What's wrong with this code?**
```java
int count = 0;
list.forEach(s -> count++);
```
A: Compile error. count must be effectively final. Lambda can't modify local variables from enclosing scope.

**Q: What will this code print?**
```java
Function<Integer, Integer> f = x -> x + 1;
Function<Integer, Integer> g = x -> x * 2;
System.out.println(f.andThen(g).apply(3));
System.out.println(f.compose(g).apply(3));
```
A: 8, 7. andThen: (3+1)*2=8. compose: (3*2)+1=7.

**Q: What's the output?**
```java
Predicate<String> p1 = s -> s.length() > 3;
Predicate<String> p2 = s -> s.startsWith("A");
System.out.println(p1.and(p2).test("Apple"));
System.out.println(p1.or(p2).test("Hi"));
```
A: true, false. "Apple" satisfies both. "Hi" satisfies neither.

**Q: What will this code print?**
```java
Supplier<String> s = () -> "Hello";
System.out.println(s.get());
System.out.println(s.get());
```
A: "Hello", "Hello". Supplier is called each time get() is invoked.

**Q: What's wrong with this code?**
```java
Function<String, Integer> f = String::length;
System.out.println(f.apply(null));
```
A: NullPointerException. Method reference calls length() on null.

### Tricky Interview Scenarios

**Q: What will this code print?**
```java
class Test {
    private String name = "Outer";
    
    public void test() {
        Runnable r = () -> System.out.println(this.name);
        r.run();
    }
}
new Test().test();
```
A: "Outer". Lambda's `this` refers to enclosing class instance, not the lambda itself.

**Q: What's the difference between these two?**
```java
// Option 1
Function<String, Integer> f1 = s -> s.length();
// Option 2
Function<String, Integer> f2 = String::length;
```
A: Functionally identical. f2 is more concise. Both compile to similar bytecode (invokedynamic).

**Q: What will this code print?**
```java
List<String> list = new ArrayList<>();
Consumer<String> c = s -> list.add(s);
c.accept("Hello");
System.out.println(list);
```
A: [Hello]. Lambda can modify instance variables (list is instance variable or effectively final reference).

**Q: What's wrong with this code?**
```java
@FunctionalInterface
interface MyInterface {
    void method1();
    void method2();
}
```
A: Compile error. Functional interface can have only one abstract method. This has two.

**Q: What will this code print?**
```java
BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
System.out.println(add.apply(3, 5));
```
A: 8. BiFunction takes two inputs, returns one output.

**Q: What's the output?**
```java
Predicate<String> p = s -> s.isEmpty();
System.out.println(p.negate().test(""));
```
A: false. negate() inverts the predicate. isEmpty() returns true, negate() makes it false.

**Q: What will this code do?**
```java
Function<Integer, Integer> f = x -> x / 0;
System.out.println(f.apply(10));
```
A: ArithmeticException at runtime. Lambda doesn't prevent runtime exceptions.

**Q: What's wrong with this code?**
```java
Runnable r = () -> {
    Thread.sleep(1000);
};
```
A: Compile error. Thread.sleep() throws checked InterruptedException. Runnable.run() doesn't declare it. Must wrap in try-catch.

**Q: What will this code print?**
```java
List<Integer> list = Arrays.asList(1, 2, 3);
list.replaceAll(x -> x * 2);
System.out.println(list);
```
A: [2, 4, 6]. replaceAll() is a UnaryOperator that modifies list in place.

**Q: What's the difference between these two?**
```java
// Option 1
Supplier<List<String>> s1 = () -> new ArrayList<>();
// Option 2
Supplier<List<String>> s2 = ArrayList::new;
```
A: Functionally identical. s2 is constructor reference (more concise). Both create new ArrayList on each get() call.
