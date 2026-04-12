# Minimum Spanning Tree (MST) - Complete Guide

## Prerequisites & Related Topics

- **Data Structure**: [07_union_find.md](07_union_find.md) (Kruskal's algorithm uses Union-Find)
- **Graph Foundation**: [01_theory.md](01_theory.md) (weighted graphs, connectivity)
- **Algorithms Covered**: 
  - Kruskal's (greedy + Union-Find)  
  - Prim's (greedy + priority queue)
- **Related**: [10_floyd_warshall.md](10_floyd_warshall.md) (all-pairs path), [13_max_flow_min_cut.md](13_max_flow_min_cut.md) (min-cut theorem)

## Table of Contents
1. [What is MST?](#what-is-mst)
2. [Key Properties](#key-properties)
3. [Kruskal's Algorithm](#kruskals-algorithm)
4. [Prim's Algorithm](#prims-algorithm)
5. [Comparison](#comparison)
6. [Practice Problems](#practice-problems)

---

## What is MST?

A **Minimum Spanning Tree** is a subset of edges in a weighted, undirected graph that connects all vertices with the **minimum possible total edge weight** and contains **no cycles**.

```
┌─────────────────────────────────────────────────────────────────┐
│                    MINIMUM SPANNING TREE                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  PROPERTIES:                                                    │
│  ✅ Connects all vertices (spanning)                            │
│  ✅ Has no cycles (tree)                                        │
│  ✅ Minimum total weight (minimum)                              │
│  ✅ Contains exactly V-1 edges                                  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Visual Example

```
GRAPH:                    MST (Minimum):
                         
    1 ───10─── 2          1 ────── 2
    │╲        ╱│          │╲
   6│  ╲5    4│4         │  ╲
    │    ╲  ╱  │          │    ╲
    0 ────── 3          0 ───────── 3
       15                    
                         Total Weight: 1 + 6 + 4 = 11 (minimum!)
```

---

## Key Properties

### MST Properties

```
1. CUT PROPERTY
   - For any cut in the graph, the minimum weight edge crossing
     the cut is always part of SOME MST
   
2. CYCLIC PROPERTY  
   - The maximum weight edge in any cycle is never in any MST

3. UNIQUE MST
   - If all edge weights are distinct, MST is unique
   - If weights can repeat, multiple MSTs may exist
```

---

## Kruskal's Algorithm

### The Algorithm

```java
/*
 * KRUSKAL'S ALGORITHM:
 *
 * 1. Sort all edges by weight (ascending)
 * 2. For each edge (in order):
 *    a. If adding this edge creates a cycle, SKIP it
 *    b. Otherwise, ADD it to MST
 * 3. Stop when MST has V-1 edges
 *
 * Uses Union-Find to detect cycles!
 */
```

### Java Implementation

```java
package dsa.graph.mst;

import java.util.*;

public class KruskalMST {
    private List<Edge> edges;
    private int numVertices;

    public static class Edge implements Comparable<Edge> {
        int source;
        int destination;
        int weight;
        Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
    }

    public KruskalMST(int numVertices) {
        this.numVertices = numVertices;
        this.edges = new ArrayList<>();
    }

    public void addEdge(int source, int destination, int weight) {
        edges.add(new Edge(source, destination, weight));
    }

    public List<Edge> kruskal() {
        List<Edge> mst = new ArrayList<>();
        UnionFind uf = new UnionFind(numVertices);

        Collections.sort(edges);

        for (Edge edge : edges) {
            if (mst.size() == numVertices - 1) break;

            if (!uf.connected(edge.source, edge.destination)) {
                uf.union(edge.source, edge.destination);
                mst.add(edge);
            }
        }
        return mst;
    }

    public int getTotalWeight(List<Edge> mst) {
        return mst.stream().mapToInt(e -> e.weight).sum();
    }

    private static class UnionFind {
        private int[] parent;
        private int[] rank;
        UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) parent[i] = i;
        }
        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }
        void union(int x, int y) {
            int rx = find(x), ry = find(y);
            if (rx == ry) return;
            if (rank[rx] < rank[ry]) parent[rx] = ry;
            else if (rank[rx] > rank[ry]) parent[ry] = rx;
            else { parent[ry] = rx; rank[rx]++; }
        }
        boolean connected(int x, int y) { return find(x) == find(y); }
    }

    public static void main(String[] args) {
        KruskalMST graph = new KruskalMST(4);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 6);
        graph.addEdge(0, 3, 5);
        graph.addEdge(1, 3, 15);
        graph.addEdge(2, 3, 4);

        List<Edge> mst = graph.kruskal();
        System.out.println("Kruskal MST edges:");
        for (Edge e : mst) {
            System.out.println("  " + e.source + " - " + e.destination + " (weight: " + e.weight + ")");
        }
        System.out.println("Total weight: " + graph.getTotalWeight(mst));
    }
}
```

### Time Complexity: O(E log E)
### Space Complexity: O(V)

---

## Prim's Algorithm

### The Algorithm

```java
/*
 * PRIM'S ALGORITHM:
 *
 * 1. Start with any vertex, add it to MST
 * 2. While MST has fewer than V-1 edges:
 *    a. Find minimum weight edge connecting MST to non-MST vertex
 *    b. Add that edge and vertex to MST
 * 3. Use PriorityQueue to efficiently find minimum edge
 *
 * Grows MST like a tree from a starting vertex!
 */
```

### Java Implementation

```java
package dsa.graph.mst;

import java.util.*;

public class PrimMST {
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

    public PrimMST(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        adjList.get(source).add(new Edge(destination, weight));
        adjList.get(destination).add(new Edge(source, weight));
    }

    public List<int[]> prim() {
        List<int[]> mst = new ArrayList<>();
        boolean[] inMST = new boolean[numVertices];
        PriorityQueue<int[]> pq = new PriorityQueue<>(
            Comparator.comparingInt(a -> a[2])
        );

        pq.offer(new int[]{0, -1, 0});

        while (!pq.isEmpty() && mst.size() < numVertices - 1) {
            int[] current = pq.poll();
            int vertex = current[0];
            int parent = current[1];
            int weight = current[2];

            if (inMST[vertex]) continue;

            inMST[vertex] = true;
            if (parent != -1) {
                mst.add(new int[]{parent, vertex, weight});
            }

            for (Edge edge : adjList.get(vertex)) {
                if (!inMST[edge.destination]) {
                    pq.offer(new int[]{edge.destination, vertex, edge.weight});
                }
            }
        }
        return mst;
    }

    public static void main(String[] args) {
        PrimMST graph = new PrimMST(4);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 6);
        graph.addEdge(0, 3, 5);
        graph.addEdge(1, 3, 15);
        graph.addEdge(2, 3, 4);

        List<int[]> mst = graph.prim();
        System.out.println("Prim MST edges:");
        int total = 0;
        for (int[] e : mst) {
            System.out.println("  " + e[0] + " - " + e[1] + " (weight: " + e[2] + ")");
            total += e[2];
        }
        System.out.println("Total weight: " + total);
    }
}
```

### Time Complexity: O((V + E) log V)
### Space Complexity: O(V)

---

## Comparison

| Aspect | Kruskal's | Prim's |
|--------|-----------|--------|
| **Approach** | Edge-centric (sort edges) | Vertex-centric (grow tree) |
| **Data Structure** | Union-Find | Priority Queue |
| **Best For** | Sparse graphs | Dense graphs |
| **Time (Dense)** | O(E log E) | O((V+E) log V) |
| **Time (Sparse)** | O(E log E) | O((V+E) log V) |
| **Implementation** | Simpler | More complex |

---

## Practice Problems

### Easy
1. **Min Cost to Connect All Points** (LeetCode 1584)
2. **Find Critical and Pseudo-Critical Edges** (LeetCode 1489)

### Medium
1. **Number of Operations to Make Network Connected** (LeetCode 1319)
2. **Connecting Cities With Minimum Cost** (LeetCode 1135)

---

## Summary

```
┌─────────────────────────────────────────────────────────────────┐
│                    MINIMUM SPANNING TREE                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  🎯 PURPOSE: Connect all vertices with minimum total weight     │
│                                                                  │
│  📝 ALGORITHMS:                                                │
│     1. Kruskal's: Sort edges, add if no cycle (Union-Find)  │
│     2. Prim's: Grow MST from vertex, always add minimum edge   │
│                                                                  │
│  ⏱️ TIME: O(E log E) or O((V+E) log V)                     │
│  💾 SPACE: O(V)                                                │
│                                                                  │
│  🔑 KEY PROPERTIES:                                            │
│     - Cut Property: Min edge across cut is in MST              │
│     - Cycle Property: Max edge in cycle is never in MST        │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

> **Remember**: Kruskal's is simpler - sort edges and use Union-Find to avoid cycles. Prim's grows a tree from a starting vertex using a priority queue. Both give the same minimum total weight for MST!
