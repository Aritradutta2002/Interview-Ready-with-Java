# Arrays - Complete Guide

## Table of Contents
1. [Introduction to Arrays](#introduction-to-arrays)
2. [Array Basics in Java](#array-basics-in-java)
3. [Array Types](#array-types)
4. [Array Operations](#array-operations)
5. [Time and Space Complexity](#time-and-space-complexity)
6. [Common Patterns](#common-patterns)
7. [Array Problems & Solutions](#array-problems--solutions)
8. [Java Array Utilities](#java-array-utilities)
9. [Best Practices](#best-practices)
10. [Common Pitfalls](#common-pitfalls)

---

## Introduction to Arrays

An **array** is a contiguous block of memory that stores elements of the same type. It's the most fundamental data structure in programming and serves as the building block for many other data structures.

### Why Arrays Matter

1. **Fast Access**: O(1) access by index
2. **Cache Friendly**: Contiguous memory enables CPU cache optimization
3. **Foundation**: Many other data structures built on arrays
4. **Versatile**: Used in 80% of coding problems

### Real-World Analogy

```
Array = Hotel Rooms in a Row

Room 101  Room 102  Room 103  Room 104  Room 105
   [0]       [1]       [2]       [3]       [4]

- Each room has a sequential number (index)
- You can directly go to room 103 without passing 101, 102
- All rooms are the same size (same data type)
- Rooms are next to each other (contiguous memory)
```

---

## Array Basics in Java

### Declaration and Initialization

```java
// Method 1: Declaration with size
int[] arr1 = new int[5];           // Array of 5 integers, all 0
String[] arr2 = new String[3];     // Array of 3 Strings, all null
boolean[] arr3 = new boolean[4];   // Array of 4 booleans, all false

// Method 2: Declaration with values (inline initialization)
int[] arr4 = {1, 2, 3, 4, 5};      // Size inferred as 5
String[] arr5 = {"apple", "banana", "cherry"};

// Method 3: Declaration then initialization
int[] arr6;
arr6 = new int[]{1, 2, 3};        // Anonymous array

// Method 4: Using Arrays class
int[] arr7 = new int[]{1, 2, 3, 4, 5};

// Common mistake to avoid:
int[] arr8 = new int[5]{1, 2, 3, 4, 5};  // COMPILE ERROR!
```

### Array as Objects

In Java, arrays are **objects** stored in heap memory:

```java
int[] arr = {1, 2, 3, 4, 5};

// Array properties
arr.length       // 5 (public final field, not a method!)
arr.hashCode()   // Memory address hash
arr.clone()      // Creates shallow copy
```

### Memory Representation

```
int[] arr = {10, 20, 30, 40, 50};

Memory (Heap):
┌─────────┬─────────┬─────────┬─────────┬─────────┐
│   10    │   20    │   30    │   40    │   50    │
└─────────┴─────────┴─────────┴─────────┴─────────┘
    [0]       [1]       [2]       [3]       [4]

arr reference variable points to the first element's address
```

---

## Array Types

### 1. Single Dimensional Array

```java
int[] arr = new int[5];
arr[0] = 10;
arr[1] = 20;
```

### 2. Two Dimensional Array

```java
// Declaration
int[][] matrix = new int[3][4];        // 3 rows, 4 columns
int[][] matrix2 = {{1,2}, {3,4}};      // 2x2 matrix

// Jagged array (rows of different lengths)
int[][] jagged = new int[3][];
jagged[0] = new int[2];   // Row 0 has 2 columns
jagged[1] = new int[4];   // Row 1 has 4 columns
jagged[2] = new int[1];   // Row 2 has 1 column

// Accessing elements
matrix[0][0] = 1;          // First row, first column
matrix[2][3] = 5;         // Third row, fourth column

// Memory representation:
┌────────┬────────┬────────┬────────┐
│ [0][0] │ [0][1] │ [0][2] │ [0][3] │
├────────┼────────┼────────┼────────┤
│ [1][0] │ [1][1] │ [1][2] │ [1][3] │
├────────┼────────┼────────┼────────┤
│ [2][0] │ [2][1] │ [2][2] │ [2][3] │
└────────┴────────┴────────┴────────┘
```

### 3. Three Dimensional Array

```java
int[][][] 3DArray = new int[2][3][4];  // 2 layers, 3 rows, 4 columns

// Useful for: 3D games, image processing (RGB), etc.
```

### 4. Primitive vs Reference Arrays

```java
// Primitive types - store actual values
int[] intArr = {1, 2, 3};
int[] intArrCopy = intArr.clone();
intArrCopy[0] = 100;
// intArr[0] is still 1 (independent copy)

// Reference types - store object references
String[] strArr = {"apple", "banana"};
String[] strArrCopy = strArr.clone();
strArrCopy[0] = "cherry";
// strArr[0] is still "apple" (shallow copy for primitives)
// BUT for objects, clone is shallow!
```

---

## Array Operations

### 1. Accessing Elements

```java
int[] arr = {10, 20, 30, 40, 50};

int first = arr[0];     // O(1) - 10
int last = arr[4];      // O(1) - 50
int middle = arr[2];    // O(1) - 30

// Out of bounds - throws ArrayIndexOutOfBoundsException
// int invalid = arr[10];  // Runtime error!
```

### 2. Updating Elements

```java
int[] arr = new int[5];

arr[0] = 100;           // O(1)
arr[4] = 500;            // O(1)
arr[2] = 300;            // O(1)
```

### 3. Searching

```java
int[] arr = {5, 3, 8, 1, 9, 2};

// Linear Search - O(n)
int linearSearch(int[] arr, int target) {
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] == target) return i;
    }
    return -1;
}

// Binary Search (sorted array) - O(log n)
int binarySearch(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (arr[mid] == target) return mid;
        else if (arr[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    return -1;
}
```

### 4. Insertion

```java
// Insert at end - O(1) amortized (ArrayList behavior)
int[] arr = {1, 2, 3, 4, 5};
// To insert at end, you'd need to create new array

// Insert at beginning - O(n)
int[] insertAtBeginning(int[] arr, int element) {
    int[] newArr = new int[arr.length + 1];
    newArr[0] = element;
    for (int i = 0; i < arr.length; i++) {
        newArr[i + 1] = arr[i];
    }
    return newArr;
}

// Insert at index - O(n)
int[] insertAtIndex(int[] arr, int index, int element) {
    int[] newArr = new int[arr.length + 1];
    for (int i = 0; i < index; i++) newArr[i] = arr[i];
    newArr[index] = element;
    for (int i = index; i < arr.length; i++) newArr[i + 1] = arr[i];
    return newArr;
}
```

### 5. Deletion

```java
// Delete at index - O(n)
int[] deleteAtIndex(int[] arr, int index) {
    int[] newArr = new int[arr.length - 1];
    for (int i = 0; i < index; i++) newArr[i] = arr[i];
    for (int i = index + 1; i < arr.length; i++) newArr[i - 1] = arr[i];
    return newArr;
}
```

### 6. Traversal

```java
int[] arr = {1, 2, 3, 4, 5};

// Forward traversal
for (int i = 0; i < arr.length; i++) {
    System.out.print(arr[i] + " ");  // 1 2 3 4 5
}

// Enhanced for loop
for (int num : arr) {
    System.out.print(num + " ");     // 1 2 3 4 5
}

// Backward traversal
for (int i = arr.length - 1; i >= 0; i--) {
    System.out.print(arr[i] + " ");  // 5 4 3 2 1
}
```

---

## Time and Space Complexity

### Time Complexity for Array Operations

| Operation | Time Complexity | Notes |
|-----------|-----------------|-------|
| Access by index | O(1) | Direct calculation |
| Search (unsorted) | O(n) | Linear scan |
| Search (sorted) | O(log n) | Binary search |
| Insert at end | O(1)* | Amortized |
| Insert at index | O(n) | Shift elements |
| Delete at index | O(n) | Shift elements |
| Update | O(1) | Direct access |

*ArrayList has amortized O(1) for add(), raw array needs resize

### Space Complexity

| Operation | Space Complexity |
|-----------|-----------------|
| Creating array | O(n) |
| Searching | O(1) |
| Sorting in-place | O(1) |
| Sorting with new array | O(n) |

---

## Common Patterns

### Pattern 1: Two Pointers

```java
// Useful for: Pair problems, palindrome, sorted array problems

// Example: Check if array has pair with given sum (sorted array)
boolean hasPairWithSum(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    while (left < right) {
        int sum = arr[left] + arr[right];
        if (sum == target) return true;
        else if (sum < target) left++;
        else right--;
    }
    return false;
}
```

### Pattern 2: Sliding Window

```java
// Useful for: Subarray problems, substring problems

// Example: Maximum sum subarray of size k
int maxSumSubarray(int[] arr, int k) {
    int windowSum = 0;
    for (int i = 0; i < k; i++) windowSum += arr[i];

    int maxSum = windowSum;
    for (int i = k; i < arr.length; i++) {
        windowSum += arr[i] - arr[i - k];
        maxSum = Math.max(maxSum, windowSum);
    }
    return maxSum;
}
```

### Pattern 3: Prefix Sum

```java
// Useful for: Range sum queries

int[] prefixSum(int[] arr) {
    int[] ps = new int[arr.length];
    ps[0] = arr[0];
    for (int i = 1; i < arr.length; i++) {
        ps[i] = ps[i - 1] + arr[i];
    }
    return ps;
}

// Query range sum [l, r] in O(1)
int rangeSum(int[] ps, int l, int r) {
    if (l == 0) return ps[r];
    return ps[r] - ps[l - 1];
}
```

### Pattern 4: Kadane's Algorithm

```java
// Maximum subarray sum (can have negative numbers)
int maxSubarraySum(int[] arr) {
    int maxSum = arr[0];
    int currentSum = arr[0];

    for (int i = 1; i < arr.length; i++) {
        currentSum = Math.max(arr[i], currentSum + arr[i]);
        maxSum = Math.max(maxSum, currentSum);
    }
    return maxSum;
}
```

### Pattern 5: Merge Overlapping Intervals

```java
// Sort and merge overlapping intervals
int[][] mergeIntervals(int[][] intervals) {
    if (intervals.length <= 1) return intervals;

    Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

    List<int[]> merged = new ArrayList<>();
    int[] current = intervals[0];

    for (int i = 1; i < intervals.length; i++) {
        if (current[1] >= intervals[i][0]) {
            current[1] = Math.max(current[1], intervals[i][1]);
        } else {
            merged.add(current);
            current = intervals[i];
        }
    }
    merged.add(current);
    return merged.toArray(new int[merged.size()][]);
}
```

---

## Array Problems & Solutions

### Problem 1: Remove Duplicates from Sorted Array

```java
// LeetCode 26: Remove Duplicates from Sorted Array
// Return the number of unique elements

// Approach 1: Two pointers
int removeDuplicates(int[] nums) {
    if (nums.length == 0) return 0;

    int slow = 0;
    for (int fast = 1; fast < nums.length; fast++) {
        if (nums[fast] != nums[slow]) {
            slow++;
            nums[slow] = nums[fast];
        }
    }
    return slow + 1;
}

// dry run: [1,1,2,2,3,3,3,4,4]
// slow=0, fast=1: 1!=1? no
// slow=0, fast=2: 2!=1? yes, slow=1, nums[1]=2
// slow=1, fast=3: 2!=2? no
// slow=1, fast=4: 3!=2? yes, slow=2, nums[2]=3
// result: [1,2,3,...], return 3
```

### Problem 2: Best Time to Buy and Sell Stock

```java
// LeetCode 121: Best Time to Buy and Sell Stock
// One transaction allowed

int maxProfit(int[] prices) {
    if (prices.length == 0) return 0;

    int minPrice = prices[0];
    int maxProfit = 0;

    for (int i = 1; i < prices.length; i++) {
        maxProfit = Math.max(maxProfit, prices[i] - minPrice);
        minPrice = Math.min(minPrice, prices[i]);
    }
    return maxProfit;
}
```

### Problem 3: Rotate Array

```java
// LeetCode 189: Rotate Array
// Rotate to the right by k steps

// Approach: Reverse approach
void rotate(int[] nums, int k) {
    k = k % nums.length;
    if (k == 0) return;

    reverse(nums, 0, nums.length - 1);      // Reverse all
    reverse(nums, 0, k - 1);                 // Reverse first k
    reverse(nums, k, nums.length - 1);        // Reverse rest
}

void reverse(int[] nums, int left, int right) {
    while (left < right) {
        int temp = nums[left];
        nums[left] = nums[right];
        nums[right] = temp;
        left++;
        right--;
    }
}
```

### Problem 4: Container With Most Water

```java
// LeetCode 11: Container With Most Water
// Two pointer approach

int maxArea(int[] height) {
    int left = 0, right = height.length - 1;
    int maxArea = 0;

    while (left < right) {
        int h = Math.min(height[left], height[right]);
        int w = right - left;
        maxArea = Math.max(maxArea, h * w);

        if (height[left] < height[right]) {
            left++;
        } else {
            right--;
        }
    }
    return maxArea;
}
```

### Problem 5: Find the Missing Number

```java
// LeetCode 268: Find the Missing Number
// XOR approach - O(n) time, O(1) space

int missingNumber(int[] nums) {
    int missing = nums.length;  // Start with n
    for (int i = 0; i < nums.length; i++) {
        missing ^= i ^ nums[i];
    }
    return missing;
    // XOR properties: a^a=0, a^0=a, commutative, associative
    // If array is [3,0,1], missing = 4^0^3^1^2^3^0^1 = 2
}

// Alternative: Sum formula - O(n) time, O(1) space
int missingNumberSum(int[] nums) {
    int expectedSum = nums.length * (nums.length + 1) / 2;
    int actualSum = 0;
    for (int num : nums) actualSum += num;
    return expectedSum - actualSum;
}
```

### Problem 6: Majority Element

```java
// LeetCode 169: Majority Element
// Boyer-Moore Voting Algorithm - O(n) time, O(1) space

int majorityElement(int[] nums) {
    int count = 0;
    Integer candidate = null;

    for (int num : nums) {
        if (count == 0) {
            candidate = num;
        }
        count += (num == candidate) ? 1 : -1;
    }
    return candidate;
}
```

---

## Java Array Utilities

### Arrays Class Methods

```java
import java.util.Arrays;

int[] arr = {5, 2, 8, 1, 9, 3};

// Sort in ascending order
Arrays.sort(arr);                    // [1, 2, 3, 5, 8, 9]

// Sort range
Arrays.sort(arr, 1, 4);              // Sort index 1 to 3

// Binary search (array must be sorted)
int index = Arrays.binarySearch(arr, 5);  // Returns index or negative

// Fill array
int[] filled = new int[5];
Arrays.fill(filled, 10);             // [10, 10, 10, 10, 10]

// Copy array
int[] copy = Arrays.copyOf(arr, arr.length * 2);  // Can specify new length
int[] copyRange = Arrays.copyOfRange(arr, 1, 4);   // Copy index 1 to 3

// Compare arrays
boolean equal = Arrays.equals(arr1, arr2);  // Compare contents

// toString
String str = Arrays.toString(arr);    // "[1, 2, 3, 5, 8, 9]"

// deepToString for 2D arrays
int[][] matrix = {{1,2}, {3,4}};
String matrixStr = Arrays.deepToString(matrix);
```

### ArrayList vs Raw Array

```java
import java.util.ArrayList;

// Raw array - fixed size
int[] arr = new int[5];
arr[0] = 1;  // Can't dynamically add more

// ArrayList - dynamic size
ArrayList<Integer> list = new ArrayList<>();
list.add(1);          // [1]
list.add(2);          // [1, 2]
list.add(0, 0);       // [0, 1, 2] - insert at index
list.remove(0);       // [1, 2] - remove at index
list.get(0);          // 1 - get at index
list.set(0, 10);      // [10, 2] - set at index
list.size();          // 2
list.contains(1);     // true
list.indexOf(2);     // 1
```

---

## Best Practices

### 1. Always Check Bounds

```java
// BAD - may throw ArrayIndexOutOfBoundsException
public int getElement(int[] arr, int index) {
    return arr[index];
}

// GOOD - with bounds checking
public Integer getElement(int[] arr, int index) {
    if (index < 0 || index >= arr.length) {
        return null;  // Or throw exception
    }
    return arr[index];
}
```

### 2. Use Enhanced For Loop When Index Not Needed

```java
// When you don't need the index
for (int num : arr) {
    process(num);
}

// When you need the index
for (int i = 0; i < arr.length; i++) {
    process(arr[i], i);
}
```

### 3. Prefer ArrayList for Dynamic Size

```java
// If size is fixed - use array
int[] coordinates = new int[3];

// If size changes - use ArrayList
ArrayList<Integer> dynamicList = new ArrayList<>();
```

### 4. Clone vs CopyOf

```java
int[] original = {1, 2, 3};

// clone() - creates shallow copy
int[] clone = original.clone();

// copyOf() - can change size
int[] copy = Arrays.copyOf(original, original.length + 1);
```

### 5. Be Careful with 2D Array Initialization

```java
// This creates 3 rows with 0 columns each
int[][] wrong = new int[3][0];  // Wrong!

// This is correct for 3x4 matrix
int[][] correct = new int[3][4];

// For jagged array
int[][] jagged = new int[3][];
jagged[0] = new int[4];
jagged[1] = new int[4];
jagged[2] = new int[4];
```

---

## Common Pitfalls

### 1. Off-by-One Errors

```java
int[] arr = {1, 2, 3, 4, 5};

// Wrong: arr[5] causes ArrayIndexOutOfBoundsException
for (int i = 0; i <= arr.length; i++) { }  // BAD!

// Correct:
for (int i = 0; i < arr.length; i++) { }   // GOOD
```

### 2. Modifying Array While Iterating

```java
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));

// ConcurrentModificationException if you remove while iterating
for (int i = 0; i < list.size(); i++) {
    if (list.get(i) % 2 == 0) {
        list.remove(i);  // BAD - use iterator or iterate backwards
    }
}

// Better approach - iterator
Iterator<Integer> it = list.iterator();
while (it.hasNext()) {
    if (it.next() % 2 == 0) {
        it.remove();  // GOOD
    }
}
```

### 3. Not Handling Empty Arrays

```java
// Always check for empty arrays
public void process(int[] arr) {
    if (arr == null || arr.length == 0) {
        return;  // Handle gracefully
    }
    // Process...
}
```

### 4. Integer Overflow

```java
// When calculating middle index
int mid = (left + right) / 2;        // Can overflow if left, right are large!
int mid = left + (right - left) / 2; // SAFE - always use this

// When multiplying
int product = a * b;  // Can overflow!
if (a != 0 && b > Integer.MAX_VALUE / a) {
    // Handle overflow
}
```

### 5. Confusion Between Length and Size

```java
int[] arr = {1, 2, 3};

arr.length          // For arrays - it's a FIELD, not a method
arrList.size()      // For ArrayList - it's a METHOD

String str = "hello";
str.length()        // For String - it's a METHOD
```

---

## Practice Problems

### Easy
1. Two Sum (LeetCode 1)
2. Reverse String (LeetCode 344)
3. Contains Duplicate (LeetCode 217)
4. Maximum Subarray (LeetCode 53)
5. Move Zeroes (LeetCode 283)

### Medium
1. 3Sum (LeetCode 15)
2. Container With Most Water (LeetCode 11)
3. Spiral Matrix (LeetCode 54)
4. Set Matrix Zeroes (LeetCode 73)
5. Find Minimum in Rotated Sorted Array (LeetCode 153)

### Hard
1. Median of Two Sorted Arrays (LeetCode 4)
2. Trapping Rain Water (LeetCode 42)
3. First Missing Positive (LeetCode 41)
4. Sliding Window Maximum (LeetCode 239)
5. Merge Intervals (LeetCode 56)

---

## Summary

| Concept | Key Points |
|---------|------------|
| **Array Definition** | Contiguous memory, same type, fixed size |
| **Access Time** | O(1) - direct index calculation |
| **Search Time** | O(n) linear, O(log n) binary (sorted) |
| **Insert/Delete** | O(n) - shifting elements |
| **Java Arrays** | Object in heap, length is final field |
| **Key Patterns** | Two pointers, sliding window, prefix sum |
| **Common Pitfalls** | Off-by-one, bounds checking, overflow |

---

> **Remember**: Arrays are the building blocks. Master array manipulation techniques thoroughly before moving to more complex data structures.
