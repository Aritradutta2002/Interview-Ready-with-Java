# Generics - Complete Guide

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

**Q: What is type erasure?**
A: Compiler removes generic type info at compile time. Runtime uses Object or bound type.

**Q: Why can't we create generic array?**
A: Arrays are covariant, generics are invariant. Could cause ArrayStoreException.

**Q: What is PECS?**
A: Producer Extends, Consumer Super. Use ? extends for reading, ? super for writing.

**Q: Difference between List<?> and List<Object>?**
A: List<?> accepts any type but can't add. List<Object> accepts only Object and can add.

**Q: Can static method use class type parameter?**
A: No. Static methods can have their own type parameter though.

**Q: What is a bridge method?**
A: Compiler-generated method to maintain polymorphism with type erasure.

**Q: Why List<int> not allowed?**
A: Generics work with reference types only. Use List<Integer> and autoboxing.
