# Multithreading & Concurrency - Executor Framework

## Executor Framework Overview

```java
// Old way - manual thread management
Thread thread = new Thread(task);
thread.start();

// Executor framework - managed thread pools
ExecutorService executor = Executors.newFixedThreadPool(10);
executor.submit(task);
executor.shutdown();
```

---

## Executor Interfaces

```
Executor                    # execute(Runnable)
    │
    └── ExecutorService     # submit(), shutdown(), Future
            │
            └── ScheduledExecutorService  # schedule(), scheduleAtFixedRate()
```

---

## Thread Pool Types

### 1. Fixed Thread Pool
```java
ExecutorService executor = Executors.newFixedThreadPool(10);

// Fixed number of threads
// Unbounded queue (can cause memory issues)
// Good for: Known concurrent load

executor.submit(() -> System.out.println("Task"));
executor.shutdown();  // Graceful shutdown
```

### 2. Cached Thread Pool
```java
ExecutorService executor = Executors.newCachedThreadPool();

// Creates threads as needed, reuses idle threads
// Terminates threads idle for 60 seconds
// Good for: Many short-lived async tasks
// Risk: Can create unlimited threads
```

### 3. Single Thread Executor
```java
ExecutorService executor = Executors.newSingleThreadExecutor();

// Single worker thread, unbounded queue
// Tasks executed sequentially
// Good for: Sequential task processing
```

### 4. Scheduled Thread Pool
```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

// Run after delay
scheduler.schedule(() -> System.out.println("Delayed"), 5, TimeUnit.SECONDS);

// Run periodically (fixed delay between completions)
scheduler.scheduleWithFixedDelay(task, 1, 5, TimeUnit.SECONDS);

// Run periodically (fixed rate, regardless of completion)
scheduler.scheduleAtFixedRate(task, 1, 5, TimeUnit.SECONDS);
```

### 5. Work Stealing Pool (Java 8+)
```java
ExecutorService executor = Executors.newWorkStealingPool();

// Uses ForkJoinPool
// Threads steal work from other threads
// Good for: Recursive/parallel tasks
```

---

## ThreadPoolExecutor (Custom Pool)

```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    5,                      // Core pool size
    10,                     // Max pool size
    60, TimeUnit.SECONDS,   // Keep alive time for idle threads
    new ArrayBlockingQueue<>(100),  // Bounded queue
    Executors.defaultThreadFactory(),
    new ThreadPoolExecutor.CallerRunsPolicy()  // Rejection handler
);

// Rejection policies:
// AbortPolicy - throw RejectedExecutionException (default)
// CallerRunsPolicy - run in caller's thread
// DiscardPolicy - silently discard
// DiscardOldestPolicy - discard oldest, retry
```

### Pool Sizing Formula
```
For CPU-intensive tasks:
Threads = Number of CPU cores + 1

For I/O-intensive tasks:
Threads = Number of CPU cores * (1 + Wait time / Compute time)
```

---

## Future & Callable

```java
ExecutorService executor = Executors.newFixedThreadPool(5);

// Submit Callable (returns result)
Future<Integer> future = executor.submit(() -> {
    Thread.sleep(1000);
    return 42;
});

// Check if done
boolean done = future.isDone();
boolean cancelled = future.isCancelled();

// Get result (blocking)
Integer result = future.get();  // Blocks until done
Integer result = future.get(5, TimeUnit.SECONDS);  // With timeout

// Cancel
boolean cancelled = future.cancel(true);  // true = interrupt if running

// Multiple futures
List<Callable<String>> tasks = Arrays.asList(
    () -> "Task 1",
    () -> "Task 2",
    () -> "Task 3"
);

List<Future<String>> futures = executor.invokeAll(tasks);
String firstResult = executor.invokeAny(tasks);  // Returns first completed
```

---

## CompletableFuture (Advanced)

```java
// Async execution
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    return "Hello";
}, executor);

// Chain operations
CompletableFuture<Integer> result = future
    .thenApply(String::length)
    .thenApply(len -> len * 2);

// Combine futures
CompletableFuture<String> combined = future1
    .thenCombine(future2, (s1, s2) -> s1 + s2);

// Handle errors
CompletableFuture<String> handled = future
    .exceptionally(ex -> "Error: " + ex.getMessage())
    .handle((result, ex) -> ex != null ? "Default" : result);

// Wait for all
CompletableFuture.allOf(future1, future2, future3).join();

// First completed
CompletableFuture.anyOf(future1, future2).join();
```

---

## Shutdown Patterns

```java
// Graceful shutdown
executor.shutdown();  // No new tasks, complete existing

try {
    if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
        executor.shutdownNow();  // Force shutdown
    }
} catch (InterruptedException e) {
    executor.shutdownNow();
}

// Best practice with try-with-resources (Java 19+)
try (var executor = Executors.newFixedThreadPool(10)) {
    executor.submit(task);
} // Auto-closes
```

---

## ThreadFactory

```java
ThreadFactory factory = r -> {
    Thread t = new Thread(r);
    t.setName("Worker-" + t.getId());
    t.setDaemon(false);
    t.setPriority(Thread.NORM_PRIORITY);
    t.setUncaughtExceptionHandler((thread, ex) -> {
        System.err.println("Uncaught: " + ex);
    });
    return t;
};

ExecutorService executor = Executors.newFixedThreadPool(10, factory);
```

---

## Interview Q&A

**Q: Why use Executor over creating threads manually?**
A: Thread creation is expensive. Executor reuses threads, manages lifecycle, provides better control.

**Q: What happens when queue is full in ThreadPoolExecutor?**
A: Creates new threads up to max pool size. If max reached, rejection policy applies.

**Q: Difference between shutdown() and shutdownNow()?**
A: shutdown() waits for tasks to complete. shutdownNow() interrupts running tasks.

**Q: What is the difference between fixed and cached thread pool?**
A: Fixed has bounded threads, cached creates unlimited threads as needed.

**Q: How to handle RejectedExecutionException?**
A: Use appropriate rejection policy or ensure proper shutdown timing.

**Q: What is the difference between scheduleAtFixedRate and scheduleWithFixedDelay?**
A: Fixed rate runs at exact intervals. Fixed delay waits after previous completion.

**Q: Why use bounded queue in ThreadPoolExecutor?**
A: Prevents memory issues from unbounded task accumulation.
