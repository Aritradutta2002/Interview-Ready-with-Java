# 🎯 Dynamic Programming - Quick Reference Guide

## 📋 Must-Know Problems for MAANG Interviews

### ⭐ Top 15 Problems (Priority Order)

| # | Problem | Difficulty | Pattern | Companies | Leetcode |
|---|---------|------------|---------|-----------|----------|
| 1 | Climbing Stairs | Easy | Fibonacci | Amazon, Microsoft, Google | 70 |
| 2 | House Robber | Medium | Choose/Skip | Amazon, Google, Microsoft | 198 |
| 3 | Coin Change | Medium | Unbounded Knapsack | Amazon, Microsoft, Google | 322 |
| 4 | Longest Increasing Subsequence | Medium | LIS | Amazon, Microsoft, Google | 300 |
| 5 | Longest Common Subsequence | Medium | LCS | Google, Facebook, Amazon | 1143 |
| 6 | Unique Paths | Medium | 2D Grid | Amazon, Google, Microsoft | 62 |
| 7 | Edit Distance | Hard | 2D String DP | Google, Amazon, Microsoft | 72 |
| 8 | Word Break | Medium | 1D String DP | Google, Amazon, Facebook | 139 |
| 9 | Partition Equal Subset Sum | Medium | 0/1 Knapsack | Amazon, Google, Facebook | 416 |
| 10 | Longest Palindromic Substring | Medium | Expand Center | Amazon, Microsoft, Google | 5 |
| 11 | Best Time to Buy/Sell Stock | Easy | State Machine | Amazon, Microsoft, Bloomberg | 121 |
| 12 | House Robber II | Medium | Circular Array | Amazon, Microsoft, Apple | 213 |
| 13 | Minimum Path Sum | Medium | 2D Grid Cost | Amazon, Google, Facebook | 64 |
| 14 | Coin Change 2 | Medium | Combinations | Google, Amazon, Facebook | 518 |
| 15 | Target Sum | Medium | 0/1 Knapsack | Amazon, Facebook, Microsoft | 494 |

---

## 🔥 DP Patterns Cheat Sheet

### Pattern 1: Fibonacci Sequence
**When to use:** Current state depends on previous 1-2 states
```java
// Template
dp[i] = dp[i-1] + dp[i-2]

// Problems
- Climbing Stairs (70)
- House Robber (198)
- Fibonacci Number (509)
```

### Pattern 2: 0/1 Knapsack
**When to use:** Include/exclude decisions with items
```java
// Template
dp[i][w] = max(dp[i-1][w], value[i] + dp[i-1][w-weight[i]])

// Problems
- Partition Equal Subset Sum (416)
- Target Sum (494)
- Last Stone Weight II (1049)
```

### Pattern 3: Unbounded Knapsack
**When to use:** Unlimited use of each item
```java
// Template
for (item in items) {
    for (w = item.weight; w <= capacity; w++) {
        dp[w] += dp[w - item.weight]
    }
}

// Problems
- Coin Change (322)
- Coin Change 2 (518)
- Perfect Squares (279)
```

### Pattern 4: Longest Common Subsequence (LCS)
**When to use:** Matching between two sequences
```java
// Template
if (s1[i] == s2[j])
    dp[i][j] = dp[i-1][j-1] + 1
else
    dp[i][j] = max(dp[i-1][j], dp[i][j-1])

// Problems
- Longest Common Subsequence (1143)
- Edit Distance (72)
- Delete Operation for Two Strings (583)
```

### Pattern 5: Longest Increasing Subsequence (LIS)
**When to use:** Finding increasing/decreasing patterns
```java
// Template O(n²)
for (int i = 0; i < n; i++) {
    for (int j = 0; j < i; j++) {
        if (arr[j] < arr[i])
            dp[i] = max(dp[i], dp[j] + 1)
    }
}

// Template O(n log n) - Binary Search
- Use binary search on tails array

// Problems
- Longest Increasing Subsequence (300)
- Number of LIS (673)
- Russian Doll Envelopes (354)
```

### Pattern 6: Grid/Path Problems
**When to use:** Moving in a grid (usually right/down)
```java
// Template
dp[i][j] = operation(dp[i-1][j], dp[i][j-1])

// Problems
- Unique Paths (62)
- Minimum Path Sum (64)
- Maximal Square (221)
```

### Pattern 7: Palindrome Problems
**When to use:** Checking/finding palindromes
```java
// Template - Expand Around Center
for (int i = 0; i < n; i++) {
    expandFromCenter(i, i)     // odd length
    expandFromCenter(i, i+1)   // even length
}

// Template - DP
if (s[i] == s[j] && dp[i+1][j-1])
    dp[i][j] = true

// Problems
- Longest Palindromic Substring (5)
- Palindromic Substrings (647)
- Longest Palindromic Subsequence (516)
```

### Pattern 8: Stock Trading
**When to use:** Buy/sell with constraints
```java
// Template - State Machine
buy[i] = max(buy[i-1], sell[i-1] - price[i])
sell[i] = max(sell[i-1], buy[i-1] + price[i])

// Problems
- Best Time to Buy/Sell Stock I-VI (121, 122, 123, 188, 309, 714)
```

---

## ⚡ Time/Space Complexity Quick Reference

| Pattern | Time | Space | Optimized Space |
|---------|------|-------|-----------------|
| 1D DP | O(n) | O(n) | O(1) |
| 2D DP | O(n²) | O(n²) | O(n) |
| LCS | O(mn) | O(mn) | O(min(m,n)) |
| LIS (DP) | O(n²) | O(n) | O(n) |
| LIS (Binary Search) | O(n log n) | O(n) | O(n) |
| Knapsack 0/1 | O(nW) | O(nW) | O(W) |
| Unbounded Knapsack | O(nW) | O(W) | O(W) |

---

## 🎓 Interview Tips

### 1. How to Approach Any DP Problem

```
Step 1: Can I use recursion to solve this?
        ↓
Step 2: Are there overlapping subproblems?
        ↓ YES
Step 3: Define DP state
        ↓
Step 4: Write recurrence relation
        ↓
Step 5: Implement (Memoization OR Tabulation)
        ↓
Step 6: Optimize space if possible
```

### 2. Common DP State Definitions

- **1D Array:** `dp[i]` = answer using elements 0 to i
- **2D Array:** `dp[i][j]` = answer using elements i to j
- **With Previous:** `dp[i][prev]` = answer at i with previous state
- **Knapsack:** `dp[i][w]` = max value with i items and weight w

### 3. Red Flags for DP

✅ Use DP when you see:
- "Maximum/Minimum"
- "Count number of ways"
- "Longest/Shortest"
- Overlapping subproblems
- Optimal substructure

❌ Don't use DP when:
- Need actual paths/sequences (often need backtracking)
- Greedy works (like interval scheduling)
- Problem has no overlapping subproblems

### 4. Space Optimization Tricks

**2D → 1D:**
```java
// Before: dp[i][j] depends on dp[i-1][j] and dp[i][j-1]
// After: Use single array, iterate carefully
for (int i = 0; i < n; i++) {
    for (int j = 0; j < m; j++) {
        dp[j] = operation(dp[j], dp[j-1])
    }
}
```

**Variables Only:**
```java
// If only need previous 1-2 values
int prev2 = init, prev1 = init
for (...) {
    int curr = operation(prev1, prev2)
    prev2 = prev1
    prev1 = curr
}
```

---

## 🏆 Company-Specific Focus

### Amazon (Focus on practical problems)
- House Robber I, II
- Coin Change I, II
- Partition Equal Subset Sum
- Unique Paths variations
- Best Time to Buy/Sell Stock

### Google (Focus on algorithms)
- Edit Distance
- Longest Increasing Subsequence (O(n log n))
- Word Break
- Burst Balloons
- Longest Common Subsequence

### Microsoft
- Climbing Stairs
- House Robber
- Minimum Path Sum
- Longest Palindromic Substring
- Coin Change

### Facebook
- Word Break
- Edit Distance
- House Robber II
- Target Sum
- Partition problems

---

## 📚 Study Schedule (4 Weeks)

### Week 1: Foundations
- Day 1-2: Climbing Stairs, Fibonacci variants
- Day 3-4: House Robber I, II
- Day 5-6: Coin Change I, II
- Day 7: Practice and review

### Week 2: Strings
- Day 1-2: Longest Palindromic Substring
- Day 3-4: Edit Distance
- Day 5-6: Word Break
- Day 7: LCS and variants

### Week 3: 2D DP
- Day 1-2: Unique Paths, Minimum Path Sum
- Day 3-4: LIS (both approaches)
- Day 5-6: Maximal Square, Dungeon Game
- Day 7: Practice

### Week 4: Advanced
- Day 1-2: All 6 Stock problems
- Day 3-4: Knapsack variants (Partition, Target Sum)
- Day 5-6: Burst Balloons, Palindrome Partitioning
- Day 7: Mock interviews

---

## ⚠️ Common Mistakes to Avoid

1. ❌ Not defining the DP state clearly
2. ❌ Forgetting base cases
3. ❌ Off-by-one errors in indices
4. ❌ Wrong loop order (combinations vs permutations)
5. ❌ Not considering space optimization
6. ❌ Confusing substring vs subsequence
7. ❌ Not handling edge cases (empty input, single element)
8. ❌ Using global variables in memoization
9. ❌ Not initializing DP array properly
10. ❌ Overcomplicating simple problems

---

## 🎯 Final Checklist Before Interview

- [ ] Can explain what DP is and when to use it
- [ ] Know difference between memoization and tabulation
- [ ] Can identify DP patterns quickly
- [ ] Practiced top 15 problems
- [ ] Can optimize space complexity
- [ ] Understand time complexity analysis
- [ ] Can draw DP tables for small examples
- [ ] Know how to handle edge cases
- [ ] Practiced explaining solutions clearly
- [ ] Timed practice on random DP problems

---

**Good Luck! 🚀**

*Remember: Understanding patterns is more important than memorizing solutions!*

