# 📊 Tabulation — Bottom-Up Dynamic Programming

> *"Build the answer from the ground up."*

---

## 📌 Table of Contents
1. [What is Tabulation?](#1-what-is-tabulation)
2. [How Tabulation Works](#2-how-tabulation-works)
3. [Universal Template](#3-universal-template)
4. [Converting Memoization to Tabulation](#4-converting-memoization-to-tabulation)
5. [1D Tabulation Examples](#5-1d-tabulation-examples)
6. [2D Tabulation Examples](#6-2d-tabulation-examples)
7. [Space Optimization Techniques](#7-space-optimization-techniques)
8. [Iteration Order — The Critical Detail](#8-iteration-order--the-critical-detail)
9. [Printing the Actual Solution (Backtracking)](#9-printing-the-actual-solution-backtracking)
10. [When to Prefer Tabulation](#10-when-to-prefer-tabulation)

---

## 1. What is Tabulation?

**Tabulation** = **Iterative Table Filling**

It's the **bottom-up** approach to DP where you:
1. Create a **table** (array) to store subproblem results
2. Fill the table starting from **base cases**
3. Iteratively build up to the **final answer**
4. No recursion involved — purely iterative

```
MEMOIZATION: Top → Down (recursive, lazy)
TABULATION:  Bottom → Up (iterative, eager)
```

### Why "Bottom-Up"?
```
Fibonacci Tabulation:
  dp[0] = 0  ← Start from BASE (bottom)
  dp[1] = 1
  dp[2] = dp[1] + dp[0] = 1
  dp[3] = dp[2] + dp[1] = 2
  dp[4] = dp[3] + dp[2] = 3
  dp[5] = dp[4] + dp[3] = 5  ← Build UP to answer
```

---

## 2. How Tabulation Works

### Step-by-Step Process

```
1. Determine the STATE (what defines a subproblem)
2. Create a DP TABLE with appropriate dimensions
3. Initialize BASE CASES in the table
4. Determine ITERATION ORDER (dependencies must be computed first)
5. Fill the table using the RECURRENCE RELATION
6. Return the ANSWER from the table
```

### Visual: Fibonacci Tabulation

```
Index:  0   1   2   3   4   5
        ↓   ↓
dp:   [ 0 , 1 , _ , _ , _ , _ ]   ← Base cases

Step 1: dp[2] = dp[1] + dp[0] = 1 + 0 = 1
dp:   [ 0 , 1 , 1 , _ , _ , _ ]

Step 2: dp[3] = dp[2] + dp[1] = 1 + 1 = 2
dp:   [ 0 , 1 , 1 , 2 , _ , _ ]

Step 3: dp[4] = dp[3] + dp[2] = 2 + 1 = 3
dp:   [ 0 , 1 , 1 , 2 , 3 , _ ]

Step 4: dp[5] = dp[4] + dp[3] = 3 + 2 = 5
dp:   [ 0 , 1 , 1 , 2 , 3 , 5 ]   ← Answer: dp[5] = 5
```

---

## 3. Universal Template

### Template 1: 1D Tabulation

```java
int solve(int n) {
    // Step 1: Create table
    int[] dp = new int[n + 1];
    
    // Step 2: Base cases
    dp[0] = BASE_VALUE_0;
    dp[1] = BASE_VALUE_1;
    
    // Step 3: Fill table (bottom-up)
    for (int i = 2; i <= n; i++) {
        dp[i] = f(dp[i-1], dp[i-2], ...);  // Recurrence
    }
    
    // Step 4: Return answer
    return dp[n];
}
```

### Template 2: 2D Tabulation

```java
int solve(int m, int n) {
    // Step 1: Create table
    int[][] dp = new int[m + 1][n + 1];
    
    // Step 2: Base cases (first row and first column)
    for (int i = 0; i <= m; i++) dp[i][0] = BASE_ROW;
    for (int j = 0; j <= n; j++) dp[0][j] = BASE_COL;
    
    // Step 3: Fill table
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            dp[i][j] = f(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]);
        }
    }
    
    // Step 4: Return answer
    return dp[m][n];
}
```

### Template 3: Minimization with Choices

```java
int solve(int[] arr, int target) {
    int[] dp = new int[target + 1];
    Arrays.fill(dp, Integer.MAX_VALUE);
    dp[0] = 0;  // Base case
    
    for (int i = 1; i <= target; i++) {
        for (int choice : arr) {
            if (choice <= i && dp[i - choice] != Integer.MAX_VALUE) {
                dp[i] = Math.min(dp[i], dp[i - choice] + 1);
            }
        }
    }
    
    return dp[target] == Integer.MAX_VALUE ? -1 : dp[target];
}
```

---

## 4. Converting Memoization to Tabulation

### The 5-Step Conversion

```
Step 1: Identify the STATE variables from memoization
Step 2: Create a DP table with same dimensions as memo array
Step 3: Set BASE CASES in the table
Step 4: Determine ITERATION ORDER (reverse of recursion direction)
Step 5: Replace recursive calls with table lookups
```

### Example: 0/1 Knapsack Conversion

**Memoization Version:**
```java
int[][] memo;

int dp(int[] wt, int[] val, int i, int w) {
    if (i < 0 || w == 0) return 0;
    if (memo[i][w] != -1) return memo[i][w];
    
    int skip = dp(wt, val, i - 1, w);
    int take = 0;
    if (wt[i] <= w) take = val[i] + dp(wt, val, i - 1, w - wt[i]);
    
    return memo[i][w] = Math.max(skip, take);
}
```

**Tabulation Version:**
```java
int knapsack(int[] wt, int[] val, int W) {
    int n = wt.length;
    int[][] dp = new int[n + 1][W + 1];
    
    // Base case: dp[0][w] = 0 (no items) — already 0 by default
    
    for (int i = 1; i <= n; i++) {
        for (int w = 0; w <= W; w++) {
            dp[i][w] = dp[i - 1][w];  // Skip item
            if (wt[i - 1] <= w) {
                dp[i][w] = Math.max(dp[i][w], 
                    val[i - 1] + dp[i - 1][w - wt[i - 1]]);  // Take item
            }
        }
    }
    
    return dp[n][W];
}
```

### Conversion Mapping
```
Memoization                    →  Tabulation
─────────────────────────────────────────────
Recursive function             →  Nested loops
Base case (if condition)       →  Table initialization
memo[i][j] = result            →  dp[i][j] = result
Recursive call dp(i-1, j)     →  Table lookup dp[i-1][j]
Return memo[n][W]              →  Return dp[n][W]
Top-to-bottom recursion        →  Bottom-to-top iteration
```

---

## 5. 1D Tabulation Examples

### Example 1: Climbing Stairs

```java
public int climbStairs(int n) {
    if (n <= 2) return n;
    int[] dp = new int[n + 1];
    dp[1] = 1;
    dp[2] = 2;
    
    for (int i = 3; i <= n; i++) {
        dp[i] = dp[i - 1] + dp[i - 2];
    }
    
    return dp[n];
}
// Time: O(n), Space: O(n)

// Space Optimized: O(1)
public int climbStairsOptimized(int n) {
    if (n <= 2) return n;
    int prev2 = 1, prev1 = 2;
    for (int i = 3; i <= n; i++) {
        int curr = prev1 + prev2;
        prev2 = prev1;
        prev1 = curr;
    }
    return prev1;
}
```

### Example 2: House Robber

```java
public int rob(int[] nums) {
    int n = nums.length;
    if (n == 1) return nums[0];
    
    int[] dp = new int[n];
    dp[0] = nums[0];
    dp[1] = Math.max(nums[0], nums[1]);
    
    for (int i = 2; i < n; i++) {
        dp[i] = Math.max(
            dp[i - 1],              // Skip current house
            dp[i - 2] + nums[i]     // Rob current house
        );
    }
    
    return dp[n - 1];
}
// Time: O(n), Space: O(n)

// Space Optimized: O(1)
public int robOptimized(int[] nums) {
    int prev2 = 0, prev1 = 0;
    for (int num : nums) {
        int curr = Math.max(prev1, prev2 + num);
        prev2 = prev1;
        prev1 = curr;
    }
    return prev1;
}
```

### Example 3: Coin Change (Minimum Coins)

```java
public int coinChange(int[] coins, int amount) {
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, amount + 1);  // "Infinity"
    dp[0] = 0;
    
    for (int i = 1; i <= amount; i++) {
        for (int coin : coins) {
            if (coin <= i) {
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
    }
    
    return dp[amount] > amount ? -1 : dp[amount];
}
// Time: O(amount × coins), Space: O(amount)
```

### Example 4: Maximum Subarray (Kadane's Algorithm)
> This is actually DP in disguise!

```java
public int maxSubArray(int[] nums) {
    // dp[i] = max subarray sum ending at index i
    int[] dp = new int[nums.length];
    dp[0] = nums[0];
    int maxSum = dp[0];
    
    for (int i = 1; i < nums.length; i++) {
        dp[i] = Math.max(nums[i], dp[i - 1] + nums[i]);
        maxSum = Math.max(maxSum, dp[i]);
    }
    
    return maxSum;
}
// Time: O(n), Space: O(n)

// Space Optimized: O(1)
public int maxSubArrayOptimized(int[] nums) {
    int currentMax = nums[0], globalMax = nums[0];
    for (int i = 1; i < nums.length; i++) {
        currentMax = Math.max(nums[i], currentMax + nums[i]);
        globalMax = Math.max(globalMax, currentMax);
    }
    return globalMax;
}
```

### Example 5: Perfect Squares

```java
public int numSquares(int n) {
    int[] dp = new int[n + 1];
    Arrays.fill(dp, Integer.MAX_VALUE);
    dp[0] = 0;
    
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j * j <= i; j++) {
            dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
        }
    }
    
    return dp[n];
}
// Time: O(n × √n), Space: O(n)
```

---

## 6. 2D Tabulation Examples

### Example 1: Longest Common Subsequence

```java
public int longestCommonSubsequence(String text1, String text2) {
    int m = text1.length(), n = text2.length();
    int[][] dp = new int[m + 1][n + 1];
    // Base: dp[0][j] = 0, dp[i][0] = 0 (already 0)
    
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                dp[i][j] = dp[i - 1][j - 1] + 1;
            } else {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
    }
    
    return dp[m][n];
}
// Time: O(m × n), Space: O(m × n)
```

### Example 2: Edit Distance

```java
public int minDistance(String word1, String word2) {
    int m = word1.length(), n = word2.length();
    int[][] dp = new int[m + 1][n + 1];
    
    // Base cases
    for (int i = 0; i <= m; i++) dp[i][0] = i;  // Delete all
    for (int j = 0; j <= n; j++) dp[0][j] = j;  // Insert all
    
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                dp[i][j] = dp[i - 1][j - 1];  // No operation needed
            } else {
                dp[i][j] = 1 + Math.min(
                    dp[i - 1][j - 1],  // Replace
                    Math.min(dp[i - 1][j],  // Delete
                             dp[i][j - 1])  // Insert
                );
            }
        }
    }
    
    return dp[m][n];
}
// Time: O(m × n), Space: O(m × n)
```

### Example 3: Unique Paths

```java
public int uniquePaths(int m, int n) {
    int[][] dp = new int[m][n];
    
    // Base: first row and first column = 1 (only one way)
    for (int i = 0; i < m; i++) dp[i][0] = 1;
    for (int j = 0; j < n; j++) dp[0][j] = 1;
    
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
        }
    }
    
    return dp[m - 1][n - 1];
}
// Time: O(m × n), Space: O(m × n)

// Space Optimized: O(n)
public int uniquePathsOptimized(int m, int n) {
    int[] dp = new int[n];
    Arrays.fill(dp, 1);
    
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            dp[j] += dp[j - 1];
        }
    }
    
    return dp[n - 1];
}
```

### Example 4: Minimum Path Sum

```java
public int minPathSum(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[][] dp = new int[m][n];
    
    dp[0][0] = grid[0][0];
    
    // First row
    for (int j = 1; j < n; j++) dp[0][j] = dp[0][j - 1] + grid[0][j];
    // First column
    for (int i = 1; i < m; i++) dp[i][0] = dp[i - 1][0] + grid[i][0];
    
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            dp[i][j] = grid[i][j] + Math.min(dp[i - 1][j], dp[i][j - 1]);
        }
    }
    
    return dp[m - 1][n - 1];
}
// Time: O(m × n), Space: O(m × n)
```

---

## 7. Space Optimization Techniques

### Technique 1: Two Rows → One Row

**When `dp[i][j]` depends only on `dp[i-1][...]`:**

```java
// Before: O(m × n) space
int[][] dp = new int[m + 1][n + 1];
for (int i = 1; i <= m; i++)
    for (int j = 1; j <= n; j++)
        dp[i][j] = dp[i-1][j-1] + 1;  // Only uses previous row

// After: O(n) space — two rows
int[] prev = new int[n + 1];
int[] curr = new int[n + 1];
for (int i = 1; i <= m; i++) {
    for (int j = 1; j <= n; j++)
        curr[j] = prev[j-1] + 1;
    int[] temp = prev;
    prev = curr;
    curr = temp;  // Swap (avoid clone for performance)
}
```

### Technique 2: Single Row (Right-to-Left)

**For 0/1 Knapsack pattern:**

```java
// 2D → 1D by iterating RIGHT TO LEFT
int[] dp = new int[W + 1];
for (int i = 0; i < n; i++) {
    for (int w = W; w >= wt[i]; w--) {  // ← RIGHT TO LEFT
        dp[w] = Math.max(dp[w], dp[w - wt[i]] + val[i]);
    }
}
// Why right-to-left? To ensure we use values from previous row (i-1),
// not the current row (i) — prevents using same item twice
```

### Technique 3: Single Row (Left-to-Right)

**For Unbounded Knapsack pattern:**

```java
// Unbounded: can use each item multiple times
int[] dp = new int[W + 1];
for (int i = 0; i < n; i++) {
    for (int w = wt[i]; w <= W; w++) {  // ← LEFT TO RIGHT
        dp[w] = Math.max(dp[w], dp[w - wt[i]] + val[i]);
    }
}
// Left-to-right allows using current row values = using item again
```

### Technique 4: Variables Only

```java
// When only 2-3 previous values needed
// Fibonacci: dp[i] depends on dp[i-1] and dp[i-2]
int prev2 = 0, prev1 = 1;
for (int i = 2; i <= n; i++) {
    int curr = prev1 + prev2;
    prev2 = prev1;
    prev1 = curr;
}
```

### Space Optimization Summary

| Pattern | Original | Optimized | How |
|---------|----------|-----------|-----|
| `dp[i] = f(dp[i-1])` | O(n) | O(1) | Two variables |
| `dp[i] = f(dp[i-1], dp[i-2])` | O(n) | O(1) | Three variables |
| `dp[i][j] = f(dp[i-1][...])` | O(m×n) | O(n) | Two rows or one row |
| `dp[i][j] = f(dp[i-1][j], dp[i][j-1])` | O(m×n) | O(n) | One row (left-to-right) |
| `dp[i][j] = f(dp[i-1][j], dp[i-1][j-1])` | O(m×n) | O(n) | One row (right-to-left) |

---

## 8. Iteration Order — The Critical Detail

### Rule: Dependencies Must Be Computed BEFORE Current Cell

```
If dp[i][j] depends on dp[i-1][j-1], dp[i-1][j], dp[i][j-1]:

    dp[i-1][j-1]  dp[i-1][j]
         ↘           ↓
    dp[i][j-1]  → dp[i][j]    ← Current cell

Iteration: i from 1 to m, j from 1 to n (left-to-right, top-to-bottom) ✅
```

### Common Iteration Patterns

```
Pattern 1: Linear (left to right)
dp[i] depends on dp[i-1], dp[i-2]
→ for (i = 2; i <= n; i++)

Pattern 2: 2D Standard (top-left to bottom-right)
dp[i][j] depends on dp[i-1][j], dp[i][j-1], dp[i-1][j-1]
→ for (i = 1..m) for (j = 1..n)

Pattern 3: Interval DP (by length)
dp[i][j] depends on dp[i][k] and dp[k+1][j] where i ≤ k < j
→ for (len = 2..n) for (i = 0..n-len) { j = i+len-1; }

Pattern 4: Knapsack 1D (right to left)
dp[w] depends on dp[w - wt[i]] from PREVIOUS iteration
→ for (w = W; w >= wt[i]; w--)

Pattern 5: Diagonal
dp[i][j] depends on dp[i+1][j-1]
→ for (i = n-1; i >= 0; i--) for (j = i; j < n; j++)
```

### ⚠️ Wrong Order = Wrong Answer

```java
// 0/1 Knapsack with 1D array:

// ❌ WRONG: Left to right (uses item multiple times!)
for (int w = wt[i]; w <= W; w++)
    dp[w] = Math.max(dp[w], dp[w - wt[i]] + val[i]);
// dp[w - wt[i]] might already be updated in THIS iteration
// → Same item used multiple times → This is UNBOUNDED knapsack!

// ✅ CORRECT: Right to left (each item used at most once)
for (int w = W; w >= wt[i]; w--)
    dp[w] = Math.max(dp[w], dp[w - wt[i]] + val[i]);
// dp[w - wt[i]] is from PREVIOUS iteration → Item used at most once
```

---

## 9. Printing the Actual Solution (Backtracking)

### Technique: Trace Back Through the DP Table

After filling the DP table, trace back from the answer to reconstruct the solution.

### Example 1: Print LCS String

```java
public String printLCS(String s1, String s2) {
    int m = s1.length(), n = s2.length();
    int[][] dp = new int[m + 1][n + 1];
    
    // Fill DP table
    for (int i = 1; i <= m; i++)
        for (int j = 1; j <= n; j++)
            if (s1.charAt(i-1) == s2.charAt(j-1))
                dp[i][j] = dp[i-1][j-1] + 1;
            else
                dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
    
    // Backtrack to find the actual LCS
    StringBuilder lcs = new StringBuilder();
    int i = m, j = n;
    while (i > 0 && j > 0) {
        if (s1.charAt(i-1) == s2.charAt(j-1)) {
            lcs.append(s1.charAt(i-1));  // This char is in LCS
            i--; j--;
        } else if (dp[i-1][j] > dp[i][j-1]) {
            i--;  // Came from top
        } else {
            j--;  // Came from left
        }
    }
    
    return lcs.reverse().toString();
}
```

### Example 2: Print Knapsack Items

```java
public List<Integer> knapsackItems(int[] wt, int[] val, int W) {
    int n = wt.length;
    int[][] dp = new int[n + 1][W + 1];
    
    // Fill DP table
    for (int i = 1; i <= n; i++)
        for (int w = 0; w <= W; w++) {
            dp[i][w] = dp[i-1][w];
            if (wt[i-1] <= w)
                dp[i][w] = Math.max(dp[i][w], val[i-1] + dp[i-1][w - wt[i-1]]);
        }
    
    // Backtrack to find selected items
    List<Integer> items = new ArrayList<>();
    int w = W;
    for (int i = n; i > 0; i--) {
        if (dp[i][w] != dp[i-1][w]) {
            items.add(i - 1);  // Item i-1 was selected
            w -= wt[i - 1];
        }
    }
    
    Collections.reverse(items);
    return items;
}
```

### Example 3: Print Shortest Path in Grid

```java
public List<int[]> shortestPath(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[][] dp = new int[m][n];
    
    dp[0][0] = grid[0][0];
    for (int j = 1; j < n; j++) dp[0][j] = dp[0][j-1] + grid[0][j];
    for (int i = 1; i < m; i++) dp[i][0] = dp[i-1][0] + grid[i][0];
    
    for (int i = 1; i < m; i++)
        for (int j = 1; j < n; j++)
            dp[i][j] = grid[i][j] + Math.min(dp[i-1][j], dp[i][j-1]);
    
    // Backtrack from (m-1, n-1) to (0, 0)
    List<int[]> path = new ArrayList<>();
    int i = m - 1, j = n - 1;
    path.add(new int[]{i, j});
    
    while (i > 0 || j > 0) {
        if (i == 0) j--;
        else if (j == 0) i--;
        else if (dp[i-1][j] < dp[i][j-1]) i--;
        else j--;
        path.add(new int[]{i, j});
    }
    
    Collections.reverse(path);
    return path;
}
```

---

## 10. When to Prefer Tabulation

### ✅ Use Tabulation When:

```
1. ALL states will be computed → No wasted computation
2. Need SPACE OPTIMIZATION → Rolling array / variables
3. Deep recursion risk (n > 10^4) → No stack overflow
4. Performance critical → No recursion overhead
5. Need to PRINT the solution → Backtracking through table
6. Iteration order is CLEAR and SIMPLE
```

### ❌ Avoid Tabulation When:

```
1. State space is SPARSE → Many states never needed
2. Iteration order is COMPLEX → Hard to determine
3. Quick prototyping → Memoization is faster to write
4. Tree/Graph DP → Recursion is more natural
```

### Tabulation vs Memoization — Final Comparison

```
                    TABULATION              MEMOIZATION
                    ──────────              ───────────
Speed:              ⚡ Faster               🐢 Slightly slower
Space opt:          ✅ Easy                 ❌ Hard
Stack overflow:     ✅ No risk              ⚠️ Risk for large n
Code simplicity:    🟡 Depends              ✅ Usually simpler
Sparse states:      ❌ Wastes work          ✅ Only needed states
Iteration order:    ⚠️ Must determine       ✅ Automatic
Solution printing:  ✅ Easy backtrack       🟡 Possible but harder
```

## 11. Advanced: Under The Hood (Why Tabulation is Faster)

Beyond just avoiding the recursion call stack overhead, tabulation is practically faster in languages like Java or C++ due to **CPU Cache Locality**.

* **Memoization** often jumps around memory randomly depending on which state is requested by the recursive calls (e.g., a hash map lookup or jumping across a 2D array matrix non-sequentially).
* **Tabulation** accesses arrays in a predictable, linear fashion (e.g., row by row). The CPU detects this pattern and pre-loads the next chunks of memory into the blazing-fast L1/L2 cache (Cache Prefetching). A cache hit can be up to 100x faster than fetching from main memory (RAM)!

## 12. Common Tabulation Pitfalls

1. **Integer Overflow with `Integer.MAX_VALUE`:**
   When doing minimum-type DP (like Coin Change), you often initialize the array with `Integer.MAX_VALUE`. 
   ```java
   // ❌ DANGER: If dp[i-coin] is MAX_VALUE, adding 1 causes overflow to negative!
   dp[i] = Math.min(dp[i], dp[i-coin] + 1); 
   ```
   **Fix:** Check for "infinity" first, or initialize with a safe large number (like `amount + 1` or `Integer.MAX_VALUE / 2`).

2. **Off-by-One Array Sizes:**
   If the parameter is `n`, the array typically needs to be size `n + 1` (allowing index `0` to `n`). Forgetting the `+ 1` usually results in an `ArrayIndexOutOfBoundsException` on the very last step.

3. **Wrong Default Values:**
   * **Max/Longest** problems: default array to `0` or `Integer.MIN_VALUE`.
   * **Min/Shortest** problems: default array to `Integer.MAX_VALUE` (or safe pseudo-infinity).
   * **Combinatorics (Counting ways)**: usually defaults to `0`, but base case `dp[0]` is often `1`.

---

## 🔥 Quick Reference: Tabulation Patterns

```java
// Pattern 1: Linear DP
for (int i = start; i <= n; i++)
    dp[i] = f(dp[i-1], dp[i-2]);

// Pattern 2: 2D Grid DP
for (int i = 1; i < m; i++)
    for (int j = 1; j < n; j++)
        dp[i][j] = f(dp[i-1][j], dp[i][j-1]);

// Pattern 3: Knapsack (items × capacity)
for (int i = 1; i <= n; i++)
    for (int w = W; w >= wt[i-1]; w--)  // 0/1: right-to-left
        dp[w] = max(dp[w], dp[w-wt[i-1]] + val[i-1]);

// Pattern 4: Interval DP (by length)
for (int len = 2; len <= n; len++)
    for (int i = 0; i + len - 1 < n; i++) {
        int j = i + len - 1;
        dp[i][j] = f(dp[i][k], dp[k+1][j]);
    }

// Pattern 5: Count ways with modulo
for (int i = 1; i <= n; i++)
    dp[i] = (dp[i-1] + dp[i-2]) % MOD;
```

---

## 📚 Next Steps

| Topic | File |
|-------|------|
| Memoization (Top-Down DP) | [MEMOIZATION.MD](MEMOIZATION.MD) |
| DP Patterns Mastery | [PATTERNS.MD](PATTERNS.MD) |
| 0/1 Knapsack Variants | [KNAPSACK.MD](KNAPSACK.MD) |
| Longest Common Subsequence | [LCS.MD](LCS.MD) |
| Matrix Chain Multiplication | [MCM.MD](MCM.MD) |
| DP on Trees | [DP_ON_TREES.MD](DP_ON_TREES.MD) |

---

> **Pro Tip:** Always write down the recursive relation on paper before attempting to code tabular DP. Once you have the recurrence and base cases, building the table is just mechanical loop-writing! 🚀
