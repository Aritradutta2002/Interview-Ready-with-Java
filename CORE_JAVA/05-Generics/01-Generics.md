# Generics - Complete Guide

## Table of Contents
1. [Why Generics?](#why-generics)
2. [Generic Classes](#generic-classes)
3. [Generic Methods](#generic-methods)
4. [Bounded Type Parameters](#bounded-type-parameters)
5. [Wildcards](#wildcards)
6. [PECS Principle](#pecs-principle)
7. [Type Erasure](#type-erasure)
8. [Generic Restrictions](#generic-restrictions)
9. [Interview Q&A](#interview-qa)
10. [Common Traps](#common-traps)
11. [Related Topics](#related-topics)

---

## Why Generics?

```java
// Before Java 5 (Raw types)
List list = new ArrayList();
list.add("Hello");
String s = (String) list.get(0);  // Cast required

// With Generics
List<String> list = new ArrayList<>();
list.add("Hello");
String s = list.get(0);  // No cast needed
```

**Benefits:**
- Type safety at compile time
- Eliminate casts
- Better code reuse

---

## Generic Classes

```java
public class Box<T> {
    private T value;
    
    public void set(T value) { this.value = value; }
    public T get() { return value; }
}

// Usage
Box<Integer> intBox = new Box<>();
intBox.set(42);
Integer i = intBox.get();

Box<String> strBox = new Box<>();
strBox.set("Hello");
```

### Multiple Type Parameters
```java
public class Pair<K, V> {
    private K key;
    private V value;
    
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() { return key; }
    public V getValue() { return value; }
}

Pair<String, Integer> pair = new Pair<>("Age", 25);
```

---

## Generic Methods

```java
public class Utils {
    // Generic method
    public static <T> void print(T item) {
        System.out.println(item);
    }
    
    // Generic method with multiple types
    public static <K, V> void printPair(K key, V value) {
        System.out.println(key + " = " + value);
    }
    
    // Bounded type parameter
    public static <T extends Number> double add(T a, T b) {
        return a.doubleValue() + b.doubleValue();
    }
    
    // Bounded with interface
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }
}

// Usage
Utils.print("Hello");           // Compiler infers type
Utils.<String>print("Hello");   // Explicit type
Utils.printPair("Age", 25);
Utils.max(3, 5);                // Returns 5
```

---

## Bounded Type Parameters

### Single Bound
```java
// Must be Number or subclass
public class NumberBox<T extends Number> {
    private T value;
    
    public double doubleValue() {
        return value.doubleValue();  // Number method available
    }
}
```

### Multiple Bounds
```java
// Must extend Number AND implement Comparable
public <T extends Number & Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}

// Class must come first, then interfaces
public <T extends MyClass & Serializable & Cloneable> void process(T item) { }
```

---

## Wildcards

### 1. Unbounded Wildcard (?)
```java
void printList(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
    // list.add("item");  // ❌ Compile error (except null)
    Object obj = list.get(0);  // ✅ Can read as Object
}
```

**Use when:**
- Only reading as Object
- Methods of Object are sufficient
- Writing null is acceptable

### 2. Upper Bounded Wildcard (? extends T)
```java
// Accepts List<Number>, List<Integer>, List<Double>
public double sum(List<? extends Number> list) {
    double total = 0;
    for (Number n : list) {
        total += n.doubleValue();  // ✅ Can read as Number
    }
    // list.add(10);  // ❌ Cannot write (except null)
    return total;
}

List<Integer> ints = Arrays.asList(1, 2, 3);
sum(ints);  // Works!
```

**Use when:**
- Reading from collection
- Need flexibility in type

### 3. Lower Bounded Wildcard (? super T)
```java
// Accepts List<Integer>, List<Number>, List<Object>
public void addNumbers(List<? super Integer> list) {
    list.add(1);  // ✅ Can write Integer
    list.add(2);
    // Integer i = list.get(0);  // ❌ Can only read as Object
    Object obj = list.get(0);  // ✅
}

List<Number> numbers = new ArrayList<>();
addNumbers(numbers);  // Works!
```

**Use when:**
- Writing to collection
- Need flexibility in type

---

## PECS Principle

**Producer Extends, Consumer Super**

```java
// Producer - you READ from it → ? extends T
public void readFrom(List<? extends Number> producer) {
    Number n = producer.get(0);  // Produces Numbers
}

// Consumer - you WRITE to it → ? super T
public void writeTo(List<? super Integer> consumer) {
    consumer.add(1);  // Consumes Integers
}

// Both read and write → Use exact type T
public void readAndWrite(List<Integer> list) {
    Integer i = list.get(0);
    list.add(1);
}
```

### Example: Copy Method
```java
public static <T> void copy(List<? super T> dest, List<? extends T> src) {
    for (T item : src) {  // src is producer (read)
        dest.add(item);   // dest is consumer (write)
    }
}

List<Integer> src = Arrays.asList(1, 2, 3);
List<Number> dest = new ArrayList<>();
copy(dest, src);  // Copies Integer list to Number list
```

---

## Type Erasure

Generics exist only at compile time. JVM has no knowledge of generics.

### Before Erasure
```java
public class Box<T> {
    private T value;
    public void set(T value) { this.value = value; }
    public T get() { return value; }
}
```

### After Erasure
```java
public class Box {
    private Object value;
    public void set(Object value) { this.value = value; }
    public Object get() { return value; }
}
```

### Bridge Methods
```java
class StringBox extends Box<String> {
    @Override
    public void set(String value) { }
    
    // Compiler generates bridge method
    public void set(Object value) {
        set((String) value);
    }
}
```

### Implications
```java
// ❌ Cannot use instanceof with generic type
if (list instanceof List<String>) { }  // Compile error

// ❌ Cannot create generic array
T[] array = new T[10];  // Compile error

// ❌ Cannot overload with same erasure
void print(List<String> list) { }
void print(List<Integer> list) { }  // Compile error

// ❌ Cannot instantiate type parameter
T item = new T();  // Compile error

// ✅ Workarounds
T[] array = (T[]) new Object[10];
T item = clazz.newInstance();  // Pass Class<T>
```

---

## Generic Restrictions

| Cannot Do | Reason |
|-----------|--------|
| `new T()` | Type erased at runtime |
| `new T[]` | Arrays are covariant |
| `static T field` | Static shared across instances |
| `instanceof T` | Type erased |
| `T.class` | Type erased |
| `List<String>.class` | Type erased |
| `List<int>` | Primitives not allowed |

---

## Interview Q&A

### Basic Level

**Q: What is type erasure?**
A: Compiler removes generic type info at compile time. Runtime uses Object or bound type.

**Q: Why can't we create generic array?**
A: Arrays are covariant, generics are invariant. Could cause ArrayStoreException.

**Q: What is PECS?**
A: Producer Extends, Consumer Super. Use ? extends for reading, ? super for writing.

**Q: Difference between List<?> and List<Object>?**
A: List<?> accepts any type but can't add (except null). List<Object> accepts only Object and can add Object instances.

**Q: Can static method use class type parameter?**
A: No. Static methods can have their own type parameter though.

**Q: What is a bridge method?**
A: Compiler-generated method to maintain polymorphism with type erasure.

**Q: Why List<int> not allowed?**
A: Generics work with reference types only. Use List<Integer> and autoboxing.

**Q: What is the difference between T and ? in generics?**
A: T is a type parameter (can be used in method body). ? is a wildcard (unknown type, limited usage).

### Intermediate Level

**Q: What is the difference between List<? extends Number> and List<Number>?**
A: List<? extends Number> accepts List<Integer>, List<Double>, etc. (read-only). List<Number> accepts only List<Number> (read-write).

**Q: Can you add elements to List<? extends Number>?**
A: No (except null). Compiler doesn't know exact type, so can't guarantee type safety.

**Q: Can you add elements to List<? super Integer>?**
A: Yes, you can add Integer and its subtypes. Can't add Number or Object (not guaranteed to be accepted).

**Q: What is the difference between bounded and unbounded wildcards?**
A: Unbounded (?) accepts any type. Bounded (? extends T or ? super T) restricts to subtypes or supertypes.

**Q: What is the difference between covariance and contravariance?**
A: Covariance (? extends T) = can read as T. Contravariance (? super T) = can write T. Invariance (T) = can read and write T.

**Q: Can you have multiple bounds in generics?**
A: Yes. `<T extends Class & Interface1 & Interface2>`. Class must come first, then interfaces.

**Q: What is the difference between raw type and Object?**
A: Raw type (List) bypasses type checking. List<Object> is type-safe and requires explicit Object.

**Q: Can you use instanceof with generic types?**
A: No. Type is erased at runtime. `list instanceof List<String>` is illegal. `list instanceof List` is legal.

### Advanced Level

**Q: What is reifiable type?**
A: Type whose information is fully available at runtime. Primitives, raw types, arrays of reifiable types. Generics are NOT reifiable (erased).

**Q: Why can't you create array of generic type?**
A: Arrays are reifiable (know their type at runtime). Generics are not (type erased). Mixing them causes heap pollution.

**Q: What is heap pollution?**
A: When a variable of parameterized type refers to an object that's not of that type. Causes ClassCastException at unexpected places.

**Q: What is the difference between List and List<?>?**
A: List is raw type (no type safety). List<?> is wildcard (type-safe but read-only).

**Q: Can you overload methods with different generic types?**
A: No. `void method(List<String>)` and `void method(List<Integer>)` have same erasure (both become `List`). Compile error.

**Q: What is the difference between T extends Comparable<T> and T extends Comparable<? super T>?**
A: First requires T to be comparable to itself. Second allows T to be comparable to its supertype (more flexible).

**Q: Can you have generic exception class?**
A: No. Generic classes cannot extend Throwable. Catch blocks need reifiable types.

**Q: What is the difference between List<String>[] and List<?>[]?**
A: Both are problematic. First is illegal (compile error). Second is legal but unsafe (heap pollution risk).

### Scenario-Based Questions

**Q: What will this code print?**
```java
List<Integer> ints = new ArrayList<>();
List<? extends Number> nums = ints;
nums.add(10);
```
A: Compile error. Can't add to List<? extends Number> (except null). Compiler doesn't know if it's List<Integer>, List<Double>, etc.

**Q: What's wrong with this code?**
```java
List<String>[] array = new List<String>[10];
```
A: Compile error. Can't create array of parameterized type. Arrays are covariant, generics are invariant.

**Q: What will this code print?**
```java
List<Integer> ints = new ArrayList<>();
List<Number> nums = ints;
```
A: Compile error. Generics are invariant. List<Integer> is NOT a subtype of List<Number>.

**Q: What's the output?**
```java
List<?> list = new ArrayList<String>();
list.add("Hello");
```
A: Compile error. Can't add to List<?> (except null). Unknown type.

**Q: What will this code print?**
```java
List<Integer> ints = Arrays.asList(1, 2, 3);
List<? super Integer> list = ints;
list.add(10);
System.out.println(list);
```
A: [1, 2, 3, 10]. Can add Integer to List<? super Integer>.

**Q: What's wrong with this code?**
```java
public <T> T[] toArray(List<T> list) {
    return (T[]) new Object[list.size()];
}
```
A: ClassCastException at runtime. Object[] cannot be cast to T[]. Use `(T[]) Array.newInstance(clazz, size)` instead.

### Tricky Interview Scenarios

**Q: What will this code print?**
```java
List<String> list1 = new ArrayList<>();
List<String> list2 = new ArrayList<>();
System.out.println(list1.getClass() == list2.getClass());
```
A: true. Type erasure - both are ArrayList at runtime. Generic type info is erased.

**Q: What's the output?**
```java
List<Integer> ints = new ArrayList<>();
ints.add(1);
List raw = ints;
raw.add("String");
Integer i = ints.get(1);
```
A: ClassCastException at runtime. Raw type bypasses type checking. "String" added to List<Integer>.

**Q: What will this code print?**
```java
class Box<T> {
    T value;
}
Box<Integer> intBox = new Box<>();
Box<String> strBox = new Box<>();
System.out.println(intBox.getClass() == strBox.getClass());
```
A: true. Type erasure - both are Box at runtime.

**Q: What's wrong with this code?**
```java
public <T> void method(List<T> list) {
    if (list instanceof List<String>) {
        // ...
    }
}
```
A: Compile error. Can't use instanceof with parameterized type. Type is erased at runtime.

**Q: What will this code print?**
```java
List<Integer> ints = Arrays.asList(1, 2, 3);
List<? extends Number> nums = ints;
Number n = nums.get(0);
System.out.println(n);
```
A: 1. Can read from List<? extends Number> as Number.

**Q: What's the output?**
```java
List<String> list = new ArrayList<>();
list.add("Hello");
Object obj = list;
List<Integer> ints = (List<Integer>) obj;
System.out.println(ints.get(0));
```
A: ClassCastException at runtime. Unchecked cast warning at compile time. "Hello" cannot be cast to Integer.

**Q: What will this code print?**
```java
class Parent<T> {
    T value;
}
class Child extends Parent<String> {
}
Child c = new Child();
c.value = "Hello";
System.out.println(c.value);
```
A: "Hello". Child inherits Parent<String>, so value is String type.

**Q: What's wrong with this code?**
```java
public <T> void method() {
    T obj = new T();
}
```
A: Compile error. Can't instantiate type parameter. Type is erased at runtime. Use `T obj = clazz.newInstance()` with Class<T> parameter.

---

## Common Traps

### ❌ Trap 1: Using Raw Types

**Why it's wrong**:
Raw types bypass compile-time type safety, leading to runtime ClassCastException.

**Incorrect Code**:
```java
List list = new ArrayList();  // Raw type
list.add("String");
list.add(123);
String s = (String) list.get(1);  // Runtime ClassCastException!
```

**Correct Code**:
```java
List<String> list = new ArrayList<>();
list.add("String");
// list.add(123);  // Compile error - caught early!
String s = list.get(0);  // No cast needed
```

**Interview Tip**:
Never use raw types. They exist only for backward compatibility with pre-Java 5 code.

---

### ❌ Trap 2: Heap Pollution with Generic Arrays

**Why it's wrong**:
Creating generic arrays can cause heap pollution and runtime errors.

**Incorrect Code**:
```java
List<String>[] stringLists = new List<String>[10];  // Compile error
List<String>[] stringLists = (List<String>[]) new List[10];  // Compiles with warning

stringLists[0] = new ArrayList<String>();
stringLists[0].add("Hello");

// Heap pollution possible:
Object[] objects = stringLists;
objects[0] = new ArrayList<Integer>();  // No error!
String s = stringLists[0].get(0);  // ClassCastException at runtime!
```

**Correct Code**:
```java
// Use List of Lists instead
List<List<String>> stringLists = new ArrayList<>();
stringLists.add(new ArrayList<>());
stringLists.get(0).add("Hello");

// Or use varargs with @SafeVarargs
@SafeVarargs
public static <T> List<T> asList(T... elements) {
    List<T> list = new ArrayList<>();
    for (T element : elements) {
        list.add(element);
    }
    return list;
}
```

**Interview Tip**:
Arrays and generics don't mix well due to type erasure. Use collections instead of arrays for generic types.

---

### ❌ Trap 3: Confusing ? extends and ? super

**Why it's wrong**:
Using the wrong wildcard prevents reading or writing, causing compile errors.

**Incorrect Code**:
```java
// Want to add integers to list
public void addNumbers(List<? extends Number> list) {
    list.add(1);  // ❌ Compile error!
    list.add(1.5);  // ❌ Compile error!
    // Can't add because compiler doesn't know exact type
}

// Want to read numbers from list
public double sum(List<? super Integer> list) {
    double total = 0;
    for (Integer n : list) {  // ❌ Compile error!
        total += n;
    }
    return total;
}
```

**Correct Code**:
```java
// Producer (reading) - use extends
public double sum(List<? extends Number> list) {
    double total = 0;
    for (Number n : list) {  // ✅ Can read as Number
        total += n.doubleValue();
    }
    return total;
}

// Consumer (writing) - use super
public void addNumbers(List<? super Integer> list) {
    list.add(1);  // ✅ Can write Integer
    list.add(2);
}
```

**Interview Tip**:
Remember PECS: Producer Extends, Consumer Super. If you're reading (producing values), use extends. If you're writing (consuming values), use super.

---

### ❌ Trap 4: Static Field with Type Parameter

**Why it's wrong**:
Static fields are shared across all instances, but type parameters are per-instance.

**Incorrect Code**:
```java
public class Box<T> {
    private static T value;  // ❌ Compile error!
    
    public static T getValue() {  // ❌ Compile error!
        return value;
    }
}
```

**Correct Code**:
```java
public class Box<T> {
    private T value;  // ✅ Instance field
    
    public T getValue() {  // ✅ Instance method
        return value;
    }
    
    // Static method with its own type parameter
    public static <E> Box<E> create(E value) {
        Box<E> box = new Box<>();
        box.value = value;
        return box;
    }
}
```

**Interview Tip**:
Static members cannot use class type parameters. Static methods can have their own type parameters though.

---

### ❌ Trap 5: Overloading with Same Erasure

**Why it's wrong**:
After type erasure, both methods have the same signature, causing compile error.

**Incorrect Code**:
```java
public class Processor {
    public void process(List<String> list) {
        // Process strings
    }
    
    public void process(List<Integer> list) {  // ❌ Compile error!
        // Process integers
    }
    // Both erase to process(List)
}
```

**Correct Code**:
```java
public class Processor {
    public void processStrings(List<String> list) {
        // Process strings
    }
    
    public void processIntegers(List<Integer> list) {
        // Process integers
    }
    
    // Or use a single generic method
    public <T> void process(List<T> list, Class<T> type) {
        if (type == String.class) {
            // Process as strings
        } else if (type == Integer.class) {
            // Process as integers
        }
    }
}
```

**Interview Tip**:
You cannot overload methods that have the same erasure. Use different method names or a single generic method with type discrimination.

---

## Related Topics

- [Collections Framework](../04-Collections-Framework/01-List-Implementations.md) - Generic collections (List, Set, Map)
- [Java 8 Features - Stream API](../06-Java-8-Features/02-Stream-API.md) - Generic streams and type inference
- [Reflection & Annotations](../14-Reflection-Annotations/01-Reflection-and-Annotations.md) - Working with generic types at runtime
- [OOP - Polymorphism](../01-Core-Java-OOP/05-OOP-Polymorphism-Deep-Dive.md) - Generics enable compile-time polymorphism
- [Design Patterns](../09-Design-Patterns/01-Design-Patterns.md) - Generic design patterns (Factory, Builder)

---

*Last Updated: February 2026*
*Java Version: 5+, 8, 11, 17, 21*
