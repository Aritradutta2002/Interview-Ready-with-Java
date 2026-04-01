# Foundations of Data Structures and Algorithms

## Table of Contents
1. [Introduction](#introduction)
2. [What are Data Structures?](#what-are-data-structures)
3. [What are Algorithms?](#what-are-algorithms)
4. [Why DSA Matters](#why-dsa-matters)
5. [Types of Data Structures](#types-of-data-structures)
6. [Types of Algorithms](#types-of-algorithms)
7. [Problem Solving Approaches](#problem-solving-approaches)
8. [DSA in Java](#dsa-in-java)
9. [Key Terminology](#key-terminology)

---

## Introduction

Data Structures and Algorithms (DSA) form the **backbone of computer science** and software engineering. Every program you write, every software system you build, relies on the careful selection and implementation of data structures and algorithms to solve problems efficiently.

### What You Will Learn in This Series

This comprehensive series covers:

| Category | Topics |
|----------|--------|
| **Foundations** | Big O, Complexity Analysis, Core Concepts |
| **Linear Structures** | Arrays, Strings, Linked Lists, Stacks, Queues |
| **Non-Linear Structures** | Trees, Heaps, Graphs, Tries |
| **Algorithms** | Sorting, Searching, Recursion, Dynamic Programming |
| **Advanced Techniques** | Greedy, Backtracking, Bit Manipulation |

---

## What are Data Structures?

A **data structure** is a way of **organizing and storing data** in a computer so that it can be accessed and modified efficiently. The choice of data structure significantly impacts the performance of your code.

### Real-World Analogy

Think of data structures like different types of **storage containers** in your home:

| Storage Type | Data Structure | Use Case |
|--------------|-----------------|----------|
| Closet with rods and hangers | Linked List | Sequential access, FIFO |
| Drawer with compartments | Array | Random access by index |
| Stack of trays | Stack | LIFO, undo operations |
| Queue at a coffee shop | Queue | First-come, first-served |
| Family tree organization | Tree | Hierarchical data |
| City road network | Graph | Complex relationships |

---

## What are Algorithms?

An **algorithm** is a **step-by-step procedure** or **set of instructions** designed to solve a specific problem or accomplish a task.

### Characteristics of a Good Algorithm

1. **Input**: Zero or more quantities
2. **Output**: At least one quantity
3. **Definiteness**: Each instruction is clear and unambiguous
4. **Finiteness**: The algorithm terminates after a finite number of steps
5. **Effectiveness**: Every step can be performed in a finite amount of time

### Algorithm vs Program

| Aspect | Algorithm | Program |
|--------|-----------|---------|
| Nature | Logical/Abstract | Concrete/Implementation |
| Language | Pseudo-code, Flowcharts | Programming Language |
| Execution | Not directly executable | Directly executable |
| Focus | Problem-solving approach | Implementation details |

---

## Why DSA Matters

### 1. Problem-Solving Efficiency

The same problem can be solved in **milliseconds or minutes** depending on the chosen algorithm:

```java
// INEFFICIENT: O(n²) - Finding duplicate in unsorted array
boolean hasDuplicate(int[] arr) {
    for (int i = 0; i < arr.length; i++) {
        for (int j = i + 1; j < arr.length; j++) {
            if (arr[i] == arr[j]) return true;
        }
    }
    return false;
}

// EFFICIENT: O(n) - Using HashSet
boolean hasDuplicateEfficient(int[] arr) {
    Set<Integer> seen = new HashSet<>();
    for (int num : arr) {
        if (seen.contains(num)) return true;
        seen.add(num);
    }
    return false;
}
```

### 2. Technical Interviews

DSA is the **core of technical interviews** at top companies:
- Google, Meta, Amazon, Apple, Microsoft
- Bloomberg, Goldman Sachs, Stripe
- ByteDance, Uber, Airbnb

### 3. System Design

Understanding DSA helps in:
- Database index structures (B-Trees, B+Trees)
- Message queues and buffers
- Cache implementations (LRU, LFU)
- Network routing algorithms

---

## Types of Data Structures

### Classification Overview

```
Data Structures
├── Linear
│   ├── Static (Fixed Size)
│   │   └── Array
│   └── Dynamic (Variable Size)
│       ├── Linked List
│       ├── Stack
│       └── Queue
└── Non-Linear
    ├── Tree
    │   ├── Binary Tree
    │   ├── Binary Search Tree
    │   ├── AVL Tree
    │   ├── Red-Black Tree
    │   ├── B-Tree
    │   └── Segment Tree
    ├── Graph
    │   ├── Directed
    │   ├── Undirected
    │   ├── Weighted
    │   └── DAG
    └── Hash-Based
        ├── HashMap
        ├── HashSet
        └── Trie
```

### Primitive vs Non-Primitive

**Primitive Data Types:**
- `byte`, `short`, `int`, `long`
- `float`, `double`
- `char`, `boolean`

**Non-Primitive Data Types:**
- Arrays, Strings
- Classes, Interfaces
- Collections Framework

---

## Types of Algorithms

### By Approach

| Algorithm Type | Description | Examples |
|---------------|-------------|----------|
| **Brute Force** | Try all possibilities | Linear search, Generate all subsets |
| **Divide & Conquer** | Split, solve, merge | Merge Sort, Binary Search |
| **Dynamic Programming** | Memoization + Recursion | Fibonacci, Knapsack |
| **Greedy** | Make locally optimal choices | Activity Selection, Huffman Coding |
| **Backtracking** | Build solution incrementally, backtrack | N-Queens, Sudoku |
| **Graph Algorithms** | Traverse/graph structures | BFS, DFS, Dijkstra |

### By Problem Type

- **Sorting**: Arrange data in order
- **Searching**: Find specific data
- **Graph Traversal**: Visit all nodes
- **Optimization**: Find best solution
- **Pathfinding**: Find shortest path

---

## Problem Solving Approaches

### The FAST Method

A systematic approach to solving DSA problems:

```
F - Focus on the Problem
    - Understand the input and output
    - Identify constraints
    - Consider edge cases

A - Analyze and Explore
    - Work through examples manually
    - Identify patterns
    - Consider multiple approaches

S - Strategize and Design
    - Choose data structures
    - Design algorithm
    - Plan complexity

T - Implement and Test
    - Code the solution
    - Test with examples
    - Optimize if needed
```

### Problem-Solving Patterns

| Pattern | When to Use |
|---------|------------|
| Two Pointers | Searching pairs, palindrome checks |
| Sliding Window | Subarray/substring problems |
| Prefix Sum | Range sum queries |
| Fast & Slow Pointers | Cycle detection |
| Merge Intervals | Overlapping intervals |
| BFS/DFS | Graph/tree traversal |

---

## DSA in Java

### Java Collections Framework Hierarchy

```
Iterable
    │
    └── Collection
            │
            ├── List (Ordered, Duplicate allowed)
            │   ├── ArrayList
            │   ├── LinkedList
            │   ├── Vector
            │   └── Stack
            │
            ├── Set (Unique elements)
            │   ├── HashSet
            │   ├── LinkedHashSet
            │   └── TreeSet
            │
            ├── Queue (FIFO)
            │   ├── PriorityQueue
            │   ├── Deque
            │   │   ├── ArrayDeque
            │   │   └── LinkedList
            │   └── BlockingQueue
            │       ├── LinkedBlockingQueue
            │       └── ArrayBlockingQueue
            │
            └── Map (Key-Value pairs)
                ├── HashMap
                ├── LinkedHashMap
                ├── TreeMap
                ├── Hashtable
                └── ConcurrentHashMap
```

### Choosing the Right Data Structure

```java
// Need fast random access? → ArrayList
List<Integer> list = new ArrayList<>();
int element = list.get(index);  // O(1)

// Need fast insertion/deletion at ends? → LinkedList
List<Integer> linked = new LinkedList<>();
linked.addFirst(element);  // O(1)
linked.addLast(element);   // O(1)

// Need unique elements? → HashSet
Set<Integer> set = new HashSet<>();
set.add(element);  // O(1) average

// Need key-value pairs? → HashMap
Map<String, Integer> map = new HashMap<>();
map.put(key, value);  // O(1) average

// Need sorted data? → TreeSet / TreeMap
Set<Integer> sortedSet = new TreeSet<>();
Map<String, Integer> sortedMap = new TreeMap<>();

// Need priority-based processing? → PriorityQueue
Queue<Integer> pq = new PriorityQueue<>();
pq.offer(element);  // O(log n)
```

---

## Key Terminology

### Complexity Terms

| Term | Symbol | Description |
|------|--------|-------------|
| Big O | O(n) | Worst-case upper bound |
| Big Omega | Ω(n) | Best-case lower bound |
| Big Theta | θ(n) | Tight bound (both O and Ω) |
| Little O | o(n) | Upper bound but not tight |

### Data Structure Terms

| Term | Definition |
|------|------------|
| **Node** | Basic unit of a data structure |
| **Head/Tail** | First/last node in a list |
| **Root** | Top node in a tree |
| **Leaf** | Node with no children |
| **Parent/Child** | Nodes connected by an edge |
| **Siblings** | Nodes with same parent |
| **Depth** | Number of edges from root to node |
| **Height** | Maximum depth of a node in tree |
| **Degree** | Number of children of a node |
| **Cycle** | Path that loops back to start |
| **Connected** | Path exists between all nodes |
| **Acyclic** | No cycles |

### Algorithm Terms

| Term | Definition |
|------|------------|
| **Recursion** | Function calling itself |
| **Iteration** | Loop-based repetition |
| **Memoization** | Caching results |
| **Tabulation** | Bottom-up DP |
| **Backtracking** | Undo choices that don't work |
| **Greedy** | Local optimum leads to global optimum |
| **Invariant** | Condition that remains true |

---

## What's Next?

Now that you understand the foundations, let's dive deep into each topic:

1. **[Big O Notation](./big_o_notation.md)** - Master time and space complexity
2. **[Complexity Cheatsheet](./complexity_cheatsheet.md)** - Quick reference for all complexities
3. **[Arrays](./01_array/theory.md)** - The most fundamental data structure
4. **[Linked Lists](./03_linkedlist/theory.md)** - Dynamic sequential storage
5. And many more...

---

## Practice Problems

To solidify your understanding:

1. Identify the data structure best suited for each scenario
2. Analyze the time complexity of existing code
3. Implement basic data structures from scratch
4. Solve problems using multiple approaches

---

> **Remember**: "Data structures are the building blocks of efficient software. Algorithms are the blueprints that tell you how to use those building blocks."
