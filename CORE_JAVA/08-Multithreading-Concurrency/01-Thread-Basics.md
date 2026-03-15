# Multithreading & Concurrency - Thread Basics

## Thread Lifecycle

```
        ┌─────────────┐
        │    NEW      │  Thread created, not started
        └──────┬──────┘
               │ start()
        ┌──────▼──────┐
        │  RUNNABLE   │  Ready to run, waiting for CPU
        └──────┬──────┘
               │ CPU picks thread
        ┌──────▼──────┐
        │  RUNNING    │  Executing code
        └──────┬──────┘
               │
    ┌──────────┼──────────┐
    │          │          │
┌───▼───┐ ┌────▼────┐ ┌───▼───┐
│BLOCKED│ │ WAITING │ │TIMED_ │
│       │ │         │ │WAITING│
└───┬───┘ └────┬────┘ └───┬───┘
    │          │          │
    └──────────┼──────────┘
               │ notify/sleep ends
        ┌──────▼──────┐
        │ TERMINATED  │  Thread finished
        └─────────────┘
```

---

## Creating Threads

### 1. Extending Thread
```java
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread running: " + Thread.currentThread().getName());
    }
}

MyThread thread = new MyThread();
thread.start();  // Don't call run() directly!
```

### 2. Implementing Runnable (Preferred)
```java
class MyTask implements Runnable {
    @Override
    public void run() {
        System.out.println("Task running");
    }
}

Thread thread = new Thread(new MyTask());
thread.start();

// With lambda
Thread thread = new Thread(() -> System.out.println("Lambda thread"));
thread.start();
```

### 3. Implementing Callable (Returns value)
```java
class MyCallable implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        return 42;
    }
}

ExecutorService executor = Executors.newFixedThreadPool(1);
Future<Integer> future = executor.submit(new MyCallable());
Integer result = future.get();  // Blocks until result available
```

---

## Thread Methods

```java
Thread thread = new Thread(task);

// Start thread
thread.start();

// Sleep (pause execution)
Thread.sleep(1000);  // 1 second

// Join (wait for thread to complete)
thread.join();           // Wait indefinitely
thread.join(5000);       // Wait max 5 seconds

// Interrupt
thread.interrupt();      // Set interrupt flag
if (Thread.interrupted()) { /* handle */ }

// Yield (hint to scheduler)
Thread.yield();          // Give up CPU time slice

// Get info
String name = thread.getName();
int priority = thread.getPriority();
boolean alive = thread.isAlive();
Thread.State state = thread.getState();

// Set properties
thread.setName("Worker-1");
thread.setPriority(Thread.MAX_PRIORITY);  // 1-10, default 5
thread.setDaemon(true);  // Daemon threads don't prevent JVM exit
```

---

## Runnable vs Callable

| Feature | Runnable | Callable |
|---------|----------|----------|
| Return | void | Type parameter T |
| Exception | Cannot throw checked | Can throw Exception |
| Method | run() | call() |
| Use case | Fire-and-forget | Need result |

---

## Thread Priority

```java
// Priority range: 1 (MIN) to 10 (MAX), default 5 (NORM)
thread.setPriority(Thread.MIN_PRIORITY);   // 1
thread.setPriority(Thread.NORM_PRIORITY);  // 5
thread.setPriority(Thread.MAX_PRIORITY);   // 10

// Note: Priority is just a hint to OS scheduler
// Not guaranteed to be respected
```

---

## Daemon Threads

```java
Thread daemon = new Thread(() -> {
    while (true) {
        // Background task
    }
});
daemon.setDaemon(true);  // Must set before start()
daemon.start();

// JVM exits when only daemon threads remain
// Examples: GC, finalizer
```

---

## Thread States

| State | Description |
|-------|-------------|
| NEW | Created but not started |
| RUNNABLE | Ready to run or running |
| BLOCKED | Waiting for monitor lock |
| WAITING | Waiting indefinitely (join, wait) |
| TIMED_WAITING | Waiting with timeout (sleep, timed wait) |
| TERMINATED | Completed execution |

---

## Synchronization Basics

### synchronized Keyword
```java
// Synchronized method
public synchronized void increment() {
    count++;  // Atomic operation
}

// Synchronized block
public void increment() {
    synchronized(this) {
        count++;
    }
}

// Static synchronized (class-level lock)
public static synchronized void staticMethod() { }

// Synchronized block on class
public void method() {
    synchronized(MyClass.class) {
        // Class-level lock
    }
}
```

### Object Lock vs Class Lock
```java
// Object lock - instance level
synchronized(this) { }           // Current instance
synchronized(lockObject) { }     // Specific object

// Class lock - class level
synchronized(MyClass.class) { }  // Class object
static synchronized void m() { } // Same as above
```

---

## wait(), notify(), notifyAll()

```java
class SharedResource {
    private boolean available = false;
    
    public synchronized void produce() throws InterruptedException {
        while (available) {
            wait();  // Release lock and wait
        }
        // Produce item
        available = true;
        notifyAll();  // Wake up waiting threads
    }
    
    public synchronized void consume() throws InterruptedException {
        while (!available) {
            wait();  // Release lock and wait
        }
        // Consume item
        available = false;
        notifyAll();  // Wake up waiting threads
    }
}
```

**Key Points:**
- Must be called from synchronized context
- `wait()` releases lock, `sleep()` doesn't
- `notify()` wakes one thread, `notifyAll()` wakes all
- Always use in while loop (spurious wakeup)

---

## Interview Q&A

**Q: Difference between start() and run()?**
A: start() creates new thread and calls run(). Calling run() directly executes in current thread.

**Q: Can we start a thread twice?**
A: No. Thread can only be started once. Throws IllegalThreadStateException.

**Q: What happens when thread throws exception?**
A: Thread terminates. UncaughtExceptionHandler can catch it.

**Q: Difference between sleep() and wait()?**
A: sleep() doesn't release lock, wait() does. wait() must be in synchronized block.

**Q: Why use while loop with wait()?**
A: To handle spurious wakeups and check condition after waking.

**Q: What is daemon thread?**
A: Background thread that doesn't prevent JVM exit. GC is daemon thread.

**Q: Can we make daemon thread after start()?**
A: No. Throws IllegalThreadStateException.

**Q: What is thread starvation?**
A: When a thread cannot gain CPU time due to other threads with higher priority.


---

## Common Traps

### ❌ Trap 1: Calling run() Instead of start()

**Why it's wrong**:
Calling run() directly executes in the current thread, not a new thread.

**Incorrect Code**:
```java
Thread thread = new Thread(() -> {
    System.out.println("Thread: " + Thread.currentThread().getName());
});
thread.run();  // ❌ Executes in main thread!
// Output: Thread: main
```

**Correct Code**:
```java
Thread thread = new Thread(() -> {
    System.out.println("Thread: " + Thread.currentThread().getName());
});
thread.start();  // ✅ Creates new thread
// Output: Thread: Thread-0
```

**Interview Tip**:
Always use start() to create a new thread. run() is just a regular method call.

---

### ❌ Trap 2: Not Handling InterruptedException Properly

**Why it's wrong**:
Swallowing InterruptedException or not restoring interrupt status breaks thread cancellation.

**Incorrect Code**:
```java
public void process() {
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        // ❌ Swallowing exception - interrupt status lost!
    }
}
```

**Correct Code**:
```java
// Option 1: Propagate exception
public void process() throws InterruptedException {
    Thread.sleep(1000);
}

// Option 2: Restore interrupt status
public void process() {
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();  // ✅ Restore interrupt status
        // Handle interruption
    }
}
```

**Interview Tip**:
Either propagate InterruptedException or restore interrupt status with Thread.currentThread().interrupt().

---

### ❌ Trap 3: Using notify() Instead of notifyAll()

**Why it's wrong**:
notify() wakes only one thread, which might not be the right one, causing deadlock.

**Incorrect Code**:
```java
class SharedResource {
    private int value = 0;
    
    public synchronized void produce() throws InterruptedException {
        while (value == 10) wait();
        value++;
        notify();  // ❌ Might wake another producer instead of consumer!
    }
    
    public synchronized void consume() throws InterruptedException {
        while (value == 0) wait();
        value--;
        notify();  // ❌ Might wake another consumer instead of producer!
    }
}
```

**Correct Code**:
```java
class SharedResource {
    private int value = 0;
    
    public synchronized void produce() throws InterruptedException {
        while (value == 10) wait();
        value++;
        notifyAll();  // ✅ Wake all waiting threads
    }
    
    public synchronized void consume() throws InterruptedException {
        while (value == 0) wait();
        value--;
        notifyAll();  // ✅ Wake all waiting threads
    }
}
```

**Interview Tip**:
Use notifyAll() unless you're certain only one type of thread is waiting. notify() can cause subtle deadlocks.

---

### ❌ Trap 4: Using if Instead of while with wait()

**Why it's wrong**:
Spurious wakeups can occur, causing thread to proceed without condition being met.

**Incorrect Code**:
```java
public synchronized void consume() throws InterruptedException {
    if (queue.isEmpty()) {  // ❌ if statement
        wait();
    }
    // Spurious wakeup might occur - queue still empty!
    Object item = queue.remove();  // NoSuchElementException!
}
```

**Correct Code**:
```java
public synchronized void consume() throws InterruptedException {
    while (queue.isEmpty()) {  // ✅ while loop
        wait();
    }
    // Condition rechecked after wakeup
    Object item = queue.remove();
}
```

**Interview Tip**:
Always use while loop with wait() to recheck condition after wakeup. This handles spurious wakeups and multiple consumers.

---

### ❌ Trap 5: Synchronizing on Mutable Object

**Why it's wrong**:
If the lock object changes, threads synchronize on different objects, losing synchronization.

**Incorrect Code**:
```java
class Counter {
    private Integer count = 0;  // ❌ Mutable wrapper
    
    public void increment() {
        synchronized(count) {  // ❌ Lock object changes!
            count++;  // Creates new Integer object
        }
    }
}
```

**Correct Code**:
```java
class Counter {
    private int count = 0;
    private final Object lock = new Object();  // ✅ Immutable lock
    
    public void increment() {
        synchronized(lock) {  // ✅ Lock never changes
            count++;
        }
    }
    
    // Or synchronize on this
    public synchronized void increment() {
        count++;
    }
}
```

**Interview Tip**:
Always synchronize on a final, immutable object. Never synchronize on Integer, String, or other mutable wrappers.

---

## Related Topics

- [Executor Framework](./02-Executor-Framework.md) - Thread pools and task execution
- [Locks & Atomic Classes](./03-Locks-Atomic.md) - Advanced synchronization mechanisms
- [Concurrency Problems](./04-Concurrency-Problems.md) - Deadlock, race conditions, thread safety
- [Collections Framework - ConcurrentHashMap](../04-Collections-Framework/02-Map-Implementations.md#concurrenthashmap-very-important) - Thread-safe collections
- [Java 8 Features - CompletableFuture](../06-Java-8-Features/03-Optional-CompletableFuture.md) - Asynchronous programming
- [Java Memory Model](../02-Java-Memory-Model/01-Memory-Areas.md) - Thread stacks and heap

---

*Last Updated: February 2026*
*Java Version: 8, 11, 17, 21*
