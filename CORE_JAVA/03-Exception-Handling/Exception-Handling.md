# Exception Handling - Complete Guide

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
