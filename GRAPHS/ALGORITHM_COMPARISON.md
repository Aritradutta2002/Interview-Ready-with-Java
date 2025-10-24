# Graph Algorithms - Quick Reference & Comparison

## 🎯 When to Use Which Algorithm

### **Shortest Path Problems**

| Algorithm             | Time Complexity | Space | Best For                               | Limitations                       |
|-----------------------|-----------------|-------|----------------------------------------|-----------------------------------|
| **BFS (Unweighted)**  | O(V+E)          | O(V)  | Unweighted graphs, minimum hops        | Only unweighted                   |
| **Dijkstra**          | O((V+E)log V)   | O(V)  | Non-negative weighted graphs           | Doesn't handle negative weights   |
| **Bellman-Ford**      | O(VE)           | O(V)  | Graphs with negative weights           | Slow, but detects negative cycles |
| **Floyd-Warshall**    | O(V³)           | O(V²) | All-pairs shortest paths, small graphs | Memory intensive                  |
| **DAG Shortest Path** | O(V+E)          | O(V)  | Directed acyclic graphs                | Only works on DAGs                |
| **0-1 BFS**           | O(V+E)          | O(V)  | Edges with weight 0 or 1               | Limited weight range              |
| **A***                | O(E) best case  | O(V)  | Grid pathfinding with heuristic        | Needs good heuristic              |

### **Graph Traversal**

| Algorithm           | Time   | Space | Order          | Use Case                          |
|---------------------|--------|-------|----------------|-----------------------------------|
| **BFS**             | O(V+E) | O(V)  | Level-by-level | Shortest path, level-order        |
| **DFS (Recursive)** | O(V+E) | O(V)  | Depth-first    | Cycle detection, topological sort |
| **DFS (Iterative)** | O(V+E) | O(V)  | Depth-first    | Avoid stack overflow              |

### **Cycle Detection**

| Graph Type     | Algorithm                           | Time      | Space |
|----------------|-------------------------------------|-----------|-------|
| **Undirected** | DFS with parent tracking            | O(V+E)    | O(V)  |
| **Undirected** | Union-Find                          | O(E·α(V)) | O(V)  |
| **Directed**   | DFS with recursion stack            | O(V+E)    | O(V)  |
| **Directed**   | Kahn's algorithm (topological sort) | O(V+E)    | O(V)  |

### **Minimum Spanning Tree (MST)**

| Algorithm   | Time           | Space | Best For                    |
|-------------|----------------|-------|-----------------------------|
| **Kruskal** | O(E log E)     | O(V)  | Sparse graphs (fewer edges) |
| **Prim**    | O((V+E) log V) | O(V)  | Dense graphs (many edges)   |

### **Strongly Connected Components (SCC)**

| Algorithm    | Time   | Space | Notes                              |
|--------------|--------|-------|------------------------------------|
| **Kosaraju** | O(V+E) | O(V)  | Easier to understand, 2 DFS passes |
| **Tarjan**   | O(V+E) | O(V)  | Single DFS pass, more complex      |

### **Topological Sort**

| Algorithm        | Time   | Space | Detects Cycles?              |
|------------------|--------|-------|------------------------------|
| **Kahn's (BFS)** | O(V+E) | O(V)  | Yes (empty result if cycle)  |
| **DFS-based**    | O(V+E) | O(V)  | Yes (returns empty if cycle) |

---

## 🔑 Problem Type Recognition

### **Pattern: Shortest Path**

**Indicators:** "minimum distance", "shortest path", "fewest steps"

**Choose:**

- Unweighted → **BFS**
- Weighted (non-negative) → **Dijkstra**
- Weighted (with negative) → **Bellman-Ford**
- All pairs → **Floyd-Warshall**
- DAG → **DAG Shortest Path** (topological sort)

**Examples:**

- ✅ Word Ladder
- ✅ Network Delay Time
- ✅ Cheapest Flights Within K Stops

---

### **Pattern: Connectivity**

**Indicators:** "connected components", "islands", "provinces", "groups"

**Choose:**

- Count components → **DFS/BFS** or **Union-Find**
- Dynamic connectivity → **Union-Find**

**Examples:**

- ✅ Number of Islands
- ✅ Number of Provinces
- ⭐ Friend Circles
- ⭐ Accounts Merge (Union-Find)

---

### **Pattern: Cycle Detection**

**Indicators:** "detect cycle", "circular dependency", "can finish"

**Choose:**

- Undirected → **DFS with parent** or **Union-Find**
- Directed → **DFS with recursion stack** or **Kahn's**

**Examples:**

- ✅ Course Schedule (directed)
- ⭐ Redundant Connection (undirected)
- ⭐ Graph Valid Tree

---

### **Pattern: Topological Sort**

**Indicators:** "ordering", "prerequisite", "schedule", "dependencies"

**Choose:** **Kahn's algorithm** (BFS) or **DFS-based**

**Examples:**

- ✅ Course Schedule II
- ✅ Alien Dictionary
- ⭐ Sequence Reconstruction
- ⭐ Minimum Height Trees

---

### **Pattern: Multi-Source BFS**

**Indicators:** "multiple starting points", "spread", "expand simultaneously"

**Choose:** **BFS with all sources in initial queue**

**Examples:**

- ✅ Rotting Oranges
- ⭐ Walls and Gates
- ⭐ Shortest Distance from All Buildings

---

### **Pattern: Bipartite**

**Indicators:** "two groups", "two sets", "partition", "bi-coloring"

**Choose:** **BFS/DFS with 2-coloring**

**Examples:**

- ✅ Is Graph Bipartite (BipartiteCheck.java)
- ⭐ Possible Bipartition

---

### **Pattern: Union-Find**

**Indicators:** "dynamic connectivity", "grouping", "merging sets"

**Choose:** **Union-Find with path compression**

**Examples:**

- ✅ Number of Provinces
- ⭐ Redundant Connection
- ⭐ Accounts Merge
- ⭐ Similar String Groups

---

### **Pattern: Grid Traversal**

**Indicators:** "matrix", "grid", "2D array", "island"

**Choose:** **DFS/BFS on grid** (treat as implicit graph)

**Examples:**

- ✅ Number of Islands
- ⭐ Surrounded Regions
- ⭐ Pacific Atlantic Water Flow
- ⭐ Shortest Path in Binary Matrix

---

### **Pattern: Bridge/Articulation Point**

**Indicators:** "critical connection", "remove edge/node", "disconnects graph"

**Choose:** **Tarjan's algorithm** (DFS with low-link values)

**Examples:**

- ✅ Bridges and Articulation Points
- ⭐ Critical Connections in Network

---

### **Pattern: Minimum Spanning Tree**

**Indicators:** "connect all nodes", "minimum cost", "spanning tree"

**Choose:**

- Sparse graph → **Kruskal (Union-Find)**
- Dense graph → **Prim (Priority Queue)**

**Examples:**

- ⭐ Min Cost to Connect All Points
- ⭐ Optimize Water Distribution

---

### **Pattern: Eulerian Path/Circuit**

**Indicators:** "visit all edges exactly once", "reconstruct path"

**Choose:** **Hierholzer's algorithm**

**Examples:**

- ✅ Eulerian Path Circuit
- ⭐ Reconstruct Itinerary

---

## 📊 Complexity Comparison

### **Time Complexity (V = vertices, E = edges)**

| Operation                       | BFS/DFS    | Dijkstra      | Bellman-Ford | Floyd-Warshall | Union-Find |
|---------------------------------|------------|---------------|--------------|----------------|------------|
| **Traversal**                   | O(V+E)     | -             | -            | -              | -          |
| **Single-source shortest path** | O(V+E)*    | O((V+E)logV)  | O(VE)        | -              | -          |
| **All-pairs shortest path**     | O(V(V+E))* | O(V(V+E)logV) | -            | O(V³)          | -          |
| **Cycle detection**             | O(V+E)     | -             | O(VE)        | -              | O(E·α(V))  |
| **MST**                         | -          | -             | -            | -              | O(E·α(V))  |

*Only for unweighted graphs

---

## 🎯 Space Complexity

| Algorithm            | Auxiliary Space | Notes                  |
|----------------------|-----------------|------------------------|
| **BFS**              | O(V)            | Queue + visited array  |
| **DFS (Recursive)**  | O(V)            | Call stack             |
| **DFS (Iterative)**  | O(V)            | Explicit stack         |
| **Dijkstra**         | O(V)            | Priority queue         |
| **Bellman-Ford**     | O(V)            | Distance array         |
| **Floyd-Warshall**   | O(V²)           | 2D distance matrix     |
| **Union-Find**       | O(V)            | Parent and rank arrays |
| **Topological Sort** | O(V)            | Queue + indegree array |

---

## 🏢 Company-Specific Focus

### **Google**

- Shortest path variations
- Advanced graph problems (SCC, bridges)
- Grid-based problems

### **Meta/Facebook**

- BFS/DFS applications
- Union-Find
- Social network problems

### **Amazon**

- Basic traversals
- Topological sort
- Grid problems

### **Microsoft**

- Shortest paths
- Graph connectivity
- MST problems

---

## 🎓 Learning Path

### **Beginner (Week 1-2)**

1. ✅ BFS, DFS (both recursive and iterative)
2. ✅ Connected Components
3. ✅ Cycle Detection (both directed and undirected)
4. ✅ Bipartite Check

### **Intermediate (Week 3-4)**

1. ✅ Topological Sort
2. ✅ Dijkstra's Algorithm
3. ✅ Union-Find
4. ✅ Basic problems (Islands, Provinces, Rotting Oranges)

### **Advanced (Week 5-6)**

1. ✅ Bellman-Ford, Floyd-Warshall
2. ✅ MST (Kruskal, Prim)
3. ✅ SCC (Kosaraju, Tarjan)
4. ✅ Bridges and Articulation Points

### **Expert (Week 7-8)**

1. ⭐ Flow algorithms (Ford-Fulkerson, Dinic)
2. ⭐ Bipartite matching
3. ⭐ Complex problem variations
4. ⭐ Optimization techniques

---

## 💡 Pro Tips

### **Choosing Between BFS and DFS**

- **Use BFS when:**
    - Finding shortest path (unweighted)
    - Level-order traversal needed
    - Solution likely near root

- **Use DFS when:**
    - Need to explore all paths
    - Backtracking required
    - Memory constrained (iterative DFS)

### **Dijkstra vs Bellman-Ford**

- **Dijkstra:** Faster, but requires non-negative weights
- **Bellman-Ford:** Slower, handles negative weights, detects negative cycles

### **Kruskal vs Prim**

- **Kruskal:** Better for sparse graphs, sort edges once
- **Prim:** Better for dense graphs, maintains heap

### **When to Use Union-Find**

- Dynamic connectivity queries
- Building MST (Kruskal)
- Grouping elements
- Cycle detection in undirected graphs

---

## 🔍 Debugging Checklist

### **Common Mistakes**

- [ ] Forgot to mark nodes as visited
- [ ] Wrong graph direction (directed vs undirected)
- [ ] Off-by-one errors in grid problems
- [ ] Not handling disconnected components
- [ ] Integer overflow in distance calculations
- [ ] Wrong queue type (Queue vs Deque vs PriorityQueue)

### **Testing Strategy**

1. **Empty graph** (0 nodes)
2. **Single node** (1 node)
3. **Two nodes** (connected/disconnected)
4. **Disconnected components**
5. **Cycle present**
6. **Tree structure**
7. **Complete graph**
8. **Large graph** (performance test)

---

## 📚 Resources

### **Practice Platforms**

- LeetCode: Graph tag (~200 problems)
- CodeForces: Graph section
- GeeksforGeeks: Graph algorithms

### **Books**

- "Introduction to Algorithms" (CLRS)
- "Algorithm Design Manual" (Skiena)
- "Competitive Programming" (Halim)

### **Visualizations**

- VisuAlgo.net
- GraphOnline.ru
- Algorithm-visualizer.org

---

**Last Updated:** 2025-10-23
**Status:** Ready for enhancement with Priority 1 additions
