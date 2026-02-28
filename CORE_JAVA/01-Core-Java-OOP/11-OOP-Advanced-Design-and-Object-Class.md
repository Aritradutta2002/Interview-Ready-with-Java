# Advanced OOP Interview Topics

## Table of Contents
1. [Composition vs Inheritance](#composition-vs-inheritance)
2. [Covariant Return Types](#covariant-return-types)
3. [Method Hiding vs Overriding](#method-hiding-vs-overriding)
4. [Object Class Methods](#object-class-methods)
5. [Cloning in Java](#cloning-in-java)
6. [Serialization](#serialization)
7. [Association, Aggregation, Composition](#association-aggregation-composition)
8. [SOLID in OOP Context](#solid-in-oop-context)

---

## Composition vs Inheritance

### The Problem with Inheritance

```java
// ❌ Fragile Base Class Problem
class Bird {
    void fly() {
        System.out.println("Flying");
    }
}

class Penguin extends Bird {
    // Penguin can't fly! But inherits fly()
}

// ❌ Tight coupling
class Stack extends ArrayList {  // Stack IS-A ArrayList? No!
    // Inherits all ArrayList methods - wrong semantics
}
```

### Composition - Better Approach

```java
// ✅ Composition over Inheritance
class Stack {
    private List<Object> list = new ArrayList<>();  // HAS-A relationship
    
    public void push(Object item) {
        list.add(item);
    }
    
    public Object pop() {
        if (list.isEmpty()) throw new EmptyStackException();
        return list.remove(list.size() - 1);
    }
    
    // Only expose stack operations, not all List methods
}
```

### When to Use What

| Use Inheritance When | Use Composition When |
|---------------------|---------------------|
| IS-A relationship | HAS-A relationship |
| Code reuse in related classes | Code reuse across unrelated classes |
| Polymorphism needed | Flexibility needed |
| Base class is stable | Behavior may change at runtime |

### Liskov Substitution Principle

```java
// ✅ Valid inheritance
class Rectangle {
    protected int width, height;
    
    public void setWidth(int w) { this.width = w; }
    public void setHeight(int h) { this.height = h; }
    public int getArea() { return width * height; }
}

// ❌ Violates LSP
class Square extends Rectangle {
    @Override
    public void setWidth(int w) {
        width = height = w;  // Breaks Rectangle contract!
    }
    
    @Override
    public void setHeight(int h) {
        width = height = h;  // Breaks Rectangle contract!
    }
}

// Test
Rectangle r = new Square();
r.setWidth(5);
r.setHeight(10);
System.out.println(r.getArea());  // Expected: 50, Got: 100!
```

---

## Covariant Return Types

### Definition
Overridden method can return a subtype of the parent's return type.

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

// Usage
Animal animal = new Dog();
Dog puppy = (Dog) animal.reproduce();  // No cast needed in Java 5+
```

### Benefits
- More specific return types
- Eliminates casting
- Better type safety

```java
// Before Java 5
class Parent {
    Object get() { return new Object(); }
}

class Child extends Parent {
    Object get() { return new String("Hello"); }  // Return Object
}

// Usage required casting
String s = (String) new Child().get();

// After Java 5 (Covariant)
class Parent {
    Object get() { return new Object(); }
}

class Child extends Parent {
    @Override
    String get() { return "Hello"; }  // Return specific type
}

// No casting needed
String s = new Child().get();
```

---

## Method Hiding vs Overriding

### Method Overriding (Instance Methods)

```java
class Parent {
    void show() {  // Instance method
        System.out.println("Parent");
    }
}

class Child extends Parent {
    @Override
    void show() {  // Overriding
        System.out.println("Child");
    }
}

// Runtime polymorphism
Parent p = new Child();
p.show();  // Output: "Child" (runtime binding)
```

### Method Hiding (Static Methods)

```java
class Parent {
    static void show() {  // Static method
        System.out.println("Parent");
    }
}

class Child extends Parent {
    static void show() {  // Hiding, not overriding
        System.out.println("Child");
    }
}

// Compile-time binding
Parent p = new Child();
p.show();  // Output: "Parent" (compile-time binding)

Child c = new Child();
c.show();  // Output: "Child"
```

### Key Differences

| Aspect | Overriding | Hiding |
|--------|-----------|--------|
| Methods | Instance methods | Static methods |
| Binding | Runtime (dynamic) | Compile-time (static) |
| Polymorphism | Yes | No |
| Annotation | @Override | Not applicable |
| Resolution | Based on object type | Based on reference type |

---

## Object Class Methods

### All Methods

```java
public class Object {
    // 1. toString()
    public String toString();
    
    // 2. equals()
    public boolean equals(Object obj);
    
    // 3. hashCode()
    public native int hashCode();
    
    // 4. getClass()
    public final native Class<?> getClass();
    
    // 5. clone()
    protected native Object clone() throws CloneNotSupportedException;
    
    // 6. finalize()
    @Deprecated(since="9")
    protected void finalize() throws Throwable;
    
    // 7. wait()
    public final void wait() throws InterruptedException;
    public final void wait(long timeout) throws InterruptedException;
    public final void wait(long timeout, int nanos) throws InterruptedException;
    
    // 8. notify()
    public final native void notify();
    
    // 9. notifyAll()
    public final native void notifyAll();
}
```

### equals() and hashCode() Contract

```java
class Person {
    private String name;
    private int age;
    
    // Must override both together
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}

// Contract:
// 1. If equals() returns true, hashCode() must be same
// 2. If hashCode() different, equals() must return false
// 3. hashCode() should be consistent
```

### toString() Best Practice

```java
class Employee {
    private int id;
    private String name;
    
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

// Usage
Employee e = new Employee(1, "John");
System.out.println(e);  // Employee{id=1, name='John'}
```

---

## Cloning in Java

### Shallow Copy vs Deep Copy

```java
// Shallow Copy - copies references
class Shallow implements Cloneable {
    int[] data;
    
    public Shallow clone() throws CloneNotSupportedException {
        return (Shallow) super.clone();  // Shallow copy
    }
}

// Deep Copy - creates new objects
class Deep implements Cloneable {
    int[] data;
    
    public Deep clone() throws CloneNotSupportedException {
        Deep copy = (Deep) super.clone();
        copy.data = this.data.clone();  // Deep copy
        return copy;
    }
}

// Test
Shallow s1 = new Shallow();
s1.data = new int[]{1, 2, 3};
Shallow s2 = s1.clone();
s2.data[0] = 100;
System.out.println(s1.data[0]);  // 100! (shared reference)
```

### Copy Constructor Alternative

```java
class Person {
    private String name;
    private List<String> hobbies;
    
    // Copy constructor
    public Person(Person other) {
        this.name = other.name;
        this.hobbies = new ArrayList<>(other.hobbies);  // Deep copy
    }
}
```

---

## Serialization

### Basic Serialization

```java
// Serializable class
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private transient String password;  // Won't be serialized
    private static String company;      // Won't be serialized
    
    // Custom serialization
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        // Encrypt password before saving
        out.writeObject(encrypt(password));
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Decrypt password after reading
        password = decrypt((String) in.readObject());
    }
}

// Serialize
Employee emp = new Employee("John", "secret");
ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("emp.ser"));
oos.writeObject(emp);

// Deserialize
ObjectInputStream ois = new ObjectInputStream(new FileInputStream("emp.ser"));
Employee restored = (Employee) ois.readObject();
```

### Externalizable Interface

```java
public class Employee implements Externalizable {
    private String name;
    private int age;
    
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeInt(age);
    }
    
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        age = in.readInt();
    }
}
```

---

## Association, Aggregation, Composition

### Association (Has-A, weakest)

```java
// Student and Teacher are associated
class Student {
    private Teacher teacher;  // Can have different teachers
    
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}

class Teacher {
    // Teacher exists independently
}
```

### Aggregation (Has-A, whole-part, independent)

```java
// Department HAS Professors
// Professors can exist without Department
class Department {
    private List<Professor> professors;  // Aggregation
    
    public Department(List<Professor> professors) {
        this.professors = professors;  // Reference passed in
    }
}

class Professor {
    // Can exist without Department
}
```

### Composition (Has-A, whole-part, dependent)

```java
// House HAS Rooms
// Rooms cannot exist without House
class House {
    private List<Room> rooms;  // Composition
    
    public House() {
        this.rooms = new ArrayList<>();  // Created with House
        rooms.add(new Room());
        rooms.add(new Room());
    }
}

class Room {
    // Cannot exist without House
}
```

### Comparison

| Type | Relationship | Lifetime | Example |
|------|-------------|----------|---------|
| Association | Uses | Independent | Doctor - Patient |
| Aggregation | Has-A | Independent | University - Professor |
| Composition | Contains | Dependent | Car - Engine |

---

## SOLID in OOP Context

> **📌 For full SOLID coverage with detailed examples, see [10-SOLID-Principles/](../10-SOLID-Principles/)**

Quick OOP-focused summary:
- **S - Single Responsibility:** One class, one reason to change
- **O - Open/Closed:** Open for extension, closed for modification (use interfaces)
- **L - Liskov Substitution:** Subtypes must be substitutable for parent types
- **I - Interface Segregation:** Prefer small, specific interfaces over fat ones
- **D - Dependency Inversion:** Depend on abstractions, not concrete implementations

---

## Interview Questions

### Q1: Composition vs Inheritance - which to prefer?
**A:** Prefer composition. Inheritance creates tight coupling and fragile base class problem. Composition is more flexible.

### Q2: What is covariant return type?
**A:** Overridden method can return a subtype of parent's return type. Introduced in Java 5.

### Q3: Can static method be overridden?
**A:** No, static methods are hidden, not overridden. No runtime polymorphism.

### Q4: Why override equals() and hashCode() together?
**A:** Contract requires equal objects to have equal hash codes. Used in HashMap, HashSet.

### Q5: Difference between shallow and deep copy?
**A:** Shallow copies references, deep copies actual objects. Use clone() or copy constructor.

### Q6: Association vs Aggregation vs Composition?
**A:** 
- Association: Uses relationship
- Aggregation: Has-A, independent lifetime
- Composition: Contains, dependent lifetime

### Q7: What is serialVersionUID?
**A:** Version number for serialization. Ensures compatibility between serialized and deserialized objects.

### Q8: Serializable vs Externalizable?
**A:** Externalizable gives full control over serialization process. See [15-IO-NIO/](../15-IO-NIO/) for detailed coverage.
