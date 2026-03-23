# Complexity Cheatsheet

## Quick Reference for Data Structures & Algorithms Complexity

---

## Data Structures Complexity Chart

### Arrays

| Operation | Time Complexity |
|-----------|-----------------|
| Access (by index) | O(1) |
| Search (unsorted) | O(n) |
| Search (sorted - binary) | O(log n) |
| Insert | O(n) |
| Delete | O(n) |
| Append | O(1) amortized |

### ArrayList (Java)

| Operation | Time Complexity |
|-----------|-----------------|
| get(index) | O(1) |
| add(element) | O(1) amortized |
| add(index, element) | O(n) |
| remove(index) | O(n) |
| contains() | O(n) |
| size() | O(1) |

### LinkedList (Java)

| Operation | Time Complexity |
|-----------|-----------------|
| getFirst() | O(1) |
| getLast() | O(1) |
| get(index) | O(n) |
| addFirst() | O(1) |
| addLast() | O(1) |
| removeFirst() | O(1) |
| removeLast() | O(1) |
| remove(index) | O(n) |
| contains() | O(n) |
| iterator.remove() | O(1) |

### HashMap/HashSet (Java)

| Operation | Time Complexity (Average) | Time Complexity (Worst) |
|-----------|--------------------------|------------------------|
| get(key) | O(1) | O(n) |
| put(key, value) | O(1) | O(n) |
| containsKey() | O(1) | O(n) |
| containsValue() | O(n) | O(n) |
| remove(key) | O(1) | O(n) |

### TreeMap/TreeSet (Java)

| Operation | Time Complexity |
|-----------|-----------------|
| get(key) | O(log n) |
| put(key, value) | O(log n) |
| containsKey() | O(log n) |
| remove(key) | O(log n) |
| firstKey() | O(log n) |
| lastKey() | O(log n) |

### PriorityQueue (Java)

| Operation | Time Complexity |
|-----------|-----------------|
| offer(element) | O(log n) |
| poll() | O(log n) |
| peek() | O(1) |
| contains() | O(n) |
| remove(element) | O(n) |

### Stack (Java)

| Operation | Time Complexity |
|-----------|-----------------|
| push(element) | O(1) |
| pop() | O(1) |
| peek() | O(1) |
| isEmpty() | O(1) |

### Queue (Java)

| Operation | Time Complexity |
|-----------|-----------------|
| offer(element) | O(1) |
| poll() | O(1) |
| peek() | O(1) |
| isEmpty() | O(1) |

### Deque (Java)

| Operation | Time Complexity |
|-----------|-----------------|
| addFirst() | O(1) |
| addLast() | O(1) |
| removeFirst() | O(1) |
| removeLast() | O(1) |
| getFirst() | O(1) |
| getLast() | O(1) |

---

## Sorting Algorithms Complexity

| Algorithm | Best | Average | Worst | Space | Stable |
|-----------|------|---------|-------|-------|--------|
| **Bubble Sort** | O(n) | O(n²) | O(n²) | O(1) | Yes |
| **Selection Sort** | O(n²) | O(n²) | O(n²) | O(1) | No |
| **Insertion Sort** | O(n) | O(n²) | O(n²) | O(1) | Yes |
| **Merge Sort** | O(n log n) | O(n log n) | O(n log n) | O(n) | Yes |
| **Quick Sort** | O(n log n) | O(n log n) | O(n²) | O(log n) | No |
| **Heap Sort** | O(n log n) | O(n log n) | O(n log n) | O(1) | No |
| **Counting Sort** | O(n + k) | O(n + k) | O(n + k) | O(k) | Yes |
| **Radix Sort** | O(nk) | O(nk) | O(nk) | O(n + k) | Yes |
| **Bucket Sort** | O(n + k) | O(n + k) | O(n²) | O(n + k) | Yes |

*k = range of input (for counting/radix), n = number of elements*

---

## Searching Algorithms Complexity

| Algorithm | Time Complexity | Space | Notes |
|-----------|-----------------|-------|-------|
| **Linear Search** | O(n) | O(1) | Works on unsorted arrays |
| **Binary Search** | O(log n) | O(1) | Requires sorted array |
| **Ternary Search** | O(log₃ n) | O(1) | Less efficient than binary |
| **Jump Search** | O(√n) | O(1) | Requires sorted array |
| **Exponential Search** | O(log n) | O(1) | For unbounded arrays |

---

## Graph Algorithms Complexity

### Traversal

| Algorithm | Time | Space |
|-----------|------|-------|
| **BFS** | O(V + E) | O(V) |
| **DFS** | O(V + E) | O(V) |
| **Topological Sort** | O(V + E) | O(V) |

### Shortest Path

| Algorithm | Time | Space | Notes |
|-----------|------|-------|-------|
| **Dijkstra (Heap)** | O((V + E) log V) | O(V) | No negative weights |
| **Bellman-Ford** | O(V·E) | O(V) | Handles negative weights |
| **Floyd-Warshall** | O(V³) | O(V²) | All-pairs shortest path |
| **BFS (unweighted)** | O(V + E) | O(V) | For unweighted graphs |

### Minimum Spanning Tree

| Algorithm | Time | Space |
|-----------|------|-------|
| **Prim's** | O((V + E) log V) | O(V) |
| **Kruskal's** | O(E log V) | O(V) |

---

## Tree Data Structure Complexity

### Binary Search Tree (Balanced)

| Operation | Time Complexity |
|-----------|-----------------|
| Search | O(log n) |
| Insert | O(log n) |
| Delete | O(log n) |
| Traversal | O(n) |
| Find Min/Max | O(log n) |

### AVL Tree / Red-Black Tree

| Operation | Time Complexity |
|-----------|-----------------|
| Search | O(log n) |
| Insert | O(log n) |
| Delete | O(log n) |
| Balance | O(1) rotation |

### B-Tree / B+ Tree

| Operation | Time Complexity |
|-----------|-----------------|
| Search | O(log n) |
| Insert | O(log n) |
| Delete | O(log n) |
| Range Query | O(log n + k) |

### Heap

| Operation | Time Complexity |
|-----------|-----------------|
| Insert | O(log n) |
| Delete Min/Max | O(log n) |
| Get Min/Max | O(1) |
| Build Heap | O(n) |
| Heap Sort | O(n log n) |

### Segment Tree

| Operation | Time Complexity |
|-----------|-----------------|
| Build | O(n) |
| Query | O(log n) |
| Update | O(log n) |

### Trie

| Operation | Time Complexity |
|-----------|-----------------|
| Insert | O(L) |
| Search | O(L) |
| Starts With | O(L) |
| Delete | O(L) |

*L = length of word/string*

---

## Dynamic Programming Complexity

### Template

```
Time Complexity = O(states × transitions)
Space Complexity = O(states)
```

### Common DP Patterns

| Problem | Time | Space |
|---------|------|-------|
| **Fibonacci** | O(n) | O(1) or O(n) |
| **0/1 Knapsack** | O(n·W) | O(n·W) or O(W) |
| **Unbounded Knapsack** | O(n·W) | O(n·W) or O(W) |
| **Longest Common Subsequence** | O(m·n) | O(m·n) or O(min(m,n)) |
| **Longest Increasing Subsequence** | O(n²) or O(n log n) | O(n) or O(n log n) |
| **Coin Change (Min)** | O(amount·n) | O(amount) |
| **Coin Change (Count)** | O(amount·n) | O(amount) |
| **Edit Distance** | O(m·n) | O(m·n) or O(min(m,n)) |

*m, n = dimensions of input, W = weight capacity*

---

## Recursion & Backtracking Complexity

### Template

```
Time Complexity = O(branches^depth × work_per_call)
Space Complexity = O(depth)
```

### Common Patterns

| Problem | Time | Space |
|---------|------|-------|
| **Subsets** | O(n·2ⁿ) | O(n·2ⁿ) or O(n) |
| **Permutations** | O(n·n!) | O(n·n!) or O(n) |
| **Combinations** | O(n·C(n,k)) | O(k) |
| **N-Queens** | O(n!) | O(n) |
| **Sudoku** | O(9^m) | O(m) |

*m = number of empty cells*

---

## Bit Manipulation Operations

| Operation | Time Complexity |
|-----------|-----------------|
| Get bit | O(1) |
| Set bit | O(1) |
| Clear bit | O(1) |
| Update bit | O(1) |
| Check power of 2 | O(1) |
| Count set bits | O(1) to O(log n) |
| Multiply/Divide by 2 | O(1) |

---

## Common Time Complexibilities Quick Reference

```
O(1)          - Hash table lookup, array index access
O(log n)      - Binary search, balanced tree operations
O(n)          - Single loop, linear search
O(n log n)    - Merge sort, heap sort, most O(n) sorts
O(n²)         - Nested loops, insertion sort, bubble sort
O(n³)         - Triple nested loops, naive matrix multiplication
O(2ⁿ)         - Recursive fibonacci, power set generation
O(n!)         - Generate all permutations, brute force traveling salesman
```

---

## Space Complexity Quick Reference

```
O(1)          - Primitive variables, constant extra space
O(log n)      - Recursion stack (balanced divide & conquer)
O(n)          - Array, HashMap, linear recursion stack
O(n log n)    - Merge sort auxiliary space
O(n²)         - 2D array, recursion for optimal BST
O(2ⁿ)         - Memoization table for exponential DP
```

---

## When to Use What

### Data Structure Selection Guide

| Need | Best Choice |
|------|-------------|
| Fast random access | Array, ArrayList |
| Fast insertion/deletion at ends | LinkedList, ArrayDeque |
| Sorted data with fast operations | TreeSet, TreeMap |
| Unique elements, fast lookup | HashSet |
| Key-value pairs, fast lookup | HashMap |
| Priority queue | PriorityQueue (Heap) |
| FIFO processing | Queue, Deque |
| LIFO processing | Stack |
| Prefix matching | Trie |
| Range queries | Segment Tree, Fenwick Tree |
| Graph representation | Adjacency List/Matrix |

### Algorithm Selection Guide

| Need | Best Choice |
|------|-------------|
| General sorting | Arrays.sort() (Timsort) |
| Stable sorting | Merge Sort |
| Integer sorting (small range) | Counting Sort, Radix Sort |
| Searching sorted data | Binary Search |
| Single source shortest path (no negative) | Dijkstra |
| Single source shortest path (negative) | Bellman-Ford |
| All pairs shortest path | Floyd-Warshall |
| Minimum spanning tree | Prim's or Kruskal's |
| Topological ordering | Kahn's algorithm |
| Cycle detection | Union-Find or DFS |
| Connected components | BFS or DFS |

---

## Java Collections Summary

```java
// Choose based on your needs:

// Fast access, slow insert/delete → ArrayList
List<Integer> list = new ArrayList<>();

// Fast insert/delete at ends, slow random access → LinkedList
List<Integer> linked = new LinkedList<>();

// Unique elements, no order → HashSet
Set<Integer> hashSet = new HashSet<>();

// Unique elements, sorted → TreeSet
Set<Integer> treeSet = new TreeSet<>();

// Unique elements, insertion order → LinkedHashSet
Set<Integer> linkedHashSet = new LinkedHashSet<>();

// Key-value, no order → HashMap
Map<String, Integer> hashMap = new HashMap<>();

// Key-value, sorted by key → TreeMap
Map<String, Integer> treeMap = new TreeMap<>();

// Key-value, insertion order → LinkedHashMap
Map<String, Integer> linkedHashMap = new LinkedHashMap<>();

// FIFO queue → LinkedList or ArrayDeque
Queue<Integer> queue = new LinkedList<>();
Queue<Integer> arrayDeque = new ArrayDeque<>();

// LIFO stack → Deque (better than Stack)
Deque<Integer> stack = new ArrayDeque<>();

// Priority queue → PriorityQueue
Queue<Integer> pq = new PriorityQueue<>();

// Thread-safe → ConcurrentHashMap, etc.
Map<String, Integer> concurrent = new ConcurrentHashMap<>();
```

---

## Interview Must-Know Complexities

| Topic | Must Know |
|-------|-----------|
| **Arrays** | Binary search O(log n), merge sort O(n log n) |
| **HashMap** | O(1) average, O(n) worst |
| **ArrayList** | get O(1), add O(1) amortized, remove O(n) |
| **LinkedList** | add/remove at ends O(1), get/search O(n) |
| **String** | immutable, concatenation O(n) |
| **StringBuilder** | mutable, append O(1) amortized |
| **TreeMap** | O(log n) for all operations |
| **PriorityQueue** | O(log n) insert/remove, O(1) peek |
| **HashSet vs TreeSet** | O(1) vs O(log n) |
| **HashMap vs TreeMap** | O(1) vs O(log n) |

---

## Memory Usage Reference

| Data Type | Size |
|-----------|------|
| boolean | 1 byte |
| byte | 1 byte |
| char | 2 bytes |
| short | 2 bytes |
| int | 4 bytes |
| float | 4 bytes |
| long | 8 bytes |
| double | 8 bytes |
| Object reference | 4 bytes (64-bit JVM) |
| Array header | 12 bytes + padding |
| String (internal) | 24 bytes (object) + char[] |

---

## Amortized vs Worst Case

| Structure | Operation | Amortized | Worst |
|-----------|-----------|-----------|-------|
| ArrayList | add() | O(1) | O(n) |
| Dynamic Array | resize | O(1) | O(n) |
| HashMap | put() | O(1) | O(n) |
| StringBuilder | append() | O(1) | O(n) |

---

**Remember**: When in doubt in interviews, default to worst-case analysis unless specifically asked about amortized or average case.
