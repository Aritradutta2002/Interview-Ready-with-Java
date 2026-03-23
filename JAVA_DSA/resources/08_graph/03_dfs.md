# DFS (Depth-First Search) - Complete Guide

## Table of Contents
1. [What is DFS?](#what-is-dfs)
2. [How DFS Works](#how-dfs-works)
3. [DFS Algorithm](#dfs-algorithm)
4. [DFS Implementation](#dfs-implementation)
5. [DFS with Pre/Post Order](#dfs-with-prepost-order)
6. [Cycle Detection](#cycle-detection)
7. [Path Finding](#path-finding)
8. [Topological Sort with DFS](#topological-sort-with-dfs)
9. [Common DFS Problems](#common-dfs-problems)
10. [Tips and Tricks](#tips-and-tricks)
11. [Complexity Analysis](#complexity-analysis)

---

## What is DFS?

**DFS (Depth-First Search)** is a graph/tree traversal algorithm that explores as **deep as possible along each branch before backtracking**. It uses a **Stack (LIFO)** data structure, either implicitly via recursion or explicitly.

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

DFS TRAVERSAL ORDER (starting from 0):

DFS Order: 0 → 1 → backtrack → 2 → 4 → backtrack → 3

One possible DFS sequence: 0, 1, 2, 4, 3
```

### DFS vs BFS Visualization

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
0, 1, 2, 3, 4            0 → 1 → 2 → 4 → 3
                         (one possible DFS order)

BFS uses: Queue           DFS uses: Stack (or recursion)
```

---

## How DFS Works

### Step-by-Step Process (Recursive)

```java
/*
 * DFS ALGORITHM STEPS (RECURSIVE):
 *
 * 1. Start with a source vertex
 * 2. Mark it as VISITED
 * 3. Process it
 * 4. For each UNVISITED neighbor:
 *    a. Recursively call DFS on neighbor
 * 5. BACKTRACK when no unvisited neighbors
 */

Step 1: Start at 0, visited = {0}
        DFS(0)

Step 2: Process 0, call DFS(1) [first neighbor]
        DFS(0) → DFS(1)

        0(visited)
       /│╲
      / │ ╲
     /  │  ╲
    1───2───3    ← Exploring
         │
         4

Step 3: Process 1, no unvisited neighbors, BACKTRACK to 0
        DFS(1) returns to DFS(0)

        0(visited)
       /│╲
      / │ ╲
     /  │  ╲
    1✓──2───3    ← Done with 1
         │
         4

Step 4: Continue to next neighbor of 0, call DFS(2)
        DFS(0) → DFS(2)

        0(visited)
       /│╲
      / │ ╲
     /  │  ╲
    1✓──2───3    ← Exploring
         │
         4

Step 5: Process 2, call DFS(4) [first neighbor]
        DFS(2) → DFS(4)

        0(visited)
       /│╲
      / │ ╲
     /  │  ╲
    1✓──2✓───3
         │
         4          ← Exploring

Step 6: Process 4, no unvisited neighbors, BACKTRACK
        DFS(4) returns to DFS(2), DFS(2) returns to DFS(0)

Step 7: Continue to next neighbor of 0, call DFS(3)
        DFS(0) → DFS(3)

        0(visited)
       /│╲
      / │ ╲
     /  │  ╲
    1✓──2✓───3    ← Exploring
         │
         4✓

Step 8: Process 3, no unvisited neighbors, BACKTRACK to 0
        DFS(3) returns to DFS(0), DFS(0) complete

FINAL ORDER: 0 → 1 → 2 → 4 → 3 (one possible DFS order)
```

### Iterative DFS with Stack

```java
/*
 * ITERATIVE DFS STEPS:
 *
 * 1. Create empty stack
 * 2. Push source vertex
 * 3. While stack is NOT empty:
 *    a. Pop vertex
 *    b. If not visited:
 *       - Mark as visited
 *       - Process it
 *       - Push all unvisited neighbors
 */

Stack operations:

Push 0:           Stack: [0]
Pop 0, Push neighbors: Stack: [1, 2, 3]  (order depends on add)
Pop 3, Push neighbors: Stack: [1, 2] (3 has no unvisited neighbors)
Pop 2, Push neighbors: Stack: [1, 4]
Pop 4:              Stack: [1]
Pop 1:              Stack: []
```

---

## DFS Algorithm

### Pseudocode

```
DFS_RECURSIVE(graph, vertex, visited):
    visited[vertex] = true
    process(vertex)

    for each neighbor of vertex:
        if not visited[neighbor]:
            DFS_RECURSIVE(graph, neighbor, visited)


DFS_ITERATIVE(graph, source):
    create empty stack
    push source to stack

    while stack is not empty:
        vertex = pop stack
        if not visited[vertex]:
            visited[vertex] = true
            process(vertex)

            for each neighbor of vertex:
                if not visited[neighbor]:
                    push neighbor to stack
```

### Java Implementation - Basic DFS

```java
package dsa.graph.traversal;

import java.util.*;

public class DFS {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;

    public DFS(int numVertices) {
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

    // RECURSIVE DFS
    public void dfsRecursive(int start) {
        boolean[] visited = new boolean[numVertices];
        System.out.print("DFS (Recursive): ");
        dfsHelper(start, visited);
        System.out.println();
    }

    private void dfsHelper(int vertex, boolean[] visited) {
        visited[vertex] = true;
        System.out.print(vertex + " ");

        for (int neighbor : adjList.get(vertex)) {
            if (!visited[neighbor]) {
                dfsHelper(neighbor, visited);
            }
        }
    }

    // ITERATIVE DFS
    public void dfsIterative(int start) {
        boolean[] visited = new boolean[numVertices];
        Stack<Integer> stack = new Stack<>();
        List<Integer> result = new ArrayList<>();

        stack.push(start);

        while (!stack.isEmpty()) {
            int vertex = stack.pop();

            if (!visited[vertex]) {
                visited[vertex] = true;
                result.add(vertex);

                // Push neighbors in reverse order to maintain order
                List<Integer> neighbors = adjList.get(vertex);
                for (int i = neighbors.size() - 1; i >= 0; i--) {
                    if (!visited[neighbors.get(i)]) {
                        stack.push(neighbors.get(i));
                    }
                }
            }
        }

        System.out.print("DFS (Iterative): " + result);
        System.out.println();
    }

    // DFS FOR ALL COMPONENTS
    public void dfsAllComponents() {
        boolean[] visited = new boolean[numVertices];
        int componentCount = 0;

        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                componentCount++;
                System.out.print("Component " + componentCount + ": ");
                dfsHelper(i, visited);
                System.out.println();
            }
        }
        System.out.println("Total components: " + componentCount);
    }

    public static void main(String[] args) {
        DFS graph = new DFS(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        /*
         * Graph:
         *     0
         *    /│╲
         *   1 2 3
         *    ╲│  │
         *     2───4
         */

        graph.dfsRecursive(0);
        graph.dfsIterative(0);

        System.out.println("\n=== Disconnected Graph ===");
        DFS disconnected = new DFS(6);
        disconnected.addEdge(0, 1);
        disconnected.addEdge(0, 2);
        disconnected.addEdge(3, 4);
        // Vertex 5 is isolated

        disconnected.dfsAllComponents();
    }
}

/*
OUTPUT:
DFS (Recursive): 0 1 2 3 4
DFS (Iterative): [0, 1, 2, 3, 4]

=== Disconnected Graph ===
Component 1: 0 1 2
Component 2: 3 4
Component 3: 5
Total components: 3
*/
```

---

## DFS Implementation

### DFS with Discovery and Finish Times

```java
package dsa.graph.traversal;

import java.util.*;

public class DFSTimeStamps {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;
    private int time;
    private int[] discoveryTime;
    private int[] finishTime;
    private int[] parent;
    private String[] color;  // WHITE=unvisited, GRAY=in progress, BLACK=done

    public DFSTimeStamps(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
        this.discoveryTime = new int[numVertices];
        this.finishTime = new int[numVertices];
        this.parent = new int[numVertices];
        this.color = new String[numVertices];
        Arrays.fill(parent, -1);
        Arrays.fill(discoveryTime, -1);
        Arrays.fill(finishTime, -1);
        Arrays.fill(color, "WHITE");
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
        adjList.get(destination).add(source);
    }

    public void dfs() {
        time = 0;

        for (int i = 0; i < numVertices; i++) {
            if (color[i].equals("WHITE")) {
                dfsVisit(i);
            }
        }
    }

    private void dfsVisit(int u) {
        time++;
        discoveryTime[u] = time;
        color[u] = "GRAY";
        System.out.println("Discovery: " + u + " at time " + time);

        for (int v : adjList.get(u)) {
            if (color[v].equals("WHITE")) {
                parent[v] = u;
                System.out.println("  Tree edge: " + u + " -> " + v);
                dfsVisit(v);
            } else if (color[v].equals("GRAY")) {
                System.out.println("  Back edge: " + u + " -> " + v + " (cycle!)");
            } else if (discoveryTime[u] < discoveryTime[v]) {
                System.out.println("  Forward edge: " + u + " -> " + v);
            } else {
                System.out.println("  Cross edge: " + u + " -> " + v);
            }
        }

        color[u] = "BLACK";
        time++;
        finishTime[u] = time;
        System.out.println("Finish: " + u + " at time " + time);
    }

    public void printTimes() {
        System.out.println("\nVertex | Discovery | Finish | Parent");
        System.out.println("-------|-----------|--------|-------");
        for (int i = 0; i < numVertices; i++) {
            String parentStr = parent[i] == -1 ? "N/A" : String.valueOf(parent[i]);
            System.out.printf("%7d|%11d|%8d|%7s%n", i, discoveryTime[i], finishTime[i], parentStr);
        }
    }

    public static void main(String[] args) {
        DFSTimeStamps graph = new DFSTimeStamps(6);
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
         *     3──4──5
         */

        graph.dfs();
        graph.printTimes();
    }
}

/*
OUTPUT:
Discovery: 0 at time 1
  Tree edge: 0 -> 1
Discovery: 1 at time 2
  Tree edge: 1 -> 3
Discovery: 3 at time 3
  Tree edge: 3 -> 4
Discovery: 4 at time 4
  Tree edge: 4 -> 5
Finish: 5 at time 5
Finish: 4 at time 6
Finish: 3 at time 7
  Back edge: 2 -> 3 (cycle!)
Finish: 1 at time 8
  Tree edge: 0 -> 2
Discovery: 2 at time 9
Finish: 2 at time 10
Finish: 0 at time 11

Vertex | Discovery | Finish | Parent
-------|-----------|--------|-------
      0|          1|      11|    N/A
      1|          2|       8|      0
      2|          9|      10|      0
      3|          3|       7|      1
      4|          4|       6|      3
      5|          5|       5|      4
*/
```

### DFS for Directed Graph

```java
package dsa.graph.traversal;

import java.util.*;

public class DFSDirected {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;

    public DFSDirected(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);  // Only one direction!
    }

    public void dfs(int start) {
        boolean[] visited = new boolean[numVertices];
        System.out.print("DFS from " + start + ": ");
        dfsHelper(start, visited);
        System.out.println();
    }

    private void dfsHelper(int vertex, boolean[] visited) {
        visited[vertex] = true;
        System.out.print(vertex + " ");

        for (int neighbor : adjList.get(vertex)) {
            if (!visited[neighbor]) {
                dfsHelper(neighbor, visited);
            }
        }
    }

    public void dfsAllReachable(int start) {
        boolean[] visited = new boolean[numVertices];
        List<Integer> reachable = new ArrayList<>();
        dfsCollect(start, visited, reachable);
        System.out.println("Reachable from " + start + ": " + reachable);
    }

    private void dfsCollect(int vertex, boolean[] visited, List<Integer> result) {
        visited[vertex] = true;
        result.add(vertex);

        for (int neighbor : adjList.get(vertex)) {
            if (!visited[neighbor]) {
                dfsCollect(neighbor, visited, result);
            }
        }
    }

    public static void main(String[] args) {
        DFSDirected graph = new DFSDirected(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(3, 4);
        graph.addEdge(2, 3);

        /*
         * Directed Graph:
         *     0 → 1
         *     ↓   ↓
         *     2 → 3 → 4
         */

        graph.dfs(0);
        graph.dfs(4);  // From isolated vertex
        graph.dfsAllReachable(0);
        graph.dfsAllReachable(4);
    }
}

/*
OUTPUT:
DFS from 0: 0 1 3 4 2
DFS from 4: 4
Reachable from 0: [0, 1, 3, 4, 2]
Reachable from 4: [4]
*/
```

---

## DFS with Pre/Post Order

### Tree Traversals (DFS Variants)

```java
package dsa.graph.traversal;

import java.util.*;

public class TreeDFSOrders {

    public static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // PREORDER: Process before children (Root, Left, Right)
    public List<Integer> preorder(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        preorderHelper(root, result);
        return result;
    }

    private void preorderHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        result.add(node.val);                    // Process ROOT first
        preorderHelper(node.left, result);      // Then LEFT
        preorderHelper(node.right, result);     // Then RIGHT
    }

    // INORDER: Process between children (Left, Root, Right)
    public List<Integer> inorder(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private void inorderHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        inorderHelper(node.left, result);      // Left first
        result.add(node.val);                   // Then ROOT
        inorderHelper(node.right, result);     // Then RIGHT
    }

    // POSTORDER: Process after children (Left, Right, Root)
    public List<Integer> postorder(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        postorderHelper(root, result);
        return result;
    }

    private void postorderHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        postorderHelper(node.left, result);    // Left first
        postorderHelper(node.right, result);   // Then RIGHT
        result.add(node.val);                  // Then ROOT
    }

    public static void main(String[] args) {
        /*
         * Tree:
         *         1
         *        /│╲
         *       2 3 4
         *      /│  │
         *     5 6  7
         */

        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(5);
        root.left.right = new TreeNode(6);
        root.right.left = new TreeNode(7);

        TreeDFSOrders solver = new TreeDFSOrders();

        System.out.println("Preorder:  " + solver.preorder(root));
        // Output: [1, 2, 5, 6, 3, 7]

        System.out.println("Inorder:   " + solver.inorder(root));
        // Output: [5, 2, 6, 1, 7, 3]

        System.out.println("Postorder: " + solver.postorder(root));
        // Output: [5, 6, 2, 7, 3, 1]
    }
}
```

### Iterative Tree Traversals

```java
package dsa.graph.traversal;

import java.util.*;

public class IterativeTreeTraversals {

    public static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // ITERATIVE PREORDER
    public List<Integer> preorderIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.add(node.val);

            if (node.right != null) stack.push(node.right);  // Push right first
            if (node.left != null) stack.push(node.left);   // Push left second
        }
        return result;
    }

    // ITERATIVE INORDER
    public List<Integer> inorderIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;

        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
            current = stack.pop();
            result.add(current.val);
            current = current.right;
        }
        return result;
    }

    // ITERATIVE POSTORDER (Two Stack Method)
    public List<Integer> postorderIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Stack<TreeNode> stack1 = new Stack<>();
        Stack<TreeNode> stack2 = new Stack<>();

        stack1.push(root);
        while (!stack1.isEmpty()) {
            TreeNode node = stack1.pop();
            stack2.push(node);

            if (node.left != null) stack1.push(node.left);
            if (node.right != null) stack1.push(node.right);
        }

        while (!stack2.isEmpty()) {
            result.add(stack2.pop().val);
        }
        return result;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);

        IterativeTreeTraversals solver = new IterativeTreeTraversals();

        System.out.println("Preorder:  " + solver.preorderIterative(root));
        System.out.println("Inorder:   " + solver.inorderIterative(root));
        System.out.println("Postorder: " + solver.postorderIterative(root));
    }
}
```

---

## Cycle Detection

### Cycle Detection in Undirected Graph

```java
package dsa.graph.applications;

import java.util.*;

public class CycleDetectionUndirectedDFS {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;

    public CycleDetectionUndirectedDFS(int numVertices) {
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

        // Check all components
        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                if (dfsDetect(i, visited, -1)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfsDetect(int vertex, boolean[] visited, int parent) {
        visited[vertex] = true;

        for (int neighbor : adjList.get(vertex)) {
            if (!visited[neighbor]) {
                if (dfsDetect(neighbor, visited, vertex)) {
                    return true;
                }
            } else if (neighbor != parent) {
                // Found a visited node that's not the parent = cycle!
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // Graph WITHOUT cycle (tree)
        CycleDetectionUndirectedDFS graph1 = new CycleDetectionUndirectedDFS(4);
        graph1.addEdge(0, 1);
        graph1.addEdge(1, 2);
        graph1.addEdge(2, 3);
        System.out.println("Tree has cycle: " + graph1.hasCycle());
        // Output: false

        // Graph WITH cycle
        CycleDetectionUndirectedDFS graph2 = new CycleDetectionUndirectedDFS(4);
        graph2.addEdge(0, 1);
        graph2.addEdge(1, 2);
        graph2.addEdge(2, 0);  // Creates cycle: 0-1-2-0
        graph2.addEdge(2, 3);
        System.out.println("Graph with cycle has cycle: " + graph2.hasCycle());
        // Output: true
    }
}
```

### Cycle Detection in Directed Graph

```java
package dsa.graph.applications;

import java.util.*;

public class CycleDetectionDirectedDFS {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;

    // WHITE = unvisited, GRAY = in current path, BLACK = fully processed
    private String[] color;

    public CycleDetectionDirectedDFS(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        this.color = new String[numVertices];
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
            color[i] = "WHITE";
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
    }

    public boolean hasCycle() {
        // Reset colors
        Arrays.fill(color, "WHITE");

        // Check all components
        for (int i = 0; i < numVertices; i++) {
            if (color[i].equals("WHITE")) {
                if (dfsDetect(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfsDetect(int vertex) {
        color[vertex] = "GRAY";  // Mark as being processed

        for (int neighbor : adjList.get(vertex)) {
            if (color[neighbor].equals("GRAY")) {
                // Found a GRAY node in current path = cycle!
                return true;
            }
            if (color[neighbor].equals("WHITE")) {
                if (dfsDetect(neighbor)) {
                    return true;
                }
            }
        }

        color[vertex] = "BLACK";  // Mark as fully processed
        return false;
    }

    // Find the cycle path
    public List<Integer> findCyclePath() {
        Arrays.fill(color, "WHITE");
        int[] parent = new int[numVertices];
        Arrays.fill(parent, -1);

        for (int i = 0; i < numVertices; i++) {
            if (color[i].equals("WHITE")) {
                List<Integer> cycle = dfsFindCycle(i, parent);
                if (cycle != null) {
                    return cycle;
                }
            }
        }
        return null;
    }

    private List<Integer> dfsFindCycle(int vertex, int[] parent) {
        color[vertex] = "GRAY";

        for (int neighbor : adjList.get(vertex)) {
            if (color[neighbor].equals("GRAY")) {
                // Found cycle! Reconstruct path
                List<Integer> cycle = new ArrayList<>();
                cycle.add(neighbor);
                int current = vertex;
                while (current != neighbor) {
                    cycle.add(current);
                    current = parent[current];
                }
                cycle.add(neighbor);
                Collections.reverse(cycle);
                return cycle;
            }
            if (color[neighbor].equals("WHITE")) {
                parent[neighbor] = vertex;
                List<Integer> cycle = dfsFindCycle(neighbor, parent);
                if (cycle != null) {
                    return cycle;
                }
            }
        }

        color[vertex] = "BLACK";
        return null;
    }

    public static void main(String[] args) {
        // DAG (no cycle)
        CycleDetectionDirectedDFS graph1 = new CycleDetectionDirectedDFS(4);
        graph1.addEdge(0, 1);
        graph1.addEdge(0, 2);
        graph1.addEdge(1, 3);
        graph1.addEdge(2, 3);
        System.out.println("DAG has cycle: " + graph1.hasCycle());
        // Output: false

        // Graph WITH cycle
        CycleDetectionDirectedDFS graph2 = new CycleDetectionDirectedDFS(4);
        graph2.addEdge(0, 1);
        graph2.addEdge(1, 2);
        graph2.addEdge(2, 3);
        graph2.addEdge(3, 1);  // Creates cycle: 1-2-3-1
        System.out.println("Graph with cycle has cycle: " + graph2.hasCycle());
        // Output: true

        List<Integer> cycle = graph2.findCyclePath();
        System.out.println("Cycle path: " + cycle);
        // Output: [1, 2, 3, 1]
    }
}
```

---

## Path Finding

### Find if Path Exists

```java
package dsa.graph.applications;

import java.util.*;

public class PathFindingDFS {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;

    public PathFindingDFS(int numVertices) {
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

    public boolean hasPath(int source, int destination) {
        boolean[] visited = new boolean[numVertices];
        return dfsPath(source, destination, visited);
    }

    private boolean dfsPath(int current, int destination, boolean[] visited) {
        if (current == destination) return true;
        if (visited[current]) return false;

        visited[current] = true;

        for (int neighbor : adjList.get(current)) {
            if (dfsPath(neighbor, destination, visited)) {
                return true;
            }
        }
        return false;
    }

    public List<Integer> findPath(int source, int destination) {
        boolean[] visited = new boolean[numVertices];
        List<Integer> path = new ArrayList<>();
        int[] parent = new int[numVertices];
        Arrays.fill(parent, -1);

        dfsPathCollect(source, destination, visited, parent);

        if (parent[destination] == -1 && source != destination) {
            return Collections.emptyList();  // No path
        }

        // Reconstruct path
        int current = destination;
        while (current != -1) {
            path.add(current);
            current = parent[current];
        }
        Collections.reverse(path);
        return path;
    }

    private boolean dfsPathCollect(int current, int destination,
                                   boolean[] visited, int[] parent) {
        if (current == destination) return true;
        if (visited[current]) return false;

        visited[current] = true;

        for (int neighbor : adjList.get(current)) {
            if (!visited[neighbor]) {
                parent[neighbor] = current;
                if (dfsPathCollect(neighbor, destination, visited, parent)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Find ALL paths between source and destination
    public List<List<Integer>> findAllPaths(int source, int destination) {
        List<List<Integer>> allPaths = new ArrayList<>();
        List<Integer> currentPath = new ArrayList<>();
        boolean[] visited = new boolean[numVertices];

        dfsAllPaths(source, destination, visited, currentPath, allPaths);
        return allPaths;
    }

    private void dfsAllPaths(int current, int destination,
                            boolean[] visited, List<Integer> currentPath,
                            List<List<Integer>> allPaths) {
        visited[current] = true;
        currentPath.add(current);

        if (current == destination) {
            allPaths.add(new ArrayList<>(currentPath));
        } else {
            for (int neighbor : adjList.get(current)) {
                if (!visited[neighbor]) {
                    dfsAllPaths(neighbor, destination, visited, currentPath, allPaths);
                }
            }
        }

        currentPath.remove(currentPath.size() - 1);
        visited[current] = false;  // Backtrack
    }

    public static void main(String[] args) {
        PathFindingDFS graph = new PathFindingDFS(6);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);

        System.out.println("Path from 0 to 5: " + graph.hasPath(0, 5));
        // Output: true

        System.out.println("Path from 5 to 0: " + graph.hasPath(5, 0));
        // Output: true (undirected)

        System.out.println("Path from 0 to 5: " + graph.findPath(0, 5));
        // Output: [0, 1, 3, 4, 5] or [0, 2, 3, 4, 5]

        System.out.println("All paths from 0 to 5:");
        for (List<Integer> path : graph.findAllPaths(0, 5)) {
            System.out.println("  " + path);
        }
    }
}
```

### Longest Path in DAG

```java
package dsa.graph.applications;

import java.util.*;

public class LongestPathDAG {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;
    private int[] indegree;

    public LongestPathDAG(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        this.indegree = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        adjList.get(source).add(destination);
        indegree[destination]++;
    }

    public int[] longestPath(int source) {
        int[] dist = new int[numVertices];
        Arrays.fill(dist, Integer.MIN_VALUE);
        dist[source] = 0;

        // Topological sort using Kahn's algorithm
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numVertices; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }

        while (!queue.isEmpty()) {
            int vertex = queue.poll();

            for (int neighbor : adjList.get(vertex)) {
                if (dist[vertex] != Integer.MIN_VALUE) {
                    dist[neighbor] = Math.max(dist[neighbor], dist[vertex] + 1);
                }
                indegree[neighbor]--;
                if (indegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        return dist;
    }

    public static void main(String[] args) {
        LongestPathDAG graph = new LongestPathDAG(6);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 5, 1);

        int[] distances = graph.longestPath(0);
        System.out.println("Longest distances from 0:");
        for (int i = 0; i < distances.length; i++) {
            System.out.println("  0 → " + i + " = " + distances[i]);
        }
    }
}
```

---

## Topological Sort with DFS

```java
package dsa.graph.applications;

import java.util.*;

public class TopologicalSortDFS {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;
    private Stack<Integer> stack;
    private String[] color;

    public TopologicalSortDFS(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        this.stack = new Stack<>();
        this.color = new String[numVertices];
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
            color[i] = "WHITE";
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
    }

    public List<Integer> topologicalSort() {
        // Check for cycle first
        for (int i = 0; i < numVertices; i++) {
            if (color[i].equals("WHITE")) {
                if (hasCycle(i)) {
                    return Collections.emptyList();  // Cycle exists!
                }
            }
        }

        // Reset colors for actual DFS
        Arrays.fill(color, "WHITE");
        stack.clear();

        for (int i = 0; i < numVertices; i++) {
            if (color[i].equals("WHITE")) {
                dfsSort(i);
            }
        }

        // Pop from stack to get topological order
        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }

    private void dfsSort(int vertex) {
        color[vertex] = "GRAY";

        for (int neighbor : adjList.get(vertex)) {
            if (color[neighbor].equals("GRAY")) {
                throw new IllegalStateException("Cycle detected!");
            }
            if (color[neighbor].equals("WHITE")) {
                dfsSort(neighbor);
            }
        }

        color[vertex] = "BLACK";
        stack.push(vertex);  // Push after processing all neighbors
    }

    // Detect cycle helper
    private boolean hasCycle(int vertex) {
        color[vertex] = "GRAY";

        for (int neighbor : adjList.get(vertex)) {
            if (color[neighbor].equals("GRAY")) {
                return true;  // Back edge = cycle
            }
            if (color[neighbor].equals("WHITE") && hasCycle(neighbor)) {
                return true;
            }
        }

        color[vertex] = "BLACK";
        return false;
    }

    public static void main(String[] args) {
        TopologicalSortDFS graph = new TopologicalSortDFS(6);
        graph.addEdge(0, 1);  // Task 0 must come before Task 1
        graph.addEdge(0, 2);  // Task 0 must come before Task 2
        graph.addEdge(1, 3);  // Task 1 must come before Task 3
        graph.addEdge(2, 3);  // Task 2 must come before Task 3
        graph.addEdge(3, 4);  // Task 3 must come before Task 4
        graph.addEdge(4, 5);  // Task 4 must come before Task 5

        /*
         * Graph:
         *     0 ──┬── 1 ── 3 ── 4 ── 5
         *         │
         *         └──── 2 ──────────── ↑
         *
         * One topological order: 0, 1, 2, 3, 4, 5
         */

        List<Integer> order = graph.topologicalSort();
        System.out.println("Topological Order: " + order);
        // Output: [0, 2, 1, 3, 4, 5] (or any valid order)

        // Another example - Course Schedule
        System.out.println("\n=== Course Schedule ===");
        TopologicalSortDFS courses = new TopologicalSortDFS(4);
        courses.addEdge(1, 0);  // Course 1 requires Course 0
        courses.addEdge(2, 0);  // Course 2 requires Course 0
        courses.addEdge(3, 2);  // Course 3 requires Course 2
        courses.addEdge(3, 1);  // Course 3 requires Course 1

        order = courses.topologicalSort();
        System.out.println("Course Order: " + order);
    }
}
```

---

## Common DFS Problems

### 1. Number of Islands (DFS Version)

```java
package dsa.graph.problems;

import java.util.*;

public class NumberOfIslandsDFS {
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;

        int numRows = grid.length;
        int numCols = grid[0].length;
        int islands = 0;

        boolean[][] visited = new boolean[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (grid[i][j] == '1' && !visited[i][j]) {
                    islands++;
                    dfsExplore(grid, visited, i, j, numRows, numCols);
                }
            }
        }
        return islands;
    }

    private void dfsExplore(char[][] grid, boolean[][] visited,
                           int row, int col, int numRows, int numCols) {
        if (row < 0 || row >= numRows || col < 0 || col >= numCols
            || grid[row][col] == '0' || visited[row][col]) {
            return;
        }

        visited[row][col] = true;

        // Explore all 4 directions
        dfsExplore(grid, visited, row + 1, col, numRows, numCols);  // Down
        dfsExplore(grid, visited, row - 1, col, numRows, numCols);  // Up
        dfsExplore(grid, visited, row, col + 1, numRows, numCols);  // Right
        dfsExplore(grid, visited, row, col - 1, numRows, numCols);  // Left
    }

    public static void main(String[] args) {
        char[][] grid = {
            {'1', '1', '0', '0', '0'},
            {'1', '1', '0', '0', '0'},
            {'0', '0', '1', '0', '0'},
            {'0', '0', '0', '1', '1'}
        };

        NumberOfIslandsDFS solution = new NumberOfIslandsDFS();
        System.out.println("Number of islands: " + solution.numIslands(grid));
        // Output: 3
    }
}
```

### 2. Flood Fill (LeetCode 733)

```java
package dsa.graph.problems;

public class FloodFillDFS {
    public int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
        int originalColor = image[sr][sc];
        if (originalColor == newColor) return image;

        dfsFill(image, sr, sc, originalColor, newColor, image.length, image[0].length);
        return image;
    }

    private void dfsFill(int[][] image, int row, int col,
                        int originalColor, int newColor,
                        int numRows, int numCols) {
        if (row < 0 || row >= numRows || col < 0 || col >= numCols
            || image[row][col] != originalColor) {
            return;
        }

        image[row][col] = newColor;

        dfsFill(image, row + 1, col, originalColor, newColor, numRows, numCols);
        dfsFill(image, row - 1, col, originalColor, newColor, numRows, numCols);
        dfsFill(image, row, col + 1, originalColor, newColor, numRows, numCols);
        dfsFill(image, row, col - 1, originalColor, newColor, numRows, numCols);
    }

    public static void main(String[] args) {
        int[][] image = {
            {1, 1, 1},
            {1, 1, 0},
            {1, 0, 1}
        };

        FloodFillDFS solution = new FloodFillDFS();
        int[][] result = solution.floodFill(image, 1, 1, 2);

        System.out.println("Flood fill result:");
        for (int[] row : result) {
            for (int pixel : row) {
                System.out.print(pixel + " ");
            }
            System.out.println();
        }
    }
}

/*
OUTPUT:
Flood fill result:
2 2 2
2 2 0
2 0 1
*/
```

### 3. Number of Connected Components

```java
package dsa.graph.problems;

import java.util.*;

public class ConnectedComponentsDFS {
    public int countComponents(int n, int[][] edges) {
        Map<Integer, List<Integer>> adjList = new HashMap<>();
        for (int i = 0; i < n; i++) {
            adjList.put(i, new ArrayList<>());
        }
        for (int[] edge : edges) {
            adjList.get(edge[0]).add(edge[1]);
            adjList.get(edge[1]).add(edge[0]);
        }

        boolean[] visited = new boolean[n];
        int count = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                count++;
                dfs(i, adjList, visited);
            }
        }
        return count;
    }

    private void dfs(int vertex, Map<Integer, List<Integer>> adjList, boolean[] visited) {
        visited[vertex] = true;
        for (int neighbor : adjList.get(vertex)) {
            if (!visited[neighbor]) {
                dfs(neighbor, adjList, visited);
            }
        }
    }

    public static void main(String[] args) {
        ConnectedComponentsDFS solution = new ConnectedComponentsDFS();
        int[][] edges = {{0, 1}, {1, 2}, {3, 4}};
        System.out.println("Components: " + solution.countComponents(5, edges));
        // Output: 2 (components: {0,1,2} and {3,4})
    }
}
```

### 4. Keys and Rooms (LeetCode 841)

```java
package dsa.graph.problems;

import java.util.*;

public class KeysAndRooms {
    public boolean canVisitAllRooms(List<List<Integer>> rooms) {
        int n = rooms.size();
        boolean[] visited = new boolean[n];
        int visitedCount = 0;

        dfs(0, rooms, visited);
        for (boolean v : visited) {
            if (v) visitedCount++;
        }
        return visitedCount == n;
    }

    private void dfs(int room, List<List<Integer>> rooms, boolean[] visited) {
        if (visited[room]) return;

        visited[room] = true;

        for (int key : rooms.get(room)) {
            if (!visited[key]) {
                dfs(key, rooms, visited);
            }
        }
    }

    public static void main(String[] args) {
        List<List<Integer>> rooms = new ArrayList<>();
        rooms.add(Arrays.asList(1, 3));       // Room 0 has keys to 1, 3
        rooms.add(Arrays.asList(3, 0, 1));    // Room 1 has keys to 3, 0, 1
        rooms.add(Arrays.asList(2));          // Room 2 has key to 2
        rooms.add(Arrays.asList(1));          // Room 3 has key to 1

        KeysAndRooms solution = new KeysAndRooms();
        System.out.println("Can visit all rooms: " + solution.canVisitAllRooms(rooms));
        // Output: false (can't reach room 2)
    }
}
```

---

## Tips and Tricks

### 1. When to Use DFS

```java
/*
 * ✅ USE DFS WHEN:
 *
 * 1. Finding PATHS (all paths, longest path, any path)
 *    → DFS naturally explores one path completely
 *
 * 2. Topological sorting
 *    → DFS + stack gives topological order
 *
 * 3. Cycle detection in directed graphs
 *    → GRAY/BLACK coloring works perfectly
 *
 * 4. Finding CONNECTED COMPONENTS
 *    → Simple DFS from unvisited nodes
 *
 * 5. Generating PERMUTATIONS/COMBINATIONS
 *    → DFS with backtracking
 *
 * 6. Solving MAZES
 *    → DFS explores one path, backtracks
 *
 * 7. Tree/Graph traversals (inorder, preorder, postorder)
 *    → DFS is natural for trees
 *
 * ❌ DON'T USE DFS WHEN:
 *
 * 1. Finding SHORTEST PATH (unweighted)
 *    → BFS is better (level-by-level)
 *
 * 2. Memory is limited and graph is deep
 *    → BFS may be safer (queue vs call stack)
 *
 * 3. Need SHORTEST NUMBER OF STEPS
 *    → BFS guarantees minimum steps
 *
 * 4. Graph has very deep recursion (stack overflow risk)
 *    → Use iterative DFS instead
 */
```

### 2. DFS vs Recursion

```java
// DFS IS RECURSION on graphs!
// The call stack IS the traversal stack

// RECURSIVE DFS (uses call stack)
public void dfsRecursive(int vertex, boolean[] visited) {
    visited[vertex] = true;
    System.out.print(vertex + " ");

    for (int neighbor : adjList.get(vertex)) {
        if (!visited[neighbor]) {
            dfsRecursive(neighbor, visited);  // Recursive call = push to stack
        }
    }
}  // Function return = pop from stack

// ITERATIVE DFS (explicit stack)
public void dfsIterative(int vertex, boolean[] visited) {
    Stack<Integer> stack = new Stack<>();
    stack.push(vertex);

    while (!stack.isEmpty()) {
        int v = stack.pop();
        if (!visited[v]) {
            visited[v] = true;
            System.out.print(v + " ");

            for (int neighbor : adjList.get(v)) {
                if (!visited[neighbor]) {
                    stack.push(neighbor);
                }
            }
        }
    }
}
```

### 3. Avoiding Stack Overflow

```java
// ❌ RISKY: Deep recursion can cause StackOverflowError
public void dfsRecursiveDeep(int vertex) {
    // If graph has 100,000+ nodes in a chain, this will crash!
    dfsRecursiveDeep(vertex + 1);
}

// ✅ SAFER: Iterative DFS for deep graphs
public void dfsIterativeDeep(int start) {
    Stack<Integer> stack = new Stack<>();
    Set<Integer> visited = new HashSet<>();
    stack.push(start);

    while (!stack.isEmpty()) {
        int vertex = stack.pop();
        if (!visited.contains(vertex)) {
            visited.add(vertex);
            // Process vertex
            for (int neighbor : adjList.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    stack.push(neighbor);
                }
            }
        }
    }
}

// ✅ ALTERNATIVE: Increase stack size (Java runtime flag)
// java -Xss10m MyProgram
```

### 4. Common DFS Patterns

```java
// Pattern 1: Count islands/connected components
public int countComponents(int[][] grid) {
    int count = 0;
    for (int i = 0; i < grid.length; i++) {
        for (int j = 0; j < grid[0].length; j++) {
            if (grid[i][j] == '1') {
                count++;
                dfsSink(grid, i, j);  // Sink the entire component
            }
        }
    }
    return count;
}

// Pattern 2: Find path with backtracking
public boolean findPath(int[][] maze, int[] start, int[] end) {
    Set<String> visited = new HashSet<>();
    return dfsPath(maze, start[0], start[1], end, visited);
}

private boolean dfsPath(int[][] maze, int row, int col,
                       int[] end, Set<String> visited) {
    if (row < 0 || row >= maze.length || col < 0 || col >= maze[0].length
        || maze[row][col] == 1 || visited.contains(row + "," + col)) {
        return false;
    }

    if (row == end[0] && col == end[1]) return true;

    visited.add(row + "," + col);

    // Try all 4 directions
    return dfsPath(maze, row + 1, col, end, visited)
        || dfsPath(maze, row - 1, col, end, visited)
        || dfsPath(maze, row, col + 1, end, visited)
        || dfsPath(maze, row, col - 1, end, visited);
}
```

---

## Complexity Analysis

### Time Complexity

| Operation | Time | Explanation |
|-----------|------|-------------|
| Traversal | O(V + E) | Visit each vertex once, each edge once |
| Path Finding | O(V + E) | DFS traversal + path reconstruction |
| Cycle Detection | O(V + E) | DFS traversal |
| Topological Sort | O(V + E) | DFS traversal + stack operations |
| Finding All Paths | O(V + E) worst | Exponential in worst case! |

### Space Complexity

| Scenario | Space | Explanation |
|----------|-------|-------------|
| Basic DFS | O(V) | visited array + recursion stack |
| With Parent Array | O(V) | visited + parent |
| Iterative DFS | O(V) | explicit stack |
| Path Finding | O(V) | visited + path storage |
| Finding All Paths | O(V) worst | But can be O(V!) for dense graphs |
| Recursive Depth | O(H) | H = height of graph/tree |

### DFS vs BFS Summary

| Criteria | DFS | BFS |
|----------|-----|-----|
| **Data Structure** | Stack (or recursion) | Queue |
| **Memory** | O(H) depth | O(W) width |
| **Shortest Path** | ❌ No | ✅ Yes (unweighted) |
| **All Paths** | ✅ Easy | ⚠️ Possible but harder |
| **Topological Sort** | ✅ Natural | ❌ Not possible |
| **Cycle Detection** | ✅ GRAY/BLACK | ⚠️ Needs parent tracking |
| **Level Order** | ❌ Not natural | ✅ Natural |
| **Deep Graphs** | ⚠️ Stack overflow risk | ✅ Safer |

---

## Practice Problems

### Easy
1. **Maximum Depth of Binary Tree** (LeetCode 104)
2. **Symmetric Tree** (LeetCode 101)
3. **Path Sum** (LeetCode 112)
4. **Flood Fill** (LeetCode 733)
5. **Number of Islands** (LeetCode 200)

### Medium
1. **Number of Connected Components** (LeetCode 323)
2. **Keys and Rooms** (LeetCode 841)
3. **Longest Increasing Path in Matrix** (LeetCode 329)
4. **Course Schedule** (LeetCode 207)
5. **Clone Graph** (LeetCode 133)

### Hard
1. **Word Search II** (LeetCode 212)
2. **Number of Ways to Reorder Array** (Hard)
3. **Region Cut By Slashes** (LeetCode 959)
4. **Critical Connections in Network** (LeetCode 1192)
5. **Parallel Courses** (LeetCode 1136)

---

## Summary

```
┌─────────────────────────────────────────────────────────────────┐
│                         DFS KEY POINTS                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  🏃 ALGORITHM:                                                  │
│     1. Use STACK (or recursion)                                 │
│     2. Go DEEP before wide                                       │
│     3. BACKTRACK when stuck                                      │
│                                                                  │
│  ⏱️ TIME: O(V + E)                                             │
│     - Every vertex visited once                                   │
│     - Every edge traversed once                                   │
│                                                                  │
│  💾 SPACE: O(V) average, O(H) recursive                        │
│     - visited array + stack/call stack                           │
│                                                                  │
│  ✅ BEST FOR:                                                  │
│     - Path finding (any, all, longest)                         │
│     - Topological sorting                                        │
│     - Cycle detection (directed)                                 │
│     - Tree traversals (in/pre/post order)                        │
│     - Backtracking problems                                       │
│     - Connected components                                       │
│                                                                  │
│  ❌ NOT FOR:                                                   │
│     - Shortest path (use BFS)                                    │
│     - Minimum steps (use BFS)                                    │
│     - Level-order (use BFS)                                      │
│                                                                  │
│  ⚠️ WATCH OUT FOR:                                            │
│     - Stack overflow with deep recursion                         │
│     - Exponential time for finding all paths                     │
│     - Stack overflow with deep graphs                            │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

> **Remember**: DFS explores deep before wide, making it perfect for path finding, topological sorting, and backtracking problems. BFS explores level by level, perfect for shortest path. Choose wisely based on your problem!
