# Mockito Mocking

## Table of Contents
1. [What is Mocking?](#what-is-mocking)
2. [Why Use Mockito?](#why-use-mockito)
3. [Creating Mocks](#creating-mocks)
4. [Stubbing Methods](#stubbing-methods)
5. [Verifying Interactions](#verifying-interactions)
6. [Argument Matchers](#argument-matchers)
7. [Spies](#spies)
8. [Argument Captors](#argument-captors)
9. [Interview Questions](#interview-questions)
10. [Common Traps](#common-traps)
11. [Related Topics](#related-topics)

---

## What is Mocking?

**Mocking** creates fake objects that simulate real dependencies, allowing you to:
- Test code in isolation
- Avoid external dependencies (databases, APIs, file systems)
- Control behavior of dependencies
- Verify interactions

```
Real Object                    Mock Object
┌──────────────┐              ┌──────────────┐
│ Database     │              │ Mock DB      │
│ - Real data  │              │ - Fake data  │
│ - Slow       │              │ - Fast       │
│ - External   │              │ - Controlled │
└──────────────┘              └──────────────┘
```

---

## Why Use Mockito?

**Mockito** is the most popular mocking framework for Java.

### Without Mocking

```java
class UserService {
    private UserRepository repository;
    
    public User getUser(Long id) {
        return repository.findById(id);  // Requires real database
    }
}

// Testing requires database setup
@Test
void testGetUser() {
    // Need to set up database, insert test data, etc.
}
```

### With Mockito

```java
@Test
void testGetUser() {
    UserRepository mockRepo = mock(UserRepository.class);
    when(mockRepo.findById(1L)).thenReturn(new User("John"));
    
    UserService service = new UserService(mockRepo);
    User user = service.getUser(1L);
    
    assertEquals("John", user.getName());
    // No database needed!
}
```

---

## Creating Mocks

### Method 1: mock() Method

```java
import static org.mockito.Mockito.*;

@Test
void testWithMock() {
    UserRepository mockRepo = mock(UserRepository.class);
    // mockRepo is a fake object
}
```

### Method 2: @Mock Annotation

```java
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository mockRepo;
    
    @Test
    void testGetUser() {
        // mockRepo is automatically initialized
    }
}
```

### Method 3: @InjectMocks

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository mockRepo;
    
    @InjectMocks
    private UserService userService;  // Mocks injected automatically
    
    @Test
    void testGetUser() {
        when(mockRepo.findById(1L)).thenReturn(new User("John"));
        User user = userService.getUser(1L);
        assertEquals("John", user.getName());
    }
}
```

---

## Stubbing Methods

### Basic Stubbing

```java
@Test
void testStubbing() {
    List<String> mockList = mock(List.class);
    
    // Stub method to return specific value
    when(mockList.get(0)).thenReturn("first");
    when(mockList.get(1)).thenReturn("second");
    
    assertEquals("first", mockList.get(0));
    assertEquals("second", mockList.get(1));
    assertNull(mockList.get(999));  // Unstubbed methods return null
}
```


### Stubbing with Exceptions

```java
@Test
void testException() {
    UserRepository mockRepo = mock(UserRepository.class);
    
    // Stub to throw exception
    when(mockRepo.findById(999L)).thenThrow(new UserNotFoundException());
    
    assertThrows(UserNotFoundException.class, () -> {
        mockRepo.findById(999L);
    });
}
```

### Stubbing Void Methods

```java
@Test
void testVoidMethod() {
    UserRepository mockRepo = mock(UserRepository.class);
    
    // Stub void method to throw exception
    doThrow(new RuntimeException()).when(mockRepo).deleteById(1L);
    
    assertThrows(RuntimeException.class, () -> {
        mockRepo.deleteById(1L);
    });
}
```

### Multiple Return Values

```java
@Test
void testMultipleReturns() {
    Iterator<String> mockIterator = mock(Iterator.class);
    
    // Return different values on successive calls
    when(mockIterator.next())
        .thenReturn("first")
        .thenReturn("second")
        .thenReturn("third");
    
    assertEquals("first", mockIterator.next());
    assertEquals("second", mockIterator.next());
    assertEquals("third", mockIterator.next());
}
```

### Stubbing with Callbacks

```java
@Test
void testCallback() {
    UserRepository mockRepo = mock(UserRepository.class);
    
    // Use thenAnswer for complex logic
    when(mockRepo.findById(anyLong())).thenAnswer(invocation -> {
        Long id = invocation.getArgument(0);
        return new User("User" + id);
    });
    
    assertEquals("User1", mockRepo.findById(1L).getName());
    assertEquals("User2", mockRepo.findById(2L).getName());
}
```


---

## Verifying Interactions

### Basic Verification

```java
@Test
void testVerification() {
    List<String> mockList = mock(List.class);
    
    mockList.add("one");
    mockList.add("two");
    mockList.clear();
    
    // Verify method was called
    verify(mockList).add("one");
    verify(mockList).add("two");
    verify(mockList).clear();
}
```

### Verification with Times

```java
@Test
void testVerifyTimes() {
    List<String> mockList = mock(List.class);
    
    mockList.add("one");
    mockList.add("one");
    mockList.add("two");
    
    verify(mockList, times(2)).add("one");
    verify(mockList, times(1)).add("two");
    verify(mockList, never()).add("three");
    verify(mockList, atLeast(1)).add("one");
    verify(mockList, atMost(3)).add(anyString());
}
```

### Verification Order

```java
@Test
void testVerifyOrder() {
    List<String> mockList = mock(List.class);
    
    mockList.add("first");
    mockList.add("second");
    
    InOrder inOrder = inOrder(mockList);
    inOrder.verify(mockList).add("first");
    inOrder.verify(mockList).add("second");
}
```

### Verify No Interactions

```java
@Test
void testNoInteractions() {
    List<String> mockList = mock(List.class);
    
    // Verify no methods were called
    verifyNoInteractions(mockList);
}
```


---

## Argument Matchers

### Common Matchers

```java
import static org.mockito.ArgumentMatchers.*;

@Test
void testMatchers() {
    UserRepository mockRepo = mock(UserRepository.class);
    
    // any() matchers
    when(mockRepo.findById(anyLong())).thenReturn(new User("John"));
    when(mockRepo.findByName(anyString())).thenReturn(new User("Jane"));
    when(mockRepo.findByAge(anyInt())).thenReturn(new User("Bob"));
    
    // Specific values
    when(mockRepo.findById(eq(1L))).thenReturn(new User("Alice"));
    
    // Null matchers
    when(mockRepo.findByEmail(isNull())).thenReturn(null);
    when(mockRepo.findByEmail(isNotNull())).thenReturn(new User("Test"));
}
```

### Custom Matchers

```java
@Test
void testCustomMatcher() {
    UserRepository mockRepo = mock(UserRepository.class);
    
    when(mockRepo.findByAge(intThat(age -> age > 18)))
        .thenReturn(new User("Adult"));
    
    User user = mockRepo.findByAge(25);
    assertEquals("Adult", user.getName());
}
```

### Combining Matchers

```java
@Test
void testCombiningMatchers() {
    UserService mockService = mock(UserService.class);
    
    // All arguments must use matchers if one does
    when(mockService.createUser(anyString(), eq(30), any(Address.class)))
        .thenReturn(new User("John", 30));
}
```


---

## Spies

**Spy** wraps a real object, allowing you to stub some methods while keeping others real.

```java
@Test
void testSpy() {
    List<String> realList = new ArrayList<>();
    List<String> spyList = spy(realList);
    
    // Real method called
    spyList.add("one");
    spyList.add("two");
    
    assertEquals(2, spyList.size());  // Real behavior
    
    // Stub specific method
    when(spyList.size()).thenReturn(100);
    
    assertEquals(100, spyList.size());  // Stubbed behavior
    assertEquals("one", spyList.get(0));  // Real behavior
}
```

### Spy vs Mock

| Mock | Spy |
|------|-----|
| Fake object | Wraps real object |
| All methods stubbed (return null/0/false) | Real methods called unless stubbed |
| Use for external dependencies | Use for partial mocking |

```java
@Test
void testSpyVsMock() {
    // Mock: all methods return default values
    List<String> mockList = mock(List.class);
    mockList.add("one");
    assertEquals(0, mockList.size());  // Returns 0 (not real behavior)
    
    // Spy: real methods called
    List<String> spyList = spy(new ArrayList<>());
    spyList.add("one");
    assertEquals(1, spyList.size());  // Returns 1 (real behavior)
}
```


---

## Argument Captors

**ArgumentCaptor** captures arguments passed to mocked methods for verification.

```java
@Test
void testArgumentCaptor() {
    UserRepository mockRepo = mock(UserRepository.class);
    UserService service = new UserService(mockRepo);
    
    service.createUser("John", 30);
    
    // Capture the argument
    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    verify(mockRepo).save(captor.capture());
    
    // Verify captured argument
    User capturedUser = captor.getValue();
    assertEquals("John", capturedUser.getName());
    assertEquals(30, capturedUser.getAge());
}
```

### Capturing Multiple Arguments

```java
@Test
void testMultipleCaptures() {
    UserRepository mockRepo = mock(UserRepository.class);
    
    mockRepo.save(new User("John"));
    mockRepo.save(new User("Jane"));
    mockRepo.save(new User("Bob"));
    
    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    verify(mockRepo, times(3)).save(captor.capture());
    
    List<User> capturedUsers = captor.getAllValues();
    assertEquals(3, capturedUsers.size());
    assertEquals("John", capturedUsers.get(0).getName());
    assertEquals("Jane", capturedUsers.get(1).getName());
    assertEquals("Bob", capturedUsers.get(2).getName());
}
```


---

## Interview Questions

### Q1: What is mocking?

**Difficulty**: Easy

**Answer**:
Mocking creates fake objects that simulate real dependencies. Benefits:
- Test code in isolation
- Avoid external dependencies (databases, APIs)
- Control behavior of dependencies
- Fast test execution

---

### Q2: What is Mockito?

**Difficulty**: Easy

**Answer**:
Mockito is the most popular mocking framework for Java. It provides:
- `mock()` to create fake objects
- `when().thenReturn()` to stub methods
- `verify()` to check interactions
- Argument matchers and captors

---

### Q3: Difference between mock() and spy()?

**Difficulty**: Medium

**Answer**:

| mock() | spy() |
|--------|-------|
| Creates fake object | Wraps real object |
| All methods stubbed | Real methods called unless stubbed |
| Use for external dependencies | Use for partial mocking |

```java
List<String> mock = mock(List.class);
mock.add("one");
assertEquals(0, mock.size());  // Stubbed (returns 0)

List<String> spy = spy(new ArrayList<>());
spy.add("one");
assertEquals(1, spy.size());  // Real behavior
```

---

### Q4: What is @InjectMocks?

**Difficulty**: Medium

**Answer**:
`@InjectMocks` automatically injects `@Mock` dependencies into the test subject:

```java
@Mock
private UserRepository mockRepo;

@InjectMocks
private UserService userService;  // mockRepo injected automatically
```

---

### Q5: How to verify a method was called?

**Difficulty**: Easy

**Answer**:
Use `verify()`:

```java
verify(mockRepo).save(any(User.class));
verify(mockRepo, times(2)).findById(1L);
verify(mockRepo, never()).deleteById(1L);
```

---

### Q6: What are argument matchers?

**Difficulty**: Medium

**Answer**:
Argument matchers match method arguments flexibly:

```java
when(mockRepo.findById(anyLong())).thenReturn(user);
when(mockRepo.findByName(eq("John"))).thenReturn(user);
when(mockRepo.findByAge(intThat(age -> age > 18))).thenReturn(user);
```

Common matchers: `any()`, `anyString()`, `anyInt()`, `eq()`, `isNull()`, `isNotNull()`

---

### Q7: What is ArgumentCaptor?

**Difficulty**: Medium

**Answer**:
`ArgumentCaptor` captures arguments passed to mocked methods:

```java
ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
verify(mockRepo).save(captor.capture());

User capturedUser = captor.getValue();
assertEquals("John", capturedUser.getName());
```

---

### Q8: How to stub void methods?

**Difficulty**: Medium

**Answer**:
Use `doThrow()`, `doNothing()`, `doAnswer()`:

```java
doThrow(new RuntimeException()).when(mockRepo).deleteById(1L);
doNothing().when(mockRepo).clear();
doAnswer(invocation -> {
    // Custom logic
    return null;
}).when(mockRepo).save(any());
```

---

### Q9: How to verify method call order?

**Difficulty**: Medium

**Answer**:
Use `InOrder`:

```java
InOrder inOrder = inOrder(mockList);
inOrder.verify(mockList).add("first");
inOrder.verify(mockList).add("second");
```

---

### Q10: When to use spy()?

**Difficulty**: Hard

**Answer**:
Use `spy()` when you need partial mocking:
- Most methods should use real behavior
- Only a few methods need stubbing
- Testing legacy code with complex dependencies

**Example**: Stub a slow method while keeping others real:
```java
UserService spy = spy(new UserService());
doReturn(cachedData).when(spy).fetchFromDatabase();  // Stub slow method
spy.processData();  // Real method
```


---

## Common Traps

### ❌ Trap 1: Mixing Matchers and Concrete Values

**Why it's wrong**:
If one argument uses a matcher, ALL arguments must use matchers.

**Incorrect Code**:
```java
when(mockService.createUser("John", anyInt()))  // ❌ Mixing
    .thenReturn(user);
```

**Correct Code**:
```java
when(mockService.createUser(eq("John"), anyInt()))  // ✅ All matchers
    .thenReturn(user);
```

**Interview Tip**:
Use `eq()` for concrete values when mixing with matchers.

---

### ❌ Trap 2: Stubbing Final Methods

**Why it's wrong**:
Mockito cannot mock final methods (by default).

**Incorrect Code**:
```java
class UserService {
    public final User getUser(Long id) {  // ← final
        return repository.findById(id);
    }
}

UserService mock = mock(UserService.class);
when(mock.getUser(1L)).thenReturn(user);  // ❌ Doesn't work
```

**Correct Code**:
```java
// Option 1: Remove final keyword
class UserService {
    public User getUser(Long id) {  // ✅ Not final
        return repository.findById(id);
    }
}

// Option 2: Enable inline mocking (mockito-inline)
// Add to pom.xml: mockito-inline dependency
```

**Interview Tip**:
Avoid `final` methods in classes you need to mock.

---

### ❌ Trap 3: Over-Mocking

**Why it's wrong**:
Mocking too much makes tests brittle and less valuable.

**Incorrect Code**:
```java
@Test
void testCalculator() {
    Calculator mock = mock(Calculator.class);
    when(mock.add(2, 3)).thenReturn(5);
    
    assertEquals(5, mock.add(2, 3));  // ❌ Testing the mock, not real code!
}
```

**Correct Code**:
```java
@Test
void testCalculator() {
    Calculator calc = new Calculator();  // ✅ Test real object
    assertEquals(5, calc.add(2, 3));
}
```

**Interview Tip**:
Only mock external dependencies (databases, APIs). Test real business logic.

---

## Related Topics

- [JUnit Basics](./01-JUnit-Basics.md) - Testing fundamentals
- [TestNG](./03-TestNG.md) - Alternative testing framework
- [Testing Best Practices](./04-Testing-Best-Practices.md) - TDD, test design
- [Design Patterns](../09-Design-Patterns/01-Design-Patterns.md) - Dependency Injection
- [Spring Testing](../../SPRING_SPRINGBOOT/07-Spring-Testing/) - Spring-specific testing

---

*Last Updated: February 2026*  
*Java Version: 8, 11, 17, 21*  
*Mockito Version: 5.x*
