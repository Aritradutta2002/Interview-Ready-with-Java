# Bridges & Articulation Points - Complete Guide

## Table of Contents
1. [What are Bridges?](#what-are-bridges)
2. [What are Articulation Points?](#what-are-articulation-points)
3. [Tarjan's Algorithm](#tarjans-algorithm)
4. [Java Implementation](#java-implementation)
5. [Why They Matter](#why-they-matter)
6. [Applications](#applications)
7. [Practice Problems](#practice-problems)

---

## What are Bridges?

### Definition

```
┌─────────────────────────────────────────────────────────────────┐
│                         BRIDGE (CUT EDGE)                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  DEFINITION:                                                    │
│  An edge in a graph whose REMOVAL increases the number of          │
│  connected components (i.e., disconnects the graph)              │
│                                                                  │
│  EQUIVALENT:                                                    │
│  An edge (u, v) is a bridge if and only if:                    │
│  - There is NO other path between u and v EXCEPT this edge      │
│  - u and v are in different DFS subtrees                        │
│                                                                  │
│  GRAPH STATE:                                                   │
│  BEFORE removing bridge:                                         │
│      u ───── v                                                  │
│      │         │                                                │
│      │         │                                                │
│      ▼         ▼                                                │
│   [connected]                                                   │
│                                                                  │
│  AFTER removing bridge:                                         │
│      u         v                                               │
│      │         │                                                │
│      ▼         ▼                                                │
│   [disconnected]                                                │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Visual Examples

```
GRAPH 1 - HAS BRIDGES:

        A ─── B ─── C ─── D
        │           │
        │           │
        E ──────────

Bridges: (A-B), (B-C), (C-D)
If you remove (B-C), the graph splits into two components:
  Component 1: A, B, E
  Component 2: C, D


GRAPH 2 - NO BRIDGES (has cycles):

        A ─── B
        │╲    │
        │  ╲  │
        │    ╲│
        C ───── D

No bridges! Every edge is part of a cycle.
Removing any edge still keeps the graph connected.
```

---

## What are Articulation Points?

### Definition

```
┌─────────────────────────────────────────────────────────────────┐
│                    ARTICULATION POINT                             │
│                    (CUT VERTEX)                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  DEFINITION:                                                    │
│  A vertex in a graph whose REMOVAL increases the number of       │
│  connected components (disconnects the graph)                    │
│                                                                  │
│  TYPES:                                                         │
│  1. ROOT: If root has 2+ children in DFS tree, it's an         │
│           articulation point (unless it's a leaf)                 │
│                                                                  │
│  2. INTERMEDIATE: If vertex v has a child w where there's       │
│     NO back edge from w (or descendants) to v or ancestors       │
│     of v, then v is an articulation point                       │
│                                                                  │
│  WHY IMPORTANT:                                                 │
│  - Single point of failure in networks                          │
│  - Critical infrastructure nodes                                │
│  - Network reliability analysis                                  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Visual Examples

```
GRAPH WITH ARTICULATION POINTS:

        A
        │
        B ──────── C
        │╲        │
        │  ╲      │
        │    ╲    │
        D      E ──

Articulation Point: B
If you remove B, the graph splits:
  Component 1: A
  Component 2: C, E
  Component 3: D

C is NOT an articulation point because removing C
still leaves E connected to the rest via other paths.


CRITICAL NETWORK TOPOLOGY:

    Server A ─── Router B ─── Router C ─── Server D
                     │
                     │
                  Router E

B is an articulation point! If B fails:
- A becomes isolated
- C and D can still communicate
- E becomes isolated from C/D
```

---

## Tarjan's Algorithm

### The Algorithm

```java
/*
 * TARCAN'S ALGORITHM FOR BRIDGES & ARTICULATION POINTS:
 *
 * Uses DFS with discovery times and low values:
 *
 * - disc[v]: Discovery time of vertex v (when first visited)
 * - low[v]: Lowest discovery time reachable from v's DFS subtree
 *           (including back edges)
 *
 * KEY INSIGHT:
 * - low[v] = min(disc[v], disc[back-edge-target], low[child])
 *
 * BRIDGE CONDITION:
 *   Edge (u, v) where v is a child of u in DFS tree is a BRIDGE if:
 *   low[v] > disc[u]
 *   (No back edge from v's subtree can reach u or above)
 *
 * ARTICULATION POINT CONDITIONS:
 *   1. ROOT: Is root if it has 2+ DFS children
 *   2. NON-ROOT: u is articulation point if it has a child v
 *      where low[v] >= disc[u]
 */
```

### Time & Discovery Values Explained

```
DFS TREE FOR GRAPH:

        0
       /│╲
      / │ ╲
     1  2  3
      \│  │
       4  5
        ╲│/
         6

Assume DFS starts from 0:
disc[0] = 0
disc[1] = 1  (visit 1 first)
disc[4] = 2  (visit 4 from 1)
disc[6] = 3  (visit 6 from 4)
disc[2] = 4  (visit 2 from 0)
disc[3] = 5  (visit 3 from 0)
disc[5] = 6  (visit 5 from 3)


CALCULATING LOW VALUES:

For vertex 6:
  disc[6] = 3
  Back edge: none to ancestors
  low[6] = min(3, 4, 2, 1, 0) = 0 (via 4-1 back edge!)

For vertex 4:
  disc[4] = 2
  Back edge: 4→1 (1 is ancestor)
  low[4] = min(2, 1, low[6]) = min(2, 1, 0) = 0

For vertex 1:
  disc[1] = 1
  Back edge: 1→0? No (0 is parent)
  low[1] = min(1, low[4]) = min(1, 0) = 0


BRIDGE IDENTIFICATION:
  Edge (1, 4): low[4] = 0 < disc[1] = 1? NO → NOT a bridge
  Edge (0, 1): low[1] = 0 < disc[0] = 0? NO → NOT a bridge
  Edge (0, 2): disc[2] = 4, children of 2 = none
              → Edge (0,2) is a bridge if no back edges
```

---

## Java Implementation

### Complete Implementation

```java
package dsa.graph.bridges;

import java.util.*;

public class BridgesAndArticulationPoints {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;
    private int time;
    private int[] disc;
    private int[] low;
    private boolean[] visited;
    private boolean[] isArticulationPoint;
    private List<int[]> bridges;

    public BridgesAndArticulationPoints(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
        this.disc = new int[numVertices];
        this.low = new int[numVertices];
        this.visited = new boolean[numVertices];
        this.isArticulationPoint = new boolean[numVertices];
        this.bridges = new ArrayList<>();
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
        adjList.get(destination).add(source);
    }

    public void findBridgesAndArticulationPoints() {
        time = 0;

        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                dfs(i, -1);
            }
        }
    }

    private void dfs(int u, int parent) {
        visited[u] = true;
        disc[u] = low[u] = ++time;
        int children = 0;

        for (int v : adjList.get(u)) {
            if (!visited[v]) {
                children++;
                dfs(v, u);

                // Update low value after DFS
                low[u] = Math.min(low[u], low[v]);

                // BRIDGE CONDITION: low[v] > disc[u]
                if (low[v] > disc[u]) {
                    bridges.add(new int[]{Math.min(u, v), Math.max(u, v)});
                }

                // ARTICULATION POINT (non-root):
                // If low[v] >= disc[u], u is articulation point
                if (low[v] >= disc[u] && parent != -1) {
                    isArticulationPoint[u] = true;
                }

            } else if (v != parent) {
                // Back edge found - update low
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        // ARTICULATION POINT (root):
        // If root has 2+ children, it's an articulation point
        if (parent == -1 && children > 1) {
            isArticulationPoint[u] = true;
        }
    }

    public List<int[]> getBridges() {
        return new ArrayList<>(bridges);
    }

    public List<Integer> getArticulationPoints() {
        List<Integer> points = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            if (isArticulationPoint[i]) {
                points.add(i);
            }
        }
        return points;
    }

    public static void main(String[] args) {
        // Example 1: Graph with bridges and articulation points
        System.out.println("=== Example 1 ===");
        BridgesAndArticulationPoints graph1 = new BridgesAndArticulationPoints(5);
        graph1.addEdge(0, 1);
        graph1.addEdge(0, 2);
        graph1.addEdge(1, 2);
        graph1.addEdge(1, 3);
        graph1.addEdge(3, 4);

        /*
         * Graph:
         *     0
         *    /│╲
         *   1 │ 2
         *   │  │
         *   3───4
         *
         * Bridge: (1, 3) and (3, 4)
         * Articulation Point: 1, 3
         */

        graph1.findBridgesAndArticulationPoints();
        System.out.println("Bridges: " + graph1.getBridges());
        System.out.println("Articulation Points: " + graph1.getArticulationPoints());

        // Example 2: Graph with no bridges (fully cyclic)
        System.out.println("\n=== Example 2 ===");
        BridgesAndArticulationPoints graph2 = new BridgesAndArticulationPoints(4);
        graph2.addEdge(0, 1);
        graph2.addEdge(1, 2);
        graph2.addEdge(2, 3);
        graph2.addEdge(3, 0);
        graph2.addEdge(0, 2);

        /*
         * Graph (fully cyclic):
         *   0 ─── 1
         *   │╲   ╱│
         *   │  ╲╱  │
         *   │  ╱╲  │
         *   3 ─── 2
         *
         * No bridges!
         * No articulation points (removing any vertex still connects the rest)
         */

        graph2.findBridgesAndArticulationPoints();
        System.out.println("Bridges: " + graph2.getBridges());
        System.out.println("Articulation Points: " + graph2.getArticulationPoints());

        // Example 3: Tree (all edges are bridges)
        System.out.println("\n=== Example 3 (Tree) ===");
        BridgesAndArticulationPoints tree = new BridgesAndArticulationPoints(5);
        tree.addEdge(0, 1);
        tree.addEdge(0, 2);
        tree.addEdge(2, 3);
        tree.addEdge(2, 4);

        /*
         * Tree:
         *     0
         *    /│╲
         *   1 2  │
         *      │  │
         *      3  4
         *
         * All edges are bridges!
         * Articulation Points: 0, 2
         */

        tree.findBridgesAndArticulationPoints();
        System.out.println("Bridges: " + tree.getBridges());
        System.out.println("Articulation Points: " + tree.getArticulationPoints());
    }
}

/*
EXPECTED OUTPUT:

=== Example 1 ===
Bridges: [[1, 3], [3, 4]]
Articulation Points: [1, 3]

=== Example 2 ===
Bridges: []
Articulation Points: []

=== Example 3 (Tree) ===
Bridges: [[0, 1], [0, 2], [2, 3], [2, 4]]
Articulation Points: [0, 2]
*/
```

---

## Why They Matter

### Real-World Applications

```
┌─────────────────────────────────────────────────────────────────┐
│                    WHY BRIDGES MATTER                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. NETWORK RELIABILITY                                          │
│     - Finding single points of failure                           │
│     - Identifying critical connections                           │
│     - Building resilient networks                                 │
│                                                                  │
│  2. ROAD TRANSPORTATION                                          │
│     - Finding bridges in road network                           │
│     - If bridge is down, which cities become isolated?         │
│     - Critical infrastructure planning                           │
│                                                                  │
│  3. SOCIAL NETWORKS                                              │
│     - Finding key connectors between communities                 │
│     - Influencers who if removed, split communities             │
│                                                                  │
│  4. COMPUTER NETWORKS                                            │
│     - Critical links in internet backbone                        │
│     - Router failures and their impact                          │
│                                                                  │
│  5. POWER GRIDS                                                  │
│     - Transmission line criticality                             │
│     - Substation importance                                      │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Applications

### 1. Finding Critical Connections in Network

```java
/*
 * CRITICAL CONNECTIONS IN NETWORK (LeetCode 1192)
 *
 * A connection is critical if removing it disconnects the network.
 * These are exactly bridges in the graph!
 */

// Solution: Use Tarjan's algorithm to find all bridges
// Time: O(V + E)
```

### 2. 2-Edge-Connected Components

```java
/*
 * 2-EDGE-CONNECTED COMPONENTS:
 *
 * A graph is 2-edge-connected if it has no bridges.
 * Components separated by bridges are 2-edge-connected.
 *
 * Applications:
 * - Finding regions that remain connected after edge failure
 * - Network reliability analysis
 */
```

### 3. Biconnected Components (Vertex Connectivity)

```java
/*
 * BICONNECTED COMPONENTS (VERTEX VERSION):
 *
 * Similar to bridges, but for vertices (articulation points).
 * A component is biconnected if removing any single vertex
 * doesn't disconnect it.
 *
 * Applications:
 * - Finding communities in social networks
 * - Identifying critical servers in distributed systems
 */
```

---

## Practice Problems

### Bridges
1. **Critical Connections in a Network** (LeetCode 1192) ⭐
2. **Find Bridges in Graph** (Multiple platforms)

### Articulation Points
1. **Articulation Points in Graph** (CSES)
2. **Number of Operations to Make Network Connected** (LeetCode 1319)

### Combined
1. **Biconnected Components** (CSES)
2. **Police Query** (similar problems)

---

## Summary

```
┌─────────────────────────────────────────────────────────────────┐
│              BRIDGES & ARTICULATION POINTS                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  BRIDGE (Cut Edge):                                             │
│  - Removal disconnects the graph                                  │
│  - Condition: low[v] > disc[u] for edge (u, v) where v is child │
│  - In trees, ALL edges are bridges                               │
│                                                                  │
│  ARTICULATION POINT (Cut Vertex):                               │
│  - Removal disconnects the graph                                  │
│  - Root: 2+ DFS children                                        │
│  - Non-root: low[child] >= disc[vertex]                        │
│                                                                  │
│  ALGORITHM: Tarjan's (single DFS)                              │
│  ⏱️ TIME: O(V + E)                                           │
│  💾 SPACE: O(V)                                                │
│                                                                  │
│  KEY VALUES:                                                    │
│  - disc[v]: Discovery time in DFS                                │
│  - low[v]: Lowest discovery time reachable from v's subtree      │
│                                                                  │
│  BRIDGE: low[child] > disc[parent]                            │
│  ARTICULATION POINT: low[child] >= disc[vertex] (non-root)     │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

> **Remember**: Bridges and articulation points identify critical infrastructure in networks. Tarjan's algorithm finds them both in a single DFS pass using discovery times and low values. Bridges are edges where low[child] > disc[parent]. Articulation points are vertices where low[child] >= disc[vertex] for any child (or root with 2+ children).
