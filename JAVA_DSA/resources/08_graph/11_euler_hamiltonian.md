# Eulerian & Hamiltonian Paths - Complete Guide

## Table of Contents
1. [Eulerian Paths and Circuits](#eulerian-paths-and-circuits)
2. [Fleury's Algorithm](#fleury's-algorithm)
3. [Hamiltonian Paths and Cycles](#hamiltonian-paths-and-cycles)
4. [NP-Completeness](#np-completeness)
5. [Practice Problems](#practice-problems)

---

## Eulerian Paths and Circuits

### What is Eulerian?

```
┌─────────────────────────────────────────────────────────────────┐
│                    EULERIAN PATH/CIRCUIT                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  EULERIAN CIRCUIT:                                              │
│  - Visits EVERY EDGE exactly once                              │
│  - Returns to starting vertex                                  │
│                                                                  │
│  EULERIAN PATH:                                                │
│  - Visits EVERY EDGE exactly once                              │
│  - Does NOT need to return to start                           │
│                                                                  │
│  For undirected graph:                                         │
│  - Eulerian Circuit: All vertices have even degree, connected │
│  - Eulerian Path: Exactly 0 or 2 vertices have odd degree      │
│                                                                  │
│  For directed graph:                                           │
│  - Eulerian Circuit: In-degree == Out-degree for all vertices │
│  - Eulerian Path: 0 or 1 vertex has out-degree - in-degree = 1│
│                   and 0 or 1 vertex has in-degree - out-degree=1│
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Java Implementation

```java
package dsa.graph.special;

import java.util.*;

public class EulerianPath {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;
    private int[] inDegree;
    private int[] outDegree;

    public EulerianPath(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        this.inDegree = new int[numVertices];
        this.outDegree = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
        outDegree[source]++;
        inDegree[destination]++;
    }

    public boolean hasEulerianPath() {
        int startVertices = 0;
        int endVertices = 0;

        for (int i = 0; i < numVertices; i++) {
            if (Math.abs(inDegree[i] - outDegree[i]) > 1) {
                return false;
            }
            if (outDegree[i] - inDegree[i] == 1) {
                startVertices++;
            }
            if (inDegree[i] - outDegree[i] == 1) {
                endVertices++;
            }
        }

        return (startVertices == 0 && endVertices == 0) ||
               (startVertices == 1 && endVertices == 1);
    }

    public List<Integer> findEulerianPath() {
        if (!hasEulerianPath()) {
            return Collections.emptyList();
        }

        Map<Integer, List<Integer>> tempAdj = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            tempAdj.put(i, new ArrayList<>(adjList.get(i)));
        }

        Stack<Integer> stack = new Stack<>();
        List<Integer> path = new ArrayList<>();

        int startVertex = findStartVertex();
        stack.push(startVertex);

        while (!stack.isEmpty()) {
            int v = stack.peek();
            if (!tempAdj.get(v).isEmpty()) {
                int next = tempAdj.get(v).remove(tempAdj.get(v).size() - 1);
                stack.push(next);
            } else {
                path.add(stack.pop());
            }
        }

        Collections.reverse(path);
        return path;
    }

    private int findStartVertex() {
        for (int i = 0; i < numVertices; i++) {
            if (outDegree[i] - inDegree[i] == 1) {
                return i;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        EulerianPath graph = new EulerianPath(5);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 0);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 2);

        System.out.println("Has Eulerian Path: " + graph.hasEulerianPath());
        System.out.println("Path: " + graph.findEulerianPath());
    }
}
```

---

## Hamiltonian Paths and Cycles

### What is Hamiltonian?

```
┌─────────────────────────────────────────────────────────────────┐
│                  HAMILTONIAN PATH/CYCLE                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  HAMILTONIAN CYCLE:                                             │
│  - Visits EVERY VERTEX exactly once                            │
│  - Returns to starting vertex                                  │
│                                                                  │
│  HAMILTONIAN PATH:                                             │
│  - Visits EVERY VERTEX exactly once                            │
│  - Does NOT need to return to start                           │
│                                                                  │
│  ⚠️ NP-Complete problem!                                       │
│  No known polynomial-time algorithm exists                      │
│  Must use backtracking or heuristics                           │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Backtracking Solution

```java
package dsa.graph.special;

import java.util.*;

public class HamiltonianPath {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;
    private List<Integer> hamiltonianCycle;
    private List<Integer> hamiltonianPath;

    public HamiltonianPath(int numVertices) {
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

    public List<Integer> findHC() {
        hamiltonianCycle = null;
        List<Integer> current = new ArrayList<>();
        boolean[] visited = new boolean[numVertices];
        visited[0] = true;
        current.add(0);
        hamiltonianBacktrack(current, visited, 0);
        return hamiltonianCycle;
    }

    private void hamiltonianBacktrack(List<Integer> current, boolean[] visited, int last) {
        if (current.size() == numVertices) {
            if (adjList.get(last).contains(0)) {
                current.add(0);
                hamiltonianCycle = new ArrayList<>(current);
                current.remove(current.size() - 1);
            }
            return;
        }

        for (int neighbor : adjList.get(last)) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                current.add(neighbor);
                hamiltonianBacktrack(current, visited, neighbor);
                if (hamiltonianCycle != null) return;
                current.remove(current.size() - 1);
                visited[neighbor] = false;
            }
        }
    }

    public List<Integer> findHP() {
        hamiltonianPath = null;
        for (int start = 0; start < numVertices; start++) {
            List<Integer> current = new ArrayList<>();
            boolean[] visited = new boolean[numVertices];
            visited[start] = true;
            current.add(start);
            hamiltonianPathBacktrack(current, visited, start);
            if (hamiltonianPath != null) return hamiltonianPath;
        }
        return Collections.emptyList();
    }

    private void hamiltonianPathBacktrack(List<Integer> current, boolean[] visited, int last) {
        if (current.size() == numVertices) {
            hamiltonianPath = new ArrayList<>(current);
            return;
        }

        for (int neighbor : adjList.get(last)) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                current.add(neighbor);
                hamiltonianPathBacktrack(current, visited, neighbor);
                if (hamiltonianPath != null) return;
                current.remove(current.size() - 1);
                visited[neighbor] = false;
            }
        }
    }

    public static void main(String[] args) {
        HamiltonianPath graph = new HamiltonianPath(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 3);
        graph.addEdge(0, 4);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        System.out.println("Hamiltonian Cycle: " + graph.findHC());
        System.out.println("Hamiltonian Path: " + graph.findHP());
    }
}
```

---

## NP-Completeness

### Why Hamiltonian is Hard

```
┌─────────────────────────────────────────────────────────────────┐
│              HAMILTONIAN vs EULERIAN                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  EULERIAN:                                                      │
│  ✅ Polynomial time O(V + E)                                   │
│  ✅ Simple degree-based conditions                              │
│  ✅ Efficient algorithm exists                                   │
│                                                                  │
│  HAMILTONIAN:                                                   │
│  ❌ NP-Complete                                                │
│  ❌ No known polynomial algorithm                               │
│  ❌ Must try exponential possibilities                          │
│                                                                  │
│  Traveling Salesman Problem (TSP) is Hamiltonian Cycle!         │
│  With weights, becomes optimization problem                      │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Practice Problems

### Eulerian
1. **Reconstruct Itinerary** (LeetCode 332)
2. **Valid Arrangement of Pairs** (LeetCode 1259)

### Hamiltonian
1. **Find Path That Visits All Cities** (LeetCode 1675)
2. **Find the Smallest Value of the Rayleigh Quotient**

---

## Summary

```
┌─────────────────────────────────────────────────────────────────┐
│          EULERIAN vs HAMILTONIAN                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  EULERIAN (Easy):                                               │
│  ✅ Visit every EDGE once                                       │
│  ✅ O(V + E) time                                               │
│  ✅ Degree-based conditions                                     │
│                                                                  │
│  HAMILTONIAN (Hard - NP-Complete):                              │
│  ❌ Visit every VERTEX once                                     │
│  ❌ Exponential time required                                    │
│  ❌ Backtracking or heuristics needed                           │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

> **Remember**: Eulerian is about edges - easy with degree checks! Hamiltonian is about vertices - hard, requires backtracking. This is a classic example of how similar-sounding problems can have vastly different complexities!
