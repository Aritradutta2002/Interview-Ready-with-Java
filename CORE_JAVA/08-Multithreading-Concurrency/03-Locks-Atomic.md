# Multithreading & Concurrency - Locks & Atomics

## Lock Interface

```java
public interface Lock {
    void lock();
    void lockInterruptibly() throws InterruptedException;
    boolean tryLock();
    boolean tryLock(long time, TimeUnit unit);
    void unlock();
    Condition newCondition();
}
```

---

## ReentrantLock

```java
class Counter {
    private final Lock lock = new ReentrantLock();
    private int count = 0;
    
    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();  // Always in finally!
        }
    }
    
    // Try lock (non-blocking)
    public boolean tryIncrement() {
        if (lock.tryLock()) {
            try {
                count++;
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false;
    }
    
    // Try lock with timeout
    public boolean incrementWithTimeout() throws InterruptedException {
        if (lock.tryLock(1, TimeUnit.SECONDS)) {
            try {
                count++;
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false;
    }
}
```

### ReentrantLock vs synchronized

| Feature | synchronized | ReentrantLock |
|---------|--------------|---------------|
| Lock release | Automatic | Manual (finally) |
| Interruptible | No | Yes |
| Try lock | No | Yes |
| Fairness | No | Yes (optional) |
| Condition | Single | Multiple |
| Performance | Good | Better under contention |

### Fair vs Unfair Lock
```java
// Unfair (default) - may starve waiting threads
Lock unfairLock = new ReentrantLock();

// Fair - FIFO order
Lock fairLock = new ReentrantLock(true);
```

---

## ReadWriteLock

```java
class Cache {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<String, Object> cache = new HashMap<>();
    
    // Multiple readers can access simultaneously
    public Object get(String key) {
        lock.readLock().lock();
        try {
            return cache.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    // Only one writer, blocks all readers
    public void put(String key, Object value) {
        lock.writeLock().lock();
        try {
            cache.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
```

---

## StampedLock (Java 8+)

```java
class Point {
    private double x, y;
    private final StampedLock lock = new StampedLock();
    
    // Optimistic read (no locking)
    public double distanceFromOrigin() {
        long stamp = lock.tryOptimisticRead();
        double currentX = x, currentY = y;
        
        if (!lock.validate(stamp)) {
            // Optimistic read failed, get read lock
            stamp = lock.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
    
    // Write lock
    public void move(double deltaX, double deltaY) {
        long stamp = lock.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            lock.unlockWrite(stamp);
        }
    }
}
```

---

## Condition Variables

```java
class BoundedBuffer<T> {
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    
    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;
    
    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                notFull.await();  // Release lock, wait for signal
            }
            queue.add(item);
            notEmpty.signal();  // Wake up one consumer
        } finally {
            lock.unlock();
        }
    }
    
    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            T item = queue.poll();
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }
}
```

---

## Atomic Classes

```java
import java.util.concurrent.atomic.*;

// AtomicInteger
AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet();     // ++i
counter.getAndIncrement();     // i++
counter.decrementAndGet();     // --i
counter.getAndDecrement();     // i--
counter.addAndGet(5);          // i += 5
counter.get();                 // Read
counter.set(10);               // Write
counter.compareAndSet(10, 20); // CAS: if == 10, set to 20

// AtomicReference
AtomicReference<String> ref = new AtomicReference<>("initial");
ref.compareAndSet("initial", "updated");

// AtomicStampedReference (solve ABA problem)
AtomicStampedReference<Integer> stamped = new AtomicStampedReference<>(100, 0);
int[] stampHolder = new int[1];
Integer value = stamped.get(stampHolder);
int stamp = stampHolder[0];
stamped.compareAndSet(value, 200, stamp, stamp + 1);

// AtomicLong, AtomicBoolean, AtomicIntegerArray, etc.
```

### CAS (Compare-And-Swap)
```java
// Atomic operation without locking
public final int incrementAndGet() {
    return unsafe.getAndAddInt(this, valueOffset, 1) + 1;
}

// Underlying: CPU atomic instruction
// If current value == expected, set to new value
// Returns true if successful, false otherwise
```

---

## LongAdder & LongAccumulator (Java 8+)

```java
// Better than AtomicLong for high contention
LongAdder adder = new LongAdder();
adder.increment();
adder.add(5);
long sum = adder.sum();      // Not atomic
long sum = adder.sumThenReset();

// LongAccumulator for custom operations
LongAccumulator max = new LongAccumulator(Long::max, Long.MIN_VALUE);
max.accumulate(100);
max.accumulate(200);
long maxValue = max.get();  // 200
```

---

## volatile Keyword

```java
class Flag {
    // Guarantees visibility across threads
    // Does NOT guarantee atomicity for compound operations
    private volatile boolean running = true;
    
    // ✅ Good for: single read/write
    public void stop() { running = false; }
    public boolean isRunning() { return running; }
    
    // ❌ Bad for: compound operations
    private volatile int counter = 0;
    public void increment() { counter++; }  // NOT thread-safe!
    // Use AtomicInteger instead
}
```

### volatile vs Atomic vs synchronized

| Feature | volatile | Atomic | synchronized |
|---------|----------|--------|--------------|
| Visibility | ✅ | ✅ | ✅ |
| Atomicity | ❌ | ✅ | ✅ |
| Mutual Exclusion | ❌ | ❌ | ✅ |
| Performance | Best | Good | Slower |

---

## Interview Q&A

**Q: What is the difference between ReentrantLock and synchronized?**
A: ReentrantLock is interruptible, has tryLock, supports fairness, and multiple conditions.

**Q: What is ReadWriteLock?**
A: Allows multiple readers OR one writer. Improves concurrency for read-heavy workloads.

**Q: What is optimistic locking?**
A: Read without locking, validate at end. If validation fails, acquire lock and retry.

**Q: What is CAS?**
A: Compare-And-Swap. Atomic CPU instruction. If current == expected, set to new value.

**Q: What is the ABA problem?**
A: Value changes A→B→A. CAS succeeds but state changed. Solution: AtomicStampedReference.

**Q: When to use LongAdder vs AtomicLong?**
A: LongAdder for high contention writes. AtomicLong for low contention or when need atomic read.

**Q: What does volatile guarantee?**
A: Visibility (all threads see latest value) and ordering (prevents reordering). Not atomicity.

**Q: Why use Condition over wait/notify?**
A: Multiple conditions per lock, interruptible wait, timeout support.
