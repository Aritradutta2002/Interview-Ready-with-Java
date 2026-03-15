# Exception Handling - Complete Guide

## Table of Contents
1. [Exception Hierarchy](#exception-hierarchy)
2. [Checked vs Unchecked Exceptions](#checked-vs-unchecked-exceptions)
3. [Common Exceptions](#common-exceptions)
4. [Exception Handling Keywords](#exception-handling-keywords)
5. [Throw vs Throws](#throw-vs-throws)
6. [Custom Exception](#custom-exception)
7. [Exception Chaining](#exception-chaining)
8. [Best Practices](#best-practices)
9. [Exception Handling Patterns](#exception-handling-patterns)
10. [Interview Q&A](#interview-qa)
11. [Common Traps](#common-traps)
12. [Related Topics](#related-topics)

---

## Exception Hierarchy

```
                ┌─────────────┐
                │  Throwable  │
                └──────┬──────┘
           ┌───────────┴───────────┐
           │                       │
    ┌──────▼──────┐         ┌──────▼──────┐
    │   Error     │         │  Exception  │
    │ (Unchecked) │         │             │
    └─────────────┘         └──────┬──────┘
                                   │
                    ┌──────────────┴──────────────┐
                    │                             │
            ┌───────▼───────┐            ┌────────▼────────┐
            │   Checked     │            │    Unchecked    │
            │  Exception    │            │   (Runtime)     │
            │ IOException   │            │ RuntimeException│
            │ SQLException  │            │ NullPointerException
            └───────────────┘            └─────────────────┘
```

---

## Checked vs Unchecked Exceptions

| Feature | Checked | Unchecked |
|---------|---------|-----------|
| Compile-time | Must handle | Optional |
| Extends | Exception | RuntimeException |
| Examples | IOException, SQLException | NPE, ArrayIndexOutOfBounds |
| Use case | Recoverable | Programming errors |

---

## Common Exceptions

### Checked Exceptions
```java
IOException           // I/O failures
SQLException          // Database errors
ClassNotFoundException // Class not found
FileNotFoundException // File not found
InterruptedException  // Thread interrupted
```

### Unchecked Exceptions
```java
NullPointerException          // Null reference
ArrayIndexOutOfBoundsException // Invalid array index
ArithmeticException          // Division by zero
ClassCastException           // Invalid cast
IllegalArgumentException     // Invalid argument
IllegalStateException        // Invalid state
NumberFormatException        // Invalid number format
ConcurrentModificationException // Concurrent modification
```

### Errors (Not Exceptions)
```java
StackOverflowError      // Deep recursion
OutOfMemoryError        // Heap full
NoClassDefFoundError    // Class unavailable
VirtualMachineError     // JVM failure
```

---

## Exception Handling Keywords

```java
try {
    // Code that may throw exception
} catch (SpecificException e) {
    // Handle specific exception
} catch (Exception e) {
    // Handle general exception (last)
} finally {
    // Always executes (cleanup)
}
```

### try-with-resources (Java 7+)
```java
// Auto-closeable resources
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"));
     FileWriter fw = new FileWriter("output.txt")) {
    // Resources automatically closed
} // No need for finally block
```

### Multi-catch Block (Java 7+)
```java
try {
    // code
} catch (IOException | SQLException e) {
    // Handle multiple exceptions
    e.printStackTrace();
}
```

---

## Throw vs Throws

```java
// throw - Actually throw an exception
public void validate(int age) {
    if (age < 0) {
        throw new IllegalArgumentException("Age cannot be negative");
    }
}

// throws - Declare exception in method signature
public void readFile(String path) throws IOException {
    Files.readAllLines(Paths.get(path));
}
```

---

## Custom Exception

```java
// Checked custom exception
public class InsufficientBalanceException extends Exception {
    private double balance;
    private double amount;
    
    public InsufficientBalanceException(double balance, double amount) {
        super(String.format("Balance: %.2f, Required: %.2f", balance, amount));
        this.balance = balance;
        this.amount = amount;
    }
    
    public double getBalance() { return balance; }
    public double getAmount() { return amount; }
}

// Unchecked custom exception
public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException(String message) {
        super(message);
    }
}
```

---

## Exception Chaining

```java
try {
    // Low-level operation
} catch (IOException e) {
    // Wrap in higher-level exception
    throw new BusinessException("Failed to process file", e);
}

public class BusinessException extends Exception {
    public BusinessException(String message, Throwable cause) {
        super(message, cause);  // Preserves original stack trace
    }
}
```

---

## Best Practices

### 1. Catch Specific Exceptions First
```java
try {
    // code
} catch (FileNotFoundException e) {
    // Handle missing file
} catch (IOException e) {
    // Handle other I/O errors
} catch (Exception e) {
    // Last resort
}
```

### 2. Don't Catch Errors
```java
// ❌ Bad
catch (Error e) { }  // Errors are not meant to be caught

// ✅ Good - Let errors propagate
```

### 3. Don't Swallow Exceptions
```java
// ❌ Bad
catch (Exception e) { }  // Silent failure

// ✅ Good
catch (Exception e) {
    log.error("Failed to process", e);
    throw new RuntimeException(e);
}
```

### 4. Use try-with-resources
```java
// ❌ Bad
BufferedReader br = null;
try {
    br = new BufferedReader(new FileReader("file.txt"));
    // use br
} finally {
    if (br != null) br.close();
}

// ✅ Good
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    // use br
}
```

### 5. Preserve Stack Trace
```java
// ❌ Bad - Loses original trace
catch (Exception e) {
    throw new MyException("Error");
}

// ✅ Good - Preserves trace
catch (Exception e) {
    throw new MyException("Error", e);
}
```

---

## Exception Handling Patterns

### 1. Fail Fast
```java
public void withdraw(double amount) {
    if (amount <= 0) {
        throw new IllegalArgumentException("Amount must be positive");
    }
    if (amount > balance) {
        throw new InsufficientBalanceException(balance, amount);
    }
    balance -= amount;
}
```

### 2. Return Default Value
```java
public int parseOrDefault(String s, int defaultValue) {
    try {
        return Integer.parseInt(s);
    } catch (NumberFormatException e) {
        return defaultValue;
    }
}
```

### 3. Optional for Expected Failures
```java
public Optional<Integer> parse(String s) {
    try {
        return Optional.of(Integer.parseInt(s));
    } catch (NumberFormatException e) {
        return Optional.empty();
    }
}
```

---

## Interview Q&A

**Q: Can we have try without catch?**
A: Yes, but must have finally. `try { } finally { }`

**Q: Can finally block throw exception?**
A: Yes. If both try and finally throw, finally's exception suppresses try's.

**Q: What if return is in try and finally?**
A: Finally's return overrides try's return.

**Q: Can we rethrow exception?**
A: Yes. `catch (Exception e) { throw e; }`

**Q: What is suppressed exception?**
A: In try-with-resources, if try and close() both throw, close()'s exception is suppressed.

**Q: Difference between Error and Exception?**
A: Error - serious problems, shouldn't catch. Exception - recoverable conditions.

**Q: Is RuntimeException checked?**
A: No, it's unchecked. Compiler doesn't enforce handling.

**Q: Can we have empty catch block?**
A: Yes, but it's bad practice. At minimum, log the exception.

---

## Common Traps

### ❌ Trap 1: Swallowing Exceptions

**Why it's wrong**:
Silent failures make debugging impossible and hide critical errors.

**Incorrect Code**:
```java
try {
    processPayment(amount);
} catch (Exception e) {
    // Do nothing - exception swallowed
}
```

**Correct Code**:
```java
try {
    processPayment(amount);
} catch (PaymentException e) {
    logger.error("Payment processing failed", e);
    throw new BusinessException("Unable to process payment", e);
}
```

**Interview Tip**:
Always log exceptions at minimum. If you can't handle it, rethrow it or wrap it in a higher-level exception.

---

### ❌ Trap 2: Catching Exception Instead of Specific Types

**Why it's wrong**:
Catching generic Exception catches everything including RuntimeExceptions you didn't intend to handle.

**Incorrect Code**:
```java
try {
    String data = readFile(path);
    int value = Integer.parseInt(data);
} catch (Exception e) {
    // Catches IOException, NumberFormatException, and even NullPointerException
    return defaultValue;
}
```

**Correct Code**:
```java
try {
    String data = readFile(path);
    int value = Integer.parseInt(data);
} catch (IOException e) {
    logger.error("Failed to read file", e);
    throw new DataAccessException("Cannot read configuration", e);
} catch (NumberFormatException e) {
    logger.warn("Invalid number format, using default");
    return defaultValue;
}
```

**Interview Tip**:
Catch specific exceptions. Only catch Exception if you truly need to handle all exceptions at that level.

---

### ❌ Trap 3: Finally Block Overriding Return Value

**Why it's wrong**:
Return statement in finally block overrides return from try/catch, causing unexpected behavior.

**Incorrect Code**:
```java
public int getValue() {
    try {
        return 10;
    } finally {
        return 20;  // This overrides the try block's return
    }
}
// Returns 20, not 10!
```

**Correct Code**:
```java
public int getValue() {
    int result = 0;
    try {
        result = 10;
    } finally {
        // Cleanup only, no return
        logger.debug("Method completed");
    }
    return result;
}
```

**Interview Tip**:
Never use return statements in finally blocks. Use finally only for cleanup operations.

---

### ❌ Trap 4: Not Closing Resources in Pre-Java 7 Code

**Why it's wrong**:
If exception occurs before close(), resource leaks happen.

**Incorrect Code**:
```java
BufferedReader br = new BufferedReader(new FileReader("file.txt"));
try {
    String line = br.readLine();
    // process line
    br.close();  // Never reached if exception occurs
} catch (IOException e) {
    e.printStackTrace();
}
```

**Correct Code (Pre-Java 7)**:
```java
BufferedReader br = null;
try {
    br = new BufferedReader(new FileReader("file.txt"));
    String line = br.readLine();
    // process line
} catch (IOException e) {
    e.printStackTrace();
} finally {
    if (br != null) {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

**Correct Code (Java 7+)**:
```java
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    String line = br.readLine();
    // process line
} catch (IOException e) {
    e.printStackTrace();
}
```

**Interview Tip**:
Always use try-with-resources for AutoCloseable resources. It handles closing even if exceptions occur.

---

### ❌ Trap 5: Throwing Exception from Finally Block

**Why it's wrong**:
Exception thrown in finally suppresses the original exception from try block.

**Incorrect Code**:
```java
try {
    throw new IOException("Original exception");
} finally {
    throw new RuntimeException("Finally exception");
}
// Only RuntimeException is thrown, IOException is lost!
```

**Correct Code**:
```java
try {
    throw new IOException("Original exception");
} finally {
    try {
        // Cleanup that might throw
        resource.close();
    } catch (Exception e) {
        logger.error("Cleanup failed", e);
        // Don't rethrow
    }
}
```

**Interview Tip**:
Never throw exceptions from finally blocks. Catch and log any exceptions that occur during cleanup.

---

## Related Topics

- [I/O & NIO](../15-IO-NIO/01-IO-NIO.md) - try-with-resources for file operations
- [JDBC](../16-JDBC/01-JDBC-Basics.md) - SQLException handling and connection management
- [Multithreading & Concurrency](../08-Multithreading-Concurrency/01-Thread-Basics.md) - InterruptedException handling
- [Java 8 Features - Optional](../06-Java-8-Features/03-Optional-CompletableFuture.md) - Alternative to exceptions for expected failures
- [Best Practices](../10-SOLID-Principles/01-SOLID-Principles.md) - Exception handling in SOLID design

---

*Last Updated: February 2026*
*Java Version: 8, 11, 17, 21*
