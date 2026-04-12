# 📈 Longest Increasing Subsequence (LIS)

## 1. Basic LIS — O(n²)

```java
public int lengthOfLIS(int[] nums) {
    int n = nums.length;
    int[] dp = new int[n];
    Arrays.fill(dp, 1);
    
    for (int i = 1; i < n; i++) {
        for (int j = 0; j < i; j++) {
            if (nums[i] > nums[j]) {
                dp[i] = Math.max(dp[i], dp[j] + 1);
            }
        }
    }
    return Arrays.stream(dp).max().getAsInt();
}
```

## 2. LIS — O(n log n) with Patience Sorting

```java
public int lengthOfLISFast(int[] nums) {
    int[] tails = new int[nums.length];
    int size = 0;
    
    for (int num : nums) {
        int left = 0, right = size;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (tails[mid] < num) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        tails[left] = num;
        if (left == size) size++;
    }
    return size;
}
```

## 3. Print LIS

Use the dp array from O(n²) solution and backtrack.

## 4. LIS Variants

### Longest Bitonic Subsequence
```
LIS + LDS = Longest bitonic
```

### Longest Decreasing Subsequence
```
Similar to LIS but with > condition
```

### Max Sum Increasing Subsequence
```
dp[i] = nums[i] + max(dp[j]) where j < i and nums[j] < nums[i]
```

## 🎯 Practice
- LeetCode 300. LIS
- LeetCode 673. Number of Longest LIS
- Codeforces 455A

