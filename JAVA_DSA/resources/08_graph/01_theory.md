# Graph Theory - Complete Guide

## Table of Contents
1. [What is a Graph?](#what-is-a-graph)
2. [Graph Terminology](#graph-terminology)
3. [Types of Graphs](#types-of-graphs)
4. [Graph Representations](#graph-representations)
5. [Building Graphs in Java](#building-graphs-in-java)
6. [Graph Traversal Overview](#graph-traversal-overview)
7. [Real-World Applications](#real-world-applications)
8. [Key Insights and Tips](#key-insights-and-tips)

---

## What is a Graph?

A **graph** is a non-linear data structure consisting of **vertices** (also called nodes) and **edges** that connect pairs of vertices. Unlike trees, graphs can have cycles and multiple connections between nodes.

### Real-World Analogies

```
┌─────────────────────────────────────────────────────────────────┐
│                    REAL-WORLD GRAPH EXAMPLES                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  🗺️  SOCIAL NETWORK          🛫  FLIGHT ROUTES         💻  INTERNET │
│                                                                  │
│     Alice ─── Bob              Mumbai ─── Delhi          Google ── Yahoo │
│      │╲    ╱│                 ╱│╲      │                 │╲      │╱ │
│      │ ╲  ╱ │                │ │╲     │                 │ ╲    │ ╱ │
│      │  ╲╱  │                │ │ ╲    │                 │  ╲   │  ╱ │
│     Carol   David           London Paris                Bing  Amazon │
│                                                                  │
│  Each person is a vertex,     Cities are vertices,      Websites are  │
│  friendships are edges         flights are edges         links are edges│
└─────────────────────────────────────────────────────────────────┘
```

### Why Graphs Matter

1. **Social Networks** - Facebook, LinkedIn connections
2. **Navigation** - Google Maps, shortest path
3. **Internet** - Web page links, routing
4. **Dependencies** - Package managers, build systems
5. **recommendations** - Netflix, Amazon product recommendations

---

## Graph Terminology

### Essential Terms

```
Vertex (Node)     : A point in the graph (A, B, C, D)
Edge              : A connection between two vertices
Adjacent/Nighbors : Vertices connected by an edge
Path              : Sequence of vertices connected by edges
Cycle             : A path that starts and ends at the same vertex
Degree            : Number of edges connected to a vertex
                  - In-degree: edges coming INTO a vertex
                  - Out-degree: edges going OUT of a vertex
```

### Visual Terminology

```
        A
       /│╲
      / │ ╲
     /  │  ╲
    B───C───D

Vertex A: degree = 3 (connected to B, C, D)
Vertex B: degree = 2 (connected to A, C)
Vertex C: degree = 4 (connected to A, B, D and self-loop? No, 3)

Path A → C → D: length = 2 edges
Cycle: A → B → C → A (3 edges, forms a triangle)
```

### More Terminology

| Term | Definition | Example |
|------|------------|---------|
| **Isolated Vertex** | No edges connected | Disconnected node |
| **Connected Graph** | Path exists between every pair | Fully connected network |
| **Disconnected Graph** | Some vertices unreachable | Split network |
| **Weighted Graph** | Edges have weights/costs | Distance, price |
| **Unweighted Graph** | All edges equal weight | Friend relationship |
| **Directed Graph** | Edges have direction | Follow on Twitter |
| **Undirected Graph** | Edges bidirectional | Facebook friends |
| **Cyclic Graph** | Contains at least one cycle | Circular dependencies |
| **Acyclic Graph** | No cycles | Tree structure |

---

## Types of Graphs

### 1. Based on Edge Direction

#### Undirected Graph
```java
/*
 * UNDIRECTED GRAPH
 *
 * Edges are bidirectional
 * If there's an edge A-B, you can travel A→B and B→A
 *
 *     0 ─── 1
 *     │╲    │
 *     │  ╲  │
 *     │    ╲│
 *     2 ─── 3
 *
 * Adjacency: 0 connects to {1, 2, 3}
 *            1 connects to {0, 3}
 *            2 connects to {0, 3}
 *            3 connects to {0, 1, 2}
 */
```

#### Directed Graph (Digraph)
```java
/*
 * DIRECTED GRAPH (Digraph)
 *
 * Edges have direction (arrows)
 * Edge A→B means you can travel A→B but NOT B→A
 *
 *        0 ──→ 1
 *        │      ↓
 *        ↓      2
 *        3 ←───
 *
 * Outgoing edges from 0: {1, 3}
 * Incoming edges to 0: {3}
 * Outgoing edges from 1: {2}
 * Incoming edges to 1: {0}
 */
```

### 2. Based on Weights

#### Unweighted Graph
```java
/*
 * UNWEIGHTED GRAPH
 *
 * All edges have the same weight (usually 1 or weightless)
 * Used for counting steps or hops
 *
 *     A ─── B ─── C
 *     │              (All edges = 1 step)
 *     D
 */
```

#### Weighted Graph
```java
/*
 * WEIGHTED GRAPH
 *
 * Edges have weights (distance, cost, time, etc.)
 *
 *         5
 *     A ─── B
 *    2│    3│
 *     D ─── C
 *        4
 *
 * Distance A→B directly: 5
 * Distance A→D: 2
 * Distance A→C via D: 2 + 4 = 6
 */
```

### 3. Special Graph Types

#### Complete Graph
```java
/*
 * COMPLETE GRAPH (Kn)
 *
 * Every vertex is connected to every other vertex
 * Number of edges = n(n-1)/2 for undirected, n(n-1) for directed
 *
 *     K4 (4 vertices, 6 edges)
 *
 *       0
 *      /│╲
 *     / │ ╲
 *    1───2───3
 */
```

#### Bipartite Graph
```java
/*
 * BIPARTITE GRAPH
 *
 * Vertices can be divided into two disjoint sets
 * All edges connect vertices from different sets
 *
 *     SET A: {0, 1}        SET B: {2, 3, 4}
 *
 *       0 ─── 2      0 ─── 4
 *       │            │
 *       1 ─── 3      1 ─── 2
 *
 * Used in: Job assignments, matching problems
 */
```

#### DAG (Directed Acyclic Graph)
```java
/*
 * DAG (Directed Acyclic Graph)
 *
 * Directed graph with NO cycles
 * Very important in scheduling, dependencies
 *
 *       A ─── B ─── D
 *       ↑
 *       C
 *       ↑
 *       E
 *
 * Topological order possible: E, C, A, B, D
 */
```

#### Tree (Special Graph)
```java
/*
 * TREE (Special Graph)
 *
 * - Connected
 * - Acyclic
 * - N vertices, N-1 edges
 * - Any two vertices connected by exactly one path
 *
 *       A (root)
 *      /│╲
 *     B C D
 *    ╱     ╲
 *   E       F
 *
 * All trees are bipartite!
 * All trees are DAGs!
 */
```

---

## Graph Representations

### 1. Adjacency Matrix

```java
/*
 * ADJACENCY MATRIX
 *
 * 2D array where matrix[i][j] = 1 means edge i→j exists
 *
 * For UNDIRECTED graph (symmetric):
 *
 *     0 ─── 1
 *     │╲    │
 *     │  ╲  │
 *     2───3 │
 *          ╲│
 *           4
 *
 *       0  1  2  3  4
 *    ┌────────────────
 *  0 │ 0  1  1  1  0
 *  1 │ 1  0  0  1  0
 *  2 │ 1  0  0  1  0
 *  3 │ 1  1  1  0  1
 *  4 │ 0  0  0  1  0
 *
 * Pros:
 *   ✅ O(1) edge lookup
 *   ✅ Easy to add/remove edges
 *
 * Cons:
 *   ❌ O(V²) space (V = number of vertices)
 *   ❌ O(V) to find neighbors of a vertex
 */
```

#### Java Implementation - Adjacency Matrix

```java
package dsa.graph.representation;

import java.util.*;

public class AdjacencyMatrix {
    private int[][] matrix;
    private int numVertices;
    private boolean isWeighted;
    private boolean isDirected;

    public AdjacencyMatrix(int numVertices, boolean isWeighted, boolean isDirected) {
        this.numVertices = numVertices;
        this.isWeighted = isWeighted;
        this.isDirected = isDirected;
        this.matrix = new int[numVertices][numVertices];
    }

    public void addEdge(int source, int destination, int weight) {
        if (isWeighted) {
            matrix[source][destination] = weight;
            if (!isDirected) {
                matrix[destination][source] = weight;
            }
        } else {
            matrix[source][destination] = 1;
            if (!isDirected) {
                matrix[destination][source] = 1;
            }
        }
    }

    public void removeEdge(int source, int destination) {
        matrix[source][destination] = 0;
        if (!isDirected) {
            matrix[destination][source] = 0;
        }
    }

    public boolean hasEdge(int source, int destination) {
        return matrix[source][destination] != 0;
    }

    public List<Integer> getNeighbors(int vertex) {
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            if (matrix[vertex][i] != 0) {
                neighbors.add(i);
            }
        }
        return neighbors;
    }

    public int getEdgeWeight(int source, int destination) {
        return matrix[source][destination];
    }

    public void print() {
        System.out.print("   ");
        for (int i = 0; i < numVertices; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < numVertices; i++) {
            System.out.print(i + ": ");
            for (int j = 0; j < numVertices; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        AdjacencyMatrix graph = new AdjacencyMatrix(5, false, false);

        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        System.out.println("Adjacency Matrix:");
        graph.print();

        System.out.println("\nNeighbors of vertex 0: " + graph.getNeighbors(0));
        System.out.println("Neighbors of vertex 3: " + graph.getNeighbors(3));
        System.out.println("Has edge 0->1? " + graph.hasEdge(0, 1));
        System.out.println("Has edge 1->0? " + graph.hasEdge(1, 0));
    }
}

/*
OUTPUT:
Adjacency Matrix:
   0 1 2 3 4
0: 0 1 1 1 0
1: 1 0 0 1 0
2: 1 0 0 1 0
3: 1 1 1 0 1
4: 0 0 0 1 0

Neighbors of vertex 0: [1, 2, 3]
Neighbors of vertex 3: [0, 1, 2, 4]
Has edge 0->1? true
Has edge 1->0? true
*/
```

### 2. Adjacency List

```java
/*
 * ADJACENCY LIST
 *
 * Each vertex maintains a list of its neighbors
 *
 *     0 ─── 1
 *     │╲    │
 *     │  ╲  │
 *     2───3 │
 *          ╲│
 *           4
 *
 * Adjacency List:
 * 0 -> [1, 2, 3]
 * 1 -> [0, 3]
 * 2 -> [0, 3]
 * 3 -> [0, 1, 2, 4]
 * 4 -> [3]
 *
 * Pros:
 *   ✅ O(V + E) space (E = number of edges)
 *   ✅ O(1) to iterate neighbors of a vertex
 *   ✅ Easy to add vertices
 *
 * Cons:
 *   ❌ Edge lookup is O(V) worst case
 *   ❌ Less cache-friendly than matrix
 */
```

#### Java Implementation - Adjacency List

```java
package dsa.graph.representation;

import java.util.*;

public class AdjacencyList {
    private Map<Integer, List<Integer>> adjacencyList;
    private Map<Integer, List<Edge>> weightedAdjacencyList;
    private boolean isWeighted;
    private boolean isDirected;

    private static class Edge {
        int destination;
        int weight;

        Edge(int destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "→" + destination + "(w:" + weight + ")";
        }
    }

    public AdjacencyList(boolean isWeighted, boolean isDirected) {
        this.isWeighted = isWeighted;
        this.isDirected = isDirected;
        this.adjacencyList = new HashMap<>();
        this.weightedAdjacencyList = new HashMap<>();
    }

    public void addVertex(int vertex) {
        if (!isWeighted) {
            adjacencyList.putIfAbsent(vertex, new ArrayList<>());
        } else {
            weightedAdjacencyList.putIfAbsent(vertex, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        if (isWeighted) {
            weightedAdjacencyList.putIfAbsent(source, new ArrayList<>());
            weightedAdjacencyList.get(source).add(new Edge(destination, weight));
            if (!isDirected) {
                weightedAdjacencyList.putIfAbsent(destination, new ArrayList<>());
                weightedAdjacencyList.get(destination).add(new Edge(source, weight));
            }
        } else {
            adjacencyList.putIfAbsent(source, new ArrayList<>());
            adjacencyList.get(source).add(destination);
            if (!isDirected) {
                adjacencyList.putIfAbsent(destination, new ArrayList<>());
                adjacencyList.get(destination).add(source);
            }
        }
    }

    public void removeEdge(int source, int destination) {
        if (isWeighted) {
            if (weightedAdjacencyList.containsKey(source)) {
                weightedAdjacencyList.get(source).removeIf(e -> e.destination == destination);
            }
            if (!isDirected && weightedAdjacencyList.containsKey(destination)) {
                weightedAdjacencyList.get(destination).removeIf(e -> e.destination == source);
            }
        } else {
            if (adjacencyList.containsKey(source)) {
                adjacencyList.get(source).remove((Integer) destination);
            }
            if (!isDirected && adjacencyList.containsKey(destination)) {
                adjacencyList.get(destination).remove((Integer) source);
            }
        }
    }

    public boolean hasEdge(int source, int destination) {
        if (isWeighted) {
            if (!weightedAdjacencyList.containsKey(source)) return false;
            return weightedAdjacencyList.get(source).stream()
                    .anyMatch(e -> e.destination == destination);
        } else {
            if (!adjacencyList.containsKey(source)) return false;
            return adjacencyList.get(source).contains(destination);
        }
    }

    public List<Integer> getNeighbors(int vertex) {
        if (isWeighted) {
            List<Integer> neighbors = new ArrayList<>();
            if (weightedAdjacencyList.containsKey(vertex)) {
                weightedAdjacencyList.get(vertex).forEach(e -> neighbors.add(e.destination));
            }
            return neighbors;
        }
        return adjacencyList.getOrDefault(vertex, new ArrayList<>());
    }

    public List<Edge> getWeightedNeighbors(int vertex) {
        return weightedAdjacencyList.getOrDefault(vertex, new ArrayList<>());
    }

    public int getEdgeWeight(int source, int destination) {
        if (!isWeighted) return 1;
        if (!weightedAdjacencyList.containsKey(source)) return -1;
        return weightedAdjacencyList.get(source).stream()
                .filter(e -> e.destination == destination)
                .findFirst()
                .map(e -> e.weight)
                .orElse(-1);
    }

    public void print() {
        if (isWeighted) {
            for (Map.Entry<Integer, List<Edge>> entry : weightedAdjacencyList.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        } else {
            for (Map.Entry<Integer, List<Integer>> entry : adjacencyList.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== UNWEIGHTED UNDIRECTED ===");
        AdjacencyList graph1 = new AdjacencyList(false, false);
        graph1.addEdge(0, 1);
        graph1.addEdge(0, 2);
        graph1.addEdge(0, 3);
        graph1.addEdge(1, 3);
        graph1.addEdge(2, 3);
        graph1.addEdge(3, 4);
        graph1.print();

        System.out.println("\n=== WEIGHTED DIRECTED ===");
        AdjacencyList graph2 = new AdjacencyList(true, true);
        graph2.addEdge(0, 1, 4);
        graph2.addEdge(0, 2, 2);
        graph2.addEdge(1, 2, 5);
        graph2.addEdge(1, 3, 1);
        graph2.addEdge(2, 3, 3);
        graph2.print();
    }
}

/*
OUTPUT:
=== UNWEIGHTED UNDIRECTED ===
0 -> [1, 2, 3]
1 -> [0, 3]
2 -> [0, 3]
3 -> [0, 1, 2, 4]
4 -> [3]

=== WEIGHTED DIRECTED ===
0 -> [→1(w:4), →2(w:2)]
1 -> [→2(w:5), →3(w:1)]
2 -> [→3(w:3)]
3 -> []
*/
```

### 3. Edge List

```java
/*
 * EDGE LIST
 *
 * List of all edges in the graph
 * Each edge is represented as (source, destination) or (source, destination, weight)
 *
 * For graph:
 *     0 ─── 1
 *     │
 *     2 ─── 3
 *
 * Edge List: [(0,1), (0,2), (2,3)]
 *
 * Pros:
 *   ✅ Simple representation
 *   ✅ O(E) space
 *   ✅ Good for sparse graphs
 *
 * Cons:
 *   ❌ Finding if edge exists is O(E)
 *   ❌ Finding neighbors is O(E)
 */
```

#### Java Implementation - Edge List

```java
package dsa.graph.representation;

import java.util.*;

public class EdgeList {
    private List<Edge> edges;
    private int numVertices;
    private boolean isWeighted;
    private boolean isDirected;

    private static class Edge {
        int source;
        int destination;
        int weight;

        Edge(int source, int destination) {
            this.source = source;
            this.destination = destination;
            this.weight = 1;
        }

        Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        @Override
        public String toString() {
            if (weight == 1) {
                return "(" + source + " → " + destination + ")";
            }
            return "(" + source + " → " + destination + ", w:" + weight + ")";
        }
    }

    public EdgeList(int numVertices, boolean isWeighted, boolean isDirected) {
        this.numVertices = numVertices;
        this.isWeighted = isWeighted;
        this.isDirected = isDirected;
        this.edges = new ArrayList<>();
    }

    public void addEdge(int source, int destination, int weight) {
        if (isWeighted) {
            edges.add(new Edge(source, destination, weight));
        } else {
            edges.add(new Edge(source, destination));
        }
        if (!isDirected && source != destination) {
            edges.add(new Edge(destination, source, weight));
        }
    }

    public boolean hasEdge(int source, int destination) {
        for (Edge edge : edges) {
            if (edge.source == source && edge.destination == destination) {
                return true;
            }
        }
        return false;
    }

    public List<Integer> getNeighbors(int vertex) {
        List<Integer> neighbors = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.source == vertex) {
                neighbors.add(edge.destination);
            }
        }
        return neighbors;
    }

    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }

    public void print() {
        System.out.println("Vertices: " + numVertices);
        System.out.println("Edges: " + edges);
    }

    public static void main(String[] args) {
        EdgeList graph = new EdgeList(4, false, false);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(2, 3);
        graph.print();

        System.out.println("\n=== WEIGHTED DIRECTED ===");
        EdgeList weighted = new EdgeList(4, true, true);
        weighted.addEdge(0, 1, 4);
        weighted.addEdge(0, 2, 2);
        weighted.addEdge(2, 3, 3);
        weighted.print();
    }
}

/*
OUTPUT:
Vertices: 4
Edges: [(0 → 1), (1 → 0), (0 → 2), (2 → 0), (2 → 3), (3 → 2)]

=== WEIGHTED DIRECTED ===
Vertices: 4
Edges: [(0 → 1, w:4), (0 → 2, w:2), (2 → 3, w:3)]
*/
```

---

## Building Graphs in Java

### Graph Class for Common Algorithms

```java
package dsa.graph;

import java.util.*;

public class Graph {
    private Map<Integer, List<Edge>> adjList;
    private int numVertices;
    private boolean isWeighted;
    private boolean isDirected;

    public static class Edge {
        int destination;
        int weight;

        Edge(int destination) {
            this.destination = destination;
            this.weight = 1;
        }

        Edge(int destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    public Graph(int numVertices) {
        this(numVertices, false, false);
    }

    public Graph(int numVertices, boolean isWeighted, boolean isDirected) {
        this.numVertices = numVertices;
        this.isWeighted = isWeighted;
        this.isDirected = isDirected;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        addEdge(source, destination, 1);
    }

    public void addEdge(int source, int destination, int weight) {
        adjList.get(source).add(new Edge(destination, weight));
        if (!isDirected) {
            adjList.get(destination).add(new Edge(source, weight));
        }
    }

    public List<Edge> getNeighbors(int vertex) {
        return adjList.getOrDefault(vertex, new ArrayList<>());
    }

    public List<Integer> getAllVertices() {
        return new ArrayList<>(adjList.keySet());
    }

    public int getNumVertices() {
        return numVertices;
    }

    public boolean isWeighted() {
        return isWeighted;
    }

    public boolean isDirected() {
        return isDirected;
    }

    public void printGraph() {
        for (Map.Entry<Integer, List<Edge>> entry : adjList.entrySet()) {
            System.out.print("Vertex " + entry.getKey() + " -> ");
            for (Edge edge : entry.getValue()) {
                System.out.print(edge.destination);
                if (isWeighted) {
                    System.out.print("(w:" + edge.weight + ")");
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public static Graph createSampleUndirected() {
        Graph graph = new Graph(5, false, false);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        return graph;
    }

    public static Graph createSampleDirected() {
        Graph graph = new Graph(5, false, true);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);
        graph.addEdge(4, 3);
        return graph;
    }

    public static Graph createWeightedDirected() {
        Graph graph = new Graph(5, true, true);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 8);
        graph.addEdge(2, 4, 10);
        graph.addEdge(3, 4, 2);
        return graph;
    }

    public static void main(String[] args) {
        System.out.println("=== UNDIRECTED GRAPH ===");
        Graph undirected = createSampleUndirected();
        undirected.printGraph();

        System.out.println("\n=== DIRECTED GRAPH ===");
        Graph directed = createSampleDirected();
        directed.printGraph();

        System.out.println("\n=== WEIGHTED DIRECTED GRAPH ===");
        Graph weighted = createWeightedDirected();
        weighted.printGraph();
    }
}

/*
OUTPUT:
=== UNDIRECTED GRAPH ===
Vertex 0 -> 1 2 3
Vertex 1 -> 0 3
Vertex 2 -> 0 3
Vertex 3 -> 0 1 2 4
Vertex 4 -> 3

=== DIRECTED GRAPH ===
Vertex 0 -> 1 2
Vertex 1 -> 2
Vertex 2 -> 3
Vertex 3 -> 1
Vertex 4 -> 3

=== WEIGHTED DIRECTED GRAPH ===
Vertex 0 -> 1(w:4) 2(w:2)
Vertex 1 -> 2(w:1) 3(w:5)
Vertex 2 -> 3(w:8) 4(w:10)
Vertex 3 -> 4(w:2)
Vertex 4 ->
*/
```

---

## Graph Traversal Overview

### Comparison of BFS vs DFS

```
┌────────────────────────────────────────────────────────────────────────┐
│                        BFS vs DFS COMPARISON                          │
├────────────────────────────────────────────────────────────────┬───────┤
│                          BFS (Breadth-First)                      │ DFS   │
├────────────────────────────────────────────────────────────────┼───────┤
│ Strategy                    │ Visit all neighbors first        │ DFS   │
│ Data Structure              │ Queue (FIFO)                     │ Stack │
│ Time Complexity             │ O(V + E)                         │ O(V+E)│
│ Space Complexity            │ O(V)                             │ O(V)  │
│ Shortest Path               │ ✅ Yes (unweighted)              │ ❌ No │
│ Memory for Sparse Graphs    │ Lower                            │ Higher│
│ Memory for Dense Graphs     │ Higher                           │ Lower │
│ Finding Connected Components│ Good                             │ Good  │
│ Topological Sort            │ ❌ No                            │ ✅ Yes │
│ Cycle Detection             │ ✅ Yes                           │ ✅ Yes│
└────────────────────────────────────────────────────────────────┴───────┘
```

### When to Use BFS

1. **Shortest path** in unweighted graphs
2. **Level-order traversal** in trees
3. **Finding connected components**
4. **Minimum steps/hops problems**
5. **Shortest path in terms of number of edges**

### When to Use DFS

1. **Topological sorting**
2. **Cycle detection**
3. **Path finding** (all paths, longest path)
4. **Maze solving**
5. **Connected components in undirected graphs**
6. **Tree traversal** (inorder, preorder, postorder)

---

## Real-World Applications

### 1. Social Networks

```java
/*
 * SOCIAL NETWORK GRAPH
 *
 * Vertices: Users
 * Edges: Friendships (undirected) or Follows (directed)
 *
 *     Alice ─── Bob ─── Charlie
 *      │                 │
 *      │                 │
 *     Dave ─────────── Elena
 *
 * Applications:
 * - Friend recommendations (BFS to find friends of friends)
 * - Degrees of separation (shortest path)
 * - Influencer detection (degree centrality)
 * - Mutual friends (intersection of neighbor lists)
 */
```

### 2. Google Maps / Navigation

```java
/*
 * NAVIGATION GRAPH
 *
 * Vertices: Locations/Intersections
 * Edges: Roads (weighted by distance/time)
 *
 * Applications:
 * - Shortest path (Dijkstra's, A*)
 * - Traffic-aware routing (dynamic weights)
 * - Delivery route optimization
 * - Public transit planning
 */
```

### 3. Package Dependencies

```java
/*
 * PACKAGE MANAGER GRAPH (DAG)
 *
 * Vertices: Packages
 * Edges: Dependencies
 *
 *     main ───┬─── util
 *             │
 *     logger ─┘
 *       │
 *     config ───┬─── file-io
 *              │
 *             net
 *
 * Topological sort gives installation order!
 */
```

### 4. Web Page Links

```java
/*
 * WEB GRAPH (DAG)
 *
 * Vertices: Web pages
 * Edges: Links between pages
 *
 * Applications:
 * - PageRank algorithm
 * - Web crawling
 * - Finding related pages
 */
```

### 5. Network Routing

```java
/*
 * NETWORK ROUTING GRAPH
 *
 * Vertices: Routers/Computers
 * Edges: Network connections
 *
 * Applications:
 * - Finding shortest path (RIP, OSPF)
 * - Finding most reliable path
 * - Load balancing
 */
```

---

## Key Insights and Tips

### Choosing Graph Representation

```
┌─────────────────────────────────────────────────────────────────┐
│              WHICH REPRESENTATION TO USE?                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ADJACENCY MATRIX - Use when:                                   │
│  ✅ Need O(1) edge lookup                                       │
│  ✅ Graph is dense (E ≈ V²)                                     │
│  ✅ Need to add/remove edges frequently                          │
│  ✅ Simple implementation preferred                             │
│                                                                  │
│  ADJACENCY LIST - Use when:                                     │
│  ✅ Graph is sparse (E << V²)                                    │
│  ✅ Need to iterate all neighbors efficiently                    │
│  ✅ Need to add vertices dynamically                             │
│  ✅ Most common for competitive programming                       │
│                                                                  │
│  EDGE LIST - Use when:                                          │
│  ✅ Need to sort edges by weight                                 │
│  ✅ Kruskal's MST algorithm                                       │
│  ✅ Very sparse graphs                                            │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Common Pitfalls

```java
// ❌ WRONG: Forgetting to handle disconnected components
public void dfsWrong(int start) {
    visited[start] = true;
    for (int neighbor : graph.getNeighbors(start)) {
        if (!visited[neighbor]) {  // May miss disconnected components!
            dfsWrong(neighbor);
        }
    }
}

// ✅ CORRECT: Iterate through ALL vertices
public void dfsCorrect() {
    visited = new boolean[numVertices];
    for (int i = 0; i < numVertices; i++) {
        if (!visited[i]) {
            dfsHelper(i);
        }
    }
}

// ❌ WRONG: Not handling self-loops in cycle detection
public boolean hasCycleWrong() {
    for (int i = 0; i < numVertices; i++) {
        if (dfsDetect(i, -1)) return true;  // -1 means no parent
    }
    return false;
}
```

### Java-Specific Tips

```java
// 1. Use HashMap/ArrayList for flexible adjacency lists
Map<Integer, List<Integer>> adjList = new HashMap<>();

// 2. Consider using Integer vs int for vertices
// int is faster, but Integer allows null and HashMap keys

// 3. For weighted graphs, create Edge class
private static class Edge {
    int destination;
    int weight;
    // Include constructor, getters
}

// 4. Handle edge cases
if (vertex < 0 || vertex >= numVertices) {
    throw new IllegalArgumentException("Invalid vertex");
}

// 5. For large graphs, consider using primitive arrays
int[] distances = new int[numVertices];  // Faster than Integer[]
Arrays.fill(distances, Integer.MAX_VALUE);
```

### Time and Space Complexities

| Operation | Adjacency Matrix | Adjacency List | Edge List |
|-----------|-----------------|----------------|-----------|
| Add Edge | O(1) | O(1) | O(1) amortized |
| Remove Edge | O(1) | O(V) | O(E) |
| Has Edge | O(1) | O(V) | O(E) |
| Get Neighbors | O(V) | O(degree) | O(E) |
| Space | O(V²) | O(V + E) | O(E) |

---

## Summary

```
KEY TAKEAWAYS:

1. GRAPH BASICS
   - Vertices (nodes) + Edges (connections) = Graph
   - Can be directed/undirected, weighted/unweighted
   - Can have cycles or be acyclic (DAG)

2. REPRESENTATIONS
   - Matrix: O(1) lookup, O(V²) space
   - List: O(V+E) space, good for sparse graphs
   - Edge: O(E) space, good for sorting

3. BUILDING BLOCKS
   - BFS: Queue, shortest path, level order
   - DFS: Stack, cycle detection, topological sort

4. COMMON ALGORITHMS
   - BFS/DFS: O(V + E)
   - Dijkstra: O((V+E) log V) with priority queue
   - Bellman-Ford: O(V * E)
   - Union-Find: Near O(1) with path compression
```

---

## Practice Problems

### Easy
1. Number of Islands (LeetCode 200)
2. Clone Graph (LeetCode 133)
3. Max Depth of Binary Tree (Level-order BFS)
4. Flood Fill (LeetCode 733)
5. Nearest Exit from Entrance in Maze (LeetCode 1926)

### Medium
1. Number of Connected Components (LeetCode 323)
2. Course Schedule (LeetCode 207)
3. Pacific Atlantic Water Flow (LeetCode 417)
4. Rotting Oranges (LeetCode 994)
5. Shortest Path in Binary Matrix (LeetCode 1293)

### Hard
1. Word Ladder (LeetCode 127)
2. Bus Routes (LeetCode 815)
3. Minimum Cost to Reach Destination (Flight scheduling)
4. Merge Accounts (LeetCode 721)
5. Longest Path With Minimum Edges

---

> **Remember**: Graphs are incredibly versatile. Master the basics of representation and traversal, and you'll be able to solve a wide variety of problems. Always consider whether your graph is directed/undirected and weighted/unweighted before choosing an algorithm.
