# Java Memory Model - JVM Internals

## Memory Areas in JVM

```
┌─────────────────────────────────────────────────────────────┐
│                        JVM MEMORY                           │
├─────────────────┬─────────────────┬─────────────────────────┤
│   METHOD AREA   │     HEAP        │      STACK              │
│  (Class data)   │  (Objects)      │  (Method frames)        │
├─────────────────┼─────────────────┼─────────────────────────┤
│   PC REGISTER   │  NATIVE STACK   │                         │
│ (Thread PC)     │  (Native calls) │                         │
└─────────────────┴─────────────────┴─────────────────────────┘
```

---

## 1. Stack Memory

**Purpose:** Store method frames, local variables, method calls.

```java
public void method() {
    int x = 10;           // Stack (primitive)
    String s = "hello";   // Stack: reference, Heap: object
    Object obj = new Object();  // Stack: reference, Heap: object
}
```

**Characteristics:**
- Thread-safe (each thread has own stack)
- LIFO structure
- Auto cleanup when method ends
- Smaller than heap
- StackOverflowError when full

**Stack Frame contains:**
- Local variable array
- Operand stack
- Frame data (return address, exception table)

---

## 2. Heap Memory

**Purpose:** Store objects and their instance variables.

```java
Object obj = new Object();  // Created in heap
int[] arr = new int[1000];  // Array in heap
```

**Heap Structure (Generational GC):**
```
┌─────────────────────────────────────────┐
│              HEAP                       │
├──────────────┬──────────────────────────┤
│  Young Gen   │       Old Gen            │
│ (Eden + S0   │    (Tenured)             │
│     + S1)    │                          │
├──────────────┴──────────────────────────┤
│         String Pool (Java 7+)           │
└─────────────────────────────────────────┘
```

**Characteristics:**
- Shared across threads
- GC managed
- Larger than stack
- OutOfMemoryError when full

---

## 3. Method Area (Metaspace in Java 8+)

**Purpose:** Store class metadata, static variables, constant pool.

```java
class MyClass {
    static int count = 0;           // Method area
    static final int MAX = 100;     // Method area
    void method() { }
}
```

**Stored here:**
- Class structures
- Method bytecode
- Runtime constant pool
- Static variables
- Field/method references

**Metaspace vs PermGen:**
| Metaspace (Java 8+) | PermGen (Java 7-) |
|---------------------|-------------------|
| Native memory | JVM heap |
| Auto-growing | Fixed size |
| No OutOfMemoryError | Can run out |

---

## 4. Program Counter (PC) Register

**Purpose:** Store address of current instruction for each thread.

- Each thread has its own PC
- For native methods: PC is undefined

---

## 5. Native Method Stack

**Purpose:** Store native method calls (JNI).

---

## Stack vs Heap Comparison

| Feature | Stack | Heap |
|---------|-------|------|
| Stores | Primitives, references | Objects, arrays |
| Access | Fast | Slower |
| Thread | One per thread | Shared |
| Size | Fixed/Small | Dynamic/Large |
| GC | No | Yes |
| Error | StackOverflowError | OutOfMemoryError |
| Lifetime | Method scope | Until GC |

---

## String Pool (String Constant Pool)

**Location:** Heap (Java 7+), was in PermGen before.

```java
String s1 = "Hello";      // Pool
String s2 = "Hello";      // Reuses from pool
String s3 = new String("Hello");  // Heap (not pool)

System.out.println(s1 == s2);  // true
System.out.println(s1 == s3);  // false
System.out.println(s1 == s3.intern());  // true (adds to pool)
```

---

## Interview Q&A

**Q: Where are static variables stored?**
A: Method area (Metaspace in Java 8+).

**Q: Where is String pool located?**
A: Heap memory (Java 7+).

**Q: What happens when stack is full?**
A: StackOverflowError (usually from infinite recursion).

**Q: What happens when heap is full?**
A: OutOfMemoryError after GC cannot free space.

**Q: Is method area part of heap?**
A: Logically separate, but implementation varies.

**Q: Where are local object references stored?**
A: Reference on stack, object on heap.
