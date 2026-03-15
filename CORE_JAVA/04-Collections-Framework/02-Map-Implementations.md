# Collections Framework - Maps

## Map Hierarchy

```
                    Map
            ┌────────┼────────┐
            ↓        ↓        ↓
        HashMap  LinkedHashMap TreeMap
            ↓
    ConcurrentHashMap
```

---

## HashMap Deep Dive (MOST IMPORTANT)

### Internal Structure (Java 8+)
```
┌─────────────────────────────────────────────────────┐
│                    HashMap                          │
├─────────────────────────────────────────────────────┤
│ Node<K,V>[] table;  // Array of buckets            │
│ int size;           // Number of entries            │
│ int threshold;      // Capacity * loadFactor        │
│ final float loadFactor;  // Default 0.75            │
└─────────────────────────────────────────────────────┘

Bucket structure:
┌──────┐    ┌──────┐    ┌──────┐
│ Node │ →  │ Node │ →  │ Node │ → null  (Linked List)
└──────┘    └──────┘    └──────┘
    ↓ (when > 8 nodes)
┌──────┐
│TreeNode│ (Red-Black Tree for O(log n))
└──────┘
```

### How put() Works
```java
public V put(K key, V value) {
    // 1. Calculate hash
    int hash = hash(key);  // hash = key.hashCode() ^ (key.hashCode() >>> 16)
    
    // 2. Calculate bucket index
    int index = (n - 1) & hash;  // n = table length
    
    // 3. If bucket empty, create node
    if (table[index] == null) {
        table[index] = new Node<>(hash, key, value, null);
    }
    // 4. If bucket not empty, traverse chain
    else {
        Node<K,V> node = table[index];
        while (node != null) {
            // Check if key exists (hash + equals)
            if (node.hash == hash && 
                (node.key == key || key.equals(node.key))) {
                return node.setValue(value);  // Update existing
            }
            node = node.next;
        }
        // Add new node at head
        table[index] = new Node<>(hash, key, value, table[index]);
    }
    
    // 5. Check if resize needed
    if (++size > threshold) resize();
}
```

### Hash Function
```java
static final int hash(Object key) {
    int h;
    // XOR with upper 16 bits to spread entropy
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

### Why XOR with >>> 16?
- Spreads high bits to lower bits
- Reduces collisions when table is small
- Better distribution

### Rehashing/Resize
```java
void resize() {
    // 1. Double the capacity
    int newCap = oldCap << 1;  // 2x
    
    // 2. Create new array
    Node<K,V>[] newTab = new Node[newCap];
    
    // 3. Rehash all entries
    for (Node<K,V> node : table) {
        while (node != null) {
            // Recalculate index: (newCap - 1) & hash
            // Either same index or index + oldCap
        }
    }
}
```

### Treeification (Java 8+)
```java
// When chain length > 8 and table size >= 64
// Convert linked list to Red-Black Tree
if (binCount >= TREEIFY_THRESHOLD - 1) {
    treeifyBin(tab, hash);
}
// Tree back to linked list when nodes < 6
```

### Load Factor
- Default: 0.75
- Threshold = capacity × loadFactor
- When size > threshold → resize
- Trade-off: Memory vs Collision

---

## HashMap Key Points

| Aspect | Detail |
|--------|--------|
| Initial Capacity | 16 |
| Load Factor | 0.75 |
| Resize | 2x capacity |
| Null Keys | 1 null key allowed |
| Null Values | Multiple null values allowed |
| Ordering | No guarantee |
| Thread Safety | Not thread-safe |

---

## LinkedHashMap

### Internal Structure
```java
// Extends HashMap with a doubly linked list running through entries
public class LinkedHashMap<K,V> extends HashMap<K,V> {
    // Each entry has before/after pointers
    static class Entry<K,V> extends HashMap.Node<K,V> {
        Entry<K,V> before, after;
    }
    
    transient Entry<K,V> head;  // Oldest entry
    transient Entry<K,V> tail;  // Newest entry
    final boolean accessOrder;  // false=insertion, true=access order
}
```

### Key Features
```java
// Default: Insertion order
LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
map.put("C", 3);
map.put("A", 1);
map.put("B", 2);
// Iteration: C=3, A=1, B=2 (insertion order)

// Access order (useful for LRU cache)
LinkedHashMap<String, Integer> accessMap = new LinkedHashMap<>(16, 0.75f, true);
accessMap.put("A", 1);
accessMap.put("B", 2);
accessMap.put("C", 3);
accessMap.get("A");  // Access A
// Iteration: B=2, C=3, A=1 (A moved to end)
```

### LRU Cache Implementation
```java
class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;
    
    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);  // accessOrder = true
        this.capacity = capacity;
    }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;  // Remove oldest when over capacity
    }
}

// Usage
LRUCache<String, Integer> cache = new LRUCache<>(3);
cache.put("A", 1);
cache.put("B", 2);
cache.put("C", 3);
cache.get("A");       // Access A
cache.put("D", 4);    // Evicts B (least recently used)
// Cache: C=3, A=1, D=4
```

### LinkedHashMap Key Points

| Aspect | Detail |
|--------|--------|
| Ordering | Insertion order (default) or access order |
| Null Keys | 1 null key allowed |
| Performance | Slightly slower than HashMap (linked list overhead) |
| Thread Safety | Not thread-safe |
| Use Case | Predictable iteration, LRU cache |

---

## TreeMap

### Internal Structure
```
┌─────────────────────────────────────────────────────┐
│                    TreeMap                           │
│          (Red-Black Tree / Self-Balancing BST)      │
├─────────────────────────────────────────────────────┤
│                      [50]                           │
│                     /     \                         │
│                  [30]      [70]                     │
│                 /   \     /   \                     │
│              [20]  [40] [60]  [80]                  │
│                                                     │
│  All operations: O(log n)                           │
│  Sorted by natural order or Comparator              │
└─────────────────────────────────────────────────────┘
```

```java
// Natural ordering
TreeMap<String, Integer> map = new TreeMap<>();
map.put("Banana", 2);
map.put("Apple", 1);
map.put("Cherry", 3);
// Iteration: Apple=1, Banana=2, Cherry=3 (sorted)

// Custom ordering
TreeMap<String, Integer> descMap = new TreeMap<>(Comparator.reverseOrder());
descMap.put("A", 1);
descMap.put("B", 2);
// Iteration: B=2, A=1
```

### Navigation Methods
```java
TreeMap<Integer, String> map = new TreeMap<>();
map.put(10, "A"); map.put(20, "B"); map.put(30, "C"); map.put(40, "D");

map.firstKey();          // 10
map.lastKey();           // 40
map.lowerKey(25);        // 20 (greatest key strictly < 25)
map.higherKey(25);       // 30 (least key strictly > 25)
map.floorKey(25);        // 20 (greatest key <= 25)
map.ceilingKey(25);      // 30 (least key >= 25)

// Range views (backed by original map)
map.headMap(30);         // {10=A, 20=B} (keys < 30)
map.tailMap(20);         // {20=B, 30=C, 40=D} (keys >= 20)
map.subMap(15, 35);      // {20=B, 30=C} (keys in [15, 35))

// Polling (retrieve and remove)
Map.Entry<Integer, String> first = map.pollFirstEntry();  // 10=A
Map.Entry<Integer, String> last = map.pollLastEntry();     // 40=D
```

### TreeMap Key Points

| Aspect | Detail |
|--------|--------|
| Ordering | Sorted (natural or Comparator) |
| Null Keys | Not allowed (throws NPE with natural ordering) |
| Performance | O(log n) for all operations |
| Internal | Red-Black Tree |
| Thread Safety | Not thread-safe |
| Implements | NavigableMap → SortedMap → Map |

---

## ConcurrentHashMap (VERY IMPORTANT)

### Internal Structure (Java 8+)
```
┌─────────────────────────────────────────────────────┐
│              ConcurrentHashMap (Java 8+)             │
├─────────────────────────────────────────────────────┤
│                                                     │
│  Node<K,V>[] table;  // Same bucket array           │
│                                                     │
│  Key Difference from HashMap:                       │
│  - CAS for insertion into empty bucket              │
│  - synchronized on first node of bucket for updates │
│  - No whole-map lock!                               │
│                                                     │
│  Bucket 0    Bucket 1    Bucket 2    Bucket 3       │
│  [Node]      [null]      [Node]      [Node]         │
│    ↓                       ↓           ↓            │
│  [Node]                 [TreeNode]   [Node]         │
│    ↓                       ↓                        │
│  [null]                 [TreeNode]                   │
│                                                     │
│  Each bucket locked independently                   │
│  Readers NEVER blocked (volatile reads)             │
└─────────────────────────────────────────────────────┘
```

### Java 7 vs Java 8 Implementation

| Aspect | Java 7 (Segment-based) | Java 8+ (Node-based) |
|--------|------------------------|----------------------|
| Locking | Segment locks (16 segments) | Per-bucket synchronized |
| Granularity | Segment level | Node level |
| Read lock | Required | Not required (volatile) |
| Data structure | Array + LinkedList | Array + LinkedList + Red-Black Tree |
| Concurrency | Fixed 16 segments | Dynamic per-bucket |

### Key Operations
```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

// Thread-safe put
map.put("key", 1);

// Atomic putIfAbsent
map.putIfAbsent("key", 2);  // Only puts if key doesn't exist

// Atomic compute
map.compute("key", (k, v) -> v == null ? 1 : v + 1);

// Atomic computeIfAbsent (lazy initialization)
map.computeIfAbsent("key", k -> expensiveComputation(k));

// Atomic merge
map.merge("key", 1, Integer::sum);  // Add 1 to existing value

// Bulk operations (Java 8+)
map.forEach(2, (k, v) -> System.out.println(k + "=" + v)); // parallelism threshold = 2
long count = map.mappingCount();  // Better than size() for large maps

// Search (returns first non-null result)
String found = map.search(1, (k, v) -> v > 100 ? k : null);

// Reduce
int total = map.reduce(1, (k, v) -> v, Integer::sum);
```

### ConcurrentHashMap vs HashMap vs Hashtable vs Collections.synchronizedMap

| Feature | HashMap | Hashtable | synchronizedMap | ConcurrentHashMap |
|---------|---------|-----------|-----------------|-------------------|
| Thread-safe | No | Yes | Yes | Yes |
| Null key | Yes | No | Yes | No |
| Null value | Yes | No | Yes | No |
| Lock | None | Entire map | Entire map | Per-bucket |
| Performance | Best single-thread | Slow | Slow | Best multi-thread |
| Iterator | Fail-fast | Fail-fast | Fail-fast | Weakly consistent |
| Legacy | No | Yes | No | No |

### Fail-fast vs Weakly Consistent
```java
// Fail-fast (HashMap) - throws ConcurrentModificationException
Map<String, Integer> map = new HashMap<>();
map.put("A", 1); map.put("B", 2);
for (String key : map.keySet()) {
    map.remove(key);  // ❌ ConcurrentModificationException!
}

// Weakly consistent (ConcurrentHashMap) - no exception
ConcurrentHashMap<String, Integer> cMap = new ConcurrentHashMap<>();
cMap.put("A", 1); cMap.put("B", 2);
for (String key : cMap.keySet()) {
    cMap.remove(key);  // ✅ No exception (may not reflect all changes)
}
```

---

## Map Comparison Summary

| Feature | HashMap | LinkedHashMap | TreeMap | ConcurrentHashMap |
|---------|---------|---------------|---------|-------------------|
| Ordering | None | Insertion/Access | Sorted | None |
| Get/Put | O(1) | O(1) | O(log n) | O(1) |
| Null key | Yes | Yes | No | No |
| Thread-safe | No | No | No | Yes |
| Internal | Array+List+Tree | HashMap+LinkedList | Red-Black Tree | Array+List+Tree |
| Use case | General purpose | LRU cache, ordered | Sorted data | Concurrent access |

---

## Interview Q&A

**Q: How does HashMap work internally?**
A: Array of buckets. Key's hashCode() determines bucket via `(n-1) & hash`. Collisions form linked list, converted to Red-Black Tree when chain > 8 nodes.

**Q: Why is capacity always power of 2?**
A: Enables bitwise AND `(n-1) & hash` instead of expensive modulo. Also makes resize efficient (elements either stay or move by oldCap).

**Q: What happens when two keys have same hashCode?**
A: Collision. Stored in same bucket as linked list. Distinguished by equals(). After 8 nodes, converted to tree.

**Q: HashMap vs ConcurrentHashMap?**
A: HashMap is not thread-safe. ConcurrentHashMap uses per-bucket locking (Java 8+) and CAS for reads. ConcurrentHashMap doesn't allow null keys/values.

**Q: Why doesn't ConcurrentHashMap allow null?**
A: Ambiguity - can't distinguish between key not found (returns null) and value is null. In concurrent context this creates race conditions.

**Q: How to implement LRU cache?**
A: Extend LinkedHashMap with `accessOrder=true` and override `removeEldestEntry()`.

**Q: When to use TreeMap?**
A: When you need sorted keys, range queries, or navigation operations (floor, ceiling, lower, higher).

**Q: What is the difference between HashMap and Hashtable?**
A: HashMap allows null key/values, not synchronized, faster. Hashtable is synchronized, no null, legacy class.

**Q: What is load factor in HashMap?**
A: Ratio (default 0.75) that triggers resize when size exceeds capacity × loadFactor. Higher = more collisions, less memory. Lower = fewer collisions, more memory.

**Q: What is the initial capacity of HashMap?**
A: 16. Doubles on each resize. Always power of 2.


---

## Common Traps

### ❌ Trap 1: Not Overriding equals() and hashCode() for Custom Keys

**Why it's wrong**:
HashMap uses hashCode() to find bucket and equals() to find key. If not overridden, object identity is used instead of logical equality.

**Incorrect Code**:
```java
class Person {
    String name;
    int age;
    
    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    // Missing equals() and hashCode()
}

Map<Person, String> map = new HashMap<>();
Person p1 = new Person("John", 30);
map.put(p1, "Engineer");

Person p2 = new Person("John", 30);
String job = map.get(p2);  // Returns null! Different object reference
```

**Correct Code**:
```java
class Person {
    String name;
    int age;
    
    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}

Map<Person, String> map = new HashMap<>();
Person p1 = new Person("John", 30);
map.put(p1, "Engineer");

Person p2 = new Person("John", 30);
String job = map.get(p2);  // Returns "Engineer" ✅
```

**Interview Tip**:
Always override both equals() and hashCode() together. If equals() returns true, hashCode() must return same value.

---

### ❌ Trap 2: Modifying Key After Insertion

**Why it's wrong**:
Changing a key's hashCode after insertion makes it impossible to find the entry.

**Incorrect Code**:
```java
class MutableKey {
    int value;
    
    MutableKey(int value) { this.value = value; }
    
    @Override
    public int hashCode() { return value; }
    
    @Override
    public boolean equals(Object o) {
        return o instanceof MutableKey && ((MutableKey) o).value == value;
    }
}

Map<MutableKey, String> map = new HashMap<>();
MutableKey key = new MutableKey(10);
map.put(key, "Value");

key.value = 20;  // ❌ Modifying key!
String result = map.get(key);  // Returns null! Key is in wrong bucket
```

**Correct Code**:
```java
// Use immutable keys
final class ImmutableKey {
    private final int value;
    
    ImmutableKey(int value) { this.value = value; }
    
    public int getValue() { return value; }
    
    @Override
    public int hashCode() { return value; }
    
    @Override
    public boolean equals(Object o) {
        return o instanceof ImmutableKey && ((ImmutableKey) o).value == value;
    }
}

Map<ImmutableKey, String> map = new HashMap<>();
ImmutableKey key = new ImmutableKey(10);
map.put(key, "Value");
// key.value = 20;  // ✅ Compile error - immutable!
```

**Interview Tip**:
Always use immutable objects as HashMap keys. String, Integer, and other wrapper classes are immutable and safe to use.

---

### ❌ Trap 3: ConcurrentModificationException During Iteration

**Why it's wrong**:
Modifying HashMap during iteration (except via iterator.remove()) throws ConcurrentModificationException.

**Incorrect Code**:
```java
Map<String, Integer> map = new HashMap<>();
map.put("A", 1);
map.put("B", 2);
map.put("C", 3);

for (String key : map.keySet()) {
    if (map.get(key) == 2) {
        map.remove(key);  // ❌ ConcurrentModificationException!
    }
}
```

**Correct Code**:
```java
// Option 1: Use Iterator.remove()
Map<String, Integer> map = new HashMap<>();
map.put("A", 1);
map.put("B", 2);
map.put("C", 3);

Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
while (it.hasNext()) {
    Map.Entry<String, Integer> entry = it.next();
    if (entry.getValue() == 2) {
        it.remove();  // ✅ Safe removal
    }
}

// Option 2: Use removeIf (Java 8+)
map.entrySet().removeIf(entry -> entry.getValue() == 2);

// Option 3: Collect keys to remove, then remove
Set<String> toRemove = new HashSet<>();
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    if (entry.getValue() == 2) {
        toRemove.add(entry.getKey());
    }
}
toRemove.forEach(map::remove);
```

**Interview Tip**:
Use Iterator.remove() or collect keys first. For concurrent access, use ConcurrentHashMap which has weakly consistent iterators.

---

### ❌ Trap 4: Using ConcurrentHashMap.size() for Synchronization Logic

**Why it's wrong**:
size() in ConcurrentHashMap is not atomic with other operations. Value may change immediately after reading.

**Incorrect Code**:
```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

// Thread 1
if (map.size() < 10) {  // ❌ Race condition!
    map.put("key", 1);  // Another thread might have added between check and put
}
```

**Correct Code**:
```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

// Use atomic operations
map.computeIfAbsent("key", k -> {
    if (map.size() < 10) {
        return 1;
    }
    return null;  // Don't insert
});

// Or use explicit synchronization if complex logic needed
synchronized (map) {
    if (map.size() < 10) {
        map.put("key", 1);
    }
}
```

**Interview Tip**:
Use atomic operations like computeIfAbsent, putIfAbsent, merge instead of check-then-act patterns with ConcurrentHashMap.

---

### ❌ Trap 5: Assuming TreeMap Allows Null Keys

**Why it's wrong**:
TreeMap with natural ordering calls compareTo() on keys, which throws NullPointerException for null.

**Incorrect Code**:
```java
TreeMap<String, Integer> map = new TreeMap<>();
map.put("A", 1);
map.put(null, 2);  // ❌ NullPointerException!
```

**Correct Code**:
```java
// Option 1: Use HashMap if null keys needed
Map<String, Integer> map = new HashMap<>();
map.put("A", 1);
map.put(null, 2);  // ✅ Works

// Option 2: Use custom Comparator that handles null
TreeMap<String, Integer> map = new TreeMap<>(
    Comparator.nullsFirst(Comparator.naturalOrder())
);
map.put("A", 1);
map.put(null, 2);  // ✅ Works with custom comparator
```

**Interview Tip**:
TreeMap doesn't allow null keys with natural ordering. Use HashMap for null keys or provide a null-safe Comparator.

---

## Related Topics

- [Collections Framework - Lists](./01-List-Implementations.md) - ArrayList, LinkedList comparison
- [Collections Framework - Sets & Queues](./03-Set-Queue-Utilities.md) - HashSet, TreeSet implementations
- [Generics](../05-Generics/01-Generics.md) - Type safety in collections
- [Multithreading & Concurrency](../08-Multithreading-Concurrency/01-Thread-Basics.md) - Thread-safe collections
- [OOP - equals() and hashCode()](../01-Core-Java-OOP/08-OOP-equals-hashCode-toString.md) - Contract for HashMap keys
- [Java 8 Features - Stream API](../06-Java-8-Features/02-Stream-API.md) - Functional operations on maps

---

*Last Updated: February 2026*
*Java Version: 8, 11, 17, 21*
