# Testing Best Practices

## Table of Contents
1. [Test-Driven Development (TDD)](#test-driven-development-tdd)
2. [Test Design Principles](#test-design-principles)
3. [Test Naming Conventions](#test-naming-conventions)
4. [Test Organization](#test-organization)
5. [Code Coverage](#code-coverage)
6. [Testing Anti-Patterns](#testing-anti-patterns)
7. [Integration vs Unit Tests](#integration-vs-unit-tests)
8. [Interview Questions](#interview-questions)
9. [Common Traps](#common-traps)
10. [Related Topics](#related-topics)

---

## Test-Driven Development (TDD)

### TDD Cycle: Red-Green-Refactor

```
┌─────────────────────────────────────────────────────────┐
│                    TDD Cycle                             │
├─────────────────────────────────────────────────────────┤
│  1. RED: Write a failing test                           │
│  2. GREEN: Write minimal code to pass                   │
│  3. REFACTOR: Improve code without breaking tests       │
│  4. Repeat                                              │
└─────────────────────────────────────────────────────────┘
```

### Example: TDD in Action

**Step 1: RED - Write failing test**
```java
@Test
void testAdd() {
    Calculator calc = new Calculator();
    assertEquals(5, calc.add(2, 3));  // ❌ Fails (method doesn't exist)
}
```

**Step 2: GREEN - Minimal code to pass**
```java
class Calculator {
    public int add(int a, int b) {
        return a + b;  // ✅ Test passes
    }
}
```

**Step 3: REFACTOR - Improve code**
```java
class Calculator {
    public int add(int a, int b) {
        validateInputs(a, b);
        return a + b;  // ✅ Still passes
    }
    
    private void validateInputs(int a, int b) {
        // Validation logic
    }
}
```

### Benefits of TDD

- **Better design**: Forces you to think about API before implementation
- **Higher confidence**: Tests written first ensure coverage
- **Faster debugging**: Failing tests pinpoint issues immediately
- **Living documentation**: Tests document expected behavior


---

## Test Design Principles

### FIRST Principles

```
F - Fast: Tests should run quickly
I - Independent: Tests should not depend on each other
R - Repeatable: Same result every time
S - Self-Validating: Pass or fail, no manual verification
T - Timely: Written at the right time (TDD: before code)
```

### AAA Pattern (Arrange-Act-Assert)

```java
@Test
void testUserCreation() {
    // Arrange: Set up test data
    String name = "John";
    int age = 30;
    
    // Act: Execute the code under test
    User user = new User(name, age);
    
    // Assert: Verify the result
    assertEquals("John", user.getName());
    assertEquals(30, user.getAge());
}
```

### Given-When-Then (BDD Style)

```java
@Test
void testWithdrawal() {
    // Given: Initial state
    Account account = new Account(100.0);
    
    // When: Action
    account.withdraw(30.0);
    
    // Then: Expected outcome
    assertEquals(70.0, account.getBalance());
}
```

### One Assertion Per Test (Guideline)

```java
// ❌ Multiple unrelated assertions
@Test
void testUser() {
    User user = new User("John", 30);
    assertEquals("John", user.getName());
    assertEquals(30, user.getAge());
    assertTrue(user.isActive());
}

// ✅ Separate tests for each concern
@Test
void testUserName() {
    User user = new User("John", 30);
    assertEquals("John", user.getName());
}

@Test
void testUserAge() {
    User user = new User("John", 30);
    assertEquals(30, user.getAge());
}

@Test
void testUserIsActive() {
    User user = new User("John", 30);
    assertTrue(user.isActive());
}
```

**Exception**: Use `assertAll()` for related assertions:
```java
@Test
void testUser() {
    User user = new User("John", 30);
    assertAll("User properties",
        () -> assertEquals("John", user.getName()),
        () -> assertEquals(30, user.getAge()),
        () -> assertTrue(user.isActive())
    );
}
```


---

## Test Naming Conventions

### Pattern 1: methodName_stateUnderTest_expectedBehavior

```java
@Test
void add_positiveNumbers_returnsSum() {
    assertEquals(5, calculator.add(2, 3));
}

@Test
void withdraw_insufficientFunds_throwsException() {
    assertThrows(InsufficientFundsException.class, () -> {
        account.withdraw(1000);
    });
}
```

### Pattern 2: should_expectedBehavior_when_stateUnderTest

```java
@Test
void should_returnSum_when_addingPositiveNumbers() {
    assertEquals(5, calculator.add(2, 3));
}

@Test
void should_throwException_when_withdrawingWithInsufficientFunds() {
    assertThrows(InsufficientFundsException.class, () -> {
        account.withdraw(1000);
    });
}
```

### Pattern 3: given_when_then

```java
@Test
void given_accountWithBalance_when_withdraw_then_balanceDecreases() {
    Account account = new Account(100);
    account.withdraw(30);
    assertEquals(70, account.getBalance());
}
```

### Use @DisplayName for Readability

```java
@Test
@DisplayName("Adding two positive numbers should return their sum")
void testAdd() {
    assertEquals(5, calculator.add(2, 3));
}
```


---

## Test Organization

### Package Structure

```
src/
├── main/
│   └── java/
│       └── com/example/
│           ├── service/
│           │   └── UserService.java
│           └── repository/
│               └── UserRepository.java
└── test/
    └── java/
        └── com/example/
            ├── service/
            │   └── UserServiceTest.java
            └── repository/
                └── UserRepositoryTest.java
```

**Rule**: Mirror production package structure in test directory.

### Test Class Organization

```java
class UserServiceTest {
    // 1. Fields
    private UserService userService;
    private UserRepository mockRepository;
    
    // 2. Setup
    @BeforeEach
    void setUp() {
        mockRepository = mock(UserRepository.class);
        userService = new UserService(mockRepository);
    }
    
    // 3. Tests grouped by functionality
    @Nested
    @DisplayName("User Creation Tests")
    class UserCreationTests {
        @Test
        void testCreateUser() { }
        
        @Test
        void testCreateUserWithInvalidData() { }
    }
    
    @Nested
    @DisplayName("User Retrieval Tests")
    class UserRetrievalTests {
        @Test
        void testGetUser() { }
        
        @Test
        void testGetUserNotFound() { }
    }
    
    // 4. Teardown
    @AfterEach
    void tearDown() {
        // Cleanup
    }
}
```

### Test Data Builders

```java
class UserBuilder {
    private String name = "John";
    private int age = 30;
    private String email = "john@example.com";
    
    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }
    
    public UserBuilder withAge(int age) {
        this.age = age;
        return this;
    }
    
    public User build() {
        return new User(name, age, email);
    }
}

// Usage in tests
@Test
void testUser() {
    User user = new UserBuilder()
        .withName("Alice")
        .withAge(25)
        .build();
    
    assertEquals("Alice", user.getName());
}
```


---

## Code Coverage

### What is Code Coverage?

Percentage of code executed by tests:
- **Line Coverage**: % of lines executed
- **Branch Coverage**: % of if/else branches executed
- **Method Coverage**: % of methods called

### Tools

- **JaCoCo**: Most popular Java coverage tool
- **Cobertura**: Alternative coverage tool
- **IntelliJ IDEA**: Built-in coverage

### Coverage Goals

```
┌─────────────────────────────────────────────────────────┐
│                  Coverage Guidelines                     │
├─────────────────────────────────────────────────────────┤
│  Unit Tests: 80-90% coverage                            │
│  Integration Tests: 60-70% coverage                     │
│  Critical Code: 100% coverage (payment, security)       │
│  Getters/Setters: Can skip                              │
└─────────────────────────────────────────────────────────┘
```

### JaCoCo Example

```xml
<!-- pom.xml -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

**Run**: `mvn clean test jacoco:report`

### Coverage is Not Everything

```java
// ❌ 100% coverage but useless test
@Test
void testAdd() {
    calculator.add(2, 3);  // No assertion!
}

// ✅ Meaningful test
@Test
void testAdd() {
    assertEquals(5, calculator.add(2, 3));
}
```

**Remember**: High coverage doesn't guarantee quality tests.


---

## Testing Anti-Patterns

### 1. Test Interdependence

```java
// ❌ Tests depend on execution order
class BadTest {
    private static User user;
    
    @Test
    void test1_createUser() {
        user = new User("John");  // Creates shared state
    }
    
    @Test
    void test2_updateUser() {
        user.setAge(30);  // Depends on test1
    }
}

// ✅ Independent tests
class GoodTest {
    @Test
    void testCreateUser() {
        User user = new User("John");
        assertNotNull(user);
    }
    
    @Test
    void testUpdateUser() {
        User user = new User("John");  // Own setup
        user.setAge(30);
        assertEquals(30, user.getAge());
    }
}
```

### 2. Testing Implementation Details

```java
// ❌ Testing private methods
@Test
void testPrivateMethod() {
    // Using reflection to test private method
    Method method = MyClass.class.getDeclaredMethod("privateMethod");
    method.setAccessible(true);
    method.invoke(instance);
}

// ✅ Test public API
@Test
void testPublicBehavior() {
    MyClass instance = new MyClass();
    assertEquals(expected, instance.publicMethod());
}
```

### 3. Excessive Mocking

```java
// ❌ Mocking everything
@Test
void testCalculator() {
    Calculator mock = mock(Calculator.class);
    when(mock.add(2, 3)).thenReturn(5);
    assertEquals(5, mock.add(2, 3));  // Testing the mock!
}

// ✅ Test real objects
@Test
void testCalculator() {
    Calculator calc = new Calculator();
    assertEquals(5, calc.add(2, 3));  // Testing real code
}
```

### 4. Slow Tests

```java
// ❌ Slow test
@Test
void testSlowOperation() {
    Thread.sleep(5000);  // 5 seconds!
    // Test logic
}

// ✅ Fast test with mocking
@Test
void testFastOperation() {
    ExternalService mock = mock(ExternalService.class);
    when(mock.fetchData()).thenReturn(cachedData);
    // Test completes in milliseconds
}
```

### 5. Ignoring Test Failures

```java
// ❌ Disabling failing tests
@Test
@Disabled("Fails sometimes, will fix later")
void testUnstable() {
    // Flaky test
}

// ✅ Fix the test
@Test
void testStable() {
    // Properly fixed test
}
```


---

## Integration vs Unit Tests

### Unit Tests

**Characteristics:**
- Test single unit (class/method) in isolation
- Fast (milliseconds)
- No external dependencies
- Use mocks for dependencies

```java
@Test
void testUserService() {
    UserRepository mockRepo = mock(UserRepository.class);
    when(mockRepo.findById(1L)).thenReturn(new User("John"));
    
    UserService service = new UserService(mockRepo);
    User user = service.getUser(1L);
    
    assertEquals("John", user.getName());
}
```

### Integration Tests

**Characteristics:**
- Test multiple components together
- Slower (seconds)
- Use real dependencies (database, APIs)
- Test actual integration

```java
@SpringBootTest
@AutoConfigureTestDatabase
class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void testUserCreation() {
        User user = new User("John", 30);
        userService.createUser(user);
        
        User saved = userRepository.findById(user.getId()).orElseThrow();
        assertEquals("John", saved.getName());
    }
}
```

### Test Pyramid

```
        ┌─────────┐
        │   E2E   │  Few, slow, expensive
        ├─────────┤
        │ Integr. │  Some, moderate speed
        ├─────────┤
        │  Unit   │  Many, fast, cheap
        └─────────┘
```

**Guidelines:**
- **70% Unit Tests**: Fast, isolated, many
- **20% Integration Tests**: Moderate, test interactions
- **10% E2E Tests**: Slow, test full system


---

## Interview Questions

### Q1: What is TDD?

**Difficulty**: Easy

**Answer**:
Test-Driven Development (TDD) is a development approach where you write tests before code:
1. **RED**: Write a failing test
2. **GREEN**: Write minimal code to pass
3. **REFACTOR**: Improve code without breaking tests

**Benefits**: Better design, higher confidence, faster debugging.

---

### Q2: What are the FIRST principles?

**Difficulty**: Medium

**Answer**:
- **F**ast: Tests run quickly
- **I**ndependent: No dependencies between tests
- **R**epeatable: Same result every time
- **S**elf-Validating: Pass or fail automatically
- **T**imely: Written at the right time (TDD: before code)

---

### Q3: What is the AAA pattern?

**Difficulty**: Easy

**Answer**:
AAA (Arrange-Act-Assert) is a test structure pattern:
- **Arrange**: Set up test data
- **Act**: Execute code under test
- **Assert**: Verify result

```java
@Test
void test() {
    // Arrange
    Calculator calc = new Calculator();
    
    // Act
    int result = calc.add(2, 3);
    
    // Assert
    assertEquals(5, result);
}
```

---

### Q4: What is code coverage?

**Difficulty**: Easy

**Answer**:
Code coverage measures the percentage of code executed by tests:
- **Line Coverage**: % of lines executed
- **Branch Coverage**: % of branches executed
- **Method Coverage**: % of methods called

**Tools**: JaCoCo, Cobertura

**Goal**: 80-90% for unit tests, 100% for critical code.

---

### Q5: Difference between unit and integration tests?

**Difficulty**: Medium

**Answer**:

| Unit Tests | Integration Tests |
|------------|-------------------|
| Test single unit in isolation | Test multiple components together |
| Fast (milliseconds) | Slower (seconds) |
| Use mocks | Use real dependencies |
| Many tests | Fewer tests |

**Test Pyramid**: 70% unit, 20% integration, 10% E2E.

---

### Q6: What is the Test Pyramid?

**Difficulty**: Medium

**Answer**:
The Test Pyramid shows the ideal distribution of tests:
- **Base (70%)**: Unit tests - fast, many
- **Middle (20%)**: Integration tests - moderate
- **Top (10%)**: E2E tests - slow, few

**Rationale**: More fast tests, fewer slow tests.

---

### Q7: What are testing anti-patterns?

**Difficulty**: Hard

**Answer**:
Common testing mistakes:
1. **Test Interdependence**: Tests depend on each other
2. **Testing Implementation**: Testing private methods
3. **Excessive Mocking**: Mocking everything
4. **Slow Tests**: Tests take too long
5. **Ignoring Failures**: Disabling failing tests

---

### Q8: Should you test private methods?

**Difficulty**: Medium

**Answer**:
**No**. Test public API only. Private methods are implementation details.

**Rationale**:
- Private methods are tested indirectly via public methods
- Testing private methods makes tests brittle
- Refactoring breaks tests

**Exception**: If a private method is complex, consider extracting it to a separate class.

---

### Q9: What is a test builder pattern?

**Difficulty**: Medium

**Answer**:
Test builders create test data fluently:

```java
User user = new UserBuilder()
    .withName("Alice")
    .withAge(25)
    .build();
```

**Benefits**: Readable, reusable, flexible test data creation.

---

### Q10: How to handle flaky tests?

**Difficulty**: Hard

**Answer**:
Flaky tests pass/fail randomly. Causes:
- **Race conditions**: Use proper synchronization
- **External dependencies**: Mock or use test containers
- **Time-dependent logic**: Use fixed clocks
- **Shared state**: Ensure test isolation

**Solution**: Fix the root cause, don't ignore or retry.


---

## Common Traps

### ❌ Trap 1: Testing Implementation Instead of Behavior

**Why it's wrong**:
Tests break when refactoring internal implementation.

**Incorrect Code**:
```java
@Test
void testInternalState() {
    User user = new User("John", 30);
    // Testing internal field directly
    Field field = User.class.getDeclaredField("name");
    field.setAccessible(true);
    assertEquals("John", field.get(user));  // ❌ Brittle
}
```

**Correct Code**:
```java
@Test
void testPublicBehavior() {
    User user = new User("John", 30);
    assertEquals("John", user.getName());  // ✅ Test public API
}
```

**Interview Tip**:
Test behavior through public API, not internal implementation.

---

### ❌ Trap 2: Not Cleaning Up Resources

**Why it's wrong**:
Resource leaks cause tests to fail or slow down.

**Incorrect Code**:
```java
@Test
void testDatabase() {
    Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
    // Use connection
    // ❌ No cleanup!
}
```

**Correct Code**:
```java
@Test
void testDatabase() throws SQLException {
    try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:test")) {
        // Use connection
    }  // ✅ Auto-closed
}
```

**Interview Tip**:
Always clean up resources in `@AfterEach` or use try-with-resources.

---

### ❌ Trap 3: Ignoring Test Failures

**Why it's wrong**:
Disabled tests accumulate technical debt.

**Incorrect Code**:
```java
@Test
@Disabled("Fails sometimes, will fix later")
void testFlaky() {
    // ❌ Ignoring the problem
}
```

**Correct Code**:
```java
@Test
void testFixed() {
    // ✅ Fix the root cause (race condition, external dependency, etc.)
}
```

**Interview Tip**:
Never ignore test failures. Fix flaky tests immediately.

---

## Related Topics

- [JUnit Basics](./01-JUnit-Basics.md) - JUnit testing framework
- [Mockito Mocking](./02-Mockito-Mocking.md) - Mocking with Mockito
- [TestNG](./03-TestNG.md) - TestNG framework
- [Design Patterns](../09-Design-Patterns/01-Design-Patterns.md) - Builder pattern for test data
- [SOLID Principles](../10-SOLID-Principles/01-SOLID-Principles.md) - Testable code design

---

*Last Updated: February 2026*  
*Java Version: 8, 11, 17, 21*
