# Big O Notation - Complete Guide

## Table of Contents
1. [What is Big O?](#what-is-big-o)
2. [Why Big O Matters](#why-big-o-matters)
3. [Time Complexity](#time-complexity)
4. [Space Complexity](#space-complexity)
5. [Common Complexities](#common-complexities)
6. [Analyzing Functions](#analyzing-functions)
7. [Dropping Constants](#dropping-constants)
8. [Common Patterns](#common-patterns)
9. [Best, Average, Worst Cases](#best-average-worst-cases)
10. [Java Examples](#java-examples)
11. [Visual Comparison](#visual-comparison)

---

## What is Big O?

Big O notation describes the **upper bound (worst-case)** growth rate of an algorithm's time or space requirements as the input size increases.

### Formal Definition

```
f(n) = O(g(n)) if and only if
∃ positive constants c and n₀ such that:
0 ≤ f(n) ≤ c·g(n) for ALL n ≥ n₀
```

This means: **f(n) grows no faster than g(n)**

### Intuition

| Big O | Name | Example |
|-------|------|---------|
| O(1) | Constant | Array index access |
| O(log n) | Logarithmic | Binary search |
| O(n) | Linear | Single loop |
| O(n log n) | Linearithmic | Merge sort |
| O(n²) | Quadratic | Nested loops |
| O(2ⁿ) | Exponential | Recursive fibonacci |
| O(n!) | Factorial | Permutations |

---

## Why Big O Matters

### Real-World Impact

```java
// Input size: n = 10,000,000

// O(n) algorithm: 10,000,000 operations
// Takes ~0.01 seconds

// O(n²) algorithm: 100,000,000,000,000 operations
// Takes ~115 days!

// O(log n) algorithm: 23 operations
// Takes ~0.000001 seconds
```

### The Power of Improvement

| Original | Improvement | For n=1,000,000 |
|----------|-------------|------------------|
| O(n²) | O(n log n) | 2M ops vs 1T ops |
| O(n) | O(log n) | 20 ops vs 1M ops |
| O(2ⁿ) | O(n!) | Extremely significant |

---

## Time Complexity

### Operations Count Analysis

```java
// Example 1: O(1) - Constant Time
int getFirst(int[] arr) {
    return arr[0];  // One operation regardless of size
}

// Example 2: O(n) - Linear Time
int sum(int[] arr) {
    int sum = 0;
    for (int num : arr) {  // n operations
        sum += num;
    }
    return sum;
}

// Example 3: O(n²) - Quadratic Time
boolean hasDuplicate(int[] arr) {
    for (int i = 0; i < arr.length; i++) {
        for (int j = i + 1; j < arr.length; j++) {  // n(n-1)/2 operations
            if (arr[i] == arr[j]) return true;
        }
    }
    return false;
}

// Example 4: O(n³) - Cubic Time
void cubeExample(int[][] matrix) {
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            for (int k = 0; k < n; k++)  // n³ operations
                process(matrix[i][j][k]);
}
```

### Loop Analysis Rules

```java
// Rule 1: Sequential loops → Add complexities
void sequential(int n) {
    for (int i = 0; i < n; i++) print(i);      // O(n)
    for (int i = 0; i < n; i++) print(i);      // O(n)
}
// Total: O(n) + O(n) = O(2n) = O(n)

// Rule 2: Nested loops → Multiply complexities
void nested(int n) {
    for (int i = 0; i < n; i++)        // O(n)
        for (int j = 0; j < n; j++)   // × O(n)
            print(i, j);
}
// Total: O(n) × O(n) = O(n²)

// Rule 3: Loop with increments → Often still O(n)
void incrementing(int n) {
    for (int i = 0; i < n; i += 2) print(i);  // Still O(n) - half operations
}

// Rule 4: Logarithmic loops → O(log n)
void logarithmic(int n) {
    for (int i = 1; i < n; i *= 2) print(i);  // i = 1,2,4,8,16... ≤ n
}  // Runs log₂(n) times
```

---

## Space Complexity

Space complexity measures **additional memory** used by the algorithm (not including input).

```java
// Example 1: O(1) - Constant Space
int sum(int[] arr) {
    int sum = 0;  // Only primitive variables
    for (int num : arr) sum += num;
    return sum;
}

// Example 2: O(n) - Linear Space
int[] doubleArray(int[] arr) {
    int[] result = new int[arr.length];  // New array of size n
    for (int i = 0; i < arr.length; i++)
        result[i] = arr[i] * 2;
    return result;
}

// Example 3: O(log n) - Logarithmic Space (recursion stack)
int binarySearch(int[] arr, int target, int left, int right) {
    if (left > right) return -1;
    int mid = left + (right - left) / 2;  // Stack depth: log₂(n)
    if (arr[mid] == target) return mid;
    else if (arr[mid] < target)
        return binarySearch(arr, target, mid + 1, right);
    else
        return binarySearch(arr, target, left, mid - 1);
}

// Example 4: O(n) - Linear Space for recursion
long factorial(int n) {
    if (n <= 1) return 1;
    return n * factorial(n - 1);  // Call stack depth: n
}
```

---

## Common Complexities

### Complexity Chart

```
Time
  ↑
  │                                    ★ O(n!)
  │                               ★
  │                           ★
  │                       ★
  │                   ★         ★ O(2ⁿ)
  │               ★
  │           ★
  │       ★                  ★ O(n³)
  │   ★              ★
  │            ★
  │        ★              ★ O(n²)
  │    ★          ★
  │  ★      ★
  │    ★            ★ O(n log n)
  │  ★
  │★           ★ O(n)
  │      ★
  │ ★    ★ O(log n)
  │★
  └────────────────────────────────────→ Input Size (n)
  O(1)
```

### Detailed Breakdown

| Complexity | Name | 10 | 100 | 1000 | 1000000 |
|-----------|------|-----|------|------|---------|
| O(1) | Constant | 1 | 1 | 1 | 1 |
| O(log n) | Logarithmic | 3 | 7 | 10 | 20 |
| O(n) | Linear | 10 | 100 | 1000 | 1M |
| O(n log n) | Linearithmic | 30 | 700 | 10K | 20M |
| O(n²) | Quadratic | 100 | 10K | 1M | 1T |
| O(n³) | Cubic | 1K | 1M | 1B | 1Q |
| O(2ⁿ) | Exponential | 1K | 1.3×10³⁰ | - | - |
| O(n!) | Factorial | 3.6M | - | - | - |

---

## Analyzing Functions

### Step-by-Step Guide

```java
// Let's analyze this function step by step:
void analyzeExample(int[][] matrix, int n) {
    // Statement 1: O(1)
    int x = 0;

    // Statement 2: O(n) - single loop
    for (int i = 0; i < n; i++) {
        x += i;
    }

    // Statement 3: O(n²) - nested loop
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            x += matrix[i][j];
        }
    }

    // Statement 4: O(log n) - logarithmic loop
    for (int i = 1; i < n; i *= 2) {
        x += i;
    }

    // Total: O(1) + O(n) + O(n²) + O(log n) = O(n²)
    // We drop lower order terms
}

// Another example:
int complexFunction(int n) {
    int count = 0;

    // O(n)
    for (int i = 0; i < n; i++)
        count++;

    // O(n log n)
    for (int i = 1; i < n; i *= 2)
        for (int j = 0; j < i; j++)
            count++;

    // O(log n)
    for (int i = n; i > 0; i /= 2)
        count++;

    return count;
    // Total: O(n + n log n + log n) = O(n log n)
}
```

### Recursive Analysis

```java
// Recurrence: T(n) = T(n-1) + O(1)
// Solution: O(n)
int linearRecursion(int n) {
    if (n <= 0) return 0;
    return n + linearRecursion(n - 1);
}

// Recurrence: T(n) = T(n/2) + O(1)
// Solution: O(log n)
int binaryRecursion(int n) {
    if (n <= 1) return n;
    return binaryRecursion(n / 2) + 1;
}

// Recurrence: T(n) = 2T(n/2) + O(1)
// Solution: O(n)
int divideConquer(int n) {
    if (n <= 1) return n;
    return divideConquer(n / 2) + divideConquer(n / 2);
}

// Recurrence: T(n) = T(n-1) + O(1)
// Solution: O(n)
int fibonacci(int n) {
    if (n <= 1) return n;
    return fibonacci(n - 1) + fibonacci(n - 2);
}
// Note: This is O(2ⁿ) actually due to two recursive calls!
// Memoization reduces it to O(n)
```

---

## Dropping Constants

### Why We Drop Constants

```java
// All these are O(n):
void example1(int n) {
    for (int i = 0; i < n; i++)      // Loop runs n times
        print(i);
}

void example2(int n) {
    for (int i = 0; i < 5*n; i++)     // Loop runs 5n times
        print(i);
}

void example3(int n) {
    for (int i = 0; i < n; i++)      // Loop 1: n times
        print(i);
    for (int i = 0; i < n; i++)      // Loop 2: n times
        print(i);
}

// All are O(n) because constants don't matter at scale
```

### Rules for Simplification

```
1. Drop constants:
   - O(2n) → O(n)
   - O(n/2) → O(n)
   - O(3n + 5) → O(n)

2. Drop lower-order terms:
   - O(n² + n) → O(n²)
   - O(n³ + n² + n) → O(n³)
   - O(1) + O(n) + O(n log n) → O(n log n)

3. Combine terms:
   - O(n) + O(n) → O(2n) → O(n)
   - O(n) × O(n) → O(n²)
```

---

## Common Patterns

### Pattern 1: Single Loop - O(n)

```java
void singleLoop(int[] arr) {
    for (int i = 0; i < arr.length; i++) {  // n iterations
        process(arr[i]);
    }
}
```

### Pattern 2: Nested Loop (Same Variable) - O(n²)

```java
void nestedSame(int n) {
    for (int i = 0; i < n; i++)           // n times
        for (int j = 0; j < n; j++)       // n times each
            process(i, j);
}
```

### Pattern 3: Nested Loop (Different Variables) - O(n×m)

```java
void nestedDiff(int n, int m) {
    for (int i = 0; i < n; i++)           // n times
        for (int j = 0; j < m; j++)       // m times each
            process(i, j);
}
// If n = m: O(n²)
```

### Pattern 4: Half Loop - O(n/2) = O(n)

```java
void halfLoop(int n) {
    for (int i = 0; i < n; i += 2)  // Still O(n)
        process(i);
}
```

### Pattern 5: Logarithmic Loop - O(log n)

```java
void logLoop(int n) {
    for (int i = 1; i < n; i *= 2)  // 1, 2, 4, 8, ... < n
        process(i);                  // log₂(n) iterations
}
```

### Pattern 6: Logarithmic Loop (Decrement) - O(log n)

```java
void logLoopDec(int n) {
    for (int i = n; i > 0; i /= 2)  // n, n/2, n/4, ... > 0
        process(i);                  // log₂(n) iterations
}
```

### Pattern 7: Nested Logarithmic - O(n log n)

```java
void nestedLog(int n) {
    for (int i = 0; i < n; i++)           // n times
        for (int j = 1; j < n; j *= 2)    // log n times
            process(i, j);
}
```

### Pattern 8: Skip Half Each Time - O(log n)

```java
void skipHalf(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (arr[mid] == target) return mid;
        else if (arr[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
}
```

---

## Best, Average, Worst Cases

### Different Cases for Same Algorithm

```java
// Linear Search: O(n) worst/average, O(1) best
int linearSearch(int[] arr, int target) {
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] == target) return i;  // Best: O(1) if first element
    }
    return -1;  // Worst: O(n) if not found or last element
}
```

### Case Comparison

| Algorithm | Best | Average | Worst |
|-----------|------|---------|-------|
| **Linear Search** | O(1) | O(n) | O(n) |
| **Binary Search** | O(1) | O(log n) | O(log n) |
| **Bubble Sort** | O(n) | O(n²) | O(n²) |
| **Quick Sort** | O(n log n) | O(n log n) | O(n²) |
| **Merge Sort** | O(n log n) | O(n log n) | O(n log n) |
| **HashMap Get** | O(1) | O(1) | O(n)* |

*Only when hash collisions occur

### Why Worst Case Matters

```java
// Quicksort is O(n²) worst case but O(n log n) average
// For real-world use, average case is often acceptable
// But for safety-critical systems, worst case matters more

// In interviews, when asked for complexity,
// they usually mean WORST CASE unless specified otherwise
```

---

## Java Examples

### ArrayList Operations

```java
ArrayList<Integer> list = new ArrayList<>();

// Amortized Analysis: O(1) average, O(n) worst
list.add(element);          // Amortized O(1)

// O(1) - direct index access
list.get(index);

// O(n) - need to shift elements
list.remove(index);

// O(n) - need to shift elements
list.add(index, element);

// O(n) - contains uses linear search
list.contains(element);

// O(n) - size is stored, O(1) actually
list.size();
```

### HashMap Operations

```java
HashMap<String, Integer> map = new HashMap<>();

// O(1) average, O(n) worst (hash collisions)
map.put(key, value);
map.get(key);
map.containsKey(key);

// O(n) - iterate through all entries
map.containsValue(value);

// O(n) - iterate through all keys
for (String key : map.keySet()) { }

// O(n) - iterate through all entries
for (Map.Entry<String, Integer> entry : map.entrySet()) { }
```

### String Operations

```java
String s = "hello";

// O(1)
s.length();

// O(n) - creates new string
s + "world";           // "helloworld"
s.concat("world");     // "helloworld"

// O(n) - creates new string
s.substring(1, 3);     // "el"

// O(n) - parses entire string
s.indexOf('l');        // 2 (first occurrence)

// O(n) - compares character by character
s.equals("hello");     // true
```

### Collections Operations

```java
List<Integer> arrayList = new ArrayList<>();   // O(1) access
List<Integer> linkedList = new LinkedList<>(); // O(1) insert/delete at ends

// Both are O(n) for search
// ArrayList: O(1) access, O(n) insert/delete
// LinkedList: O(n) access, O(1) insert/delete at known position

Set<Integer> hashSet = new HashSet<>();       // O(1) add, remove, contains
Set<Integer> treeSet = new TreeSet<>();      // O(log n) add, remove, contains

Map<Integer, String> hashMap = new HashMap<>();
Map<Integer, String> treeMap = new TreeMap<>();
```

---

## Visual Comparison

### Growth Rate Visualization

```
n=10:
O(1)       : █
O(log n)   : ███
O(n)       : ██████████
O(n log n) : ████████████
O(n²)      : ████████████████████████████████
O(2ⁿ)      : ████████████████████████████████████████ (1024)

n=100:
O(1)       : █
O(log n)   : █████
O(n)       : ████████████████████████████████████████
O(n log n) : ████████████████████████████████████████████████████
O(n²)      : (too large to display)
O(2ⁿ)      : (astronomically large)

n=1000:
O(1)       : █
O(log n)   : ██████
O(n)       : ██████████ (and continues...)
```

### Real-World Times

Assuming 1 billion operations per second:

| Complexity | n = 10 | n = 100 | n = 1,000 | n = 1,000,000 |
|-----------|--------|---------|-----------|---------------|
| O(1) | <1ns | <1ns | <1ns | <1ns |
| O(log n) | <1ns | <1ns | <1ns | <1ns |
| O(n) | 10ns | 100ns | 1μs | 1ms |
| O(n log n) | 33ns | 664ns | 10μs | 20ms |
| O(n²) | 100ns | 10μs | 1ms | 16min |
| O(n³) | 1μs | 1ms | 1s | 31years |
| O(2ⁿ) | 1μs | 4×10¹³ years | ∞ | ∞ |

---

## Key Takeaways

1. **Big O is Upper Bound**: It describes the worst-case growth rate
2. **Drop Constants**: O(2n) simplifies to O(n)
3. **Drop Lower Terms**: In O(n² + n), n² dominates
4. **Identify Loops**: Nested loops multiply, sequential loops add
5. **Consider Both**: Time AND space complexity matter
6. **Practice**: Analyze code mentally before running

---

## Practice Exercises

1. What is the time complexity of:
```java
for (int i = 0; i < n; i++)
    for (int j = i; j < n; j++)
        print(i, j);
```

2. What is the space complexity of:
```java
int[] createTable(int n) {
    int[][] table = new int[n][n];
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            table[i][j] = i * j;
    return table[0];
}
```

3. Analyze the recursive function:
```java
void recurse(int n) {
    if (n <= 0) return;
    recurse(n - 1);
    recurse(n - 1);
}
```

---

## Answers

1. **O(n²)** - Inner loop runs n + (n-1) + (n-2) + ... + 1 = n(n+1)/2 = O(n²)

2. **O(n²)** - Creates n×n 2D array, then returns first row (garbage collects rest)

3. **O(2ⁿ)** - Each call creates 2 more calls until n=0
