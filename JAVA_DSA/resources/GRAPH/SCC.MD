# Strongly Connected Components (SCC) - Complete Guide

## Table of Contents
1. [What is SCC?](#what-is-scc)
2. [Key Concepts](#key-concepts)
3. [Kosaraju's Algorithm](#kosarajus-algorithm)
4. [Tarjan's Algorithm](#tarjans-algorithm)
5. [Applications](#applications)
6. [Practice Problems](#practice-problems)

---

## What is SCC?

A **Strongly Connected Component** is a maximal subgraph where every vertex is reachable from every other vertex within that subgraph.

```
┌─────────────────────────────────────────────────────────────────┐
│              STRONGLY CONNECTED COMPONENT                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  DEFINITION:                                                    │
│  In a directed graph, vertices u and v are strongly connected    │
│  if there exists a path from u to v AND a path from v to u.     │
│                                                                  │
│  A Strongly Connected Component is a maximal set of vertices    │
│  where every pair is strongly connected.                         │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Visual Example

```
DIRECTED GRAPH:              STRONGLY CONNECTED COMPONENTS:

    0 ─────→ 1                   ┌─────┐
    ↓╱      │                     │ 0,1 │
    2 ────→ 3                     │  2  │
    │       ↑                      └─────┘     ┌─────┐
    └───────┘                         SCC 1     │ 3,4 │
                                                 └─────┘
    SCCs: {0,1,2}, {3,4}                SCC 2

    - In {0,1,2}: 0→1→2→0 (all reachable)
    - In {3,4}: 3→4→3 (all reachable)
    - No path from SCC1 to SCC2 and back!
```

---

## Key Concepts

### Condensation Graph (Meta-Graph)

```
ORIGINAL GRAPH:              METAGRAPH (SCCs as nodes):

    0 ─────→ 1                   ┌───┐
    ↓╱      │                     │ A │ (contains 0,1,2)
    2 ────→ 3                     └─┬─┘
    │       ↑                        │
    └───────┘                        ▼
                              ┌───┐     ┌───┐
                              │ B │────→│ C │ (contains 3) (contains 4)
                              └───┘     └───┘

    - SCCs become nodes in meta-graph
    - Edges between SCCs form DAG
    - No cycles in meta-graph!
```

---

## Kosaraju's Algorithm

### The Algorithm

```java
/*
 * KOSARAJU'S ALGORITHM (Two-pass DFS):
 *
 * 1. First Pass: Do DFS on original graph, push vertices to stack
 *    in finish order (post-order)
 *
 * 2. Second Pass: Reverse all edges (create transpose graph)
 *    Pop vertices from stack, do DFS on reversed graph
 *    Each DFS tree from second pass = one SCC
 *
 * INTUITION:
 * - First pass finds "finish times"
 * - Vertices that finish last are in source SCCs
 * - Second pass explores in reverse topological order
 */
```

### Java Implementation

```java
package dsa.graph.scc;

import java.util.*;

public class KosarajuSCC {
    private Map<Integer, List<Integer>> adjList;
    private Map<Integer, List<Integer>> reverseAdj;
    private int numVertices;
    private boolean[] visited;
    private Stack<Integer> stack;

    public KosarajuSCC(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        this.reverseAdj = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
            reverseAdj.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
        reverseAdj.get(destination).add(source);
    }

    public List<List<Integer>> findSCCs() {
        visited = new boolean[numVertices];
        stack = new Stack<>();

        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                dfsFirstPass(i);
            }
        }

        visited = new boolean[numVertices];
        List<List<Integer>> sccs = new ArrayList<>();

        while (!stack.isEmpty()) {
            int vertex = stack.pop();
            if (!visited[vertex]) {
                List<Integer> component = new ArrayList<>();
                dfsSecondPass(vertex, component);
                sccs.add(component);
            }
        }
        return sccs;
    }

    private void dfsFirstPass(int vertex) {
        visited[vertex] = true;
        for (int neighbor : adjList.get(vertex)) {
            if (!visited[neighbor]) {
                dfsFirstPass(neighbor);
            }
        }
        stack.push(vertex);
    }

    private void dfsSecondPass(int vertex, List<Integer> component) {
        visited[vertex] = true;
        component.add(vertex);
        for (int neighbor : reverseAdj.get(vertex)) {
            if (!visited[neighbor]) {
                dfsSecondPass(neighbor, component);
            }
        }
    }

    public static void main(String[] args) {
        KosarajuSCC graph = new KosarajuSCC(5);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 0);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        List<List<Integer>> sccs = graph.findSCCs();
        System.out.println("Strongly Connected Components:");
        int count = 1;
        for (List<Integer> scc : sccs) {
            System.out.println("  SCC " + count++ + ": " + scc);
        }
    }
}

/*
OUTPUT:
Strongly Connected Components:
  SCC 1: [4]
  SCC 2: [0, 1, 2]
  SCC 3: [3]
*/
```

### Time Complexity: O(V + E)
### Space Complexity: O(V + E)

---

## Tarjan's Algorithm

### The Algorithm

```java
/*
 * TARCAN'S ALGORITHM (Single-pass DFS):
 *
 * Uses DFS with two arrays:
 * - disc[u]: Discovery time of vertex u
 * - low[u]: Lowest discovery time reachable from u's subtree
 *
 * A vertex u is the root of an SCC if:
 *   low[u] == disc[u]
 *
 * Vertices on stack form the current SCC
 */
```

### Java Implementation

```java
package dsa.graph.scc;

import java.util.*;

public class TarjanSCC {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;
    private int time;
    private int[] disc;
    private int[] low;
    private boolean[] onStack;
    private Stack<Integer> stack;
    private List<List<Integer>> sccs;

    public TarjanSCC(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
    }

    public List<List<Integer>> findSCCs() {
        disc = new int[numVertices];
        low = new int[numVertices];
        onStack = new boolean[numVertices];
        stack = new Stack<>();
        sccs = new ArrayList<>();
        time = 0;

        for (int i = 0; i < numVertices; i++) {
            if (disc[i] == 0) {
                dfs(i);
            }
        }
        return sccs;
    }

    private void dfs(int u) {
        disc[u] = low[u] = ++time;
        stack.push(u);
        onStack[u] = true;

        for (int v : adjList.get(u)) {
            if (disc[v] == 0) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        if (low[u] == disc[u]) {
            List<Integer> component = new ArrayList<>();
            while (true) {
                int v = stack.pop();
                onStack[v] = false;
                component.add(v);
                if (v == u) break;
            }
            sccs.add(component);
        }
    }

    public static void main(String[] args) {
        TarjanSCC graph = new TarjanSCC(5);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 0);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        List<List<Integer>> sccs = graph.findSCCs();
        System.out.println("Strongly Connected Components:");
        int count = 1;
        for (List<Integer> scc : sccs) {
            System.out.println("  SCC " + count++ + ": " + scc);
        }
    }
}

/*
OUTPUT:
Strongly Connected Components:
  SCC 1: [4]
  SCC 2: [0, 1, 2]
  SCC 3: [3]
*/
```

### Time Complexity: O(V + E)
### Space Complexity: O(V)

---

## Applications

### 1. Web Crawling
```java
/*
 * SCCs help identify strongly related web pages
 * Pages in same SCC have mutual links
 */
```

### 2. Social Networks
```java
/*
 * Find tightly connected communities
 * People who all follow each other
 */
```

### 3. Game Theory
```java
/*
 * Find cycles in game states
 * Determine winning/losing positions
 */
```

---

## Practice Problems

### Easy
1. **Number of Provinces** (LeetCode 547)
2. **Find the Town Judge** (LeetCode 997)

### Medium
1. **Critical Connections in a Network** (LeetCode 1192)
2. **Loud and Rich** (LeetCode 851)

---

## Summary

```
┌─────────────────────────────────────────────────────────────────┐
│            STRONGLY CONNECTED COMPONENTS                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  🎯 PURPOSE: Find maximal subgraphs where all vertices          │
│             can reach each other                                 │
│                                                                  │
│  📝 ALGORITHMS:                                                │
│     1. Kosaraju's: Two-pass DFS (forward + reverse)           │
│     2. Tarjan's: Single-pass DFS with disc/low values          │
│                                                                  │
│  ⏱️ TIME: O(V + E) for both                                  │
│  💾 SPACE: O(V + E)                                            │
│                                                                  │
│  🔑 KEY INSIGHT:                                                │
│     - SCCs in meta-graph form a DAG                             │
│     - Condensation graph = SCCs as nodes                       │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

> **Remember**: Kosaraju's is simpler to understand - DFS forward, then DFS on reversed graph. Tarjan's is more efficient single-pass. Both find all SCCs in O(V+E) time!
