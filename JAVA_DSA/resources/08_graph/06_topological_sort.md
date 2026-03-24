# Topological Sort - Complete Guide

## Table of Contents
1. [What is Topological Sort?](#what-is-topological-sort)
2. [Why Topological Sort?](#why-topological-sort)
3. [Kahn's Algorithm (BFS)](#kahns-algorithm-bfs)
4. [DFS-based Topological Sort](#dfs-based-topological-sort)
5. [Cycle Detection](#cycle-detection)
6. [Applications](#applications)
7. [Practice Problems](#practice-problems)

---

## What is Topological Sort?

**Topological Sort** arranges vertices of a **DAG (Directed Acyclic Graph)** in a linear order such that for every directed edge **u → v**, **u comes before v** in the ordering.

### 🎯 CRITICAL: Edge Direction Convention

```
CONVENTION:        Edge u → v means:  "u MUST COMPLETE BEFORE v"
                                 OR:  "v DEPENDS ON u"
                                 OR:  "u is prerequisite for v"

COURSE EXAMPLE:    Edge 0 → 1  =  "Take course 0, then course 1"
                   Result: [0, 1, ...] ← 0 comes before 1

BUILD EXAMPLE:     Edge main.o → app  =  "Compile main.o before app"
                   Result: [main.o, app, ...] ← main.o comes first

⚠️ REVERSING THIS DIRECTION PRODUCES WRONG ORDERING!
```

### Key Characteristics

```
┌─────────────────────────────────────────────────────────────────┐
│                  TOPOLOGICAL SORT                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ✅ ONLY WORKS: On DAGs (Directed Acyclic Graphs)              │
│  ✅ FINDS: Linear ordering of vertices                         │
│  ✅ USES: Dependency resolution, task scheduling               │
│  ✅ TIME:  O(V + E)                                            │
│  ✅ SPACE: O(V)                                                │
│                                                                  │
│  ❌ NOT POSSIBLE: If graph has a cycle                       │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Visual Example

```
DAG:
    [0]────→[1]
     │      ↕
     ↓      │
    [2]────→[3]

Valid Topological Orders:
  0, 2, 1, 3
  0, 1, 2, 3
  2, 0, 1, 3
  2, 0, 3, 1
  (many possible, all valid)

Invalid Orders:
  1, 0, 2, 3  (1→3 but 1 comes before 0, edge 0→1 violated? No wait)
  3, 2, 1, 0  (3 depends on 1 and 2, but comes first!)

Actually valid: Only orders where if there's edge u→v, u comes before v
```

### Real-World Analogy

```
BUILDING A HOUSE (Topological Order):

Step 1: Foundation must come before Walls
Step 2: Walls must come before Roof
Step 3: Roof must come before Paint
Step 4: Paint must come before Decorations

Valid Order: Foundation → Walls → Roof → Paint → Decorations

If there's a cycle (Paint needs Decor lamp, Decor lamp needs special Paint),
TOPOLOGICAL SORT IS IMPOSSIBLE!
```

---

## Why Topological Sort?

### Applications

```java
/*
 * 1. BUILD SYSTEMS (Maven, Gradle)
 *    - Dependencies must be installed before dependent package
 *    - A → B means A must be installed before B
 *
 * 2. COURSE SCHEDULING
 *    - Prerequisites must be taken before advanced courses
 *    - CS101 → CS201 means CS101 must be taken before CS201
 *
 * 3. TASK SCHEDULING
 *    - Build systems, CI/CD pipelines
 *    - Task dependencies
 *
 * 4. PACKAGE MANAGERS (npm, pip, apt)
 *    - Dependencies resolution
 *
 * 5. DEADLOCK DETECTION
 *    - If topological sort fails, there's a cycle = deadlock!
 */
```

---

## Kahn's Algorithm (BFS)

### The Algorithm

```java
/*
 * KAHN'S ALGORITHM (BFS-based):
 *
 * 1. Calculate indegree (number of incoming edges) for each vertex
 * 2. Add all vertices with indegree 0 to queue
 * 3. While queue is not empty:
 *    a. Dequeue vertex, add to result
 *    b. For each neighbor:
 *       - Decrease its indegree by 1
 *       - If indegree becomes 0, add to queue
 * 4. If result contains all vertices → valid topological order
 * 5. If result doesn't contain all vertices → cycle exists!
 */
```

### Java Implementation

```java
package dsa.graph.topological;

import java.util.*;

public class TopologicalSortKahn {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;
    private int[] indegree;

    public TopologicalSortKahn(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        this.indegree = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
        indegree[destination]++;
    }

    public List<Integer> topologicalSort() {
        Queue<Integer> queue = new LinkedList<>();
        List<Integer> result = new ArrayList<>();

        // Add all vertices with indegree 0
        for (int i = 0; i < numVertices; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            result.add(vertex);

            // Reduce indegree of neighbors
            for (int neighbor : adjList.get(vertex)) {
                indegree[neighbor]--;
                if (indegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Check if cycle exists
        if (result.size() != numVertices) {
            return Collections.emptyList();  // Cycle detected!
        }

        return result;
    }

    public static void main(String[] args) {
        // Edge u → v means: u MUST COMPLETE BEFORE v
        // 
        // Course Schedule:
        // - Course 0 → Course 1: Take 0 before 1
        // - Course 0 → Course 2: Take 0 before 2
        // - Course 1 → Course 3: Take 1 before 3
        // - Course 2 → Course 3: Take 2 before 3
        // Result should be: [0, 1, 2, 3] or [0, 2, 1, 3]
        
        TopologicalSortKahn courses = new TopologicalSortKahn(4);
        courses.addEdge(0, 1);  // 0 → 1: Course 0 prerequisite for Course 1
        courses.addEdge(0, 2);  // 0 → 2: Course 0 prerequisite for Course 2
        courses.addEdge(1, 3);  // 1 → 3: Course 1 prerequisite for Course 3
        courses.addEdge(2, 3);  // 2 → 3: Course 2 prerequisite for Course 3

        List<Integer> order = courses.topologicalSort();
        System.out.println("Course order: " + order);
        // Possible output: [0, 1, 2, 3] or [0, 2, 1, 3]

        // Cycle Example
        TopologicalSortKahn cycle = new TopologicalSortKahn(3);
        cycle.addEdge(0, 1);
        cycle.addEdge(1, 2);
        cycle.addEdge(2, 0);  // Creates cycle!

        List<Integer> cycleOrder = cycle.topologicalSort();
        System.out.println("With cycle: " + cycleOrder);
        // Output: [] (empty - cycle detected!)
    }
}
```

### Step-by-Step Dry Run

```java
/*
GRAPH:
    0 → 2 → 3
    ↓   ↑
    1 → 4

Edges: (0,2), (1,4), (2,3), (2,4), (1,2)

INDEGREE:
  indegree[0] = 0
  indegree[1] = 0
  indegree[2] = 2 (from 0, 1)
  indegree[3] = 1 (from 2)
  indegree[4] = 2 (from 1, 2)

STEP 1: Queue = [0, 1] (indegree 0 vertices)
  Result: []

STEP 2: Dequeue 0, add to result
  Reduce indegree of 2: indegree[2] = 1
  Queue = [1]
  Result: [0]

STEP 3: Dequeue 1, add to result
  Reduce indegree of 2: indegree[2] = 0, add to queue
  Reduce indegree of 4: indegree[4] = 1
  Queue = [2]
  Result: [0, 1]

STEP 4: Dequeue 2, add to result
  Reduce indegree of 3: indegree[3] = 0, add to queue
  Reduce indegree of 4: indegree[4] = 0, add to queue
  Queue = [3, 4]
  Result: [0, 1, 2]

STEP 5: Dequeue 3, add to result
  No neighbors
  Queue = [4]
  Result: [0, 1, 2, 3]

STEP 6: Dequeue 4, add to result
  No neighbors
  Queue = []
  Result: [0, 1, 2, 3, 4]

RESULT: Valid topological order: [0, 1, 2, 3, 4]
*/
```

---

## DFS-based Topological Sort

### The Algorithm

```java
/*
 * DFS-BASED TOPOLOGICAL SORT:
 *
 * 1. Perform DFS from each unvisited vertex
 * 2. After exploring all neighbors, add vertex to stack
 * 3. Pop from stack to get topological order
 *
 * Key insight: In DFS, when we finish exploring a vertex,
 * all its dependents (vertices that depend on it) are already in stack
 */
```

### Java Implementation

```java
package dsa.graph.topological;

import java.util.*;

public class TopologicalSortDFS {
    private Map<Integer, List<Integer>> adjList;
    private int numVertices;
    private String[] color;  // WHITE, GRAY, BLACK
    private Stack<Integer> stack;

    public TopologicalSortDFS(int numVertices) {
        this.numVertices = numVertices;
        this.adjList = new HashMap<>();
        this.color = new String[numVertices];
        this.stack = new Stack<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.put(i, new ArrayList<>());
            color[i] = "WHITE";
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
    }

    public List<Integer> topologicalSort() {
        stack.clear();
        Arrays.fill(color, "WHITE");

        for (int i = 0; i < numVertices; i++) {
            if (color[i].equals("WHITE")) {
                if (!dfs(i)) {
                    return Collections.emptyList();  // Cycle detected!
                }
            }
        }

        // Pop from stack to get order
        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }

    private boolean dfs(int vertex) {
        color[vertex] = "GRAY";  // Currently processing

        for (int neighbor : adjList.get(vertex)) {
            if (color[neighbor].equals("GRAY")) {
                return false;  // Back edge = cycle!
            }
            if (color[neighbor].equals("WHITE")) {
                if (!dfs(neighbor)) {
                    return false;
                }
            }
        }

        color[vertex] = "BLACK";  // Done processing
        stack.push(vertex);  // Add to stack after processing neighbors
        return true;
    }

    public static void main(String[] args) {
        TopologicalSortDFS courses = new TopologicalSortDFS(4);
        courses.addEdge(1, 0);
        courses.addEdge(2, 0);
        courses.addEdge(3, 1);
        courses.addEdge(3, 2);

        List<Integer> order = courses.topologicalSort();
        System.out.println("DFS Topological Order: " + order);
    }
}
```

### Step-by-Step Dry Run

```java
/*
GRAPH:
    0 → 2 → 3
    ↓   ↑
    1 ──┘

DFS from 0:
  Visit 0 (GRAY)
  Visit 2 (GRAY)
  Visit 3 (GRAY) - no unvisited neighbors
  Stack push: 3
  Back to 2 - no unvisited neighbors
  Stack push: 2
  Back to 0
  Stack push: 0

DFS from 1:
  Visit 1 (GRAY)
  Already visited 2 (BLACK), skip
  Stack push: 1

Final stack: [3, 2, 0, 1]
Pop order: [1, 0, 2, 3]

Valid topological order: 1, 0, 2, 3
(1→0, 1→2, 0→2, all edges satisfied!)
*/
```

---

## Cycle Detection

### How Topological Sort Detects Cycles

```java
/*
 * CYCLE DETECTION IN TOPOLOGICAL SORT:
 *
 * Kahn's Algorithm:
 * - If result size < number of vertices, cycle exists
 * - Because cycle creates a deadlock where no vertex has indegree 0
 *
 * DFS-based:
 * - If we encounter a GRAY vertex during DFS, cycle exists
 * - GRAY = currently in recursion stack
 * - Finding it again = back edge = cycle!
 */

public boolean hasCycle() {
    // Reset
    Arrays.fill(color, "WHITE");

    for (int i = 0; i < numVertices; i++) {
        if (color[i].equals("WHITE")) {
            if (dfsDetect(i)) {
                return true;  // Cycle found!
            }
        }
    }
    return false;
}

private boolean dfsDetect(int vertex) {
    color[vertex] = "GRAY";

    for (int neighbor : adjList.get(vertex)) {
        if (color[neighbor].equals("GRAY")) {
            return true;  // Back edge = cycle!
        }
        if (color[neighbor].equals("WHITE")) {
            if (dfsDetect(neighbor)) {
                return true;
            }
        }
    }

    color[vertex] = "BLACK";
    return false;
}
```

---

## Applications

### 1. Course Schedule

```java
package dsa.graph.applications;

import java.util.*;

public class CourseSchedule {
    public boolean canFinishAllCourses(int numCourses, int[][] prerequisites) {
        Map<Integer, List<Integer>> adjList = new HashMap<>();
        int[] indegree = new int[numCourses];

        for (int i = 0; i < numCourses; i++) {
            adjList.put(i, new ArrayList<>());
        }

        for (int[] prereq : prerequisites) {
            adjList.get(prereq[1]).add(prereq[0]);
            indegree[prereq[0]]++;
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }

        int completed = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            completed++;

            for (int next : adjList.get(course)) {
                indegree[next]--;
                if (indegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }

        return completed == numCourses;
    }

    public static void main(String[] args) {
        CourseSchedule solution = new CourseSchedule();

        int[][] prerequisites1 = {{1, 0}, {2, 0}, {3, 1}, {3, 2}};
        System.out.println("Can finish: " + solution.canFinishAllCourses(4, prerequisites1));
        // Output: true

        int[][] prerequisites2 = {{1, 0}, {0, 1}};  // Circular!
        System.out.println("Can finish: " + solution.canFinishAllCourses(2, prerequisites2));
        // Output: false
    }
}
```

### 2. Build Order

```java
package dsa.graph.applications;

import java.util.*;

public class BuildOrder {
    public String[] findBuildOrder(String[] projects, String[][] dependencies) {
        Map<String, List<String>> adjList = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();

        for (String project : projects) {
            adjList.put(project, new ArrayList<>());
            indegree.put(project, 0);
        }

        for (String[] dep : dependencies) {
            String prerequisite = dep[0];
            String dependent = dep[1];
            adjList.get(prerequisite).add(dependent);
            indegree.put(dependent, indegree.get(dependent) + 1);
        }

        Queue<String> queue = new LinkedList<>();
        for (String project : projects) {
            if (indegree.get(project) == 0) {
                queue.offer(project);
            }
        }

        List<String> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            String project = queue.poll();
            order.add(project);

            for (String dependent : adjList.get(project)) {
                indegree.put(dependent, indegree.get(dependent) - 1);
                if (indegree.get(dependent) == 0) {
                    queue.offer(dependent);
                }
            }
        }

        return order.toArray(new String[0]);
    }
}
```

---

## Practice Problems

### Easy
1. **Course Schedule** (LeetCode 207)
2. **Course Schedule II** (LeetCode 210)
3. **Find Eventual Safe States** (LeetCode 802)

### Medium
1. **Parallel Courses** (LeetCode 1136)
2. **Minimum Height Trees** (LeetCode 310)
3. **Alien Dictionary** (LeetCode 269)

### Hard
1. **Sequence Reconstruction** (LeetCode 444)
2. **Build a Matrix With Conditions** (LeetCode 2392)

---

## Summary

```
┌─────────────────────────────────────────────────────────────────┐
│                   TOPOLOGICAL SORT                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  🎯 PURPOSE: Linear ordering of DAG vertices                   │
│                                                                  │
│  ⏱️ TIME: O(V + E)                                            │
│  💾 SPACE: O(V)                                                │
│                                                                  │
│  ✅ REQUIREMENT: Graph must be a DAG (no cycles)                │
│                                                                  │
│  📝 METHODS:                                                    │
│     1. Kahn's Algorithm (BFS + indegree)                       │
│     2. DFS-based (stack after DFS)                             │
│                                                                  │
│  📝 STEPS (Kahn's):                                            │
│     1. Calculate indegree for all vertices                     │
│     2. Add all indegree-0 vertices to queue                    │
│     3. Process queue, reduce neighbor indegrees                 │
│     4. If all vertices processed → valid order                  │
│     5. If not → cycle exists!                                   │
│                                                                  │
│  🔍 CYCLE DETECTION:                                            │
│     - Kahn's: result.size() < V                               │
│     - DFS: encounter GRAY vertex (back edge)                   │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

> **Remember**: Topological sort only works on DAGs! Use Kahn's algorithm (BFS) or DFS-based approach. If topological sort fails, your graph has a cycle - great for deadlock detection!
