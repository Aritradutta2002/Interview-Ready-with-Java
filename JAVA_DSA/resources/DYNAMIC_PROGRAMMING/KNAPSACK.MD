# 🛍️ Knapsack Problems — Complete Guide

## 1. 0/1 Knapsack (Classic)

Each item can be used **0 or 1 time**.

### Tabulation (Space Optimized)

```java
public int knapsack01(int[] wt, int[] val, int W) {
    int n = wt.length;
    int[] dp = new int[W + 1];
    
    for (int i = 0; i < n; i++) {
        for (int w = W; w >= wt[i]; w--) {  // Right to left!
            dp[w] = Math.max(dp[w], dp[w - wt[i]] + val[i]);
        }
    }
    return dp[W];
}
```
**Time:** O(nW), **Space:** O(W)

## 2. Unbounded Knapsack

Items can be used **unlimited times**.

```java
public int unboundedKnapsack(int[] wt, int[] val, int W) {
    int[] dp = new int[W + 1];
    for (int i = 0; i < wt.length; i++) {
        for (int w = wt[i]; w <= W; w++) {  // Left to right!
            dp[w] = Math.max(dp[w], dp[w - wt[i]] + val[i]);
        }
    }
    return dp[W];
}
```

## 3. Subset Sum

**Can we achieve exactly target W?** (Value = 1 for all items)

```java
public boolean subsetSum(int[] nums, int target) {
    boolean[] dp = new boolean[target + 1];
    dp[0] = true;
    
    for (int num : nums) {
        for (int i = target; i >= num; i--) {
            dp[i] = dp[i] || dp[i - num];
        }
    }
    return dp[target];
}
```

## 4. Equal Partition Subset Sum

**Can we partition array into 2 equal sum subsets?**

```java
public boolean canPartition(int[] nums) {
    int sum = Arrays.stream(nums).sum();
    if (sum % 2 != 0) return false;
    
    return subsetSum(nums, sum / 2);
}
```

## 5. Count Subsets with Given Sum

```java
public int countSubsets(int[] nums, int target) {
    int[] dp = new int[target + 1];
    dp[0] = 1;  // 1 way to make sum 0
    
    for (int num : nums) {
        for (int i = target; i >= num; i--) {
            dp[i] += dp[i - num];
        }
    }
    return dp[target];
}
```

## 6. Minimum Subset Sum Difference

**Find minimum |sum1 - sum2| where sum1 + sum2 = total**

```java
public int minSubsetSumDifference(int[] nums) {
    int total = Arrays.stream(nums).sum();
    boolean[] dp = new boolean[total / 2 + 1];
    dp[0] = true;
    
    for (int num : nums) {
        for (int i = total / 2; i >= num; i--) {
            dp[i] = dp[i] || dp[i - num];
        }
    }
    
    int maxAchievable = 0;
    for (int i = total / 2; i >= 0; i--) {
        if (dp[i]) {
            maxAchievable = i;
            break;
        }
    }
    return total - 2 * maxAchievable;
}
```

## 7. Space Optimization Summary

```
0/1 Knapsack:       Right-to-left iteration → O(W)
Unbounded Knapsack: Left-to-right → O(W)
Subset Sum:         Right-to-left → O(target)
Count Subsets:      Right-to-left → O(target)
```

## 🔥 Quick Reference

```
Problem                    | Recurrence | Iteration Order
───────────────────────────┼────────────┼─────────────────
0/1 Knapsack              | max        | Right→Left
Unbounded Knapsack        | max        | Left→Right  
Subset Sum (boolean)      | OR         | Right→Left
Count Subsets             | +          | Right→Left
```

## 🎯 Practice Problems
- LeetCode 416. Partition Equal Subset Sum
- LeetCode 322. Coin Change
- Codeforces 1198B 
- SPOJ KNAPSACK

