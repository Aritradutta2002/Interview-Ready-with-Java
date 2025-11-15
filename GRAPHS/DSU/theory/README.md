# Disjoint Set Union (DSU) / Union-Find

## 🎯 Overview

DSU is a **CORE GRAPH ALGORITHM** that efficiently handles dynamic connectivity in graphs. It's essential for:
- Finding connected components
- Detecting cycles in undirected graphs  
- Implementing Kruskal's MST algorithm
- Solving network connectivity problems

## 📁 Files in This Package

### 1. **DisjointSetUnion.java** ⭐ MASTER FILE
**Complete DSU implementation with all optimizations**

**Key Features:**
- Path Compression (makes find O(α(n)))
- Union by Rank (keeps tree balanced)
- Cycle detection
- Valid tree checking
- Component size tracking

**Methods:**
- `find(x)` - Find root with path compression
- `union(x, y)` - Connect two components
- `connected(x, y)` - Check if nodes connected
- `hasCycle(edges[][])` - Detect cycle in graph
- `isValidTree(n, edges[][])` - Check if graph is valid tree

**Time Complexity:** O(α(n)) ≈ O(1) per operation (inverse Ackermann)

---

### 2. **GraphProblemsWithDSU.java** ⭐ LEETCODE PROBLEMS
**8 Classic LeetCode problems solved using DSU**

| Problem | LeetCode # | Difficulty | Company |
|---------|-----------|------------|----------|
| Redundant Connection | #684 | Medium | Amazon, Google |
| Graph Valid Tree | #261 | Medium | Facebook, Amazon |
| Number of Provinces | #547 | Medium | Amazon, Google |
| Number of Islands | #200 | Medium | Amazon, Facebook |
| Accounts Merge | #721 | Medium | Google, Facebook |
| Most Stones Removed | #947 | Medium | Google |
| Satisfiability of Equations | #990 | Medium | Google |
| Smallest String with Swaps | #1202 | Medium | Amazon |

**Pattern:** All use DSU to group related elements into components

---

### 3. **KruskalMST.java** ⭐ MINIMUM SPANNING TREE
**Kruskal's algorithm using DSU**

**Problems Covered:**
1. **Basic Kruskal's MST** - Classic algorithm
2. **Min Cost to Connect All Points** (#1584) - Medium
3. **Connecting Cities with Min Cost** (#1135) - Medium (Premium)
4. **Critical and Pseudo-Critical Edges** (#1489) - Hard

**Algorithm:**
```
1. Sort all edges by weight
2. For each edge (u, v, weight):
   - If find(u) != find(v):
     - Add edge to MST
     - union(u, v)
3. Stop when MST has V-1 edges
```

**Time:** O(E log E) for sorting + O(E α(V)) for DSU ≈ **O(E log E)**

---

## 🎓 When to Use DSU?

✅ **USE DSU when you need:**
- Dynamic connectivity queries
- Cycle detection in UNDIRECTED graphs
- Connected components counting
- Minimum Spanning Tree (Kruskal's)
- Grouping elements with transitive relationships

❌ **DON'T use DSU for:**
- Directed graphs (use DFS/BFS for cycle detection)
- Finding actual paths (DSU only tracks connectivity)
- Dynamic edge removal (DSU doesn't support efficient deletion)

---

## 💡 DSU vs DFS/BFS

| Feature | DSU | DFS/BFS |
|---------|-----|---------|
| **Connectivity Query** | O(α(n)) ≈ O(1) | O(V + E) |
| **Add Edge** | O(α(n)) ≈ O(1) | Need to rebuild |
| **Multiple Queries** | ✅ Excellent | ❌ Slow |
| **Find Path** | ❌ No | ✅ Yes |
| **Directed Graphs** | ❌ No | ✅ Yes |
| **Static Graphs** | ⚠️ OK | ✅ Better |
| **Dynamic Graphs** | ✅ Perfect | ❌ Poor |

**Rule of Thumb:** If you need many connectivity queries on a dynamic graph, use DSU!

---

## 🚀 Quick Start

### Run Any File:
```bash
# Navigate to DSU folder
cd GRAPHS/DSU

# Run any implementation
java DisjointSetUnion.java
java GraphProblemsWithDSU.java
java KruskalMST.java
```

### Template Code:
```java
// Initialize DSU for n nodes
DSU dsu = new DSU(n);

// Add edge (u, v)
if (dsu.union(u, v)) {
    // Nodes were in different components (no cycle)
} else {
    // Nodes were already connected (cycle detected!)
}

// Check connectivity
if (dsu.connected(u, v)) {
    // Path exists between u and v
}

// Get number of components
int components = dsu.getComponents();
```

---

## 📊 Complexity Summary

| Operation | Without Optimization | With Path Compression | With Both Optimizations |
|-----------|---------------------|----------------------|------------------------|
| find(x) | O(n) worst | O(log n) | O(α(n)) ≈ O(1) |
| union(x,y) | O(n) worst | O(log n) | O(α(n)) ≈ O(1) |
| connected(x,y) | O(n) worst | O(log n) | O(α(n)) ≈ O(1) |

**α(n) = Inverse Ackermann function (grows EXTREMELY slowly)**
- α(10^80) ≈ 4
- For all practical purposes: α(n) ≤ 5

---

## 🎯 MAANG/FAANG Interview Tips

### Google Favorites:
- Word Search II (Trie + DSU hybrid)
- Accounts Merge (#721)
- Most Stones Removed (#947)

### Amazon Favorites:
- Number of Islands (#200)
- Min Cost to Connect All Points (#1584)
- Graph Valid Tree (#261)

### Facebook Favorites:
- Redundant Connection (#684)
- Number of Provinces (#547)
- Accounts Merge (#721)

### Common Follow-ups:
1. "Can you do this without DSU?" → Try DFS/BFS
2. "What if graph is directed?" → Use DFS/Tarjan's
3. "What if we remove edges?" → Reverse time or rebuild
4. "Optimize space?" → DSU is already optimal O(n)

---

## 🔑 Key Insights

1. **DSU = Graph Connectivity Tool**
   - Think of union() as "adding an edge"
   - Think of find() as "which component am I in?"

2. **Cycle Detection Pattern:**
   ```java
   for (int[] edge : edges) {
       if (find(edge[0]) == find(edge[1])) {
           // CYCLE DETECTED!
       }
       union(edge[0], edge[1]);
   }
   ```

3. **MST Pattern (Kruskal's):**
   ```java
   sort(edges by weight);
   for (Edge e : edges) {
       if (union(e.src, e.dest)) {
           mst.add(e);
       }
   }
   ```

4. **Grid → Graph Mapping:**
   ```java
   // Map (i, j) to node index
   int node = i * cols + j;
   ```

---

## 📚 Study Order

### Beginner (Learn First):
1. `DisjointSetUnion.java` - Master the implementation
2. Run examples, understand path compression
3. Practice union/find operations

### Intermediate (Build Skills):
4. `GraphProblemsWithDSU.java` - Solve 8 problems
5. Focus on: Redundant Connection, Valid Tree, Provinces
6. Understand cycle detection pattern

### Advanced (Master Level):
7. `KruskalMST.java` - MST algorithm
8. Min Cost to Connect Points
9. Critical Edges problem (HARD)

---

## ✅ Practice Checklist

**Must-Do (Interview Critical):**
- [ ] Implement DSU from scratch
- [ ] Solve Graph Valid Tree (#261)
- [ ] Solve Number of Provinces (#547)
- [ ] Solve Redundant Connection (#684)
- [ ] Understand Kruskal's algorithm

**Should-Do (Build Mastery):**
- [ ] Solve Accounts Merge (#721)
- [ ] Solve Min Cost Connect Points (#1584)
- [ ] Solve Number of Islands with DSU (#200)
- [ ] Implement both path compression techniques

**Nice-to-Have (Advanced):**
- [ ] Critical Edges in MST (#1489)
- [ ] Most Stones Removed (#947)
- [ ] Smallest String with Swaps (#1202)

---

## 🎉 You're Ready When...

✅ You can implement DSU from memory  
✅ You know WHEN to use DSU vs DFS/BFS  
✅ You can detect cycles using DSU  
✅ You understand path compression intuitively  
✅ You can explain Kruskal's algorithm  
✅ You've solved at least 5 DSU problems  

---

**Good luck with your MAANG/FAANG interviews! 🚀**

*Remember: DSU is a GRAPH algorithm - keep it in the GRAPHS folder!*
