# Covariance, Contravariance, and Invariance in Java

## Table of Contents
1. [Understanding Variance](#understanding-variance)
2. [Covariance in Java](#covariance-in-java)
3. [Contravariance in Java](#contravariance-in-java)
4. [Invariance in Java](#invariance-in-java)
5. [Arrays are Covariant (The Problem)](#arrays-are-covariant-the-problem)
6. [Generics are Invariant (The Solution)](#generics-are-invariant-the-solution)
7. [Wildcards: Achieving Variance](#wildcards-achieving-variance)
8. [Interview Questions](#interview-questions)
9. [Common Traps](#common-traps)
10. [Related Topics](#related-topics)

---

## Understanding Variance

**Variance** describes how subtyping between complex types relates to subtyping between their components.

```
Given: Dog extends Animal

┌─────────────────────────────────────────────────────────────┐
│                    VARIANCE TYPES                            │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  COVARIANCE:     If Dog ⊆ Animal, then List<Dog> ⊆ List<Animal>  │
│                  (Subtype relationship preserved)            │
│                  Example: Arrays, return types              │
│                                                             │
│  CONTRAVARIANCE: If Dog ⊆ Animal, then List<Animal> ⊆ List<Dog>  │
│                  (Subtype relationship reversed)             │
│                  Example: Method parameters (conceptually)   │
│                                                             │
│  INVARIANCE:     Dog ⊆ Animal, but List<Dog> ≠ List<Animal> │
│                  (No subtype relationship)                   │
│                  Example: Java Generics (by default)         │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## Covariance in Java

### 1. Covariant Return Types (Java 5+)

Overriding method can return a **subtype** of the original return type.

```java
class Animal {
    Animal reproduce() {
        return new Animal();
    }
}

class Dog extends Animal {
    @Override
    Dog reproduce() {  // ✅ Covariant return type
        return new Dog();
    }
}

Animal a = new Dog();
Animal offspring = a.reproduce();  // Returns Dog, assigned to Animal
```

**Why it works:**
- Dog IS-A Animal
- Returning Dog where Animal is expected is safe (Liskov Substitution Principle)

**Before Java 5:**
```java
class Dog extends Animal {
    @Override
    Animal reproduce() {  // Had to return exact type
        return new Dog();
    }
}
```

### 2. Covariant Method Overriding

```java
class Shape {
    Shape copy() { return new Shape(); }
}

class Circle extends Shape {
    @Override
    Circle copy() {  // ✅ Returns Circle instead of Shape
        return new Circle();
    }
}

// Usage
Shape s = new Circle();
Shape copy = s.copy();  // Returns Circle, but type is Shape

Circle c = new Circle();
Circle copy2 = c.copy();  // Returns Circle, type is Circle
```

### 3. Covariance in Exceptions (throws clause)

```java
class Parent {
    void method() throws IOException {
        // Can throw IOException or subclasses
    }
}

class Child extends Parent {
    @Override
    void method() throws FileNotFoundException {  // ✅ Subtype of IOException
        // Can throw same or narrower exception
    }
}
```

**Rule:** Overriding method can throw:
- Same exception
- Subclass of exception
- No exception
- **NOT** a broader exception

```java
class Parent {
    void method() throws IOException { }
}

class Child extends Parent {
    @Override
    void method() throws Exception {  // ❌ COMPILE ERROR
        // Exception is broader than IOException
    }
}
```

---

## Contravariance in Java

### Method Parameter Types (NOT Contravariant in Java)

In theory, method parameters should be contravariant, but **Java doesn't support this**.

```java
class Animal { }
class Dog extends Animal { }

class AnimalHandler {
    void handle(Dog d) {
        System.out.println("Handling dog");
    }
}

class DogHandler extends AnimalHandler {
    @Override
    void handle(Animal a) {  // ❌ COMPILE ERROR
        // Java doesn't allow contravariant parameters
        System.out.println("Handling animal");
    }
}
```

**Why contravariance would make sense:**
- If `DogHandler` can handle any `Animal`, it can certainly handle a `Dog`
- But Java requires exact parameter types for overriding

**Workaround: Overloading**
```java
class DogHandler extends AnimalHandler {
    @Override
    void handle(Dog d) {  // Override
        System.out.println("Handling dog");
    }
    
    void handle(Animal a) {  // Overload (not override)
        System.out.println("Handling animal");
    }
}
```

---

## Invariance in Java

### Generics are Invariant

```java
class Animal { }
class Dog extends Animal { }

List<Dog> dogs = new ArrayList<>();
List<Animal> animals = dogs;  // ❌ COMPILE ERROR
// List<Dog> is NOT a subtype of List<Animal>
```

**Why?** Type safety.

```java
// If this were allowed:
List<Dog> dogs = new ArrayList<>();
List<Animal> animals = dogs;  // Hypothetically allowed

animals.add(new Cat());  // Would compile (Cat is Animal)
Dog d = dogs.get(0);     // ClassCastException! Cat is not Dog
```

### Invariance Prevents Type Pollution

```java
List<String> strings = new ArrayList<>();
List<Object> objects = strings;  // ❌ Not allowed

// If allowed:
objects.add(123);  // Integer is Object
String s = strings.get(0);  // ClassCastException!
```

---

## Arrays are Covariant (The Problem)

### Arrays Allow Covariance

```java
Dog[] dogs = new Dog[10];
Animal[] animals = dogs;  // ✅ Compiles (arrays are covariant)

animals[0] = new Cat();  // ❌ ArrayStoreException at RUNTIME!
// JVM checks type at runtime and throws exception
```

**Why this is problematic:**
- Compiles fine
- Fails at runtime
- Type safety violated

### The ArrayStoreException

```java
Object[] objects = new String[10];  // ✅ Compiles
objects[0] = "Hello";               // ✅ Works
objects[1] = 123;                   // ❌ ArrayStoreException at runtime
```

**JVM Protection:**
- Arrays remember their component type at runtime
- JVM checks every array store operation
- Throws `ArrayStoreException` if type mismatch

### Why Arrays are Covariant

**Historical reason:** Before generics (Java 5), needed a way to write generic algorithms.

```java
// Pre-generics way to sort any array
void sort(Object[] array) {
    // Sort logic
}

String[] strings = {"c", "a", "b"};
sort(strings);  // Works because String[] is Object[]
```

---

## Generics are Invariant (The Solution)

### Type Safety at Compile Time

```java
List<Dog> dogs = new ArrayList<>();
List<Animal> animals = dogs;  // ❌ COMPILE ERROR

// Prevents:
// animals.add(new Cat());  // Would never compile
```

**Benefits:**
- Errors caught at compile time
- No runtime type checks needed
- Better performance (no runtime overhead)

### Invariance Example

```java
class Box<T> {
    private T item;
    
    void set(T item) { this.item = item; }
    T get() { return item; }
}

Box<Dog> dogBox = new Box<>();
Box<Animal> animalBox = dogBox;  // ❌ COMPILE ERROR

// Even though Dog extends Animal,
// Box<Dog> does NOT extend Box<Animal>
```

---

## Wildcards: Achieving Variance

### Upper Bounded Wildcard (? extends T) — Covariance

**Producer Extends** — Read-only, produces values.

```java
List<? extends Animal> animals = new ArrayList<Dog>();  // ✅ Covariant

Animal a = animals.get(0);  // ✅ Can read as Animal
animals.add(new Dog());     // ❌ COMPILE ERROR
animals.add(new Animal());  // ❌ COMPILE ERROR
animals.add(null);          // ✅ Only null allowed
```

**Why can't add?**
- `animals` could be `List<Dog>`, `List<Cat>`, or `List<Animal>`
- Compiler doesn't know which, so adding is unsafe

**Use case: Reading from collection**
```java
double sumWeights(List<? extends Animal> animals) {
    double total = 0;
    for (Animal a : animals) {  // ✅ Can read as Animal
        total += a.getWeight();
    }
    return total;
}

List<Dog> dogs = List.of(new Dog(), new Dog());
sumWeights(dogs);  // ✅ Works
```

### Lower Bounded Wildcard (? super T) — Contravariance

**Consumer Super** — Write-only, consumes values.

```java
List<? super Dog> dogs = new ArrayList<Animal>();  // ✅ Contravariant

dogs.add(new Dog());     // ✅ Can add Dog
dogs.add(new Puppy());   // ✅ Can add subtype of Dog
dogs.add(new Animal());  // ❌ COMPILE ERROR

Object obj = dogs.get(0);  // ✅ Can only read as Object
Dog d = dogs.get(0);       // ❌ COMPILE ERROR
```

**Why can add Dog?**
- `dogs` could be `List<Dog>`, `List<Animal>`, or `List<Object>`
- All can accept Dog, so adding Dog is safe

**Use case: Writing to collection**
```java
void addDogs(List<? super Dog> list) {
    list.add(new Dog());   // ✅ Safe
    list.add(new Puppy()); // ✅ Safe
}

List<Animal> animals = new ArrayList<>();
addDogs(animals);  // ✅ Works
```

### PECS Principle

**Producer Extends, Consumer Super**

```java
// Producer (reading) — use extends
<T> void copy(List<? extends T> source, List<? super T> dest) {
    for (T item : source) {  // Read from source (producer)
        dest.add(item);      // Write to dest (consumer)
    }
}

List<Dog> dogs = List.of(new Dog(), new Dog());
List<Animal> animals = new ArrayList<>();
copy(dogs, animals);  // ✅ Works
```

### Unbounded Wildcard (?)

```java
List<?> list = new ArrayList<String>();

Object obj = list.get(0);  // ✅ Can read as Object
list.add("Hello");         // ❌ COMPILE ERROR
list.add(null);            // ✅ Only null allowed
```

**Use case: When you don't care about type**
```java
void printSize(List<?> list) {
    System.out.println(list.size());  // Only need size, not elements
}
```

---

## Interview Questions

### Q1: What is covariance?

**Difficulty**: Medium

**Answer**:
Covariance means subtype relationship is preserved. If `Dog extends Animal`, then `List<Dog>` would be a subtype of `List<Animal>` (if covariant). Java arrays are covariant, but generics are not (by default).

**Example**: Covariant return types allow overriding method to return a subtype.

---

### Q2: Why are arrays covariant but generics invariant?

**Difficulty**: Hard

**Answer**:
**Arrays**: Covariant for historical reasons (pre-generics). Causes runtime `ArrayStoreException` when type mismatch.

**Generics**: Invariant for type safety. Prevents type pollution at compile time. No runtime checks needed.

```java
// Arrays (covariant) — runtime error
Object[] arr = new String[10];
arr[0] = 123;  // ArrayStoreException

// Generics (invariant) — compile error
List<Object> list = new ArrayList<String>();  // ❌ Won't compile
```

---

### Q3: What is the PECS principle?

**Difficulty**: Medium

**Answer**:
**Producer Extends, Consumer Super**

- **Producer** (reading): Use `? extends T`
- **Consumer** (writing): Use `? super T`

```java
void copy(List<? extends T> source, List<? super T> dest) {
    for (T item : source) {  // Read from producer
        dest.add(item);      // Write to consumer
    }
}
```

---

### Q4: Can you add elements to List<? extends Animal>?

**Difficulty**: Medium

**Answer**:
No (except `null`). The list could be `List<Dog>`, `List<Cat>`, etc. Compiler doesn't know which, so adding is unsafe.

```java
List<? extends Animal> list = new ArrayList<Dog>();
list.add(new Dog());  // ❌ COMPILE ERROR
list.add(null);       // ✅ Only null allowed
```

---

### Q5: What can you read from List<? super Dog>?

**Difficulty**: Medium

**Answer**:
Only `Object`. The list could be `List<Dog>`, `List<Animal>`, or `List<Object>`. The only common supertype is `Object`.

```java
List<? super Dog> list = new ArrayList<Animal>();
Object obj = list.get(0);  // ✅ Only Object
Dog d = list.get(0);       // ❌ COMPILE ERROR
```

---

### Q6: What is contravariance?

**Difficulty**: Hard

**Answer**:
Contravariance means subtype relationship is reversed. If `Dog extends Animal`, then `List<Animal>` would be a subtype of `List<Dog>` (if contravariant). Java doesn't support contravariant method parameters, but `? super T` provides contravariance for generics.

---

### Q7: Why does ArrayStoreException exist?

**Difficulty**: Medium

**Answer**:
Because arrays are covariant. JVM must check every array store operation at runtime to prevent type pollution.

```java
Animal[] animals = new Dog[10];
animals[0] = new Cat();  // ArrayStoreException
```

---

### Q8: What is invariance?

**Difficulty**: Easy

**Answer**:
Invariance means no subtype relationship. Even if `Dog extends Animal`, `List<Dog>` is NOT a subtype of `List<Animal>`. Java generics are invariant by default.

---

### Q9: When to use unbounded wildcard (List<?>)?

**Difficulty**: Medium

**Answer**:
When you don't care about the element type, only the collection itself.

```java
void printSize(List<?> list) {
    System.out.println(list.size());  // Don't need element type
}
```

Can read as `Object`, but can't add anything (except `null`).

---

### Q10: Can overriding method return a supertype?

**Difficulty**: Easy

**Answer**:
No. Overriding method must return the same type or a subtype (covariant return type). Returning a supertype would violate Liskov Substitution Principle.

```java
class Animal {
    Dog getAnimal() { return new Dog(); }
}

class Dog extends Animal {
    @Override
    Animal getAnimal() {  // ❌ COMPILE ERROR
        return new Animal();  // Can't return supertype
    }
}
```

---

## Common Traps

### ❌ Trap 1: Assuming Generics are Covariant

**Why it's wrong**:
Generics are invariant. `List<Dog>` is NOT a `List<Animal>`.

**Incorrect Code**:
```java
List<Dog> dogs = new ArrayList<>();
List<Animal> animals = dogs;  // ❌ COMPILE ERROR
```

**Correct Code**:
```java
List<? extends Animal> animals = dogs;  // ✅ Use wildcard
```

**Interview Tip**:
Use `? extends T` for covariance, `? super T` for contravariance.

---

### ❌ Trap 2: Adding to List<? extends T>

**Why it's wrong**:
Can't add elements (except `null`) because compiler doesn't know exact type.

**Incorrect Code**:
```java
List<? extends Animal> animals = new ArrayList<Dog>();
animals.add(new Dog());  // ❌ COMPILE ERROR
```

**Correct Code**:
```java
List<Animal> animals = new ArrayList<>();
animals.add(new Dog());  // ✅ Use concrete type
```

**Interview Tip**:
`? extends T` is for reading (producer), not writing.

---

### ❌ Trap 3: Reading from List<? super T> as T

**Why it's wrong**:
Can only read as `Object` because exact type is unknown.

**Incorrect Code**:
```java
List<? super Dog> list = new ArrayList<Animal>();
Dog d = list.get(0);  // ❌ COMPILE ERROR
```

**Correct Code**:
```java
Object obj = list.get(0);  // ✅ Read as Object
```

**Interview Tip**:
`? super T` is for writing (consumer), not reading.

---

## Related Topics

- [Generics](../05-Generics/01-Generics.md) - Type parameters and wildcards
- [Collections Framework](../04-Collections-Framework/01-List-Implementations.md) - Generic collections
- [Polymorphism](./05-OOP-Polymorphism-Deep-Dive.md) - Covariant return types
- [SOLID Principles](../10-SOLID-Principles/01-SOLID-Principles.md) - Liskov Substitution Principle

---

*Last Updated: February 2026*  
*Java Version: 5+ (Generics), 8, 11, 17, 21*
