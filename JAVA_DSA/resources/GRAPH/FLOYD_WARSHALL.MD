# Floyd-Warshall Algorithm - Complete Guide

## Prerequisites & Related Topics

- **Single-Source Shortest Path**: [04_dijkstra.md](04_dijkstra.md) (non-negative weights), [05_bellman_ford.md](05_bellman_ford.md) (negative weights)
- **When to Use Floyd-Warshall**: Need ALL-PAIRS shortest path (not just single source)
- **Trade-offs**: 
  - Dense graphs (E ≈ V²): Floyd-Warshall O(V³) often faster than running Dijkstra V times
  - Sparse graphs (E << V²): Run Dijkstra V times is better
  - Small graphs: Floyd-Warshall more practical
- **Negative Cycle Detection**: Built-in via negative values on diagonal

## Table of Contents
1. [What is Floyd-Warshall?](#what-is-floyd-warshall)
2. [Algorithm](#algorithm)
3. [Java Implementation](#java-implementation)
4. [Path Reconstruction](#path-reconstruction)
5. [Applications](#applications)
6. [Practice Problems](#practice-problems)

---

## What is Floyd-Warshall?

**Floyd-Warshall** finds the **shortest path between all pairs** of vertices in a weighted graph. Unlike Dijkstra (single-source), it computes shortest paths from **every vertex to every other vertex**.

```
┌─────────────────────────────────────────────────────────────────┐
│                   FLOYD-WARSHALL ALGORITHM                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ✅ FINDS: Shortest path between ALL pairs                   │
│  ✅ WORKS: Directed or undirected, positive or negative weights  │
│  ✅ DETECTS: Negative cycles                                    │
│  ✅ TIME:  O(V³)                                              │
│  ✅ SPACE: O(V²)                                               │
│                                                                  │
│  ⚠️ SLOW for very large graphs (V > 1000)                    │
│  ✅ FAST for small/medium graphs                                 │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Algorithm

### The Algorithm

```java
/*
 * FLOYD-WARSHALL ALGORITHM:
 *
 * Uses Dynamic Programming approach
 *
 * Key Idea:
 * - dist[i][j] = shortest path from i to j
 * - Consider each vertex k as intermediate point
 * - dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])
 *
 * For all k from 1 to V:
 *   For all i from 1 to V:
 *     For all j from 1 to V:
 *       if dist[i][k] + dist[k][j] < dist[i][j]:
 *         dist[i][j] = dist[i][k] + dist[k][j]
 *
 * This is O(V³) time complexity
 */
```

### Step-by-Step Example

```java
/*
GRAPH:
    0 ───2─── 1
    │╲       ╱│
    1│  ╲4  3│
    │    ╲  │
    │     ╲ │
    2 ───3─── 3

Initial Distance Matrix:
      0   1   2   3
    ┌────────────────
  0 │  0   2   1   4
  1 │  2   0   3   3
  2 │  1   3   0   2
  3 │  4   3   2   0

Step 1: Consider k=0 as intermediate
  dist[1][2] = min(dist[1][2], dist[1][0] + dist[0][2]) = min(3, 2+1) = 3
  dist[2][1] = min(dist[2][1], dist[2][0] + dist[0][1]) = min(3, 1+2) = 3

Step 2: Consider k=1 as intermediate
  dist[0][2] = min(dist[0][2], dist[0][1] + dist[1][2]) = min(1, 2+3) = 1
  ... and so on

Final Distance Matrix (all shortest paths):
      0   1   2   3
    ┌────────────────
  0 │  0   2   1   3
  1 │  2   0   2   3
  2 │  1   2   0   2
  3 │  3   3   2   0
*/
```

---

## Java Implementation

### Basic Implementation

```java
package dsa.graph.allpairs;

import java.util.*;

public class FloydWarshall {
    private int[][] dist;
    private int numVertices;
    private static final int INF = Integer.MAX_VALUE / 2;

    public FloydWarshall(int numVertices) {
        this.numVertices = numVertices;
        this.dist = new int[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++) {
            Arrays.fill(dist[i], INF);
            dist[i][i] = 0;
        }
    }

    public void addEdge(int source, int destination, int weight) {
        dist[source][destination] = weight;
    }

    public int[][] floydWarshall() {
        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
        return dist;
    }

    public boolean hasNegativeCycle() {
        for (int i = 0; i < numVertices; i++) {
            if (dist[i][i] < 0) {
                return true;
            }
        }
        return false;
    }

    public void printDistances() {
        System.out.println("All Pairs Shortest Distances:");
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (dist[i][j] == INF) {
                    System.out.print("INF ");
                } else {
                    System.out.print(dist[i][j] + "   ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        FloydWarshall fw = new FloydWarshall(4);
        fw.addEdge(0, 1, 2);
        fw.addEdge(0, 2, 1);
        fw.addEdge(0, 3, 4);
        fw.addEdge(1, 2, 3);
        fw.addEdge(1, 3, 3);
        fw.addEdge(2, 3, 2);

        fw.floydWarshall();
        fw.printDistances();

        if (fw.hasNegativeCycle()) {
            System.out.println("Negative cycle detected!");
        }
    }
}

/*
OUTPUT:
All Pairs Shortest Distances:
0   2   1   3
2   0   2   3
1   2   0   2
3   3   2   0
*/
```

### Time Complexity: O(V³)
### Space Complexity: O(V²)

---

## Path Reconstruction

### With Next Node Array

```java
package dsa.graph.allpairs;

import java.util.*;

public class FloydWarshallPath {
    private int[][] dist;
    private int[][] next;
    private int numVertices;
    private static final int INF = Integer.MAX_VALUE / 2;

    public FloydWarshallPath(int numVertices) {
        this.numVertices = numVertices;
        this.dist = new int[numVertices][numVertices];
        this.next = new int[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++) {
            Arrays.fill(dist[i], INF);
            Arrays.fill(next[i], -1);
            dist[i][i] = 0;
        }
    }

    public void addEdge(int source, int destination, int weight) {
        dist[source][destination] = weight;
        next[source][destination] = destination;
    }

    public void floydWarshall() {
        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
    }

    public List<Integer> getPath(int source, int destination) {
        if (next[source][destination] == -1) {
            return Collections.emptyList();
        }

        List<Integer> path = new ArrayList<>();
        path.add(source);
        int current = source;

        while (current != destination) {
            current = next[current][destination];
            path.add(current);
        }
        return path;
    }

    public static void main(String[] args) {
        FloydWarshallPath fw = new FloydWarshallPath(4);
        fw.addEdge(0, 1, 2);
        fw.addEdge(0, 2, 1);
        fw.addEdge(0, 3, 4);
        fw.addEdge(1, 2, 3);
        fw.addEdge(1, 3, 3);
        fw.addEdge(2, 3, 2);

        fw.floydWarshall();

        System.out.println("Path from 0 to 3: " + fw.getPath(0, 3));
        System.out.println("Path from 1 to 0: " + fw.getPath(1, 0));
        System.out.println("Path from 2 to 1: " + fw.getPath(2, 1));
    }
}
```

---

## Applications

### 1. Transitive Closure

```java
/*
 * Floyd-Warshall can find reachability:
 * - dist[i][j] < INF means j is reachable from i
 *
 * Useful for: Finding if path exists between any two vertices
 */
```

### 2. Finding Diameter of Graph

```java
/*
 * Diameter = maximum shortest path distance between any two vertices
 * Floyd-Warshall makes this easy:
 * diameter = max(dist[i][j]) for all i, j
 */
```

### 3. Negative Cycle Detection

```java
/*
 * If dist[i][i] < 0 after running Floyd-Warshall,
 * there's a negative cycle reachable from i
 *
 * In currency exchange: arbitrage opportunity!
 */
```

---

## Practice Problems

### Easy
1. **Find the City With the Smallest Number** (LeetCode 1334)
2. **Network Delay Time** (can use FW for small graphs)

### Medium
1. **Detect Negative Cycle** (all pairs)
2. **Minimum Path Sum** (LeetCode 64)

### Hard
1. **Maximum Profit From Transactions** (FW for small K)
2. **Critical Connections** (LeetCode 1192)

---

## Summary

```
┌─────────────────────────────────────────────────────────────────┐
│                   FLOYD-WARSHALL ALGORITHM                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  🎯 PURPOSE: All pairs shortest paths                         │
│                                                                  │
│  ⏱️ TIME: O(V³)                                              │
│  💾 SPACE: O(V²)                                               │
│                                                                  │
│  ✅ HANDLES: Positive, negative weights, cycles                 │
│  ✅ FINDS: Shortest path from every vertex to every vertex     │
│  ❌ SLOW for: Large graphs (V > 1000)                        │
│                                                                  │
│  📝 ALGORITHM:                                                 │
│     For each k as intermediate:                               │
│       For each i:                                              │
│         For each j:                                            │
│           dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])│
│                                                                  │
│  ⚠️ KEY INSIGHT:                                                │
│     - If dist[i][i] < 0 after algorithm, negative cycle exists  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

> **Remember**: Floyd-Warshall computes ALL pairs shortest paths in O(V³). Perfect for small graphs where you need distances between all pairs. For single-source, use Dijkstra or Bellman-Ford instead!
