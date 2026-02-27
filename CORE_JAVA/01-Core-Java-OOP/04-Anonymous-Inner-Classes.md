# Anonymous Classes & Inner Classes - Complete Interview Guide

## Table of Contents
1. [Types of Nested Classes](#types-of-nested-classes)
2. [Inner Classes (Non-static)](#inner-classes-non-static)
3. [Static Nested Classes](#static-nested-classes)
4. [Anonymous Classes](#anonymous-classes)
5. [Local Classes](#local-classes)
6. [Interview Questions](#interview-questions)

---

## Types of Nested Classes

```
┌─────────────────────────────────────┐
│           Nested Classes            │
├─────────────────────────────────────┤
│  ┌─────────────┐  ┌───────────────┐ │
│  │   Inner     │  │    Static     │ │
│  │   Class     │  │Nested Class   │ │
│  └──────┬──────┘  └───────────────┘ │
│         │                           │
│    ┌────┴────┐                      │
│    │         │                      │
│ ┌──┴──┐   ┌─┴────┐                 │
│ │Local│   │Anony-│                 │
│ │Class│   │mous  │                 │
│ └─────┘   └──────┘                 │
└─────────────────────────────────────┘
```

---

## Inner Classes (Non-static)

### Definition
A class defined within another class. It has access to all members of the outer class (including private).

```java
class Outer {
    private int outerVar = 10;
    
    // Inner class
    class Inner {
        void display() {
            System.out.println("Outer var: " + outerVar); // ✅ Access private
        }
    }
}

// Usage
Outer outer = new Outer();
Outer.Inner inner = outer.new Inner();  // Need outer instance
inner.display();
```

### Key Characteristics
- **Cannot** have static declarations (except static final constants)
- **Can** access all outer class members
- Requires outer class instance to create

```java
class Outer {
    private static int staticVar = 100;
    private int instanceVar = 200;
    
    class Inner {
        // static int x = 10;  // ❌ ERROR! Cannot have static members
        static final int CONSTANT = 100; // ✅ OK - constant
        
        void show() {
            System.out.println(staticVar);    // ✅ Access static
            System.out.println(instanceVar);  // ✅ Access instance
        }
    }
}
```

---

## Static Nested Classes

### Definition
A static class defined within another class. Behaves like a top-level class but is scoped within outer class.

```java
class Outer {
    private static int staticVar = 10;
    private int instanceVar = 20;
    
    static class StaticNested {
        void display() {
            System.out.println(staticVar);      // ✅ Access static
            // System.out.println(instanceVar); // ❌ ERROR! No instance access
        }
    }
}

// Usage - No outer instance needed
Outer.StaticNested nested = new Outer.StaticNested();
```

### Use Cases
- **Builder Pattern**: `Person.Builder builder = new Person.Builder()`
- Grouping related classes
- Better encapsulation

```java
// Builder Pattern Example
public class Computer {
    private String cpu;
    private int ram;
    
    private Computer(Builder builder) {
        this.cpu = builder.cpu;
        this.ram = builder.ram;
    }
    
    // Static nested class
    public static class Builder {
        private String cpu;
        private int ram;
        
        public Builder cpu(String cpu) {
            this.cpu = cpu;
            return this;
        }
        
        public Builder ram(int ram) {
            this.ram = ram;
            return this;
        }
        
        public Computer build() {
            return new Computer(this);
        }
    }
}

// Usage
Computer pc = new Computer.Builder()
    .cpu("Intel i7")
    .ram(16)
    .build();
```

---

## Anonymous Classes

### Definition
A class without a name, declared and instantiated in a single expression. Used for one-time implementations.

```java
// Traditional way
class MyRunnable implements Runnable {
    public void run() {
        System.out.println("Running");
    }
}

// Anonymous class way
Runnable r = new Runnable() {
    @Override
    public void run() {
        System.out.println("Running");
    }
};
```

### Common Use Cases

#### 1. Thread Creation
```java
// Anonymous Thread
Thread t = new Thread() {
    @Override
    public void run() {
        System.out.println("Thread running");
    }
};
t.start();
```

#### 2. ActionListener (GUI)
```java
button.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Button clicked");
    }
});
```

#### 3. Comparator
```java
Collections.sort(list, new Comparator<String>() {
    @Override
    public int compare(String s1, String s2) {
        return s1.length() - s2.length();
    }
});
```

### Anonymous Class with Abstract Class
```java
abstract class Animal {
    abstract void makeSound();
}

// Anonymous subclass
Animal dog = new Animal() {
    @Override
    void makeSound() {
        System.out.println("Bark");
    }
};

dog.makeSound(); // Output: Bark
```

### Key Characteristics
- Cannot have explicit constructors
- Can access final or effectively final local variables
- Cannot define static members (except constants)

```java
void method() {
    final int x = 10;
    int y = 20; // Effectively final
    
    Runnable r = new Runnable() {
        @Override
        public void run() {
            System.out.println(x); // ✅ OK
            System.out.println(y); // ✅ OK (effectively final)
            // y = 30; // ❌ Would make y not effectively final
        }
    };
}
```

---

## Local Classes

### Definition
A class defined within a method or block. Scope is limited to that block.

```java
void process() {
    // Local class inside method
    class LocalProcessor {
        void process() {
            System.out.println("Processing...");
        }
    }
    
    LocalProcessor processor = new LocalProcessor();
    processor.process();
} // Local class not accessible outside
```

### Characteristics
- Cannot have access modifiers (public, private, etc.)
- Can access final/effectively final local variables
- Can access outer class members

```java
class Outer {
    private int outerVar = 10;
    
    void method(final int param) {
        final int localVar = 20;
        
        class Local {
            void display() {
                System.out.println(outerVar);   // ✅ Outer class member
                System.out.println(param);      // ✅ Final parameter
                System.out.println(localVar);   // ✅ Final local variable
            }
        }
        
        new Local().display();
    }
}
```

---

## Comparison Table

| Feature | Inner Class | Static Nested | Anonymous | Local |
|---------|-------------|---------------|-----------|-------|
| Access to outer instance | ✅ Yes | ❌ No | ✅ Yes | ✅ Yes |
| Can have static members | ❌ No (except constants) | ✅ Yes | ❌ No | ❌ No |
| Requires outer instance | ✅ Yes | ❌ No | ✅ Yes | ✅ Yes |
| Has name | ✅ Yes | ✅ Yes | ❌ No | ✅ Yes |
| Can extend/implement | ✅ Yes | ✅ Yes | ✅ One only | ✅ Yes |
| Access modifiers | ✅ Yes | ✅ Yes | ❌ No | ❌ No |

---

## Interview Questions

### Q1: Can we have static methods in inner class?
**A:** No, inner (non-static) classes cannot have static methods or static fields (except static final constants).

```java
class Outer {
    class Inner {
        // static void method() {} // ❌ ERROR
        static final int X = 10; // ✅ OK
    }
}
```

### Q2: Why use anonymous classes?
**A:** 
- One-time use implementations
- Reduces boilerplate code
- Common in event handling, callbacks, comparators
- Replaced by lambdas in Java 8+ for functional interfaces

### Q3: Memory leak with inner classes?
**A:** Yes! Inner classes hold reference to outer class. If inner class instance lives longer, outer class cannot be garbage collected.

```java
// Potential memory leak
public class Outer {
    private byte[] largeData = new byte[1024 * 1024]; // 1MB
    
    class Inner {
        void doSomething() {}
    }
    
    public Inner getInner() {
        return new Inner(); // Holds reference to Outer
    }
}

// Fix: Use static nested class
public static class Inner { // No reference to outer
    void doSomething() {}
}
```

### Q4: Difference between anonymous class and lambda?
**A:**
| Anonymous Class | Lambda |
|-----------------|--------|
| Can implement any interface/abstract class | Only functional interfaces |
| Creates new class file | Uses invokedynamic |
| Can override Object methods | Cannot |
| `this` refers to anonymous class | `this` refers to enclosing class |

### Q5: Can local class access non-final local variables?
**A:** No, only final or effectively final variables (Java 8+).

### Q6: What is effectively final?
**A:** A variable that is not declared final but never modified after initialization.

```java
void method() {
    int x = 10; // Effectively final
    // x = 20;  // Would make it NOT effectively final
    
    Runnable r = () -> System.out.println(x); // ✅ OK
}
```

---

## Quick Reference

```java
// Inner Class
Outer.Inner inner = new Outer().new Inner();

// Static Nested Class
Outer.StaticNested nested = new Outer.StaticNested();

// Anonymous Class
Runnable r = new Runnable() {
    public void run() { }
};

// Lambda (Java 8+)
Runnable r = () -> { };

// Local Class
void method() {
    class Local { }
    new Local();
}
```
