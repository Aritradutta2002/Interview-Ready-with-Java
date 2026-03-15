# Reflection & Annotations - Interview Guide

## Part 1: Reflection API

### What is Reflection?
Ability to inspect and manipulate classes, methods, fields, and constructors **at runtime**. Used heavily by frameworks (Spring, Hibernate, JUnit).

```java
// Get Class object
Class<?> clazz = String.class;                     // From class literal
Class<?> clazz = "Hello".getClass();               // From instance
Class<?> clazz = Class.forName("java.lang.String"); // From fully-qualified name
```

---

### Inspecting a Class

```java
Class<?> clazz = Employee.class;

// Class info
String name = clazz.getName();              // "com.example.Employee"
String simpleName = clazz.getSimpleName();  // "Employee"
int modifiers = clazz.getModifiers();       // public, abstract, final, etc.
boolean isFinal = Modifier.isFinal(modifiers);

// Superclass and interfaces
Class<?> parent = clazz.getSuperclass();         // Parent class
Class<?>[] interfaces = clazz.getInterfaces();   // Implemented interfaces

// Constructors
Constructor<?>[] constructors = clazz.getDeclaredConstructors();

// Fields
Field[] fields = clazz.getDeclaredFields();      // All fields (including private)
Field[] publicFields = clazz.getFields();        // Only public fields

// Methods
Method[] methods = clazz.getDeclaredMethods();   // All methods (including private)
Method[] publicMethods = clazz.getMethods();     // Public + inherited
```

---

### Accessing Private Members

```java
class Secret {
    private String password = "hidden";
    
    private void secretMethod() {
        System.out.println("Secret!");
    }
}

Secret obj = new Secret();
Class<?> clazz = obj.getClass();

// Access private field
Field field = clazz.getDeclaredField("password");
field.setAccessible(true);                     // Bypass access control
String value = (String) field.get(obj);        // "hidden"
field.set(obj, "newPassword");                 // Modify field

// Invoke private method
Method method = clazz.getDeclaredMethod("secretMethod");
method.setAccessible(true);
method.invoke(obj);                            // Prints "Secret!"
```

---

### Creating Objects via Reflection

```java
// Using Constructor
Class<?> clazz = Class.forName("com.example.Employee");
Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, int.class);
Object obj = constructor.newInstance("John", 30);

// Default constructor
Object obj = clazz.getDeclaredConstructor().newInstance();
```

---

### When is Reflection Used?
- **Spring Framework:** Dependency Injection, bean creation
- **Hibernate/JPA:** Entity mapping, field access
- **JUnit:** Test method discovery, `@Test` annotation
- **Serialization:** JSON/XML libraries (Jackson, Gson)
- **Proxy/AOP:** Dynamic proxies for cross-cutting concerns

### Drawbacks of Reflection
- **Performance:** Slower than direct method calls
- **Security:** Bypasses access control
- **Compile-time safety:** No compile-time checking
- **Maintainability:** Harder to debug and understand

---

## Part 2: Annotations

### What are Annotations?
Metadata that provides information to the compiler, runtime, or tools. Do not directly affect code execution.

```java
@Override            // Compiler checks if method actually overrides
@Deprecated          // Marks as deprecated
@SuppressWarnings    // Suppresses compiler warnings
@FunctionalInterface // Marks functional interface
```

---

### Built-in Annotations

```java
// @Override — Compile-time check for method overriding
class Child extends Parent {
    @Override
    void display() { }  // Compiler error if not actually overriding
}

// @Deprecated — Mark as deprecated with info
@Deprecated(since = "9", forRemoval = true)
public void oldMethod() { }

// @SuppressWarnings — Suppress specific warnings
@SuppressWarnings("unchecked")
List<String> list = (List<String>) rawList;

@SuppressWarnings({"unused", "deprecation"})
void method() { }

// @FunctionalInterface — Ensure exactly one abstract method
@FunctionalInterface
interface Calculator {
    int calculate(int a, int b);
}

// @SafeVarargs — Suppress heap pollution warning
@SafeVarargs
final <T> void process(T... items) { }
```

---

### Creating Custom Annotations

```java
// Define custom annotation
@Retention(RetentionPolicy.RUNTIME)    // When available
@Target(ElementType.METHOD)             // Where it can be applied
public @interface MyTest {
    String value() default "";          // Element with default
    int timeout() default 0;           // Another element
    String[] tags() default {};        // Array element
}

// Usage
class TestClass {
    @MyTest(value = "login test", timeout = 5000, tags = {"smoke", "regression"})
    void testLogin() { }
    
    @MyTest("simple test")  // Uses value() shorthand
    void testSimple() { }
}
```

---

### Retention Policies

```java
@Retention(RetentionPolicy.SOURCE)   // Discarded by compiler (e.g., @Override)
@Retention(RetentionPolicy.CLASS)    // In .class file, not at runtime (default)
@Retention(RetentionPolicy.RUNTIME)  // Available at runtime via reflection
```

| Policy | Compile | .class File | Runtime | Example |
|--------|---------|-------------|---------|---------|
| SOURCE | ✅ | ❌ | ❌ | @Override, @SuppressWarnings |
| CLASS | ✅ | ✅ | ❌ | Default if not specified |
| RUNTIME | ✅ | ✅ | ✅ | @Deprecated, Spring @Autowired |

---

### Target Types

```java
@Target(ElementType.TYPE)            // Class, interface, enum
@Target(ElementType.METHOD)          // Method
@Target(ElementType.FIELD)           // Field
@Target(ElementType.PARAMETER)       // Method parameter
@Target(ElementType.CONSTRUCTOR)     // Constructor
@Target(ElementType.LOCAL_VARIABLE)  // Local variable
@Target(ElementType.ANNOTATION_TYPE) // Other annotations
@Target(ElementType.TYPE_USE)        // Any type use (Java 8+)

// Multiple targets
@Target({ElementType.METHOD, ElementType.FIELD})
```

---

### Processing Annotations at Runtime

```java
// Read annotations via reflection
Class<?> clazz = TestClass.class;

for (Method method : clazz.getDeclaredMethods()) {
    if (method.isAnnotationPresent(MyTest.class)) {
        MyTest annotation = method.getAnnotation(MyTest.class);
        System.out.println("Test: " + annotation.value());
        System.out.println("Timeout: " + annotation.timeout());
        
        // Execute the test method
        method.invoke(clazz.getDeclaredConstructor().newInstance());
    }
}
```

---

### Real-World Annotation Examples

```java
// Spring Framework
@Component                           // Mark as Spring bean
@Autowired                           // Dependency injection
@RequestMapping("/api/users")        // URL mapping
@Transactional                       // Transaction management

// JPA/Hibernate
@Entity                              // Map to database table
@Table(name = "users")               // Table name
@Id @GeneratedValue                  // Primary key
@Column(name = "user_name")          // Column mapping

// JUnit 5
@Test                                // Test method
@BeforeEach                          // Run before each test
@DisplayName("User login test")      // Custom test name
@ParameterizedTest                   // Parameterized test
```

---

## Interview Q&A

**Q: What is Reflection used for?**
A: Inspecting and manipulating classes at runtime. Used by Spring (DI), Hibernate (ORM), JUnit (test discovery).

**Q: Is Reflection slow?**
A: Yes, slower than direct access. JIT can't optimize reflective calls. Use sparingly.

**Q: What is the difference between getFields() and getDeclaredFields()?**
A: `getFields()` returns public fields including inherited. `getDeclaredFields()` returns all fields of the class only (including private).

**Q: What are the three retention policies?**
A: SOURCE (compile only), CLASS (in bytecode), RUNTIME (available via reflection).

**Q: Can annotations have methods with parameters?**
A: Annotation elements look like methods but can only return: primitives, String, Class, enums, annotations, or arrays of these.

**Q: What is @Inherited annotation?**
A: When a meta-annotation marks a custom annotation, subclasses inherit the parent's annotation.

**Q: How does Spring use annotations?**
A: Component scanning finds `@Component`, `@Service`, etc. `@Autowired` triggers dependency injection via reflection.
