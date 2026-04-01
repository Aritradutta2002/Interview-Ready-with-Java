# Graph Coloring & 2-SAT - Complete Guide

## Table of Contents
1. [Graph Coloring](#graph-coloring)
2. [Bipartite Graph Check](#bipartite-graph-check)
3. [Maximum Bipartite Matching](#maximum-bipartite-matching)
4. [2-SAT Problem](#2-sat-problem)
5. [Practice Problems](#practice-problems)

---

## Maximum Bipartite Matching

**Maximum Matching** finds the largest set of edges where no two edges share a vertex (in a bipartite graph).

### Applications
- Job assignment (workers → jobs)
- Dating app (users → matches)
- Server allocation (tasks → servers)
- Hall's theorem verification

### Solution via Max Flow

Convert bipartite matching to max-flow problem:
1. Create source connected to SET-A (weight 1)
2. Connect SET-B to sink (weight 1)
3. Connect matched pairs (weight 1)
4. Run max-flow: flow value = maximum matching size

```java
// Simplified: Use Max Flow algorithm with capacity 1 everywhere
// See [13_max_flow_min_cut.md](13_max_flow_min_cut.md) for implementation
// Time: O(V·E²) with Ford-Fulkerson or O(E log V) with Dinic's
```

### Complexity

| Algorithm | Time | Notes |
|-----------|------|-------|
| Ford-Fulkerson | O(V·E²) | Simpler, slower |
| Dinic's Algorithm | O(V²·E) | Optimized |
| Hopcroft-Karp | O(E√V) | Fastest, complex |

---

## Graph Coloring

### What is Graph Coloring?

```
┌─────────────────────────────────────────────────────────────────┐
│                      GRAPH COLORING                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  DEFINITION:                                                     │
│  Assign colors to vertices such that no adjacent vertices         │
│  share the same color                                            │
│                                                                  │
│  CHROMATIC NUMBER:                                               │
│  Minimum number of colors needed to properly color a graph       │
│                                                                  │
│  APPLICATIONS:                                                   │
│  - Map coloring (no adjacent regions same color)                 │
│  - Register allocation in compilers                               │
│  - Scheduling problems                                           │
│  - Frequency assignment in wireless networks                      │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Greedy Coloring Algorithm

```java
package dsa.graph.coloring;

import java.util.*;

public class GreedyColoring {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;

    public GreedyColoring(int numVertices) {
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

    public int[] greedyColor() {
        int[] colors = new int[numVertices];
        Arrays.fill(colors, -1);  // -1 means uncolored
        boolean[] available;

        for (int v = 0; v < numVertices; v++) {
            available = new boolean[numVertices];
            Arrays.fill(available, true);

            // Mark colors of adjacent vertices as unavailable
            for (int neighbor : adjList.get(v)) {
                if (colors[neighbor] != -1) {
                    available[colors[neighbor]] = false;
                }
            }

            // Find first available color
            int color = 0;
            while (color < numVertices && !available[color]) {
                color++;
            }
            colors[v] = color;
        }
        return colors;
    }

    public static void main(String[] args) {
        GreedyColoring graph = new GreedyColoring(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        int[] colors = graph.greedyColor();
        System.out.println("Vertex colors: ");
        for (int i = 0; i < colors.length; i++) {
            System.out.println("  Vertex " + i + " -> Color " + colors[i]);
        }
    }
}
```

### Time Complexity: O(V²)
### Space Complexity: O(V)

---

## Bipartite Graph Check

### What is Bipartite?

```
┌─────────────────────────────────────────────────────────────────┐
│                      BIPARTITE GRAPH                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  DEFINITION:                                                     │
│  A graph whose vertices can be divided into two DISJOINT sets     │
│  such that every edge connects a vertex in one set to a vertex   │
│  in the other set                                                │
│                                                                  │
│  EQUIVALENT TO:                                                  │
│  - Graph with chromatic number = 2                               │
│  - Graph with NO ODD LENGTH CYCLES                               │
│                                                                  │
│  TEST:                                                           │
│  BFS/DFS with alternating colors                                 │
│  If conflict found -> not bipartite                              │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Java Implementation

```java
package dsa.graph.coloring;

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
        for (int i = 0; i < numVertices; i++) {
            if (color[i] == -1) {
                if (!bfsCheck(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean bfsCheck(int start) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        color[start] = 0;

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
        return true;
    }

    public List<Integer> getSetA() {
        List<Integer> setA = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            if (color[i] == 0) setA.add(i);
        }
        return setA;
    }

    public List<Integer> getSetB() {
        List<Integer> setB = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            if (color[i] == 1) setB.add(i);
        }
        return setB;
    }

    public static void main(String[] args) {
        // Bipartite graph (even cycle)
        BipartiteCheck graph1 = new BipartiteCheck(4);
        graph1.addEdge(0, 1);
        graph1.addEdge(1, 2);
        graph1.addEdge(2, 3);
        graph1.addEdge(3, 0);
        System.out.println("Square (bipartite): " + graph1.isBipartite());
        System.out.println("Set A: " + graph1.getSetA() + ", Set B: " + graph1.getSetB());

        // Not bipartite (odd cycle)
        BipartiteCheck graph2 = new BipartiteCheck(3);
        graph2.addEdge(0, 1);
        graph2.addEdge(1, 2);
        graph2.addEdge(2, 0);  // Triangle - odd cycle
        System.out.println("Triangle (not bipartite): " + graph2.isBipartite());
    }
}

/*
OUTPUT:
Square (bipartite): true
Set A: [0, 2], Set B: [1, 3]
Triangle (not bipartite): false
*/
```

---

## 2-SAT Problem

### What is 2-SAT?

```
┌─────────────────────────────────────────────────────────────────┐
│                         2-SAT                                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  SAT (Boolean Satisfiability):                                   │
│  - Given a boolean formula, is there an assignment that        │
│    makes it true?                                                │
│  - NP-Complete in general                                       │
│                                                                  │
│  2-SAT (2-Satisfiability):                                      │
│  - Each clause has exactly 2 literals                           │
│  - Example: (A ∨ B) ∧ (¬A ∨ C) ∧ (¬C ∨ ¬B)                   │
│  - SOLVABLE IN POLYNOMIAL TIME!                                │
│                                                                  │
│  SOLUTION:                                                      │
│  - Implication Graph                                            │
│  - SCCs must not contain both X and ¬X                          │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Java Implementation

```java
package dsa.graph.twosat;

import java.util.*;

public class TwoSAT {
    private int numVariables;
    private Map<Integer, List<Integer>> adjList;
    private Map<Integer, List<Integer>> reverseAdj;
    private int time;
    private int[] disc;
    private int[] low;
    private boolean[] onStack;
    private Stack<Integer> stack;
    private List<List<Integer>> sccs;

    public TwoSAT(int numVariables) {
        this.numVariables = numVariables;
        this.adjList = new HashMap<>();
        this.reverseAdj = new HashMap<>();
        for (int i = 0; i < 2 * numVariables; i++) {
            adjList.put(i, new ArrayList<>());
            reverseAdj.put(i, new ArrayList<>());
        }
    }

    private int var(int x) { return Math.abs(x) - 1; }
    private int lit(int x) { return x > 0 ? 2 * var(x) : 2 * var(x) + 1; }
    private int neg(int l) { return l ^ 1; }

    // (A OR B) => (¬A -> B) AND (¬B -> A)
    public void addClause(int a, int b) {  // a OR b
        adjList.get(neg(lit(a))).add(lit(b));   // ¬a -> b
        adjList.get(neg(lit(b))).add(lit(a));   // ¬b -> a
        reverseAdj.get(lit(b)).add(neg(lit(a)));
        reverseAdj.get(lit(a)).add(neg(lit(b)));
    }

    public boolean isSatisfiable() {
        // Kosaraju's SCC
        disc = new int[2 * numVariables];
        low = new int[2 * numVariables];
        onStack = new boolean[2 * numVariables];
        stack = new Stack<>();
        time = 0;
        sccs = new ArrayList<>();

        // First DFS
        for (int i = 0; i < 2 * numVariables; i++) {
            if (disc[i] == 0) {
                dfs1(i);
            }
        }

        // Second DFS on reverse graph
        Arrays.fill(onStack, false);
        for (int i = 0; i < 2 * numVariables; i++) {
            disc[i] = 0;
        }
        time = 0;
        for (int i = 0; i < 2 * numVariables; i++) {
            if (disc[i] == 0) {
                List<Integer> component = new ArrayList<>();
                dfs2(i, component);
                sccs.add(component);
            }
        }

        // Check: no variable and its negation in same SCC
        for (int i = 0; i < numVariables; i++) {
            if (sameSCC(2 * i, 2 * i + 1)) {
                return false;
            }
        }
        return true;
    }

    private void dfs1(int v) {
        disc[v] = low[v] = ++time;
        stack.push(v);
        onStack[v] = true;

        for (int u : adjList.get(v)) {
            if (disc[u] == 0) {
                dfs1(u);
                low[v] = Math.min(low[v], low[u]);
            } else if (onStack[u]) {
                low[v] = Math.min(low[v], disc[u]);
            }
        }

        if (low[v] == disc[v]) {
            while (true) {
                int u = stack.pop();
                onStack[u] = false;
                if (u == v) break;
            }
        }
    }

    private void dfs2(int v, List<Integer> component) {
        disc[v] = low[v] = ++time;
        component.add(v);
        onStack[v] = true;

        for (int u : reverseAdj.get(v)) {
            if (disc[u] == 0) {
                dfs2(u, component);
            }
        }
    }

    private boolean sameSCC(int a, int b) {
        for (List<Integer> scc : sccs) {
            if (scc.contains(a) && scc.contains(b)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // (A ∨ B) ∧ (¬A ∨ C) ∧ (¬C ∨ ¬B)
        TwoSAT sat = new TwoSAT(3);
        sat.addClause(1, 2);   // A ∨ B
        sat.addClause(-1, 3);  // ¬A ∨ C
        sat.addClause(-3, -2); // ¬C ∨ ¬B

        System.out.println("Satisfiable: " + sat.isSatisfiable());
    }
}
```

### Time Complexity: O(V + E)
### Space Complexity: O(V + E)

---

## Practice Problems

### Graph Coloring
1. **Is Graph Bipartite?** (LeetCode 785)
2. **Possible Bipartition** (LeetCode 886)

### 2-SAT
1. **2-SAT Problem** (CSES)
2. **Horn SAT** (variation)

---

## Summary

```
┌─────────────────────────────────────────────────────────────────┐
│                  GRAPH COLORING & 2-SAT                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  GRAPH COLORING:                                                │
│  - Assign colors, no adjacent same                               │
│  - Bipartite = chromatic number 2                               │
│  - Can check with BFS/DFS alternating colors                    │
│                                                                  │
│  BIPARTITE CHECK:                                               │
│  - 2-colorable = no odd cycles                                │
│  - BFS/DFS with color alternation                              │
│                                                                  │
│  2-SAT:                                                         │
│  - Each clause has exactly 2 literals                            │
│  - Convert to implication graph                                  │
│  - Check: X and ¬X not in same SCC                             │
│  - O(V + E) using Kosaraju's algorithm                          │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

> **Remember**: Bipartite graphs are 2-colorable! 2-SAT is polynomial-time solvable using SCC. These are powerful techniques for constraint satisfaction problems!
