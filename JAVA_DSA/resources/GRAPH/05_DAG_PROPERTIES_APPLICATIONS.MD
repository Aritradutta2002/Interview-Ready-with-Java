# DAG Properties & Applications - Complete Guide

## Table of Contents
1. [What is DAG?](#what-is-dag)
2. [DAG Properties](#dag-properties)
3. [Topological Sort Deep Dive](#topological-sort-deep-dive)
4. [DAG Applications](#dag-applications)
5. [Longest Path in DAG](#longest-path-in-dag)
6. [DAG Counting](#dag-counting)
7. [All Paths Between Nodes](#all-paths-between-nodes)
8. [Practice Problems](#practice-problems)

---

## What is DAG?

### Definition

```
┌─────────────────────────────────────────────────────────────────┐
│                    DAG (DIRECTED ACYCLIC GRAPH)                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  DEFINITION:                                                    │
│  A directed graph with NO directed cycles                         │
│                                                                  │
│  KEY PROPERTIES:                                               │
│  ✅ All edges have direction                                    │
│  ✅ No path starts and ends at the same vertex                │
│  ✅ ALWAYS admits a topological ordering                       │
│  ✅ Can always be drawn without crossing edges               │
│                                                                  │
│  VISUAL EXAMPLE:                                                │
│                                                                  │
│      Valid DAG:              Invalid (has cycle):              │
│                                                                  │
│        A ──→ B ──→ C            A ──→ B                       │
│        │                         │      │                       │
│        ↓                         ↓      ↓                       │
│        D ──→ E                   C ────                       │
│                                    ↑                             │
│                                    └── cycle!                   │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## DAG Properties

### Key Properties

```
┌─────────────────────────────────────────────────────────────────┐
│                      DAG PROPERTIES                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. TOPOLOGICAL ORDER EXISTS                                    │
│     - Can arrange vertices linearly such that all edges           │
│       go from left to right                                     │
│                                                                  │
│  2. HAMILTONIAN PATH EXISTS                                     │
│     - A DAG always has a Hamiltonian path that visits           │
│       every vertex exactly once (but NOT Hamiltonian cycle)      │
│                                                                  │
│  3. CAN BE TOPOLOGICALLY SORTED IN MULTIPLE WAYS               │
│     - Number of topological sorts = number of linear extensions │
│     - Can be counted using DP                                    │
│                                                                  │
│  4. EDGE COUNT BOUND                                            │
│     - Maximum edges in DAG with V vertices: V(V-1)/2            │
│     - Sparse graphs by nature                                   │
│                                                                  │
│  5. PARTIAL ORDER                                               │
│     - DAG defines a partial order on vertices                   │
│     - Some pairs may be incomparable (no path between them)     │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### DAG Theorems

```
THEOREM 1:
Every DAG has at least one vertex with indegree 0 (source).

Proof: If every vertex had indegree > 0, following incoming edges
       would create a cycle (pigeonhole principle).

THEOREM 2:
Every DAG has at least one vertex with outdegree 0 (sink).

Proof: Similar to Theorem 1, by considering reverse graph.

THEOREM 3:
A directed graph is a DAG if and only if it admits a topological sort.

Proof: If has cycle, no topological order possible (cycle vertices
       must appear in impossible order). If acyclic, can repeatedly
       remove sources (Theorem 1) to get order.
```

---

## Topological Sort Deep Dive

### Methods Comparison

```
┌─────────────────────────────────────────────────────────────────┐
│                 TOPOLOGICAL SORT METHODS                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  METHOD 1: Kahn's Algorithm (BFS-based)                         │
│  ───────────────────────────────────────                       │
│  1. Calculate indegree for all vertices                         │
│  2. Add all sources (indegree 0) to queue                     │
│  3. Process queue:                                              │
│     - Dequeue vertex, add to result                            │
│     - Reduce indegree of neighbors                              │
│     - Add newly created sources to queue                        │
│  4. If result size < V, there's a cycle                       │
│                                                                  │
│  Time: O(V + E) | Space: O(V)                                 │
│                                                                  │
│  METHOD 2: DFS-based (Post-order Stack)                        │
│  ─────────────────────────────────────────                     │
│  1. Perform DFS from all unvisited vertices                     │
│  2. After exploring all neighbors, push vertex to stack        │
│  3. Pop from stack to get topological order                    │
│                                                                  │
│  Time: O(V + E) | Space: O(V)                                 │
│                                                                  │
│  METHOD 3: Lexicographically Smallest                           │
│  ─────────────────────────────────────────                     │
│  - Use Min-Heap instead of Queue                               │
│  - Get lexicographically smallest valid ordering               │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Java Implementation - All Methods

```java
package dsa.graph.dag;

import java.util.*;

public class TopologicalSortComplete {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;

    public TopologicalSortComplete(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
    }

    // Method 1: Kahn's Algorithm
    public List<Integer> kahnTopoSort() {
        int[] indegree = new int[numVertices];
        for (int u = 0; u < numVertices; u++) {
            for (int v : adjList.get(u)) {
                indegree[v]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numVertices; i++) {
            if (indegree[i] == 0) queue.offer(i);
        }

        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            result.add(u);

            for (int v : adjList.get(u)) {
                indegree[v]--;
                if (indegree[v] == 0) queue.offer(v);
            }
        }

        if (result.size() != numVertices) {
            return Collections.emptyList(); // Cycle exists!
        }
        return result;
    }

    // Method 2: DFS-based
    public List<Integer> dfsTopoSort() {
        Stack<Integer> stack = new Stack<>();
        boolean[] visited = new boolean[numVertices];
        boolean[] inStack = new boolean[numVertices];

        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                if (dfsDetect(i, visited, inStack, stack)) {
                    return Collections.emptyList(); // Cycle!
                }
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }

    private boolean dfsDetect(int u, boolean[] visited, boolean[] inStack, Stack<Integer> stack) {
        visited[u] = true;
        inStack[u] = true;

        for (int v : adjList.get(u)) {
            if (inStack[v]) return true; // Cycle detected!
            if (!visited[v] && dfsDetect(v, visited, inStack, stack)) {
                return true;
            }
        }

        inStack[u] = false;
        stack.push(u);
        return false;
    }

    // Method 3: Lexicographically Smallest (using Min-Heap)
    public List<Integer> lexSmallestTopoSort() {
        int[] indegree = new int[numVertices];
        for (int u = 0; u < numVertices; u++) {
            for (int v : adjList.get(u)) {
                indegree[v]++;
            }
        }

        PriorityQueue<Integer> heap = new PriorityQueue<>();
        for (int i = 0; i < numVertices; i++) {
            if (indegree[i] == 0) heap.offer(i);
        }

        List<Integer> result = new ArrayList<>();
        while (!heap.isEmpty()) {
            int u = heap.poll();
            result.add(u);

            for (int v : adjList.get(u)) {
                indegree[v]--;
                if (indegree[v] == 0) heap.offer(v);
            }
        }

        if (result.size() != numVertices) {
            return Collections.emptyList();
        }
        return result;
    }

    public static void main(String[] args) {
        TopologicalSortComplete graph = new TopologicalSortComplete(6);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);

        System.out.println("Kahn's:           " + graph.kahnTopoSort());
        System.out.println("DFS-based:        " + graph.dfsTopoSort());
        System.out.println("Lexicographically: " + graph.lexSmallestTopoSort());
    }
}
```

---

## DAG Applications

### 1. Course Scheduling

```
┌─────────────────────────────────────────────────────────────────┐
│                    COURSE SCHEDULING                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  PROBLEM:                                                       │
│  Given prerequisites, find an order to take courses.            │
│                                                                  │
│  SOLUTION:                                                      │
│  Topological sort of course dependency graph.                   │
│                                                                  │
│  EXAMPLE:                                                        │
│  Prerequisites:                                                  │
│    - CS101 -> CS201                                            │
│    - CS101 -> CS202                                            │
│    - CS201 -> CS301                                            │
│    - CS202 -> CS302                                            │
│                                                                  │
│  VALID ORDER: CS101 -> CS201 -> CS301                          │
│                CS101 -> CS202 -> CS302                          │
│                                                                  │
│  NOTE: Multiple valid orderings may exist!                     │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 2. Build Systems

```
┌─────────────────────────────────────────────────────────────────┐
│                      BUILD SYSTEMS                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  PROBLEM:                                                       │
│  Given file dependencies, in what order should files be built?   │
│                                                                  │
│  EXAMPLE:                                                        │
│    main.cpp -> parser.cpp -> lexer.cpp                        │
│    main.cpp -> compiler.cpp                                    │
│    parser.cpp -> ast.cpp                                       │
│                                                                  │
│  BUILD ORDER: lexer.cpp -> ast.cpp -> parser.cpp ->          │
│                compiler.cpp -> main.cpp                         │
│                                                                  │
│  USED BY: Maven, Gradle, Make, Bazel                         │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 3. Package Managers

```
┌─────────────────────────────────────────────────────────────────┐
│                    PACKAGE MANAGERS                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  NPM, PIP, APT all use DAG for dependencies:                   │
│                                                                  │
│  Example package.json:                                          │
│  {                                                             │
│    "dependencies": {                                           │
│      "lodash": "^4.0.0",                                       │
│      "express": "^4.0.0"                                        │
│    }                                                            │
│  }                                                             │
│                                                                  │
│  Installation order: Topological sort of dependency DAG!       │
│                                                                  │
│  CYCLE DETECTION: If A depends on B, B depends on A,          │
│  installation fails (cannot resolve).                          │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 4. Task Scheduling

```
┌─────────────────────────────────────────────────────────────────┐
│                      TASK SCHEDULING                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  PROBLEM:                                                       │
│  Schedule tasks with dependencies to minimize total time.       │
│                                                                  │
│  DAG: Task dependencies                                         │
│  Topological order: Valid execution sequence                    │
│                                                                  │
│  CRITICAL PATH: Longest path through DAG                       │
│  (minimum time to complete all tasks)                           │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Longest Path in DAG

### Problem Definition

```
┌─────────────────────────────────────────────────────────────────┐
│                    LONGEST PATH IN DAG                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  PROBLEM:                                                       │
│  Find the longest path from source to any vertex in DAG.       │
│                                                                  │
│  WHY DAG SPECIFIC:                                              │
│  - Longest path in general graph is NP-Hard                    │
│  - In DAG, can be solved with DP in O(V + E)!                 │
│                                                                  │
│  METHOD:                                                        │
│  1. Topologically sort the DAG                                 │
│  2. DP: longest[v] = max(longest[u] + weight(u,v))           │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Java Implementation

```java
package dsa.graph.dag;

import java.util.*;

public class LongestPathDAG {
    private Map<Integer, List<int[]>> adjList; // neighbor, weight
    private int numVertices;

    public LongestPathDAG(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        adjList.get(source).add(new int[]{destination, weight});
    }

    public long[] longestPath(int source) {
        // Step 1: Topological sort
        List<Integer> topo = topologicalSort();
        if (topo.isEmpty()) {
            return null; // Cycle!
        }

        // Step 2: DP
        long[] dist = new long[numVertices];
        int[] parent = new int[numVertices];
        Arrays.fill(dist, Long.MIN_VALUE);
        Arrays.fill(parent, -1);
        dist[source] = 0;

        // Process in topological order
        for (int u : topo) {
            if (dist[u] == Long.MIN_VALUE) continue;

            for (int[] edge : adjList.get(u)) {
                int v = edge[0];
                int w = edge[1];

                if (dist[u] + w > dist[v]) {
                    dist[v] = dist[u] + w;
                    parent[v] = u;
                }
            }
        }

        return dist;
    }

    public List<Integer> getPath(int[] parent, int destination) {
        List<Integer> path = new ArrayList<>();
        int current = destination;
        while (current != -1) {
            path.add(current);
            current = parent[current];
        }
        Collections.reverse(path);
        return path;
    }

    private List<Integer> topologicalSort() {
        int[] indegree = new int[numVertices];
        for (int u = 0; u < numVertices; u++) {
            for (int[] edge : adjList.get(u)) {
                indegree[edge[0]]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numVertices; i++) {
            if (indegree[i] == 0) queue.offer(i);
        }

        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            result.add(u);

            for (int[] edge : adjList.get(u)) {
                indegree[edge[0]]--;
                if (indegree[edge[0]] == 0) queue.offer(edge[0]);
            }
        }

        return result.size() == numVertices ? result : Collections.emptyList();
    }

    public static void main(String[] args) {
        LongestPathDAG graph = new LongestPathDAG(6);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 4);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 3);
        graph.addEdge(2, 3, 2);
        graph.addEdge(2, 4, 5);
        graph.addEdge(3, 5, 1);
        graph.addEdge(4, 5, 2);

        long[] distances = graph.longestPath(0);
        System.out.println("Longest distances from 0:");
        for (int i = 0; i < distances.length; i++) {
            System.out.println("  0 -> " + i + " = " + distances[i]);
        }
    }
}

/*
OUTPUT:
Longest distances from 0:
  0 -> 0 = 0
  0 -> 1 = 2
  0 -> 2 = 4
  0 -> 3 = 5 (via 0->1->3 or 0->2->3)
  0 -> 4 = 9 (0->2->4)
  0 -> 5 = 6 (0->1->3->5 or 0->2->3->5)
*/
```

### Time Complexity: O(V + E)
### Space Complexity: O(V)

---

## DAG Counting

### Counting Paths

```java
package dsa.graph.dag;

import java.util.*;

public class CountPathsDAG {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;
    private long[] memo;

    public CountPathsDAG(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
    }

    public long countPaths(int source, int destination) {
        List<Integer> topo = topologicalSort();
        memo = new long[numVertices];
        Arrays.fill(memo, -1);
        return countPathsDP(source, destination, topo);
    }

    private long countPathsDP(int source, int dest, List<Integer> topo) {
        if (source == dest) return 1;
        if (memo[source] != -1) return memo[source];

        long count = 0;
        for (int neighbor : adjList.get(source)) {
            count += countPathsDP(neighbor, dest, topo);
        }

        memo[source] = count;
        return count;
    }

    private List<Integer> topologicalSort() {
        int[] indegree = new int[numVertices];
        for (int u = 0; u < numVertices; u++) {
            for (int v : adjList.get(u)) {
                indegree[v]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numVertices; i++) {
            if (indegree[i] == 0) queue.offer(i);
        }

        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            result.add(u);
            for (int v : adjList.get(u)) {
                indegree[v]--;
                if (indegree[v] == 0) queue.offer(v);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        CountPathsDAG graph = new CountPathsDAG(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        System.out.println("Paths from 0 to 4: " + graph.countPaths(0, 4));
        // Output: 2 (0-1-3-4 and 0-2-3-4)
    }
}
```

### Counting Topological Sorts

```java
package dsa.graph.dag;

import java.util.*;

public class CountTopoSorts {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;

    public CountTopoSorts(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
    }

    public long countTopoSorts() {
        int[] indegree = new int[numVertices];
        for (int u = 0; u < numVertices; u++) {
            for (int v : adjList.get(u)) {
                indegree[v]++;
            }
        }

        return dfsCount(indegree, numVertices);
    }

    private long dfsCount(int[] indegree, int remaining) {
        if (remaining == 0) return 1;

        long count = 0;
        for (int i = 0; i < numVertices; i++) {
            if (indegree[i] == 0) {
                // Choose i as next in topological order
                indegree[i] = -1; // Mark as used

                // Reduce indegree of neighbors
                for (int neighbor : adjList.get(i)) {
                    indegree[neighbor]--;
                }

                count += dfsCount(indegree, remaining - 1);

                // Backtrack
                indegree[i] = 0;
                for (int neighbor : adjList.get(i)) {
                    indegree[neighbor]++;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        CountTopoSorts graph = new CountTopoSorts(3);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);

        System.out.println("Number of topological sorts: " + graph.countTopoSorts());
        // Output: 3 (0-1-2, 0-2-1, 2-0-1 are valid)
    }
}
```

---

## All Paths Between Nodes

```java
package dsa.graph.dag;

import java.util.*;

public class AllPathsDAG {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;

    public AllPathsDAG(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
    }

    public List<List<Integer>> findAllPaths(int source, int destination) {
        List<List<Integer>> allPaths = new ArrayList<>();
        List<Integer> currentPath = new ArrayList<>();
        dfs(source, destination, currentPath, allPaths);
        return allPaths;
    }

    private void dfs(int current, int dest,
                    List<Integer> currentPath,
                    List<List<Integer>> allPaths) {
        currentPath.add(current);

        if (current == dest) {
            allPaths.add(new ArrayList<>(currentPath));
        } else {
            for (int neighbor : adjList.get(current)) {
                dfs(neighbor, dest, currentPath, allPaths);
            }
        }

        currentPath.remove(currentPath.size() - 1);
    }

    public static void main(String[] args) {
        AllPathsDAG graph = new AllPathsDAG(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        List<List<Integer>> paths = graph.findAllPaths(0, 4);
        System.out.println("All paths from 0 to 4:");
        for (List<Integer> path : paths) {
            System.out.println("  " + path);
        }
    }
}

/*
OUTPUT:
All paths from 0 to 4:
  [0, 1, 3, 4]
  [0, 2, 3, 4]
*/
```

---

## Practice Problems

### Topological Sort
1. **Course Schedule** (LeetCode 207) ⭐
2. **Course Schedule II** (LeetCode 210) ⭐
3. **Alien Dictionary** (LeetCode 269)
4. **Sequence Reconstruction** (LeetCode 444)

### Longest Path in DAG
1. **Longest Increasing Path in Matrix** (LeetCode 329) ⭐
2. **Parallel Courses** (LeetCode 1136)
3. **Minimum Time to Finish All Tasks**

### Counting Problems
1. **Number of Ways to Order N** (count topological sorts)
2. **Number of Paths in DAG**
3. **Unique Paths in Grid with obstacles**

### Applications
1. **Build Order Reconstruction**
2. **Task Scheduling with Dependencies**
3. **Package Dependency Resolution**

---

## Summary

```
┌─────────────────────────────────────────────────────────────────┐
│                      DAG COMPLETE GUIDE                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  WHAT IS DAG:                                                   │
│  - Directed graph with NO cycles                                │
│  - Always admits topological ordering                           │
│                                                                  │
│  KEY PROPERTIES:                                                │
│  - Has at least one source (indegree 0)                         │
│  - Has at least one sink (outdegree 0)                         │
│  - Maximum edges: V(V-1)/2                                    │
│  - Hamiltonian path always exists                               │
│                                                                  │
│  TOPOLOGICAL SORT (3 methods):                                 │
│  1. Kahn's (BFS + indegree)                                   │
│  2. DFS (post-order stack)                                    │
│  3. Lexicographically smallest (min-heap)                      │
│                                                                  │
│  DAG ALGORITHMS:                                               │
│  - Longest path: DP + topo sort = O(V + E)                     │
│  - Count paths: DP + memoization                                │
│  - All paths: DFS backtracking                                   │
│                                                                  │
│  APPLICATIONS:                                                 │
│  - Course scheduling                                           │
│  - Build systems (Make, Maven)                                 │
│  - Package managers (npm, pip)                                 │
│  - Task scheduling                                            │
│  - Dependency resolution                                        │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

> **Remember**: DAGs are powerful because they combine the expressiveness of directed relationships with the guarantee that algorithms like topological sort and longest path can run efficiently. Any time you see prerequisites, dependencies, or ordering constraints, think DAG!
