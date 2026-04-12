# 🧠 Dynamic Programming — Complete Theory (A to Z)

> *"Those who cannot remember the past are condemned to repeat it."* — This is literally what DP solves.

---

## 📌 Table of Contents
1. [What is Dynamic Programming?](#1-what-is-dynamic-programming)
2. [Two Key Properties](#2-two-key-properties)
3. [DP vs Recursion vs Greedy vs Divide & Conquer](#3-dp-vs-recursion-vs-greedy-vs-divide--conquer)
4. [Top-Down vs Bottom-Up](#4-top-down-vs-bottom-up)
5. [How to Think in DP — State Design](#5-how-to-think-in-dp--state-design)
6. [DP Problem Solving Framework](#6-dp-problem-solving-framework)
7. [Complexity Analysis in DP](#7-complexity-analysis-in-dp)
8. [Space Optimization Techniques](#8-space-optimization-techniques)
9. [Classification of DP Problems](#9-classification-of-dp-problems)
10. [Common Mistakes & Debugging](#10-common-mistakes--debugging)
11. [When NOT to Use DP](#11-when-not-to-use-dp)

---

## 1. What is Dynamic Programming?

**Dynamic Programming (DP)** is an algorithmic technique that solves complex problems by:
1. Breaking them into **smaller overlapping subproblems**
2. Solving each subproblem **only once**
3. **Storing** the results to avoid redundant computation

### The Core Idea
```
Instead of solving the same subproblem again and again,
solve it ONCE → store the result → reuse it when needed.
```

### Simple Analogy
```
Teacher: "What is 3 + 4?"
Student: "7"
Teacher: "What is 3 + 4 + 5?"
Student: "I already know 3+4=7, so 7+5 = 12"  ← THIS IS DP!
```

### Fibonacci — The Classic Example

**Without DP (Pure Recursion):**
```java
// Time: O(2^n) — EXPONENTIAL! 💀
int fib(int n) {
    if (n <= 1) return n;
    return fib(n - 1) + fib(n - 2);  // Same subproblems solved repeatedly
}
```

**Recursion Tree for fib(5):**
```
                    fib(5)
                   /      \
              fib(4)       fib(3)      ← fib(3) computed TWICE
             /    \        /    \
         fib(3)  fib(2)  fib(2) fib(1) ← fib(2) computed THREE times
        /    \
    fib(2)  fib(1)
```

**With DP (Memoization):**
```java
// Time: O(n) — LINEAR! ✅
int[] memo = new int[n + 1];
Arrays.fill(memo, -1);

int fib(int n) {
    if (n <= 1) return n;
    if (memo[n] != -1) return memo[n];  // Already solved? Return it!
    return memo[n] = fib(n - 1) + fib(n - 2);
}
```

**With DP (Tabulation):**
```java
// Time: O(n), Space: O(n)
int fib(int n) {
    int[] dp = new int[n + 1];
    dp[0] = 0; dp[1] = 1;
    for (int i = 2; i <= n; i++) {
        dp[i] = dp[i - 1] + dp[i - 2];
    }
    return dp[n];
}
```

**With Space Optimization:**
```java
// Time: O(n), Space: O(1) 🔥
int fib(int n) {
    if (n <= 1) return n;
    int prev2 = 0, prev1 = 1;
    for (int i = 2; i <= n; i++) {
        int curr = prev1 + prev2;
        prev2 = prev1;
        prev1 = curr;
    }
    return prev1;
}
```

---

## 2. Two Key Properties

A problem can be solved using DP **if and only if** it has these two properties:

### Property 1: Optimal Substructure
> The optimal solution to the problem can be constructed from optimal solutions of its subproblems.

```
Example: Shortest Path A → D
If shortest path is A → B → C → D,
then A → B → C must also be the shortest path from A to C.

✅ Shortest Path has Optimal Substructure
❌ Longest Simple Path does NOT (in general graphs)
```

**How to verify:**
- Can you express the solution in terms of solutions to smaller instances?
- Does `OPT(problem) = f(OPT(subproblem1), OPT(subproblem2), ...)`?

### Property 2: Overlapping Subproblems
> The same subproblems are solved multiple times during recursion.

```
Fibonacci: fib(5) calls fib(3) and fib(4), both of which call fib(2)
→ fib(2) is an OVERLAPPING SUBPROBLEM

Merge Sort: Each subarray is unique, never repeated
→ NO overlapping subproblems → Use Divide & Conquer, NOT DP
```

**Visual Comparison:**
```
DP (Fibonacci):              D&C (Merge Sort):
     f(5)                      [3,1,4,2]
    /    \                     /        \
  f(4)   f(3)  ← OVERLAP   [3,1]     [4,2]   ← NO OVERLAP
  /  \   /  \              /   \     /   \
f(3) f(2) f(2) f(1)      [3]  [1]  [4]  [2]
```

### Quick Check Table
| Property | Has it? | Technique |
|----------|---------|-----------|
| Optimal Substructure + Overlapping Subproblems | ✅ ✅ | **Dynamic Programming** |
| Optimal Substructure + NO Overlapping | ✅ ❌ | **Divide & Conquer** |
| Greedy Choice Property + Optimal Substructure | ✅ ✅ | **Greedy** |
| Neither | ❌ ❌ | **Brute Force / Other** |

---

## 3. DP vs Recursion vs Greedy vs Divide & Conquer

### Comparison Table

| Aspect | Recursion | DP | Greedy | Divide & Conquer |
|--------|-----------|-----|--------|-----------------|
| **Approach** | Break into subproblems | Break + Store results | Local optimal choice | Break into independent parts |
| **Subproblems** | May repeat | Overlapping, stored | No subproblems | Non-overlapping |
| **Guarantee** | Correct (if complete) | Optimal | Not always optimal | Optimal (for its problems) |
| **Time** | Often exponential | Polynomial | Usually O(n log n) | O(n log n) typically |
| **Space** | Stack space | Table/Cache | O(1) usually | Stack space |
| **Example** | Fibonacci naive | Fibonacci DP | Activity Selection | Merge Sort |

### When to Use What?

```
🤔 Can I make a greedy choice that's always optimal?
   YES → Use GREEDY (fastest)
   NO ↓

🤔 Does the problem have overlapping subproblems?
   YES → Use DP
   NO ↓

🤔 Can I divide into independent subproblems?
   YES → Use DIVIDE & CONQUER
   NO → Use BRUTE FORCE / BACKTRACKING
```

### Key Insight
```
Recursion = the STRUCTURE of the solution
DP = Recursion + MEMORY (caching)
Greedy = Recursion + PRUNING (only best choice)
```

---

## 4. Top-Down vs Bottom-Up

### Top-Down (Memoization)
- Start from the **main problem**, recurse down to base cases
- Use a **cache** (array/HashMap) to store results
- **Lazy**: Only solves subproblems that are actually needed

```java
// Template
int solve(int state, int[] memo) {
    // Base case
    if (baseCondition) return baseValue;
    
    // Check cache
    if (memo[state] != -1) return memo[state];
    
    // Compute and store
    int result = /* combine subproblem solutions */;
    return memo[state] = result;
}
```

### Bottom-Up (Tabulation)
- Start from **base cases**, build up to the main problem
- Use a **table** (array) filled iteratively
- **Eager**: Solves ALL subproblems in order

```java
// Template
int solve(int n) {
    int[] dp = new int[n + 1];
    
    // Base cases
    dp[0] = baseValue0;
    dp[1] = baseValue1;
    
    // Fill table
    for (int i = 2; i <= n; i++) {
        dp[i] = /* combine dp[smaller states] */;
    }
    
    return dp[n];
}
```

### Comparison

| Aspect | Top-Down (Memo) | Bottom-Up (Tab) |
|--------|----------------|-----------------|
| **Direction** | Problem → Base case | Base case → Problem |
| **Implementation** | Recursion + Cache | Iteration + Table |
| **Subproblems solved** | Only needed ones | All of them |
| **Stack overflow risk** | Yes (deep recursion) | No |
| **Space optimization** | Harder | Easier (rolling array) |
| **Code readability** | More intuitive | Sometimes tricky |
| **Speed** | Slightly slower (recursion overhead) | Slightly faster |
| **When to prefer** | Complex state transitions, sparse states | When all states needed, need space optimization |

### 💡 Pro Tip: Conversion Strategy
```
Step 1: Write recursive solution
Step 2: Add memoization (Top-Down DP) ← Usually easiest
Step 3: Convert to tabulation (Bottom-Up DP)
Step 4: Optimize space if possible
```

---

## 5. How to Think in DP — State Design

**State design is the HARDEST and MOST IMPORTANT part of DP.**

### The 5-Step Framework

#### Step 1: Define the State
> What information do I need to uniquely describe a subproblem?

```
Ask yourself: "What changes between subproblems?"

Fibonacci: Only the index changes → state = dp[i]
Knapsack:  Index AND remaining capacity → state = dp[i][w]
LCS:       Position in string1 AND string2 → state = dp[i][j]
```

#### Step 2: Define the Transition (Recurrence Relation)
> How does the current state relate to previous states?

```
Fibonacci:  dp[i] = dp[i-1] + dp[i-2]
Knapsack:   dp[i][w] = max(dp[i-1][w], dp[i-1][w-wt[i]] + val[i])
LCS:        dp[i][j] = dp[i-1][j-1] + 1           (if match)
                      = max(dp[i-1][j], dp[i][j-1]) (if no match)
```

#### Step 3: Define the Base Cases
> What are the smallest subproblems I can solve directly?

```
Fibonacci:  dp[0] = 0, dp[1] = 1
Knapsack:   dp[0][w] = 0 (no items), dp[i][0] = 0 (no capacity)
LCS:        dp[0][j] = 0, dp[i][0] = 0 (empty string)
```

#### Step 4: Define the Answer
> Where in the DP table is my final answer?

```
Fibonacci:  dp[n]
Knapsack:   dp[n][W]
LCS:        dp[m][n]
```

#### Step 5: Determine the Order of Computation
> In what order should I fill the table?

```
Fibonacci:  Left to right (i = 2 to n)
Knapsack:   Row by row, left to right
LCS:        Row by row, left to right
```

### State Design Cheat Sheet

| Problem Type | Common States | Dimensions |
|-------------|---------------|------------|
| Linear sequence | `dp[i]` = answer for first i elements | 1D |
| Two sequences | `dp[i][j]` = answer for first i of seq1, first j of seq2 | 2D |
| Knapsack-type | `dp[i][w]` = answer using first i items with capacity w | 2D |
| Grid | `dp[i][j]` = answer at cell (i,j) | 2D |
| Interval | `dp[i][j]` = answer for subarray [i..j] | 2D |
| Bitmask | `dp[mask]` = answer for subset represented by mask | 1D (2^n states) |
| Digit | `dp[pos][tight][sum]` = count of numbers | 3D+ |
| Tree | `dp[node][state]` = answer for subtree rooted at node | 2D |

---

## 6. DP Problem Solving Framework

### The Universal Template

```
1. IDENTIFY: Is this a DP problem?
   → Can I break it into subproblems?
   → Are subproblems overlapping?
   → Does it ask for optimal/count/feasibility?

2. DEFINE STATE: What parameters define a subproblem?
   → What's the minimum info needed?

3. WRITE RECURRENCE: How do states relate?
   → What choices do I have at each step?
   → dp[current] = f(dp[previous states])

4. BASE CASES: What are the trivial subproblems?

5. IMPLEMENT: Top-down or Bottom-up?

6. OPTIMIZE: Can I reduce space?
```

### How to Identify DP Problems

**🚩 Red Flags that scream "USE DP!":**

```
1. "Find the MINIMUM/MAXIMUM..."
2. "Count the NUMBER OF WAYS..."
3. "Is it POSSIBLE to...?" (feasibility)
4. "Find the LONGEST/SHORTEST..."
5. "What is the OPTIMAL..."
6. Problem has choices at each step
7. Future decisions depend on past decisions
8. Brute force is exponential
```

**🚩 Common Problem Patterns:**

```
"Given an array, find the maximum sum subsequence..."     → Linear DP
"Given two strings, find the longest common..."           → Two-sequence DP
"Given items with weights and values, maximize..."        → Knapsack DP
"Given a grid, find the minimum cost path..."             → Grid DP
"Given a set, partition into two subsets..."              → Subset Sum DP
"Count numbers from 1 to N with property X..."           → Digit DP
"Given a tree, find the maximum independent set..."       → Tree DP
```

---

## 7. Complexity Analysis in DP

### Formula
```
Time Complexity  = (Number of States) × (Transition Cost per State)
Space Complexity = (Number of States) × (Space per State)
```

### Examples

| Problem | States | Transition | Time | Space |
|---------|--------|------------|------|-------|
| Fibonacci | n | O(1) | O(n) | O(n) → O(1) |
| 0/1 Knapsack | n × W | O(1) | O(nW) | O(nW) → O(W) |
| LCS | m × n | O(1) | O(mn) | O(mn) → O(n) |
| Matrix Chain | n² | O(n) | O(n³) | O(n²) |
| TSP (Bitmask) | 2^n × n | O(n) | O(2^n × n²) | O(2^n × n) |
| Digit DP | pos × sum × tight | O(10) | O(digits × sum × 2 × 10) | O(digits × sum × 2) |

### ⚠️ Important Notes
```
- DP time is usually PSEUDO-POLYNOMIAL for knapsack-type problems
  (polynomial in the VALUE of input, not the SIZE)
  
- If W = 10^9, O(nW) knapsack is NOT feasible!
  → Need different approach or optimization

- Bitmask DP: 2^n states → feasible only for n ≤ 20-22
```

---

## 8. Space Optimization Techniques

### Technique 1: Rolling Array (Most Common)
When `dp[i]` only depends on `dp[i-1]`:

```java
// Before: O(n×m) space
int[][] dp = new int[n + 1][m + 1];
for (int i = 1; i <= n; i++)
    for (int j = 1; j <= m; j++)
        dp[i][j] = f(dp[i-1][j], dp[i-1][j-1]);

// After: O(m) space — use TWO rows
int[] prev = new int[m + 1];
int[] curr = new int[m + 1];
for (int i = 1; i <= n; i++) {
    for (int j = 1; j <= m; j++)
        curr[j] = f(prev[j], prev[j-1]);
    prev = curr.clone(); // or swap references
}
```

### Technique 2: Single Row (When filling right-to-left)
For 0/1 Knapsack:

```java
// O(W) space instead of O(n×W)
int[] dp = new int[W + 1];
for (int i = 0; i < n; i++)
    for (int w = W; w >= wt[i]; w--)  // RIGHT TO LEFT! ← Critical
        dp[w] = Math.max(dp[w], dp[w - wt[i]] + val[i]);
```

### Technique 3: Variables Only
When only 2-3 previous values needed:

```java
// Fibonacci: O(1) space
int prev2 = 0, prev1 = 1;
for (int i = 2; i <= n; i++) {
    int curr = prev1 + prev2;
    prev2 = prev1;
    prev1 = curr;
}
```

### Space Optimization Decision Tree
```
Does dp[i] depend on dp[i-1] only?
├── YES → Can I use 1D array?
│   ├── Fill left-to-right: Use TWO arrays (prev, curr)
│   └── Fill right-to-left: Use ONE array
└── NO → Does it depend on dp[i-1] and dp[i-2]?
    ├── YES → Use THREE variables or TWO arrays
    └── NO → Usually can't optimize easily
```

---

## 9. Classification of DP Problems

### By Structure

```
📁 LINEAR DP
├── Single sequence: LIS, House Robber, Jump Game
├── Two sequences: LCS, Edit Distance, Interleaving String
└── Multi-sequence: 3-sequence LCS

📁 KNAPSACK DP
├── 0/1 Knapsack: Subset Sum, Equal Partition
├── Unbounded Knapsack: Coin Change, Rod Cutting
└── Bounded Knapsack: Limited supply items

📁 GRID/MATRIX DP
├── Path counting: Unique Paths
├── Min/Max path: Min Path Sum, Dungeon Game
└── Submatrix: Maximal Square, Maximal Rectangle

📁 INTERVAL DP
├── Matrix Chain Multiplication
├── Burst Balloons
└── Palindrome Partitioning

📁 TREE DP
├── Subtree problems: House Robber III
├── Path problems: Diameter, Max Path Sum
└── Re-rooting: Sum of distances

📁 STRING DP
├── Matching: Wildcard, Regex
├── Palindrome: Longest Palindromic Subsequence
└── Transformation: Edit Distance

📁 BITMASK DP
├── TSP (Traveling Salesman)
├── Assignment Problem
└── SOS (Sum over Subsets)

📁 DIGIT DP
├── Count numbers with digit properties
└── Digit sum constraints

📁 GAME THEORY DP
├── Stone Game variants
├── Nim Game
└── Minimax

📁 STATE MACHINE DP
├── Stock Buy/Sell problems
└── Finite automata transitions

📁 ADVANCED DP
├── Convex Hull Trick
├── Divide & Conquer Optimization
├── Knuth's Optimization
└── Slope Trick
```

### By Difficulty (Competitive Programming)

```
🟢 EASY (Codeforces 800-1200, LeetCode Easy-Medium)
├── Fibonacci, Climbing Stairs
├── House Robber
├── Min Cost Climbing Stairs
├── Maximum Subarray (Kadane's)
└── Coin Change (basic)

🟡 MEDIUM (Codeforces 1200-1600, LeetCode Medium)
├── 0/1 Knapsack, Subset Sum
├── LCS, LIS
├── Unique Paths, Min Path Sum
├── Word Break
├── Decode Ways
└── Longest Palindromic Subsequence

🟠 HARD (Codeforces 1600-2000, LeetCode Medium-Hard)
├── Edit Distance
├── Burst Balloons
├── Matrix Chain Multiplication
├── Palindrome Partitioning II
├── Stock Buy/Sell with cooldown/fee
└── Interleaving String

🔴 EXPERT (Codeforces 2000+, LeetCode Hard)
├── Bitmask DP (TSP)
├── Digit DP
├── Tree DP (re-rooting)
├── Convex Hull Trick
├── SOS DP
└── Profile DP (broken profile)
```

---

## 10. Common Mistakes & Debugging

### Mistake 1: Wrong Base Cases
```java
// ❌ WRONG: Forgot base case
int solve(int n) {
    return dp[n] = solve(n-1) + solve(n-2); // Stack overflow for n=0!
}

// ✅ CORRECT
int solve(int n) {
    if (n <= 1) return n;  // Base case FIRST
    if (dp[n] != -1) return dp[n];
    return dp[n] = solve(n-1) + solve(n-2);
}
```

### Mistake 2: Wrong Initialization
```java
// ❌ WRONG: Default 0 when looking for minimum
int[] dp = new int[n + 1]; // All zeros
// dp[i] = min(dp[i], ...) → Always 0!

// ✅ CORRECT
int[] dp = new int[n + 1];
Arrays.fill(dp, Integer.MAX_VALUE);
dp[0] = 0; // Only base case is 0
```

### Mistake 3: Wrong Iteration Order
```java
// ❌ WRONG: 0/1 Knapsack with left-to-right (uses item multiple times!)
for (int w = wt[i]; w <= W; w++)
    dp[w] = Math.max(dp[w], dp[w - wt[i]] + val[i]);

// ✅ CORRECT: Right-to-left for 0/1 Knapsack
for (int w = W; w >= wt[i]; w--)
    dp[w] = Math.max(dp[w], dp[w - wt[i]] + val[i]);
```

### Mistake 4: Integer Overflow
```java
// ❌ WRONG: Count of ways can overflow
dp[i] = dp[i-1] + dp[i-2]; // Can exceed int range!

// ✅ CORRECT: Use long or modular arithmetic
long[] dp = new long[n + 1];
dp[i] = (dp[i-1] + dp[i-2]) % MOD;
```

### Mistake 5: Off-by-One Errors
```java
// ❌ WRONG: Array too small
int[] dp = new int[n]; // Index 0 to n-1
dp[n] = ...; // ArrayIndexOutOfBoundsException!

// ✅ CORRECT
int[] dp = new int[n + 1]; // Index 0 to n
```

### Debugging Tips
```
1. Print the DP table — verify values manually
2. Test with smallest inputs (n=0, n=1, n=2)
3. Verify base cases independently
4. Check if transition covers ALL cases
5. Verify iteration order matches dependencies
6. Use assert statements for invariants
```

---

## 11. When NOT to Use DP

```
❌ Problem has NO overlapping subproblems
   → Use Divide & Conquer instead

❌ Greedy choice property exists
   → Use Greedy (simpler and faster)

❌ State space is too large (e.g., 10^18 states)
   → Need mathematical formula or matrix exponentiation

❌ Problem requires ALL solutions (not just count/optimal)
   → Use Backtracking

❌ Problem is on a general graph with negative cycles
   → DP may not terminate correctly

❌ Constraints suggest O(n log n) or O(n)
   → DP might be too slow; look for greedy/two-pointer/binary search
```

---

## 🔥 Golden Rules of DP

```
1. ALWAYS start with recursion — understand the problem first
2. IDENTIFY what changes between subproblems — that's your STATE
3. WRITE the recurrence BEFORE coding
4. VERIFY base cases with small examples
5. CHOOSE top-down for complex states, bottom-up for optimization
6. OPTIMIZE space as the LAST step
7. PRACTICE patterns — most DP problems are variations of known patterns
```

---

## 📚 What's Next?

| Topic | File |
|-------|------|
| Top-Down Approach | [MEMOIZATION.MD](MEMOIZATION.MD) |
| Bottom-Up Approach | [TABULATION.MD](TABULATION.MD) |
| Knapsack Problems | [KNAPSACK.MD](KNAPSACK.MD) |
| Longest Common Subsequence | [LCS.MD](LCS.MD) |
| Longest Increasing Subsequence | [LIS.MD](LIS.MD) |
| Coin Change Problems | [COIN_CHANGE.MD](COIN_CHANGE.MD) |
| Grid DP | [GRID_DP.MD](GRID_DP.MD) |
| String DP | [STRING_DP.MD](STRING_DP.MD) |
| Tree DP | [TREE_DP.MD](TREE_DP.MD) |
| Bitmask DP | [BITMASK_DP.MD](BITMASK_DP.MD) |
| Interval DP | [INTERVAL_DP.MD](INTERVAL_DP.MD) |
| Digit DP | [DIGIT_DP.MD](DIGIT_DP.MD) |
| Game Theory DP | [GAME_THEORY_DP.MD](GAME_THEORY_DP.MD) |
| State Machine DP | [STATE_MACHINE_DP.MD](STATE_MACHINE_DP.MD) |
| Partition DP | [PARTITION_DP.MD](PARTITION_DP.MD) |
| Advanced Optimizations | [OPTIMIZATION_TECHNIQUES.MD](OPTIMIZATION_TECHNIQUES.MD) |
| Patterns & Tricks | [PATTERNS_AND_TRICKS.MD](PATTERNS_AND_TRICKS.MD) |
| Practice Problems | [PRACTICE_PROBLEMS.MD](PRACTICE_PROBLEMS.MD) |
