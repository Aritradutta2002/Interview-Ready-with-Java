# Maximum Flow & Min Cut - Complete Guide

## Table of Contents
1. [Flow Networks](#flow-networks)
2. [Ford-Fulkerson Method](#ford-fulkerson-method)
3. [Edmonds-Karp Algorithm](#edmonds-karp-algorithm)
4. [Dinic's Algorithm](#dinics-algorithm)
5. [Min Cut Theorem](#min-cut-theorem)
6. [Practice Problems](#practice-problems)

---

## Flow Networks

### What is Maximum Flow?

```
┌─────────────────────────────────────────────────────────────────┐
│                       FLOW NETWORK                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  COMPONENTS:                                                    │
│  - Source (s): Where flow originates                            │
│  - Sink (t): Where flow ends                                    │
│  - Capacity: Maximum flow through an edge                         │
│                                                                  │
│  RULES:                                                         │
│  1. Capacity constraint: 0 ≤ flow ≤ capacity                   │
│  2. Flow conservation: Flow in = Flow out (for intermediate)   │
│                                                                  │
│  GOAL:                                                          │
│  Maximize total flow from source to sink                         │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Visual Example

```
FLOW NETWORK:

        10
    0 ────→ 1
    │╲     ↑╱
   8│  ╲3  │2
    │    ╲  │
    ↓     ╲ ↓
    2 ────→ 3
        7

    Maximum Flow = 11 (0→2→3: 7, 0→1→3: 4)
```

---

## Ford-Fulkerson Method

### The Algorithm

```java
/*
 * FORD-FULKERSON METHOD:
 *
 * 1. While there exists an augmenting path from s to t:
 *    a. Find the minimum residual capacity along path (bottleneck)
 *    b. Increase flow by that amount
 * 2. Return total flow
 *
 * Key insight: Residual graph tracks remaining capacity
 */
```

### Java Implementation

```java
package dsa.graph.flow;

import java.util.*;

public class FordFulkerson {
    private int numVertices;
    private int[][] capacity;
    private int[][] flow;

    public FordFulkerson(int numVertices) {
        this.numVertices = numVertices;
        this.capacity = new int[numVertices][numVertices];
        this.flow = new int[numVertices][numVertices];
    }

    public void addEdge(int from, int to, int cap) {
        capacity[from][to] = cap;
    }

    private boolean bfs(int[] parent, int source, int sink) {
        boolean[] visited = new boolean[numVertices];
        Arrays.fill(visited, false);
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(source);
        visited[source] = true;

        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (int u = 0; u < numVertices; u++) {
                if (!visited[u] && residualCapacity(v, u) > 0) {
                    queue.offer(u);
                    visited[u] = true;
                    parent[u] = v;
                }
            }
        }
        return visited[sink];
    }

    private int residualCapacity(int u, int v) {
        return capacity[u][v] - flow[u][v];
    }

    public int maxFlow(int source, int sink) {
        int[] parent = new int[numVertices];
        int maxFlowValue = 0;

        while (bfs(parent, source, sink)) {
            // Find minimum residual capacity along path
            int pathFlow = Integer.MAX_VALUE;
            int v = sink;
            while (v != source) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, residualCapacity(u, v));
                v = u;
            }

            // Update residual capacities
            v = sink;
            while (v != source) {
                int u = parent[v];
                flow[u][v] += pathFlow;
                flow[v][u] -= pathFlow;  // Reverse edge
                v = u;
            }

            maxFlowValue += pathFlow;
        }

        return maxFlowValue;
    }

    public static void main(String[] args) {
        FordFulkerson ff = new FordFulkerson(6);
        ff.addEdge(0, 1, 16);
        ff.addEdge(0, 2, 13);
        ff.addEdge(1, 2, 10);
        ff.addEdge(1, 3, 12);
        ff.addEdge(2, 1, 4);
        ff.addEdge(2, 4, 14);
        ff.addEdge(3, 2, 9);
        ff.addEdge(3, 5, 20);
        ff.addEdge(4, 3, 7);
        ff.addEdge(4, 5, 4);

        System.out.println("Maximum Flow: " + ff.maxFlow(0, 5));
        // Expected: 23
    }
}
```

### Time Complexity: O(E × max_flow)
### Space Complexity: O(V²)

---

## Edmonds-Karp Algorithm

### What is Edmonds-Karp?

```java
/*
 * EDMONDS-KARP = FORD-FULKERSON + BFS
 *
 * KEY DIFFERENCE:
 * - Uses BFS to find augmenting path
 * - Guarantees O(V × E²) time complexity
 * - Always finds shortest augmenting path (in terms of edges)
 */
```

### Java Implementation

```java
package dsa.graph.flow;

import java.util.*;

public class EdmondsKarp {
    private int numVertices;
    private int[][] capacity;

    public EdmondsKarp(int numVertices) {
        this.numVertices = numVertices;
        this.capacity = new int[numVertices][numVertices];
    }

    public void addEdge(int from, int to, int cap) {
        capacity[from][to] = cap;
    }

    private int bfs(int source, int sink, int[] parent) {
        Arrays.fill(parent, -1);
        parent[source] = source;
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{source, Integer.MAX_VALUE});

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int v = current[0];
            int flow = current[1];

            for (int u = 0; u < numVertices; u++) {
                if (parent[u] == -1 && capacity[v][u] > 0) {
                    parent[u] = v;
                    int newFlow = Math.min(flow, capacity[v][u]);
                    if (u == sink) {
                        return newFlow;
                    }
                    queue.offer(new int[]{u, newFlow});
                }
            }
        }
        return 0;
    }

    public int maxFlow(int source, int sink) {
        int[] parent = new int[numVertices];
        int maxFlowValue = 0;

        while (true) {
            int flow = bfs(source, sink, parent);
            if (flow == 0) break;

            int v = sink;
            while (v != source) {
                int u = parent[v];
                capacity[u][v] -= flow;
                capacity[v][u] += flow;
                v = u;
            }
            maxFlowValue += flow;
        }
        return maxFlowValue;
    }

    public static void main(String[] args) {
        EdmondsKarp ek = new EdmondsKarp(6);
        ek.addEdge(0, 1, 16);
        ek.addEdge(0, 2, 13);
        ek.addEdge(1, 2, 10);
        ek.addEdge(1, 3, 12);
        ek.addEdge(2, 1, 4);
        ek.addEdge(2, 4, 14);
        ek.addEdge(3, 2, 9);
        ek.addEdge(3, 5, 20);
        ek.addEdge(4, 3, 7);
        ek.addEdge(4, 5, 4);

        System.out.println("Maximum Flow: " + ek.maxFlow(0, 5));
    }
}
```

### Time Complexity: O(V × E²)
### Space Complexity: O(V²)

---

## Min Cut Theorem

### What is Min Cut?

```
┌─────────────────────────────────────────────────────────────────┐
│                       MIN CUT THEOREM                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  MINIMUM S-T CUT:                                                │
│  - Partition vertices into S and T (disjoint)                    │
│  - Source in S, Sink in T                                       │
│  - Minimize sum of capacities crossing the cut                    │
│                                                                  │
│  MAX-FLOW MIN-CUT THEOREM:                                      │
│  - Maximum flow value = Minimum cut capacity                      │
│  - Cut separates vertices into reachable from source              │
│    and not reachable in residual graph                           │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Finding the Min Cut

```java
package dsa.graph.flow;

import java.util.*;

public class MinCut {
    private int numVertices;
    private int[][] capacity;

    public MinCut(int numVertices) {
        this.numVertices = numVertices;
        this.capacity = new int[numVertices][numVertices];
    }

    public void addEdge(int from, int to, int cap) {
        capacity[from][to] = cap;
    }

    public int minCut(int source, int sink, Set<Integer> reachable) {
        // After max flow, find reachable vertices from source
        // in residual graph
        boolean[] visited = new boolean[numVertices];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(source);
        visited[source] = true;
        reachable.add(source);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (int u = 0; u < numVertices; u++) {
                if (!visited[u] && capacity[v][u] > 0) {
                    queue.offer(u);
                    visited[u] = true;
                    reachable.add(u);
                }
            }
        }

        // Cut capacity = sum of capacities of edges from S to T
        int cutValue = 0;
        for (int u : reachable) {
            for (int v = 0; v < numVertices; v++) {
                if (!reachable.contains(v)) {
                    cutValue += capacity[u][v];
                }
            }
        }
        return cutValue;
    }

    public static void main(String[] args) {
        MinCut mc = new MinCut(4);
        mc.addEdge(0, 1, 10);
        mc.addEdge(0, 2, 5);
        mc.addEdge(1, 3, 8);
        mc.addEdge(2, 3, 7);

        Set<Integer> reachable = new HashSet<>();
        int cutValue = mc.minCut(0, 3, reachable);
        System.out.println("Min Cut Value: " + cutValue);
        System.out.println("Reachable from source: " + reachable);
    }
}
```

---

## Applications

### 1. Maximum Bipartite Matching

```java
/*
 * BIPARTITE MATCHING AS MAX FLOW:
 *
 * Source → Left Set (capacity 1)
 * Left Set → Right Set (capacity 1 if edge exists)
 * Right Set → Sink (capacity 1)
 *
 * Maximum matching = Maximum flow!
 */
```

### 2. Image Segmentation

```java
/*
 * IMAGE SEGMENTATION AS MAX FLOW:
 *
 * - Pixels as nodes
 * - Source = foreground
 * - Sink = background
 * - Edge capacities = similarity/difference
 */
```

---

## Practice Problems

### Easy
1. **Maximum Number of Acceptance**
2. **Resource Assignment**

### Medium
1. **Maximum Bipartite Matching** (LeetCode 862)
2. **Cat and Dog** (similar to LeetCode 886)

### Hard
1. **Maximum Flow in Network**
2. **Min Cut in Weighted Graph**

---

## Summary

```
┌─────────────────────────────────────────────────────────────────┐
│                    MAXIMUM FLOW & MIN CUT                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  FLOW NETWORK:                                                  │
│  - Source (s), Sink (t), Capacities                           │
│  - Capacity: 0 ≤ flow ≤ cap                                   │
│  - Conservation: flow in = flow out                            │
│                                                                  │
│  FORD-FULKERSON:                                               │
│  - O(E × max_flow)                                           │
│  - Any augmenting path works                                   │
│                                                                  │
│  EDMONDS-KARP:                                                 │
│  - O(V × E²)                                                │
│  - BFS for augmenting paths (shortest first)                   │
│                                                                  │
│  MAX-FLOW MIN-CUT THEOREM:                                     │
│  - max_flow = min_cut                                         │
│  - After max flow, reachable from source = S side             │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

> **Remember**: Max flow = Min cut! Edmonds-Karp uses BFS for efficiency. These algorithms are powerful for assignment problems, image segmentation, and bipartite matching!
