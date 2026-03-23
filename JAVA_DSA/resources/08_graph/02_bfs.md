# BFS (Breadth-First Search) - Complete Guide

## Table of Contents
1. [What is BFS?](#what-is-bfs)
2. [How BFS Works](#how-bfs-works)
3. [BFS Algorithm](#bfs-algorithm)
4. [BFS Implementation](#bfs-implementation)
5. [BFS Applications](#bfs-applications)
6. [Shortest Path Problems](#shortest-path-problems)
7. [Level-Order Traversal](#level-order-traversal)
8. [Common BFS Problems](#common-bfs-problems)
9. [Tips and Tricks](#tips-and-tricks)
10. [Complexity Analysis](#complexity-analysis)

---

## What is BFS?

**BFS (Breadth-First Search)** is a graph/tree traversal algorithm that explores all vertices at the **current depth before moving to vertices at the next depth level**. It uses a **Queue (FIFO)** data structure.

### Visual Explanation

```
GRAPH:
        0
       /│╲
      / │ ╲
     /  │  ╲
    1───2───3
         │
         4

BFS TRAVERSAL ORDER (starting from 0):

Level 0: [0]
         ↓
Level 1: [1, 2, 3]        (neighbors of 0)
         ↓
Level 2: [4]               (neighbors of 2)
         ↓
Level 3: []                (no more unvisited)

BFS Order: 0 → 1 → 2 → 3 → 4
```

### BFS vs DFS Visualization

```
Same Graph - Different Traversal:

        0
       /│╲
      / │ ╲
     /  │  ╲
    1───2───3
         │
         4

BFS (Level by Level):     DFS (Deep before Wide):
0, 1, 2, 3, 4            0, 1, 2, 4, 3
                         (one possible DFS order)
```

---

## How BFS Works

### Step-by-Step Process

```java
/*
 * BFS ALGORITHM STEPS:
 *
 * 1. Start with a source vertex
 * 2. Mark it as VISITED
 * 3. Add it to QUEUE
 * 4. While queue is NOT empty:
 *    a. Dequeue vertex
 *    b. Process it
 *    c. For each UNVISITED neighbor:
 *       - Mark as VISITED
 *       - Add to QUEUE
 */

Step 1: Start at 0, queue = [0]

        0
       /│╲
      / │ ╲
     /  │  ╲
    ?───?───?    ← All unvisited

Step 2: Dequeue 0, enqueue neighbors [1,2,3]
        queue = [1, 2, 3]

        0(visited)
       /│╲
      / │ ╲
     /  │  ╲
    1───2───3

Step 3: Dequeue 1, no unvisited neighbors
        queue = [2, 3]

        0(visited)
       /│╲
      / │ ╲
     /  │  ╲
    1✓──2───3

Step 4: Dequeue 2, enqueue neighbor [4]
        queue = [3, 4]

        0(visited)
       /│╲
      / │ ╲
     /  │  ╲
    1✓──2✓──3
          │
          4

Step 5: Dequeue 3, no unvisited neighbors
        queue = [4]

        0(visited)
       /│╲
      / │ ╲
     /  │  ╲
    1✓──2✓──3✓
          │
          4

Step 6: Dequeue 4, no unvisited neighbors
        queue = []

FINAL ORDER: 0 → 1 → 2 → 3 → 4
```

---

## BFS Algorithm

### Pseudocode

```
BFS(graph, source):
    create empty queue
    create visited array[vertices]

    visited[source] = true
    queue.enqueue(source)

    while queue is not empty:
        vertex = queue.dequeue()
        process(vertex)

        for each neighbor of vertex:
            if neighbor is not visited:
                visited[neighbor] = true
                queue.enqueue(neighbor)
```

### Java Implementation - Basic BFS

```java
package dsa.graph.traversal;

import java.util.*;

public class BFS {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;

    public BFS(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
        adjList.get(destination).add(source);  // Undirected graph
    }

    public void bfs(int start) {
        boolean[] visited = new boolean[numVertices];
        Queue<Integer> queue = new LinkedList<>();

        visited[start] = true;
        queue.offer(start);

        System.out.print("BFS Order: ");
        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            System.out.print(vertex + " ");

            for (int neighbor : adjList.get(vertex)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
        System.out.println();
    }

    public void bfsAllComponents() {
        boolean[] visited = new boolean[numVertices];
        int componentCount = 0;

        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                componentCount++;
                System.out.print("Component " + componentCount + ": ");
                bfsComponent(i, visited);
                System.out.println();
            }
        }
        System.out.println("Total components: " + componentCount);
    }

    private void bfsComponent(int start, boolean[] visited) {
        Queue<Integer> queue = new LinkedList<>();

        visited[start] = true;
        queue.offer(start);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            System.out.print(vertex + " ");

            for (int neighbor : adjList.get(vertex)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
    }

    public static void main(String[] args) {
        BFS graph = new BFS(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        graph.bfs(0);
        // Output: BFS Order: 0 1 2 3 4

        System.out.println("\n=== Disconnected Graph ===");
        BFS disconnected = new BFS(6);
        disconnected.addEdge(0, 1);
        disconnected.addEdge(0, 2);
        disconnected.addEdge(3, 4);
        // Vertex 5 is isolated

        disconnected.bfsAllComponents();
        // Output:
        // Component 1: 0 1 2
        // Component 2: 3 4
        // Total components: 3
    }
}
```

---

## BFS Implementation

### BFS with Distance Array

```java
package dsa.graph.traversal;

import java.util.*;

public class BFSWithDistance {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;

    public BFSWithDistance(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
        adjList.get(destination).add(source);
    }

    public int[] bfsWithDistance(int start) {
        int[] distance = new int[numVertices];
        Arrays.fill(distance, -1);

        Queue<Integer> queue = new LinkedList<>();
        distance[start] = 0;
        queue.offer(start);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();

            for (int neighbor : adjList.get(vertex)) {
                if (distance[neighbor] == -1) {
                    distance[neighbor] = distance[vertex] + 1;
                    queue.offer(neighbor);
                }
            }
        }
        return distance;
    }

    public void printDistances(int start) {
        int[] distances = bfsWithDistance(start);
        System.out.println("Distances from vertex " + start + ":");
        for (int i = 0; i < distances.length; i++) {
            System.out.println("  " + start + " → " + i + " = " + distances[i]);
        }
    }

    public static void main(String[] args) {
        BFSWithDistance graph = new BFSWithDistance(6);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);

        /*
         * Graph:
         *     0
         *    /│╲
         *   1 2 3
         *    ╲│  │
         *     3───4───5
         */

        graph.printDistances(0);
        /*
         * Output:
         * Distances from vertex 0:
         *   0 → 0 = 0
         *   0 → 1 = 1
         *   0 → 2 = 1
         *   0 → 3 = 2 (via 1 or 2)
         *   0 → 4 = 3
         *   0 → 5 = 4
         */
    }
}
```

### BFS with Parent/Path Reconstruction

```java
package dsa.graph.traversal;

import java.util.*;

public class BFSWithParent {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;

    public BFSWithParent(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
        adjList.get(destination).add(source);
    }

    public int[] bfs(int start) {
        int[] parent = new int[numVertices];
        Arrays.fill(parent, -1);

        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[numVertices];

        visited[start] = true;
        queue.offer(start);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();

            for (int neighbor : adjList.get(vertex)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    parent[neighbor] = vertex;
                    queue.offer(neighbor);
                }
            }
        }
        return parent;
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

    public List<Integer> findShortestPath(int source, int destination) {
        int[] parent = bfs(source);
        return getPath(parent, destination);
    }

    public static void main(String[] args) {
        BFSWithParent graph = new BFSWithParent(6);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);

        List<Integer> path = graph.findShortestPath(0, 5);
        System.out.println("Shortest path from 0 to 5: " + path);
        // Output: Shortest path from 0 to 5: [0, 1, 3, 4, 5]

        path = graph.findShortestPath(2, 1);
        System.out.println("Shortest path from 2 to 1: " + path);
        // Output: Shortest path from 2 to 1: [2, 0, 1]
    }
}
```

### BFS for Directed Graph

```java
package dsa.graph.traversal;

import java.util.*;

public class BFSDirected {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;

    public BFSDirected(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);  // Only add one direction!
    }

    public void bfs(int start) {
        boolean[] visited = new boolean[numVertices];
        Queue<Integer> queue = new LinkedList<>();

        visited[start] = true;
        queue.offer(start);

        System.out.print("BFS from " + start + ": ");
        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            System.out.print(vertex + " ");

            for (int neighbor : adjList.get(vertex)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
        System.out.println();
    }

    public void bfsAllReachable(int start) {
        boolean[] visited = new boolean[numVertices];
        Queue<Integer> queue = new LinkedList<>();
        int reachableCount = 0;

        visited[start] = true;
        queue.offer(start);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            reachableCount++;

            for (int neighbor : adjList.get(vertex)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }

        System.out.println("From " + start + ", reachable vertices: " + reachableCount);
    }

    public static void main(String[] args) {
        BFSDirected graph = new BFSDirected(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(3, 4);
        // Note: No path from 4 to others

        /*
         * Directed Graph:
         *     0 → 1 → 3 → 4
         *     ↓
         *     2
         */

        graph.bfs(0);
        // Output: BFS from 0: 0 1 2 3 4

        graph.bfs(4);
        // Output: BFS from 4: 4 (only itself!)

        graph.bfsAllReachable(0);
        // Output: From 0, reachable vertices: 5

        graph.bfsAllReachable(4);
        // Output: From 4, reachable vertices: 1
    }
}
```

---

## BFS Applications

### 1. Finding Connected Components

```java
package dsa.graph.applications;

import java.util.*;

public class ConnectedComponents {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;

    public ConnectedComponents(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
        adjList.get(destination).add(source);
    }

    public int countComponents() {
        boolean[] visited = new boolean[numVertices];
        int count = 0;

        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                count++;
                bfs(i, visited);
            }
        }
        return count;
    }

    private void bfs(int start, boolean[] visited) {
        Queue<Integer> queue = new LinkedList<>();
        visited[start] = true;
        queue.offer(start);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            for (int neighbor : adjList.get(vertex)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
    }

    public List<List<Integer>> getAllComponents() {
        boolean[] visited = new boolean[numVertices];
        List<List<Integer>> components = new ArrayList<>();

        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                List<Integer> component = new ArrayList<>();
                bfsCollect(i, visited, component);
                components.add(component);
            }
        }
        return components;
    }

    private void bfsCollect(int start, boolean[] visited, List<Integer> component) {
        Queue<Integer> queue = new LinkedList<>();
        visited[start] = true;
        queue.offer(start);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            component.add(vertex);

            for (int neighbor : adjList.get(vertex)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
    }

    public static void main(String[] args) {
        ConnectedComponents graph = new ConnectedComponents(7);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 2);
        graph.addEdge(3, 4);
        // 5 and 6 are isolated

        System.out.println("Number of components: " + graph.countComponents());
        // Output: Number of components: 3

        System.out.println("Components: " + graph.getAllComponents());
        // Output: Components: [[0, 1, 2], [3, 4], [5], [6]]
    }
}
```

### 2. Cycle Detection in Undirected Graph

```java
package dsa.graph.applications;

import java.util.*;

public class CycleDetectionUndirected {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;

    public CycleDetectionUndirected(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
        adjList.get(destination).add(source);
    }

    public boolean hasCycle() {
        boolean[] visited = new boolean[numVertices];

        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                if (bfsDetect(i, visited, -1)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean bfsDetect(int start, boolean[] visited, int parent) {
        Queue<int[]> queue = new LinkedList<>();  // [vertex, parent]
        visited[start] = true;
        queue.offer(new int[]{start, parent});

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int vertex = current[0];
            int par = current[1];

            for (int neighbor : adjList.get(vertex)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(new int[]{neighbor, vertex});
                } else if (neighbor != par) {
                    // If visited and not parent, we found a cycle!
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // Graph WITHOUT cycle
        CycleDetectionUndirected graph1 = new CycleDetectionUndirected(4);
        graph1.addEdge(0, 1);
        graph1.addEdge(1, 2);
        graph1.addEdge(2, 3);
        System.out.println("Graph 1 has cycle: " + graph1.hasCycle());
        // Output: false

        // Graph WITH cycle
        CycleDetectionUndirected graph2 = new CycleDetectionUndirected(4);
        graph2.addEdge(0, 1);
        graph2.addEdge(1, 2);
        graph2.addEdge(2, 0);  // Creates cycle: 0-1-2-0
        graph2.addEdge(2, 3);
        System.out.println("Graph 2 has cycle: " + graph2.hasCycle());
        // Output: true
    }
}
```

### 3. Bipartite Graph Check

```java
package dsa.graph.applications;

import java.util.*;

public class BipartiteCheck {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;
    private int[] color;  // -1 = uncolored, 0 = color A, 1 = color B

    public BipartiteCheck(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        this.color = new int[numVertices];
        Arrays.fill(color, -1);
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
        adjList.get(destination).add(source);
    }

    public boolean isBipartite() {
        Queue<Integer> queue = new LinkedList<>();

        for (int i = 0; i < numVertices; i++) {
            if (color[i] == -1) {
                color[i] = 0;
                queue.offer(i);

                while (!queue.isEmpty()) {
                    int vertex = queue.poll();

                    for (int neighbor : adjList.get(vertex)) {
                        if (color[neighbor] == -1) {
                            color[neighbor] = 1 - color[vertex];  // Alternate color
                            queue.offer(neighbor);
                        } else if (color[neighbor] == color[vertex]) {
                            return false;  // Same color adjacent = not bipartite
                        }
                    }
                }
            }
        }
        return true;
    }

    public void printPartition() {
        System.out.println("Set A (color 0): ");
        for (int i = 0; i < numVertices; i++) {
            if (color[i] == 0) System.out.print(i + " ");
        }
        System.out.println("\nSet B (color 1): ");
        for (int i = 0; i < numVertices; i++) {
            if (color[i] == 1) System.out.print(i + " ");
        }
    }

    public static void main(String[] args) {
        // Bipartite graph (even cycle)
        BipartiteCheck graph1 = new BipartiteCheck(4);
        graph1.addEdge(0, 1);
        graph1.addEdge(1, 2);
        graph1.addEdge(2, 3);
        graph1.addEdge(3, 0);
        System.out.println("Graph 1 is bipartite: " + graph1.isBipartite());
        // Output: true

        // Not bipartite (odd cycle)
        BipartiteCheck graph2 = new BipartiteCheck(3);
        graph2.addEdge(0, 1);
        graph2.addEdge(1, 2);
        graph2.addEdge(2, 0);  // Triangle - odd cycle
        System.out.println("Graph 2 is bipartite: " + graph2.isBipartite());
        // Output: false
    }
}
```

---

## Shortest Path Problems

### Shortest Path in Unweighted Graph

```java
package dsa.graph.shortestpath;

import java.util.*;

public class ShortestPathUnweighted {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;

    public ShortestPathUnweighted(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
        adjList.get(destination).add(source);
    }

    public int[] shortestDistance(int source) {
        int[] distance = new int[numVertices];
        Arrays.fill(distance, -1);

        Queue<Integer> queue = new LinkedList<>();
        distance[source] = 0;
        queue.offer(source);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            for (int neighbor : adjList.get(vertex)) {
                if (distance[neighbor] == -1) {
                    distance[neighbor] = distance[vertex] + 1;
                    queue.offer(neighbor);
                }
            }
        }
        return distance;
    }

    public List<Integer> shortestPath(int source, int destination) {
        int[] parent = new int[numVertices];
        Arrays.fill(parent, -1);

        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[numVertices];

        visited[source] = true;
        queue.offer(source);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();

            if (vertex == destination) break;

            for (int neighbor : adjList.get(vertex)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    parent[neighbor] = vertex;
                    queue.offer(neighbor);
                }
            }
        }

        // Reconstruct path
        List<Integer> path = new ArrayList<>();
        int current = destination;
        while (current != -1) {
            path.add(current);
            current = parent[current];
        }
        Collections.reverse(path);

        if (path.get(0) != source) {
            return Collections.emptyList();  // No path exists
        }
        return path;
    }

    public static void main(String[] args) {
        ShortestPathUnweighted graph = new ShortestPathUnweighted(7);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);
        graph.addEdge(5, 6);

        System.out.println("Shortest distances from 0:");
        int[] dist = graph.shortestDistance(0);
        for (int i = 0; i < dist.length; i++) {
            System.out.println("  0 → " + i + " = " + dist[i]);
        }

        System.out.println("\nShortest path from 0 to 6: " + graph.shortestPath(0, 6));
        // Output: [0, 3, 4, 5, 6] or [0, 3, 4, 5, 6]
    }
}
```

### Number of Shortest Paths

```java
package dsa.graph.shortestpath;

import java.util.*;

public class NumberOfShortestPaths {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;

    public NumberOfShortestPaths(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
        adjList.get(destination).add(source);
    }

    public long countShortestPaths(int source, int destination) {
        int[] distance = new int[numVertices];
        Arrays.fill(distance, -1);

        long[] pathCount = new long[numVertices];
        Arrays.fill(pathCount, 0);

        Queue<Integer> queue = new LinkedList<>();
        distance[source] = 0;
        pathCount[source] = 1;
        queue.offer(source);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();

            for (int neighbor : adjList.get(vertex)) {
                if (distance[neighbor] == -1) {
                    distance[neighbor] = distance[vertex] + 1;
                    pathCount[neighbor] = pathCount[vertex];
                    queue.offer(neighbor);
                } else if (distance[neighbor] == distance[vertex] + 1) {
                    pathCount[neighbor] += pathCount[vertex];
                }
            }
        }

        return pathCount[destination];
    }

    public static void main(String[] args) {
        NumberOfShortestPaths graph = new NumberOfShortestPaths(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        /*
         *     0
         *    /│╲
         *   1 2 │
         *    ╲│  │
         *     3───4
         *
         * Paths from 0 to 4:
         * 0→1→3→4
         * 0→2→3→4
         * Both have length 3
         */

        System.out.println("Number of shortest paths from 0 to 4: " +
            graph.countShortestPaths(0, 4));
        // Output: 2
    }
}
```

---

## Level-Order Traversal

### Binary Tree Level Order

```java
package dsa.graph.traversal;

import java.util.*;

public class LevelOrderTraversal {

    public static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> currentLevel = new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                currentLevel.add(node.val);

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            result.add(currentLevel);
        }
        return result;
    }

    public List<Integer> leftView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                if (i == 0) result.add(node.val);  // First node at each level
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }
        return result;
    }

    public List<Integer> rightView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                if (i == levelSize - 1) result.add(node.val);  // Last node at each level
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);

        /*
         *       1
         *      /│╲
         *     2  3 4
         *    /│  ││
         *   4 5  6 7
         */

        LevelOrderTraversal lot = new LevelOrderTraversal();

        System.out.println("Level Order: " + lot.levelOrder(root));
        // Output: [[1], [2, 3], [4, 5, 6, 7]]

        System.out.println("Left View: " + lot.leftView(root));
        // Output: [1, 2, 4]

        System.out.println("Right View: " + lot.rightView(root));
        // Output: [1, 3, 7]
    }
}
```

---

## Common BFS Problems

### 1. Number of Islands (LeetCode 200)

```java
package dsa.graph.problems;

import java.util.*;

public class NumberOfIslands {
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;

        int numRows = grid.length;
        int numCols = grid[0].length;
        int islands = 0;

        boolean[][] visited = new boolean[numRows][numCols];
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (grid[i][j] == '1' && !visited[i][j]) {
                    islands++;
                    bfsExplore(grid, visited, i, j, directions);
                }
            }
        }
        return islands;
    }

    private void bfsExplore(char[][] grid, boolean[][] visited,
                             int row, int col, int[][] directions) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{row, col});
        visited[row][col] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();

            for (int[] dir : directions) {
                int newRow = current[0] + dir[0];
                int newCol = current[1] + dir[1];

                if (isValid(newRow, newCol, grid.length, grid[0].length) &&
                    grid[newRow][newCol] == '1' && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    queue.offer(new int[]{newRow, newCol});
                }
            }
        }
    }

    private boolean isValid(int row, int col, int numRows, int numCols) {
        return row >= 0 && row < numRows && col >= 0 && col < numCols;
    }

    public static void main(String[] args) {
        char[][] grid = {
            {'1', '1', '0', '0', '0'},
            {'1', '1', '0', '0', '0'},
            {'0', '0', '1', '0', '0'},
            {'0', '0', '0', '1', '1'}
        };

        NumberOfIslands solution = new NumberOfIslands();
        System.out.println("Number of islands: " + solution.numIslands(grid));
        // Output: 3
    }
}
```

### 2. Rotting Oranges (LeetCode 994)

```java
package dsa.graph.problems;

import java.util.*;

public class RottingOranges {
    public int orangesRotting(int[][] grid) {
        if (grid == null || grid.length == 0) return -1;

        int rows = grid.length;
        int cols = grid[0].length;
        Queue<int[]> queue = new LinkedList<>();
        int freshOranges = 0;

        // Find all rotten oranges and count fresh oranges
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 2) {
                    queue.offer(new int[]{i, j, 0});  // row, col, time
                } else if (grid[i][j] == 1) {
                    freshOranges++;
                }
            }
        }

        if (freshOranges == 0) return 0;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int maxTime = 0;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0], col = current[1], time = current[2];
            maxTime = Math.max(maxTime, time);

            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];

                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols
                    && grid[newRow][newCol] == 1) {
                    grid[newRow][newCol] = 2;  // Rot!
                    freshOranges--;
                    queue.offer(new int[]{newRow, newCol, time + 1});
                }
            }
        }

        return freshOranges == 0 ? maxTime : -1;
    }

    public static void main(String[] args) {
        int[][] grid = {
            {2, 1, 1},
            {1, 1, 0},
            {0, 1, 1}
        };

        RottingOranges solution = new RottingOranges();
        System.out.println("Minutes to rot all oranges: " + solution.orangesRotting(grid));
        // Output: 4
    }
}
```

### 3. Minimum Knight Moves (LeetCode 1197)

```java
package dsa.graph.problems;

import java.util.*;

public class MinimumKnightMoves {
    public int minKnightMoves(int x, int y) {
        // Normalize to positive quadrant (chessboard is symmetric)
        x = Math.abs(x);
        y = Math.abs(y);

        // BFS from origin (0, 0)
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0, 0});  // x, y, moves

        Set<String> visited = new HashSet<>();
        visited.add("0,0");

        int[][] directions = {{2, 1}, {1, 2}, {-1, 2}, {-2, 1},
                              {-2, -1}, {-1, -2}, {1, -2}, {2, -1}};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currX = current[0], currY = current[1], moves = current[2];

            if (currX == x && currY == y) {
                return moves;
            }

            for (int[] dir : directions) {
                int newX = currX + dir[0];
                int newY = currY + dir[1];
                String key = newX + "," + newY;

                // Pruning: don't go too far in negative direction
                if (!visited.contains(key) && newX >= -1 && newY >= -1) {
                    visited.add(key);
                    queue.offer(new int[]{newX, newY, moves + 1});
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        MinimumKnightMoves solution = new MinimumKnightMoves();
        System.out.println("Min moves from (0,0) to (2,1): " + solution.minKnightMoves(2, 1));
        // Output: 1
        System.out.println("Min moves from (0,0) to (5,5): " + solution.minKnightMoves(5, 5));
        // Output: 4
    }
}
```

---

## Tips and Tricks

### 1. When to Use BFS

```java
/*
 * ✅ USE BFS WHEN:
 *
 * 1. Finding SHORTEST PATH (unweighted graph)
 *    → BFS guarantees shortest path in terms of edges
 *
 * 2. Finding MINIMUM STEPS/HOPS
 *    → Each BFS level = 1 step
 *
 * 3. Level-order traversal
 *    → Process all nodes at level L before level L+1
 *
 * 4. Finding CONNECTED COMPONENTS
 *    → Simple BFS from unvisited nodes
 *
 * 5. Cycle detection (undirected graphs)
 *    → If visited neighbor that isn't parent → cycle!
 *
 * 6. Bipartite graph checking
 *    → Color alternating levels
 *
 * ❌ DON'T USE BFS WHEN:
 *
 * 1. Finding LONGEST PATH
 *    → DFS is better for exhaustive search
 *
 * 2. Topological sorting
 *    → DFS is natural choice
 *
 * 3. Memory is tight and graph is dense
 *    → Consider DFS (less overhead)
 *
 * 4. Path existence only (don't need shortest)
 *    → DFS may be simpler
 */
```

### 2. BFS Optimization Tips

```java
// ❌ SLOW: Using ArrayList as queue
ArrayList<Integer> queue = new ArrayList<>();
queue.add(0);        // Add to end
queue.remove(0);    // Remove from front - O(n)!

// ✅ FAST: Using LinkedList as queue
Queue<Integer> queue = new LinkedList<>();
queue.offer(0);     // Add to end - O(1)
queue.poll();        // Remove from front - O(1)

// ❌ SLOW: Using Integer in tight loops
Integer visited = 0;  // Autoboxing overhead
if (!visitedSet.contains(vertex))  // Auto-unboxing overhead

// ✅ FAST: Using primitive int
int visited = 0;
if ((visitedMask & (1 << vertex)) == 0)  // Bit manipulation
```

### 3. BFS Common Patterns

```java
// Pattern 1: BFS with levels
public void bfsWithLevels(int start) {
    Queue<Integer> queue = new LinkedList<>();
    boolean[] visited = new boolean[numVertices];
    int level = 0;

    visited[start] = true;
    queue.offer(start);

    while (!queue.isEmpty()) {
        int size = queue.size();
        System.out.println("Level " + level + ":");

        for (int i = 0; i < size; i++) {
            int vertex = queue.poll();
            System.out.print(vertex + " ");

            for (int neighbor : adjList.get(vertex)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
        level++;
        System.out.println();
    }
}

// Pattern 2: Multi-source BFS
public int multiSourceBFS(List<Integer> sources) {
    Queue<int[]> queue = new LinkedList<>();
    boolean[] visited = new boolean[numVertices];
    int distance = 0;

    for (int source : sources) {
        visited[source] = true;
        queue.offer(new int[]{source, 0});
    }

    while (!queue.isEmpty()) {
        int[] current = queue.poll();
        int vertex = current[0], dist = current[1];

        for (int neighbor : adjList.get(vertex)) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                queue.offer(new int[]{neighbor, dist + 1});
            }
        }
    }
    return distance;
}
```

---

## Complexity Analysis

### Time Complexity

| Operation | Time | Explanation |
|-----------|------|-------------|
| Traversal | O(V + E) | Visit each vertex once, each edge once |
| Shortest Path | O(V + E) | BFS traversal + path reconstruction |
| Connected Components | O(V + E) | BFS from each unvisited vertex |
| Cycle Detection | O(V + E) | BFS traversal |

### Space Complexity

| Scenario | Space | Explanation |
|----------|-------|-------------|
| Basic BFS | O(V) | visited array + queue |
| With Parent | O(V) | visited + parent array |
| With Distance | O(V) | visited + distance array |
| Dense Graph | O(V²) | Adjacency matrix |
| Sparse Graph | O(V + E) | Adjacency list |

### BFS vs Other Algorithms

| Algorithm | Time | Space | Use Case |
|-----------|------|-------|----------|
| **BFS** | O(V + E) | O(V) | Shortest path (unweighted) |
| **DFS** | O(V + E) | O(V) | Path finding, cycle detection |
| **Dijkstra** | O((V+E) log V) | O(V) | Shortest path (weighted) |
| **Bellman-Ford** | O(V × E) | O(V) | Negative weights |

---

## Practice Problems

### Easy
1. **Number of Islands** (LeetCode 200)
2. **Maximum Depth of Binary Tree** (LeetCode 104)
3. **Flood Fill** (LeetCode 733)
4. **Clone Graph** (LeetCode 133)
5. **岛屿数量** (Number of Islands in Chinese)

### Medium
1. **Rotting Oranges** (LeetCode 994)
2. **Shortest Path in Binary Matrix** (LeetCode 1293)
3. **Nearest Exit from Entrance in Maze** (LeetCode 1926)
4. **Cut Off Trees for Golf Event** (LeetCode 675)
5. **Cheapest Flights Within K Stops** (LeetCode 787)

### Hard
1. **Word Ladder** (LeetCode 127)
2. **Bus Routes** (LeetCode 815)
3. **Minimum Knight Moves** (LeetCode 1197)
4. **Trapping Rain Water II** (LeetCode 407)
5. **Median of Matrix** (Hard)

---

## Summary

```
┌─────────────────────────────────────────────────────────────────┐
│                         BFS KEY POINTS                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  🏃 ALGORITHM:                                                  │
│     1. Use QUEUE (FIFO)                                        │
│     2. Mark visited when enqueueing (NOT when dequeuing)       │
│     3. Visit all neighbors before moving to next level         │
│                                                                  │
│  ⏱️ TIME: O(V + E)                                             │
│     - Every vertex visited once                                 │
│     - Every edge traversed once                                 │
│                                                                  │
│  💾 SPACE: O(V)                                                │
│     - visited array + queue                                      │
│                                                                  │
│  ✅ BEST FOR:                                                  │
│     - Shortest path (unweighted)                               │
│     - Minimum steps/problems                                    │
│     - Level-order traversal                                     │
│     - Connected components                                      │
│     - Cycle detection (undirected)                             │
│     - Bipartite checking                                       │
│                                                                  │
│  ❌ NOT FOR:                                                   │
│     - Weighted shortest path (use Dijkstra)                     │
│     - Topological sort (use DFS)                               │
│     - Finding all paths (use DFS)                              │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

> **Remember**: BFS explores level by level, making it perfect for shortest path problems in unweighted graphs. Always mark vertices as visited when you add them to the queue, not when you process them, to avoid duplicates!
