# Collections Framework - Lists

## Collection Hierarchy

```
                Iterable
                    │
                Collection
                ↙    ↓    ↘
            List    Set    Queue
             ↓       ↓      ↓
        ArrayList  HashSet  LinkedList
        LinkedList TreeSet  PriorityQueue
        Vector     LinkedHashSet ArrayDeque
```

---

## ArrayList vs LinkedList vs Vector

| Feature | ArrayList | LinkedList | Vector |
|---------|-----------|------------|--------|
| Internal | Dynamic Array | Doubly Linked List | Dynamic Array |
| Access | O(1) | O(n) | O(1) |
| Insert/Delete | O(n) | O(1) at ends | O(n) |
| Thread-safe | No | No | Yes (synchronized) |
| Memory | Less | More (nodes) | Less |
| Performance | Fast read | Fast write | Slow (sync) |

---

## ArrayList Deep Dive

### Internal Structure
```java
public class ArrayList<E> {
    Object[] elementData;  // Backing array
    int size;              // Number of elements
    
    // Default initial capacity: 10
    // Growth: newCapacity = oldCapacity + (oldCapacity >> 1)
    //         = 1.5x growth
}
```

### Key Operations
```java
ArrayList<Integer> list = new ArrayList<>();

// Add - amortized O(1)
list.add(1);           // Append
list.add(0, 2);        // Insert at index - O(n)

// Get - O(1)
int val = list.get(0);

// Remove - O(n)
list.remove(0);        // By index
list.remove(Integer.valueOf(1));  // By value

// Size
int size = list.size();

// Capacity vs Size
list.ensureCapacity(100);  // Pre-allocate
list.trimToSize();         // Reduce capacity to size
```

### When ArrayList Resizes
```java
ArrayList<Integer> list = new ArrayList<>(3);  // Capacity: 3
list.add(1); list.add(2); list.add(3);  // Size: 3, Capacity: 3
list.add(4);  // Triggers resize
// New capacity = 3 + (3 >> 1) = 3 + 1 = 4 (actually 1.5x)
// Arrays.copyOf creates new array
```

---

## LinkedList Deep Dive

### Internal Structure
```java
public class LinkedList<E> {
    Node<E> first;
    Node<E> last;
    int size;
    
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;
    }
}
```

### Key Operations
```java
LinkedList<Integer> list = new LinkedList<>();

// Add at ends - O(1)
list.addFirst(1);
list.addLast(2);

// Remove from ends - O(1)
list.removeFirst();
list.removeLast();

// As Queue/Deque
list.offer(3);      // Add to tail
list.poll();        // Remove from head
list.peek();        // View head

// As Stack
list.push(4);       // Add to head
list.pop();         // Remove from head
```

---

## Vector (Legacy - Avoid)

```java
Vector<Integer> vector = new Vector<>();
vector.add(1);  // Synchronized method

// Better alternative
List<Integer> syncList = Collections.synchronizedList(new ArrayList<>());
// Or use CopyOnWriteArrayList for concurrent reads
```

---

## Comparison Scenarios

### When to use ArrayList?
- Frequent random access
- Rare insertions/deletions in middle
- Memory efficiency matters

### When to use LinkedList?
- Frequent insertions/deletions at beginning/end
- Implementing queue/stack
- No random access needed

### When to use Vector?
- **Never.** Use `Collections.synchronizedList()` or `CopyOnWriteArrayList`

---

## Common Operations

### Iteration
```java
List<String> list = Arrays.asList("A", "B", "C");

// 1. For-each (read-only)
for (String s : list) { }

// 2. Iterator (can remove)
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    if (s.equals("B")) it.remove();
}

// 3. ListIterator (bidirectional)
ListIterator<String> lit = list.listIterator();
while (lit.hasNext()) { lit.next(); }
while (lit.hasPrevious()) { lit.previous(); }

// 4. Index-based
for (int i = 0; i < list.size(); i++) { list.get(i); }

// 5. forEach + lambda
list.forEach(s -> System.out.println(s));
```

### Conversion
```java
// Array to List
String[] arr = {"A", "B", "C"};
List<String> list1 = Arrays.asList(arr);      // Fixed size, backed by array
List<String> list2 = List.of(arr);            // Java 9+, immutable
List<String> list3 = new ArrayList<>(Arrays.asList(arr));  // Mutable

// List to Array
String[] array = list.toArray(new String[0]);  // Type-safe
String[] array2 = list.toArray(String[]::new); // Method reference
```

---

## Interview Q&A

**Q: What is the default capacity of ArrayList?**
A: 10. Grows by 1.5x when full.

**Q: How does ArrayList grow internally?**
A: Creates new array with 1.5x capacity, copies elements via Arrays.copyOf().

**Q: Why is ArrayList faster than LinkedList for random access?**
A: ArrayList uses array (O(1) index access). LinkedList must traverse nodes (O(n)).

**Q: Can ArrayList contain duplicates?**
A: Yes, List allows duplicates.

**Q: What happens when you remove from ArrayList?**
A: Elements shift left to fill gap. O(n) operation.

**Q: Difference between Arrays.asList() and List.of()?**
A: Arrays.asList() - mutable elements, fixed size. List.of() - immutable, Java 9+.

**Q: Is ArrayList thread-safe?**
A: No. Use Collections.synchronizedList() or CopyOnWriteArrayList.

**Q: Memory overhead of LinkedList vs ArrayList?**
A: LinkedList has more overhead (node objects with prev/next references).
