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
    Point p = new Point