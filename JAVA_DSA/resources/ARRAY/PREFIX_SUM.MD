# Prefix Sum - Complete Guide

## Table of Contents
1. [What is Prefix Sum?](#what-is-prefix-sum)
2. [Why Prefix Sum?](#why-prefix-sum)
3. [Basic Implementation](#basic-implementation)
4. [Range Sum Queries](#range-sum-queries)
5. [Variations of Prefix Sum](#variations-of-prefix-sum)
6. [Common Problems](#common-problems)
7. [Java Implementations](#java-implementations)
8. [Time and Space Complexity](#time-and-space-complexity)

---

## What is Prefix Sum?

Prefix sum is a technique where we precompute a **cumulative sum array** where each element represents the sum of all elements from the beginning up to that index.

### Visual Representation

```
Original Array:
Index:     0    1    2    3    4    5
Value:    [3    7    2    8    5    9]

Prefix Sum Array:
Index:     0    1    2    3    4    5
Value:    [3   10   12   20   25   34]
            │    │    │    │    │    │
            │    │    │    │    │    └── sum[0..5] = 34
            │    │    │    │    └─────── sum[0..4] = 25
            │    │    │    └───────────── sum[0..3] = 20
            │    │    └────────────────── sum[0..2] = 12
            │    └─────────────────────── sum[0..1] = 10
            └──────────────────────────── sum[0..0] = 3
```

---

## Why Prefix Sum?

### The Problem

Without prefix sum - finding sum of range [L, R] requires O(n) time:
```java
int rangeSumNaive(int[] arr, int L, int R) {
    int sum = 0;
    for (int i = L; i <= R; i++) {
        sum += arr[i];
    }
    return sum;
}
// If we need 1000 range queries, each O(n), total = O(1000 * n)
```

With prefix sum - finding sum of range [L, R] requires O(1) time:
```java
// Preprocess once: O(n)
int[] prefix = computePrefixSum(arr);

// Query: O(1)
int rangeSum(int[] prefix, int L, int R) {
    if (L == 0) return prefix[R];
    return prefix[R] - prefix[L - 1];
}
// 1000 queries = O(1000 * 1) = O(1000)
```

### Real-World Use Cases

1. **Leetcode "Range Sum Query"** - Database range queries
2. **Image Processing** - Sum of pixel values in sub-regions
3. **Game Development** - Sum of damage/healing in time ranges
4. **Financial Analysis** - Stock price ranges, transaction sums
5. **Prefix Count** - Count occurrences in prefix

---

## Basic Implementation

### 1D Prefix Sum

```java
int[] computePrefixSum(int[] arr) {
    int n = arr.length;
    int[] prefix = new int[n];
    prefix[0] = arr[0];

    for (int i = 1; i < n; i++) {
        prefix[i] = prefix[i - 1] + arr[i];
    }
    return prefix;
}

// Dry run: arr = [3, 7, 2, 8, 5, 9]
// prefix[0] = 3
// prefix[1] = 3 + 7 = 10
// prefix[2] = 10 + 2 = 12
// prefix[3] = 12 + 8 = 20
// prefix[4] = 20 + 5 = 25
// prefix[5] = 25 + 9 = 34
// Result: [3, 10, 12, 20, 25, 34]
```

### Handling Edge Cases

```java
int[] computePrefixSumSafe(int[] arr) {
    if (arr == null || arr.length == 0) {
        return new int[0];
    }

    int n = arr.length;
    int[] prefix = new int[n];
    prefix[0] = arr[0];

    for (int i = 1; i < n; i++) {
        prefix[i] = prefix[i - 1] + arr[i];
    }
    return prefix;
}
```

---

## Range Sum Queries

### Query Formula

```java
// Sum of range [L, R] (inclusive)
// prefix[R] = arr[0] + arr[1] + ... + arr[R]
// prefix[L-1] = arr[0] + arr[1] + ... + arr[L-1]
// prefix[R] - prefix[L-1] = arr[L] + ... + arr[R]

int rangeSum(int[] prefix, int L, int R) {
    if (L == 0) {
        return prefix[R];
    }
    return prefix[R] - prefix[L - 1];
}

// Example: arr = [3, 7, 2, 8, 5, 9], prefix = [3, 10, 12, 20, 25, 34]
// rangeSum(prefix, 2, 4) = 12 + 20 + 25 + 34 - 12 = ?
// Actually: prefix[4] - prefix[1] = 25 - 10 = 15
// Check: arr[2] + arr[3] + arr[4] = 2 + 8 + 5 = 15 ✓
```

### Prefix Sum Class (Range Query Data Structure)

```java
class PrefixSum {
    private int[] prefix;

    public PrefixSum(int[] arr) {
        if (arr == null || arr.length == 0) {
            this.prefix = new int[0];
            return;
        }

        int n = arr.length;
        this.prefix = new int[n];
        this.prefix[0] = arr[0];

        for (int i = 1; i < n; i++) {
            this.prefix[i] = this.prefix[i - 1] + arr[i];
        }
    }

    // Query sum from index L to R (inclusive)
    public int query(int L, int R) {
        if (L < 0 || R >= prefix.length || L > R) {
            throw new IllegalArgumentException("Invalid range");
        }

        if (L == 0) {
            return prefix[R];
        }
        return prefix[R] - prefix[L - 1];
    }

    // Query sum from 0 to R
    public int queryTo(int R) {
        return query(0, R);
    }
}
```

---

## Variations of Prefix Sum

### 1. Prefix Sum with Modulo

```java
// When dealing with circular sums or modulo arithmetic
int[] computePrefixSumMod(int[] arr, int mod) {
    int n = arr.length;
    int[] prefix = new int[n];
    prefix[0] = ((arr[0] % mod) + mod) % mod;  // Handle negative

    for (int i = 1; i < n; i++) {
        prefix[i] = (prefix[i - 1] + arr[i]) % mod;
        if (prefix[i] < 0) prefix[i] += mod;   // Handle negative
    }
    return prefix;
}
```

### 2. 2D Prefix Sum

```java
// For matrix range sum queries
class PrefixSum2D {
    private int[][] prefix;

    public PrefixSum2D(int[][] matrix) {
        if (matrix == null || matrix.length == 0) return;

        int m = matrix.length;
        int n = matrix[0].length;

        prefix = new int[m][n];
        prefix[0][0] = matrix[0][0];

        // First row
        for (int j = 1; j < n; j++) {
            prefix[0][j] = prefix[0][j - 1] + matrix[0][j];
        }

        // First column
        for (int i = 1; i < m; i++) {
            prefix[i][0] = prefix[i - 1][0] + matrix[i][0];
        }

        // Rest of the matrix
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                prefix[i][j] = matrix[i][j] + prefix[i - 1][j]
                             + prefix[i][j - 1] - prefix[i - 1][j - 1];
            }
        }
    }

    // Query sum of rectangle from (row1, col1) to (row2, col2)
    public int query(int row1, int col1, int row2, int col2) {
        if (row1 == 0 && col1 == 0) {
            return prefix[row2][col2];
        }
        if (row1 == 0) {
            return prefix[row2][col2] - prefix[row2][col1 - 1];
        }
        if (col1 == 0) {
            return prefix[row2][col2] - prefix[row1 - 1][col2];
        }
        return prefix[row2][col2] - prefix[row1 - 1][col2]
             - prefix[row2][col1 - 1] + prefix[row1 - 1][col1 - 1];
    }
}

/*
Matrix:
  [1, 2, 3]
  [4, 5, 6]
  [7, 8, 9]

Prefix Sum Matrix:
  [1,  3,  6]
  [5, 12, 21]
  [12, 27, 45]

Query rectangle (1,1) to (2,2):
= 45 - 12 - 5 + 1 = 29
= 5 + 6 + 8 + 9 = 28 (Wait, let's recalculate)
Actually: 5 + 6 + 8 + 9 = 28
*/
```

### 3. Difference Array (Prefix Sum Inverse)

```java
// When you need to apply multiple range updates efficiently
// Instead of updating each element in range O(n), use difference array O(1)

class DifferenceArray {
    private int[] diff;

    // Initialize diff array
    public DifferenceArray(int[] arr) {
        int n = arr.length;
        diff = new int[n];
        diff[0] = arr[0];
        for (int i = 1; i < n; i++) {
            diff[i] = arr[i] - arr[i - 1];
        }
    }

    // Apply range increment [L, R] by val - O(1)
    public void rangeIncrement(int L, int R, int val) {
        diff[L] += val;
        if (R + 1 < diff.length) {
            diff[R + 1] -= val;
        }
    }

    // Get the final array after all increments
    public int[] getFinalArray() {
        int[] result = new int[diff.length];
        result[0] = diff[0];
        for (int i = 1; i < diff.length; i++) {
            result[i] = result[i - 1] + diff[i];
        }
        return result;
    }
}

/*
Example:
Initial: [0, 0, 0, 0, 0]
diff:    [0, 0, 0, 0, 0]

rangeIncrement(1, 3, 5):
diff[1] += 5  -> diff = [0, 5, 0, 0, 0]
diff[4] -= 5  -> diff = [0, 5, 0, 0, -5]  (if 4 < length)

rangeIncrement(2, 4, 2):
diff[2] += 2  -> diff = [0, 5, 2, 0, -5]
diff[5] -= 2  -> Index 5 out of bounds, ignore

Final: [0, 5, 7, 7, 2]
*/
```

### 4. Prefix Sum for Boolean/Existence

```java
// When you need to check if all elements in range satisfy condition
class PrefixBoolean {
    private int[] prefix;

    public PrefixBoolean(boolean[] arr) {
        int n = arr.length;
        prefix = new int[n];
        prefix[0] = arr[0] ? 1 : 0;

        for (int i = 1; i < n; i++) {
            prefix[i] = prefix[i - 1] + (arr[i] ? 1 : 0);
        }
    }

    // Count true values in range [L, R]
    public int countTrue(int L, int R) {
        if (L == 0) return prefix[R];
        return prefix[R] - prefix[L - 1];
    }

    // Check if all true in range [L, R]
    public boolean allTrue(int L, int R) {
        int count = countTrue(L, R);
        return count == (R - L + 1);
    }
}
```

---

## Common Problems

### Problem 1: Running Sum of 1D Array

```java
// LeetCode 1480: Running Sum of 1D Array
// Simple prefix sum problem

public int[] runningSum(int[] nums) {
    int[] result = new int[nums.length];
    result[0] = nums[0];

    for (int i = 1; i < nums.length; i++) {
        result[i] = result[i - 1] + nums[i];
    }
    return result;
}

// Input: [1, 2, 3, 4]
// Output: [1, 3, 6, 10]
```

### Problem 2: Maximum Subarray Sum with Prefix Sum

```java
// Find maximum subarray sum using prefix sum
int maxSubarraySum(int[] arr) {
    int maxSum = Integer.MIN_VALUE;
    int minPrefix = 0;
    int prefixSum = 0;

    for (int i = 0; i < arr.length; i++) {
        prefixSum += arr[i];

        // Max sum = prefixSum - minPrefix seen so far
        maxSum = Math.max(maxSum, prefixSum - minPrefix);

        // Update minimum prefix
        minPrefix = Math.min(minPrefix, prefixSum);
    }
    return maxSum;
}
```

### Problem 3: Find Pivot Index

```java
// LeetCode 724: Find Pivot Index
// Where left sum equals right sum

public int pivotIndex(int[] nums) {
    int totalSum = 0;
    for (int num : nums) totalSum += num;

    int leftSum = 0;
    for (int i = 0; i < nums.length; i++) {
        // rightSum = totalSum - leftSum - nums[i]
        if (leftSum == totalSum - leftSum - nums[i]) {
            return i;
        }
        leftSum += nums[i];
    }
    return -1;
}

// Dry run: [1, 7, 3, 6, 5, 6]
// total = 28
// i=0: left=0, right=28-0-1=27, not equal
// i=1: left=1, right=28-1-7=20, not equal
// i=2: left=8, right=28-8-3=17, not equal
// i=3: left=11, right=28-11-6=11, EQUAL! return 3
```

### Problem 4: Subarray Sum Equals K

```java
// LeetCode 560: Subarray Sum Equals K
// Count number of continuous subarrays whose sum equals k

public int subarraySum(int[] nums, int k) {
    int count = 0;
    int prefixSum = 0;

    // HashMap to store frequency of prefix sums
    HashMap<Integer, Integer> map = new HashMap<>();
    map.put(0, 1);  // For subarrays starting from index 0

    for (int num : nums) {
        prefixSum += num;

        // If (prefixSum - k) exists, we found subarray with sum k
        if (map.containsKey(prefixSum - k)) {
            count += map.get(prefixSum - k);
        }

        map.put(prefixSum, map.getOrDefault(prefixSum, 0) + 1);
    }
    return count;
}

// Dry run: nums = [1, 1, 1], k = 2
// prefix=0: map[0]=1
// prefix=1: map[1-2] = map[-1]? no. map[1]=1. map={0:1, 1:1}
// prefix=2: map[2-2] = map[0]=1, count=1. map[2]=1. map={0:1, 1:1, 2:1}
// prefix=3: map[3-2] = map[1]=1, count=2. map[3]=1
// Answer: 2 ([1,1] at index 0-1 and 1-2)
```

### Problem 5: Product of Array Except Self

```java
// LeetCode 238: Product of Array Except Self
// Use prefix and suffix products

public int[] productExceptSelf(int[] nums) {
    int n = nums.length;
    int[] result = new int[n];

    // Prefix product
    result[0] = 1;
    for (int i = 1; i < n; i++) {
        result[i] = result[i - 1] * nums[i - 1];
    }

    // Suffix product (multiply from right)
    int suffix = 1;
    for (int i = n - 1; i >= 0; i--) {
        result[i] *= suffix;
        suffix *= nums[i];
    }
    return result;
}

// Dry run: [1, 2, 3, 4]
// Prefix pass: result = [1, 1, 2, 6]
// Suffix pass:
// i=3: result[3] = 6*1 = 6, suffix = 4
// i=2: result[2] = 2*4 = 8, suffix = 12
// i=1: result[1] = 1*12 = 12, suffix = 24
// i=0: result[0] = 1*24 = 24
// Result: [24, 12, 8, 6]
```

### Problem 6: Maximum Size Subarray Sum Equals K

```java
// LeetCode 325: Maximum Size Subarray Sum Equals K

public int maxSubArrayLen(int[] nums, int k) {
    int maxLen = 0;
    int prefixSum = 0;

    HashMap<Integer, Integer> map = new HashMap<>();
    map.put(0, -1);  // For subarray starting from index 0

    for (int i = 0; i < nums.length; i++) {
        prefixSum += nums[i];

        if (map.containsKey(prefixSum - k)) {
            maxLen = Math.max(maxLen, i - map.get(prefixSum - k));
        }

        // Only store first occurrence (for longest subarray)
        if (!map.containsKey(prefixSum)) {
            map.put(prefixSum, i);
        }
    }
    return maxLen;
}
```

---

## Java Implementations

### Complete Prefix Sum Implementation

```java
import java.util.*;

class PrefixSumArray {
    private int[] prefix;
    private int[] original;

    public PrefixSumArray(int[] arr) {
        this.original = arr.clone();
        computePrefixSum();
    }

    private void computePrefixSum() {
        int n = original.length;
        prefix = new int[n];
        if (n == 0) return;

        prefix[0] = original[0];
        for (int i = 1; i < n; i++) {
            prefix[i] = prefix[i - 1] + original[i];
        }
    }

    // Range sum query [L, R] inclusive
    public int rangeSum(int L, int R) {
        validateRange(L, R);
        if (L == 0) return prefix[R];
        return prefix[R] - prefix[L - 1];
    }

    // Total sum of array
    public int totalSum() {
        if (prefix.length == 0) return 0;
        return prefix[prefix.length - 1];
    }

    // Sum from beginning to index
    public int sumTo(int R) {
        return rangeSum(0, R);
    }

    // Find if there exists subarray with sum k
    public boolean hasSubarrayWithSum(int k) {
        HashSet<Integer> set = new HashSet<>();
        set.add(0);

        for (int i = 0; i < original.length; i++) {
            int current = prefix[i];
            if (set.contains(current - k)) {
                return true;
            }
            set.add(current);
        }
        return false;
    }

    // Count subarrays with sum k
    public int countSubarraysWithSum(int k) {
        int count = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);

        for (int i = 0; i < original.length; i++) {
            int current = prefix[i];
            count += map.getOrDefault(current - k, 0);
            map.put(current, map.getOrDefault(current, 0) + 1);
        }
        return count;
    }

    private void validateRange(int L, int R) {
        if (L < 0 || R >= original.length || L > R) {
            throw new IllegalArgumentException("Invalid range: [" + L + ", " + R + "]");
        }
    }

    // Debug print
    public void printArrays() {
        System.out.println("Original: " + Arrays.toString(original));
        System.out.println("Prefix:   " + Arrays.toString(prefix));
    }
}
```

---

## Time and Space Complexity

| Operation | Time | Space |
|-----------|------|-------|
| Build Prefix Sum | O(n) | O(n) |
| Range Sum Query | O(1) | O(1) |
| m Range Queries | O(n + m) | O(n) |

### Comparison with Other Approaches

| Approach | Build | Each Query | m Queries |
|----------|-------|-----------|----------|
| Naive | O(1) | O(n) | O(n×m) |
| Prefix Sum | O(n) | O(1) | O(n + m) |
| Segment Tree | O(n) | O(log n) | O(n + m log n) |
| Fenwick Tree | O(n log n)* | O(log n) | O(n log n + m log n) |

*Can be O(n) with proper initialization

---

## Key Takeaways

1. **Preprocessing is Key**: Spend O(n) upfront to answer queries in O(1)
2. **Formula**: Range sum [L, R] = prefix[R] - prefix[L-1] (or just prefix[R] if L=0)
3. **Variations**: 2D prefix sum, difference array, prefix boolean, etc.
4. **HashMap + Prefix Sum**: Powerful combination for subarray sum problems
5. **Space Tradeoff**: O(n) space for O(1) query time

---

## Practice Problems

1. **LeetCode 1480** - Running Sum of 1D Array
2. **LeetCode 724** - Find Pivot Index
3. **LeetCode 560** - Subarray Sum Equals K
4. **LeetCode 238** - Product of Array Except Self
5. **LeetCode 304** - Range Sum Query 2D - Immutable
6. **LeetCode 528** - Random Pick with Weight
7. **LeetCode 437** - Path Sum III
8. **LeetCode 325** - Maximum Size Subarray Sum Equals K

---

> **Remember**: Prefix sum transforms O(n) queries into O(1) by preprocessing once. It's one of the most powerful optimization techniques in array problems.
