# 🌳 Minimum Spanning Tree (MST) — Complete Beginner to Advanced Guide

> **Everything you need to know about MST: Theory, Intuition, Algorithms, Java Code, and Practice Problems — all in one place.**

---

## 📚 Table of Contents

1. [What is a Graph? (Foundation)](#1-what-is-a-graph-foundation)
2. [What is a Tree?](#2-what-is-a-tree)
3. [What is a Spanning Tree?](#3-what-is-a-spanning-tree)
4. [What is a Minimum Spanning Tree (MST)?](#4-what-is-a-minimum-spanning-tree-mst)
5. [Real-Life Analogies of MST](#5-real-life-analogies-of-mst)
6. [Key Properties of MST](#6-key-properties-of-mst)
7. [Graph Representation in Java](#7-graph-representation-in-java)
8. [Kruskal's Algorithm — Theory + Java Code](#8-kruskals-algorithm--theory--java-code)
9. [Union-Find (Disjoint Set Union) — The Backbone of Kruskal's](#9-union-find-disjoint-set-union--the-backbone-of-kruskals)
10. [Prim's Algorithm — Theory + Java Code](#10-prims-algorithm--theory--java-code)
11. [Kruskal's vs Prim's — Side-by-Side Comparison](#11-kruskals-vs-prims--side-by-side-comparison)
12. [Borůvka's Algorithm — Theory + Java Code](#12-bor%C5%AFvkas-algorithm--theory--java-code)
13. [How to Check if MST is Unique](#13-how-to-check-if-mst-is-unique)
14. [MST on Dense vs Sparse Graphs](#14-mst-on-dense-vs-sparse-graphs)
15. [Second Minimum Spanning Tree](#15-second-minimum-spanning-tree)
16. [MST in Directed Graphs (Minimum Arborescence)](#16-mst-in-directed-graphs-minimum-arborescence)
17. [Common MST Interview Problems + Java Solutions](#17-common-mst-interview-problems--java-solutions)
18. [Time and Space Complexity Cheat Sheet](#18-time-and-space-complexity-cheat-sheet)
19. [Common Mistakes & How to Avoid Them](#19-common-mistakes--how-to-avoid-them)
20. [Quick Revision Summary](#20-quick-revision-summary)

---

## 1. What is a Graph? (Foundation)

### 📖 Theory

A **Graph** is a collection of:
- **Vertices (Nodes)** — the entities (cities, computers, people)
- **Edges** — connections between those entities

Graphs come in two flavors:

| Type | Description | Example |
|------|-------------|---------|
| **Undirected** | Edges have no direction | Road between two cities |
| **Directed** | Edges point one way | One-way street |
| **Weighted** | Edges have a cost/weight | Road distance in km |
| **Unweighted** | All edges are equal | Social connections |

MST works on **undirected, weighted, connected** graphs.

### 📊 Visual Example

```
     4       2
  A ---- B ---- C
  |      |      |
 3|     6|     1|
  |      |      |
  D ---- E ---- F
     5       8
```

- Nodes: A, B, C, D, E, F
- Edges with weights are shown on the lines

---

## 2. What is a Tree?

### 📖 Theory

A **Tree** is a special type of graph with these properties:

1. ✅ It is **connected** (every node can reach every other node)
2. ✅ It has **no cycles** (no loops)
3. ✅ If it has `V` vertices, it has exactly `V-1` edges

```
     A
    / \
   B   C
  / \
 D   E
```

The above is a tree with 5 nodes and 4 edges. Notice: no cycles!

---

## 3. What is a Spanning Tree?

### 📖 Theory

Given a graph with `V` vertices and `E` edges, a **Spanning Tree** is a subgraph that:

1. ✅ **Includes ALL vertices** of the original graph
2. ✅ Is a **tree** (connected + no cycles)
3. ✅ Has exactly **V-1 edges**

### Example

Original Graph:
```
  A --3-- B
  |       |
  2       5
  |       |
  C --1-- D
```

Possible Spanning Trees:
```
Tree 1:        Tree 2:        Tree 3:
A --3-- B      A --3-- B      A
|              |               \
2              2             2  \
|              |               \
C --1-- D      C       D      C --1-- D
               (invalid:         |
               disconnected)     (missing B)
```

A graph can have **multiple** spanning trees. We want the one with **minimum total weight**.

---

## 4. What is a Minimum Spanning Tree (MST)?

### 📖 Theory

A **Minimum Spanning Tree (MST)** is a spanning tree where the **sum of all edge weights is as small as possible**.

**Formally:** Given a connected, undirected, weighted graph G = (V, E), the MST is a spanning tree T such that the sum of weights of edges in T is minimized.

### Key Facts

- A graph with `V` vertices will have an MST with exactly `V-1` edges
- There can be **multiple MSTs** if edge weights are repeated (but their total weight is the same)
- If all edge weights are **distinct**, the MST is **unique**

### Example

```
Graph:
  A --4-- B
  |     / |
  2   1   3
  |  /    |
  C --6-- D

MST:
  A --2-- C
  |
  B --1-- C (via edge BC weight 1)
  |
  B --3-- D

MST edges: A-C(2) + B-C(1) + B-D(3) = Total weight = 6
```

---

## 5. Real-Life Analogies of MST

### 🏙️ City Road Network

Imagine you need to connect 5 cities with roads so that:
- Every city is reachable from every other city
- The total road construction cost is minimum

The MST gives you the cheapest way to connect all cities!

### 💡 Electrical Power Grid

A power company wants to connect all houses in a region to the power grid. Each connection has a cost. MST helps find the cheapest network of wires.

### 🌐 Computer Network

A network engineer wants to connect all computers in a building using cables. MST minimizes the total cable length while keeping everyone connected.

### 🧬 Biology — Evolutionary Trees

MST is used in bioinformatics to find phylogenetic (evolutionary) trees from genetic distance data.

---

## 6. Key Properties of MST

### Property 1: Cut Property (Greedy Safe Edge)

> If you cut the graph into two groups (a "cut"), the **minimum-weight edge** crossing that cut is **always part of some MST**.

This is the fundamental property both Kruskal's and Prim's rely on.

### Property 2: Cycle Property

> For any cycle in the graph, the **maximum-weight edge** in that cycle is **never part of any MST**.

You can remove the heaviest edge from any cycle without disconnecting the graph.

### Property 3: Uniqueness

> If all edge weights are **distinct**, the MST is **unique**.

### Property 4: MST Total Weight

> The MST minimizes the **sum of all edge weights** (not the number of edges — that's always V-1).

### Property 5: Subgraph Property

> Any subtree of an MST is also an MST of its own induced subgraph.

---

## 7. Graph Representation in Java

Before jumping into MST algorithms, let's see how to represent graphs in Java.

### Method 1: Adjacency Matrix

```java
// Adjacency Matrix representation
// adj[i][j] = weight of edge between i and j, 0 if no edge

public class GraphMatrix {
    int V;
    int[][] adj;

    GraphMatrix(int v) {
        V = v;
        adj = new int[V][V];
    }

    void addEdge(int u, int v, int weight) {
        adj[u][v] = weight;
        adj[v][u] = weight; // undirected graph
    }

    void printGraph() {
        System.out.println("Adjacency Matrix:");
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                System.out.print(adj[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        GraphMatrix g = new GraphMatrix(4);
        g.addEdge(0, 1, 5);
        g.addEdge(0, 2, 3);
        g.addEdge(1, 3, 2);
        g.addEdge(2, 3, 7);
        g.printGraph();
    }
}
```

**Output:**
```
Adjacency Matrix:
0 5 3 0 
5 0 0 2 
3 0 0 7 
0 2 7 0 
```

### Method 2: Adjacency List (Preferred for MST)

```java
import java.util.*;

public class GraphList {
    int V;
    List<int[]>[] adj; // adj[u] = list of {v, weight}

    @SuppressWarnings("unchecked")
    GraphList(int v) {
        V = v;
        adj = new ArrayList[V];
        for (int i = 0; i < V; i++)
            adj[i] = new ArrayList<>();
    }

    void addEdge(int u, int v, int weight) {
        adj[u].add(new int[]{v, weight});
        adj[v].add(new int[]{u, weight}); // undirected
    }

    void printGraph() {
        for (int i = 0; i < V; i++) {
            System.out.print("Node " + i + " --> ");
            for (int[] edge : adj[i]) {
                System.out.print("[to:" + edge[0] + ", wt:" + edge[1] + "] ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        GraphList g = new GraphList(4);
        g.addEdge(0, 1, 5);
        g.addEdge(0, 2, 3);
        g.addEdge(1, 3, 2);
        g.addEdge(2, 3, 7);
        g.printGraph();
    }
}
```

**Output:**
```
Node 0 --> [to:1, wt:5] [to:2, wt:3] 
Node 1 --> [to:0, wt:5] [to:3, wt:2] 
Node 2 --> [to:0, wt:3] [to:3, wt:7] 
Node 3 --> [to:1, wt:2] [to:2, wt:7] 
```

### Method 3: Edge List (Used in Kruskal's)

```java
import java.util.*;

public class GraphEdgeList {
    int V, E;
    int[][] edges; // edges[i] = {u, v, weight}
    int edgeCount = 0;

    GraphEdgeList(int v, int e) {
        V = v;
        E = e;
        edges = new int[E][3];
    }

    void addEdge(int u, int v, int weight) {
        edges[edgeCount][0] = u;
        edges[edgeCount][1] = v;
        edges[edgeCount][2] = weight;
        edgeCount++;
    }

    public static void main(String[] args) {
        GraphEdgeList g = new GraphEdgeList(4, 4);
        g.addEdge(0, 1, 5);
        g.addEdge(0, 2, 3);
        g.addEdge(1, 3, 2);
        g.addEdge(2, 3, 7);

        System.out.println("Edges: [u, v, weight]");
        for (int[] edge : g.edges) {
            System.out.println(Arrays.toString(edge));
        }
    }
}
```

---

## 8. Kruskal's Algorithm — Theory + Java Code

### 📖 Theory

**Kruskal's Algorithm** builds the MST by greedily picking the smallest edge that doesn't form a cycle.

**Step-by-step:**
1. **Sort** all edges by weight (smallest first)
2. Initialize each node as its own component (using Union-Find)
3. For each edge (in sorted order):
   - If the two endpoints are in **different components** → **add edge to MST**
   - If they are in the **same component** → **skip** (would form a cycle)
4. Repeat until MST has `V-1` edges

### 🎯 Intuition

Think of it like this: You have scattered cities. You start connecting them using the cheapest roads first. If a road would connect two already-connected groups, you skip it (no need to pay for it). You stop when all cities are in one group.

### 📊 Dry Run Example

```
Graph Edges:
  A--B: 4
  A--C: 2
  B--C: 1
  B--D: 5
  C--D: 3

Step 1: Sort edges by weight:
  B-C:1, A-C:2, C-D:3, A-B:4, B-D:5

Step 2: Process edges:
  B-C:1 → B and C in different sets → ADD ✅  MST: {B-C}
  A-C:2 → A and C in different sets → ADD ✅  MST: {B-C, A-C}
  C-D:3 → C and D in different sets → ADD ✅  MST: {B-C, A-C, C-D}
  A-B:4 → A and B in SAME set     → SKIP ❌
  B-D:5 → B and D in SAME set     → SKIP ❌

MST Total Weight = 1 + 2 + 3 = 6
```

### ☕ Java Code — Kruskal's Algorithm

```java
import java.util.*;

public class KruskalMST {

    // ─── Edge class to represent each edge ───────────────────────────────────
    static class Edge implements Comparable<Edge> {
        int src, dest, weight;

        Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        // Sort edges by weight (ascending)
        @Override
        public int compareTo(Edge other) {
            return this.weight - other.weight;
        }
    }

    // ─── Union-Find (DSU) ─────────────────────────────────────────────────────
    static int[] parent, rank;

    static void makeSet(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;  // each node is its own parent
            rank[i] = 0;
        }
    }

    // Find with Path Compression
    static int find(int x) {
        if (parent[x] != x)
            parent[x] = find(parent[x]); // path compression
        return parent[x];
    }

    // Union by Rank
    static boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) return false; // same component → cycle!

        if (rank[rootX] < rank[rootY])
            parent[rootX] = rootY;
        else if (rank[rootX] > rank[rootY])
            parent[rootY] = rootX;
        else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        return true;
    }

    // ─── Kruskal's MST ────────────────────────────────────────────────────────
    static void kruskalMST(int V, List<Edge> edges) {
        // Step 1: Sort all edges by weight
        Collections.sort(edges);

        makeSet(V);

        List<Edge> mst = new ArrayList<>();
        int totalWeight = 0;

        System.out.println("Processing edges in sorted order:");
        System.out.println("─────────────────────────────────────────");

        // Step 2: Pick edges greedily
        for (Edge e : edges) {
            System.out.printf("Edge (%d — %d) weight: %d  →  ", e.src, e.dest, e.weight);

            if (union(e.src, e.dest)) {
                mst.add(e);
                totalWeight += e.weight;
                System.out.println("ADDED ✅");
            } else {
                System.out.println("SKIPPED ❌ (forms cycle)");
            }

            if (mst.size() == V - 1) break; // MST is complete
        }

        // Print MST
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("  Minimum Spanning Tree (Kruskal's)");
        System.out.println("═══════════════════════════════════════");
        System.out.println("Edge\t\tWeight");
        System.out.println("─────────────────────");
        for (Edge e : mst) {
            System.out.printf("%d  ——  %d\t\t%d%n", e.src, e.dest, e.weight);
        }
        System.out.println("─────────────────────");
        System.out.println("Total MST Weight: " + totalWeight);
    }

    // ─── Main ─────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        int V = 4;
        List<Edge> edges = new ArrayList<>();

        // Graph:
        //   0 --10-- 1
        //   |  \     |
        //   6   5    15
        //   |    \   |
        //   2 --4-- 3

        edges.add(new Edge(0, 1, 10));
        edges.add(new Edge(0, 2, 6));
        edges.add(new Edge(0, 3, 5));
        edges.add(new Edge(1, 3, 15));
        edges.add(new Edge(2, 3, 4));

        kruskalMST(V, edges);
    }
}
```

**Output:**
```
Processing edges in sorted order:
─────────────────────────────────────────
Edge (2 — 3) weight: 4  →  ADDED ✅
Edge (0 — 3) weight: 5  →  ADDED ✅
Edge (0 — 2) weight: 6  →  SKIPPED ❌ (forms cycle)
Edge (0 — 1) weight: 10  →  ADDED ✅

═══════════════════════════════════════
  Minimum Spanning Tree (Kruskal's)
═══════════════════════════════════════
Edge		Weight
─────────────────────
2  ——  3		4
0  ——  3		5
0  ——  1		10
─────────────────────
Total MST Weight: 19
```

---

## 9. Union-Find (Disjoint Set Union) — The Backbone of Kruskal's

### 📖 Theory

**Union-Find** (also called Disjoint Set Union — DSU) is a data structure that efficiently tracks which elements belong to the same group (component).

It supports two operations:
- **find(x)**: Which group does `x` belong to?
- **union(x, y)**: Merge the groups of `x` and `y`

### Why is it needed in Kruskal's?

We need to know: "If I add this edge, will it create a cycle?"
- If both endpoints are in the **same group** → Adding edge = cycle → Skip
- If they are in **different groups** → Safe to add → Union the groups

### Optimization 1: Path Compression

Without optimization, finding the root can be O(n) in worst case.

```
Before path compression (chain):
1 → 2 → 3 → 4 → 5

After find(1) with path compression:
All nodes point directly to root:
1 → 5
2 → 5
3 → 5
4 → 5
```

```java
static int find(int x) {
    if (parent[x] != x)
        parent[x] = find(parent[x]); // make x point to root directly!
    return parent[x];
}
```

### Optimization 2: Union by Rank

Always attach the shorter tree under the taller tree to keep the structure flat.

```java
static boolean union(int x, int y) {
    int rootX = find(x);
    int rootY = find(y);

    if (rootX == rootY) return false; // already in same set

    // Attach smaller rank tree under larger rank tree
    if (rank[rootX] < rank[rootY])
        parent[rootX] = rootY;
    else if (rank[rootX] > rank[rootY])
        parent[rootY] = rootX;
    else {
        parent[rootY] = rootX;
        rank[rootX]++; // only increment when ranks are equal
    }
    return true;
}
```

### Complete DSU Implementation

```java
public class DisjointSetUnion {
    int[] parent, rank, size;
    int components;

    DisjointSetUnion(int n) {
        parent = new int[n];
        rank = new int[n];
        size = new int[n];
        components = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
            size[i] = 1;
        }
    }

    // Find root with path compression
    int find(int x) {
        if (parent[x] != x)
            parent[x] = find(parent[x]);
        return parent[x];
    }

    // Union by rank, returns true if merged successfully
    boolean union(int x, int y) {
        int rx = find(x), ry = find(y);
        if (rx == ry) return false;

        if (rank[rx] < rank[ry]) { int tmp = rx; rx = ry; ry = tmp; }
        parent[ry] = rx;
        size[rx] += size[ry];
        if (rank[rx] == rank[ry]) rank[rx]++;
        components--;
        return true;
    }

    boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    int componentSize(int x) {
        return size[find(x)];
    }

    public static void main(String[] args) {
        DisjointSetUnion dsu = new DisjointSetUnion(5);
        // Initially: {0}, {1}, {2}, {3}, {4}

        dsu.union(0, 1);
        // Now: {0,1}, {2}, {3}, {4}

        dsu.union(2, 3);
        // Now: {0,1}, {2,3}, {4}

        dsu.union(0, 3);
        // Now: {0,1,2,3}, {4}

        System.out.println("0 and 2 connected? " + dsu.connected(0, 2)); // true
        System.out.println("0 and 4 connected? " + dsu.connected(0, 4)); // false
        System.out.println("Components: " + dsu.components);             // 2
        System.out.println("Size of 0's component: " + dsu.componentSize(0)); // 4
    }
}
```

**Time Complexity:** Nearly O(1) per operation (amortized O(α(n)) — the inverse Ackermann function, which is ≤ 5 for all practical inputs).

---

## 10. Prim's Algorithm — Theory + Java Code

### 📖 Theory

**Prim's Algorithm** builds the MST by **growing** it from a starting node, always picking the cheapest edge that connects the growing tree to a new vertex.

**Step-by-step:**
1. Start from any vertex (say vertex 0)
2. Mark it as **in the MST**
3. Look at all edges going from the MST to vertices **not yet in MST**
4. Pick the **minimum weight** edge
5. Add the connected vertex to MST
6. Repeat until all vertices are in MST

### 🎯 Intuition

Imagine you're building a road network. You start in one city. You look at all roads you can build from your current network. You always build the cheapest road that connects a NEW city. You keep expanding until all cities are connected.

### 📊 Dry Run Example

```
Graph:
  0 --2-- 1
  |       |
  3       5
  |       |
  2 --1-- 3

Start at vertex 0.

Step 1: MST = {0}
  Candidates from 0: edge(0,1,2), edge(0,2,3)
  Pick minimum: edge(0,1,2) → ADD vertex 1

Step 2: MST = {0, 1}
  Candidates: edge(0,2,3), edge(1,3,5)
  Pick minimum: edge(0,2,3) → ADD vertex 2

Step 3: MST = {0, 1, 2}
  Candidates: edge(1,3,5), edge(2,3,1)
  Pick minimum: edge(2,3,1) → ADD vertex 3

MST edges: (0,1,2) + (0,2,3) + (2,3,1) = 6
```

### ☕ Java Code — Prim's Algorithm (using Priority Queue)

```java
import java.util.*;

public class PrimMST {

    static void primMST(int V, List<int[]>[] adj) {
        // key[i]   = minimum weight edge to add vertex i to MST
        // parent[i] = which vertex in MST connected to i
        // inMST[i] = is vertex i already in MST?

        int[] key = new int[V];
        int[] parent = new int[V];
        boolean[] inMST = new boolean[V];

        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        key[0] = 0; // Start from vertex 0

        // Min-heap: {key value, vertex}
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, 0}); // {key=0, vertex=0}

        System.out.println("Processing vertices in Prim's order:");
        System.out.println("─────────────────────────────────────────");

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int u = curr[1];

            if (inMST[u]) continue; // already processed
            inMST[u] = true;

            System.out.println("Added vertex " + u + " to MST (via edge weight: " + curr[0] + ")");

            // Explore all neighbors of u
            for (int[] neighbor : adj[u]) {
                int v = neighbor[0];
                int weight = neighbor[1];

                // If v is not in MST and this edge is cheaper than known key[v]
                if (!inMST[v] && weight < key[v]) {
                    key[v] = weight;
                    parent[v] = u;
                    pq.offer(new int[]{key[v], v});
                }
            }
        }

        // Print the MST
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("  Minimum Spanning Tree (Prim's)");
        System.out.println("═══════════════════════════════════════");
        System.out.println("Edge\t\tWeight");
        System.out.println("─────────────────────");
        int totalWeight = 0;
        for (int i = 1; i < V; i++) {
            System.out.printf("%d  ——  %d\t\t%d%n", parent[i], i, key[i]);
            totalWeight += key[i];
        }
        System.out.println("─────────────────────");
        System.out.println("Total MST Weight: " + totalWeight);
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        int V = 5;
        List<int[]>[] adj = new ArrayList[V];
        for (int i = 0; i < V; i++)
            adj[i] = new ArrayList<>();

        // Graph:
        //   0 --2-- 1 --3-- 2
        //   |               |
        //   6               8
        //   |               |
        //   3 ------5------ 4

        int[][] edges = {
            {0, 1, 2}, {0, 3, 6}, {1, 2, 3},
            {1, 3, 8}, {1, 4, 5}, {2, 4, 7},
            {3, 4, 9}
        };

        for (int[] e : edges) {
            adj[e[0]].add(new int[]{e[1], e[2]});
            adj[e[1]].add(new int[]{e[0], e[2]});
        }

        primMST(V, adj);
    }
}
```

**Output:**
```
Processing vertices in Prim's order:
─────────────────────────────────────────
Added vertex 0 to MST (via edge weight: 0)
Added vertex 1 to MST (via edge weight: 2)
Added vertex 2 to MST (via edge weight: 3)
Added vertex 4 to MST (via edge weight: 5)
Added vertex 3 to MST (via edge weight: 6)

═══════════════════════════════════════
  Minimum Spanning Tree (Prim's)
═══════════════════════════════════════
Edge		Weight
─────────────────────
0  ——  1		2
1  ——  2		3
1  ——  4		5
0  ——  3		6
─────────────────────
Total MST Weight: 16
```

### ☕ Prim's Algorithm — Simple Array Version (for learning)

```java
public class PrimSimple {

    // Simple O(V²) version using arrays — easier to understand
    static void primMST(int V, int[][] graph) {
        int[] key = new int[V];       // min edge weight to add each vertex
        int[] parent = new int[V];    // MST structure
        boolean[] inMST = new boolean[V];

        Arrays.fill(key, Integer.MAX_VALUE);
        key[0] = 0;
        parent[0] = -1;

        for (int count = 0; count < V - 1; count++) {
            // Find vertex with minimum key value not yet in MST
            int u = -1;
            for (int v = 0; v < V; v++) {
                if (!inMST[v] && (u == -1 || key[v] < key[u]))
                    u = v;
            }

            inMST[u] = true;

            // Update key values of adjacent vertices
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !inMST[v] && graph[u][v] < key[v]) {
                    key[v] = graph[u][v];
                    parent[v] = u;
                }
            }
        }

        // Print MST
        System.out.println("MST Edges and Weights:");
        int total = 0;
        for (int i = 1; i < V; i++) {
            System.out.println(parent[i] + " - " + i + " : " + key[i]);
            total += key[i];
        }
        System.out.println("Total weight: " + total);
    }

    public static void main(String[] args) {
        int V = 5;
        int[][] graph = {
            {0, 2, 0, 6, 0},
            {2, 0, 3, 8, 5},
            {0, 3, 0, 0, 7},
            {6, 8, 0, 0, 9},
            {0, 5, 7, 9, 0}
        };

        primMST(V, graph);
    }
}
```

---

## 11. Kruskal's vs Prim's — Side-by-Side Comparison

| Feature | Kruskal's | Prim's |
|---------|-----------|--------|
| **Approach** | Edge-based (global) | Vertex-based (local growth) |
| **Data Structure** | Union-Find + Sorted edges | Priority Queue (Min-Heap) |
| **Time Complexity** | O(E log E) | O(E log V) with PQ |
| **Best for** | Sparse graphs (few edges) | Dense graphs (many edges) |
| **Starting point** | No fixed start needed | Starts from one vertex |
| **Disconnected graph** | Easy to detect (edges left) | Harder |
| **Implementation** | Slightly more complex | Moderate |
| **Memory** | O(E) for edge list | O(V) for key/parent arrays |

### When to use which?

```
Sparse Graph (E ≈ V):
  → Use Kruskal's (sorting E edges is faster)

Dense Graph (E ≈ V²):
  → Use Prim's with adjacency matrix → O(V²)
  → Kruskal's would need to sort V² edges

Parallel edges or self-loops:
  → Kruskal's handles them naturally
  → Prim's may need extra care

Finding MST without a fixed start:
  → Kruskal's is more natural
```

---

## 12. Borůvka's Algorithm — Theory + Java Code

### 📖 Theory

**Borůvka's Algorithm** (oldest MST algorithm, 1926) works in rounds:

1. Start: every vertex is its own component
2. Each round: For every component, find the **cheapest edge** leaving it
3. Add all those edges (merging components)
4. Repeat until only one component remains

**Number of rounds:** O(log V) — because each round at least halves the number of components.

```
Round 1: Each node picks cheapest outgoing edge
Round 2: Each merged component picks cheapest outgoing edge
...until one component
```

### ☕ Java Code — Borůvka's Algorithm

```java
import java.util.*;

public class BoruvkaMST {

    static int[] parent, rank;

    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    static void union(int x, int y) {
        int rx = find(x), ry = find(y);
        if (rx == ry) return;
        if (rank[rx] < rank[ry]) parent[rx] = ry;
        else if (rank[rx] > rank[ry]) parent[ry] = rx;
        else { parent[ry] = rx; rank[rx]++; }
    }

    static void boruvkaMST(int V, int[][] edges, int E) {
        parent = new int[V];
        rank = new int[V];
        for (int i = 0; i < V; i++) { parent[i] = i; rank[i] = 0; }

        int mstWeight = 0;
        int mstEdges = 0;
        List<int[]> mst = new ArrayList<>();

        // cheapest[i] = cheapest edge for component i
        int[] cheapest = new int[V];

        int numComponents = V;

        System.out.println("Borůvka's Algorithm — Round by Round:");

        int round = 1;
        while (numComponents > 1) {
            System.out.println("\n--- Round " + round++ + " (Components: " + numComponents + ") ---");
            Arrays.fill(cheapest, -1);

            // Find cheapest edge for each component
            for (int i = 0; i < E; i++) {
                int u = edges[i][0], v = edges[i][1], w = edges[i][2];
                int cu = find(u), cv = find(v);

                if (cu != cv) {
                    if (cheapest[cu] == -1 || edges[cheapest[cu]][2] > w)
                        cheapest[cu] = i;
                    if (cheapest[cv] == -1 || edges[cheapest[cv]][2] > w)
                        cheapest[cv] = i;
                }
            }

            // Add cheapest edges to MST
            for (int i = 0; i < V; i++) {
                if (cheapest[i] != -1) {
                    int u = edges[cheapest[i]][0];
                    int v = edges[cheapest[i]][1];
                    int w = edges[cheapest[i]][2];

                    int cu = find(u), cv = find(v);
                    if (cu != cv) {
                        union(u, v);
                        mstWeight += w;
                        mstEdges++;
                        mst.add(edges[cheapest[i]]);
                        numComponents--;
                        System.out.println("  Added edge: " + u + " - " + v + " (weight: " + w + ")");
                    }
                }
            }
        }

        System.out.println("\n═══════════════════════════════════════");
        System.out.println("  Minimum Spanning Tree (Borůvka's)");
        System.out.println("═══════════════════════════════════════");
        for (int[] e : mst)
            System.out.println(e[0] + " — " + e[1] + " : " + e[2]);
        System.out.println("Total MST Weight: " + mstWeight);
    }

    public static void main(String[] args) {
        int V = 4;
        int[][] edges = {
            {0, 1, 10}, {0, 2, 6}, {0, 3, 5},
            {1, 3, 15}, {2, 3, 4}
        };

        boruvkaMST(V, edges, edges.length);
    }
}
```

**Output:**
```
Borůvka's Algorithm — Round by Round:

--- Round 1 (Components: 4) ---
  Added edge: 2 - 3 (weight: 4)
  Added edge: 0 - 3 (weight: 5)
  Added edge: 0 - 1 (weight: 10)

--- Round 2 (Components: 1) ---

═══════════════════════════════════════
  Minimum Spanning Tree (Borůvka's)
═══════════════════════════════════════
2 — 3 : 4
0 — 3 : 5
0 — 1 : 10
Total MST Weight: 19
```

---

## 13. How to Check if MST is Unique

### 📖 Theory

The MST is **unique** if and only if there are **no ties** in edge weights that could lead to different spanning trees.

More formally: The MST is unique if for every cut in the graph, there is exactly **one minimum-weight edge** crossing that cut.

### ☕ Java Code

```java
import java.util.*;

public class UniqueMST {

    static int[] parent, rank;

    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    static boolean union(int x, int y) {
        int rx = find(x), ry = find(y);
        if (rx == ry) return false;
        if (rank[rx] < rank[ry]) parent[rx] = ry;
        else if (rank[rx] > rank[ry]) parent[ry] = rx;
        else { parent[ry] = rx; rank[rx]++; }
        return true;
    }

    static void init(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) { parent[i] = i; }
    }

    // Group edges by weight and check if any weight group has a "critical" edge
    // An edge is critical if removing it disconnects the MST
    static boolean isUniqueMST(int V, int[][] edges) {
        // Sort edges by weight
        Arrays.sort(edges, (a, b) -> a[2] - b[2]);

        init(V);

        int mstWeight = 0;
        List<int[]> mstEdges = new ArrayList<>();

        // Build standard MST
        for (int[] e : edges) {
            if (union(e[0], e[1])) {
                mstEdges.add(e);
                mstWeight += e[2];
            }
        }

        // Check uniqueness: try replacing each MST edge with a same-weight edge
        // If possible → MST is NOT unique
        for (int[] mstEdge : mstEdges) {
            int target = mstEdge[2]; // weight of this MST edge

            // Try to build MST without this edge, using an alternate edge of same weight
            init(V);
            int altWeight = 0;
            boolean usedAlternate = false;

            for (int[] e : edges) {
                if (e == mstEdge) continue; // skip current MST edge
                if (union(e[0], e[1])) {
                    altWeight += e[2];
                    if (e[2] == target) usedAlternate = true;
                }
            }

            // Check if an MST of same weight exists using a different set of edges
            if (altWeight == mstWeight && usedAlternate) {
                System.out.println("MST is NOT unique (multiple MSTs exist with weight " + mstWeight + ")");
                return false;
            }
        }

        System.out.println("MST IS unique with weight " + mstWeight);
        return true;
    }

    public static void main(String[] args) {
        // Test 1: Unique MST (all distinct weights)
        System.out.println("=== Test 1: All distinct edge weights ===");
        int[][] edges1 = {{0,1,1}, {0,2,2}, {1,2,3}, {1,3,4}, {2,3,5}};
        isUniqueMST(4, edges1);

        // Test 2: Non-unique MST (duplicate weights allowing swap)
        System.out.println("\n=== Test 2: Equal weight edges ===");
        int[][] edges2 = {{0,1,1}, {0,2,1}, {1,2,2}, {1,3,3}, {2,3,3}};
        isUniqueMST(4, edges2);
    }
}
```

---

## 14. MST on Dense vs Sparse Graphs

### 📖 Theory

The choice of algorithm dramatically impacts performance:

| Graph Type | E (edges) | Best Algorithm | Complexity |
|------------|-----------|----------------|------------|
| Sparse | E ≈ V | Kruskal's | O(E log E) ≈ O(V log V) |
| Dense | E ≈ V² | Prim's (adj matrix) | O(V²) |
| Very Dense | E = V² | Prim's | O(V²) << O(V² log V²) |

### Why does density matter?

**Kruskal's** sorts all E edges: O(E log E)
- Sparse: E=100, V=100 → 100 * 7 = 700 ops
- Dense: E=10000, V=100 → 10000 * 13 = 130000 ops

**Prim's** with adjacency matrix: O(V²)
- Always: 100² = 10000 ops regardless of E

### ☕ Java Code — Prim's O(V²) for Dense Graphs

```java
import java.util.Arrays;

public class PrimDense {

    // O(V²) version ideal for dense graphs
    static int primDense(int V, int[][] graph) {
        int[] key = new int[V];
        boolean[] inMST = new boolean[V];
        int[] parent = new int[V];

        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        key[0] = 0;

        int totalWeight = 0;

        for (int count = 0; count < V; count++) {
            // O(V): pick vertex with minimum key not in MST
            int u = -1;
            for (int v = 0; v < V; v++)
                if (!inMST[v] && (u == -1 || key[v] < key[u]))
                    u = v;

            inMST[u] = true;
            totalWeight += key[u];

            // O(V): update keys of neighbors
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !inMST[v] && graph[u][v] < key[v]) {
                    key[v] = graph[u][v];
                    parent[v] = u;
                }
            }
        }

        System.out.println("MST edges (Dense Graph Prim's):");
        for (int i = 1; i < V; i++)
            System.out.println("  " + parent[i] + " - " + i + " : " + key[i]);

        return totalWeight;
    }

    public static void main(String[] args) {
        // Dense 4-node graph (represented as adj matrix)
        int[][] graph = {
            {0, 1, 3, 4},
            {1, 0, 2, 5},
            {3, 2, 0, 1},
            {4, 5, 1, 0}
        };

        int weight = primDense(4, graph);
        System.out.println("Total MST Weight: " + weight);
    }
}
```

---

## 15. Second Minimum Spanning Tree

### 📖 Theory

The **Second Minimum Spanning Tree** is the spanning tree with the smallest total weight among all spanning trees **excluding the MST** (or the next MST if multiple exist with the same weight).

**Algorithm:**
1. Find the MST
2. For each edge NOT in MST:
   - Temporarily ADD it to the MST (creating a cycle)
   - Remove the **maximum-weight edge** in that cycle (other than the added edge)
   - This gives a candidate spanning tree
3. The candidate with minimum weight is the second MST

### ☕ Java Code — Second MST

```java
import java.util.*;

public class SecondMST {

    static int V;
    static boolean[][] inMST;
    static int[][] maxEdge; // maxEdge[u][v] = max weight on path u→v in MST
    static List<int[]>[] mstAdj;
    static int[] parent;
    static int[] rankArr;

    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    static boolean union(int x, int y) {
        int rx = find(x), ry = find(y);
        if (rx == ry) return false;
        if (rankArr[rx] < rankArr[ry]) parent[rx] = ry;
        else if (rankArr[rx] > rankArr[ry]) parent[ry] = rx;
        else { parent[ry] = rx; rankArr[rx]++; }
        return true;
    }

    // DFS to precompute max edge on path from src to every other node in MST
    static void dfs(int u, int par, int maxW, int src) {
        maxEdge[src][u] = maxW;
        for (int[] neighbor : mstAdj[u]) {
            int v = neighbor[0], w = neighbor[1];
            if (v != par) {
                dfs(v, u, Math.max(maxW, w), src);
            }
        }
    }

    @SuppressWarnings("unchecked")
    static int secondMST(int V, int[][] edges) {
        SecondMST.V = V;
        inMST = new boolean[V][V];
        maxEdge = new int[V][V];
        mstAdj = new ArrayList[V];
        for (int i = 0; i < V; i++) mstAdj[i] = new ArrayList<>();

        // Sort and build MST using Kruskal's
        int[][] sortedEdges = edges.clone();
        Arrays.sort(sortedEdges, (a, b) -> a[2] - b[2]);

        parent = new int[V];
        rankArr = new int[V];
        for (int i = 0; i < V; i++) parent[i] = i;

        int mstWeight = 0;

        for (int[] e : sortedEdges) {
            if (union(e[0], e[1])) {
                mstWeight += e[2];
                inMST[e[0]][e[1]] = inMST[e[1]][e[0]] = true;
                mstAdj[e[0]].add(new int[]{e[1], e[2]});
                mstAdj[e[1]].add(new int[]{e[0], e[2]});
            }
        }

        System.out.println("MST Weight: " + mstWeight);

        // Precompute max edge on path between every pair in MST
        for (int i = 0; i < V; i++) dfs(i, -1, 0, i);

        // Try each non-MST edge to find second MST
        int secondWeight = Integer.MAX_VALUE;
        int[] bestSwap = null;

        for (int[] e : edges) {
            int u = e[0], v = e[1], w = e[2];
            if (!inMST[u][v]) {
                // Adding edge (u,v) and removing max edge on path u→v
                int newWeight = mstWeight + w - maxEdge[u][v];
                if (newWeight < secondWeight) {
                    secondWeight = newWeight;
                    bestSwap = e;
                }
            }
        }

        System.out.println("Second MST Weight: " + secondWeight);
        if (bestSwap != null)
            System.out.println("Key swap: Add edge (" + bestSwap[0] + "-" + bestSwap[1] +
                               " wt:" + bestSwap[2] + "), remove path max edge of weight " +
                               maxEdge[bestSwap[0]][bestSwap[1]]);

        return secondWeight;
    }

    public static void main(String[] args) {
        int[][] edges = {
            {0, 1, 1}, {0, 2, 2}, {0, 3, 3},
            {1, 2, 4}, {2, 3, 5}
        };
        secondMST(4, edges);
    }
}
```

---

## 16. MST in Directed Graphs (Minimum Arborescence)

### 📖 Theory

For **directed** graphs, MST becomes the **Minimum Spanning Arborescence** (also called Minimum Cost Arborescence or Minimum Directed Spanning Tree).

This is solved by **Edmonds' Algorithm** (also called Chu-Liu/Edmonds' algorithm).

Key differences:
- We want a **directed spanning tree** rooted at a given vertex
- All other vertices must be reachable from the root
- Each non-root vertex has **exactly one incoming edge**

### ☕ Java Code — Chu-Liu/Edmonds' Algorithm (Minimum Arborescence)

```java
import java.util.*;

public class MinArborescence {

    // Simplified Edmonds' / Chu-Liu Algorithm
    // Returns minimum cost arborescence rooted at 'root'
    static long edmondsArborescence(int n, int root, int[][] edges) {
        int[] inEdge = new int[n];    // cheapest incoming edge for each node
        int[] inWeight = new int[n];  // weight of cheapest incoming edge
        int[] from = new int[n];      // source of cheapest incoming edge

        while (true) {
            // Step 1: Find minimum incoming edge for each non-root node
            Arrays.fill(inWeight, Integer.MAX_VALUE);
            for (int[] e : edges) {
                int u = e[0], v = e[1], w = e[2];
                if (v != root && w < inWeight[v]) {
                    inWeight[v] = w;
                    from[v] = u;
                }
            }

            // Step 2: Check if all non-root nodes have an incoming edge
            for (int i = 0; i < n; i++) {
                if (i != root && inWeight[i] == Integer.MAX_VALUE) {
                    System.out.println("No arborescence exists!");
                    return -1;
                }
            }

            // Step 3: Detect cycles in the "selected edge" graph
            int[] id = new int[n];
            Arrays.fill(id, -1);
            int[] visited = new int[n];
            Arrays.fill(visited, -1);

            int numCycles = 0;
            for (int i = 0; i < n; i++) {
                if (i == root) continue;
                int v = i;
                // Walk until we hit root, a visited node, or a cycle
                while (visited[v] == -1 && id[v] == -1 && v != root) {
                    visited[v] = i;
                    v = from[v];
                }

                // If we found a cycle
                if (v != root && id[v] == -1 && visited[v] == i) {
                    // Mark cycle nodes
                    int u = v;
                    do {
                        id[u] = numCycles;
                        u = from[u];
                    } while (u != v);
                    numCycles++;
                }
            }

            // If no cycles → we have our arborescence
            if (numCycles == 0) {
                long total = 0;
                for (int i = 0; i < n; i++)
                    if (i != root) total += inWeight[i];
                return total;
            }

            // Contract cycles and continue
            for (int i = 0; i < n; i++)
                if (id[i] == -1) id[i] = numCycles++;

            // Update edges after contraction
            List<int[]> newEdges = new ArrayList<>();
            for (int[] e : edges) {
                int u = id[e[0]], v = id[e[1]], w = e[2];
                if (u != v) {
                    newEdges.add(new int[]{u, v, w - inWeight[e[1]]});
                }
            }

            edges = newEdges.toArray(new int[0][]);
            n = numCycles;
            root = id[root];
        }
    }

    public static void main(String[] args) {
        // Directed weighted graph
        // edges[i] = {from, to, weight}
        int[][] edges = {
            {0, 1, 2}, {0, 2, 3},
            {1, 2, 1}, {2, 1, 1},
            {1, 3, 4}, {2, 3, 2}
        };

        long result = edmondsArborescence(4, 0, edges);
        System.out.println("Minimum Arborescence Weight from root 0: " + result);
    }
}
```

---

## 17. Common MST Interview Problems + Java Solutions

### Problem 1: Connecting Cities with Minimum Cost (LeetCode 1135)

```
Given n cities and possible connections with costs, find the minimum
cost to connect all cities.
```

```java
import java.util.*;

public class ConnectingCities {

    static int[] parent, rank;

    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    static boolean union(int x, int y) {
        int rx = find(x), ry = find(y);
        if (rx == ry) return false;
        if (rank[rx] < rank[ry]) parent[rx] = ry;
        else if (rank[rx] > rank[ry]) parent[ry] = rx;
        else { parent[ry] = rx; rank[rx]++; }
        return true;
    }

    // connections[i] = {city1, city2, cost}
    static int minimumCost(int n, int[][] connections) {
        parent = new int[n + 1];
        rank = new int[n + 1];
        for (int i = 0; i <= n; i++) parent[i] = i;

        // Sort by cost
        Arrays.sort(connections, (a, b) -> a[2] - b[2]);

        int totalCost = 0, edgesUsed = 0;

        for (int[] conn : connections) {
            if (union(conn[0], conn[1])) {
                totalCost += conn[2];
                edgesUsed++;
                if (edgesUsed == n - 1) break;
            }
        }

        return edgesUsed == n - 1 ? totalCost : -1; // -1 if impossible
    }

    public static void main(String[] args) {
        int n = 3;
        int[][] connections = {{1,2,5},{1,3,6},{2,3,1}};
        System.out.println("Minimum cost: " + minimumCost(n, connections)); // Output: 6
    }
}
```

---

### Problem 2: Min Cost to Connect All Points (LeetCode 1584)

```
Given n points on a 2D plane, connect all points using Manhattan distance.
Find minimum cost to connect all points.
Manhattan distance between (x1,y1) and (x2,y2) = |x1-x2| + |y1-y2|
```

```java
import java.util.*;

public class MinCostConnectPoints {

    // Prim's approach (efficient for dense graphs — all pairs connected)
    static int minCostConnectPoints(int[][] points) {
        int n = points.length;
        int[] minDist = new int[n];
        boolean[] inMST = new boolean[n];
        Arrays.fill(minDist, Integer.MAX_VALUE);
        minDist[0] = 0;

        int totalCost = 0;

        for (int i = 0; i < n; i++) {
            // Pick node not in MST with minimum distance
            int u = -1;
            for (int v = 0; v < n; v++)
                if (!inMST[v] && (u == -1 || minDist[v] < minDist[u]))
                    u = v;

            inMST[u] = true;
            totalCost += minDist[u];

            // Update distances to remaining nodes
            for (int v = 0; v < n; v++) {
                if (!inMST[v]) {
                    int dist = Math.abs(points[u][0] - points[v][0])
                             + Math.abs(points[u][1] - points[v][1]);
                    minDist[v] = Math.min(minDist[v], dist);
                }
            }
        }

        return totalCost;
    }

    public static void main(String[] args) {
        int[][] points = {{0,0},{2,2},{3,10},{5,2},{7,0}};
        System.out.println("Min cost: " + minCostConnectPoints(points)); // Output: 20
    }
}
```

---

### Problem 3: Redundant Connection (LeetCode 684)

```
Given a tree with one extra edge added, find the redundant edge
(the edge that causes a cycle).
```

```java
public class RedundantConnection {

    static int[] parent, rank;

    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    static boolean union(int x, int y) {
        int rx = find(x), ry = find(y);
        if (rx == ry) return false; // already connected → this edge is redundant!
        if (rank[rx] < rank[ry]) parent[rx] = ry;
        else if (rank[rx] > rank[ry]) parent[ry] = rx;
        else { parent[ry] = rx; rank[rx]++; }
        return true;
    }

    static int[] findRedundantConnection(int[][] edges) {
        int n = edges.length;
        parent = new int[n + 1];
        rank = new int[n + 1];
        for (int i = 0; i <= n; i++) parent[i] = i;

        for (int[] edge : edges) {
            if (!union(edge[0], edge[1])) {
                return edge; // this edge creates a cycle → redundant!
            }
        }
        return new int[]{};
    }

    public static void main(String[] args) {
        int[][] edges = {{1,2},{1,3},{2,3}};
        int[] result = findRedundantConnection(edges);
        System.out.println("Redundant edge: [" + result[0] + ", " + result[1] + "]");
        // Output: [2, 3]
    }
}
```

---

### Problem 4: Number of Operations to Make Network Connected (LeetCode 1319)

```
Given n computers and cables connecting some of them, find the minimum
number of cables you need to move to connect all computers.
Return -1 if impossible.
```

```java
public class NetworkConnected {

    static int[] parent;

    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    static boolean union(int x, int y) {
        int rx = find(x), ry = find(y);
        if (rx == ry) return false;
        parent[rx] = ry;
        return true;
    }

    static int makeConnected(int n, int[][] connections) {
        if (connections.length < n - 1) return -1; // not enough cables

        parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        int extraEdges = 0; // count redundant edges (can be repurposed)
        for (int[] conn : connections) {
            if (!union(conn[0], conn[1])) {
                extraEdges++; // this cable is redundant → can be moved
            }
        }

        // Count disconnected components
        int components = 0;
        for (int i = 0; i < n; i++)
            if (find(i) == i) components++;

        int needed = components - 1; // cables needed to connect all components

        return extraEdges >= needed ? needed : -1;
    }

    public static void main(String[] args) {
        System.out.println(makeConnected(4, new int[][]{{0,1},{0,2},{1,2}})); // 1
        System.out.println(makeConnected(6, new int[][]{{0,1},{0,2},{0,3},{1,2},{1,3}})); // 2
    }
}
```

---

### Problem 5: Kruskal's for Finding MST Weight (Classic)

```java
import java.util.*;

public class MSTWeight {

    static int[] parent, rank;

    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    static boolean union(int x, int y) {
        int rx = find(x), ry = find(y);
        if (rx == ry) return false;
        if (rank[rx] < rank[ry]) parent[rx] = ry;
        else if (rank[rx] > rank[ry]) parent[ry] = rx;
        else { parent[ry] = rx; rank[rx]++; }
        return true;
    }

    static int findMSTWeight(int n, int[][] edges) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        Arrays.sort(edges, (a, b) -> a[2] - b[2]);

        int weight = 0, count = 0;
        for (int[] e : edges) {
            if (union(e[0], e[1])) {
                weight += e[2];
                if (++count == n - 1) break;
            }
        }

        return count == n - 1 ? weight : -1; // -1 if graph is disconnected
    }

    public static void main(String[] args) {
        int[][] edges = {{0,1,1},{0,2,2},{1,2,3},{1,3,4},{2,3,5}};
        System.out.println("MST weight: " + findMSTWeight(4, edges)); // 7
    }
}
```

---

## 18. Time and Space Complexity Cheat Sheet

### Kruskal's Algorithm

| Operation | Complexity |
|-----------|------------|
| Sort edges | O(E log E) |
| Union-Find ops | O(E · α(V)) ≈ O(E) |
| **Total Time** | **O(E log E)** = O(E log V) |
| **Space** | **O(V + E)** |

Since E ≤ V², we have log E ≤ 2 log V, so O(E log E) = O(E log V).

### Prim's Algorithm

| Implementation | Time Complexity | Space | Best For |
|----------------|-----------------|-------|----------|
| Adjacency Matrix + Array | O(V²) | O(V²) | Dense graphs |
| Adjacency List + Binary Heap | O(E log V) | O(V + E) | Sparse graphs |
| Adjacency List + Fibonacci Heap | O(E + V log V) | O(V + E) | Theory only |

### Borůvka's Algorithm

| Complexity | Value |
|------------|-------|
| Rounds | O(log V) |
| Work per round | O(E) |
| **Total Time** | **O(E log V)** |
| Space | O(V + E) |

### Summary Table

| Algorithm | Time | Space | Best Use Case |
|-----------|------|-------|---------------|
| Kruskal's | O(E log E) | O(E) | Sparse graphs, simple to implement |
| Prim's (PQ) | O(E log V) | O(V + E) | Sparse graphs with adjacency list |
| Prim's (Matrix) | O(V²) | O(V²) | Dense graphs |
| Borůvka's | O(E log V) | O(V + E) | Parallel/distributed settings |

---

## 19. Common Mistakes & How to Avoid Them

### ❌ Mistake 1: Not checking for cycle before adding edge (Kruskal's)

```java
// WRONG: Just add edges without checking
for (Edge e : sortedEdges) {
    mst.add(e); // WRONG — may add edges creating cycles!
}

// CORRECT: Use Union-Find to check
for (Edge e : sortedEdges) {
    if (union(e.src, e.dest)) { // Only add if no cycle
        mst.add(e);
    }
}
```

### ❌ Mistake 2: Forgetting to initialize Union-Find

```java
// WRONG: parent array not initialized
int[] parent = new int[V]; // defaults to all zeros → every node has parent 0!

// CORRECT:
int[] parent = new int[V];
for (int i = 0; i < V; i++) parent[i] = i; // each node is its own parent
```

### ❌ Mistake 3: Using wrong number of nodes in Kruskal's (1-indexed vs 0-indexed)

```java
// LeetCode problems often use 1-indexed nodes
// WRONG: parent = new int[n] (misses index n!)
int[] parent = new int[n + 1]; // CORRECT for 1-indexed problems
for (int i = 0; i <= n; i++) parent[i] = i;
```

### ❌ Mistake 4: Not handling disconnected graphs

```java
// WRONG: Assuming MST always exists
static int kruskal(int V, List<Edge> edges) {
    // ... algorithm ...
    return totalWeight; // What if graph is disconnected?
}

// CORRECT: Check if MST is complete
static int kruskal(int V, List<Edge> edges) {
    // ... algorithm ...
    if (mstEdges.size() != V - 1) return -1; // Disconnected graph
    return totalWeight;
}
```

### ❌ Mistake 5: Integer overflow in weight calculation

```java
// WRONG: May overflow if weights are large
int totalWeight = 0;
totalWeight += edge.weight; // int overflow possible

// CORRECT: Use long for large weights
long totalWeight = 0;
totalWeight += edge.weight;
```

### ❌ Mistake 6: Prim's — Not handling stale entries in Priority Queue

```java
// Without this check, stale (outdated) entries cause wrong results
while (!pq.isEmpty()) {
    int[] curr = pq.poll();
    int u = curr[1];

    if (inMST[u]) continue; // ← THIS IS CRITICAL — skip stale entries
    inMST[u] = true;
    // ...
}
```

---

## 20. Quick Revision Summary

### 🎯 What is MST?
- Spanning tree of a graph with **minimum total edge weight**
- Always has exactly **V-1 edges** for V vertices
- Graph must be **connected, undirected, weighted**

### 🔑 Key Algorithms

| Algorithm | Core Idea | Key Data Structure |
|-----------|-----------|-------------------|
| **Kruskal's** | Sort edges, add cheapest non-cycle edge | Union-Find |
| **Prim's** | Grow tree from one vertex, pick cheapest adjacent edge | Priority Queue |
| **Borůvka's** | Each component picks cheapest outgoing edge | Union-Find |

### 📐 Key Formulas

```
MST edges = V - 1
Kruskal's complexity = O(E log E)
Prim's complexity (PQ) = O(E log V)
Prim's complexity (matrix) = O(V²)
```

### 🧠 Key Properties

1. **Cut Property**: Min-weight edge crossing any cut → in MST
2. **Cycle Property**: Max-weight edge in any cycle → NOT in MST
3. **Unique MST**: All distinct edge weights → exactly 1 MST
4. **Greedy works**: Both algorithms are correct due to cut property

### 🏆 Which Algorithm to Choose?

```
Sparse graph (E ≈ V):    → Kruskal's
Dense graph (E ≈ V²):   → Prim's with adjacency matrix O(V²)
Interview default:       → Kruskal's (simpler, clean code)
Parallel computing:      → Borůvka's
```

### 💡 MST Template (Kruskal's — Most Used in Interviews)

```java
// 1. Sort edges by weight
Arrays.sort(edges, (a, b) -> a[2] - b[2]);

// 2. Initialize DSU
int[] parent = new int[n];
for (int i = 0; i < n; i++) parent[i] = i;

// 3. Kruskal's loop
int total = 0, count = 0;
for (int[] e : edges) {
    int rx = find(parent, e[0]), ry = find(parent, e[1]);
    if (rx != ry) {
        parent[rx] = ry;    // union
        total += e[2];      // add weight
        count++;
        if (count == n - 1) break; // MST complete
    }
}
return count == n - 1 ? total : -1;
```

---

> **🎓 Congratulations!** You've completed the full A-to-Z guide on Minimum Spanning Trees. Practice the interview problems, understand the underlying properties, and you'll ace any MST problem!

---

*Made with ❤️ for learners. Happy Coding! 🚀*
