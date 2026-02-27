# Garbage Collection - Complete Guide

## What is Garbage Collection?

Automatic memory management that removes unreachable objects from heap.

---

## How GC Determines Unreachable Objects

### 1. Reference Counting
- Count references to object
- When count = 0, collect
- Issue: Circular references

### 2. Reachability Analysis (Java uses this)
- Start from GC Roots
- Mark reachable objects
- Sweep unreachable

**GC Roots:**
- Stack local variables
- Static variables
- JNI references
- Thread stacks
- Synchronized objects

---

## Generational Hypothesis

**Most objects die young.** → Divide heap by age.

```
┌────────────────────────────────────────────────────┐
│                    HEAP                            │
├────────────────────────────────────────────────────┤
│  YOUNG GENERATION          │    OLD GENERATION     │
│  ┌─────────┬─────┬─────┐   │                       │
│  │  EDEN   │ S0  │ S1  │   │     TENURED          │
│  │ (new)   │(Surv│(Surv│   │    (long-lived)      │
│  └─────────┴─────┴─────┘   │                       │
│  Minor GC (fast)           │   Major/Full GC       │
└────────────────────────────────────────────────────┘
```

**Object Lifecycle:**
1. Created in Eden
2. Survives GC → moved to Survivor (S0/S1)
3. Age threshold reached → moved to Old Gen

---

## GC Algorithms

### 1. Serial GC
```
- Single thread
- Stop-the-world
- Good for small apps
- Flag: -XX:+UseSerialGC
```

### 2. Parallel GC (Throughput)
```
- Multiple threads for GC
- Stop-the-world
- Default in Java 8
- Flag: -XX:+UseParallelGC
```

### 3. CMS (Concurrent Mark Sweep) - Deprecated in Java 9
```
- Concurrent with application
- Low pause times
- Fragmentation issues
- Flag: -XX:+UseConcMarkSweepGC
```

### 4. G1 (Garbage First) - Default in Java 9+
```
- Region-based heap
- Predictable pause times
- Concurrent marking
- Flag: -XX:+UseG1GC
```

**G1 Regions:**
```
┌────┬────┬────┬────┬────┐
│ E  │ S  │ O  │ E  │ O  │  E=Eden, S=Survivor, O=Old
├────┼────┼────┼────┼────┤
│ O  │ H  │ H  │ E  │ S  │  H=Humongous (large objects)
└────┴────┴────┴────┴────┘
```

### 5. ZGC (Z Garbage Collector) - Java 15+
```
- Sub-millisecond pauses
- Handles multi-terabyte heaps
- Concurrent phases
- Flag: -XX:+UseZGC
```

### 6. Shenandoah - Java 12+
```
- Low pause times
- Concurrent compaction
- Flag: -XX:+UseShenandoahGC
```

---

## GC Comparison Table

| GC | Pause Time | Throughput | Heap Size | Java Version |
|----|------------|------------|-----------|--------------|
| Serial | High | Low | < 100MB | All |
| Parallel | Medium | High | Any | All |
| CMS | Low | Medium | Any | Deprecated |
| G1 | Low | High | > 4GB | 9+ Default |
| ZGC | Very Low | High | > 100GB | 15+ |
| Shenandoah | Very Low | High | Any | 12+ |

---

## Types of GC Events

| Type | Scope | Trigger | Speed |
|------|-------|---------|-------|
| Minor GC | Young Gen | Eden full | Fast |
| Major GC | Old Gen | Old Gen full | Slower |
| Full GC | Entire Heap | System.gc(), allocation failure | Slowest |

---

## Object References

### 1. Strong Reference (Default)
```java
Object obj = new Object();  // Never collected while reachable
```

### 2. Soft Reference
```java
SoftReference<Object> soft = new SoftReference<>(new Object());
Object obj = soft.get();  // Collected only when memory needed
```
**Use case:** Caches

### 3. Weak Reference
```java
WeakReference<Object> weak = new WeakReference<>(new Object());
Object obj = weak.get();  // Collected in next GC
```
**Use case:** WeakHashMap, canonical maps

### 4. Phantom Reference
```java
PhantomReference<Object> phantom = new PhantomReference<>(new Object(), queue);
// get() always returns null
// Enqueued after collection, before finalization
```
**Use case:** Cleanup before deallocation

---

## finalize() Method

```java
@Override
protected void finalize() throws Throwable {
    try {
        // Cleanup resources
    } finally {
        super.finalize();
    }
}
```

**Problems:**
- Unpredictable when called
- Performance overhead
- Deprecated in Java 9
- Use try-with-resources or Cleaner instead

---

## Memory Leaks in Java

**Common Causes:**
```java
// 1. Static collections
static List<Object> cache = new ArrayList<>();  // Never GC'd

// 2. Unclosed resources
Connection conn = dataSource.getConnection();  // Not closed

// 3. Listener not removed
button.addActionListener(listener);  // Never removed

// 4. ThreadLocal
ThreadLocal<Object> local = new ThreadLocal<>();
local.set(obj);  // Thread pool reuses threads
```

**Detection:**
- Heap dump analysis (jmap, VisualVM)
- GC logs: `-Xlog:gc*`
- Memory profilers

---

## Important JVM Flags

```bash
# Heap size
-Xms512m          # Initial heap
-Xmx2g            # Max heap

# Young generation
-Xmn256m          # Young gen size
-XX:NewRatio=2    # Old:Young ratio

# GC Selection
-XX:+UseG1GC
-XX:+UseZGC

# GC Logging
-Xlog:gc*:file=gc.log

# Metaspace
-XX:MetaspaceSize=128m
-XX:MaxMetaspaceSize=256m
```

---

## Interview Q&A

**Q: When does an object become eligible for GC?**
A: When no live thread can reach it (unreachable from GC roots).

**Q: What is the difference between Minor and Major GC?**
A: Minor GC collects Young Gen (fast), Major GC collects Old Gen (slower).

**Q: Why is G1 better than CMS?**
A: G1 has predictable pause times, no fragmentation, concurrent compaction.

**Q: What triggers Full GC?**
A: System.gc(), Old Gen full, Metaspace full, allocation failure.

**Q: Can we force garbage collection?**
A: System.gc() is just a hint, not guaranteed.

**Q: What is the difference between Soft and Weak reference?**
A: Soft: collected when memory needed. Weak: collected in next GC cycle.
