# Collections Framework - Sets, Queues & Utilities

## Set Implementations

### HashSet vs TreeSet vs LinkedHashSet

| Feature | HashSet | LinkedHashSet | TreeSet |
|---------|---------|---------------|---------|
| Ordering | None | Insertion order | Sorted (natural/comparator) |
| Internal | HashMap | LinkedHashMap | TreeMap (Red-Black Tree) |
| Null | 1 null | 1 null | No null (if natural ordering) |
| Performance | O(1) | O(1) | O(log n) |
| Use case | Fast lookup | Predictable order | Sorted data |

### HashSet
```java
Set<String> set = new HashSet<>();
set.add("A");  // true
set.add("A");  // false (duplicate)
set.contains("A");  // O(1)

// Internal: HashMap with dummy value
// set.add(e) → map.put(e, PRESENT)
```

### LinkedHashSet
```java
Set<String> set = new LinkedHashSet<>();
set.add("C");
set.add("A");
set.add("B");
// Iteration: C, A, B (insertion order preserved)
```

### TreeSet
```java
// Natural ordering
TreeSet<Integer> set = new TreeSet<>();
set.add(3); set.add(1); set.add(2);
// Iteration: 1, 2, 3

// Custom ordering
TreeSet<String> set = new TreeSet<>(Comparator.reverseOrder());
set.add("A"); set.add("B"); set.add("C");
// Iteration: C, B, A

// Navigation methods
set.first();           // Smallest
set.last();            // Largest
set.lower(2);          // Greatest < 2
set.higher(2);         // Least > 2
set.floor(2);          // Greatest <= 2
set.ceiling(2);        // Least >= 2
set.headSet(3);        // Elements < 3
set.tailSet(2);        // Elements >= 2
```

---

## Queue Implementations

### Queue Types
```
Queue (Interface)
    ├── PriorityQueue (Sorted)
    ├── LinkedList (FIFO)
    └── ArrayDeque (Double-ended)
```

### PriorityQueue
```java
// Min-heap by default
PriorityQueue<Integer> pq = new PriorityQueue<>();
pq.offer(5);
pq.offer(1);
pq.offer(3);
pq.poll();  // Returns 1 (smallest first)

// Max-heap
PriorityQueue<Integer> maxPQ = new PriorityQueue<>(
    Collections.reverseOrder()
);

// Custom object
PriorityQueue<Task> taskPQ = new PriorityQueue<>(
    Comparator.comparingInt(Task::getPriority)
);
```

**Internal:** Binary heap (array-based)
- Insert: O(log n)
- Remove min/max: O(log n)
- Peek: O(1)

### ArrayDeque (Double-Ended Queue)
```java
ArrayDeque<Integer> deque = new ArrayDeque<>();

// As Stack (faster than Stack class)
deque.push(1);      // Add to front
deque.pop();        // Remove from front

// As Queue
deque.offer(1);     // Add to end
deque.poll();       // Remove from front

// Deque operations
deque.addFirst(1);
deque.addLast(2);
deque.removeFirst();
deque.removeLast();
deque.peekFirst();
deque.peekLast();
```

**Why ArrayDeque over Stack?**
- Stack extends Vector (synchronized, slow)
- ArrayDeque is unsynchronized, faster
- No capacity restrictions

---

## Comparable vs Comparator

### Comparable (Natural Ordering)
```java
class Person implements Comparable<Person> {
    String name;
    int age;
    
    @Override
    public int compareTo(Person other) {
        return Integer.compare(this.age, other.age);
    }
}

// Usage
List<Person> people = new ArrayList<>();
Collections.sort(people);  // Uses compareTo
```

### Comparator (Custom Ordering)
```java
// Anonymous class
Comparator<Person> byName = new Comparator<Person>() {
    @Override
    public int compare(Person p1, Person p2) {
        return p1.name.compareTo(p2.name);
    }
};

// Lambda
Comparator<Person> byAge = (p1, p2) -> p1.age - p2.age;

// Method reference
Comparator<Person> byName = Comparator.comparing(Person::getName);

// Chaining
Comparator<Person> byAgeThenName = Comparator
    .comparingInt(Person::getAge)
    .thenComparing(Person::getName);

// Reverse
Comparator<Person> byAgeDesc = Comparator.comparing(Person::getAge).reversed();
```

### Key Differences

| Comparable | Comparator |
|------------|------------|
| `compareTo()` method | `compare()` method |
| In class itself | Separate class |
| Single ordering | Multiple orderings |
| `java.lang` | `java.util` |
| Collections.sort(list) | Collections.sort(list, comparator) |

---

## Collections Utility Class

### Sorting
```java
List<Integer> list = Arrays.asList(3, 1, 2);
Collections.sort(list);                    // Natural order
Collections.sort(list, Collections.reverseOrder());  // Reverse
Collections.reverse(list);                 // Reverse in place
```

### Searching
```java
// Binary search (list must be sorted)
int index = Collections.binarySearch(list, 2);

// Find min/max
Collections.min(list);
Collections.max(list);
```

### Immutable Operations
```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B"));

// Unmodifiable views
List<String> unmodifiable = Collections.unmodifiableList(list);
// unmodifiable.add("C");  // UnsupportedOperationException

// Empty collections (immutable)
List<String> empty = Collections.emptyList();
Set<String> emptySet = Collections.emptySet();
Map<String, String> emptyMap = Collections.emptyMap();

// Singleton collections
Set<String> singleton = Collections.singleton("A");
List<String> singletonList = Collections.singletonList("A");
```

### Synchronized Wrappers
```java
List<String> syncList = Collections.synchronizedList(new ArrayList<>());
Set<String> syncSet = Collections.synchronizedSet(new HashSet<>());
Map<String, String> syncMap = Collections.synchronizedMap(new HashMap<>());

// Note: Still need synchronization for iteration
synchronized (syncList) {
    for (String s : syncList) { }
}
```

### Frequency & Disjoint
```java
List<Integer> list = Arrays.asList(1, 2, 2, 3, 2);
int freq = Collections.frequency(list, 2);  // 3

List<Integer> list2 = Arrays.asList(4, 5, 6);
boolean disjoint = Collections.disjoint(list, list2);  // true (no common)
```

---

## Interview Q&A

**Q: How does HashSet ensure uniqueness?**
A: Uses HashMap internally. add() returns false if key already exists.

**Q: Why TreeSet doesn't allow null?**
A: null.compareTo() throws NPE. Can use custom Comparator that handles null.

**Q: Difference between poll() and remove() in Queue?**
A: poll() returns null if empty. remove() throws NoSuchElementException.

**Q: Which is faster: ArrayDeque or LinkedList as Queue?**
A: ArrayDeque. Better cache locality, no node overhead.

**Q: How to make ArrayList immutable?**
A: `List.of()` (Java 9+) or `Collections.unmodifiableList()`.

**Q: What is the time complexity of TreeSet operations?**
A: O(log n) for add, remove, contains (Red-Black Tree).

**Q: Can Set contain duplicates?**
A: No. Adding duplicate returns false, Set unchanged.

**Q: Difference between offer() and add() in Queue?**
A: offer() returns false if fails. add() throws exception.
