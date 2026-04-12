# Bellman-Ford Algorithm - Complete Guide

## Prerequisites & Related Topics

- **Prerequisite**: [04_dijkstra.md](04_dijkstra.md) (understand basic shortest path concept)
- **When to Use Over Dijkstra**: Graphs with **negative edge weights** or when you need **negative cycle detection**
- **Optimization**: SPFA (Shortest Path Faster Algorithm) - uses queue instead of O(VE) iteration
- **Related**: [10_floyd_warshall.md](10_floyd_warshall.md) (all-pairs with negative weights)

## Table of Contents
1. [What is Bellman-Ford?](#what-is-bellman-ford)
2. [Why Bellman-Ford?](#why-bellman-ford)
3. [Algorithm Steps](#algorithm-steps)
4. [Java Implementation](#java-implementation)
5. [Negative Cycle Detection](#negative-cycle-detection)
6. [Comparison with Dijkstra](#comparison-with-dijkstra)
7. [Practice Problems](#practice-problems)

---

## What is Bellman-Ford?

**Bellman-Ford Algorithm** finds the **shortest path** from a source vertex to all other vertices in a **weighted graph**. Unlike Dijkstra, it **handles negative edge weights** and can **detect negative cycles**.

### Key Characteristics

```
┌─────────────────────────────────────────────────────────────────┐
│                  BELLMAN-FORD ALGORITHM                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ✅ FINDS: Shortest path from source to ALL vertices           │
│  ✅ WORKS: Any weighted graph (positive or negative weights)   │
│  ✅ DETECTS: Negative cycles in the graph                     │
│  ✅ TIME:  O(V × E)                                          │
│  ✅ SPACE: O(V)                                                │
│                                                                  │
│  ⚠️ SLOWER than Dijkstra for non-negative weights            │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Visual Example

```
GRAPH:
         0
        /│╲
      -1│ │2
      /  │  ╲
     1   2   3
     │  ╱│   │
    -2  1  -1
     │ ╱ │   │
     2  3  4

Question: Find shortest path from 0 to all vertices

Initial distances:
  dist[0] = 0
  dist[1] = ∞
  dist[2] = ∞
  dist[3] = ∞
  dist[4] = ∞

Step 1: Relax all edges (0 to 4 iterations)
  After 1st pass:
    dist[1] = min(∞, 0 + (-1)) = -1
    dist[2] = min(∞, 0 + 2) = 2
    dist[3] = min(∞, 0 + 2) = 2
    
  After 2nd pass:
    dist[2] = min(2, -1 + 1) = 0
    dist[4] = min(∞, 2 + (-1)) = 1
    
  After 3rd pass:
    dist[4] = min(1, 0 + 1) = 1 (no change)

Final shortest distances from 0:
  0 → 0 = 0
  0 → 1 = -1
  0 → 2 = 0
  0 → 3 = 2
  0 → 4 = 1
```

---

## Why Bellman-Ford?

### What Dijkstra Can't Do

```java
/*
 * DIJKSTRA FAILS WITH NEGATIVE WEIGHTS:
 *
 *         0
 *        /│╲
 *      -2│ │4
 *      /  │  ╲
 *     1   1   2
 *
 * Dijkstra from 0:
 *   dist[1] = -2 (settled!)
 *   dist[2] = min(1, -2 + 1) = -1 (would be updated)
 *   dist[3] = 4
 *
 * BUT the path 0→1→3 = -2 + 3 = 1 is shorter than 4!
 * Dijkstra might not discover this correctly.
 *
 * BELLMAN-FORD handles it correctly by considering ALL paths.
 */
```

### The Key Insight

```java
/*
 * KEY INSIGHT: Shortest path has at most V-1 edges
 *
 * Why V-1 edges max?
 * - A simple path (no cycles) has at most V-1 edges
 * - Any path with more edges must have a cycle
 * - If the cycle has negative weight, it can be repeated infinitely
 *   (which Bellman-Ford detects!)
 *
 * Algorithm:
 * 1. Relax ALL edges, V-1 times
 * 2. After V-1 passes, distances are optimized
 * 3. One more pass to detect negative cycles
 */
```

---

## Algorithm Steps

### Pseudocode

```java
BELLMAN_FORD(graph, source):
    // Initialize
    for each vertex v:
        dist[v] = INFINITY
        prev[v] = UNDEFINED
    dist[source] = 0

    // Relax all edges V-1 times
    for i = 1 to V - 1:
        for each edge (u, v, w) in graph:
            if dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                prev[v] = u

    // Check for negative cycles
    for each edge (u, v, w) in graph:
        if dist[u] + w < dist[v]:
            return "Negative cycle detected!"

    return dist, prev
```

### Step-by-Step Dry Run

```java
/*
GRAPH:
    0 ───1─── 1
    │╲       /│
   4│  ╲3   2│
    │    ╲  │3
    │      ╲│
    2 ───── 3
        2

Edges: (0,1,4), (0,2,4), (0,3,1), (1,3,3), (2,3,2), (1,2,1)

Starting from vertex 0:

INITIAL:
  dist[0] = 0
  dist[1] = ∞
  dist[2] = ∞
  dist[3] = ∞

PASS 1:
  Relax (0,1,4): dist[1] = min(∞, 0+4) = 4
  Relax (0,2,4): dist[2] = min(∞, 0+4) = 4
  Relax (0,3,1): dist[3] = min(∞, 0+1) = 1
  Relax (1,3,3): dist[3] = min(1, 4+3) = 1
  Relax (2,3,2): dist[3] = min(1, 4+2) = 1
  Relax (1,2,1): dist[2] = min(4, 4+1) = 4

PASS 2:
  No updates! Distances already optimal.

PASS 3 (V-1 = 4, so 3 more passes, but no changes):
  No updates.

RESULT:
  dist = [0, 4, 4, 1]
  Shortest paths: 0→0=0, 0→1=4, 0→2=4, 0→3=1
*/
```

---

## Java Implementation

### Basic Bellman-Ford

```java
package dsa.graph.shortestpath;

import java.util.*;

public class BellmanFord {
    private List<Edge> edges;
    private int numVertices;

    public static class Edge {
        int source;
        int destination;
        int weight;
        Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    public BellmanFord(int numVertices) {
        this.numVertices = numVertices;
        this.edges = new ArrayList<>();
    }

    public void addEdge(int source, int destination, int weight) {
        edges.add(new Edge(source, destination, weight));
    }

    public int[] bellmanFord(int source) {
        int[] dist = new int[numVertices];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        // Relax all edges V-1 times
        for (int i = 0; i < numVertices - 1; i++) {
            boolean updated = false;
            for (Edge edge : edges) {
                if (dist[edge.source] != Integer.MAX_VALUE &&
                    dist[edge.source] + edge.weight < dist[edge.destination]) {
                    dist[edge.destination] = dist[edge.source] + edge.weight;
                    updated = true;
                }
            }
            // Early termination if no updates
            if (!updated) break;
        }

        return dist;
    }

    // CORRECTED: hasNegativeCycle() needs dist[] array as parameter
    // (The original implementation tried to use 'dist' which is not a class field)
    public boolean hasNegativeCycle(int[] dist) {
        for (Edge edge : edges) {
            if (dist[edge.source] != Integer.MAX_VALUE &&
                dist[edge.source] + edge.weight < dist[edge.destination]) {
                return true;  // Negative cycle found
            }
        }
        return false;
    }

    public static void main(String[] args) {
        BellmanFord graph = new BellmanFord(5);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 3, 3);
        graph.addEdge(2, 1, 1);
        graph.addEdge(2, 3, 4);
        graph.addEdge(3, 4, 2);

        int[] distances = graph.bellmanFord(0);

        System.out.println("Shortest distances from vertex 0:");
        for (int i = 0; i < distances.length; i++) {
            System.out.println("  0 → " + i + " = " + distances[i]);
        }
    }
}
```

### Bellman-Ford with Path Reconstruction

```java
package dsa.graph.shortestpath;

import java.util.*;

public class BellmanFordPath {
    private List<Edge> edges;
    private int numVertices;

    public static class Edge {
        int source;
        int destination;
        int weight;
        Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    public BellmanFordPath(int numVertices) {
        this.numVertices = numVertices;
        this.edges = new ArrayList<>();
    }

    public void addEdge(int source, int destination, int weight) {
        edges.add(new Edge(source, destination, weight));
    }

    public Result bellmanFord(int source) {
        int[] dist = new int[numVertices];
        int[] parent = new int[numVertices];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[source] = 0;

        // Relax all edges V-1 times
        for (int i = 0; i < numVertices - 1; i++) {
            boolean updated = false;
            for (Edge edge : edges) {
                if (dist[edge.source] != Integer.MAX_VALUE &&
                    dist[edge.source] + edge.weight < dist[edge.destination]) {
                    dist[edge.destination] = dist[edge.source] + edge.weight;
                    parent[edge.destination] = edge.source;
                    updated = true;
                }
            }
            if (!updated) break;
        }

        return new Result(dist, parent);
    }

    public boolean hasNegativeCycle(Result result) {
        for (Edge edge : edges) {
            if (result.dist[edge.source] != Integer.MAX_VALUE &&
                result.dist[edge.source] + edge.weight < result.dist[edge.destination]) {
                return true;
            }
        }
        return false;
    }

    public List<Integer> getPath(int source, int destination, Result result) {
        if (result.dist[destination] == Integer.MAX_VALUE) {
            return Collections.emptyList();
        }

        List<Integer> path = new ArrayList<>();
        int current = destination;
        while (current != -1) {
            path.add(current);
            current = result.parent[current];
        }
        Collections.reverse(path);
        return path;
    }

    public static class Result {
        int[] dist;
        int[] parent;
        Result(int[] dist, int[] parent) {
            this.dist = dist;
            this.parent = parent;
        }
    }

    public static void main(String[] args) {
        BellmanFordPath graph = new BellmanFordPath(5);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 3, 3);
        graph.addEdge(2, 1, 1);
        graph.addEdge(2, 3, 4);
        graph.addEdge(3, 4, 2);

        Result result = graph.bellmanFord(0);

        System.out.println("Shortest paths from vertex 0:");
        for (int i = 0; i < 5; i++) {
            List<Integer> path = graph.getPath(0, i, result);
            System.out.println("  0 → " + i + " = " + result.dist[i] + " via " + path);
        }
    }
}
```

---

## Negative Cycle Detection

### What is a Negative Cycle?

```java
/*
 * NEGATIVE CYCLE:
 * A cycle where the sum of edge weights is negative
 *
 * Example:
 *
 *     1 ──(-2)── 2
 *     ↑          │
 *     │          │
 *     └──(1)─────┘
 *
 * Cycle: 1→2→1 = -2 + 1 = -1 (negative!)
 *
 * If a negative cycle is reachable from source,
 * shortest path is undefined (can be -∞)
 */

public boolean detectNegativeCycle() {
    // Run V-1 relaxations
    for (int i = 0; i < numVertices - 1; i++) {
        for (Edge edge : edges) {
            if (dist[edge.source] != Integer.MAX_VALUE &&
                dist[edge.source] + edge.weight < dist[edge.destination]) {
                dist[edge.destination] = dist[edge.source] + edge.weight;
            }
        }
    }

    // One more relaxation to detect negative cycle
    for (Edge edge : edges) {
        if (dist[edge.source] != Integer.MAX_VALUE &&
            dist[edge.source] + edge.weight < dist[edge.destination]) {
            return true;  // Negative cycle exists!
        }
    }
    return false;
}
```

### Problem with Negative Cycles

```java
/*
 * WHY NEGATIVE CYCLES ARE A PROBLEM:
 *
 * If a negative cycle exists on a path from source to destination,
 * you can loop around the negative cycle infinitely to get
 * arbitrarily small distances (-infinity).
 *
 * Example:
 *     Source ─── 0 ─── 1 ─── Destination
 *                      ↓
 *                      2 ←──(negative cycle)──→
 *
 * You can go: Source → 0 → 1 → 2 → 1 → 2 → 1 → ... → Destination
 * Each loop around the negative cycle makes the path shorter!
 *
 * Bellman-Ford detects this but can't compute a finite shortest path.
 */
```

---

## Comparison with Dijkstra

| Aspect | Bellman-Ford | Dijkstra |
|--------|--------------|----------|
| **Time Complexity** | O(V × E) | O((V+E) log V) |
| **Space Complexity** | O(V) | O(V) |
| **Negative Weights** | ✅ Handles | ❌ Doesn't handle |
| **Negative Cycles** | ✅ Detects | ❌ Not detected |
| **Speed** | Slower | Faster |

### When to Use Which

```
✅ USE BELLMAN-FORD WHEN:
   - Graph has negative edge weights
   - Need to detect negative cycles
   - Simpler implementation is preferred
   - Graph is small or dense

✅ USE DIJKSTRA WHEN:
   - All edge weights are non-negative
   - Need faster performance
   - Graph is large and sparse
```

---

## Practice Problems

### Easy
1. **Detect negative cycle in graph**
2. **Shortest path with negative weights**

### Medium
1. **Cheapest Flights Within K Stops** (LeetCode 787)
2. **Network Delay Time** (LeetCode 743) - with negative weights

### Hard
1. **Find the safest path** (with risk scores)
2. **Arbitrage Detection** (currency conversion)

---

## Summary

```
┌─────────────────────────────────────────────────────────────────┐
│                   BELLMAN-FORD ALGORITHM                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  🎯 PURPOSE: Shortest path with negative weights               │
│                                                                  │
│  ⏱️ TIME: O(V × E)                                            │
│  💾 SPACE: O(V)                                                │
│                                                                  │
│  ✅ HANDLES: Negative edge weights                              │
│  ✅ DETECTS: Negative cycles                                    │
│                                                                  │
│  📝 STEPS:                                                      │
│     1. Initialize distances to ∞, source to 0                  │
│     2. Relax all edges V-1 times                               │
│     3. One more pass to detect negative cycles                  │
│                                                                  │
│  ⚠️ SLOWER than Dijkstra for non-negative weights            │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

> **Remember**: Bellman-Ford is the workhorse for shortest path with any weights. It's slower than Dijkstra but handles negative weights and detects negative cycles. Use it when you need these features!
