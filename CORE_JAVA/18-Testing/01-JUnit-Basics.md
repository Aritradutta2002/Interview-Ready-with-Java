# JUnit Basics

## Table of Contents
1. [What is JUnit?](#what-is-junit)
2. [JUnit 4 vs JUnit 5](#junit-4-vs-junit-5)
3. [JUnit 5 Architecture](#junit-5-architecture)
4. [Basic Annotations](#basic-annotations)
5. [Assertions](#assertions)
6. [Test Lifecycle](#test-lifecycle)
7. [Parameterized Tests](#parameterized-tests)
8. [Nested Tests](#nested-tests)
9. [Interview Questions](#interview-questions)
10. [Common Traps](#common-traps)
11. [Related Topics](#related-topics)

---

## What is JUnit?

**JUnit** is the most popular testing framework for Java. It provides:
- Annotations to identify test methods
- Assertions to verify expected results
- Test runners to execute tests
- Test lifecycle management

```
┌─────────────────────────────────────────────────────────┐
│                    JUnit Workflow                        │
├─────────────────────────────────────────────────────────┤
│  1. Write test methods with @Test                       │
│  2. Use assertions to verify behavior                   │
│  3. Run tests with IDE or build tool                    │
│  4. View results (pass/fail)                            │
└─────────────────────────────────────────────────────────┘
```

---

## JUnit 4 vs JUnit 5

### JUnit 4 (Legacy)

```java
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

public class CalculatorTest {
    private Calculator calculator;
    
    @Before
    public void setUp() {
        calculator = new Calculator();
    }
    
    @Test
    public void testAdd() {
        assertEquals(5, calculator.add(2, 3));
    }
    
    @After
    public void tearDown() {
        calculator = null;
    }
}
```

### JUnit 5 (Jupiter)

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    private Calculator calculator;
    
    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }
    
    @Test
    void testAdd() {
        assertEquals(5, calculator.add(2, 3));
    }
    
    @AfterEach
    void tearDown() {
        calculator = null;
    }
}
```

### Key Differences

| Feature | JUnit 4 | JUnit 5 |
|---------|---------|---------|
| Package | `org.junit` | `org.junit.jupiter.api` |
| Test class | Must be `public` | Can be package-private |
| Test method | Must be `public` | Can be package-private |
| Annotations | `@Before`, `@After` | `@BeforeEach`, `@AfterEach` |
| Assertions | `Assert.assertEquals()` | `Assertions.assertEquals()` |
| Assumptions | Limited | Enhanced |
| Parameterized | Complex | Simple with `@ParameterizedTest` |
| Nested tests | Not supported | `@Nested` |
| Dynamic tests | Not supported | `@TestFactory` |

---

## JUnit 5 Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    JUnit 5 Platform                      │
├─────────────────────────────────────────────────────────┤
│  JUnit Platform: Foundation for launching test engines  │
│                                                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │ JUnit Jupiter│  │ JUnit Vintage│  │ Third-party  │ │
│  │ (JUnit 5)    │  │ (JUnit 3/4)  │  │ engines      │ │
│  └──────────────┘  └──────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────┘
```

**Components:**
- **JUnit Platform**: Launches tests, integrates with IDEs and build tools
- **JUnit Jupiter**: New programming model for JUnit 5
- **JUnit Vintage**: Runs JUnit 3 and 4 tests

---

## Basic Annotations

### @Test

```java
@Test
void shouldAddTwoNumbers() {
    Calculator calc = new Calculator();
    int result = calc.add(2, 3);
    assertEquals(5, result);
}
```

### @DisplayName

```java
@Test
@DisplayName("Adding 2 + 3 should return 5")
void testAdd() {
    assertEquals(5, new Calculator().add(2, 3));
}
```

### @Disabled

```java
@Test
@Disabled("Not implemented yet")
void testMultiply() {
    // Test is skipped
}
```

### @BeforeEach and @AfterEach

```java
class DatabaseTest {
    private Connection connection;
    
    @BeforeEach
    void setUp() {
        connection = DriverManager.getConnection("jdbc:h2:mem:test");
        System.out.println("Connection opened");
    }
    
    @Test
    void testQuery() {
        // Use connection
    }
    
    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
        System.out.println("Connection closed");
    }
}
```

### @BeforeAll and @AfterAll

```java
class ExpensiveResourceTest {
    private static ExpensiveResource resource;
    
    @BeforeAll
    static void setUpAll() {
        resource = new ExpensiveResource();  // Created once for all tests
        System.out.println("Resource initialized");
    }
    
    @Test
    void test1() {
        // Use resource
    }
    
    @Test
    void test2() {
        // Use same resource
    }
    
    @AfterAll
    static void tearDownAll() {
        resource.close();
        System.out.println("Resource cleaned up");
    }
}
```

**Execution Order:**
```
@BeforeAll (once)
  @BeforeEach
    @Test (test1)
  @AfterEach
  @BeforeEach
    @Test (test2)
  @AfterEach
@AfterAll (once)
```

---

## Assertions

### Basic Assertions

```java
import static org.junit.jupiter.api.Assertions.*;

@Test
void testAssertions() {
    // Equality
    assertEquals(5, 2 + 3);
    assertEquals("Hello", "Hello");
    
    // Boolean
    assertTrue(5 > 3);
    assertFalse(5 < 3);
    
    // Null checks
    assertNull(null);
    assertNotNull("Not null");
    
    // Same object
    String str = "test";
    assertSame(str, str);
    assertNotSame(new String("test"), new String("test"));
    
    // Array equality
    int[] expected = {1, 2, 3};
    int[] actual = {1, 2, 3};
    assertArrayEquals(expected, actual);
}
```

### Exception Assertions

```java
@Test
void testException() {
    // Assert exception is thrown
    assertThrows(ArithmeticException.class, () -> {
        int result = 10 / 0;
    });
    
    // Assert exception with message
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        new User("", -1);
    });
    assertEquals("Name cannot be empty", exception.getMessage());
}
```

### Timeout Assertions

```java
@Test
void testTimeout() {
    // Fails if execution takes more than 1 second
    assertTimeout(Duration.ofSeconds(1), () -> {
        Thread.sleep(500);  // Passes
    });
    
    // Preemptive timeout (aborts execution)
    assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
        Thread.sleep(50);  // Passes
    });
}
```

### Grouped Assertions

```java
@Test
void testGroupedAssertions() {
    User user = new User("John", 30);
    
    // All assertions executed, all failures reported
    assertAll("User properties",
        () -> assertEquals("John", user.getName()),
        () -> assertEquals(30, user.getAge()),
        () -> assertTrue(user.isActive())
    );
}
```

### Custom Messages

```java
@Test
void testWithMessage() {
    int result = calculator.add(2, 3);
    assertEquals(5, result, "2 + 3 should equal 5");
    
    // Lazy message evaluation (only if assertion fails)
    assertEquals(5, result, () -> "Expected 5 but got " + result);
}
```

---

## Test Lifecycle

### Lifecycle Annotations

```java
class LifecycleTest {
    @BeforeAll
    static void initAll() {
        System.out.println("Before all tests");
    }
    
    @BeforeEach
    void init() {
        System.out.println("Before each test");
    }
    
    @Test
    void test1() {
        System.out.println("Test 1");
    }
    
    @Test
    void test2() {
        System.out.println("Test 2");
    }
    
    @AfterEach
    void tearDown() {
        System.out.println("After each test");
    }
    
    @AfterAll
    static void tearDownAll() {
        System.out.println("After all tests");
    }
}
```

**Output:**
```
Before all tests
Before each test
Test 1
After each test
Before each test
Test 2
After each test
After all tests
```

---

## Parameterized Tests

### @ValueSource

```java
@ParameterizedTest
@ValueSource(ints = {1, 2, 3, 4, 5})
void testIsPositive(int number) {
    assertTrue(number > 0);
}

@ParameterizedTest
@ValueSource(strings = {"", "  ", "\t", "\n"})
void testIsBlank(String input) {
    assertTrue(input.isBlank());
}
```

### @CsvSource

```java
@ParameterizedTest
@CsvSource({
    "1, 2, 3",
    "5, 5, 10",
    "10, -5, 5"
})
void testAdd(int a, int b, int expected) {
    assertEquals(expected, calculator.add(a, b));
}
```

### @MethodSource

```java
@ParameterizedTest
@MethodSource("provideTestData")
void testWithMethodSource(String input, int expected) {
    assertEquals(expected, input.length());
}

static Stream<Arguments> provideTestData() {
    return Stream.of(
        Arguments.of("hello", 5),
        Arguments.of("world", 5),
        Arguments.of("test", 4)
    );
}
```

### @EnumSource

```java
enum Status { ACTIVE, INACTIVE, PENDING }

@ParameterizedTest
@EnumSource(Status.class)
void testEnumValues(Status status) {
    assertNotNull(status);
}
```

---

## Nested Tests

```java
@DisplayName("Calculator Tests")
class CalculatorTest {
    private Calculator calculator;
    
    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }
    
    @Nested
    @DisplayName("Addition Tests")
    class AdditionTests {
        @Test
        void testPositiveNumbers() {
            assertEquals(5, calculator.add(2, 3));
        }
        
        @Test
        void testNegativeNumbers() {
            assertEquals(-5, calculator.add(-2, -3));
        }
    }
    
    @Nested
    @DisplayName("Subtraction Tests")
    class SubtractionTests {
        @Test
        void testPositiveResult() {
            assertEquals(2, calculator.subtract(5, 3));
        }
        
        @Test
        void testNegativeResult() {
            assertEquals(-2, calculator.subtract(3, 5));
        }
    }
}
```

**Benefits:**
- Logical grouping of related tests
- Better test organization
- Hierarchical test structure

---

## Interview Questions

### Q1: What is JUnit?

**Difficulty**: Easy

**Answer**:
JUnit is a unit testing framework for Java. It provides annotations (`@Test`, `@BeforeEach`, etc.), assertions (`assertEquals`, `assertTrue`), and test runners to write and execute automated tests.

---

### Q2: Difference between @BeforeEach and @BeforeAll?

**Difficulty**: Easy

**Answer**:

| @BeforeEach | @BeforeAll |
|-------------|------------|
| Runs before each test method | Runs once before all tests |
| Instance method | Must be static |
| Use for test-specific setup | Use for expensive one-time setup |

```java
@BeforeEach
void setUp() {
    // Runs before each @Test
}

@BeforeAll
static void setUpAll() {
    // Runs once before all @Test methods
}
```

---

### Q3: What are assertions in JUnit?

**Difficulty**: Easy

**Answer**:
Assertions verify expected outcomes. Common assertions:
- `assertEquals(expected, actual)` - Check equality
- `assertTrue(condition)` - Check boolean
- `assertNull(object)` - Check null
- `assertThrows(Exception.class, () -> {...})` - Check exception

---

### Q4: How to test exceptions in JUnit 5?

**Difficulty**: Medium

**Answer**:
Use `assertThrows()`:

```java
@Test
void testException() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        new User("", -1);
    });
    assertEquals("Invalid input", exception.getMessage());
}
```

---

### Q5: What is @DisplayName?

**Difficulty**: Easy

**Answer**:
`@DisplayName` provides a custom name for tests in reports:

```java
@Test
@DisplayName("Adding 2 + 3 should return 5")
void testAdd() {
    assertEquals(5, calculator.add(2, 3));
}
```

---

### Q6: What are parameterized tests?

**Difficulty**: Medium

**Answer**:
Parameterized tests run the same test with different inputs:

```java
@ParameterizedTest
@ValueSource(ints = {1, 2, 3, 4, 5})
void testIsPositive(int number) {
    assertTrue(number > 0);
}
```

**Sources**: `@ValueSource`, `@CsvSource`, `@MethodSource`, `@EnumSource`

---

### Q7: What is @Nested?

**Difficulty**: Medium

**Answer**:
`@Nested` creates hierarchical test structures:

```java
class OuterTest {
    @Nested
    class InnerTest {
        @Test
        void test() { }
    }
}
```

**Benefits**: Logical grouping, better organization.

---

### Q8: Difference between assertEquals and assertSame?

**Difficulty**: Medium

**Answer**:

| assertEquals | assertSame |
|--------------|------------|
| Checks value equality (`.equals()`) | Checks reference equality (`==`) |
| `assertEquals("test", "test")` ✅ | `assertSame("test", "test")` ✅ (string pool) |
| `assertEquals(new String("test"), new String("test"))` ✅ | `assertSame(new String("test"), new String("test"))` ❌ |

---

### Q9: How to disable a test?

**Difficulty**: Easy

**Answer**:
Use `@Disabled`:

```java
@Test
@Disabled("Not implemented yet")
void testFeature() {
    // Test is skipped
}
```

---

### Q10: What is assertAll()?

**Difficulty**: Medium

**Answer**:
`assertAll()` groups multiple assertions. All assertions execute, and all failures are reported:

```java
assertAll("User validation",
    () -> assertEquals("John", user.getName()),
    () -> assertEquals(30, user.getAge()),
    () -> assertTrue(user.isActive())
);
```

Without `assertAll()`, the first failure stops execution.

---

## Common Traps

### ❌ Trap 1: Forgetting @Test Annotation

**Why it's wrong**:
Without `@Test`, the method is not recognized as a test and won't run.

**Incorrect Code**:
```java
class CalculatorTest {
    void testAdd() {  // ← Missing @Test
        assertEquals(5, calculator.add(2, 3));
    }
}

// Test is not executed!
```

**Correct Code**:
```java
class CalculatorTest {
    @Test  // ← Required
    void testAdd() {
        assertEquals(5, calculator.add(2, 3));
    }
}
```

**Interview Tip**:
Always annotate test methods with `@Test`.

---

### ❌ Trap 2: Using assertEquals for Floating-Point Comparison

**Why it's wrong**:
Floating-point arithmetic is imprecise. Direct equality checks fail.

**Incorrect Code**:
```java
@Test
void testDivision() {
    double result = 10.0 / 3.0;
    assertEquals(3.333333, result);  // ❌ Fails due to precision
}
```

**Correct Code**:
```java
@Test
void testDivision() {
    double result = 10.0 / 3.0;
    assertEquals(3.333333, result, 0.00001);  // ✅ Delta tolerance
}
```

**Interview Tip**:
Always use delta for floating-point assertions.

---

### ❌ Trap 3: Not Cleaning Up Resources

**Why it's wrong**:
Resources (connections, files) not closed in `@AfterEach` cause leaks.

**Incorrect Code**:
```java
class DatabaseTest {
    private Connection connection;
    
    @BeforeEach
    void setUp() {
        connection = DriverManager.getConnection("jdbc:h2:mem:test");
    }
    
    @Test
    void testQuery() {
        // Use connection
    }
    
    // ❌ No cleanup! Connection leaks
}
```

**Correct Code**:
```java
class DatabaseTest {
    private Connection connection;
    
    @BeforeEach
    void setUp() {
        connection = DriverManager.getConnection("jdbc:h2:mem:test");
    }
    
    @Test
    void testQuery() {
        // Use connection
    }
    
    @AfterEach
    void tearDown() throws SQLException {
        connection.close();  // ✅ Cleanup
    }
}
```

**Interview Tip**:
Always clean up resources in `@AfterEach` or use try-with-resources.

---

## Related Topics

- [Mockito Mocking](./02-Mockito-Mocking.md) - Mocking frameworks
- [TestNG](./03-TestNG.md) - Alternative testing framework
- [Testing Best Practices](./04-Testing-Best-Practices.md) - TDD, test design
- [Exception Handling](../03-Exception-Handling/01-Exception-Handling.md) - Testing exceptions
- [Multithreading](../08-Multithreading-Concurrency/01-Thread-Basics.md) - Testing concurrent code

---

*Last Updated: February 2026*  
*Java Version: 8, 11, 17, 21*  
*JUnit Version: 5.x (Jupiter)*
