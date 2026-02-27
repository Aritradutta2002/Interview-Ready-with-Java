# Multithreading & Concurrency - Problems & Synchronizers

## Common Concurrency Problems

### 1. Race Condition
```java
// ❌ Problem: Multiple threads accessing shared data
class Counter {
    private int count = 0;
    
    public void increment() {
        count++;  // NOT atomic: read, modify, write
    }
}

// ✅ Solution: Use atomic or synchronization
class Counter {
    private AtomicInteger count = new AtomicInteger(0);
    
    public void increment() {
        count.incrementAndGet();  // Atomic
    }
}
```

### 2. Deadlock
```java
// ❌ Problem: Circular wait
Thread 1: lock A → waiting for B
Thread 2: lock B → waiting for A

// Example
Object lockA = new Object();
Object lockB = new Object();

// Thread 1
synchronized(lockA) {
    Thread.sleep(100);
    synchronized(lockB) { }  // Deadlock!
}

// Thread 2
synchronized(lockB) {
    Thread.sleep(100);
    synchronized(lockA) { }  // Deadlock!
}

// ✅ Solutions:
// 1. Fixed ordering
synchronized(lockA) {
    synchronized(lockB) { }  // Always A then B
}

// 2. Try lock with timeout
if (lockA.tryLock(1, TimeUnit.SECONDS)) {
    try {
        if (lockB.tryLock(1, TimeUnit.SECONDS)) {
            try { /* work */ } finally { lockB.unlock(); }
        }
    } finally { lockA.unlock(); }
}

// 3. Use higher-level constructs (java.util.concurrent)
```

### 3. Livelock
```java
// Threads keep responding to each other but make no progress
// Like two people stepping aside for each other repeatedly

// ✅ Solution: Add randomness/backoff
public void process() {
    while (!tryLock()) {
        Thread.sleep(random.nextInt(100));  // Random backoff
    }
}
```

### 4. Starvation
```java
// Thread cannot get CPU time due to other threads

// Causes:
// - Unfair lock (default)
// - High priority threads
// - Synchronized blocks taking long

// ✅ Solution: Use fair lock
Lock fairLock = new ReentrantLock(true);
```

---

## Synchronizers

### 1. CountDownLatch
```java
// Wait for N threads to complete
CountDownLatch latch = new CountDownLatch(3);

// Worker threads
for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        doWork();
        latch.countDown();  // Decrement count
    }).start();
}

// Main thread waits
latch.await();  // Blocks until count = 0
System.out.println("All workers done");

// One-time use! Cannot reset
```

### 2. CyclicBarrier
```java
// Wait for N threads to reach a common point
CyclicBarrier barrier = new CyclicBarrier(3, () -> {
    System.out.println("All threads reached barrier");
});

for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        try {
            phase1();
            barrier.await();  // Wait for others
            phase2();
            barrier.await();  // Reusable!
        } catch (Exception e) { }
    }).start();
}

// Can be reused after threads are released
```

### CountDownLatch vs CyclicBarrier

| Feature | CountDownLatch | CyclicBarrier |
|---------|----------------|---------------|
| Reusable | No | Yes |
| Use case | Wait for events | Wait for threads |
| Action | countDown() / await() | await() only |
| Parties | Fixed count | Fixed parties |
| Callback | No | Optional Runnable |

### 3. Semaphore
```java
// Control access to limited resources
Semaphore semaphore = new Semaphore(3);  // 3 permits

// Acquire permit
semaphore.acquire();  // Blocks if no permits
try {
    accessResource();
} finally {
    semaphore.release();  // Return permit
}

// Try acquire (non-blocking)
if (semaphore.tryAcquire()) {
    try { accessResource(); }
    finally { semaphore.release(); }
}

// Fair semaphore
Semaphore fair = new Semaphore(3, true);
```

### 4. Exchanger
```java
// Exchange data between two threads
Exchanger<List<String>> exchanger = new Exchanger<>();

// Thread 1: Producer
List<String> buffer = new ArrayList<>();
fillBuffer(buffer);
buffer = exchanger.exchange(buffer);  // Exchange with Thread 2

// Thread 2: Consumer
List<String> buffer = new ArrayList<>();
buffer = exchanger.exchange(buffer);  // Receive from Thread 1
processBuffer(buffer);
```

### 5. Phaser
```java
// More flexible barrier
Phaser phaser = new Phaser(1);  // Register main thread

for (int i = 0; i < 5; i++) {
    phaser.register();  // Dynamic registration
    new Thread(() -> {
        phase1();
        phaser.arriveAndAwaitAdvance();  // Wait for all
        phase2();
        phaser.arriveAndDeregister();  // Leave
    }).start();
}

phaser.arriveAndAwaitAdvance();  // Main thread waits
phaser.arriveAndDeregister();
```

---

## BlockingQueue Implementations

```java
// ArrayBlockingQueue - Bounded, array-backed
BlockingQueue<String> queue = new ArrayBlockingQueue<>(100);

// Producer
queue.put("item");  // Blocks if full

// Consumer
String item = queue.take();  // Blocks if empty

// Non-blocking
boolean added = queue.offer("item", 1, TimeUnit.SECONDS);
String item = queue.poll(1, TimeUnit.SECONDS);

// LinkedBlockingQueue - Optionally bounded, linked list
BlockingQueue<String> queue = new LinkedBlockingQueue<>();

// PriorityBlockingQueue - Unbounded, priority order
BlockingQueue<Task> queue = new PriorityBlockingQueue<>();

// SynchronousQueue - Zero capacity, handoff
BlockingQueue<String> queue = new SynchronousQueue<>();
// put() waits for take()

// DelayQueue - Elements available after delay
class DelayedTask implements Delayed {
    private long startTime;
    
    public long getDelay(TimeUnit unit) {
        return unit.convert(startTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }
    
    public int compareTo(Delayed o) {
        return Long.compare(startTime, ((DelayedTask) o).startTime);
    }
}
```

---

## Producer-Consumer Pattern

```java
class ProducerConsumer {
    private final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
    
    public void start() {
        // Producer
        new Thread(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    queue.put(i);
                    System.out.println("Produced: " + i);
                }
            } catch (InterruptedException e) { }
        }).start();
        
        // Consumer
        new Thread(() -> {
            try {
                while (true) {
                    Integer item = queue.take();
                    System.out.println("Consumed: " + item);
                }
            } catch (InterruptedException e) { }
        }).start();
    }
}
```

---

## Thread-Safe Collections

```java
// ConcurrentHashMap - Thread-safe HashMap
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
map.put("key", 1);
map.putIfAbsent("key", 2);  // Atomic
map.compute("key", (k, v) -> v == null ? 1 : v + 1);

// CopyOnWriteArrayList - Thread-safe List
// Good for: Frequent reads, infrequent writes
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
list.add("item");  // Creates copy of underlying array

// CopyOnWriteArraySet
CopyOnWriteArraySet<String> set = new CopyOnWriteArraySet<>();

// ConcurrentLinkedQueue - Unbounded, thread-safe queue
ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
queue.offer("item");  // Non-blocking

// ConcurrentSkipListMap - Sorted, concurrent map
ConcurrentSkipListMap<String, Integer> sortedMap = new ConcurrentSkipListMap<>();
```

---

## Interview Q&A

**Q: What is deadlock and how to prevent it?**
A: Two or more threads waiting forever for each other. Prevent with: fixed ordering, timeout, deadlock detection.

**Q: Difference between CountDownLatch and CyclicBarrier?**
A: CountDownLatch is one-time, waits for events. CyclicBarrier is reusable, waits for threads.

**Q: What is Semaphore used for?**
A: Controlling access to limited resources (connection pools, rate limiting).

**Q: What is the difference between BlockingQueue's put() and offer()?**
A: put() blocks if full. offer() returns false immediately or with timeout.

**Q: When to use CopyOnWriteArrayList?**
A: When reads vastly outnumber writes. Writes are expensive (copy entire array).

**Q: What is race condition?**
A: Multiple threads access shared data, at least one writes, no synchronization.

**Q: How does ConcurrentHashMap achieve thread-safety?**
A: Segments/buckets locked independently. No whole-map lock. CAS for reads.
