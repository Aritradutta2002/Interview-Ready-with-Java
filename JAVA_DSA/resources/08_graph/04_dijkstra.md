# Dijkstra's Algorithm - Complete Guide

## Table of Contents
1. [What is Dijkstra's Algorithm?](#what-is-dijkstras-algorithm)
2. [How Dijkstra's Works](#how-dijkstras-works)
3. [Algorithm Steps](#algorithm-steps)
4. [Java Implementation](#java-implementation)
5. [Optimized Implementation with Priority Queue](#optimized-implementation-with-priority-queue)
6. [Path Reconstruction](#path-reconstruction)
7. [Handling Negative Weights](#handling-negative-weights)
8. [Common Applications](#common-applications)
9. [Dijkstra vs Other Algorithms](#dijkstra-vs-other-algorithms)
10. [Practice Problems](#practice-problems)

---

## What is Dijkstra's Algorithm?

**Dijkstra's Algorithm** finds the **shortest path** from a source vertex to all other vertices in a **weighted graph** with **non-negative edge weights**.

### Key Characteristics

```
┌─────────────────────────────────────────────────────────────────┐
│                  DIJKSTRA'S ALGORITHM                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ✅ FINDS: Shortest path from source to ALL vertices           │
│  ✅ WORKS: Weighted graphs with non-negative weights            │
│  ✅ TIME:  O((V + E) log V) with priority queue               │
│  ✅ SPACE: O(V)                                                │
│                                                                  │
│  ❌ DOESN'T WORK: Graphs with negative edge weights            │
│  ❌ DOESN'T WORK: Graphs with negative cycles                  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Visual Example

```
GRAPH:
              10
         0 -------- 1
         │╲         │
        5│  ╲8     │15
         │    ╲    │
         │      ╲  │
         2--------3 │
           7    4  2
              20

Question: Find shortest path from 0 to all vertices

Initial distances from 0:
  dist[0] = 0 (source)
  dist[1] = 10 (direct)
  dist[2] = 5 (direct)
  dist[3] = 8 (direct)
  dist[4] = ∞ (unreachable initially)

Step 1: Pick vertex with minimum distance = 2 (distance 5)
  Update neighbors of 2:
  dist[3] = min(8, 5 + 7) = 8 (no update)

Step 2: Pick vertex with minimum distance = 1 (distance 10)
  Update neighbors of 1:
  dist[3] = min(8, 10 + 15) = 8 (no update)

Step 3: Pick vertex with minimum distance = 3 (distance 8)
  Update neighbors of 3:
  dist[4] = min(∞, 8 + 2) = 10

Final shortest distances from 0:
  0 → 0 = 0
  0 → 1 = 10
  0 → 2 = 5
  0 → 3 = 8
  0 → 4 = 10
```

---

## How Dijkstra's Works

### The Greedy Approach

```java
/*
 * DIJKSTRA'S GREEDY APPROACH:
 *
 * At each step, choose the UNVISITED vertex with the SMALLEST distance
 * This vertex is now "settled" with its final shortest distance
 *
 * Then update distances to all unvisited neighbors
 *
 * Why it works:
 * - Once a vertex is settled, no shorter path can be found
 * - Because all edge weights are non-negative
 * - Any other path would have to go through an unvisited vertex first
 * - But all unvisited vertices have distance >= current vertex
 */

Step-by-step:

    UNVISITED: {0, 1, 2, 3, 4}
    DISTANCES: {0: 0, 1: 10, 2: 5, 3: 8, 4: ∞}
    SETTLED:   {}

Step 1: Pick minimum = 0 (distance 0)
    SETTLED: {0}
    UNVISITED: {1, 2, 3, 4}

Step 2: Pick minimum = 2 (distance 5)
    SETTLED: {0, 2}
    UNVISITED: {1, 3, 4}

Step 3: Pick minimum = 3 (distance 8)
    SETTLED: {0, 2, 3}
    UNVISITED: {1, 4}

Step 4: Pick minimum = 1 (distance 10)
    SETTLED: {0, 2, 3, 1}
    UNVISITED: {4}

Step 5: Pick minimum = 4 (distance 10)
    SETTLED: {0, 2, 3, 1, 4}
    UNVISITED: {}

FINAL: All vertices settled!
```

### Why No Negative Weights?

```
PROBLEM WITH NEGATIVE WEIGHTS:

         0
        /│╲
       1 │ 2
      -5 5 -5
      /     ╲
     1───2───3
        2    3

From 0 to 3:
  Direct path: 0 → 3 = 3
  Alternative: 0 → 1 → 2 → 3 = 1 + 2 + 3 = 6

BUT if edge 1→2 has weight -3:
  Alternative: 0 → 1 → 2 → 3 = 1 + (-3) + 3 = 1

Now the alternative path is SHORTER!
But Dijkstra might have already settled vertex 1 before discovering this.

So Dijkstra CAN'T guarantee correctness with negative weights.
Use Bellman-Ford instead!
```

---

## Algorithm Steps

### Pseudocode

```java
DIJKSTRA(graph, source):
    // Initialize
    for each vertex v:
        dist[v] = INFINITY
        prev[v] = UNDEFINED
    dist[source] = 0

    // Priority queue: (distance, vertex)
    pq = new PriorityQueue()
    pq.add((0, source))

    while pq is not empty:
        (d, u) = pq.extractMin()
        if d > dist[u]:  // Skip stale entries
            continue

        for each neighbor v of u:
            alt = dist[u] + weight(u, v)
            if alt < dist[v]:
                dist[v] = alt
                prev[v] = u
                pq.add((alt, v))

    return dist, prev
```

### Step-by-Step Dry Run

```java
/*
GRAPH:
      1
  0 ─── 1
  │╲   4│
 3│  ╲  │2
  │    ╲ │
  2 ─── 3
      1

Starting from vertex 0:

INITIAL:
  dist[0] = 0
  dist[1] = ∞
  dist[2] = ∞
  dist[3] = ∞
  pq = [(0, 0)]

STEP 1:
  Extract: (0, 0)
  neighbors of 0:
    - to 1: alt = 0 + 1 = 1 < ∞, update dist[1] = 1, prev[1] = 0
    - to 2: alt = 0 + 3 = 3 < ∞, update dist[2] = 3, prev[2] = 0
    - to 3: alt = 0 + 4 = 4 < ∞, update dist[3] = 4, prev[3] = 0
  pq = [(1, 1), (3, 2), (4, 3)]

STEP 2:
  Extract: (1, 1) - minimum!
  neighbors of 1:
    - to 3: alt = 1 + 2 = 3 < 4, update dist[3] = 3, prev[3] = 1
  pq = [(3, 2), (3, 3), (4, 3)]

STEP 3:
  Extract: (3, 2) - or (3, 3), tie
  neighbors of 2:
    - to 3: alt = 3 + 1 = 4 >= 3, no update
  pq = [(3, 3), (4, 3)]

STEP 4:
  Extract: (3, 3)
  neighbors of 3:
    - nothing new
  pq = [(4, 3)]

STEP 5:
  Extract: (4, 3) - stale entry, skip

RESULT:
  dist = [0, 1, 3, 3]
  prev = [-, 0, 0, 1]

  Shortest path to 3: 0 → 1 → 3 with distance 3
*/
```

---

## Java Implementation

### Basic Implementation (Without Priority Queue)

```java
package dsa.graph.shortestpath;

import java.util.*;

public class DijkstraBasic {
    private Map<Integer, List<Edge>> adjList;
    private int numVertices;

    public static class Edge {
        int destination;
        int weight;
        Edge(int destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    public DijkstraBasic(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        adjList.get(source).add(new Edge(destination, weight));
    }

    public int[] dijkstra(int source) {
        int[] dist = new int[numVertices];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        boolean[] visited = new boolean[numVertices];

        for (int i = 0; i < numVertices; i++) {
            // Find unvisited vertex with minimum distance
            int u = findMinDistance(dist, visited);
            visited[u] = true;

            // No more reachable vertices
            if (dist[u] == Integer.MAX_VALUE) break;

            // Update distances to neighbors
            for (Edge edge : adjList.get(u)) {
                int v = edge.destination;
                int weight = edge.weight;

                if (!visited[v] && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                }
            }
        }
        return dist;
    }

    private int findMinDistance(int[] dist, boolean[] visited) {
        int minDist = Integer.MAX_VALUE;
        int minVertex = -1;

        for (int v = 0; v < numVertices; v++) {
            if (!visited[v] && dist[v] < minDist) {
                minDist = dist[v];
                minVertex = v;
            }
        }
        return minVertex;
    }

    public static void main(String[] args) {
        DijkstraBasic graph = new DijkstraBasic(5);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 5);
        graph.addEdge(0, 3, 15);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 1, 3);
        graph.addEdge(2, 3, 5);
        graph.addEdge(3, 4, 10);
        graph.addEdge(1, 4, 8);

        int[] distances = graph.dijkstra(0);

        System.out.println("Shortest distances from vertex 0:");
        for (int i = 0; i < distances.length; i++) {
            System.out.println("  0 → " + i + " = " + distances[i]);
        }
        // Expected: 0→0=0, 0→1=8 (via 2), 0→2=5, 0→3=10 (via 1 or 2), 0→4=16 (via 1 or 3)
    }
}
```

### Time Complexity: O(V²)
### Space Complexity: O(V)

---

## Optimized Implementation with Priority Queue

```java
package dsa.graph.shortestpath;

import java.util.*;

public class DijkstraOptimized {
    private Map<Integer, List<Edge>> adjList;
    private int numVertices;

    public static class Edge {
        int destination;
        int weight;
        Edge(int destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    public static class Node implements Comparable<Node> {
        int vertex;
        int distance;
        Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    public DijkstraOptimized(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        adjList.get(source).add(new Edge(destination, weight));
    }

    public int[] dijkstra(int source) {
        int[] dist = new int[numVertices];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(source, 0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int u = current.vertex;

            // Skip stale entries
            if (current.distance > dist[u]) {
                continue;
            }

            // Explore neighbors
            for (Edge edge : adjList.get(u)) {
                int v = edge.destination;
                int newDist = dist[u] + edge.weight;

                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    pq.offer(new Node(v, newDist));
                }
            }
        }
        return dist;
    }

    public static void main(String[] args) {
        DijkstraOptimized graph = new DijkstraOptimized(6);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 8);
        graph.addEdge(2, 4, 10);
        graph.addEdge(3, 4, 2);
        graph.addEdge(3, 5, 6);
        graph.addEdge(4, 5, 3);

        int[] distances = graph.dijkstra(0);

        System.out.println("Shortest distances from vertex 0:");
        for (int i = 0; i < distances.length; i++) {
            System.out.println("  0 → " + i + " = " + distances[i]);
        }
    }
}

/*
OUTPUT:
Shortest distances from vertex 0:
  0 → 0 = 0
  0 → 1 = 4
  0 → 2 = 2
  0 → 3 = 8 (via 1: 4+5=9, via 2: 2+8=10, direct 0→3 not exist)
  Wait, let me recalculate...

Actually shortest paths:
  0 → 0 = 0
  0 → 1 = 4 (direct)
  0 → 2 = 2 (direct)
  0 → 3 = min(0→1→3=9, 0→2→3=10) = 9
  0 → 4 = min(0→2→4=12, 0→3→4=11) = 11
  0 → 5 = min(0→3→5=15, 0→4→5=14) = 14
*/
```

### Time Complexity: O((V + E) log V)
### Space Complexity: O(V)

---

## Path Reconstruction

```java
package dsa.graph.shortestpath;

import java.util.*;

public class DijkstraPath {
    private Map<Integer, List<Edge>> adjList;
    private int numVertices;

    public static class Edge {
        int destination;
        int weight;
        Edge(int destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    public DijkstraPath(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        adjList.get(source).add(new Edge(destination, weight));
    }

    public Result dijkstra(int source) {
        int[] dist = new int[numVertices];
        int[] parent = new int[numVertices];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[source] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(
            Comparator.comparingInt(a -> a[1])
        );
        pq.offer(new int[]{source, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];

            if (current[1] > dist[u]) continue;

            for (Edge edge : adjList.get(u)) {
                int v = edge.destination;
                int newDist = dist[u] + edge.weight;

                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    parent[v] = u;
                    pq.offer(new int[]{v, newDist});
                }
            }
        }
        return new Result(dist, parent);
    }

    public static class Result {
        int[] dist;
        int[] parent;
        Result(int[] dist, int[] parent) {
            this.dist = dist;
            this.parent = parent;
        }
    }

    public List<Integer> getPath(int source, int destination, Result result) {
        List<Integer> path = new ArrayList<>();
        if (result.dist[destination] == Integer.MAX_VALUE) {
            return path;  // No path exists
        }

        int current = destination;
        while (current != -1) {
            path.add(current);
            current = result.parent[current];
        }
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        DijkstraPath graph = new DijkstraPath(5);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 8);
        graph.addEdge(2, 4, 10);
        graph.addEdge(3, 4, 2);

        Result result = graph.dijkstra(0);

        System.out.println("Shortest paths from vertex 0:");
        for (int i = 0; i < 5; i++) {
            List<Integer> path = graph.getPath(0, i, result);
            System.out.println("  0 → " + i + " = " + result.dist[i] + " via " + path);
        }

        /*
        OUTPUT:
        Shortest paths from vertex 0:
          0 → 0 = 0 via [0]
          0 → 1 = 4 via [0, 1]
          0 → 2 = 2 via [0, 2]
          0 → 3 = 9 via [0, 1, 3]
          0 → 4 = 11 via [0, 1, 3, 4]
        */
    }
}
```

---

## Handling Negative Weights

### When Dijkstra Fails

```java
/*
 * DIJKSTRA DOESN'T WORK WITH NEGATIVE WEIGHTS!
 *
 * Example:
 *
 *         0
 *        /│╲
 *       2 │ 2
 *      /   │  \
 *     1    │   1
 *     │    │   │
 *     3    │   2
 *      \   │   /
 *       1──┴──2
 *
 * From 0 to 2:
 *   Direct: 2
 *   Via 1: 1 + 3 = 4 (longer)
 *
 * BUT if edge 1→0 has weight -4:
 *   Via 1: 1 + (-4) + 2 = -1 (SHORTER!)
 *
 * Dijkstra would settle 0 first, then explore neighbors.
 * But the shorter path via 1 requires going through an already-settled vertex.
 *
 * Solution: Use Bellman-Ford for negative weights
 */
```

### Detection of Negative Cycles

```java
package dsa.graph.shortestpath;

public class NegativeCycleDetection {

    public boolean hasNegativeCycle(int numVertices, int[][] edges, int source) {
        int[] dist = new int[numVertices];
        java.util.Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        // Relax all edges V-1 times
        for (int i = 0; i < numVertices - 1; i++) {
            for (int[] edge : edges) {
                int u = edge[0];
                int v = edge[1];
                int w = edge[2];

                if (dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                }
            }
        }

        // Check for negative cycle - one more relaxation
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];

            if (dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v]) {
                return true;  // Negative cycle exists!
            }
        }
        return false;
    }
}
```

---

## Common Applications

### 1. GPS Navigation

```java
/*
 * GPS / Maps Application
 *
 * Vertices: Intersections, locations
 * Edges: Roads with weights = distance/time
 *
 * Dijkstra finds fastest route from current location to destination
 *
 * VARIATIONS:
 * - A* algorithm: Dijkstra + heuristic (like distance to destination)
 * - Real-time traffic: Dynamic edge weights
 */
```

### 2. Network Routing

```java
/*
 * Network Routing (like OSPF, RIP)
 *
 * Vertices: Routers
 * Edges: Network links with weights = latency/bandwidth
 *
 * Dijkstra finds optimal routing path
 */
```

### 3. Flight Reservations

```java
/*
 * Flight Booking System
 *
 * Vertices: Airports
 * Edges: Flights with weights = price/time/stops
 *
 * Dijkstra finds cheapest/shortest route
 */
```

---

## Dijkstra vs Other Algorithms

| Algorithm | Time | Space | Negative Weights | Negative Cycles | Use Case |
|-----------|------|-------|-----------------|-----------------|---------|
| **Dijkstra** | O((V+E) log V) | O(V) | ❌ No | ❌ No | Shortest path (non-negative) |
| **Bellman-Ford** | O(V × E) | O(V) | ✅ Yes | ✅ Detects | Shortest path (any weights) |
| **Floyd-Warshall** | O(V³) | O(V²) | ✅ Yes | ✅ Detects | All pairs shortest path |
| **BFS** | O(V + E) | O(V) | ❌ No | ❌ No | Unweighted shortest path |

### When to Use Dijkstra

```
✅ USE DIJKSTRA WHEN:
   - All edge weights are non-negative
   - Need shortest path from ONE source
   - Graph is large and sparse (E << V²)

❌ DON'T USE DIJKSTRA WHEN:
   - Graph has negative edge weights
   - Need all-pairs shortest path (use Floyd-Warshall)
   - Graph is unweighted (use BFS)
   - Need to detect negative cycles
```

---

## Practice Problems

### Easy
1. **Network Delay Time** (LeetCode 743)
2. **Find the City With the Smallest Number** (LeetCode 1334)
3. **Shortest Path in Binary Matrix** (LeetCode 1293)

### Medium
1. **Cheapest Flights Within K Stops** (LeetCode 787)
2. **Path With Minimum Effort** (LeetCode 1631)
3. **Swim in Rising Water** (LeetCode 778)

### Hard
1. **Minimum Cost to Reach Destination** (Flight scheduling)
2. **Find Shortest Path with Obstacles** (Grid with weights)
3. **Reachable Nodes in Subdivided Graph** (LeetCode 882)

---

## Summary

```
┌─────────────────────────────────────────────────────────────────┐
│                   DIJKSTRA'S ALGORITHM                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  🎯 PURPOSE: Find shortest path from source to all vertices     │
│                                                                  │
│  ⏱️ TIME:                                                      │
│     - Basic: O(V²)                                            │
│     - With PQ: O((V + E) log V)                               │
│                                                                  │
│  💾 SPACE: O(V)                                                 │
│                                                                  │
│  ✅ REQUIREMENTS:                                               │
│     - Non-negative edge weights                                 │
│     - Weighted graph                                            │
│                                                                  │
│  🔧 DATA STRUCTURE: Priority Queue (Min-Heap)                  │
│                                                                  │
│  📝 STEPS:                                                      │
│     1. Initialize distances (source=0, others=∞)               │
│     2. Add source to priority queue                            │
│     3. Extract minimum, update neighbors                         │
│     4. Repeat until queue empty                                │
│                                                                  │
│  ⚠️ LIMITATIONS:                                               │
│     - Doesn't work with negative weights                         │
│     - Doesn't detect negative cycles                             │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

> **Remember**: Dijkstra's algorithm is the go-to algorithm for single-source shortest path in weighted graphs with non-negative weights. Use a priority queue for optimal performance. If you have negative weights, use Bellman-Ford instead!
