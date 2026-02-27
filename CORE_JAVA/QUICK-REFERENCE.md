# Java Interview Quick Reference Cheat Sheet

## 🔥 Most Asked Topics in FAANG

| Topic | Frequency | Key Points |
|-------|-----------|------------|
| HashMap Internal | ⭐⭐⭐⭐⭐ | Hashing, buckets, treeify, resize |
| ConcurrentHashMap | ⭐⭐⭐⭐⭐ | Segments, thread-safety |
| Java 8 Streams | ⭐⭐⭐⭐⭐ | map, flatMap, reduce, collect |
| Multithreading | ⭐⭐⭐⭐⭐ | synchronized, Lock, Executor |
| Singleton Pattern | ⭐⭐⭐⭐ | Double-checked, enum |
| GC Algorithms | ⭐⭐⭐⭐ | G1, ZGC, generational |
| CompletableFuture | ⭐⭐⭐⭐ | Async, chaining, exception handling |

---

## 📊 Collections Complexity

| Operation | ArrayList | LinkedList | HashMap | TreeMap |
|-----------|-----------|------------|---------|---------|
| Get | O(1) | O(n) | O(1) | O(log n) |
| Add | O(1)* | O(1) | O(1)* | O(log n) |
| Remove | O(n) | O(1)** | O(1) | O(log n) |
| Search | O(n) | O(n) | O(1) | O(log n) |

*Amortized | **At known position

---

## 🔒 Thread Safety Quick Guide

| Need | Use |
|------|-----|
| Simple counter | AtomicInteger |
| Read-heavy cache | ConcurrentHashMap |
| Write-heavy | CopyOnWriteArrayList (if reads >> writes) |
| Producer-Consumer | BlockingQueue |
| One-time barrier | CountDownLatch |
| Reusable barrier | CyclicBarrier |
| Rate limiting | Semaphore |

---

## 🎯 Java 8+ Features Checklist

### Java 8
- [ ] Lambda expressions
- [ ] Functional interfaces (Predicate, Function, Consumer, Supplier)
- [ ] Stream API
- [ ] Optional
- [ ] Default methods in interfaces
- [ ] CompletableFuture

### Java 11
- [ ] var in lambda
- [ ] String methods (isBlank, lines, strip, repeat)
- [ ] Files.readString/writeString

### Java 17
- [ ] Sealed classes
- [ ] Records
- [ ] Pattern matching for instanceof
- [ ] Text blocks
- [ ] Switch expressions

### Java 21
- [ ] Virtual Threads
- [ ] Sequenced Collections
- [ ] Pattern matching for switch
- [ ] Record patterns

---

## 🧵 Concurrency Patterns

### Thread-safe Singleton
```java
public enum Singleton { INSTANCE; }
```

### Producer-Consumer
```java
BlockingQueue<T> queue = new ArrayBlockingQueue<>(100);
// Producer: queue.put(item);
// Consumer: queue.take();
```

### Read-Write Lock
```java
ReadWriteLock lock = new ReentrantReadWriteLock();
lock.readLock().lock();  // Multiple readers
lock.writeLock().lock(); // One writer
```

---

## 📐 Design Patterns at Glance

| Pattern | Use Case | Key Point |
|---------|----------|-----------|
| Singleton | Logger, Config | Use enum |
| Factory | Object creation | Delegate to subclasses |
| Builder | Complex objects | Fluent API |
| Adapter | Interface mismatch | Wrapper |
| Decorator | Add behavior | Wrap and enhance |
| Proxy | Lazy loading | Placeholder |
| Observer | Event notification | Publish-subscribe |
| Strategy | Algorithms | Interchangeable |
| Template | Algorithm skeleton | Override steps |

---

## ⚡ SOLID Quick Reference

| Letter | Principle | One-liner |
|--------|-----------|-----------|
| S | Single Responsibility | One reason to change |
| O | Open/Closed | Extend, don't modify |
| L | Liskov Substitution | Substitutable subclasses |
| I | Interface Segregation | Small, focused interfaces |
| D | Dependency Inversion | Depend on abstractions |

---

## 🗄️ JVM Memory Areas

| Area | Stores | Error |
|------|--------|-------|
| Stack | Method calls, locals | StackOverflowError |
| Heap | Objects, arrays | OutOfMemoryError |
| Metaspace | Class metadata | OutOfMemoryError |
| PC Register | Current instruction | - |
| Native Stack | JNI calls | - |

---

## 🔄 GC Algorithms

| GC | Best For | Java Version |
|----|----------|--------------|
| Serial | Small apps | All |
| Parallel | Throughput | Java 8 default |
| G1 | Balanced | Java 9+ default |
| ZGC | Low latency, large heap | Java 15+ |
| Shenandoah | Low latency | Java 12+ |

---

## 📝 Common Interview Questions

### HashMap
1. How does HashMap work internally?
2. What happens on collision?
3. Why is capacity power of 2?
4. Difference between HashMap and ConcurrentHashMap?

### Multithreading
1. Difference between synchronized and ReentrantLock?
2. What is deadlock? How to prevent?
3. Explain thread lifecycle
4. What is volatile?

### Java 8
1. Difference between map() and flatMap()?
2. What is a functional interface?
3. Explain Optional and its methods
4. What is method reference?

### Design Patterns
1. Implement thread-safe Singleton
2. When to use Factory vs Abstract Factory?
3. Explain Decorator pattern with example
4. What is the Observer pattern?

---

## 🎬 Last Minute Tips

1. **HashMap**: Know the internal working cold
2. **Streams**: Practice common operations
3. **Concurrency**: Understand synchronized, Lock, Executor
4. **Patterns**: Singleton, Factory, Builder, Observer, Strategy
5. **Java 8+**: Lambda, Streams, Optional, CompletableFuture
6. **JVM**: Memory areas, GC algorithms
7. **SOLID**: Know each principle with example

---

*Good luck with your interview! 🍀*
