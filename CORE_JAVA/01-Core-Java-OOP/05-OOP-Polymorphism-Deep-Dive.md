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

### Basic Level

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

**Q: What are covariant return types?**
A: Overridden method can return a subtype of parent's return type. Available since Java 5.

### Intermediate Level

**Q: Why shouldn't you call overridable methods from constructors?**
A: The overridden method runs before the subclass fields are initialized, leading to unexpected behavior (fields have default values like 0 or null).

**Q: What does `null` resolve to in overloaded methods?**
A: The most specific type. If ambiguous (multiple equally specific types), it's a compile error. Explicit cast resolves it: `test((String) null)`.

**Q: Can you override a private method?**
A: No. Private methods are not inherited, so you can't override them. You can define a method with the same signature in the subclass, but it's a new method, not an override.

**Q: Can you override a static method?**
A: No. Static methods are bound at compile time (static binding). You can define a static method with the same signature in the subclass, but it's method hiding, not overriding.

**Q: What is method hiding?**
A: When a subclass defines a static method with the same signature as a static method in the parent class. The method called depends on the reference type, not the object type.

**Q: Can you override a final method?**
A: No. Final methods cannot be overridden. Attempting to do so results in a compile error.

**Q: What happens if you change the return type in an overridden method?**
A: You can only return a covariant type (subtype). Returning an unrelated type or supertype causes a compile error.

**Q: Can you change the access modifier when overriding?**
A: Yes, but only to a more accessible level. You cannot reduce visibility (e.g., public → protected is not allowed).

### Advanced Level

**Q: Explain the difference between method overloading and method overriding with respect to exception handling.**
A: Overloading has no restrictions on exceptions. Overriding can throw same, subclass, or no exception, but NOT a broader checked exception than the parent method.

**Q: What is the diamond problem in Java and how does Java solve it?**
A: When a class inherits from two interfaces with the same default method. Java requires the class to explicitly override the method and choose which implementation to use via `InterfaceName.super.method()`.

**Q: How does the JVM implement dynamic method dispatch?**
A: Using a virtual method table (vtable). Each class has a vtable with pointers to method implementations. At runtime, JVM looks up the actual object's vtable to find the correct method.

**Q: What is the performance difference between compile-time and runtime polymorphism?**
A: Compile-time is faster (resolved at compile time). Runtime has slight overhead due to vtable lookup, but modern JVMs optimize this with JIT compilation and method inlining.

**Q: Can you have covariant return types with primitive types?**
A: No. Covariance only works with reference types. Primitives cannot be subtypes of each other in the inheritance sense.

**Q: What is contravariant parameter types and does Java support it?**
A: Contravariance means overridden method can accept a supertype parameter. Java does NOT support this - parameters must be exactly the same type (invariant).

**Q: Explain the Liskov Substitution Principle in the context of polymorphism.**
A: Objects of a superclass should be replaceable with objects of a subclass without breaking the application. Overridden methods should not weaken preconditions or strengthen postconditions.

**Q: How does polymorphism work with generics and type erasure?**
A: Generics are erased at runtime. Polymorphism works on the erased types. Bridge methods are generated to maintain polymorphism when generic types are involved.

### Scenario-Based Questions

**Q: What will this code print and why?**
```java
class Parent {
    void display() { System.out.println("Parent"); }
}
class Child extends Parent {
    void display() { System.out.println("Child"); }
}
Parent p = new Child();
p.display();
```
A: Prints "Child". Runtime polymorphism - JVM uses the actual object type (Child), not the reference type (Parent).

**Q: What will this code print and why?**
```java
class Test {
    void show(Object o) { System.out.println("Object"); }
    void show(String s) { System.out.println("String"); }
}
new Test().show(null);
```
A: Prints "String". Compile-time resolution chooses the most specific type. String is more specific than Object.

**Q: What will this code print and why?**
```java
class Parent {
    int x = 10;
}
class Child extends Parent {
    int x = 20;
}
Parent p = new Child();
System.out.println(p.x);
```
A: Prints "10". Fields are not polymorphic - resolved by reference type (Parent), not object type (Child).

**Q: Will this code compile? If yes, what will it print?**
```java
class Parent {
    Number getValue() { return 10; }
}
class Child extends Parent {
    Integer getValue() { return 20; }
}
Parent p = new Child();
System.out.println(p.getValue());
```
A: Yes, compiles. Prints "20". Covariant return type - Integer is a subtype of Number. Runtime polymorphism applies.

**Q: What's wrong with this code?**
```java
class Parent {
    void process(int x) throws IOException { }
}
class Child extends Parent {
    void process(int x) throws Exception { }
}
```
A: Compile error. Overridden method cannot throw a broader checked exception (Exception) than the parent method (IOException).

### Tricky Interview Scenarios

**Q: What will this code print?**
```java
class A {
    void m1() { System.out.print("A"); }
}
class B extends A {
    void m1() { System.out.print("B"); }
    void m2() { m1(); }
}
class C extends B {
    void m1() { System.out.print("C"); }
}
A a = new C();
((B) a).m2();
```
A: Prints "C". Even though we cast to B, the actual object is C. When m2() calls m1(), it uses dynamic dispatch and calls C's m1().

**Q: What will this code print?**
```java
class Test {
    void show(int... x) { System.out.println("varargs"); }
    void show(int x) { System.out.println("int"); }
}
new Test().show(10);
```
A: Prints "int". Exact match beats varargs in overload resolution.

**Q: What will this code print?**
```java
class Parent {
    static void display() { System.out.println("Parent"); }
}
class Child extends Parent {
    static void display() { System.out.println("Child"); }
}
Parent p = new Child();
p.display();
```
A: Prints "Parent". Static methods are not polymorphic - resolved by reference type at compile time (method hiding, not overriding).
