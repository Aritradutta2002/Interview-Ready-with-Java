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
