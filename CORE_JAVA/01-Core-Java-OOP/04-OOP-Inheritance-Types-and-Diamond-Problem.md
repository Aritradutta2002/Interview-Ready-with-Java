# Inheritance in Java - Complete Guide with Diagrams

## Table of Contents
1. [What is Inheritance?](#what-is-inheritance)
2. [Types of Inheritance](#types-of-inheritance)
3. [Java Supported vs Not Supported](#java-supported-vs-not-supported)
4. [The Diamond Problem](#the-diamond-problem)
5. [How Java Achieves Multiple Inheritance](#how-java-achieves-multiple-inheritance)
6. [Method Resolution](#method-resolution)
7. [Interview Questions](#interview-questions)

---

## What is Inheritance?

**Definition:** Inheritance is an OOP concept where a class (child/subclass) acquires properties and behaviors from another class (parent/superclass).

```java
// Parent class
class Animal {
    void eat() {
        System.out.println("Animal eats");
    }
}

// Child class
class Dog extends Animal {
    void bark() {
        System.out.println("Dog barks");
    }
}

// Usage
Dog dog = new Dog();
dog.eat();   // Inherited from Animal
dog.bark();  // Own method
```

---

## Types of Inheritance

### 1. Single Inheritance ✅ (SUPPORTED)

One child class inherits from one parent class.

```
    ┌─────────────┐
    │   Animal    │
    │   eat()     │
    └──────┬──────┘
           │ extends
           ▼
    ┌─────────────┐
    │     Dog     │
    │   bark()    │
    └─────────────┘
```

```java
class Animal {
    void eat() { }
}

class Dog extends Animal {
    void bark() { }
}
```

---

### 2. Multilevel Inheritance ✅ (SUPPORTED)

A class inherits from a class which itself inherits from another class.

```
    ┌─────────────┐
    │   Animal    │
    │   eat()     │
    └──────┬──────┘
           │ extends
           ▼
    ┌─────────────┐
    │     Dog     │
    │   bark()    │
    └──────┬──────┘
           │ extends
           ▼
    ┌─────────────┐
    │  Labrador   │
    │  fetch()    │
    └─────────────┘
```

```java
class Animal {
    void eat() { }
}

class Dog extends Animal {
    void bark() { }
}

class Labrador extends Dog {
    void fetch() { }
}

// Labrador can use: eat(), bark(), fetch()
```

---

### 3. Hierarchical Inheritance ✅ (SUPPORTED)

Multiple child classes inherit from a single parent class.

```
                    ┌─────────────┐
                    │   Animal    │
                    │   eat()     │
                    └──────┬──────┘
                           │
           ┌───────────────┼───────────────┐
           │ extends       │ extends       │ extends
           ▼               ▼               ▼
    ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
    │     Dog     │ │     Cat     │ │    Bird     │
    │   bark()    │ │   meow()    │ │   fly()     │
    └─────────────┘ └─────────────┘ └─────────────┘
```

```java
class Animal {
    void eat() { }
}

class Dog extends Animal {
    void bark() { }
}

class Cat extends Animal {
    void meow() { }
}

class Bird extends Animal {
    void fly() { }
}
```

---

### 4. Multiple Inheritance ❌ (NOT SUPPORTED with Classes)

A class inheriting from multiple parent classes.

```
    ┌─────────────┐         ┌─────────────┐
    │   Animal    │         │   Pet       │
    │   eat()     │         │   play()    │
    └──────┬──────┘         └──────┬──────┘
           │                       │
           └───────────┬───────────┘
                       │ extends
                       ▼
                ┌─────────────┐
                │     Dog     │
                └─────────────┘
```

```java
// ❌ ERROR! Java doesn't support this
class Dog extends Animal, Pet {  // NOT ALLOWED
}
```

**Why Not Supported?** See [The Diamond Problem](#the-diamond-problem)

---

### 5. Hybrid Inheritance ❌ (NOT SUPPORTED)

Combination of hierarchical and multiple inheritance.

```
                    ┌─────────────┐
                    │   Animal    │
                    └──────┬──────┘
                           │
           ┌───────────────┴───────────────┐
           │                               │
           ▼                               ▼
    ┌─────────────┐                 ┌─────────────┐
    │   Mammal    │                 │    Bird     │
    └──────┬──────┘                 └──────┬──────┘
           │                               │
           └───────────┬───────────────────┘
                       │
                       ▼
                ┌─────────────┐
                │   Bat       │  // Both mammal and bird?!
                └─────────────┘
```

**Not supported** because it involves multiple inheritance.

---

## Java Supported vs Not Supported

| Type | Support | How to Achieve |
|------|---------|----------------|
| Single | ✅ Yes | `class B extends A` |
| Multilevel | ✅ Yes | `class C extends B extends A` |
| Hierarchical | ✅ Yes | Multiple classes extend same parent |
| Multiple | ❌ No | Use **Interfaces** instead |
| Hybrid | ❌ No | Combination involving multiple inheritance |

---

## The Diamond Problem

### What is it?

When a class inherits from two classes that have the same method, there's ambiguity about which method to use.

```
                    ┌─────────────┐
                    │   Animal    │
                    │   eat()     │
                    └──────┬──────┘
                           │
           ┌───────────────┴───────────────┐
           │                               │
           ▼                               ▼
    ┌─────────────┐                 ┌─────────────┐
    │   Herbivore │                 │  Carnivore  │
    │   eat()     │                 │   eat()     │
    │ {plants}    │                 │ {meat}      │
    └──────┬──────┘                 └──────┬──────┘
           │                               │
           └───────────┬───────────────────┘
                       │
                       ▼
                ┌─────────────┐
                │   Bear      │  // Which eat() to use?
                └─────────────┘
```

```java
class Animal {
    void eat() { System.out.println("Animal eats"); }
}

class Herbivore extends Animal {
    void eat() { System.out.println("Eats plants"); }
}

class Carnivore extends Animal {
    void eat() { System.out.println("Eats meat"); }
}

// ❌ If Java allowed this:
class Bear extends Herbivore, Carnivore {
    // Which eat() method?
    // Herbivore.eat() or Carnivore.eat()?
}
```

### Why Java Avoids This
- **Ambiguity**: Compiler can't decide which method to call
- **Complexity**: Method resolution becomes complicated
- **Maintainability**: Code harder to understand and maintain

---

## How Java Achieves Multiple Inheritance

### Using Interfaces ✅

Java allows a class to implement multiple interfaces.

```
    ┌─────────────┐         ┌─────────────┐
    │  Runnable   │         │  Flyable    │
    │   run()     │         │   fly()     │
    └──────┬──────┘         └──────┬──────┘
           │                       │
           └───────────┬───────────┘
                       │ implements
                       ▼
                ┌─────────────┐
                │     Bird    │
                └─────────────┘
```

```java
interface Runnable {
    void run();
}

interface Flyable {
    void fly();
}

class Bird implements Runnable, Flyable {
    @Override
    public void run() {
        System.out.println("Bird runs");
    }
    
    @Override
    public void fly() {
        System.out.println("Bird flies");
    }
}
```

### Interface + Default Methods (Java 8+)

```java
interface Animal {
    default void eat() {
        System.out.println("Animal eats");
    }
}

interface Pet {
    default void eat() {
        System.out.println("Pet eats");
    }
}

// Must override to resolve conflict
class Dog implements Animal, Pet {
    @Override
    public void eat() {
        Animal.super.eat();  // Call specific interface method
        // OR
        System.out.println("Dog eats");
    }
}
```

---

## Method Resolution

### Order of Method Resolution

```java
class GrandParent {
    void show() { System.out.println("GrandParent"); }
}

class Parent extends GrandParent {
    void show() { System.out.println("Parent"); }
}

class Child extends Parent {
    // show() from Parent
}

Child c = new Child();
c.show(); // Output: "Parent" (nearest ancestor)
```

**Resolution Order:**
1. Current class
2. Immediate parent
3. Next parent up the chain
4. Object class

### super Keyword

```java
class Parent {
    void display() {
        System.out.println("Parent display");
    }
}

class Child extends Parent {
    void display() {
        super.display();  // Call parent method
        System.out.println("Child display");
    }
}
```

### Constructor Chaining

```java
class GrandParent {
    GrandParent() {
        System.out.println("GrandParent constructor");
    }
}

class Parent extends GrandParent {
    Parent() {
        super();  // Implicit call
        System.out.println("Parent constructor");
    }
}

class Child extends Parent {
    Child() {
        super();  // Implicit call
        System.out.println("Child constructor");
    }
}

// Output:
// GrandParent constructor
// Parent constructor
// Child constructor
```

---

## Interview Questions

### Q1: Why doesn't Java support multiple inheritance?
**A:** To avoid the "Diamond Problem" - ambiguity when same method exists in multiple parent classes.

### Q2: How to achieve multiple inheritance in Java?
**A:** Using interfaces. A class can implement multiple interfaces.

```java
class MyClass implements Interface1, Interface2, Interface3 { }
```

### Q3: Can interfaces extend multiple interfaces?
**A:** Yes! Interfaces can extend multiple interfaces.

```java
interface A { void methodA(); }
interface B { void methodB(); }
interface C extends A, B { void methodC(); }
```

### Q4: What happens if two interfaces have same default method?
**A:** Implementing class must override and resolve the conflict.

```java
interface A {
    default void show() { System.out.println("A"); }
}

interface B {
    default void show() { System.out.println("B"); }
}

class C implements A, B {
    @Override
    public void show() {
        A.super.show();  // Explicitly choose
    }
}
```

### Q5: Difference between extends and implements?
**A:**
| extends | implements |
|---------|------------|
| Class to class | Class to interface |
| Interface to interface | |
| Single inheritance | Multiple inheritance |
| Gets implementation | Gets contract |

### Q6: Can we extend multiple classes using extends?
**A:** No, extends is for single inheritance only.

### Q7: What is the root class of all Java classes?
**A:** `java.lang.Object`. Every class implicitly extends Object.

### Q8: Can a class extend itself?
**A:** No, cyclic inheritance is not allowed.

```java
class A extends A { }  // ❌ ERROR
```

### Q9: What is transitive nature of inheritance?
**A:** If A extends B, and B extends C, then A is also a C.

```java
class C { }
class B extends C { }
class A extends B { }

// A IS-A C (transitive)
```

### Q10: Can we prevent inheritance?
**A:** Yes, using `final` keyword.

```java
final class CannotExtend { }
// class Child extends CannotExtend { } // ❌ ERROR
```

---

## Quick Reference

```java
// Single Inheritance
class A { }
class B extends A { }

// Multilevel Inheritance
class A { }
class B extends A { }
class C extends B { }

// Hierarchical Inheritance
class A { }
class B extends A { }
class C extends A { }

// Multiple Inheritance (NOT ALLOWED with classes)
// class D extends B, C { } // ❌ ERROR

// Multiple Inheritance (ALLOWED with interfaces)
interface A { }
interface B { }
class C implements A, B { }

// Interface extending multiple interfaces
interface A { }
interface B { }
interface C extends A, B { }
```
