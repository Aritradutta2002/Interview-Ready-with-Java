# Union-Find (Disjoint Set Union) - Complete Guide

## Table of Contents
1. [What is Union-Find?](#what-is-union-find)
2. [Why Union-Find?](#why-union-find)
3. [Data Structure Design](#data-structure-design)
4. [Java Implementation](#java-implementation)
5. [Path Compression](#path-compression)
6. [Union by Rank](#union-by-rank)
7. [Applications](#applications)
8. [Practice Problems](#practice-problems)

---

## What is Union-Find?

**Union-Find** (also called **Disjoint Set Union** or **DSU**) is a data structure that tracks a set of elements partitioned into **disjoint (non-overlapping) sets**. It supports two main operations:

1. **Find**: Determine which set a particular element belongs to
2. **Union**: Merge two sets into a single set

### Key Characteristics

```
┌─────────────────────────────────────────────────────────────────┐
│                      UNION-FIND                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ✅ FINDS: Which set an element belongs to                     │
│  ✅ UNIONS: Merges two sets together                           │
│  ✅ DETECTS: Cycles in graphs (via Union-Find)                  │
│  ✅ TIME:  Near O(1) amortized with optimizations            │
│  ✅ SPACE: O(N)                                                │
│                                                                  │
│  🔧 ALSO KNOWN AS: Disjoint Set Union (DSU)                   │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Visual Example

```
INITIAL STATE (5 elements, each in its own set):
  0    1    2    3    4
  ○    ○    ○    ○    ○
  |    |    |    |    |
 parent:0  1    2    3    4

AFTER Union(0, 1):
  0────1    2    3    4
  ○────○    ○    ○    ○
  |
 parent:0   1    2    3    4

AFTER Union(2, 3):
  0────1    2────3    4
  ○────○    ○────○    ○

AFTER Union(0, 3):
  0────1    2────3    4
  │    │
  └────┴────────○────○   (0,1,2,3 all connected)
```

---

## Why Union-Find?

### Problems Union-Find Solves

```java
/*
 * 1. CONNECTIVITY PROBLEMS
 *    - Are two elements connected?
 *    - Number of connected components
 *    - Graph cycle detection
 *
 * 2. KRUSKAL'S MINIMUM SPANNING TREE
 *    - Greedily add edges that don't create cycles
 *    - Use Union-Find to check if vertices are already connected
 *
 * 3. LEETCODE PROBLEMS
 *    - Number of Islands II
 *    - Graph Valid Tree
 *    - Friend Circles
 *    - Redundant Connection
 */
```

### Why Not BFS/DFS for Connectivity?

```java
/*
 * BFS/DFS for connectivity:
 * - O(V + E) per query
 * - Would need to rebuild traversal for each query
 *
 * Union-Find:
 * - O(α(N)) per operation (nearly constant!)
 * - Preprocess once, query instantly
 *
 * Example:动态 connectivity queries
 * - "Is 0 connected to 3?" - Union-Find: O(1)
 * - "How many connected components?" - Union-Find: O(1)
 */
```

---

## Data Structure Design

### Basic Structure

```java
/*
 * UNION-FIND DATA STRUCTURE:
 *
 * Each element has:
 * - parent[i]: the parent of element i (or itself if root)
 * - rank[i] or size[i]: to optimize unions
 *
 * FIND OPERATION:
 * - Recursively find the root/representative of the set
 * - Root = element whose parent is itself
 *
 * UNION OPERATION:
 * - Find roots of both elements
 * - If same root, already in same set
 * - If different roots, merge the sets (link one root to other)
 */
```

### Without Optimizations

```java
/*
 * BASIC FIND (without path compression):
 *
 * int find(int x) {
 *     if (parent[x] == x) return x;
 *     return find(parent[x]);  // Recursively find root
 * }
 *
 * Time: O(N) worst case (linked list)
 *
 *
 * BASIC UNION (without rank):
 *
 * void union(int x, int y) {
 *     int rootX = find(x);
 *     int rootY = find(y);
 *     if (rootX != rootY) {
 *         parent[rootY] = rootX;  // Arbitrary link
 *     }
 * }
 *
 * Time: O(N) worst case
 */
```

---

## Java Implementation

### Basic Union-Find

```java
package dsa.graph.unionfind;

public class UnionFindBasic {
    private int[] parent;
    private int[] rank;

    public UnionFindBasic(int size) {
        parent = new int[size];
        rank = new int[size];

        for (int i = 0; i < size; i++) {
            parent[i] = i;  // Each element is its own parent
            rank[i] = 0;     // Initial rank is 0
        }
    }

    // FIND with path compression
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);  // Path compression
        }
        return parent[x];
    }

    // UNION by rank
    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return;  // Already in same set
        }

        // Union by rank: attach smaller rank tree under larger rank tree
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;  // Increase rank when equal
        }
    }

    // Check if two elements are connected
    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    // Count number of distinct sets
    public int countSets() {
        int count = 0;
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] == i) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        UnionFindBasic uf = new UnionFindBasic(5);

        System.out.println("Initial sets: " + uf.countSets());  // 5

        uf.union(0, 1);
        System.out.println("After union(0,1): " + uf.countSets());  // 4
        System.out.println("Connected(0,1): " + uf.connected(0, 1));  // true

        uf.union(2, 3);
        System.out.println("After union(2,3): " + uf.countSets());  // 3

        uf.union(1, 2);
        System.out.println("After union(1,2): " + uf.countSets());  // 2
        System.out.println("Connected(0,3): " + uf.connected(0, 3));  // true

        uf.union(3, 4);
        System.out.println("After union(3,4): " + uf.countSets());  // 1
    }
}
```

### Optimized Union-Find with Size

```java
package dsa.graph.unionfind;

public class UnionFindSize {
    private int[] parent;
    private int[] size;

    public UnionFindSize(int size) {
        this.parent = new int[size];
        this.size = new int[size];

        for (int i = 0; i < size; i++) {
            parent[i] = i;
            this.size[i] = 1;
        }
    }

    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);  // Path compression
        }
        return parent[x];
    }

    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) return;

        // Union by size: attach smaller tree under larger tree
        if (size[rootX] < size[rootY]) {
            parent[rootX] = rootY;
            size[rootY] += size[rootX];
        } else {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        }
    }

    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    public int sizeOf(int x) {
        int root = find(x);
        return size[root];
    }

    public static void main(String[] args) {
        UnionFindSize uf = new UnionFindSize(5);

        uf.union(0, 1);
        uf.union(2, 3);
        System.out.println("Size of set containing 0: " + uf.sizeOf(0));  // 2
        System.out.println("Size of set containing 2: " + uf.sizeOf(2));  // 2
        System.out.println("Size of set containing 4: " + uf.sizeOf(4));  // 1

        uf.union(0, 2);
        System.out.println("After union(0,2), size of set: " + uf.sizeOf(0));  // 4
    }
}
```

---

## Path Compression

### What is Path Compression?

```java
/*
 * PATH COMPRESSION:
 *
 * During find(), compress the path by making all visited
 * nodes point directly to the root.
 *
 * BEFORE (find 1):
 *   1 → 2 → 3 → 4 (root)
 *
 * AFTER find(1):
 *   1 → 3
 *   2 → 3 → 4
 *   3 → 4 (root)
 *
 * All nodes on path now point directly to root!
 */

// With Path Compression
public int find(int x) {
    if (parent[x] != x) {
        parent[x] = find(parent[x]);  // Recursively find and compress
    }
    return parent[x];
}

// WITHOUT Path Compression (slow!)
public int findSlow(int x) {
    if (parent[x] == x) return x;
    return findSlow(parent[x]);  // Just recurse, no compression
}

/*
 * DIFFERENCE:
 *
 * Array: [0, 1, 2, 3, 4] where parent[i] = i initially
 *
 * After Union(0,1), Union(1,2), Union(2,3), Union(3,4):
 *
 * WITHOUT path compression:
 *   parent = [0, 0, 1, 2, 3]
 *   find(4) → 3 → 2 → 1 → 0 = 4 hops!
 *
 * WITH path compression (first find):
 *   find(4) → 3 → 2 → 1 → 0 (compress)
 *   parent = [0, 0, 1, 2, 3] (still same)
 *
 * AFTER second find(4):
 *   find(4) → 0 (now 1 hop!)
 *   parent = [0, 0, 1, 0, 0]
 */
```

---

## Union by Rank

### What is Union by Rank?

```java
/*
 * UNION BY RANK:
 *
 * When merging two sets, attach the smaller rank tree
 * under the root of the larger rank tree.
 *
 * RANK = approximate height of tree (not exact size)
 *
 * WHY RANK INSTEAD OF SIZE?
 * - Rank saves space (just an int, not tracking exact sizes)
 * - Logarithmic height is maintained
 *
 * Example:
 *
 * BEFORE union(2, 5):
 *
 *     2 (rank 2)          5 (rank 1)
 *    /│╲                  │
 *   0 1 3                  7
 *
 * AFTER union (attach smaller under larger):
 *
 *     2 (rank 2)
 *    /│╲
 *   0 1 3
 *   │
 *   5 (rank 1)
 *   │
 *   7
 */

public void union(int x, int y) {
    int rootX = find(x);
    int rootY = find(y);

    if (rootX == rootY) return;

    // Union by rank
    if (rank[rootX] < rank[rootY]) {
        parent[rootX] = rootY;
    } else if (rank[rootX] > rank[rootY]) {
        parent[rootY] = rootX;
    } else {
        parent[rootY] = rootX;
        rank[rootX]++;
    }
}
```

### Why Both Optimizations?

```java
/*
 * PATH COMPRESSION + UNION BY RANK = NEAR O(1)!
 *
 * Time Complexity:
 * - Without optimizations: O(N)
 * - With path compression: O(log N) amortized
 * - With union by rank: O(log N)
 * - With BOTH: O(α(N)) amortized
 *
 * α(N) = Inverse Ackermann Function
 * - For all practical N, α(N) ≤ 4
 * - So effectively O(1)!
 *
 * Example comparison for 1000 operations on N=1,000,000:
 *
 * No optimization:  1,000,000,000 operations (too slow!)
 * Path compression: ~20,000 operations (good)
 * Union by rank:    ~20,000 operations (good)
 * BOTH:             ~10,000 operations (best!)
 */
```

---

## Applications

### 1. Number of Islands II (LeetCode 305)

```java
package dsa.graph.applications;

import java.util.*;

public class NumberOfIslandsII {
    public List<Integer> numIslands2(int m, int n, int[][] positions) {
        List<Integer> result = new ArrayList<>();
        UnionFind uf = new UnionFind(m * n);
        int count = 0;

        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        Set<Integer> visited = new HashSet<>();

        for (int[] pos : positions) {
            int r = pos[0];
            int c = pos[1];
            int idx = r * n + c;

            if (visited.contains(idx)) {
                result.add(count);
                continue;
            }

            visited.add(idx);
            uf.makeSet(idx);
            count++;

            for (int[] dir : dirs) {
                int nr = r + dir[0];
                int nc = c + dir[1];
                int nidx = nr * n + nc;

                if (nr >= 0 && nr < m && nc >= 0 && nc < n && visited.contains(nidx)) {
                    if (uf.find(idx) != uf.find(nidx)) {
                        uf.union(idx, nidx);
                        count--;
                    }
                }
            }
            result.add(count);
        }
        return result;
    }

    private static class UnionFind {
        private int[] parent;
        private int[] rank;

        UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
        }

        void makeSet(int x) {
            parent[x] = x;
            rank[x] = 0;
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return;

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }

    public static void main(String[] args) {
        NumberOfIslandsII solution = new NumberOfIslandsII();
        int m = 3, n = 3;
        int[][] positions = {{0, 0}, {0, 1}, {1, 2}, {2, 1}};

        System.out.println(solution.numIslands2(m, n, positions));
        // Output: [1, 2, 3, 2]
    }
}
```

### 2. Graph Valid Tree (LeetCode 261)

```java
package dsa.graph.applications;

import java.util.*;

public class GraphValidTree {
    public boolean validTree(int n, int[][] edges) {
        if (edges.length != n - 1) {
            return false;  // Not enough edges for a tree
        }

        UnionFind uf = new UnionFind(n);

        for (int[] edge : edges) {
            if (uf.connected(edge[0], edge[1])) {
                return false;  // Cycle detected!
            }
            uf.union(edge[0], edge[1]);
        }

        return uf.countSets() == 1;  // All nodes connected?
    }

    private static class UnionFind {
        private int[] parent;
        private int[] rank;
        private int count;

        UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            count = size;
            for (int i = 0; i < size; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return;

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            count--;
        }

        boolean connected(int x, int y) {
            return find(x) == find(y);
        }

        int countSets() {
            return count;
        }
    }

    public static void main(String[] args) {
        GraphValidTree solution = new GraphValidTree();

        int[][] edges1 = {{0, 1}, {0, 2}, {0, 3}};
        System.out.println("Valid tree: " + solution.validTree(4, edges1));  // true

        int[][] edges2 = {{0, 1}, {1, 2}, {2, 0}};  // Cycle!
        System.out.println("Valid tree: " + solution.validTree(3, edges2));  // false
    }
}
```

### 3. Redundant Connection (LeetCode 684)

```java
package dsa.graph.applications;

public class RedundantConnection {
    public int[] findRedundantConnection(int[][] edges) {
        UnionFind uf = new UnionFind(edges.length + 1);

        for (int[] edge : edges) {
            if (uf.connected(edge[0], edge[1])) {
                return edge;  // This edge creates a cycle!
            }
            uf.union(edge[0], edge[1]);
        }
        return new int[0];  // Should never reach here
    }

    private static class UnionFind {
        private int[] parent;
        private int[] rank;

        UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return;

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }

        boolean connected(int x, int y) {
            return find(x) == find(y);
        }
    }

    public static void main(String[] args) {
        RedundantConnection solution = new RedundantConnection();

        int[][] edges = {{1, 2}, {1, 3}, {2, 3}};
        int[] result = solution.findRedundantConnection(edges);
        System.out.println("Redundant edge: [" + result[0] + ", " + result[1] + "]");
        // Output: [2, 3]
    }
}
```

---

## Practice Problems

### Easy
1. **Number of Islands** (LeetCode 200)
2. **Friend Circles** (LeetCode 547)
3. **Graph Valid Tree** (LeetCode 261)

### Medium
1. **Number of Islands II** (LeetCode 305)
2. **Redundant Connection** (LeetCode 684)
3. **Satisfiability of Equality Equations** (LeetCode 990)

### Hard
1. **Swim in Rising Water** (LeetCode 778)
2. **Minimize Max Distance to Gas Station** (Hard)
3. **Process Tasks Using Servers** (LeetCode 1882)

---

## Summary

```
┌─────────────────────────────────────────────────────────────────┐
│                       UNION-FIND (DSU)                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  🎯 PURPOSE: Track disjoint sets, connectivity                │
│                                                                  │
│  ⏱️ TIME: O(α(N)) amortized ≈ O(1)                            │
│  💾 SPACE: O(N)                                                 │
│                                                                  │
│  📝 OPERATIONS:                                                │
│     - find(x): Find root/representative of set                  │
│     - union(x, y): Merge two sets                              │
│     - connected(x, y): Check if same set                       │
│                                                                  │
│  🔧 OPTIMIZATIONS:                                              │
│     1. Path Compression: Make nodes point to root directly    │
│     2. Union by Rank: Attach smaller tree under larger          │
│                                                                  │
│  ✅ KEY FORMULA:                                                │
│     α(N) = inverse Ackermann function                            │
│     For all practical N, α(N) ≤ 4 (effectively constant!)      │
│                                                                  │
│  📝 COMMON USES:                                                │
│     - Cycle detection in graphs                                │
│     - Kruskal's MST algorithm                                  │
│     - Connectivity problems                                    │
│     - Number of connected components                            │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

> **Remember**: Union-Find is one of the most elegant data structures! With path compression and union by rank, it's effectively O(1) per operation. Perfect for dynamic connectivity problems, cycle detection, and Kruskal's MST!
