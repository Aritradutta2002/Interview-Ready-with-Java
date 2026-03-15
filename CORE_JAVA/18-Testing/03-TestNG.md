# TestNG

## Table of Contents
1. [What is TestNG?](#what-is-testng)
2. [TestNG vs JUnit](#testng-vs-junit)
3. [Basic Annotations](#basic-annotations)
4. [Test Configuration](#test-configuration)
5. [Data Providers](#data-providers)
6. [Test Groups](#test-groups)
7. [Parallel Execution](#parallel-execution)
8. [Interview Questions](#interview-questions)
9. [Common Traps](#common-traps)
10. [Related Topics](#related-topics)

---

## What is TestNG?

**TestNG** (Test Next Generation) is a testing framework inspired by JUnit but with more powerful features:
- Flexible test configuration
- Data-driven testing with `@DataProvider`
- Test grouping and dependencies
- Parallel test execution
- Built-in reporting

```
┌─────────────────────────────────────────────────────────┐
│                    TestNG Features                       │
├─────────────────────────────────────────────────────────┤
│  ✅ Annotations (@Test, @BeforeMethod, etc.)            │
│  ✅ Data Providers (parameterized tests)                │
│  ✅ Test Groups (smoke, regression, etc.)               │
│  ✅ Test Dependencies (test2 depends on test1)          │
│  ✅ Parallel Execution (faster test runs)               │
│  ✅ XML Configuration (testng.xml)                      │
└─────────────────────────────────────────────────────────┘
```

---

## TestNG vs JUnit

| Feature | JUnit 5 | TestNG |
|---------|---------|--------|
| Annotations | `@Test`, `@BeforeEach` | `@Test`, `@BeforeMethod` |
| Parameterized tests | `@ParameterizedTest` | `@DataProvider` |
| Test groups | Tags | `@Test(groups = "...")` |
| Test dependencies | ❌ | `@Test(dependsOnMethods = "...")` |
| Parallel execution | Limited | Built-in |
| Configuration | Java | XML (testng.xml) |
| Assertions | `Assertions.assertEquals()` | `Assert.assertEquals()` |

**When to use TestNG:**
- Need test dependencies
- Complex test grouping
- Parallel execution
- Integration testing

**When to use JUnit:**
- Unit testing
- Simpler setup
- Better IDE support


---

## Basic Annotations

### @Test

```java
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class CalculatorTest {
    @Test
    public void testAdd() {
        Calculator calc = new Calculator();
        assertEquals(calc.add(2, 3), 5);
    }
}
```

### @BeforeMethod and @AfterMethod

```java
import org.testng.annotations.*;

public class DatabaseTest {
    private Connection connection;
    
    @BeforeMethod
    public void setUp() {
        connection = DriverManager.getConnection("jdbc:h2:mem:test");
        System.out.println("Connection opened");
    }
    
    @Test
    public void testQuery() {
        // Use connection
    }
    
    @AfterMethod
    public void tearDown() throws SQLException {
        connection.close();
        System.out.println("Connection closed");
    }
}
```

### @BeforeClass and @AfterClass

```java
public class ExpensiveResourceTest {
    private static ExpensiveResource resource;
    
    @BeforeClass
    public static void setUpClass() {
        resource = new ExpensiveResource();  // Created once
        System.out.println("Resource initialized");
    }
    
    @Test
    public void test1() {
        // Use resource
    }
    
    @Test
    public void test2() {
        // Use same resource
    }
    
    @AfterClass
    public static void tearDownClass() {
        resource.close();
        System.out.println("Resource cleaned up");
    }
}
```

### Annotation Hierarchy

```
@BeforeSuite
  @BeforeTest
    @BeforeClass
      @BeforeMethod
        @Test
      @AfterMethod
    @AfterClass
  @AfterTest
@AfterSuite
```


---

## Test Configuration

### Test Attributes

```java
@Test(description = "Test addition of two numbers")
public void testAdd() {
    assertEquals(calculator.add(2, 3), 5);
}

@Test(enabled = false)
public void testDisabled() {
    // This test is skipped
}

@Test(timeOut = 1000)  // Fails if takes more than 1 second
public void testTimeout() {
    // Fast operation
}

@Test(expectedExceptions = ArithmeticException.class)
public void testException() {
    int result = 10 / 0;  // Throws ArithmeticException
}
```

### Test Dependencies

```java
@Test
public void testLogin() {
    // Login logic
}

@Test(dependsOnMethods = "testLogin")
public void testDashboard() {
    // Runs only if testLogin passes
}

@Test(dependsOnMethods = {"testLogin", "testDashboard"})
public void testLogout() {
    // Runs only if both dependencies pass
}
```

### Priority

```java
@Test(priority = 1)
public void testFirst() {
    // Runs first
}

@Test(priority = 2)
public void testSecond() {
    // Runs second
}

@Test(priority = 3)
public void testThird() {
    // Runs third
}
```


---

## Data Providers

### Basic Data Provider

```java
@DataProvider(name = "testData")
public Object[][] provideData() {
    return new Object[][] {
        {2, 3, 5},
        {5, 5, 10},
        {10, -5, 5}
    };
}

@Test(dataProvider = "testData")
public void testAdd(int a, int b, int expected) {
    assertEquals(calculator.add(a, b), expected);
}
```

### Data Provider in Different Class

```java
public class DataProviders {
    @DataProvider(name = "numbers")
    public static Object[][] provideNumbers() {
        return new Object[][] {{1}, {2}, {3}, {4}, {5}};
    }
}

public class CalculatorTest {
    @Test(dataProvider = "numbers", dataProviderClass = DataProviders.class)
    public void testIsPositive(int number) {
        assertTrue(number > 0);
    }
}
```

### Iterator Data Provider

```java
@DataProvider(name = "users")
public Iterator<Object[]> provideUsers() {
    List<Object[]> data = new ArrayList<>();
    data.add(new Object[]{"John", 30});
    data.add(new Object[]{"Jane", 25});
    data.add(new Object[]{"Bob", 35});
    return data.iterator();
}

@Test(dataProvider = "users")
public void testUser(String name, int age) {
    assertNotNull(name);
    assertTrue(age > 0);
}
```


---

## Test Groups

### Defining Groups

```java
@Test(groups = "smoke")
public void testLogin() {
    // Smoke test
}

@Test(groups = "regression")
public void testDashboard() {
    // Regression test
}

@Test(groups = {"smoke", "regression"})
public void testLogout() {
    // Both smoke and regression
}
```

### Running Groups via testng.xml

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Test Suite">
    <test name="Smoke Tests">
        <groups>
            <run>
                <include name="smoke"/>
            </run>
        </groups>
        <classes>
            <class name="com.example.CalculatorTest"/>
        </classes>
    </test>
</suite>
```

### Group Dependencies

```java
@Test(groups = "database")
public void testDatabaseConnection() {
    // Setup database
}

@Test(groups = "api", dependsOnGroups = "database")
public void testAPI() {
    // Runs after database group
}
```


---

## Parallel Execution

### Parallel Methods

```xml
<suite name="Parallel Suite" parallel="methods" thread-count="5">
    <test name="Test">
        <classes>
            <class name="com.example.CalculatorTest"/>
        </classes>
    </test>
</suite>
```

### Parallel Classes

```xml
<suite name="Parallel Suite" parallel="classes" thread-count="3">
    <test name="Test">
        <classes>
            <class name="com.example.CalculatorTest"/>
            <class name="com.example.UserServiceTest"/>
            <class name="com.example.OrderServiceTest"/>
        </classes>
    </test>
</suite>
```

### Parallel Tests

```xml
<suite name="Parallel Suite" parallel="tests" thread-count="2">
    <test name="Test 1">
        <classes>
            <class name="com.example.Test1"/>
        </classes>
    </test>
    <test name="Test 2">
        <classes>
            <class name="com.example.Test2"/>
        </classes>
    </test>
</suite>
```

**Parallel Options:**
- `parallel="methods"` - Run test methods in parallel
- `parallel="classes"` - Run test classes in parallel
- `parallel="tests"` - Run `<test>` tags in parallel
- `parallel="instances"` - Run instances in parallel


---

## Interview Questions

### Q1: What is TestNG?

**Difficulty**: Easy

**Answer**:
TestNG is a testing framework inspired by JUnit with additional features:
- Data providers for parameterized tests
- Test groups and dependencies
- Parallel execution
- XML configuration
- Better reporting

---

### Q2: Difference between TestNG and JUnit?

**Difficulty**: Medium

**Answer**:

| Feature | JUnit 5 | TestNG |
|---------|---------|--------|
| Test dependencies | ❌ | ✅ `dependsOnMethods` |
| Parameterized tests | `@ParameterizedTest` | `@DataProvider` |
| Test groups | Tags | `@Test(groups = "...")` |
| Parallel execution | Limited | Built-in |
| Configuration | Java | XML |

---

### Q3: What is @DataProvider?

**Difficulty**: Medium

**Answer**:
`@DataProvider` supplies test data to test methods:

```java
@DataProvider(name = "testData")
public Object[][] provideData() {
    return new Object[][] {{2, 3, 5}, {5, 5, 10}};
}

@Test(dataProvider = "testData")
public void testAdd(int a, int b, int expected) {
    assertEquals(calculator.add(a, b), expected);
}
```

---

### Q4: What are test groups?

**Difficulty**: Medium

**Answer**:
Test groups categorize tests for selective execution:

```java
@Test(groups = "smoke")
public void testLogin() { }

@Test(groups = "regression")
public void testDashboard() { }
```

Run specific groups via testng.xml:
```xml
<groups>
    <run>
        <include name="smoke"/>
    </run>
</groups>
```

---

### Q5: What is dependsOnMethods?

**Difficulty**: Medium

**Answer**:
`dependsOnMethods` creates test dependencies:

```java
@Test
public void testLogin() { }

@Test(dependsOnMethods = "testLogin")
public void testDashboard() {
    // Runs only if testLogin passes
}
```

---

### Q6: How to run tests in parallel?

**Difficulty**: Medium

**Answer**:
Configure parallel execution in testng.xml:

```xml
<suite name="Suite" parallel="methods" thread-count="5">
    <!-- Tests run in parallel -->
</suite>
```

Options: `methods`, `classes`, `tests`, `instances`

---

### Q7: What is @BeforeMethod vs @BeforeClass?

**Difficulty**: Easy

**Answer**:

| @BeforeMethod | @BeforeClass |
|---------------|--------------|
| Runs before each test method | Runs once before all tests in class |
| Instance method | Must be static |
| Use for test-specific setup | Use for expensive one-time setup |

---

### Q8: How to skip a test?

**Difficulty**: Easy

**Answer**:
Use `enabled = false`:

```java
@Test(enabled = false)
public void testDisabled() {
    // Skipped
}
```

Or throw `SkipException`:
```java
@Test
public void testConditional() {
    if (condition) {
        throw new SkipException("Skipping test");
    }
}
```

---

## Common Traps

### ❌ Trap 1: Circular Dependencies

**Why it's wrong**:
Circular dependencies cause infinite loops.

**Incorrect Code**:
```java
@Test(dependsOnMethods = "test2")
public void test1() { }

@Test(dependsOnMethods = "test1")
public void test2() { }

// ❌ Circular dependency!
```

**Correct Code**:
```java
@Test
public void test1() { }

@Test(dependsOnMethods = "test1")
public void test2() { }
```

**Interview Tip**:
Avoid circular dependencies. Use linear dependency chains.

---

### ❌ Trap 2: Thread Safety in Parallel Tests

**Why it's wrong**:
Shared state causes race conditions in parallel execution.

**Incorrect Code**:
```java
public class Test {
    private static int counter = 0;  // ❌ Shared state
    
    @Test
    public void test1() {
        counter++;  // Race condition
    }
    
    @Test
    public void test2() {
        counter++;  // Race condition
    }
}
```

**Correct Code**:
```java
public class Test {
    @Test
    public void test1() {
        int counter = 0;  // ✅ Local variable
        counter++;
    }
    
    @Test
    public void test2() {
        int counter = 0;  // ✅ Local variable
        counter++;
    }
}
```

**Interview Tip**:
Avoid shared state in parallel tests. Use thread-local or local variables.

---

### ❌ Trap 3: Forgetting @DataProvider Name

**Why it's wrong**:
Mismatched names cause tests to fail.

**Incorrect Code**:
```java
@DataProvider(name = "testData")
public Object[][] provideData() {
    return new Object[][] {{1, 2, 3}};
}

@Test(dataProvider = "wrongName")  // ❌ Name mismatch
public void test(int a, int b, int c) { }
```

**Correct Code**:
```java
@DataProvider(name = "testData")
public Object[][] provideData() {
    return new Object[][] {{1, 2, 3}};
}

@Test(dataProvider = "testData")  // ✅ Correct name
public void test(int a, int b, int c) { }
```

**Interview Tip**:
Ensure `@DataProvider` name matches `dataProvider` attribute.

---

## Related Topics

- [JUnit Basics](./01-JUnit-Basics.md) - JUnit testing framework
- [Mockito Mocking](./02-Mockito-Mocking.md) - Mocking with Mockito
- [Testing Best Practices](./04-Testing-Best-Practices.md) - TDD, test design
- [Multithreading](../08-Multithreading-Concurrency/01-Thread-Basics.md) - Thread safety in tests

---

*Last Updated: February 2026*  
*Java Version: 8, 11, 17, 21*  
*TestNG Version: 7.x*
