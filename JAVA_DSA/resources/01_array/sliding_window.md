# Sliding Window - Complete Guide

## Table of Contents
1. [What is Sliding Window?](#what-is-sliding-window)
2. [Why Sliding Window?](#why-sliding-window)
3. [Types of Sliding Window](#types-of-sliding-window)
4. [Fixed Window Size](#fixed-window-size)
5. [Variable Window Size](#variable-window-size)
6. [Common Problems](#common-problems)
7. [Advanced Techniques](#advanced-techniques)
8. [Java Implementations](#java-implementations)
9. [Time and Space Complexity](#time-and-space-complexity)

---

## What is Sliding Window?

The sliding window technique is a method for efficiently processing **subarrays or substrings** by maintaining a "window" that slides through the data structure. Instead of recalculating the entire window sum each time, we incrementally update it.

### Visual Explanation

```
Array: [1, 3, -1, -3, 5, 3, 6, 7]
Window size k = 3

Window slides from left to right:

Step 1: [1, 3, -1] → sum = 3
          └─────┘
Step 2:    [3, -1, -3] → sum = -1  (remove 1, add -3)
             └─────┘
Step 3:       [-1, -3, 5] → sum = 1  (remove 3, add 5)
                └─────┘
Step 4:          [-3, 5, 3] → sum = 5  (remove -1, add 3)
                   └─────┘
Step 5:             [5, 3, 6] → sum = 14  (remove -3, add 6)
                      └─────┘
Step 6:                [3, 6, 7] → sum = 16  (remove 5, add 7)
                         └─────┘

Instead of recalculating each time, we UPDATE:
New Sum = Old Sum - leftElement + newRightElement
```

---

## Why Sliding Window?

### The Naive Approach (O(n×k))

```java
int[] arr = {1, 3, -1, -3, 5, 3, 6, 7};
int k = 3;
int n = arr.length;

for (int i = 0; i <= n - k; i++) {
    int sum = 0;
    for (int j = i; j < i + k; j++) {  // O(k) for each window
        sum += arr[j];
    }
    // Use sum...
}
// Time: O(n × k) = O(8 × 3) = 24 operations
```

### Sliding Window Approach (O(n))

```java
int[] arr = {1, 3, -1, -3, 5, 3, 6, 7};
int k = 3;

// Calculate first window
int sum = arr[0] + arr[1] + arr[2];  // O(k)

// Slide the window
for (int i = k; i < n; i++) {
    sum = sum + arr[i] - arr[i - k];  // O(1) per slide!
    // Use sum...
}
// Time: O(k) + O(n-k) = O(n) = 11 operations
```

**Improvement**: From 24 operations to 11 operations!

---

## Types of Sliding Window

### 1. Fixed Size Window
- Window size `k` remains constant
- Window slides by removing one element and adding one

```
Original: [1, 3, -1, -3, 5, 3, 6, 7], k=3

Window position 1: [1, 3, -1] (indices 0-2)
Window position 2: [3, -1, -3] (indices 1-3)
Window position 3: [-1, -3, 5] (indices 2-4)
...
```

### 2. Variable Size Window
- Window size expands and contracts based on condition
- Used when finding longest/shortest subarray satisfying condition

```
Expand: Add elements to window
Contract: Remove elements from window left side
```

---

## Fixed Window Size

### Template

```java
// Fixed sliding window template
public ResultType fixedSlidingWindow(int[] arr, int k) {
    // 1. Calculate result for first window
    int windowSum = 0;
    for (int i = 0; i < k; i++) {
        windowSum += arr[i];
    }
    ResultType result = processWindow(windowSum);

    // 2. Slide the window
    for (int i = k; i < arr.length; i++) {
        // Remove element leaving window (arr[i-k])
        // Add element entering window (arr[i])
        windowSum = windowSum - arr[i - k] + arr[i];

        // Process new window
        result = updateResult(result, windowSum);
    }

    return result;
}
```

### Maximum Sum Subarray (Fixed Size K)

```java
// LeetCode 643: Maximum Average Subarray I
// Find maximum average of subarray with exactly k elements

public double findMaxAverage(int[] nums, int k) {
    // Calculate sum of first window
    int sum = 0;
    for (int i = 0; i < k; i++) {
        sum += nums[i];
    }

    int maxSum = sum;

    // Slide window
    for (int i = k; i < nums.length; i++) {
        sum = sum - nums[i - k] + nums[i];
        maxSum = Math.max(maxSum, sum);
    }

    return (double) maxSum / k;
}

// Dry run: nums=[1,12,-3,-5,6,7], k=3
// First window [1,12,-3]: sum=10, max=10
// Slide: remove 1, add 7 -> sum=10-1+(-5)=4, max=10
// Slide: remove 12, add 6 -> sum=4-12+6=-2, max=10
// Slide: remove -3, add 7 -> sum=-2-(-3)+7=8, max=10
// Result: 10/3 = 3.333...
```

### Maximum Sliding Window

```java
// LeetCode 239: Sliding Window Maximum
// Find maximum element in each sliding window of size k

public int[] maxSlidingWindow(int[] nums, int k) {
    if (nums == null || nums.length == 0) return new int[0];

    int n = nums.length;
    int[] result = new int[n - k + 1];
    int resultIndex = 0;

    // Deque stores indices, front = maximum
    Deque<Integer> deque = new LinkedList<>();

    for (int i = 0; i < n; i++) {
        // Remove indices outside current window
        while (!deque.isEmpty() && deque.peek() < i - k + 1) {
            deque.poll();
        }

        // Remove indices whose values are smaller than current element
        // (they'll never be the maximum while current element is in window)
        while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
            deque.pollLast();
        }

        // Add current index
        deque.offer(i);

        // Window is fully formed, record maximum
        if (i >= k - 1) {
            result[resultIndex++] = nums[deque.peek()];
        }
    }

    return result;
}

/*
Dry run: nums=[1,3,-1,-3,5,3,6,7], k=3

i=0: deque=[0], window not ready
i=1: deque=[1] (3>1, remove 0), window not ready
i=2: deque=[1,2] (-1<3, keep), window ready -> result[0]=3
i=3: deque=[1,3] (remove 2, -3<3), result[1]=3
i=4: deque=[4] (remove 1,3, keep 3, add 5), result[2]=5
i=5: deque=[4,5] (-3<5, remove -3), result[3]=5
i=6: deque=[6] (remove 5,3, add 6), result[4]=6
i=7: deque=[6,7] (7>6, keep), result[5]=7

Result: [3, 3, 5, 5, 6, 7]
*/
```

---

## Variable Window Size

### Template

```java
// Variable sliding window template
public ResultType variableSlidingWindow(int[] arr, /* conditions */) {
    int left = 0;
    ResultType result = initialize();

    for (int right = 0; right < arr.length; right++) {
        // Expand window by adding arr[right]
        addToWindow(arr[right]);

        // While window is invalid (condition violated)
        while (windowInvalid()) {
            // Contract from left
            removeFromWindow(arr[left]);
            left++;
        }

        // Window is now valid, update result
        updateResult();
    }

    return result;
}
```

### Longest Subarray with Sum at Most K

```java
// LeetCode 904: Fruit Into Baskets / Longest Subarray with Sum at Most K

public int longestSubarray(int[] arr, long k) {
    int left = 0;
    int maxLen = 0;
    long sum = 0;

    for (int right = 0; right < arr.length; right++) {
        // Expand window
        sum += arr[right];

        // Contract while exceeding k
        while (sum > k && left <= right) {
            sum -= arr[left];
            left++;
        }

        // Update result (window [left, right] is valid)
        maxLen = Math.max(maxLen, right - left + 1);
    }

    return maxLen;
}

// Dry run: arr=[1,2,3], k=3
// right=0: sum=1, window valid, maxLen=1
// right=1: sum=3, window valid, maxLen=2
// right=2: sum=6>3, contract: sum=5>3, left=1; sum=3, left=2
// window valid [2,2], maxLen=1
// Result: 2
```

### Minimum Size Subarray Sum

```java
// LeetCode 209: Minimum Size Subarray Sum
// Find minimum length contiguous subarray with sum >= target

public int minSubArrayLen(int target, int[] nums) {
    int left = 0;
    int minLen = Integer.MAX_VALUE;
    int sum = 0;

    for (int right = 0; right < nums.length; right++) {
        // Expand window
        sum += nums[right];

        // Contract while we can
        while (sum >= target && left <= right) {
            // Update result before contracting
            minLen = Math.min(minLen, right - left + 1);

            // Contract from left
            sum -= nums[left];
            left++;
        }
    }

    return minLen == Integer.MAX_VALUE ? 0 : minLen;
}

// Dry run: nums=[2,3,1,2,4,3], target=7
// right=0: sum=2<7
// right=1: sum=5<7
// right=2: sum=6<7
// right=3: sum=8>=7, minLen=4, sum=8-2=6, left=1
// right=4: sum=6+2=8>=7, minLen=3, sum=8-3=5, left=2
// right=5: sum=5+3=8>=7, minLen=2, sum=8-1=7, left=3
// left=3, sum>=7, minLen=2, sum=7-2=5, left=4
// left=4, sum<7, stop contracting
// Result: 2 (subarray [4,3])
```

### Longest Substring Without Repeating Characters

```java
// LeetCode 3: Longest Substring Without Repeating Characters

public int lengthOfLongestSubstring(String s) {
    if (s == null || s.length() == 0) return 0;

    int left = 0;
    int maxLen = 0;
    HashMap<Character, Integer> charIndex = new HashMap<>();

    for (int right = 0; right < s.length(); right++) {
        char c = s.charAt(right);

        // If character exists in current window, move left pointer
        // Note: only move left if the existing index is within window
        if (charIndex.containsKey(c) && charIndex.get(c) >= left) {
            left = charIndex.get(c) + 1;
        }

        // Update character's latest index
        charIndex.put(c, right);

        // Update maximum length
        maxLen = Math.max(maxLen, right - left + 1);
    }

    return maxLen;
}

// Dry run: s="abcabcbb"
// right=0: 'a', max=1, map={a:0}
// right=1: 'b', max=2, map={a:0,b:1}
// right=2: 'c', max=3, map={a:0,b:1,c:2}
// right=3: 'a', exists at 0>=0, left=1, map={a:3,b:1,c:2}
// right=4: 'b', exists at 1>=1, left=2, map={a:3,b:4,c:2}
// right=5: 'c', exists at 2>=2, left=3, map={a:3,b:4,c:5}
// right=6: 'b', exists at 4>=3, left=5, map={a:3,b:6,c:5}
// right=7: 'b', exists at 6>=5, left=7, map={a:3,b:7,c:5}
// maxLen = 3 (from right=2-left=0)
```

---

## Common Problems

### Problem 1: Average of Subarrays of Size K

```java
// LeetCode 566: Reshape the Matrix (similar concept)

public double[] findAverages(int K, int[] arr) {
    double[] result = new double[arr.length - K + 1];

    // Calculate first window sum
    double windowSum = 0;
    for (int i = 0; i < K; i++) {
        windowSum += arr[i];
    }
    result[0] = windowSum / K;

    // Slide window
    for (int i = K; i < arr.length; i++) {
        windowSum = windowSum - arr[i - K] + arr[i];
        result[i - K + 1] = windowSum / K;
    }

    return result;
}
```

### Problem 2: Maximum Sum of Any Subarray of Size K

```java
public int maxSum(int[] arr, int k) {
    if (arr.length < k) return -1;

    // Calculate first window
    int maxSum = 0;
    for (int i = 0; i < k; i++) {
        maxSum += arr[i];
    }

    // Current window sum
    int windowSum = maxSum;

    // Slide window
    for (int i = k; i < arr.length; i++) {
        windowSum = windowSum - arr[i - k] + arr[i];
        maxSum = Math.max(maxSum, windowSum);
    }

    return maxSum;
}
```

### Problem 3: Count Occurrences of Anagrams

```java
// LeetCode 567: Permutation in String
// Check if t's anagram exists in s

public boolean checkInclusion(String t, String s) {
    if (t.length() > s.length()) return false;

    // Count frequencies for t
    int[] targetCount = new int[26];
    for (char c : t.toCharArray()) {
        targetCount[c - 'a']++;
    }

    // Sliding window on s
    int[] windowCount = new int[26];
    int left = 0;
    int requiredMatches = t.length();

    for (int right = 0; right < s.length(); right++) {
        char c = s.charAt(right);
        windowCount[c - 'a']++;

        // If current char count exceeds target, shrink from left
        while (windowCount[c - 'a'] > targetCount[c - 'a']) {
            windowCount[s.charAt(left) - 'a']--;
            left++;
        }

        // Check if current window matches
        if (right - left + 1 == t.length()) {
            return true;
        }
    }

    return false;
}
```

### Problem 4: Sliding Window Median

```java
// LeetCode 480: Sliding Window Median
// Hard problem - use two heaps or balanced BST

public double[] medianSlidingWindow(int[] nums, int k) {
    double[] result = new double[nums.length - k + 1];

    // Using two heaps approach
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();

    for (int i = 0; i < nums.length; i++) {
        // Add element
        if (maxHeap.isEmpty() || nums[i] <= maxHeap.peek()) {
            maxHeap.offer(nums[i]);
        } else {
            minHeap.offer(nums[i]);
        }

        // Balance heaps
        if (maxHeap.size() > minHeap.size() + 1) {
            minHeap.offer(maxHeap.poll());
        } else if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }

        // Remove element if window size exceeded
        if (i >= k) {
            int toRemove = nums[i - k];
            if (toRemove <= maxHeap.peek()) {
                maxHeap.remove(toRemove);
            } else {
                minHeap.remove(toRemove);
            }
        }

        // Record median when window is ready
        if (i >= k - 1) {
            if (maxHeap.size() == minHeap.size()) {
                result[i - k + 1] = (maxHeap.peek() / 2.0) + (minHeap.peek() / 2.0);
            } else {
                result[i - k + 1] = maxHeap.peek() * 1.0;
            }
        }
    }

    return result;
}
```

---

## Advanced Techniques

### 1. Two Counters (Positive/Negative)

```java
// When dealing with problems requiring balance of two properties
// Example: Find subarray with equal number of 0s and 1s

public int findMaxLength(int[] nums) {
    // Convert 0 to -1, then problem becomes finding subarray with sum 0
    int maxLen = 0;
    int sum = 0;
    HashMap<Integer, Integer> map = new HashMap<>();
    map.put(0, -1);  // For subarray from start

    for (int i = 0; i < nums.length; i++) {
        sum += (nums[i] == 0) ? -1 : 1;

        if (map.containsKey(sum)) {
            maxLen = Math.max(maxLen, i - map.get(sum));
        } else {
            map.put(sum, i);
        }
    }
    return maxLen;
}
```

### 2. Prefix Sum + Sliding Window

```java
// When each element's contribution depends on its position
// Example: Maximum average subarray where we need sum/k

public double findMaxAverage(int[] nums, int k) {
    double sum = 0;

    // Initial sum of first k elements
    for (int i = 0; i < k; i++) {
        sum += nums[i];
    }

    double maxAvg = sum / k;

    // Slide window
    for (int i = k; i < nums.length; i++) {
        sum = sum - nums[i - k] + nums[i];
        maxAvg = Math.max(maxAvg, sum / k);
    }

    return maxAvg;
}
```

### 3. Deque for Monotonic Window

```java
// When we need maximum/minimum of all windows
// Maintain decreasing/increasing deque

public int[] maxSlidingWindow(int[] nums, int k) {
    if (nums.length == 0) return new int[0];

    int[] result = new int[nums.length - k + 1];
    int resultIdx = 0;

    // Deque stores indices, values are decreasing
    Deque<Integer> deque = new LinkedList<>();

    for (int i = 0; i < nums.length; i++) {
        // Remove indices not in current window
        while (!deque.isEmpty() && deque.peek() < i - k + 1) {
            deque.poll();
        }

        // Remove indices with smaller values than current
        while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
            deque.pollLast();
        }

        // Add current index
        deque.offer(i);

        // Window is ready
        if (i >= k - 1) {
            result[resultIdx++] = nums[deque.peek()];
        }
    }

    return result;
}
```

---

## Java Implementations

### Generic Fixed Window Template

```java
import java.util.*;

class SlidingWindowFixed {
    public static <T> List<T> processWindow(
            int[] arr,
            int k,
            Function<List<Integer>, T> windowProcessor
    ) {
        List<T> results = new ArrayList<>();
        List<Integer> window = new ArrayList<>();

        // First window
        for (int i = 0; i < k; i++) {
            window.add(arr[i]);
        }
        results.add(windowProcessor.apply(new ArrayList<>(window)));

        // Slide window
        for (int i = k; i < arr.length; i++) {
            window.remove(0);
            window.add(arr[i]);
            results.add(windowProcessor.apply(new ArrayList<>(window)));
        }

        return results;
    }

    // Example usage
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        int k = 3;

        // Get max of each window
        List<Integer> maxValues = processWindow(arr, k, window -> {
            return window.stream().max(Integer::compare).orElse(0);
        });

        System.out.println("Max values: " + maxValues);
        // Output: [3, 4, 5]
    }
}
```

### Generic Variable Window Template

```java
import java.util.*;

class SlidingWindowVariable {
    public record WindowState(int left, int right, int size) {}

    public static int findLongestSubarray(
            int[] arr,
            Predicate<List<Integer>> windowValidator
    ) {
        int left = 0;
        int maxSize = 0;
        List<Integer> window = new ArrayList<>();

        for (int right = 0; right < arr.length; right++) {
            window.add(arr[right]);

            // Shrink while invalid
            while (!windowValidator.test(new ArrayList<>(window)) && left <= right) {
                window.remove(0);
                left++;
            }

            // Update answer
            maxSize = Math.max(maxSize, right - left + 1);
        }

        return maxSize;
    }

    // Example: longest subarray with sum <= 10
    public static void main(String[] args) {
        int[] arr = {1, 4, 2, 3, 5, 2};
        int maxLen = findLongestSubarray(arr, window -> {
            int sum = window.stream().mapToInt(Integer::intValue).sum();
            return sum <= 10;
        });
        System.out.println("Longest: " + maxLen);
    }
}
```

---

## Time and Space Complexity

### Complexity Analysis

| Type | Time | Space | Use Case |
|------|------|-------|----------|
| Fixed Window | O(n) | O(1) | Known window size |
| Variable Window | O(n) | O(1) | Find longest/shortest |
| With HashMap | O(n) | O(k) or O(n) | Character counting |
| With Heap/Deque | O(n log k) | O(k) | K-largest/smallest elements |

### When Sliding Window Works

1. **Data is linear/sequential** - Array, string, linked list
2. **Problem involves subarrays/substrings** - Continuous segment
3. **Window can expand/contract** - Based on condition
4. **O(n) solution required** - Too slow for O(n²) brute force

### When Sliding Window Doesn't Work

1. **Non-contiguous elements** - Skip elements in middle
2. **Random access patterns** - Tree/graph traversal needed
3. **Complex window conditions** - May need segment tree

---

## Key Takeaways

1. **Two Phases**: Initialize first window, then slide
2. **Update Formula**: New = Old - Leaving + Entering
3. **Two Types**: Fixed size (known k) vs Variable size (find optimal)
4. **Common Patterns**:
   - Deque for maximum/minimum
   - HashMap for character counting
   - Two pointers for variable size
5. **Window Validity**: Maintain condition with while loop

---

## Practice Problems

### Easy
1. **LeetCode 643** - Maximum Average Subarray I
2. **LeetCode 1343** - Number of Subarrays of Size K
3. **LeetCode 1456** - Maximum Number of Vowels in Substring

### Medium
1. **LeetCode 3** - Longest Substring Without Repeating Characters
2. **LeetCode 209** - Minimum Size Subarray Sum
3. **LeetCode 239** - Sliding Window Maximum
4. **LeetCode 567** - Permutation in String
5. **LeetCode 904** - Fruit Into Baskets

### Hard
1. **LeetCode 480** - Sliding Window Median
2. **LeetCode 76** - Minimum Window Substring
3. **LeetCode 239** - Sliding Window Maximum (Monotonic Deque)

---

> **Remember**: The key insight is that instead of recalculating the entire window each time (O(k×n)), we can update incrementally (O(n)). Always start with the brute force approach to understand, then optimize to sliding window.
