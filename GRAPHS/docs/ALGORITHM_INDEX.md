# 📊 Graph Algorithms - Complete Index

**Quick Reference Guide for all Graph Algorithms**

This index maps all graph algorithms from interview preparation lists to their implementations in this repository.

---

## 📋 Table of Contents
1. [Basic Graph Traversal](#basic-graph-traversal)
2. [Shortest Path Algorithms](#shortest-path-algorithms)
3. [Minimum Spanning Tree (MST)](#minimum-spanning-tree-mst)
4. [Cycle Detection](#cycle-detection)
5. [Topological Sort](#topological-sort)
6. [Graph Properties](#graph-properties)
7. [Advanced Algorithms](#advanced-algorithms)
8. [Quick Problem Finder](#quick-problem-finder)

---

## 🎯 Basic Graph Traversal

### **1. DFS (Depth-First Search)**
- **Files:**
  - `level1_basics/DFSRecursive.java` ⭐ (6 different approaches with detailed explanations)
  - `level1_basics/DFSIterative.java`
- **Time:** O(V + E)
- **Space:** O(V)
- **When to Use:**
  - Tree traversals
  - Cycle detection
  - Path finding
  - Connected components
  - Backtracking problems
- **Key Implementations:**
  - Basic traversal
  - All components
  - Path tracking
  - Pre & Post order
  - Cycle detection

---

### **2. BFS (Breadth-First Search)**
- **Files:**
  - `level1_basics/BFS.java` ⭐ (5 different approaches)
  - `level1_basics/BFS_IMPROVED.java`
- **Time:** O(V + E)
- **Space:** O(V)
- **When to Use:**
  - Shortest path in unweighted graphs ✅
  - Level-order traversal
  - Finding nearest node
  - Multi-source problems
- **Key Implementations:**
  - Distance calculation
  - Path reconstruction
  - Multi-source BFS
  - Level-order BFS

---

## 🛤️ Shortest Path Algorithms

### **3. Dijkstra's Algorithm (Single Source Shortest Path)**
- **File:** `level2_shortest_paths/Dijkstra.java` ⭐
- **Time:** O((V + E) log V) with priority queue, O(V²) with array
- **Space:** O(V)
- **When to Use:**
  - Non-negative edge weights
  - Single source to all destinations
  - GPS navigation, network routing
- **Key Implementations:**
  - Classic with priority queue (OPTIMIZED)
  - With path reconstruction
  - Early termination
  - Array-based (for dense graphs)
  - All-pairs variant

**LeetCode Problems:**
- Path With Minimum Effort (Techdose)
- Network Delay Time
- Cheapest Flights Within K Stops

---

### **4. Bellman-Ford Algorithm**
- **File:** `level2_shortest_paths/BellmanFord.java`
- **Time:** O(V * E)
- **Space:** O(V)
- **When to Use:**
  - Can handle negative weights
  - Detect negative cycles
  - Distributed systems

---

### **5. Floyd-Warshall (All-Pairs Shortest Path)**
- **File:** `level2_shortest_paths/FloydWarshall.java` ⭐
- **Time:** O(V³)
- **Space:** O(V²)
- **When to Use:**
  - Need distances between ALL pairs
  - Dense graphs
  - Negative weights allowed
  - Check for infinite value queries ✅
- **Key Implementations:**
  - Basic in-place version
  - With path reconstruction
  - Negative cycle detection
  - Space-optimized
  - Transitive closure

**Interview Problems:**
- Network connectivity
- Checking reachability
- Finding shortest paths for all pairs

---

### **6. Shortest Path in DAG**
- **File:** `level2_shortest_paths/DAGShortestPath.java`
- **Time:** O(V + E)
- **Space:** O(V)
- **When to Use:**
  - Directed Acyclic Graphs only
  - Faster than Dijkstra for DAGs
  - Can handle negative weights

---

## 🌳 Minimum Spanning Tree (MST)

### **7. Kruskal's Algorithm (MST)**
- **File:** `level3_mst_and_advanced/KruskalMST.java` ⭐
- **Time:** O(E log E)
- **Space:** O(V + E)
- **When to Use:**
  - Sparse graphs (fewer edges)
  - Need actual MST edges
  - Network design problems
- **Key Implementations:**
  - Basic with Union-Find
  - With edge tracking
  - Maximum spanning tree
  - Component progress tracking
  - Second-best MST

**Interview Problems:**
- Min Cost to Connect All Points (Techdose)
- Connecting Cities With Minimum Cost

---

### **8. Prim's Algorithm (MST)**
- **File:** `level3_mst_and_advanced/PrimMST.java` ⭐
- **Time:** O((V + E) log V) with priority queue, O(V²) with array
- **Space:** O(V + E)
- **When to Use:**
  - Dense graphs (many edges)
  - Growing MST from a starting point
- **Key Implementations:**
  - Optimized with priority queue
  - With edge tracking
  - Array-based (for dense graphs)
  - From specific vertex
  - Lazy Prim's
  - Maximum spanning tree

**Comparison:**
- **Kruskal's:** Better for sparse graphs, considers all edges
- **Prim's:** Better for dense graphs, grows from one vertex

---

## 🔄 Cycle Detection

### **9. Cycle Detection - Undirected Graph**
- **File:** `level1_basics/CycleDetectionUndirected.java`
- **Time:** O(V + E)
- **Space:** O(V)
- **Methods:**
  - DFS-based
  - BFS-based
  - Union-Find based

---

### **10. Cycle Detection - Directed Graph**
- **File:** `level1_basics/CycleDetectionDirected.java`
- **Time:** O(V + E)
- **Space:** O(V)
- **Methods:**
  - DFS with recursion stack
  - Color-based approach
  - Topological sort based

---

## 📊 Topological Sort

### **11. Topological Sort**
- **File:** `level1_basics/TopologicalSort.java` ⭐
- **Time:** O(V + E)
- **Space:** O(V)
- **When to Use:**
  - Task scheduling
  - Build dependencies
  - Course prerequisites
- **Key Implementations:**
  - DFS-based (post-order)
  - BFS-based (Kahn's algorithm)

**LeetCode Problems:**
- Course Schedule (Techdose)
- Course Schedule II (Techdose)
- Course Schedule IV

---

## 🔗 Graph Properties

### **12. Bipartite Graph Check**
- **File:** `level1_basics/BipartiteCheck.java`
- **Time:** O(V + E)
- **Space:** O(V)
- **When to Use:**
  - 2-coloring problems
  - Matching problems
  - Conflict resolution

**LeetCode Problems:**
- Possible Bipartition (Techdose)

---

### **13. Connected Components**
- **File:** `level1_basics/ConnectedComponents.java`
- **Time:** O(V + E)
- **Space:** O(V)
- **When to Use:**
  - Finding isolated subgraphs
  - Clustering
  - Network analysis

---

### **14. Disjoint Set Union (Union-Find / DSU)**
- **File:** `Basics/UnionFind.java` ⭐
- **Time:** O(α(n)) - nearly constant (inverse Ackermann)
- **Space:** O(V)
- **When to Use:**
  - Dynamic connectivity
  - Kruskal's MST
  - Cycle detection
  - Count of components
- **Optimizations:**
  - Path compression
  - Union by rank
  - Union by size

**Interview Problems:**
- Number of Provinces (Techdose)
- Accounts Merge
- Redundant Connection

---

## 🔥 Advanced Algorithms

### **15. Tarjan's Algorithm - Bridges**
- **File:** `level3_mst_and_advanced/TarjanBridgesArticulationPoints.java` ⭐
- **Time:** O(V + E)
- **Space:** O(V)
- **When to Use:**
  - Find critical edges
  - Network vulnerability analysis
  - Single points of failure

**LeetCode Problems:**
- Critical Connections in a Network (Techdose)

---

### **16. Articulation Points (Cut Vertices)**
- **File:** `level3_mst_and_advanced/TarjanBridgesArticulationPoints.java` ⭐
- **Time:** O(V + E)
- **Space:** O(V)
- **When to Use:**
  - Find critical vertices
  - Network reliability
  - Infrastructure analysis

---

### **17. Strongly Connected Components (SCC)**
- **Files:**
  - `level3_mst_and_advanced/TarjanSCC.java` (Tarjan's algorithm)
  - `level3_mst_and_advanced/KosarajuSCC.java` (Kosaraju's algorithm)
- **Time:** O(V + E)
- **Space:** O(V)
- **When to Use:**
  - Find strongly connected regions in directed graphs
  - Dependency analysis
  - Social network analysis

---

### **18. Eulerian Path/Circuit**
- **File:** `level3_mst_and_advanced/EulerianPathCircuit.java`
- **Time:** O(V + E)
- **Space:** O(E)
- **When to Use:**
  - Path that visits every edge exactly once
  - Circuit design
  - Route optimization

---

### **19. 0-1 BFS**
- **File:** `level2_shortest_paths/ZeroOneBFS.java`
- **Time:** O(V + E)
- **Space:** O(V)
- **When to Use:**
  - Shortest path with edge weights 0 or 1
  - Special case optimization

---

## 🎯 Quick Problem Finder

### By Technique:

| Problem Type | Algorithm | File |
|-------------|-----------|------|
| Shortest path (unweighted) | BFS | `level1_basics/BFS.java` |
| Shortest path (weighted, non-negative) | Dijkstra | `level2_shortest_paths/Dijkstra.java` |
| Shortest path (negative weights) | Bellman-Ford | `level2_shortest_paths/BellmanFord.java` |
| All-pairs shortest path | Floyd-Warshall | `level2_shortest_paths/FloydWarshall.java` |
| Minimum spanning tree (sparse) | Kruskal | `level3_mst_and_advanced/KruskalMST.java` |
| Minimum spanning tree (dense) | Prim | `level3_mst_and_advanced/PrimMST.java` |
| Cycle detection | DFS/Union-Find | `level1_basics/CycleDetection*.java` |
| Task scheduling | Topological Sort | `level1_basics/TopologicalSort.java` |
| 2-coloring | Bipartite Check | `level1_basics/BipartiteCheck.java` |
| Critical edges | Tarjan Bridges | `level3_mst_and_advanced/TarjanBridgesArticulationPoints.java` |
| Critical vertices | Articulation Points | `level3_mst_and_advanced/TarjanBridgesArticulationPoints.java` |
| Dynamic connectivity | Union-Find | `Basics/UnionFind.java` |

---

### By Company Frequency (MAANG):

#### **Amazon**
1. Clone Graph ✅
2. Number of Provinces (DSU)
3. Course Schedule (Topological Sort)
4. Min Cost to Connect All Points (Kruskal/Prim)
5. Network Delay Time (Dijkstra)

#### **Google**
1. Shortest Path to Get Food (BFS)
2. Critical Connections (Tarjan Bridges)
3. Number of Provinces (DSU)
4. Course Schedule II (Topological Sort)

#### **Meta (Facebook)**
1. Clone Graph ✅
2. Course Schedule (Topological Sort)
3. Shortest Path in Binary Matrix (BFS)
4. Is Graph Bipartite

#### **Microsoft**
1. Clone Graph ✅
2. Number of Provinces (DSU)
3. Critical Connections (Tarjan)
4. Course Schedule (Topological Sort)

#### **Apple**
1. Course Schedule (Topological Sort)
2. Number of Islands (DFS/BFS)
3. Network Delay Time (Dijkstra)

---

## 📚 Learning Path

### **Beginner (Start Here)**
1. ✅ DFS Recursive (`level1_basics/DFSRecursive.java`)
2. ✅ BFS (`level1_basics/BFS.java`)
3. ✅ Cycle Detection
4. ✅ Connected Components

### **Intermediate**
5. ✅ Topological Sort
6. ✅ Bipartite Check
7. ✅ Dijkstra's Algorithm
8. ✅ Union-Find (DSU)

### **Advanced**
9. ✅ Kruskal's & Prim's MST
10. ✅ Floyd-Warshall
11. ✅ Tarjan's Algorithm (Bridges/Articulation Points)
12. ✅ Strongly Connected Components

---

## 🎓 Algorithm Cheat Sheet

### Time Complexity Summary

| Algorithm | Time | Best For |
|-----------|------|----------|
| DFS/BFS | O(V + E) | Traversal, connectivity |
| Dijkstra | O((V+E) log V) | Single-source shortest path |
| Bellman-Ford | O(V*E) | Negative weights |
| Floyd-Warshall | O(V³) | All-pairs shortest path |
| Kruskal | O(E log E) | MST on sparse graphs |
| Prim | O((V+E) log V) | MST on dense graphs |
| Topological Sort | O(V + E) | Task scheduling |
| Union-Find | O(α(n)) | Dynamic connectivity |
| Tarjan | O(V + E) | Bridges, articulation points |

---

## 🔍 Common Patterns

### **Pattern 1: Shortest Path**
- Unweighted → **BFS**
- Weighted (non-negative) → **Dijkstra**
- Weighted (with negatives) → **Bellman-Ford**
- All pairs → **Floyd-Warshall**

### **Pattern 2: Minimum Spanning Tree**
- Sparse graph → **Kruskal**
- Dense graph → **Prim**

### **Pattern 3: Cycle Detection**
- Undirected → **DFS with parent** or **Union-Find**
- Directed → **DFS with recursion stack** or **Topological Sort**

### **Pattern 4: Connectivity**
- Check if connected → **DFS/BFS**
- Dynamic connectivity → **Union-Find**
- Count components → **DFS/BFS** or **Union-Find**

---

## 📝 Notes

- ⭐ indicates files with extensive documentation and multiple implementations
- All implementations include:
  - Detailed comments
  - Multiple approaches (normal + optimized)
  - Time/Space complexity analysis
  - Working examples with test cases
  - Edge case handling

---

## 🚀 Quick Start

1. **Basic understanding:** Start with `level1_basics/`
2. **Shortest paths:** Move to `level2_shortest_paths/`
3. **Advanced topics:** Proceed to `level3_mst_and_advanced/`
4. **Practice problems:** Check `level4_problems/`

---

*Last Updated: October 2025*
*For more details, see individual algorithm files*

