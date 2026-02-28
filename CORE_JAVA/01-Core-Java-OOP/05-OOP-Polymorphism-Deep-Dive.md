# Polymorphism Deep Dive

## Table of Contents
1. [Compile-time vs Runtime Polymorphism](#compile-time-vs-runtime-polymorphism)
2. [Method Overloading Resolution Rules](#method-overloading-resolution-rules)
3. [Upcasting and Downcasting](#upcasting-and-downcasting)
4. [Dynamic Method Dispatch](#dynamic-method-dispatch)
5. [Tricky Polymorphism Scenarios](#tricky-polymorphism-scenarios)
6. [Interview Questions](#interview-questions)

---

## Compile-time vs Runtime Polymorphism

```
┌───────────────────────────────────────────────────────────────┐
│                    POLYMORPHISM                                │
├──────────────────────────┬────────────────────────────────────┤
│  Compile-time (Static)   │     Runtime (Dynamic)              │
├──────────────────────────┼────────────────────────────────────┤
│  Method Overloading      │     Method Overriding              │
│  Resolved by COMPILER    │     Resolved by JVM at runtime     │
│  Based on reference type │     Based on object type           │
│  Faster                  │     Slightly slower (vtable)       │
│  Also: static, final,    │     Instance methods only          │
│  private methods         │                                    │
└──────────────────────────┴────────────────────────────────────┘
```

---

## Method Overloading Resolution Rules

### The Resolution Priority Order

When the compiler sees a method call, it picks the **most specific** match in this order:

```
Priority 1: EXACT MATCH
Priority 2: WIDENING (primitive promotion)
Priority 3: AUTO-BOXING / UNBOXING
Priority 4: VARARGS
```

### 1. Exact Match (Highest Priority)
```java
void print(int x)    { System.out.println("int"); }
void print(double x) { System.out.println("double"); }

print(10);    // "int"    — exact match
print(3.14);  // "double" — exact match
```

### 2. Widening (Primitive Promotion)
```java
void show(long x) { System.out.println("long"); }
void show(float x) { System.out.println("float"); }

show(10);  // "long" — int widened to long (int → long → float → double)
```

**Widening order:**
```
byte → short → int → long → float → double
         ↑
        char
```

```java
void test(long x)   { System.out.println("long"); }
void test(double x) { System.out.println("double"); }

test(10);     // "long"   — int widens to long (closest)
test(10.0f);  // "double" — float widens to double
```

### 3. Auto-Boxing / Unboxing
```java
void process(Integer x) { System.out.println("Integer"); }
void process(long x)    { System.out.println("long"); }

process(10);  // "long" — widening beats boxing!
```

**⚠️ Key Rule: Widening beats Boxing**

```java
// Only boxing version available
void handle(Integer x) { System.out.println("Integer"); }

handle(10);  // "Integer" — int auto-boxed to Integer
```

### 4. Varargs (Lowest Priority)
```java
void log(int x)      { System.out.println("int"); }
void log(int... x)   { System.out.println("varargs"); }

log(10);      // "int"     — exact match beats varargs
log(10, 20);  // "varargs" — no other match
```

### Ambiguity Scenarios
```java
void ambiguous(int x, long y)  { }
void ambiguous(long x, int y)  { }

ambiguous(10, 10);  // ❌ COMPILE ERROR — ambiguous!
// Both methods require one widening — compiler can't decide
```

```java
void tricky(Integer x) { System.out.println("Integer"); }
void tricky(Object x)  { System.out.println("Object"); }

tricky(10);   // "Integer" — most specific type wins
tricky(null); // "Integer" — most specific non-primitive wins
```

### Widening + Boxing Don't Combine
```java
void method(Long x) { System.out.println("Long"); }

method(10);  // ❌ COMPILE ERROR!
// int → long (widening) then long → Long (boxing) = NOT allowed
// Java does NOT widen then box
```

```java
// But boxing then widening IS allowed:
void method(Object x) { System.out.println("Object"); }

method(10);  // ✅ "Object" — int → Integer (box) → Object (widen)
```

---

## Upcasting and Downcasting

### Upcasting (Implicit — Always Safe)
```java
class Animal { void eat() { System.out.println("eating"); } }
class Dog extends Animal { void bark() { System.out.println("barking"); } }

// Upcasting: Child → Parent (implicit, always safe)
Animal a = new Dog();  // ✅ Implicit upcasting
a.eat();               // ✅ Works
// a.bark();           // ❌ COMPILE ERROR — Animal reference can't see Dog methods
```

### Downcasting (Explicit — Risky)
```java
Animal a = new Dog();

// Downcasting: Parent → Child (explicit, can fail)
Dog d = (Dog) a;       // ✅ Works — actual object IS a Dog
d.bark();              // ✅ Works

Animal a2 = new Animal();
Dog d2 = (Dog) a2;    // ❌ ClassCastException at RUNTIME!
// Compiles fine, but actual object is NOT a Dog
```

### Safe Downcasting with instanceof
```java
Animal a = getAnimal();  // Could be any Animal subtype

// Always check before downcasting
if (a instanceof Dog) {
    Dog d = (Dog) a;  // ✅ Safe
    d.bark();
}

// Java 16+ pattern matching
if (a instanceof Dog d) {   // Check + cast in one step
    d.bark();                // ✅ d already cast
}
```

### ClassCastException
```java
Object obj = "Hello";
Integer num = (Integer) obj;  // ❌ ClassCastException!
// String cannot be cast to Integer

// Prevention:
if (obj instanceof Integer) {
    Integer num = (Integer) obj;
}
```

### Casting Rules Summary
```
┌─────────────────────────────────────────────────────────────┐
│  Upcasting:   Child → Parent    IMPLICIT, always safe       │
│  Downcasting: Parent → Child    EXPLICIT, can throw CCE     │
│  Unrelated:   String → Integer  COMPILE ERROR               │
└─────────────────────────────────────────────────────────────┘
```

---

## Dynamic Method Dispatch

### How JVM Resolves Methods at Runtime

```java
class Shape {
    void draw() { System.out.println("Shape"); }
    static void info() { System.out.println("Shape info"); }
}

class Circle extends Shape {
    @Override
    void draw() { System.out.println("Circle"); }
    static void info() { System.out.println("Circle info"); }
}

Shape s = new Circle();

s.draw();  // "Circle" — RUNTIME dispatch (virtual method)
s.info();  // "Shape info" — COMPILE-TIME binding (static method)
```

### Fields Are NOT Polymorphic
```java
class Parent {
    int x = 10;
}

class Child extends Parent {
    int x = 20;
}

Parent p = new Child();
System.out.println(p.x);  // 10 — fields resolved by REFERENCE type, not object type!

Child c = new Child();
System.out.println(c.x);  // 20
```

### Private/Final Methods — No Polymorphism
```java
class Parent {
    private void secret() { System.out.println("Parent secret"); }
    final void locked()   { System.out.println("Parent locked"); }
    
    void callSecret() { secret(); }  // Calls Parent's secret()
}

class Child extends Parent {
    // This is a NEW method, not an override
    private void secret() { System.out.println("Child secret"); }
    // void locked() { }  // ❌ COMPILE ERROR — can't override final
}

Parent p = new Child();
p.callSecret();  // "Parent secret" — private methods are NOT virtual
```

---

## Tricky Polymorphism Scenarios

### Scenario 1: Overloading + Overriding Together
```java
class Animal {
    void speak()         { System.out.println("Animal speaks"); }
    void speak(String s) { System.out.println("Animal says: " + s); }
}

class Dog extends Animal {
    @Override
    void speak() { System.out.println("Dog barks"); }
    // speak(String) is inherited, not overridden
}

Animal a = new Dog();
a.speak();          // "Dog barks" — overriding (runtime)
a.speak("hello");   // "Animal says: hello" — inherited method
```

### Scenario 2: Constructor + Polymorphism Trap
```java
class Base {
    Base() {
        display();  // ⚠️ Calls overridden method!
    }
    void display() { System.out.println("Base"); }
}

class Derived extends Base {
    int value = 10;
    
    @Override
    void display() {
        System.out.println("Derived: " + value);
    }
}

Derived d = new Derived();
// Output: "Derived: 0"  ← NOT 10!
// Base constructor runs before Derived's fields are initialized
// value is still 0 (default) when display() is called
```

### Scenario 3: Covariant Return Types
```java
class Producer {
    Animal produce() { return new Animal(); }
}

class DogBreeder extends Producer {
    @Override
    Dog produce() { return new Dog(); }  // ✅ Covariant return
}

Producer p = new DogBreeder();
Animal a = p.produce();  // Returns Dog (runtime polymorphism)
```

### Scenario 4: null and Overloading
```java
void test(Object o)  { System.out.println("Object"); }
void test(String s)  { System.out.println("String"); }
void test(Integer i) { System.out.println("Integer"); }

test(null);  // ❌ COMPILE ERROR — ambiguous between String and Integer
// Both String and Integer are equally specific subtypes of Object
```

```java
// But this works:
void test(Object o)  { System.out.println("Object"); }
void test(String s)  { System.out.println("String"); }

test(null);  // "String" — most specific type wins
```

---

## Interview Questions

**Q: What is the difference between compile-time and runtime polymorphism?**
A: Compile-time = overloading (resolved by compiler based on reference type). Runtime = overriding (resolved by JVM based on actual object type).

**Q: What is the method overloading resolution order?**
A: Exact match → Widening → Boxing → Varargs. Widening beats boxing.

**Q: Can widening and boxing happen together?**
A: Not widening-then-boxing (`int → long → Long` fails). But boxing-then-widening works (`int → Integer → Object`).

**Q: What is upcasting vs downcasting?**
A: Upcasting = child to parent reference (implicit, safe). Downcasting = parent to child reference (explicit, can throw ClassCastException).

**Q: When does ClassCastException occur?**
A: At runtime when you downcast to a type that the actual object is NOT an instance of.

**Q: Are fields polymorphic in Java?**
A: No. Fields are resolved by reference type at compile time, not by object type.

**Q: Why shouldn't you call overridable methods from constructors?**
A: The overridden method runs before the subclass fields are initialized, leading to unexpected behavior (fields have default values like 0 or null).

**Q: What are covariant return types?**
A: Overridden method can return a subtype of parent's return type. Available since Java 5.

**Q: What does `null` resolve to in overloaded methods?**
A: The most specific type. If ambiguous (multiple equally specific types), it's a compile error. Explicit cast resolves it: `test((String) null)`.
