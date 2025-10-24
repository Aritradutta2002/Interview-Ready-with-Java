# 🚀 Graph Algorithms - Complete Interview Preparation

**A comprehensive, production-ready Java implementation of graph algorithms from beginner to advanced level.**

[![Status](https://img.shields.io/badge/Status-Production%20Ready-green)]()
[![Java](https://img.shields.io/badge/Java-8%2B-orange)]()
[![Algorithms](https://img.shields.io/badge/Algorithms-33-blue)]()
[![Problems](https://img.shields.io/badge/LeetCode-10-red)]()

---

## 📁 What's Included

### **33 Production-Ready Algorithms**

- ✅ **8 Basic Traversal & Detection** algorithms
- ✅ **6 Shortest Path** algorithms (BFS, Dijkstra, Bellman-Ford, Floyd-Warshall, etc.)
- ✅ **6 Advanced** algorithms (MST, SCC, Bridges, Eulerian)
- ✅ **10 LeetCode Problems** with solutions
- ✅ **3 Common Utilities** (Graph, UnionFind, Pair)

### **Comprehensive Documentation**

- 📖 `ANALYSIS_AND_RECOMMENDATIONS.md` - Complete analysis & roadmap
- 📖 `ALGORITHM_COMPARISON.md` - Quick reference & pattern guide
- 📖 This README - Getting started & study guide

---

## 🎯 Quick Start

### **1. Compile All Files**

**Windows:**

```powershell
cd GRAPHS
javac -d out common\*.java level1_basics\*.java level2_shortest_paths\*.java level3_mst_and_advanced\*.java level4_problems\*.java
```

**Linux/Mac:**

```bash
cd GRAPHS
mkdir -p out
javac -d out common/*.java level1_basics/*.java level2_shortest_paths/*.java level3_mst_and_advanced/*.java level4_problems/*.java
```

### **2. Run Examples**

```bash
java -cp out BFS          # Test Breadth-First Search
java -cp out DFSIterative # Test Depth-First Search
```

---

## 📚 Study Roadmap

### **Week 1-2: Foundations** ⭐

**Files to Study:**

- `common/Graph.java` - Graph representation
- `level1_basics/BFS.java` - Breadth-First Search
- `level1_basics/DFSRecursive.java` - Depth-First Search
- `level1_basics/DFSIterative.java` - Iterative DFS

**Practice:**

- Number of Islands (LC 200) ✅
- Number of Provinces (LC 547) ✅

---

### **Week 3-4: Intermediate** ⭐⭐

**Files to Study:**

- `common/UnionFind.java` - Disjoint Set
- `level1_basics/CycleDetection*.java` - Cycle detection
- `level1_basics/TopologicalSort.java` - Ordering
- `level1_basics/BipartiteCheck.java` - 2-coloring

**Practice:**

- Course Schedule (LC 207) ✅
- Course Schedule II (LC 210) ✅

---

### **Week 5-6: Shortest Paths** ⭐⭐⭐

**Files to Study:**

- `level2_shortest_paths/Dijkstra.java` - Non-negative weights
- `level2_shortest_paths/BellmanFord.java` - Negative weights
- `level2_shortest_paths/FloydWarshall.java` - All-pairs

**Practice:**

- Network Delay Time (LC 743) ✅
- Cheapest Flights (LC 787) ✅
- Word Ladder (LC 127) ✅

---

### **Week 7-8: Advanced** ⭐⭐⭐⭐

**Files to Study:**

- `level3_mst_and_advanced/KruskalMST.java` - MST
- `level3_mst_and_advanced/PrimMST.java` - MST (dense)
- `level3_mst_and_advanced/KosarajuSCC.java` - SCC
- `level3_mst_and_advanced/TarjanSCC.java` - SCC (1-pass)

**Practice:**

- Min Cost to Connect All Points (to add)
- Critical Connections (to add)

---

## 🗂️ Folder Structure

```
GRAPHS/
├── common/                  # Utilities (3 files)
│   ├── Graph.java          # Adjacency list representation
│   ├── UnionFind.java      # Disjoint set with path compression
│   └── Pair.java           # Generic pair
│
├── level1_basics/          # Fundamentals (8 files)
│   ├── BFS.java
│   ├── DFSRecursive.java
│   ├── DFSIterative.java
│   ├── BipartiteCheck.java
│   ├── ConnectedComponents.java
│   ├── CycleDetectionDirected.java
│   ├── CycleDetectionUndirected.java
│   └── TopologicalSort.java
│
├── level2_shortest_paths/  # Shortest paths (6 files)
│   ├── UnweightedShortestPath.java
│   ├── Dijkstra.java
│   ├── BellmanFord.java
│   ├── FloydWarshall.java
│   ├── DAGShortestPath.java
│   └── ZeroOneBFS.java
│
├── level3_mst_and_advanced/ # Advanced (6 files)
│   ├── PrimMST.java
│   ├── KruskalMST.java
│   ├── KosarajuSCC.java
│   ├── TarjanSCC.java
│   ├── BridgesArticulationPoints.java
│   └── EulerianPathCircuit.java
│
└── level4_problems/        # LeetCode problems (10 files)
    ├── NumberOfIslands.java          # LC 200
    ├── NumberOfProvinces.java        # LC 547
    ├── RottingOranges.java           # LC 994
    ├── WordLadder.java               # LC 127
    ├── CourseSchedule.java           # LC 207
    ├── CourseScheduleII.java         # LC 210
    ├── CloneGraph.java               # LC 133
    ├── AlienDictionary.java          # LC 269
    ├── NetworkDelayTime.java         # LC 743
    └── CheapestFlightsWithinKStops.java # LC 787
```

---

## 🔍 Algorithm Quick Reference

| Algorithm            | Time         | Space | Use When                      |
|----------------------|--------------|-------|-------------------------------|
| **BFS**              | O(V+E)       | O(V)  | Shortest path (unweighted)    |
| **DFS**              | O(V+E)       | O(V)  | Explore all paths, cycles     |
| **Dijkstra**         | O((V+E)logV) | O(V)  | Shortest path (non-negative)  |
| **Bellman-Ford**     | O(VE)        | O(V)  | Negative weights allowed      |
| **Floyd-Warshall**   | O(V³)        | O(V²) | All-pairs shortest paths      |
| **Union-Find**       | O(α(V))      | O(V)  | Dynamic connectivity          |
| **Topological Sort** | O(V+E)       | O(V)  | DAG ordering                  |
| **Kruskal MST**      | O(ElogE)     | O(V)  | Sparse graphs                 |
| **Prim MST**         | O((V+E)logV) | O(V)  | Dense graphs                  |
| **Kosaraju/Tarjan**  | O(V+E)       | O(V)  | Strongly connected components |

---

## 🎯 Problem Patterns

### **Pattern 1: Shortest Path**

**Indicators:** "minimum distance", "shortest path", "fewest steps"

- ✅ Word Ladder
- ✅ Network Delay Time
- ✅ Cheapest Flights

### **Pattern 2: Connectivity**

**Indicators:** "connected", "islands", "provinces", "groups"

- ✅ Number of Islands
- ✅ Number of Provinces

### **Pattern 3: Cycle Detection**

**Indicators:** "detect cycle", "can finish", "circular"

- ✅ Course Schedule

### **Pattern 4: Topological Sort**

**Indicators:** "ordering", "prerequisites", "dependencies"

- ✅ Course Schedule II
- ✅ Alien Dictionary

### **Pattern 5: Multi-Source BFS**

**Indicators:** "multiple sources", "spread", "expand"

- ✅ Rotting Oranges

---

## ⚡ Performance Tips

### **When to Use What**

**Shortest Path:**

- Unweighted → **BFS**
- Weighted (positive) → **Dijkstra**
- Weighted (negative) → **Bellman-Ford**
- All-pairs → **Floyd-Warshall**

**Connectivity:**

- Static → **DFS/BFS**
- Dynamic → **Union-Find**

**MST:**

- Sparse graph → **Kruskal**
- Dense graph → **Prim**

---

## 🚨 Common Mistakes

- ❌ Not marking nodes as visited
- ❌ Using wrong data structure (Queue vs PriorityQueue)
- ❌ Integer overflow in distance arrays
- ❌ Forgetting disconnected components
- ❌ Wrong direction in directed graphs

---

## 📖 Documentation Files

### **1. ANALYSIS_AND_RECOMMENDATIONS.md**

- Current state analysis (Score: 8.5/10)
- Missing algorithms (Flow, A*, Bipartite Matching, etc.)
- Roadmap for improvements
- Priority additions

### **2. ALGORITHM_COMPARISON.md**

- Algorithm comparison tables
- Pattern recognition guide
- Company-specific focus
- Learning path

---

## 🎓 Interview Preparation

### **By Company**

**Google:**

- Advanced shortest paths
- SCC, Bridges
- Grid problems

**Meta:**

- BFS/DFS
- Union-Find
- Social networks

**Amazon:**

- Basic traversals
- Topological sort
- Grid problems

**Microsoft:**

- Shortest paths
- MST
- Connectivity

---

## ✅ Checklist

Before each interview, ensure you can:

- [ ] Explain each algorithm in plain English
- [ ] Write code from scratch
- [ ] Analyze time/space complexity
- [ ] Identify when to use which algorithm
- [ ] Handle edge cases

---

## 📊 Current Status

**Strengths:**

- ✅ Clean, readable code
- ✅ Well-organized structure
- ✅ Comprehensive coverage of basics
- ✅ Production-ready implementations

**To Add (See ANALYSIS_AND_RECOMMENDATIONS.md):**

- ⭐ Flow algorithms (Ford-Fulkerson, Dinic)
- ⭐ A* Search
- ⭐ Bipartite Matching
- ⭐ 20+ more LeetCode problems

---

## 🚀 Next Steps

1. **Master the basics** (Level 1)
2. **Practice 20+ problems** (Level 4)
3. **Review patterns** (ALGORITHM_COMPARISON.md)
4. **Add missing algorithms** (ANALYSIS_AND_RECOMMENDATIONS.md)

---

## 📞 Resources

- **LeetCode:** Graph tag (200+ problems)
- **VisuAlgo:** Algorithm visualizations
- **CLRS Book:** Chapters 22-26
- **GeeksforGeeks:** Detailed tutorials

---

**Version:** 2.0  
**Last Updated:** 2025-10-23  
**Status:** ✅ Production Ready  
**Files:** 33 algorithms + 3 utilities + 2 documentation guides

**Happy Coding! 🎯**
