# Binary Search — A to Z (Basics to CP Level) in Java

## Table of Contents
1. [What is Binary Search?](#1-what-is-binary-search)
2. [Prerequisites and When to Use](#2-prerequisites-and-when-to-use)
3. [How Binary Search Works — Visual Walkthrough](#3-how-binary-search-works--visual-walkthrough)
4. [Iterative Implementation](#4-iterative-implementation)
5. [Recursive Implementation](#5-recursive-implementation)
6. [Time and Space Complexity Analysis](#6-time-and-space-complexity-analysis)
7. [Common Pitfalls and Edge Cases](#7-common-pitfalls-and-edge-cases)
8. [The 5 Core Templates](#8-the-5-core-templates)
9. [Lower Bound and Upper Bound](#9-lower-bound-and-upper-bound)
10. [First and Last Occurrence](#10-first-and-last-occurrence)
11. [Count Occurrences of a Number](#11-count-occurrences-of-a-number)
12. [Search Insert Position](#12-search-insert-position)
13. [Floor and Ceiling of a Number](#13-floor-and-ceiling-of-a-number)
14. [Peak Element in Array](#14-peak-element-in-array)
15. [Search in Rotated Sorted Array](#15-search-in-rotated-sorted-array)
16. [Find Minimum in Rotated Sorted Array](#16-find-minimum-in-rotated-sorted-array)
17. [Single Element in Sorted Array](#17-single-element-in-sorted-array)
18. [Search a 2D Matrix](#18-search-a-2d-matrix)
19. [Binary Search on Answer — Theory](#19-binary-search-on-answer--theory)
20. [Koko Eating Bananas](#20-koko-eating-bananas)
21. [Capacity to Ship Packages](#21-capacity-to-ship-packages)
22. [Aggressive Cows / Magnetic Force](#22-aggressive-cows--magnetic-force)
23. [Split Array Largest Sum — Painter's Partition](#23-split-array-largest-sum--painters-partition)
24. [Median of Two Sorted Arrays](#24-median-of-two-sorted-arrays)
25. [Kth Smallest in Sorted Matrix](#25-kth-smallest-in-sorted-matrix)
26. [Advanced CP-Level Patterns](#26-advanced-cp-level-patterns)
27. [Binary Search on Real Numbers (Doubles)](#27-binary-search-on-real-numbers-doubles)
28. [Binary Search on Monotonic Functions](#28-binary-search-on-monotonic-functions)
29. [Ternary Search vs Binary Search](#29-ternary-search-vs-binary-search)
30. [Java Built-in Binary Search APIs](#30-java-built-in-binary-search-apis)
31. [Interview Cheat Sheet](#31-interview-cheat-sheet)
32. [Top 30 Interview Questions — Quick Fire](#32-top-30-interview-questions--quick-fire)

---

## 1. What is Binary Search?

Binary Search is a **divide-and-conquer** algorithm that finds a target in a **sorted** (or monotonic) search space by repeatedly halving the range.

```
Linear Search:  [1, 3, 5, 7, 9, 11, 13, 15]  → Check one by one → O(n)
Binary Search:  [1, 3, 5, 7, 9, 11, 13, 15]  → Halve each time  → O(log n)

For n = 1,000,000:
  Linear Search → 1,000,000 operations (worst case)
  Binary Search → ~20 operations (log₂ 1,000,000 ≈ 20)
```

**Key Insight:** Every comparison eliminates **half** of the remaining search space.

---

## 2. Prerequisites and When to Use

### When Binary Search Applies
1. **Sorted array** — classic use case
2. **Monotonic function** — if `f(x)` goes from false to true (or vice versa), we can binary search the boundary
3. **Search space is orderable** — we can define "go left" or "go right" at each step
4. **Optimization problems** — "find the minimum X such that condition holds" (Binary Search on Answer)

### When NOT to Use
- Unsorted data without any monotonic property
- When you need ALL occurrences (though you can combine BS with other techniques)
- Linked lists (no O(1) random access → O(n log n) total)

---

## 3. How Binary Search Works — Visual Walkthrough

**Target = 7 in array [1, 3, 5, 7, 9, 11, 13]**

```
Step 1: low=0, high=6, mid=3 → arr[3]=7 == target → FOUND!

But let's show a miss first. Target = 11:

Step 1: low=0, high=6, mid=3
        [1, 3, 5, 7, 9, 11, 13]
                   ^
        arr[3]=7 < 11 → go RIGHT → low = 4

Step 2: low=4, high=6, mid=5
        [1, 3, 5, 7, 9, 11, 13]
                          ^
        arr[5]=11 == 11 → FOUND at index 5!
```

**Target = 6 (not present):**
```
Step 1: low=0, high=6, mid=3 → arr[3]=7 > 6  → high = 2
Step 2: low=0, high=2, mid=1 → arr[1]=3 < 6  → low  = 2
Step 3: low=2, high=2, mid=2 → arr[2]=5 < 6  → low  = 3
Step 4: low=3, high=2 → low > high → NOT FOUND, return -1
```

---

## 4. Iterative Implementation

```java
/**
 * Classic Binary Search — Iterative
 * Returns index of target, or -1 if not found.
 * Time: O(log n) | Space: O(1)
 */
public static int binarySearch(int[] arr, int target) {
    int low = 0, high = arr.length - 1;

    while (low <= high) {
        int mid = low + (high - low) / 2;   // avoids integer overflow

        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] < target) {
            low = mid + 1;
        } else {
            high = mid - 1;
        }
    }

    return -1;  // target not found
}
```

### Why `low + (high - low) / 2` and NOT `(low + high) / 2`?
```
If low = 2,000,000,000 and high = 2,000,000,000:
  (low + high) = 4,000,000,000 → OVERFLOWS int (max ~2.1 billion)
  low + (high - low) / 2 = 2,000,000,000 → SAFE
```

---

## 5. Recursive Implementation

```java
/**
 * Classic Binary Search — Recursive
 * Time: O(log n) | Space: O(log n) due to call stack
 */
public static int binarySearchRecursive(int[] arr, int target, int low, int high) {
    if (low > high) return -1;   // base case: not found

    int mid = low + (high - low) / 2;

    if (arr[mid] == target) {
        return mid;
    } else if (arr[mid] < target) {
        return binarySearchRecursive(arr, target, mid + 1, high);
    } else {
        return binarySearchRecursive(arr, target, low, mid - 1);
    }
}

// Usage:
// int idx = binarySearchRecursive(arr, target, 0, arr.length - 1);
```

### Iterative vs Recursive

```
┌────────────┬──────────────────┬──────────────────┐
│            │ Iterative        │ Recursive        │
├────────────┼──────────────────┼──────────────────┤
│ Space      │ O(1)             │ O(log n)         │
│ Speed      │ Slightly faster  │ Function overhead│
│ Readability│ Clean loops      │ Elegant          │
│ Interview  │ ✅ Preferred     │ ✅ Accepted      │
│ CP / Prod  │ ✅ Always use    │ Avoid deep calls │
└────────────┴──────────────────┴──────────────────┘
```

---

## 6. Time and Space Complexity Analysis

```
Each step: search space is halved.
n → n/2 → n/4 → n/8 → ... → 1

Number of steps k:  n / 2^k = 1  →  k = log₂(n)

┌─────────────────────┬───────────┬────────────┐
│ Operation           │ Time      │ Space      │
├─────────────────────┼───────────┼────────────┤
│ Standard BS         │ O(log n)  │ O(1)       │
│ Recursive BS        │ O(log n)  │ O(log n)   │
│ BS on answer + check│ O(n·logM) │ O(1)       │
│ BS on 2D matrix     │ O(log(mn))│ O(1)       │
│ Median of 2 arrays  │ O(log min)│ O(1)       │
└─────────────────────┴───────────┴────────────┘

Where M = size of answer search space, m×n = matrix dimensions
```

### Comparison with Other Searches
```
┌──────────────────┬────────────┬───────────────┐
│ Algorithm        │ Time       │ Requires Sort │
├──────────────────┼────────────┼───────────────┤
│ Linear Search    │ O(n)       │ No            │
│ Binary Search    │ O(log n)   │ Yes           │
│ Jump Search      │ O(√n)      │ Yes           │
│ Interpolation    │ O(log logn)│ Yes + Uniform │
│ Exponential      │ O(log n)   │ Yes           │
│ Ternary Search   │ O(log n)   │ Unimodal      │
└──────────────────┴────────────┴───────────────┘
```

---

## 7. Common Pitfalls and Edge Cases

### Pitfall 1: Integer Overflow
```java
// ❌ BAD
int mid = (low + high) / 2;

// ✅ GOOD
int mid = low + (high - low) / 2;
```

### Pitfall 2: Infinite Loop with `low < high`
```java
// ❌ Can infinite loop when low == high - 1
while (low < high) {
    int mid = low + (high - low) / 2;
    // if we set low = mid (not mid+1), loop never terminates
    low = mid;     // BUG!
}

// ✅ Use mid + 1 or switch to right-biased mid
int mid = low + (high - low + 1) / 2;  // right-biased
```

### Pitfall 3: Off-by-one on Boundaries
```java
// Always ask yourself:
// 1. Should I use low <= high or low < high?
// 2. Should mid be included or excluded on the next iteration?
// 3. What does low point to when the loop ends?
```

### Edge Cases to Always Test
```
1. Empty array: arr.length == 0
2. Single element: arr = [5]
3. Target smaller than all elements
4. Target larger than all elements
5. All elements identical: arr = [7,7,7,7,7]
6. Target at first position
7. Target at last position
8. Two elements: arr = [1, 2]
```

---

## 8. The 5 Core Templates

### Template 1: Exact Match (`low <= high`)
```java
// Find exact target. Returns -1 if not found.
// Loop ends when low > high (search space empty).
int low = 0, high = n - 1;
while (low <= high) {
    int mid = low + (high - low) / 2;
    if (arr[mid] == target) return mid;
    else if (arr[mid] < target) low = mid + 1;
    else high = mid - 1;
}
return -1;
```

### Template 2: Left Boundary / Lower Bound (`low < high`)
```java
// Find first index where arr[index] >= target
// Loop ends when low == high (converges to answer).
int low = 0, high = n;  // note: high = n (not n-1)
while (low < high) {
    int mid = low + (high - low) / 2;
    if (arr[mid] < target) low = mid + 1;
    else high = mid;
}
return low;  // first position >= target
```

### Template 3: Right Boundary / Upper Bound
```java
// Find first index where arr[index] > target
int low = 0, high = n;
while (low < high) {
    int mid = low + (high - low) / 2;
    if (arr[mid] <= target) low = mid + 1;
    else high = mid;
}
return low;  // first position > target
```

### Template 4: Binary Search on Answer (Minimize)
```java
// Find minimum value in [lo, hi] such that isValid(value) == true
int lo = MIN_POSSIBLE, hi = MAX_POSSIBLE;
while (lo < hi) {
    int mid = lo + (hi - lo) / 2;
    if (isValid(mid)) hi = mid;       // mid could be answer, search left
    else lo = mid + 1;                // mid too small
}
return lo;
```

### Template 5: Binary Search on Answer (Maximize)
```java
// Find maximum value in [lo, hi] such that isValid(value) == true
int lo = MIN_POSSIBLE, hi = MAX_POSSIBLE;
while (lo < hi) {
    int mid = lo + (hi - lo + 1) / 2;   // RIGHT-biased mid (crucial!)
    if (isValid(mid)) lo = mid;          // mid could be answer, search right
    else hi = mid - 1;                   // mid too big
}
return lo;
```

### Template Decision Flowchart
```
Do you need an exact match?
  YES → Template 1 (low <= high)
  NO ↓
Are you finding a boundary (first/last position)?
  First occurrence / lower bound → Template 2
  Last occurrence / upper bound  → Template 3
  NO ↓
Binary search on answer?
  Minimize something → Template 4
  Maximize something → Template 5
```

---

## 9. Lower Bound and Upper Bound

These are the building blocks for almost every advanced Binary Search problem.

```java
/**
 * Lower Bound: First index where arr[i] >= target
 * If target not present, returns the insertion point.
 */
public static int lowerBound(int[] arr, int target) {
    int low = 0, high = arr.length;
    while (low < high) {
        int mid = low + (high - low) / 2;
        if (arr[mid] < target) low = mid + 1;
        else high = mid;
    }
    return low;
}

/**
 * Upper Bound: First index where arr[i] > target
 */
public static int upperBound(int[] arr, int target) {
    int low = 0, high = arr.length;
    while (low < high) {
        int mid = low + (high - low) / 2;
        if (arr[mid] <= target) low = mid + 1;
        else high = mid;
    }
    return low;
}
```

### Visual Example
```
arr = [1, 3, 3, 3, 5, 7, 9], target = 3

lowerBound(arr, 3) → 1   (first index where arr[i] >= 3)
upperBound(arr, 3) → 4   (first index where arr[i] > 3)

Number of 3's = upperBound - lowerBound = 4 - 1 = 3 ✅
```

---

## 10. First and Last Occurrence

> **Leetcode 34 — Find First and Last Position of Element in Sorted Array**

```java
/**
 * Time: O(log n) | Space: O(1)
 * Two binary searches: one for first, one for last occurrence.
 */
public static int[] searchRange(int[] nums, int target) {
    int first = findFirst(nums, target);
    if (first == -1) return new int[]{-1, -1};
    int last = findLast(nums, target);
    return new int[]{first, last};
}

private static int findFirst(int[] nums, int target) {
    int low = 0, high = nums.length - 1, result = -1;
    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (nums[mid] == target) {
            result = mid;
            high = mid - 1;      // keep searching left
        } else if (nums[mid] < target) {
            low = mid + 1;
        } else {
            high = mid - 1;
        }
    }
    return result;
}

private static int findLast(int[] nums, int target) {
    int low = 0, high = nums.length - 1, result = -1;
    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (nums[mid] == target) {
            result = mid;
            low = mid + 1;       // keep searching right
        } else if (nums[mid] < target) {
            low = mid + 1;
        } else {
            high = mid - 1;
        }
    }
    return result;
}
```

---

## 11. Count Occurrences of a Number

```java
/**
 * Count how many times target appears in sorted array.
 * Time: O(log n) | Space: O(1)
 */
public static int countOccurrences(int[] arr, int target) {
    int first = findFirst(arr, target);
    if (first == -1) return 0;
    int last = findLast(arr, target);
    return last - first + 1;
}

// OR using lower/upper bound:
public static int countOccurrences2(int[] arr, int target) {
    return upperBound(arr, target) - lowerBound(arr, target);
}
```

---

## 12. Search Insert Position

> **Leetcode 35 — Search Insert Position**

```java
/**
 * Return index of target, or where it would be inserted.
 * This is exactly lowerBound.
 * Time: O(log n) | Space: O(1)
 */
public static int searchInsert(int[] nums, int target) {
    int low = 0, high = nums.length;
    while (low < high) {
        int mid = low + (high - low) / 2;
        if (nums[mid] < target) low = mid + 1;
        else high = mid;
    }
    return low;
}

// Example: nums = [1,3,5,6], target = 5 → return 2
// Example: nums = [1,3,5,6], target = 2 → return 1
// Example: nums = [1,3,5,6], target = 7 → return 4
```

---

## 13. Floor and Ceiling of a Number

```java
/**
 * Floor: Largest element <= target
 * Ceiling: Smallest element >= target
 */
public static int floor(int[] arr, int target) {
    int low = 0, high = arr.length - 1, result = -1;
    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (arr[mid] <= target) {
            result = arr[mid];     // candidate for floor
            low = mid + 1;
        } else {
            high = mid - 1;
        }
    }
    return result;
}

public static int ceiling(int[] arr, int target) {
    int low = 0, high = arr.length - 1, result = -1;
    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (arr[mid] >= target) {
            result = arr[mid];     // candidate for ceiling
            high = mid - 1;
        } else {
            low = mid + 1;
        }
    }
    return result;
}

// arr = [1, 2, 8, 10, 12, 19], target = 5
// floor(5)   → 2
// ceiling(5) → 8
```

---

## 14. Peak Element in Array

> **Leetcode 162 — Find Peak Element**

A peak element is strictly greater than its neighbors.

```java
/**
 * Binary Search on slope direction.
 * If arr[mid] < arr[mid+1] → peak is to the RIGHT
 * If arr[mid] > arr[mid+1] → peak is to the LEFT (or mid itself)
 * Time: O(log n) | Space: O(1)
 */
public static int findPeakElement(int[] nums) {
    int low = 0, high = nums.length - 1;

    while (low < high) {
        int mid = low + (high - low) / 2;
        if (nums[mid] < nums[mid + 1]) {
            low = mid + 1;     // peak is on the right
        } else {
            high = mid;        // peak is on the left or mid itself
        }
    }

    return low;  // low == high, that's the peak index
}
```

**Why does this work?**
```
The array boundaries are considered -∞.
If you are at a point going uphill (mid < mid+1), a peak MUST exist to the right.
If going downhill (mid > mid+1), a peak MUST exist to the left (or at mid).
```

---

## 15. Search in Rotated Sorted Array

> **Leetcode 33 — Search in Rotated Sorted Array (no duplicates)**

```
Original:  [1, 2, 3, 4, 5, 6, 7]
Rotated:   [4, 5, 6, 7, 1, 2, 3]   (rotated at index 4)
```

```java
/**
 * One side is always sorted. Determine which side, then decide.
 * Time: O(log n) | Space: O(1)
 */
public static int search(int[] nums, int target) {
    int low = 0, high = nums.length - 1;

    while (low <= high) {
        int mid = low + (high - low) / 2;

        if (nums[mid] == target) return mid;

        // Left half is sorted
        if (nums[low] <= nums[mid]) {
            if (target >= nums[low] && target < nums[mid]) {
                high = mid - 1;   // target is in left sorted half
            } else {
                low = mid + 1;    // target is in right half
            }
        }
        // Right half is sorted
        else {
            if (target > nums[mid] && target <= nums[high]) {
                low = mid + 1;    // target is in right sorted half
            } else {
                high = mid - 1;   // target is in left half
            }
        }
    }

    return -1;
}
```

> **Leetcode 81 — with duplicates:** Add a check: when `nums[low] == nums[mid] == nums[high]`, do `low++; high--` (worst case O(n), but average O(log n)).

```java
// Handle duplicates — add this at the start of the while loop:
if (nums[low] == nums[mid] && nums[mid] == nums[high]) {
    low++;
    high--;
    continue;
}
```

---

## 16. Find Minimum in Rotated Sorted Array

> **Leetcode 153 — No duplicates**

```java
/**
 * The minimum is the only element where arr[i] < arr[i-1].
 * Binary search: compare mid with high.
 * Time: O(log n) | Space: O(1)
 */
public static int findMin(int[] nums) {
    int low = 0, high = nums.length - 1;

    while (low < high) {
        int mid = low + (high - low) / 2;

        if (nums[mid] > nums[high]) {
            low = mid + 1;     // min is in right half
        } else {
            high = mid;        // min could be mid
        }
    }

    return nums[low];
}
```

> **Leetcode 154 — With duplicates:** When `nums[mid] == nums[high]`, we can't decide. Simply do `high--`.

```java
public static int findMinWithDups(int[] nums) {
    int low = 0, high = nums.length - 1;
    while (low < high) {
        int mid = low + (high - low) / 2;
        if (nums[mid] > nums[high]) low = mid + 1;
        else if (nums[mid] < nums[high]) high = mid;
        else high--;   // can't determine, shrink by 1
    }
    return nums[low];
}
```

---

## 17. Single Element in Sorted Array

> **Leetcode 540 — Every element appears twice except one. Find it.**

```java
/**
 * Key insight: Before the single element, pairs are at (even, odd) indices.
 *              After the single element, pairs shift to (odd, even) indices.
 * Time: O(log n) | Space: O(1)
 */
public static int singleNonDuplicate(int[] nums) {
    int low = 0, high = nums.length - 1;

    while (low < high) {
        int mid = low + (high - low) / 2;

        // Ensure mid is even (start of a pair)
        if (mid % 2 == 1) mid--;

        if (nums[mid] == nums[mid + 1]) {
            low = mid + 2;    // pair is intact, single is on right
        } else {
            high = mid;       // pair is broken, single is on left or at mid
        }
    }

    return nums[low];
}

// Example: [1,1,2,3,3,4,4,8,8] → answer is 2
```

---

## 18. Search a 2D Matrix

> **Leetcode 74 — Rows are sorted, first element of each row > last of previous row**

```java
/**
 * Treat the 2D matrix as a virtual 1D sorted array.
 * Index mapping: row = idx / cols, col = idx % cols
 * Time: O(log(m*n)) | Space: O(1)
 */
public static boolean searchMatrix(int[][] matrix, int target) {
    int m = matrix.length, n = matrix[0].length;
    int low = 0, high = m * n - 1;

    while (low <= high) {
        int mid = low + (high - low) / 2;
        int val = matrix[mid / n][mid % n];

        if (val == target) return true;
        else if (val < target) low = mid + 1;
        else high = mid - 1;
    }

    return false;
}
```

> **Leetcode 240 — Search a 2D Matrix II (rows AND columns sorted independently):**
> Use staircase search starting from top-right corner — O(m + n), not pure binary search.

```java
public static boolean searchMatrixII(int[][] matrix, int target) {
    int row = 0, col = matrix[0].length - 1;
    while (row < matrix.length && col >= 0) {
        if (matrix[row][col] == target) return true;
        else if (matrix[row][col] < target) row++;
        else col--;
    }
    return false;
}
```

---

## 19. Binary Search on Answer — Theory

This is the **most important pattern** for interviews and CP. Instead of searching in an array, we search in a **range of possible answers**.

### When to Use
```
"Find the minimum/maximum X such that some condition holds."
"Minimize the maximum ..."
"Maximize the minimum ..."
"Is it possible to achieve X within constraint Y?"
```

### Framework
```
1. Define the search space: [lo, hi] of possible answers
2. Write a check(mid) function: "Is mid a valid/feasible answer?"
3. Binary search for the optimal boundary

if (check(mid)):
    // mid works → can we do better?
    // Minimization: search left  → hi = mid
    // Maximization: search right → lo = mid
else:
    // mid doesn't work
    // Minimization: search right → lo = mid + 1
    // Maximization: search left  → hi = mid - 1
```

### Visual: Monotonic Feasibility
```
Answer:    1    2    3    4    5    6    7    8    9    10
Feasible:  ✗    ✗    ✗    ✗    ✓    ✓    ✓    ✓    ✓    ✓
                              ^
                     First feasible answer = 5 (minimize)

Answer:    1    2    3    4    5    6    7    8    9    10
Feasible:  ✓    ✓    ✓    ✓    ✓    ✓    ✗    ✗    ✗    ✗
                                   ^
                     Last feasible answer = 6 (maximize)
```

---

## 20. Koko Eating Bananas

> **Leetcode 875** — Koko can eat at speed `k` bananas/hour. Find minimum `k` to finish all piles in `h` hours.

```java
/**
 * Search space: k ∈ [1, max(piles)]
 * Check: can Koko finish all piles at speed mid within h hours?
 * Time: O(n · log(max)) | Space: O(1)
 */
public static int minEatingSpeed(int[] piles, int h) {
    int lo = 1, hi = 0;
    for (int p : piles) hi = Math.max(hi, p);

    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        if (canFinish(piles, mid, h)) {
            hi = mid;       // mid works, try smaller
        } else {
            lo = mid + 1;   // mid too slow
        }
    }

    return lo;
}

private static boolean canFinish(int[] piles, int speed, int h) {
    long hours = 0;
    for (int p : piles) {
        hours += (p + speed - 1) / speed;  // ceiling division
    }
    return hours <= h;
}

// Example: piles = [3,6,7,11], h = 8
// lo=1, hi=11
// mid=6: hours = 1+1+2+2=6 ≤ 8 → hi=6
// mid=3: hours = 1+2+3+4=10 > 8  → lo=4
// mid=5: hours = 1+2+2+3=8 ≤ 8   → hi=5
// mid=4: hours = 1+2+2+3=8 ≤ 8   → hi=4
// lo==hi==4 → answer is 4
```

---

## 21. Capacity to Ship Packages

> **Leetcode 1010** — Find minimum ship capacity to ship all packages within `days` days.

```java
/**
 * Search space: [max(weights), sum(weights)]
 * Check: can we ship all with capacity mid in ≤ days?
 * Time: O(n · log(sum - max)) | Space: O(1)
 */
public static int shipWithinDays(int[] weights, int days) {
    int lo = 0, hi = 0;
    for (int w : weights) {
        lo = Math.max(lo, w);    // must carry the heaviest single package
        hi += w;                  // worst case: ship everything in 1 day
    }

    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        if (canShip(weights, mid, days)) {
            hi = mid;
        } else {
            lo = mid + 1;
        }
    }

    return lo;
}

private static boolean canShip(int[] weights, int capacity, int days) {
    int daysNeeded = 1, currentLoad = 0;
    for (int w : weights) {
        if (currentLoad + w > capacity) {
            daysNeeded++;
            currentLoad = 0;
        }
        currentLoad += w;
    }
    return daysNeeded <= days;
}
```

---

## 22. Aggressive Cows / Magnetic Force

> **Classic Problem / Leetcode 1552** — Place `k` cows in stalls to **maximize the minimum distance** between any two cows.

```java
/**
 * Search space: [1, max(positions) - min(positions)]
 * Check: can we place all cows with at least mid distance apart?
 * Time: O(n log n + n · log(range)) | Space: O(1)
 */
public static int maxMinDistance(int[] positions, int k) {
    Arrays.sort(positions);
    int lo = 1;
    int hi = positions[positions.length - 1] - positions[0];

    while (lo < hi) {
        int mid = lo + (hi - lo + 1) / 2;   // RIGHT-biased (maximization!)
        if (canPlace(positions, k, mid)) {
            lo = mid;           // mid works, try bigger
        } else {
            hi = mid - 1;       // mid too large
        }
    }

    return lo;
}

private static boolean canPlace(int[] positions, int k, int minDist) {
    int count = 1;                     // place first cow at positions[0]
    int lastPos = positions[0];

    for (int i = 1; i < positions.length; i++) {
        if (positions[i] - lastPos >= minDist) {
            count++;
            lastPos = positions[i];
            if (count >= k) return true;
        }
    }

    return false;
}

// Example: positions = [1,2,4,8,9], k = 3
// Sorted: [1,2,4,8,9]
// lo=1, hi=8
// mid=5: place at 1, skip 2, skip 4, place at 8 → only 2 cows → false → hi=4
// mid=3: place at 1, skip 2, place at 4, skip 8? no, 8-4=4≥3, place at 8 → 3 cows → true → lo=3
// mid=4: place at 1, skip 2, skip 4(4-1=3<4), place at 8 → 2 cows → false → hi=3
// lo==hi==3 → answer is 3
```

---

## 23. Split Array Largest Sum — Painter's Partition

> **Leetcode 410** — Split array into `k` subarrays to **minimize the largest subarray sum**.

```java
/**
 * Search space: [max(nums), sum(nums)]
 * Check: can we split into ≤ k parts where each part sum ≤ mid?
 * Time: O(n · log(sum - max)) | Space: O(1)
 */
public static int splitArray(int[] nums, int k) {
    int lo = 0, hi = 0;
    for (int num : nums) {
        lo = Math.max(lo, num);
        hi += num;
    }

    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        if (canSplit(nums, k, mid)) {
            hi = mid;          // mid works, try smaller
        } else {
            lo = mid + 1;      // need larger max sum
        }
    }

    return lo;
}

private static boolean canSplit(int[] nums, int k, int maxSum) {
    int parts = 1, currentSum = 0;
    for (int num : nums) {
        if (currentSum + num > maxSum) {
            parts++;
            currentSum = 0;
        }
        currentSum += num;
    }
    return parts <= k;
}

// Painter's Partition, Book Allocation are SAME pattern!
// "Minimize the maximum" → Binary Search on the maximum value
```

---

## 24. Median of Two Sorted Arrays

> **Leetcode 4 — Hard** — This is one of the hardest binary search problems.

```java
/**
 * Binary search on partition of the smaller array.
 * Time: O(log(min(m, n))) | Space: O(1)
 */
public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
    // Always binary search on the smaller array
    if (nums1.length > nums2.length) {
        return findMedianSortedArrays(nums2, nums1);
    }

    int x = nums1.length, y = nums2.length;
    int low = 0, high = x;

    while (low <= high) {
        int partX = (low + high) / 2;
        int partY = (x + y + 1) / 2 - partX;

        int maxLeftX  = (partX == 0) ? Integer.MIN_VALUE : nums1[partX - 1];
        int minRightX = (partX == x) ? Integer.MAX_VALUE : nums1[partX];
        int maxLeftY  = (partY == 0) ? Integer.MIN_VALUE : nums2[partY - 1];
        int minRightY = (partY == y) ? Integer.MAX_VALUE : nums2[partY];

        if (maxLeftX <= minRightY && maxLeftY <= minRightX) {
            // Found correct partition
            if ((x + y) % 2 == 0) {
                return (Math.max(maxLeftX, maxLeftY)
                      + Math.min(minRightX, minRightY)) / 2.0;
            } else {
                return Math.max(maxLeftX, maxLeftY);
            }
        } else if (maxLeftX > minRightY) {
            high = partX - 1;    // move partition left
        } else {
            low = partX + 1;     // move partition right
        }
    }

    throw new IllegalArgumentException("Input arrays are not sorted");
}
```

### How the Partition Works
```
nums1: [1, 3, | 8, 9]       partX = 2
nums2: [2, 4, 5, | 7, 10]   partY = 3

Left half:  1, 3, 2, 4, 5    (partX + partY = 5 elements)
Right half: 8, 9, 7, 10      (remaining)

Valid partition if:
  maxLeftX (3) <= minRightY (7)  ✅
  maxLeftY (5) <= minRightX (8)  ✅

Median (even total = 9? No, 9 is odd):
  = max(maxLeftX, maxLeftY) = max(3, 5) = 5
```

---

## 25. Kth Smallest in Sorted Matrix

> **Leetcode 378** — Matrix where each row and column is sorted. Find kth smallest element.

```java
/**
 * Binary search on value range [matrix[0][0], matrix[n-1][n-1]].
 * Count elements ≤ mid using staircase traversal.
 * Time: O(n · log(max - min)) | Space: O(1)
 */
public static int kthSmallest(int[][] matrix, int k) {
    int n = matrix.length;
    int lo = matrix[0][0], hi = matrix[n - 1][n - 1];

    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        int count = countLessOrEqual(matrix, mid, n);

        if (count < k) {
            lo = mid + 1;
        } else {
            hi = mid;
        }
    }

    return lo;
}

private static int countLessOrEqual(int[][] matrix, int target, int n) {
    int count = 0;
    int row = n - 1, col = 0;  // start bottom-left

    while (row >= 0 && col < n) {
        if (matrix[row][col] <= target) {
            count += row + 1;   // all elements above in this column qualify
            col++;
        } else {
            row--;
        }
    }

    return count;
}
```

---

## 26. Advanced CP-Level Patterns

### Pattern 1: Binary Search + Prefix Sums

> Find the minimum length subarray with sum ≥ target.

```java
/**
 * Build prefix sums, then for each starting point,
 * binary search for the endpoint where prefix[j] - prefix[i] >= target.
 * Time: O(n log n) | Space: O(n)
 * Note: This works only if all elements are positive (prefix array is sorted).
 */
public static int minSubArrayLen(int target, int[] nums) {
    int n = nums.length;
    long[] prefix = new long[n + 1];
    for (int i = 0; i < n; i++) prefix[i + 1] = prefix[i] + nums[i];

    int result = Integer.MAX_VALUE;
    for (int i = 0; i < n; i++) {
        // Find smallest j > i such that prefix[j] - prefix[i] >= target
        // i.e. prefix[j] >= prefix[i] + target
        long need = prefix[i] + target;
        int j = lowerBound(prefix, i + 1, n + 1, need);
        if (j <= n) {
            result = Math.min(result, j - i);
        }
    }

    return result == Integer.MAX_VALUE ? 0 : result;
}

private static int lowerBound(long[] arr, int lo, int hi, long target) {
    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        if (arr[mid] < target) lo = mid + 1;
        else hi = mid;
    }
    return lo;
}
```

### Pattern 2: Binary Search + Greedy Validation

This is the pattern behind Aggressive Cows, Split Array, etc.
```
Structure:
  1. Sort input if needed
  2. Define search range [lo, hi]
  3. Binary search: for each mid, run greedy check O(n)
  4. Total: O(n log(range))
```

### Pattern 3: Binary Search on Graphs — Shortest Path with Constraints

> "Find the minimum bottleneck edge weight on a path from source to destination."

```java
/**
 * Binary search on edge weight + BFS/DFS to check connectivity
 * using only edges ≤ mid weight.
 * Time: O(E · log(maxWeight))
 */
public static int minBottleneck(int n, int[][] edges, int src, int dst) {
    int lo = 0, hi = 1_000_000;  // range of edge weights

    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        if (canReach(n, edges, src, dst, mid)) {
            hi = mid;
        } else {
            lo = mid + 1;
        }
    }

    return lo;
}

private static boolean canReach(int n, int[][] edges, int src, int dst, int maxWeight) {
    List<List<int[]>> adj = new ArrayList<>();
    for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    for (int[] e : edges) {
        if (e[2] <= maxWeight) {
            adj.get(e[0]).add(new int[]{e[1], e[2]});
            adj.get(e[1]).add(new int[]{e[0], e[2]});
        }
    }

    boolean[] visited = new boolean[n];
    Queue<Integer> queue = new LinkedList<>();
    queue.add(src);
    visited[src] = true;
    while (!queue.isEmpty()) {
        int node = queue.poll();
        if (node == dst) return true;
        for (int[] nei : adj.get(node)) {
            if (!visited[nei[0]]) {
                visited[nei[0]] = true;
                queue.add(nei[0]);
            }
        }
    }
    return false;
}
```

### Pattern 4: Binary Search + Matrix / 2D Problems

> Count pairs with sum ≤ target in two sorted arrays.

```java
/**
 * For each element in arr1, binary search in arr2.
 * Time: O(m · log n)
 */
public static int countPairsWithSumAtMost(int[] arr1, int[] arr2, int target) {
    int count = 0;
    for (int a : arr1) {
        int maxB = target - a;
        count += upperBound(arr2, maxB);
    }
    return count;
}
```

### Pattern 5: Binary Search + Bit Manipulation (CP Level)

> Find the K-th smallest XOR pair.

```java
/**
 * Binary search on XOR value.
 * For each candidate XOR value mid, count pairs with XOR ≤ mid using Trie.
 * Time: O(n · 30 · log(maxXOR)) where 30 = number of bits
 *
 * Pseudocode (full trie implementation omitted for brevity):
 */
public static int kthSmallestXorPair(int[] nums, int k) {
    Arrays.sort(nums);
    int lo = 0, hi = (1 << 30) - 1;

    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        if (countXorPairsAtMost(nums, mid) >= k) {
            hi = mid;
        } else {
            lo = mid + 1;
        }
    }

    return lo;
}

// countXorPairsAtMost can be implemented with a Trie or offline approach
```

### Pattern 6: Fractional Binary Search (CP Level)

> Maximize average of a subarray of length ≥ k.

```java
/**
 * Binary search on the answer (the average value).
 * Check: does a subarray of length ≥ k with average ≥ mid exist?
 * Subtract mid from all elements → problem becomes:
 *   "does a subarray of length ≥ k with sum ≥ 0 exist?"
 * Use prefix sums + tracking min prefix.
 * Time: O(n · log(maxVal / eps))
 */
public static double maxAverage(int[] nums, int k) {
    double lo = -1e9, hi = 1e9;

    for (int iter = 0; iter < 100; iter++) {   // ~100 iterations for 1e-9 precision
        double mid = (lo + hi) / 2.0;
        if (hasSubarrayWithAvgAtLeast(nums, k, mid)) {
            lo = mid;
        } else {
            hi = mid;
        }
    }

    return lo;
}

private static boolean hasSubarrayWithAvgAtLeast(int[] nums, int k, double avg) {
    double[] adjusted = new double[nums.length];
    for (int i = 0; i < nums.length; i++) adjusted[i] = nums[i] - avg;

    double sum = 0, prevSum = 0, minPrev = 0;
    for (int i = 0; i < nums.length; i++) {
        sum += adjusted[i];
        if (i >= k - 1) {
            if (sum - minPrev >= 0) return true;
            prevSum += adjusted[i - k + 1];
            minPrev = Math.min(minPrev, prevSum);
        }
    }
    return false;
}
```

---

## 27. Binary Search on Real Numbers (Doubles)

When the search space is continuous (not integers), we iterate a fixed number of times instead of checking `lo < hi`.

```java
/**
 * Find square root of n using binary search on doubles.
 * Precision: ~1e-9
 */
public static double sqrt(double n) {
    double lo = 0, hi = Math.max(1, n);

    for (int i = 0; i < 100; i++) {   // 100 iterations → precision ~1e-30
        double mid = (lo + hi) / 2.0;
        if (mid * mid <= n) lo = mid;
        else hi = mid;
    }

    return lo;
}

/**
 * Find cube root of n.
 */
public static double cbrt(double n) {
    double lo = -1e9, hi = 1e9;

    for (int i = 0; i < 200; i++) {
        double mid = (lo + hi) / 2.0;
        if (mid * mid * mid <= n) lo = mid;
        else hi = mid;
    }

    return lo;
}
```

### Why Fixed Iterations?
```
Each iteration halves the range.
After 100 iterations: range = initial_range / 2^100 ≈ 1e-30
This is far more precise than any floating-point can represent.

Rule of thumb:
  - 100 iterations for most problems
  - 200 iterations if range is very large (1e18)
```

---

## 28. Binary Search on Monotonic Functions

If a function `f(x)` is monotonically increasing/decreasing, we can binary search for the crossover point.

```java
/**
 * Find minimum x in [lo, hi] such that f(x) >= target.
 * Requires: f is non-decreasing.
 */
public static int searchMonotonic(int lo, int hi, int target) {
    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        if (f(mid) < target) lo = mid + 1;
        else hi = mid;
    }
    return lo;
}
```

### Real-World Example: Nth Magical Number (Leetcode 878)

```java
/**
 * A magical number is divisible by a or b.
 * Find the nth magical number.
 * f(x) = count of magical numbers in [1, x] → monotonic!
 * Time: O(log(n * min(a,b)))
 */
public static int nthMagicalNumber(int n, int a, int b) {
    long MOD = 1_000_000_007;
    long lcm = (long) a / gcd(a, b) * b;
    long lo = 1, hi = (long) n * Math.min(a, b);

    while (lo < hi) {
        long mid = lo + (hi - lo) / 2;
        long count = mid / a + mid / b - mid / lcm;

        if (count < n) lo = mid + 1;
        else hi = mid;
    }

    return (int) (lo % MOD);
}

private static int gcd(int a, int b) {
    return b == 0 ? a : gcd(b, a % b);
}
```

---

## 29. Ternary Search vs Binary Search

Ternary search finds the maximum/minimum of a **unimodal** function (a function that increases then decreases, or vice versa).

```java
/**
 * Find x in [lo, hi] that maximizes f(x) (unimodal function).
 * Time: O(log n) per query, slightly worse constant than binary search.
 */
public static double ternarySearchMax(double lo, double hi) {
    for (int i = 0; i < 200; i++) {
        double m1 = lo + (hi - lo) / 3;
        double m2 = hi - (hi - lo) / 3;

        if (f(m1) < f(m2)) {
            lo = m1;      // max is not in [lo, m1]
        } else {
            hi = m2;      // max is not in [m2, hi]
        }
    }

    return (lo + hi) / 2;
}
```

### When to Use Which
```
┌────────────────┬─────────────────────┬──────────────────────┐
│                │ Binary Search       │ Ternary Search       │
├────────────────┼─────────────────────┼──────────────────────┤
│ Function type  │ Monotonic           │ Unimodal             │
│ Finds          │ Boundary/target     │ Max/Min of function  │
│ Comparisons    │ 1 per iteration     │ 2 per iteration      │
│ Convergence    │ n / 2^k             │ n / (3/2)^k          │
│ Practical use  │ More common in CP   │ Geometry, optimization│
└────────────────┴─────────────────────┴──────────────────────┘

Pro tip: Many ternary search problems can be converted to binary search by
searching on the derivative (slope): find where slope changes from +ve to -ve.
```

---

## 30. Java Built-in Binary Search APIs

### Arrays.binarySearch()
```java
import java.util.Arrays;

int[] arr = {1, 3, 5, 7, 9, 11};

// Returns index of target, or -(insertion point) - 1 if not found
int idx = Arrays.binarySearch(arr, 7);     // returns 3
int idx2 = Arrays.binarySearch(arr, 6);    // returns -3 (insertion point would be 3)

// On a range:
int idx3 = Arrays.binarySearch(arr, 1, 4, 5);  // search in [1, 4)
```

### Collections.binarySearch()
```java
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

List<Integer> list = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));
int idx = Collections.binarySearch(list, 5);   // returns 2
int idx2 = Collections.binarySearch(list, 4);  // returns -2
```

### Extracting Insertion Point
```java
int result = Arrays.binarySearch(arr, target);
int insertionPoint;
if (result >= 0) {
    insertionPoint = result;     // found
} else {
    insertionPoint = -(result + 1);  // not found, compute insertion point
}
```

### TreeSet / TreeMap — O(log n) Lookups with Binary Search Trees
```java
TreeSet<Integer> set = new TreeSet<>(Arrays.asList(1, 3, 5, 7, 9));

set.floor(6);     // 5 — largest element ≤ 6
set.ceiling(6);   // 7 — smallest element ≥ 6
set.lower(5);     // 3 — largest element < 5
set.higher(5);    // 7 — smallest element > 5
set.first();      // 1 — minimum
set.last();       // 9 — maximum
```

### Important Caveats
```
1. Arrays.binarySearch() requires a SORTED array. If unsorted → undefined behavior.
2. If duplicates exist, there's NO guarantee which index is returned.
3. For lower/upper bound, you MUST write your own — built-in doesn't support it.
4. In interviews, always write your own binary search (never use built-in).
```

---

## 31. Interview Cheat Sheet

### Quick Pattern Recognition
```
"Find target in sorted array"              → Classic BS (Template 1)
"Find first/last occurrence"               → Boundary BS (Template 2/3)
"Find insertion position"                  → Lower Bound (Template 2)
"Find minimum in rotated array"            → Compare mid with high
"Search in rotated array"                  → Identify sorted half
"Find peak element"                        → Compare mid with mid+1
"Minimize the maximum X"                   → BS on Answer (Template 4)
"Maximize the minimum X"                   → BS on Answer (Template 5)
"Koko / Ship / Cows / Painters"            → BS on Answer + Greedy check
"Median of two sorted arrays"              → BS on partition
"Kth smallest in matrix"                   → BS on value range
"Count pairs with condition"               → BS for each element
"Subarray with sum/avg condition"          → BS + Prefix sums
"Find in infinite sorted array"            → Exponential search + BS
```

### Complexity Quick Reference
```
┌──────────────────────────────┬────────────────┬───────────┐
│ Problem Type                 │ Time           │ Space     │
├──────────────────────────────┼────────────────┼───────────┤
│ Standard BS                  │ O(log n)       │ O(1)      │
│ BS on Answer                 │ O(n · log M)   │ O(1)      │
│ 2D Matrix BS                 │ O(log(m·n))    │ O(1)      │
│ Median of 2 arrays           │ O(log min(m,n))│ O(1)      │
│ BS + Prefix Sum              │ O(n log n)     │ O(n)      │
│ BS + BFS/DFS                 │ O(E · log W)   │ O(V+E)    │
│ BS on Doubles (100 iters)    │ O(100)         │ O(1)      │
└──────────────────────────────┴────────────────┴───────────┘
```

### Common Mistakes in Interviews
```
1. ❌ (low + high) / 2          → ✅ low + (high - low) / 2
2. ❌ Forgot to sort the array  → ✅ Always confirm sorted
3. ❌ Wrong loop condition       → ✅ <= for exact match, < for boundary
4. ❌ low = mid (infinite loop)  → ✅ low = mid + 1 or use right-biased mid
5. ❌ Wrong search range in BS on Answer
   → ✅ lo = min possible, hi = max possible
6. ❌ Using built-in in interviews → ✅ Write your own
7. ❌ Not handling empty array    → ✅ Check edge case first
8. ❌ Not considering duplicates  → ✅ Clarify with interviewer
```

### Template Selection Rules
```
ASK YOURSELF:
  1. Am I searching in an array or in an answer space?
     → Array: Templates 1-3
     → Answer space: Templates 4-5

  2. Do I need exact match or boundary?
     → Exact: Template 1 (low <= high)
     → Boundary: Template 2 or 3 (low < high)

  3. Am I minimizing or maximizing?
     → Minimize: Template 4, left-biased mid, hi = mid
     → Maximize: Template 5, right-biased mid, lo = mid

  4. What is my check function?
     → Greedy scan: O(n) per check → O(n log M) total
     → BFS/DFS: O(V+E) per check → O((V+E) log M) total
```

---

## 32. Top 30 Interview Questions — Quick Fire

### Beginner
| # | Problem | Key Idea |
|---|---------|----------|
| 1 | Binary Search (basic) | Standard template |
| 2 | Search Insert Position (LC 35) | Lower bound |
| 3 | First and Last Position (LC 34) | Two binary searches |
| 4 | Count Occurrences | upper - lower bound |
| 5 | Floor/Ceiling | Track best candidate |
| 6 | Sqrt(x) (LC 69) | BS on answer, i*i <= x |

### Intermediate
| # | Problem | Key Idea |
|---|---------|----------|
| 7 | Find Peak Element (LC 162) | Slope direction |
| 8 | Search in Rotated Array (LC 33) | Identify sorted half |
| 9 | Find Min in Rotated Array (LC 153) | Compare mid with high |
| 10 | Search 2D Matrix (LC 74) | Virtual 1D index |
| 11 | Single Element (LC 540) | Pair index parity |
| 12 | Rotated Array II (LC 81) | Handle duplicates |
| 13 | Find K Closest Elements (LC 658) | BS on window start |
| 14 | Time Based Key-Value (LC 981) | BS on timestamps |

### Advanced (Binary Search on Answer)
| # | Problem | Key Idea |
|---|---------|----------|
| 15 | Koko Eating Bananas (LC 875) | Min speed |
| 16 | Ship Packages (LC 1010) | Min capacity |
| 17 | Split Array Largest Sum (LC 410) | Min-max partition |
| 18 | Aggressive Cows / Magnetic Force | Max-min distance |
| 19 | Book Allocation | Same as Split Array |
| 20 | Painter's Partition | Same as Split Array |
| 21 | Minimize Max Products (LC 2064) | Min-max distribution |
| 22 | Nth Magical Number (LC 878) | BS + LCM |

### Hard / CP Level
| # | Problem | Key Idea |
|---|---------|----------|
| 23 | Median of Two Sorted Arrays (LC 4) | Partition based BS |
| 24 | Kth Smallest in Sorted Matrix (LC 378) | BS on value + count |
| 25 | Count Smaller After Self (LC 315) | BS + ordered set/BIT |
| 26 | Find Min in Rotated II (LC 154) | Duplicates, worst O(n) |
| 27 | Smallest Rect Black Pixels (LC 302) | BS in 2D grid |
| 28 | Max Average Subarray II (LC 644) | Fractional BS |
| 29 | Kth Smallest Pair Distance (LC 719) | BS + two pointer count |
| 30 | Russian Doll Envelopes (LC 354) | Sort + BS (LIS) |

---

## Bonus: Russian Doll Envelopes (BS + LIS)

> **Leetcode 354** — Sort by width ascending, then find LIS on heights (using BS).

```java
/**
 * Sort by width ASC; if same width, sort height DESC (to avoid using same width twice).
 * Then find LIS on heights using patience sorting (binary search).
 * Time: O(n log n) | Space: O(n)
 */
public static int maxEnvelopes(int[][] envelopes) {
    Arrays.sort(envelopes, (a, b) -> a[0] == b[0] ? b[1] - a[1] : a[0] - b[0]);

    // LIS on heights using BS
    List<Integer> dp = new ArrayList<>();

    for (int[] env : envelopes) {
        int h = env[1];
        int pos = Collections.binarySearch(dp, h);
        if (pos < 0) pos = -(pos + 1);

        if (pos == dp.size()) {
            dp.add(h);
        } else {
            dp.set(pos, h);
        }
    }

    return dp.size();
}

// Why sort height DESC for same width?
// Width: [3,3], [3,4], [3,5] — all same width, can't nest.
// If we sort height ASC: [3,3],[3,4],[3,5] → LIS picks 3,4,5 → WRONG
// If we sort height DESC: [3,5],[3,4],[3,3] → LIS picks only 1  → CORRECT
```

---

## Bonus: Kth Smallest Pair Distance (LC 719)

```java
/**
 * Binary search on distance value.
 * For each candidate distance mid, count pairs with distance ≤ mid.
 * Counting uses TWO POINTER (not nested BS) for O(n).
 * Time: O(n log n + n · log W) where W = max - min
 */
public static int smallestDistancePair(int[] nums, int k) {
    Arrays.sort(nums);
    int lo = 0, hi = nums[nums.length - 1] - nums[0];

    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        int count = countPairsWithinDist(nums, mid);

        if (count < k) lo = mid + 1;
        else hi = mid;
    }

    return lo;
}

private static int countPairsWithinDist(int[] nums, int maxDist) {
    int count = 0, left = 0;
    for (int right = 1; right < nums.length; right++) {
        while (nums[right] - nums[left] > maxDist) left++;
        count += right - left;
    }
    return count;
}
```

---

## Final Summary — The Binary Search Mindset

```
┌──────────────────────────────────────────────────────────────────┐
│                    THE BINARY SEARCH MINDSET                      │
├──────────────────────────────────────────────────────────────────┤
│                                                                    │
│  1. IDENTIFY the sorted / monotonic property                      │
│     → Can you split the space into YES/NO regions?                │
│                                                                    │
│  2. DEFINE the search space                                       │
│     → Array indices? Answer range? Value range?                   │
│                                                                    │
│  3. CHOOSE the right template                                     │
│     → Exact match? Boundary? Minimize? Maximize?                  │
│                                                                    │
│  4. WRITE the check function                                      │
│     → Greedy? BFS? Prefix sum? Simple comparison?                 │
│                                                                    │
│  5. HANDLE edge cases                                             │
│     → Empty input, all same, target out of range, duplicates      │
│                                                                    │
│  Binary Search is not just an algorithm.                          │
│  It's a THINKING TOOL for reducing O(n) to O(log n).             │
│                                                                    │
└──────────────────────────────────────────────────────────────────┘
```

---

*Happy Searching! 🔍*
