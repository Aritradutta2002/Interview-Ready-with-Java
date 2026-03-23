# Graph Problem Patterns - Categorized Master List

## Table of Contents
1. [Traversal Problems](#1-traversal-problems)
2. [Shortest Path Problems](#2-shortest-path-problems)
3. [Connectivity & Components](#3-connectivity--components)
4. [Cycle Detection](#4-cycle-detection)
5. [Topological Sort Problems](#5-topological-sort-problems)
6. [Union-Find Problems](#6-union-find-problems)
7. [Tree-Specific Problems](#7-tree-specific-problems)
8. [Grid/Matrix Problems](#8-gridmatrix-problems)
9. [Weighted Graph Problems](#9-weighted-graph-problems)
10. [Advanced Algorithms](#10-advanced-algorithms)
11. [Problem-Solving Templates](#11-problem-solving-templates)

---

## 1. Traversal Problems

### BFS Patterns
```
PATTERN: Level-order traversal, shortest steps, minimum time
TEMPLATE: Queue + size tracking per level
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Number of Islands (200) | Easy | BFS/DFS flood fill |
| 2 | Max Depth of Binary Tree (104) | Easy | BFS or DFS |
| 3 | Minimum Knight Moves (1197) | Medium | BFS with visited |
| 4 | Rotting Oranges (994) | Medium | Multi-source BFS |
| 5 | Nearest Exit from Maze (1926) | Medium | BFS from entrance |
| 6 | Bus Routes (815) | Hard | BFS on routes |

### DFS Patterns
```
PATTERN: Path finding, exploring all options, backtracking
TEMPLATE: Recursion + visited + process
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Find Path Exists | Easy | DFS/BFS connectivity |
| 2 | Keys and Rooms (841) | Medium | DFS from starting room |
| 3 | Longest Increasing Path (329) | Hard | DFS + memoization |
| 4 | Word Search (79) | Medium | DFS + backtracking |
| 5 | Number of Enclaves (1020) | Medium | DFS boundary check |

---

## 2. Shortest Path Problems

### Unweighted (BFS)
```
PATTERN: Minimum steps, minimum edges, level-order
TEMPLATE: BFS from source
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Shortest Path in Binary Matrix (1293) | Medium | BFS with dist |
| 2 | Minimum Operations to Convert Number | Medium | BFS on states |
| 3 | Shortest Path with Alternating Colors (1129) | Medium | BFS with parity |

### Weighted Positive (Dijkstra)
```
PATTERN: Minimum distance, minimum cost with positive weights
TEMPLATE: PriorityQueue + dist array
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Network Delay Time (743) | Medium | Dijkstra from source |
| 2 | Find City with Smallest Neighbor (1334) | Medium | Dijkstra from all |
| 3 | Path with Minimum Effort (1631) | Medium | Dijkstra variation |
| 4 | Swim in Rising Water (778) | Hard | Dijkstra + binary search |

### With K Stops
```
PATTERN: Shortest path with constraint on intermediate nodes
TEMPLATE: Modified Bellman-Ford or Dijkstra with state
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Cheapest Flights Within K Stops (787) | Medium | Bellman-Ford or Dijkstra+DP |
| 2 | Maximum Profit in Job Scheduling | Hard | DP + binary search |

---

## 3. Connectivity & Components

### Undirected Graph
```
PATTERN: Count components, find connected regions
TEMPLATE: BFS/DFS from unvisited vertices
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Number of Provinces (547) | Medium | Union-Find or DFS |
| 2 | Number of Connected Components (323) | Medium | Union-Find |
| 3 | Graph Valid Tree (261) | Medium | Union-Find cycle check |
| 4 | Friend Circles (547) | Medium | BFS/DFS all |

### Directed Graph
```
PATTERN: Strongly connected components, reachability
TEMPLATE: Kosaraju's or Tarjan's algorithm
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Find Eventual Safe States (802) | Medium | Reverse graph + topo sort |
| 2 | Loud and Rich (851) | Medium | Topo sort on reversed DAG |

---

## 4. Cycle Detection

### Undirected Graph
```
PATTERN: Detect if adding edge creates cycle, find cycle
TEMPLATE: Union-Find or DFS with parent tracking
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Redundant Connection (684) | Medium | Union-Find |
| 2 | Find Redundant Path | Hard | Union-Find + path |

### Directed Graph
```
PATTERN: Detect cycles, detect deadlock
TEMPLATE: DFS with GRAY/BLACK coloring
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Course Schedule (207) | Medium | Topo sort detect cycle |
| 2 | Cycle in Matrix | Medium | DFS with visited |

---

## 5. Topological Sort Problems

### Basic
```
PATTERN: Prerequisites, ordering, scheduling
TEMPLATE: Kahn's algorithm or DFS post-order
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Course Schedule (207) | Medium | Topo sort |
| 2 | Course Schedule II (210) | Medium | Topo sort + order |
| 3 | Parallel Courses (1136) | Medium | Topo + counting layers |
| 4 | Minimum Height Trees (310) | Medium | Topo + remove leaves |

### Advanced
```
PATTERN: Dictionary ordering, reconstruction
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Alien Dictionary (269) | Hard | Topo + handle conflicts |
| 2 | Sequence Reconstruction (444) | Hard | Topo + uniqueness check |
| 3 | Build Matrix with Conditions (2392) | Hard | Topo + kahn's |

### Counting
```
PATTERN: Count orderings, count sequences
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Number of Ways to Reorder | Hard | Topo + combinatorics |
| 2 | Count All Possible Routes | Hard | DP + topo |

---

## 6. Union-Find Problems

### Cycle Detection
```
PATTERN: Adding edges, detect when cycle forms
TEMPLATE: Union-Find with path compression + rank
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Redundant Connection (684) | Medium | Union until union fails |
| 2 | Graph Valid Tree (261) | Medium | Union + count components |

### Dynamic Connectivity
```
PATTERN: Add connections, query connectivity
TEMPLATE: Union-Find with rollback (optional)
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Number of Islands II (305) | Hard | Union-Find with timesteps |
| 2 | Satisfiability of Equality (990) | Medium | Union for variables |
| 3 | Most Stones Removed (947) | Medium | Union by position |

### Kruskal's MST
```
PATTERN: Minimum cost to connect all, spanning tree
TEMPLATE: Sort edges + Union-Find
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Min Cost to Connect All Points (1584) | Medium | Kruskal's |
| 2 | Connecting Cities (1135) | Medium | Kruskal's |
| 3 | Find Critical and Pseudo-Critical (1489) | Hard | Kruskal's + try excluding |

---

## 7. Tree-Specific Problems

### Tree Traversal
```
PATTERN: Inorder, preorder, postorder, level order
TEMPLATE: DFS recursion or iterative with stack
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Inorder Traversal (94) | Easy | Multiple methods |
| 2 | Preorder Traversal (144) | Easy | Multiple methods |
| 3 | Postorder Traversal (145) | Easy | Multiple methods |
| 4 | Level Order Traversal (102) | Medium | BFS with levels |

### Tree Properties
```
PATTERN: Diameter, height, depth, path
TEMPLATE: DFS returning multiple values
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Diameter of Binary Tree (543) | Easy | DFS returning height |
| 2 | Balanced Binary Tree (110) | Easy | DFS checking height |
| 3 | Path Sum III (437) | Medium | Prefix sum on tree |
| 4 | Longest ZigZag Path (1372) | Medium | DFS with direction |

### LCA Problems
```
PATTERN: Lowest common ancestor queries
TEMPLATE: Various approaches (parent pointer, Euler tour, RMQ)
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Lowest Common Ancestor (236) | Medium | DFS |
| 2 | LCA of BST (235) | Easy | BST property |
| 3 | Kth Ancestor in Tree | Hard | Binary lifting |

---

## 8. Grid/Matrix Problems

### Island Problems
```
PATTERN: Connected components in 2D grid
TEMPLATE: BFS/DFS with 4/8 directions
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Number of Islands (200) | Easy | BFS/DFS |
| 2 | Max Area of Island (695) | Medium | BFS/DFS + track area |
| 3 | Island Perimeter (463) | Easy | Count edges |
| 4 | Number of Closed Islands (1254) | Medium | BFS boundary |

### Path in Grid
```
PATTERN: Find path, count paths, shortest path in matrix
TEMPLATE: BFS/DFS with visited
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Unique Paths (62) | Medium | DP |
| 2 | Unique Paths II (63) | Medium | DP with obstacles |
| 3 | Shortest Path in Grid with Obstacles | Medium | BFS |
| 4 | Longest Path in Matrix | Hard | Topo sort on DAG |

### Dynamic Programming on Grid
```
PATTERN: Optimization on grid, minimum/maximum path sum
TEMPLATE: DP with state management
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Triangle (120) | Medium | DP bottom-up |
| 2 | Minimum Path Sum (64) | Medium | DP |
| 3 | Cherry Pickup (741) | Hard | DP with states |

---

## 9. Weighted Graph Problems

### MST
```
PATTERN: Connect all nodes with minimum cost
TEMPLATE: Kruskal's or Prim's
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Min Cost to Connect All Points (1584) | Medium | Prim's or Kruskal's |
| 2 | Min Cost to Connect All Cities (1135) | Medium | Kruskal's |

### Shortest Path
```
PATTERN: Minimum distance/cost
TEMPLATE: Dijkstra, Bellman-Ford, Floyd-Warshall
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Network Delay Time (743) | Medium | Dijkstra |
| 2 | Bellman-Ford variants | Various | Negative weights |
| 3 | Floyd-Warshall | Hard | All pairs |

---

## 10. Advanced Algorithms

### Maximum Flow
```
PATTERN: Maximum throughput, assignment
TEMPLATE: Edmonds-Karp, Dinic
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Maximum bipartite matching | Various | Max flow |
| 2 | Network flow problems | Hard | Ford-Fulkerson |

### Tarjan's Algorithms
```
PATTERN: Critical connections, SCCs
TEMPLATE: Tarjan's low-link values
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Critical Connections (1192) | Hard | Tarjan's bridges |
| 2 | SCC problems | Various | Kosaraju's/Tarjan's |

### A* Search
```
PATTERN: Optimal pathfinding with heuristic
TEMPLATE: Priority queue with f = g + h
```

| # | Problem | Difficulty | Key Insight |
|---|---------|-----------|------------|
| 1 | Sliding Puzzle (773) | Hard | A* with Manhattan |
| 2 | 15-Puzzle | Hard | A* with linear conflict |

---

## 11. Problem-Solving Templates

### Template 1: BFS Shortest Path
```java
public int bfsShortestPath(int[][] grid, int[] start, int[] end) {
    int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[]{start[0], start[1], 0});
    boolean[][] visited = new boolean[grid.length][grid[0].length];
    visited[start[0]][start[1]] = true;

    while (!queue.isEmpty()) {
        int[] curr = queue.poll();
        if (curr[0] == end[0] && curr[1] == end[1]) return curr[2];

        for (int[] dir : dirs) {
            int nr = curr[0] + dir[0];
            int nc = curr[1] + dir[1];
            if (valid(nr, nc) && !visited[nr][nc]) {
                visited[nr][nc] = true;
                queue.offer(new int[]{nr, nc, curr[2] + 1});
            }
        }
    }
    return -1;
}
```

### Template 2: DFS Connected Components
```java
public void dfs(int node, Map<Integer, List<Integer>> adj, boolean[] visited) {
    visited[node] = true;
    for (int neighbor : adj.get(node)) {
        if (!visited[neighbor]) {
            dfs(neighbor, adj, visited);
        }
    }
}

// Count components:
int count = 0;
for (int i = 0; i < V; i++) {
    if (!visited[i]) {
        count++;
        dfs(i, adj, visited);
    }
}
```

### Template 3: Topological Sort (Kahn's)
```java
public List<Integer> topoSort(int V, Map<Integer, List<Integer>> adj) {
    int[] indegree = new int[V];
    for (int u = 0; u < V; u++) {
        for (int v : adj.get(u)) indegree[v]++;
    }

    Queue<Integer> q = new LinkedList<>();
    for (int i = 0; i < V; i++) {
        if (indegree[i] == 0) q.offer(i);
    }

    List<Integer> result = new ArrayList<>();
    while (!q.isEmpty()) {
        int u = q.poll();
        result.add(u);
        for (int v : adj.get(u)) {
            if (--indegree[v] == 0) q.offer(v);
        }
    }
    return result.size() == V ? result : Collections.emptyList(); // Has cycle
}
```

### Template 4: Union-Find
```java
class UnionFind {
    int[] parent, rank;
    UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }
    int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }
    void union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return;
        if (rank[px] < rank[py]) parent[px] = py;
        else if (rank[px] > rank[py]) parent[py] = px;
        else { parent[py] = px; rank[px]++; }
    }
    boolean connected(int x, int y) { return find(x) == find(y); }
}
```

### Template 5: Dijkstra
```java
public int[] dijkstra(int src, Map<Integer, List<int[]>> adj) {
    int[] dist = new int[adj.size()];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[src] = 0;
    PriorityQueue<int[]> pq = new PriorityQueue<>(
        Comparator.comparingInt(a -> a[1])
    );
    pq.offer(new int[]{src, 0});

    while (!pq.isEmpty()) {
        int[] curr = pq.poll();
        int u = curr[0], d = curr[1];
        if (d > dist[u]) continue;
        for (int[] edge : adj.get(u)) {
            int v = edge[0], w = edge[1];
            if (dist[u] + w < dist[v]) {
                dist[v] = dist[u] + w;
                pq.offer(new int[]{v, dist[v]});
            }
        }
    }
    return dist;
}
```

### Template 6: Tarjan's Bridges
```java
int time = 0;
int[] disc = new int[V], low = new int[V];
boolean[] visited = new boolean[V];
List<int[]> bridges = new ArrayList<>();

void dfs(int u, int parent) {
    visited[u] = true;
    disc[u] = low[u] = ++time;
    for (int v : adj.get(u)) {
        if (!visited[v]) {
            dfs(v, u);
            low[u] = Math.min(low[u], low[v]);
            if (low[v] > disc[u]) bridges.add(new int[]{u, v});
        } else if (v != parent) {
            low[u] = Math.min(low[u], disc[v]);
        }
    }
}
```

### Template 7: Kosaraju's SCC
```java
Stack<Integer> stack = new Stack<>();
boolean[] visited = new boolean[V];

// First DFS - fill stack
void dfs1(int u) {
    visited[u] = true;
    for (int v : adj.get(u)) if (!visited[v]) dfs1(v);
    stack.push(u);
}

// Second DFS on reversed graph
void dfs2(int u, List<Integer> component) {
    visited[u] = true;
    component.add(u);
    for (int v : revAdj.get(u)) if (!visited[v]) dfs2(v, component);
}

// Find all SCCs
List<List<Integer>> findSCCs() {
    List<List<Integer>> sccs = new ArrayList<>();
    visited = new boolean[V];
    for (int i = 0; i < V; i++) if (!visited[i]) dfs1(i);

    visited = new boolean[V];
    while (!stack.isEmpty()) {
        int v = stack.pop();
        if (!visited[v]) {
            List<Integer> component = new ArrayList<>();
            dfs2(v, component);
            sccs.add(component);
        }
    }
    return sccs;
}
```

---

## Problem Difficulty Progression

### Beginner (Start Here)
1. Number of Islands (200) - BFS/DFS basics
2. Course Schedule (207) - Topo sort
3. Clone Graph (133) - BFS/DFS
4. Max Depth of Binary Tree (104) - Tree traversal
5. Is Graph Bipartite (785) - BFS 2-coloring

### Intermediate (After Basics)
1. Cheapest Flights Within K Stops (787) - Dijkstra variant
2. Redundant Connection (684) - Union-Find
3. Network Delay Time (743) - Dijkstra
4. Number of Islands II (305) - Union-Find
5. Critical Connections (1192) - Tarjan's bridges

### Advanced (For Mastery)
1. Sliding Puzzle (773) - A* search
2. Alien Dictionary (269) - Topo + conflict resolution
3. Sequence Reconstruction (444) - Topo + uniqueness
4. Swim in Rising Water (778) - Dijkstra + binary search
5. Find Shortest Path Visiting All Nodes (847) - Bitmask DP

---

## Summary by Problem Type

```
┌─────────────────────────────────────────────────────────────────┐
│              QUICK LOOKUP BY PROBLEM TYPE                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  SHORTEST PATH:                                                │
│  - Unweighted: BFS                                             │
│  - Weighted positive: Dijkstra                                 │
│  - With constraints: Bellman-Ford or modified Dijkstra          │
│  - All pairs: Floyd-Warshall                                 │
│                                                                  │
│  CONNECTIVITY:                                                 │
│  - Count components: BFS/DFS/Union-Find                        │
│  - SCC: Kosaraju's or Tarjan's                               │
│  - Bridges: Tarjan's algorithm                                  │
│                                                                  │
│  TOPOLOGICAL:                                                 │
│  - Detect cycle: DFS with coloring                            │
│  - Get order: Kahn's or DFS post-order                       │
│  - Count orders: DP or backtracking                            │
│                                                                  │
│  CYCLES:                                                      │
│  - Undirected: DFS with parent or Union-Find                   │
│  - Directed: DFS with GRAY/BLACK                              │
│                                                                  │
│  TREES:                                                        │
│  - Traversal: BFS or DFS                                       │
│  - Properties: DFS returning multiple values                   │
│  - LCA: Various approaches                                      │
│                                                                  │
│  GRID:                                                         │
│  - Islands: BFS/DFS with directions                           │
│  - Paths: BFS or DFS                                          │
│  - DP: 2D DP with state management                           │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

> **Remember**: When you encounter a new graph problem, first identify WHAT you're being asked to find (shortest path? cycle? component?), then WHAT TYPE of graph (weighted? directed? tree?), and finally CHOOSE THE RIGHT ALGORITHM based on the templates above!
