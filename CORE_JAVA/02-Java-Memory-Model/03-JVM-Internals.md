# JVM Internals - ClassLoader, JIT, Bytecode

## JVM Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                         JVM                                  │
├─────────────────────────────────────────────────────────────┤
│  .java → [Compiler] → .class → [ClassLoader] → Runtime      │
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐       │
│  │ ClassLoader  │→ │ Method Area  │  │    Heap      │       │
│  └──────────────┘  └──────────────┘  └──────────────┘       │
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐       │
│  │   JIT        │  │   Stack      │  │  PC Register │       │
│  │  Compiler    │  └──────────────┘  └──────────────┘       │
│  └──────────────┘                                           │
└─────────────────────────────────────────────────────────────┘
```

---

## 1. ClassLoader Subsystem

### ClassLoader Hierarchy
```
        ┌─────────────────┐
        │ Bootstrap       │  (rt.jar, core classes)
        │ ClassLoader     │  - Loads java.* classes
        └────────┬────────┘
                 │
        ┌────────▼────────┐
        │ Extension       │  (jre/lib/ext)
        │ ClassLoader     │  - Loads extension classes
        └────────┬────────┘
                 │
        ┌────────▼────────┐
        │ Application     │  (CLASSPATH)
        │ ClassLoader     │  - Loads application classes
        └────────┬────────┘
                 │
        ┌────────▼────────┐
        │ Custom          │  (User-defined)
        │ ClassLoader     │
        └─────────────────┘
```

### ClassLoader Phases

**1. Loading**
- Read .class file
- Create Class object
- Store in method area

**2. Linking**
- **Verify:** Bytecode verification
- **Prepare:** Allocate memory for static variables
- **Resolve:** Replace symbolic references with direct

**3. Initialization**
- Execute static initializers
- Initialize static fields

### Delegation Model
```java
// Parent-first delegation
protected Class<?> loadClass(String name) {
    // 1. Check if already loaded
    if (findLoadedClass(name) != null) return loaded;
    
    // 2. Delegate to parent
    try {
        return parent.loadClass(name);
    } catch (ClassNotFoundException e) {}
    
    // 3. Load itself
    return findClass(name);
}
```

### Why Parent-First?
- Security: Core classes can't be replaced
- Avoid duplicate loading

### Custom ClassLoader
```java
class MyClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) {
        byte[] bytecode = loadBytesFromFile(name);
        return defineClass(name, bytecode, 0, bytecode.length);
    }
}
```

---

## 2. Bytecode

### What is Bytecode?
- Intermediate representation
- Platform-independent
- Instructions for JVM

### Bytecode Example
```java
// Java source
public int add(int a, int b) {
    return a + b;
}

// Bytecode (javap -c)
public int add(int, int);
  Code:
    0: iload_1      // Load int from local variable 1
    1: iload_2      // Load int from local variable 2
    2: iadd         // Add two integers
    3: ireturn      // Return int from method
```

### Common Bytecode Instructions
| Instruction | Description |
|-------------|-------------|
| `iconst_0` | Push int constant 0 |
| `iload_n` | Load int from local var n |
| `istore_n` | Store int to local var n |
| `iadd` | Add two ints |
| `invokevirtual` | Invoke instance method |
| `invokestatic` | Invoke static method |
| `invokespecial` | Invoke constructor/super |
| `invokeinterface` | Invoke interface method |
| `invokedynamic` | Dynamic method call (Java 7+) |
| `new` | Create new object |
| `getfield` | Get instance field |
| `putfield` | Set instance field |
| `getstatic` | Get static field |
| `putstatic` | Set static field |

---

## 3. JIT Compiler (Just-In-Time)

### Why JIT?
- Bytecode interpretation is slow
- JIT compiles hot code to native machine code

### JIT Compilation Process
```
Bytecode → Interpreter → Profiling → Hot Code? → JIT Compile → Native Code
                              ↓
                         Method calls counter
                         Loop iterations counter
```

### JIT Compilers

**1. C1 (Client Compiler)**
- Quick compilation
- Simple optimizations
- Good for startup time

**2. C2 (Server Compiler)**
- Aggressive optimizations
- Better performance
- Longer compilation time

**3. Tiered Compilation (Java 7+)**
```
Level 0: Interpreter
Level 1-3: C1 (simple optimizations)
Level 4: C2 (aggressive optimizations)
```

### JIT Optimizations

**1. Method Inlining**
```java
// Before
int result = add(a, b);
int add(int x, int y) { return x + y; }

// After inlining
int result = a + b;
```

**2. Escape Analysis**
```java
// Object doesn't escape method → allocate on stack
public void method() {
    Point p = new Point(1, 2);  // JIT may allocate on stack!
    int sum = p.x + p.y;
    // p doesn't escape → no heap allocation
}

// Object escapes → must be on heap
public Point createPoint() {
    Point p = new Point(1, 2);
    return p;  // Escapes via return → heap allocation
}
```

**Benefits of Escape Analysis:**
- Stack allocation (faster, no GC needed)
- Scalar replacement (replace object with individual fields)
- Lock elision (remove unnecessary synchronization)

**3. Loop Unrolling**
```java
// Before
for (int i = 0; i < 4; i++) {
    sum += arr[i];
}

// After unrolling
sum += arr[0];
sum += arr[1];
sum += arr[2];
sum += arr[3];
// Eliminates loop overhead
```

**4. Dead Code Elimination**
```java
public void method() {
    int x = computeExpensiveValue();
    // x is never used → JIT removes the computation entirely
}
```

**5. Null Check Elimination**
```java
// JIT can remove redundant null checks after profiling
if (obj != null) {
    obj.method();   // JIT knows obj is not null here
    obj.method2();  // No null check needed
}
```

### JIT Flags
```bash
# Print compilation info
-XX:+PrintCompilation

# Print inlining decisions
-XX:+PrintInlining

# Disable JIT (for debugging)
-Djava.compiler=NONE

# Set compilation threshold
-XX:CompileThreshold=10000  # Methods called 10000 times get compiled
```

---

## 4. Class Loading Scenarios

### When is a Class Loaded?
```java
// 1. New instance
new MyClass();

// 2. Static method call
MyClass.staticMethod();

// 3. Static field access (non-constant)
int x = MyClass.staticVar;

// 4. Reflection
Class.forName("com.example.MyClass");

// 5. Subclass loaded → parent loaded first
class Child extends Parent { }  // Parent loaded first

// NOT loaded:
// - Declaring a reference: MyClass obj;  (no loading)
// - Accessing constant: MyClass.CONSTANT  (inlined at compile time)
```

### ClassNotFoundException vs NoClassDefFoundError

| Exception | When | Cause |
|-----------|------|-------|
| ClassNotFoundException | Runtime | Class.forName() fails, class not in classpath |
| NoClassDefFoundError | Runtime | Class was available at compile time but not at runtime |

---

## 5. Java Compilation Pipeline

```
┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐
│  .java   │ →  │  javac   │ →  │  .class  │ →  │   JVM    │
│ (source) │    │(compiler)│    │(bytecode)│    │(execute) │
└──────────┘    └──────────┘    └──────────┘    └──────────┘
                                                     │
                                            ┌────────┴────────┐
                                            │                 │
                                      ┌─────▼─────┐   ┌──────▼─────┐
                                      │Interpreter│   │    JIT     │
                                      │ (line by  │   │ (compile   │
                                      │   line)   │   │ hot code)  │
                                      └───────────┘   └────────────┘
```

**javac** compiles `.java` → `.class` (bytecode)
**JVM** loads and executes bytecode via interpreter + JIT

---

## Interview Q&A

**Q: What is the difference between JDK, JRE, and JVM?**
A: JDK = JRE + dev tools (compiler, debugger). JRE = JVM + runtime libraries. JVM = Virtual machine that executes bytecode.

**Q: What is parent delegation model in ClassLoader?**
A: Child classloader delegates to parent first. Prevents core classes from being overridden.

**Q: What is the difference between C1 and C2 compilers?**
A: C1 = Client compiler (fast compile, simple optimizations). C2 = Server compiler (slow compile, aggressive optimizations).

**Q: What is Escape Analysis?**
A: JIT optimization that checks if an object escapes a method. If not, it can be stack-allocated or scalar-replaced.

**Q: What is method inlining?**
A: JIT replaces method call with the method body. Eliminates call overhead. Works best for small, frequently called methods.

**Q: What is invokedynamic?**
A: Bytecode instruction (Java 7+) for dynamic method dispatch. Used by lambdas, method references, and dynamic languages on JVM.

**Q: What does javap -c do?**
A: Disassembles .class file to show bytecode instructions. Useful for understanding what compiler generates.

**Q: How does JVM decide which code to JIT compile?**
A: Tracks method invocation count and loop back-edge count. When threshold reached (default ~10,000), JIT compiles the method.
