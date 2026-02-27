# Java 8 Features - Optional & CompletableFuture

## Optional

### What is Optional?
A container object which may or may not contain a non-null value.

```java
// Avoiding NullPointerException
// Old way
String name = person != null ? person.getName() : "Unknown";

// With Optional
String name = personOptional
    .map(Person::getName)
    .orElse("Unknown");
```

---

## Creating Optional

```java
// Empty Optional
Optional<String> empty = Optional.empty();

// Optional with value (throws NPE if null)
Optional<String> opt = Optional.of("Hello");

// Optional with nullable value
Optional<String> opt = Optional.ofNullable(possiblyNull);
```

---

## Optional Methods

### get() - Not Recommended
```java
Optional<String> opt = Optional.of("Hello");
String value = opt.get();  // Throws NoSuchElementException if empty
// Avoid: defeats the purpose of Optional
```

### orElse() - Default Value
```java
String value = opt.orElse("Default");
// Always evaluates default, even if value present
```

### orElseGet() - Lazy Default
```java
String value = opt.orElseGet(() -> computeDefault());
// Only computes default if Optional is empty
// Preferred over orElse() for expensive defaults
```

### orElseThrow() - Exception
```java
String value = opt.orElseThrow(() -> new RuntimeException("Not found"));
String value = opt.orElseThrow(MyException::new);
```

### ifPresent() - Consume if present
```java
opt.ifPresent(value -> System.out.println(value));
```

### ifPresentOrElse() (Java 9+)
```java
opt.ifPresentOrElse(
    value -> System.out.println("Value: " + value),
    () -> System.out.println("Empty")
);
```

### isPresent() & isEmpty() (Java 11+)
```java
if (opt.isPresent()) { /* has value */ }
if (opt.isEmpty()) { /* no value */ }
```

---

## Transforming Optional

### map()
```java
Optional<Integer> length = opt.map(String::length);
// If opt is empty, result is empty
```

### flatMap()
```java
// When mapping function returns Optional
Optional<String> name = personOptional
    .flatMap(Person::getNameOptional);

// Nested Optional becomes flat
Optional<Optional<String>> nested = personOptional.map(Person::getNameOptional);
Optional<String> flat = personOptional.flatMap(Person::getNameOptional);
```

### filter()
```java
Optional<String> longName = opt
    .filter(s -> s.length() > 3);
// Returns empty if filter fails
```

### stream() (Java 9+)
```java
// Convert Optional to Stream (0 or 1 elements)
Stream<String> stream = opt.stream();

// Useful for flatMap with Optionals
List<String> names = persons.stream()
    .flatMap(p -> p.getNameOptional().stream())
    .collect(Collectors.toList());
```

---

## Optional Best Practices

### ✅ DO
```java
// Return Optional from methods that may not return value
public Optional<User> findById(Long id) { }

// Use orElse/orElseGet for defaults
String name = opt.orElse("Unknown");
String name = opt.orElseGet(this::computeDefault);

// Use map/flatMap for transformations
opt.map(User::getName).orElse("Unknown");

// Use with streams
list.stream().flatMap(o -> o.stream()).collect(toList());
```

### ❌ DON'T
```java
// Don't use for fields
public class User {
    private Optional<String> name;  // ❌ Bad
}

// Don't use for method parameters
public void process(Optional<String> name) { }  // ❌ Bad

// Don't use get() directly
String value = opt.get();  // ❌ Risky

// Don't use orElse() with expensive computation
opt.orElse(expensiveComputation());  // ❌ Always called
```

---

## CompletableFuture

### What is CompletableFuture?
Implementation of Future that can be completed explicitly and supports functional callbacks.

```java
// Old Future (blocking)
Future<String> future = executor.submit(task);
String result = future.get();  // Blocks

// CompletableFuture (async, non-blocking)
CompletableFuture<String> future = CompletableFuture.supplyAsync(task);
future.thenAccept(result -> System.out.println(result));  // Callback
```

---

## Creating CompletableFuture

```java
// Completed already
CompletableFuture<String> completed = CompletableFuture.completedFuture("Done");

// Run async (no return value)
CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
    // Side effects
});

// Supply async (returns value)
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    return "Result";
});

// With custom executor
ExecutorService executor = Executors.newFixedThreadPool(10);
CompletableFuture<String> future = CompletableFuture.supplyAsync(task, executor);
```

---

## Chaining Operations

### thenApply() - Transform result
```java
CompletableFuture<Integer> lengthFuture = future
    .thenApply(String::length)
    .thenApply(len -> len * 2);
```

### thenAccept() - Consume result
```java
CompletableFuture<Void> consumed = future
    .thenAccept(result -> System.out.println(result));
```

### thenRun() - Run after completion
```java
CompletableFuture<Void> run = future
    .thenRun(() -> System.out.println("Completed"));
```

### thenCompose() - Chain dependent futures (flatMap)
```java
// When second future depends on first's result
CompletableFuture<User> userFuture = getUserId()
    .thenCompose(id -> getUserById(id));  // Returns CompletableFuture<User>
```

### thenCombine() - Combine independent futures
```java
// Combine results of two independent futures
CompletableFuture<String> combined = future1
    .thenCombine(future2, (result1, result2) -> result1 + result2);
```

---

## Handling Errors

### exceptionally()
```java
CompletableFuture<String> handled = future
    .exceptionally(ex -> {
        log.error("Error", ex);
        return "Default";
    });
```

### handle() - Handle both success and error
```java
CompletableFuture<String> handled = future
    .handle((result, ex) -> {
        if (ex != null) {
            return "Error: " + ex.getMessage();
        }
        return "Result: " + result;
    });
```

### whenComplete() - Side effects on completion
```java
CompletableFuture<String> completed = future
    .whenComplete((result, ex) -> {
        if (ex == null) {
            System.out.println("Success: " + result);
        } else {
            System.out.println("Error: " + ex);
        }
    });
```

---

## Combining Multiple Futures

### allOf() - Wait for all
```java
CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "A");
CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "B");
CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> "C");

CompletableFuture<Void> all = CompletableFuture.allOf(f1, f2, f3);
all.thenRun(() -> {
    // All completed
    String r1 = f1.join();
    String r2 = f2.join();
    String r3 = f3.join();
});
```

### anyOf() - First completed
```java
CompletableFuture<Object> first = CompletableFuture.anyOf(f1, f2, f3);
Object result = first.join();  // Result from first completed
```

---

## Getting Results

```java
// Blocking get (throws checked exceptions)
String result = future.get();
String result = future.get(5, TimeUnit.SECONDS);  // With timeout

// Blocking join (throws unchecked CompletionException)
String result = future.join();

// Get now or default
String result = future.getNow("Default");

// Complete manually
boolean completed = future.complete("Manual Result");
boolean cancelled = future.cancel(true);
```

---

## Async vs Sync Chaining

```java
// Default: same thread as previous stage
future.thenApply(fn)

// Async: common ForkJoinPool
future.thenApplyAsync(fn)

// Async: custom executor
future.thenApplyAsync(fn, executor)
```

---

## Interview Q&A

**Q: What is the difference between orElse() and orElseGet()?**
A: orElse() always evaluates default. orElseGet() only evaluates if Optional is empty (lazy).

**Q: Why shouldn't Optional be used for fields?**
A: Optional is not Serializable, adds overhead, and is designed for return types only.

**Q: What is the difference between thenApply() and thenCompose()?**
A: thenApply() wraps result in new CompletableFuture. thenCompose() flattens nested CompletableFutures (like map vs flatMap).

**Q: How to handle exceptions in CompletableFuture?**
A: Use exceptionally() for recovery, handle() for both success/error, whenComplete() for side effects.

**Q: What is the difference between allOf() and anyOf()?**
A: allOf() waits for all futures. anyOf() returns when first future completes.

**Q: What is the difference between get() and join()?**
A: get() throws checked exceptions. join() throws unchecked CompletionException.

**Q: How to make CompletableFuture blocking?**
A: Call get() or join() to block and get result.
