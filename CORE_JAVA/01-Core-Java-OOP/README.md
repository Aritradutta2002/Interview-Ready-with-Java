# Core Java OOP - Complete Interview Guide

> **Master Object-Oriented Programming concepts for Java interviews**

This folder contains comprehensive interview notes covering all OOP concepts with diagrams, code examples, and interview questions.

---

## 📚 Table of Contents

| File | Topics Covered | Link |
|------|---------------|------|
| **01-OOP-Pillars.md** | Encapsulation, Abstraction, Inheritance, Polymorphism | [Read](01-OOP-Pillars.md) |
| **02-Interface-vs-Abstract-Class.md** | Interface vs Abstract Class, Marker Interface, When to use | [Read](02-Interface-vs-Abstract-Class.md) |
| **03-Keywords.md** | final, static, transient, volatile, synchronized | [Read](03-Keywords.md) |
| **04-Anonymous-Inner-Classes.md** | Anonymous Classes, Inner Classes, Static Nested, Local Classes | [Read](04-Anonymous-Inner-Classes.md) |
| **05-Inheritance-Types.md** | All Inheritance Types, Diamond Problem, Multiple Inheritance | [Read](05-Inheritance-Types.md) |
| **06-Encapsulation-vs-Abstraction.md** | Detailed comparison with real-world examples | [Read](06-Encapsulation-vs-Abstraction.md) |
| **07-Advanced-OOP-Interview-Topics.md** | Composition vs Inheritance, Covariant Return, SOLID | [Read](07-Advanced-OOP-Interview-Topics.md) |

---

## 🎯 Quick Reference

### OOP Pillars

```
┌─────────────────────────────────────────────────────────┐
│              Object-Oriented Programming                │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │Encapsulation│  │  Inheritance│  │ Polymorphism│     │
│  │             │  │             │  │             │     │
│  │ Data hiding │  │  IS-A       │  │ Many forms  │     │
│  │ + Control   │  │  relationship│  │             │     │
│  └─────────────┘  └─────────────┘  └─────────────┘     │
│                                                         │
│              ┌─────────────┐                           │
│              │  Abstraction│                           │
│              │             │                           │
│              │ Hide complex│                           │
│              │   details   │                           │
│              └─────────────┘                           │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### Inheritance Types

```
Single              Multilevel           Hierarchical
    A                  A                    A
    │                  │                   /|\
    ▼                  ▼                  / | \
    B                  B                B  C  D
                       │
                       ▼
                       C

Multiple (❌)       Hybrid (❌)
   A     B              A
    \   /              /|\
     \ /              / | \
      C              B  C  D
                      \ | /
                       \|/
                        E
```

---

## 🔥 Most Asked Interview Questions

### Basic Level
1. **What are the 4 pillars of OOP?**
   - See [01-OOP-Pillars.md](01-OOP-Pillars.md)

2. **Difference between Encapsulation and Abstraction?**
   - See [06-Encapsulation-vs-Abstraction.md](06-Encapsulation-vs-Abstraction.md)

3. **Interface vs Abstract Class?**
   - See [02-Interface-vs-Abstract-Class.md](02-Interface-vs-Abstract-Class.md)

4. **Method Overloading vs Overriding?**
   - See [01-OOP-Pillars.md](01-OOP-Pillars.md)

### Intermediate Level
5. **Why Java doesn't support Multiple Inheritance?**
   - See [05-Inheritance-Types.md](05-Inheritance-Types.md) - Diamond Problem

6. **What are Anonymous Classes?**
   - See [04-Anonymous-Inner-Classes.md](04-Anonymous-Inner-Classes.md)

7. **Types of Inheritance in Java?**
   - See [05-Inheritance-Types.md](05-Inheritance-Types.md)

8. **final, finally, finalize difference?**
   - See [03-Keywords.md](03-Keywords.md)

### Advanced Level
9. **Composition vs Inheritance?**
   - See [07-Advanced-OOP-Interview-Topics.md](07-Advanced-OOP-Interview-Topics.md)

10. **Covariant Return Types?**
    - See [07-Advanced-OOP-Interview-Topics.md](07-Advanced-OOP-Interview-Topics.md)

11. **Method Hiding vs Overriding?**
    - See [07-Advanced-OOP-Interview-Topics.md](07-Advanced-OOP-Interview-Topics.md)

12. **Explain SOLID principles?**
    - See [07-Advanced-OOP-Interview-Topics.md](07-Advanced-OOP-Interview-Topics.md)

---

## 📖 Study Path

### Week 1: Fundamentals
- [01-OOP-Pillars.md](01-OOP-Pillars.md) - Master 4 pillars
- [02-Interface-vs-Abstract-Class.md](02-Interface-vs-Abstract-Class.md) - Understand differences
- [03-Keywords.md](03-Keywords.md) - Learn important keywords

### Week 2: Inheritance & Classes
- [05-Inheritance-Types.md](05-Inheritance-Types.md) - All inheritance types
- [04-Anonymous-Inner-Classes.md](04-Anonymous-Inner-Classes.md) - Inner classes
- [06-Encapsulation-vs-Abstraction.md](06-Encapsulation-vs-Abstraction.md) - Deep dive

### Week 3: Advanced Topics
- [07-Advanced-OOP-Interview-Topics.md](07-Advanced-OOP-Interview-Topics.md) - Advanced concepts
- Practice all interview questions
- Solve coding problems

---

## 💡 Key Concepts Summary

### Encapsulation
- Private fields + Public getters/setters
- Data hiding and validation
- JavaBeans convention

### Abstraction
- Abstract classes and interfaces
- Hiding implementation complexity
- Focus on "what" not "how"

### Inheritance
- IS-A relationship
- Code reuse
- Types: Single, Multilevel, Hierarchical
- Java doesn't support Multiple (Diamond Problem)

### Polymorphism
- Compile-time: Method Overloading
- Runtime: Method Overriding
- Dynamic method dispatch

### Inner Classes
- Inner Class (non-static)
- Static Nested Class
- Anonymous Class
- Local Class

---

## 🎯 Interview Tips

1. **Always explain with examples** - Don't just give definitions
2. **Draw diagrams** - Visual representation helps
3. **Know the differences** - Interface vs Abstract, Overloading vs Overriding
4. **Understand "why"** - Why Java doesn't support multiple inheritance
5. **Practice coding** - Write code for each concept
6. **Know real-world examples** - Bank Account, Car, Animal examples

---

## 📊 Question Distribution

| Topic | Frequency | Difficulty |
|-------|-----------|------------|
| OOP Pillars | ⭐⭐⭐⭐⭐ | Easy |
| Interface vs Abstract | ⭐⭐⭐⭐⭐ | Easy |
| Inheritance Types | ⭐⭐⭐⭐ | Medium |
| Encapsulation vs Abstraction | ⭐⭐⭐⭐ | Medium |
| Inner Classes | ⭐⭐⭐ | Medium |
| Advanced Topics | ⭐⭐⭐ | Hard |

---

## 🔗 Related Topics

- [Java Memory Model](../02-Java-Memory-Model) - Object creation, memory layout
- [Collections Framework](../04-Collections-Framework) - OOP in collections
- [Design Patterns](../09-Design-Patterns) - OOP patterns
- [SOLID Principles](../10-SOLID-Principles) - Design principles

---

**Happy Learning! 🚀**

*Master these concepts and crack any Java OOP interview question!*
