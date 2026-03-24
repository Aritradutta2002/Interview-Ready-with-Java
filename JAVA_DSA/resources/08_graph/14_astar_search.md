# A* Search Algorithm - Complete Guide

## Prerequisites & Related Topics

- **Prerequisite**: [04_dijkstra.md](04_dijkstra.md) (A* = Dijkstra + heuristic)
- **Key Concept**: A* is optimal when heuristic is **admissible** (never overestimates true cost)
- **No Heuristic (h=0)**: A* becomes Dijkstra's algorithm
- **Inadmissible Heuristic**: A* runs faster but may not find optimal solution
- **Comparison**: [02_bfs.md](02_bfs.md) (unweighted, unguided), [04_dijkstra.md](04_dijkstra.md) (weighted, unguided), A* (weighted, heuristic-guided)

## Table of Contents
1. [What is A*?](#what-is-a)
2. [Algorithm](#algorithm)
3. [Java Implementation](#java-implementation)
4. [Heuristic Functions](#heuristic-functions)
5. [Comparison](#comparison)
6. [Practice Problems](#practice-problems)

---

## What is A*?

```
┌─────────────────────────────────────────────────────────────────┐
│                       A* SEARCH                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  BEST KNOWN PATHFINDING ALGORITHM!                              │
│                                                                  │
│  f(n) = g(n) + h(n)                                            │
│                                                                  │
│  WHERE:                                                         │
│  - g(n): Cost from START to current node                       │
│  - h(n): Estimated cost from current to GOAL (heuristic)      │
│  - f(n): Estimated total cost of path through current node      │
│                                                                  │
│  PROPERTIES:                                                    │
│  ✅ Optimal if h(n) is ADMISSIBLE (never overestimates)       │
│  ✅ Consistent/Monotonic if h(n) ≤ cost(n→neighbor) + h(neighbor)│
│  ✅ Combines Dijkstra's with heuristic guidance                  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Algorithm

### The A* Algorithm

```java
/*
 * A* ALGORITHM:
 *
 * 1. Initialize open set with start node
 * 2. While open set not empty:
 *    a. Get node with lowest f(n) = g(n) + h(n)
 *    b. If goal reached, return path
 *    c. For each neighbor:
 *       - Calculate tentative g(n)
 *       - If new path is better, update and add to open set
 * 3. Return failure (no path exists)
 *
 * KEY: Priority queue ordered by f(n) = g(n) + h(n)
 */
```

---

## Java Implementation

### 8-Puzzle Solver with A*

```java
package dsa.graph.astar;

import java.util.*;

public class AStarPuzzle {
    private static final int N = 3;
    private static final int[][] goal = {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 0}
    };

    private static class State {
        int[][] board;
        int zeroRow, zeroCol;
        int g;  // Cost from start
        int h;  // Heuristic to goal
        State parent;

        State(int[][] board, int g, State parent) {
            this.board = new int[N][N];
            for (int i = 0; i < N; i++) {
                this.board[i] = board[i].clone();
            }
            this.g = g;
            this.parent = parent;
            this.h = heuristic(board);
            findZero();
        }

        void findZero() {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (board[i][j] == 0) {
                        zeroRow = i;
                        zeroCol = j;
                        return;
                    }
                }
            }
        }

        int f() { return g + h; }
    }

    // Manhattan distance heuristic (admissible)
    private static int heuristic(int[][] board) {
        int dist = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] != 0) {
                    int targetRow = (board[i][j] - 1) / N;
                    int targetCol = (board[i][j] - 1) % N;
                    dist += Math.abs(i - targetRow) + Math.abs(j - targetCol);
                }
            }
        }
        return dist;
    }

    public static void solve(int[][] start) {
        PriorityQueue<State> open = new PriorityQueue<>(
            Comparator.comparingInt(State::f)
        );

        Set<String> closed = new HashSet<>();
        open.offer(new State(start, 0, null));

        while (!open.isEmpty()) {
            State current = open.poll();
            String key = boardKey(current.board);

            if (closed.contains(key)) continue;
            closed.add(key);

            if (current.h == 0) {
                printSolution(current);
                return;
            }

            // Generate neighbors (swap zero with 4 directions)
            int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : dirs) {
                int nr = current.zeroRow + dir[0];
                int nc = current.zeroCol + dir[1];

                if (nr >= 0 && nr < N && nc >= 0 && nc < N) {
                    int[][] newBoard = new int[N][N];
                    for (int i = 0; i < N; i++) {
                        newBoard[i] = current.board[i].clone();
                    }

                    // Swap
                    newBoard[current.zeroRow][current.zeroCol] = newBoard[nr][nc];
                    newBoard[nr][nc] = 0;

                    String newKey = boardKey(newBoard);
                    if (!closed.contains(newKey)) {
                        open.offer(new State(newBoard, current.g + 1, current));
                    }
                }
            }
        }
        System.out.println("No solution found");
    }

    private static String boardKey(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sb.append(board[i][j]);
            }
        }
        return sb.toString();
    }

    private static void printSolution(State goal) {
        List<int[][]> path = new ArrayList<>();
        while (goal != null) {
            path.add(goal.board);
            goal = goal.parent;
        }
        Collections.reverse(path);

        System.out.println("Solution found in " + (path.size() - 1) + " moves:");
        for (int i = 0; i < path.size(); i++) {
            System.out.println("Step " + i + ":");
            for (int[] row : path.get(i)) {
                System.out.println(Arrays.toString(row));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[][] start = {
            {1, 2, 3},
            {4, 0, 6},
            {7, 5, 8}
        };
        solve(start);
    }
}
```

### Grid Pathfinding with A*

```java
package dsa.graph.astar;

import java.util.*;

public class AStarGrid {
    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    private static class Node {
        int row, col;
        int g;  // Cost from start
        int h;  // Heuristic to goal
        Node parent;

        Node(int row, int col, int g, int h, Node parent) {
            this.row = row;
            this.col = col;
            this.g = g;
            this.h = h;
            this.parent = parent;
        }

        int f() { return g + h; }
    }

    public static List<int[]> findPath(int[][] grid, int[] start, int[] goal) {
        int m = grid.length, n = grid[0].length;
        boolean[][] visited = new boolean[m][n];

        PriorityQueue<Node> open = new PriorityQueue<>(
            Comparator.comparingInt(Node::f)
        );

        open.offer(new Node(start[0], start[1], 0, manhattan(start, goal), null));

        while (!open.isEmpty()) {
            Node current = open.poll();

            if (current.row == goal[0] && current.col == goal[1]) {
                return reconstructPath(current);
            }

            if (visited[current.row][current.col]) continue;
            visited[current.row][current.col] = true;

            for (int[] dir : DIRS) {
                int nr = current.row + dir[0];
                int nc = current.col + dir[1];

                if (nr >= 0 && nr < m && nc >= 0 && nc < n
                    && grid[nr][nc] != 1 && !visited[nr][nc]) {
                    int g = current.g + 1;
                    int h = manhattan(new int[]{nr, nc}, goal);
                    open.offer(new Node(nr, nc, g, h, current));
                }
            }
        }
        return Collections.emptyList();  // No path
    }

    private static int manhattan(int[] a, int[] b) {
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
    }

    private static List<int[]> reconstructPath(Node node) {
        List<int[]> path = new ArrayList<>();
        while (node != null) {
            path.add(new int[]{node.row, node.col});
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        int[][] grid = {
            {0, 0, 0, 0},
            {1, 1, 0, 1},
            {0, 0, 0, 0},
            {0, 1, 1, 0}
        };
        int[] start = {0, 0};
        int[] goal = {2, 3};

        List<int[]> path = findPath(grid, start, goal);
        System.out.println("Path: " + path);
    }
}
```

---

## Heuristic Functions

### Common Heuristics

```
┌─────────────────────────────────────────────────────────────────┐
│                      HEURISTIC FUNCTIONS                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  MANHATTAN DISTANCE (Grid):                                     │
│  h(n) = |x1 - x2| + |y1 - y2|                                   │
│  - Admissible: Never overestimates                              │
│  - Used for 4-directional movement                              │
│                                                                  │
│  EUCLIDEAN DISTANCE:                                            │
│  h(n) = √((x1-x2)² + (y1-y2)²)                                 │
│  - Admissible: True straight-line distance                       │
│  - Used for 8-directional movement                              │
│                                                                  │
│  CHEBYSHEV DISTANCE:                                            │
│  h(n) = max(|x1-x2|, |y1-y2|)                                   │
│  - Admissible: Diagonal movement cost = 1                        │
│                                                                  │
│  TILES OUT OF PLACE (8-puzzle):                                │
│  h(n) = Count of tiles not in correct position                   │
│  - Admissible: Each must move at least once                      │
│                                                                  │
│  MANHATTAN + LINEAR CONFLICT (15-puzzle):                       │
│  h(n) = Manhattan + 2 × linearConflicts                         │
│  - More accurate but slower                                      │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Comparison

| Algorithm | Time | Space | Optimal | Notes |
|-----------|------|-------|---------|-------|
| **BFS** | O(V+E) | O(V) | Yes (unweighted) | Explores level by level |
| **Dijkstra** | O((V+E) log V) | O(V) | Yes | Uniform cost search |
| **Greedy Best-First** | O(V) | O(V) | No | Uses only h(n) |
| **A*** | O(E) | O(V) | Yes (admissible h) | Best of both worlds |

---

## Practice Problems

### Easy
1. **Minimum Knight Moves** (LeetCode 1197)
2. **Shortest Path in Binary Matrix** (LeetCode 1293)

### Medium
1. **Shortest Path with Alternating Colors** (LeetCode 1129)
2. **Path With Minimum Effort** (LeetCode 1631)

### Hard
1. **Sliding Puzzle** (LeetCode 773)
2. **Find Shortest Path Visiting All Nodes** (LeetCode 847)

---

## Summary

```
┌─────────────────────────────────────────────────────────────────┐
│                         A* SEARCH                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  f(n) = g(n) + h(n)                                             │
│  g(n): Cost from start to n                                     │
│  h(n): Estimated cost from n to goal (heuristic)                │
│                                                                  │
│  ✅ OPTIMAL if h(n) is ADMISSIBLE (never overestimates)          │
│  ✅ CONSISTENT if h(n) ≤ cost(n→neighbor) + h(neighbor)         │
│  ✅ Faster than Dijkstra (uses heuristic to guide search)        │
│                                                                  │
│  🔑 CHOOSE HEURISTIC WISELY:                                    │
│  - Manhattan for grid movement                                   │
│  - Euclidean for geometric distance                              │
│  - Tiles out of place for sliding puzzles                        │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

> **Remember**: A* is Dijkstra enhanced with a heuristic! If your heuristic is admissible (never overestimates), A* finds the optimal path. The heuristic guides the search, making it much faster than blind search!
