# 🧠 Memoization — Top-Down Dynamic Programming

> *"Don't solve what you've already solved."*

---

## 📌 Table of Contents
1. [What is Memoization?](#1-what-is-memoization)
2. [How Memoization Works](#2-how-memoization-works)
3. [Universal Template](#3-universal-template)
4. [Array vs HashMap Cache](#4-array-vs-hashmap-cache)
5. [1D Memoization Examples](#5-1d-memoization-examples)
6. [2D Memoization Examples](#6-2d-memoization-examples)
7. [3D+ Memoization Examples](#7-3d-memoization-examples)
8. [Converting Recursion to Memoization](#8-converting-recursion-to-memoization)
9. [Common Pitfalls](#9-common-pitfalls)
10. [When to Prefer Memoization](#10-when-to-prefer-memoization)

---

## 1. What is Memoization?

**Memoization** = **Recursion** + **Caching**

It's the **top-down** approach to DP where you:
1. Start from the **original problem**
2. Recursively break it into subproblems
3. **Cache** (store) results of subproblems
4. **Return cached result** if the subproblem was already solved

```
RECURSION:  Solve subproblems → may repeat work
MEMOIZATION: Solve subproblems → cache results → never repeat work
```

### Why "Top-Down"?
```
Top-Down: Start from dp[n] → recurse down to dp[0]
          (Start from the PROBLEM, go to BASE CASES)

Bottom-Up: Start from dp[0] → iterate up to dp[n]
           (Start from BASE CASES, go to PROBLEM)
```

---

## 2. How Memoization Works

### Step-by-Step Process

```
1. Call solve(n)
2. Is n a base case? → Return base value
3. Is memo[n] already computed? → Return memo[n]
4. Compute result by calling solve(smaller subproblems)
5. Store result: memo[n] = result
6. Return result
```

### Visual: Fibonacci with Memoization

```
Call fib(5):
  fib(5) → not cached → compute
    fib(4) → not cached → compute
      fib(3) → not cached → compute
        fib(2) → not cached → compute
          fib(1) → base case → return 1
          fib(0) → base case → return 0
        cache[2] = 1 → return 1
        fib(1) → base case → return 1
      cache[3] = 2 → return 2
      fib(2) → CACHED! → return 1  ← 🔥 No recomputation!
    cache[4] = 3 → return 3
    fib(3) → CACHED! → return 2    ← 🔥 No recomputation!
  cache[5] = 5 → return 5

Total calls: 9 (instead of 15 without memoization)
```

---

## 3. Universal Template

### Template 1: Array-based Cache (when states are integers)

```java
class Solution {
    int[] memo;
    
    public int solve(int n) {
        memo = new int[n + 1];
        Arrays.fill(memo, -1);
        return dp(n);
    }
    
    private int dp(int state) {
        // Step 1: Base case
        if (state <= 0) return BASE_VALUE;
        
        // Step 2: Check cache
        if (memo[state] != -1) return memo[state];
        
        // Step 3: Compute (make choices)
        int result = Integer.MAX_VALUE; // or MIN_VALUE for maximization
        for (/* each choice */) {
            result = Math.min(result, dp(nextState) + cost);
        }
        
        // Step 4: Cache and return
        return memo[state] = result;
    }
}
```

### Template 2: 2D Array Cache

```java
class Solution {
    int[][] memo;
    
    public int solve(int[] arr1, int[] arr2) {
        int m = arr1.length, n = arr2.length;
        memo = new int[m + 1][n + 1];
        for (int[] row : memo) Arrays.fill(row, -1);
        return dp(0, 0, arr1, arr2);
    }
    
    private int dp(int i, int j, int[] arr1, int[] arr2) {
        // Base case
        if (i == arr1.length || j == arr2.length) return 0;
        
        // Check cache
        if (memo[i][j] != -1) return memo[i][j];
        
        // Compute
        int result;
        if (arr1[i] == arr2[j]) {
            result = 1 + dp(i + 1, j + 1, arr1, arr2);
        } else {
            result = Math.max(dp(i + 1, j, arr1, arr2), 
                              dp(i, j + 1, arr1, arr2));
        }
        
        // Cache and return
        return memo[i][j] = result;
    }
}
```

### Template 3: HashMap Cache (complex/non-integer states)

```java
class Solution {
    Map<String, Integer> memo = new HashMap<>();
    
    private int dp(int i, int j, int k) {
        // Base case
        if (i < 0) return 0;
        
        // Create key
        String key = i + "," + j + "," + k;
        
        // Check cache
        if (memo.containsKey(key)) return memo.get(key);
        
        // Compute
        int result = /* ... */;
        
        // Cache and return
        memo.put(key, result);
        return result;
    }
}
```

---

## 4. Array vs HashMap Cache

### Array Cache
```java
int[] memo = new int[n + 1];
Arrays.fill(memo, -1);

// Access: O(1)
// Memory: Contiguous, cache-friendly
// Best when: States are dense integers (0 to n)
```

### HashMap Cache
```java
Map<String, Integer> memo = new HashMap<>();

// Access: O(1) amortized, but slower constant
// Memory: More overhead per entry
// Best when: States are sparse, complex, or multi-dimensional
```

### Comparison

| Aspect | Array | HashMap |
|--------|-------|---------|
| **Speed** | ⚡ Faster (direct access) | 🐢 Slower (hashing overhead) |
| **Memory** | Fixed allocation | Dynamic allocation |
| **Sparse states** | Wastes memory | ✅ Efficient |
| **Dense states** | ✅ Efficient | Wastes memory |
| **Multi-dim** | `int[][]`, `int[][][]` | `Map<String, Integer>` |
| **Negative indices** | ❌ Need offset | ✅ Works naturally |
| **Competitive prog** | ✅ Preferred | Use when necessary |

### 💡 Pro Tips

```java
// Tip 1: Use Long for 2D state encoding (faster than HashMap)
long key = (long) i * MAX_J + j;
Map<Long, Integer> memo = new HashMap<>();

// Tip 2: For boolean states, use -1/0/1 instead of -1/true/false
// -1 = not computed, 0 = false, 1 = true

// Tip 3: For "count" problems where 0 is valid answer, 
// use Integer.MIN_VALUE or null as "not computed"
Integer[] memo = new Integer[n + 1]; // null = not computed
if (memo[state] != null) return memo[state];
```

---

## 5. 1D Memoization Examples

### Example 1: Climbing Stairs
> You can climb 1 or 2 steps. How many ways to reach step n?

```java
class ClimbingStairs {
    int[] memo;
    
    public int climbStairs(int n) {
        memo = new int[n + 1];
        Arrays.fill(memo, -1);
        return dp(n);
    }
    
    private int dp(int n) {
        if (n <= 1) return 1;           // Base: 1 way to reach step 0 or 1
        if (memo[n] != -1) return memo[n];
        return memo[n] = dp(n - 1) + dp(n - 2);  // Take 1 step or 2 steps
    }
}
// Time: O(n), Space: O(n)
```

### Example 2: House Robber
> Rob houses (can't rob adjacent). Maximize money.

```java
class HouseRobber {
    int[] memo;
    
    public int rob(int[] nums) {
        memo = new int[nums.length];
        Arrays.fill(memo, -1);
        return dp(nums, 0);
    }
    
    private int dp(int[] nums, int i) {
        if (i >= nums.length) return 0;     // No more houses
        if (memo[i] != -1) return memo[i];
        
        // Choice: Rob this house (skip next) OR skip this house
        int rob = nums[i] + dp(nums, i + 2);   // Rob current, skip next
        int skip = dp(nums, i + 1);             // Skip current
        
        return memo[i] = Math.max(rob, skip);
    }
}
// Time: O(n), Space: O(n)
```

### Example 3: Min Cost Climbing Stairs
> Each step has a cost. Find minimum cost to reach the top.

```java
class MinCostClimbingStairs {
    int[] memo;
    
    public int minCostClimbingStairs(int[] cost) {
        int n = cost.length;
        memo = new int[n + 1];
        Arrays.fill(memo, -1);
        return dp(cost, n);
    }
    
    private int dp(int[] cost, int i) {
        if (i <= 1) return 0;  // Can start from step 0 or 1 for free
        if (memo[i] != -1) return memo[i];
        
        int fromOne = dp(cost, i - 1) + cost[i - 1];  // Come from 1 step below
        int fromTwo = dp(cost, i - 2) + cost[i - 2];  // Come from 2 steps below
        
        return memo[i] = Math.min(fromOne, fromTwo);
    }
}
// Time: O(n), Space: O(n)
```

### Example 4: Decode Ways
> "226" can be decoded as "BZ"(2,26), "VF"(22,6), "BBF"(2,2,6). Count ways.

```java
class DecodeWays {
    int[] memo;
    
    public int numDecodings(String s) {
        memo = new int[s.length()];
        Arrays.fill(memo, -1);
        return dp(s, 0);
    }
    
    private int dp(String s, int i) {
        if (i == s.length()) return 1;       // Successfully decoded entire string
        if (s.charAt(i) == '0') return 0;    // Can't decode leading zero
        if (memo[i] != -1) return memo[i];
        
        // Take 1 digit
        int ways = dp(s, i + 1);
        
        // Take 2 digits (if valid: 10-26)
        if (i + 1 < s.length()) {
            int twoDigit = Integer.parseInt(s.substring(i, i + 2));
            if (twoDigit >= 10 && twoDigit <= 26) {
                ways += dp(s, i + 2);
            }
        }
        
        return memo[i] = ways;
    }
}
// Time: O(n), Space: O(n)
```

---

## 6. 2D Memoization Examples

### Example 1: 0/1 Knapsack

```java
class Knapsack {
    int[][] memo;
    
    public int knapsack(int[] wt, int[] val, int W) {
        int n = wt.length;
        memo = new int[n][W + 1];
        for (int[] row : memo) Arrays.fill(row, -1);
        return dp(wt, val, n - 1, W);
    }
    
    private int dp(int[] wt, int[] val, int i, int w) {
        // Base case: no items left or no capacity
        if (i < 0 || w == 0) return 0;
        if (memo[i][w] != -1) return memo[i][w];
        
        // Choice 1: Skip item i
        int skip = dp(wt, val, i - 1, w);
        
        // Choice 2: Take item i (if it fits)
        int take = 0;
        if (wt[i] <= w) {
            take = val[i] + dp(wt, val, i - 1, w - wt[i]);
        }
        
        return memo[i][w] = Math.max(skip, take);
    }
}
// Time: O(n × W), Space: O(n × W)
```

### Example 2: Longest Common Subsequence (LCS)

```java
class LCS {
    int[][] memo;
    
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        memo = new int[m][n];
        for (int[] row : memo) Arrays.fill(row, -1);
        return dp(text1, text2, 0, 0);
    }
    
    private int dp(String s1, String s2, int i, int j) {
        if (i == s1.length() || j == s2.length()) return 0;
        if (memo[i][j] != -1) return memo[i][j];
        
        if (s1.charAt(i) == s2.charAt(j)) {
            return memo[i][j] = 1 + dp(s1, s2, i + 1, j + 1);
        }
        
        return memo[i][j] = Math.max(
            dp(s1, s2, i + 1, j),   // Skip char from s1
            dp(s1, s2, i, j + 1)    // Skip char from s2
        );
    }
}
// Time: O(m × n), Space: O(m × n)
```

### Example 3: Unique Paths with Obstacles

```java
class UniquePathsII {
    int[][] memo;
    
    public int uniquePathsWithObstacles(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        memo = new int[m][n];
        for (int[] row : memo) Arrays.fill(row, -1);
        return dp(grid, 0, 0);
    }
    
    private int dp(int[][] grid, int i, int j) {
        // Out of bounds or obstacle
        if (i >= grid.length || j >= grid[0].length || grid[i][j] == 1) return 0;
        // Reached destination
        if (i == grid.length - 1 && j == grid[0].length - 1) return 1;
        if (memo[i][j] != -1) return memo[i][j];
        
        return memo[i][j] = dp(grid, i + 1, j) + dp(grid, i, j + 1);
    }
}
// Time: O(m × n), Space: O(m × n)
```

### Example 4: Edit Distance

```java
class EditDistance {
    int[][] memo;
    
    public int minDistance(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        memo = new int[m + 1][n + 1];
        for (int[] row : memo) Arrays.fill(row, -1);
        return dp(word1, word2, m, n);
    }
    
    private int dp(String s1, String s2, int i, int j) {
        // Base cases
        if (i == 0) return j;  // Insert remaining chars of s2
        if (j == 0) return i;  // Delete remaining chars of s1
        if (memo[i][j] != -1) return memo[i][j];
        
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            return memo[i][j] = dp(s1, s2, i - 1, j - 1); // Match, no cost
        }
        
        int insert = dp(s1, s2, i, j - 1);      // Insert
        int delete = dp(s1, s2, i - 1, j);      // Delete
        int replace = dp(s1, s2, i - 1, j - 1); // Replace
        
        return memo[i][j] = 1 + Math.min(insert, Math.min(delete, replace));
    }
}
// Time: O(m × n), Space: O(m × n)
```

---

## 7. 3D+ Memoization Examples

### Example 1: Cherry Pickup (3D)
> Two people traverse grid simultaneously. Maximize cherries.

```java
class CherryPickup {
    int[][][] memo;
    
    public int cherryPickup(int[][] grid) {
        int n = grid.length;
        memo = new int[n][n][n];
        for (int[][] layer : memo)
            for (int[] row : layer)
                Arrays.fill(row, Integer.MIN_VALUE);
        
        int result = dp(grid, 0, 0, 0);
        return Math.max(0, result);
    }
    
    // Person 1 at (r1, c1), Person 2 at (r2, c2)
    // Since both move same number of steps: c2 = r1 + c1 - r2
    private int dp(int[][] grid, int r1, int c1, int r2) {
        int n = grid.length;
        int c2 = r1 + c1 - r2;
        
        // Out of bounds or thorns
        if (r1 >= n || c1 >= n || r2 >= n || c2 >= n) return -1;
        if (grid[r1][c1] == -1 || grid[r2][c2] == -1) return -1;
        
        // Both reached end
        if (r1 == n - 1 && c1 == n - 1) return grid[r1][c1];
        
        if (memo[r1][c1][r2] != Integer.MIN_VALUE) return memo[r1][c1][r2];
        
        // Cherries collected
        int cherries = grid[r1][c1];
        if (r1 != r2 || c1 != c2) cherries += grid[r2][c2]; // Don't double count
        
        // Both move: 4 combinations (down/right for each)
        int best = Math.max(
            Math.max(dp(grid, r1 + 1, c1, r2 + 1), dp(grid, r1 + 1, c1, r2)),
            Math.max(dp(grid, r1, c1 + 1, r2 + 1), dp(grid, r1, c1 + 1, r2))
        );
        
        if (best < 0) {
            memo[r1][c1][r2] = -1;
        } else {
            memo[r1][c1][r2] = cherries + best;
        }
        return memo[r1][c1][r2];
    }
}
// Time: O(n³), Space: O(n³)
```

### Example 2: Partition to K Equal Sum Subsets (Bitmask Memo)

```java
class PartitionKSubsets {
    Map<Integer, Boolean> memo = new HashMap<>();
    
    public boolean canPartitionKSubsets(int[] nums, int k) {
        int totalSum = Arrays.stream(nums).sum();
        if (totalSum % k != 0) return false;
        int target = totalSum / k;
        return dp(nums, 0, k, 0, target);
    }
    
    private boolean dp(int[] nums, int mask, int k, int currentSum, int target) {
        if (k == 0) return mask == (1 << nums.length) - 1; // All used
        if (currentSum == target) return dp(nums, mask, k - 1, 0, target); // Start new group
        if (memo.containsKey(mask)) return memo.get(mask);
        
        for (int i = 0; i < nums.length; i++) {
            if ((mask & (1 << i)) != 0) continue; // Already used
            if (currentSum + nums[i] > target) continue; // Exceeds target
            
            if (dp(nums, mask | (1 << i), k, currentSum + nums[i], target)) {
                memo.put(mask, true);
                return true;
            }
        }
        
        memo.put(mask, false);
        return false;
    }
}
// Time: O(n × 2^n), Space: O(2^n)
```

---

## 8. Converting Recursion to Memoization

### The 3-Step Conversion

```
Step 1: Write pure recursive solution (brute force)
Step 2: Identify changing parameters → these are your STATES
Step 3: Add cache for those states
```

### Example: Coin Change

**Step 1: Pure Recursion**
```java
// Brute force — exponential time
int coinChange(int[] coins, int amount) {
    if (amount == 0) return 0;
    if (amount < 0) return -1;
    
    int minCoins = Integer.MAX_VALUE;
    for (int coin : coins) {
        int sub = coinChange(coins, amount - coin);
        if (sub != -1) {
            minCoins = Math.min(minCoins, sub + 1);
        }
    }
    return minCoins == Integer.MAX_VALUE ? -1 : minCoins;
}
```

**Step 2: Identify States**
```
What changes between calls? → Only `amount` changes!
coins array is constant → NOT a state
State: dp[amount]
```

**Step 3: Add Memoization**
```java
int[] memo;

int coinChange(int[] coins, int amount) {
    memo = new int[amount + 1];
    Arrays.fill(memo, -2);  // -2 = not computed (-1 is valid answer)
    return dp(coins, amount);
}

int dp(int[] coins, int amount) {
    if (amount == 0) return 0;
    if (amount < 0) return -1;
    if (memo[amount] != -2) return memo[amount];  // ← ADDED
    
    int minCoins = Integer.MAX_VALUE;
    for (int coin : coins) {
        int sub = dp(coins, amount - coin);
        if (sub != -1) {
            minCoins = Math.min(minCoins, sub + 1);
        }
    }
    return memo[amount] = (minCoins == Integer.MAX_VALUE ? -1 : minCoins);  // ← ADDED
}
```

### Conversion Checklist
```
✅ Identify ALL changing parameters
✅ Create memo array/map with dimensions = number of changing params
✅ Choose sentinel value (-1, Integer.MIN_VALUE, null) for "not computed"
✅ Add cache check AFTER base case, BEFORE computation
✅ Store result BEFORE returning
✅ Verify sentinel doesn't conflict with valid answers
```

---

## 9. Common Pitfalls

### Pitfall 1: Sentinel Value Conflicts
```java
// ❌ WRONG: Using -1 as "not computed" when -1 is a valid answer
Arrays.fill(memo, -1);
// coinChange returns -1 for "impossible" — conflicts!

// ✅ CORRECT: Use a value that can't be a valid answer
Arrays.fill(memo, -2);        // or
Arrays.fill(memo, Integer.MIN_VALUE);  // or
Integer[] memo;  // null = not computed
```

### Pitfall 2: Forgetting to Initialize Cache
```java
// ❌ WRONG: memo is all zeros by default — 0 might be valid!
int[] memo = new int[n + 1];

// ✅ CORRECT
int[] memo = new int[n + 1];
Arrays.fill(memo, -1);
```

### Pitfall 3: Stack Overflow on Deep Recursion
```java
// ❌ For n = 10^5, recursion depth = 10^5 → StackOverflowError!
int dp(int n) {
    if (n <= 1) return n;
    if (memo[n] != -1) return memo[n];
    return memo[n] = dp(n-1) + dp(n-2);
}

// ✅ Solutions:
// 1. Increase stack size: java -Xss64m
// 2. Convert to bottom-up (tabulation)
// 3. Use iterative deepening
```

### Pitfall 4: Modifying Global State
```java
// ❌ WRONG: Modifying array during recursion without backtracking
boolean dp(int[] arr, int i, int target) {
    arr[i] = 0;  // Modifying input!
    boolean result = dp(arr, i + 1, target);
    // Forgot to restore arr[i]!
    return result;
}

// ✅ CORRECT: Either don't modify, or backtrack
boolean dp(int[] arr, int i, int target) {
    int original = arr[i];
    arr[i] = 0;
    boolean result = dp(arr, i + 1, target);
    arr[i] = original;  // Restore!
    return result;
}
```

### Pitfall 5: Wrong Dimensions
```java
// ❌ WRONG: State is (i, j) but memo is 1D
int[] memo = new int[n];
int dp(int i, int j) {
    if (memo[i] != -1) return memo[i]; // j is ignored!
}

// ✅ CORRECT: Memo dimensions must match state dimensions
int[][] memo = new int[n][m];
int dp(int i, int j) {
    if (memo[i][j] != -1) return memo[i][j];
}
```

---

## 10. When to Prefer Memoization

### ✅ Use Memoization When:

```
1. State transitions are COMPLEX or HARD to determine iteration order
   → Memoization handles order automatically via recursion

2. Not all states are needed (SPARSE state space)
   → Memoization only computes what's needed (lazy evaluation)

3. Problem is naturally RECURSIVE (trees, graphs, backtracking)
   → Memoization maps directly to recursive thinking

4. You need to QUICKLY prototype a solution
   → Add memo to brute force = instant DP

5. Multiple base cases or complex boundary conditions
   → Easier to handle in recursive structure
```

### ❌ Avoid Memoization When:

```
1. Deep recursion (n > 10^4) → Stack overflow risk
   → Use tabulation instead

2. Need SPACE OPTIMIZATION → Rolling array only works with tabulation

3. ALL states will be computed anyway → Tabulation avoids recursion overhead

4. Performance-critical competitive programming
   → Tabulation is slightly faster (no function call overhead)
```

### Decision Flowchart
```
Is the recursion depth > 10^4?
├── YES → Use TABULATION
└── NO
    ├── Is the state space sparse?
    │   ├── YES → Use MEMOIZATION ✅
    │   └── NO
    │       ├── Need space optimization?
    │       │   ├── YES → Use TABULATION
    │       │   └── NO → Either works, MEMOIZATION is easier to write
    └── Is the iteration order hard to determine?
        ├── YES → Use MEMOIZATION ✅
        └── NO → Use TABULATION (slightly faster)
```

---

## 🔥 Quick Reference: Memoization Patterns

```java
// Pattern 1: Minimize something
return memo[state] = Math.min(choice1, choice2);

// Pattern 2: Maximize something
return memo[state] = Math.max(choice1, choice2);

// Pattern 3: Count ways
return memo[state] = way1 + way2;

// Pattern 4: Boolean feasibility
return memo[state] = (choice1 || choice2) ? 1 : 0;

// Pattern 5: With modular arithmetic
return memo[state] = (way1 + way2) % MOD;
```

---

## 📚 Next Steps

| Topic | File |
|-------|------|
| Bottom-Up Approach | [TABULATION.MD](TABULATION.MD) |
| Knapsack Problems | [KNAPSACK.MD](KNAPSACK.MD) |
| All DP Patterns | [PATTERNS_AND_TRICKS.MD](PATTERNS_AND_TRICKS.MD) |
