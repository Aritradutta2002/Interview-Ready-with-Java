# 🔗 Longest Common Subsequence (LCS) — Complete Guide

## 1. Basic LCS

**Find longest subsequence common to both strings.**

### Tabulation

```java
public int longestCommonSubsequence(String text1, String text2) {
    int m = text1.length(), n = text2.length();
    int[][] dp = new int[m + 1][n + 1];
    
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (text1.charAt(i-1) == text2.charAt(j-1)) {
                dp[i][j] = dp[i-1][j-1] + 1;
            } else {
                dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
            }
        }
    }
    return dp[m][n];
}
```
**Time:** O(mn), **Space:** O(mn) → O(min(m,n))

## 2. Print LCS String

```java
public String printLCS(String s1, String s2) {
    // ... (same dp table)
    
    StringBuilder result = new StringBuilder();
    int i = m, j = n;
    while (i > 0 && j > 0) {
        if (s1.charAt(i-1) == s2.charAt(j-1)) {
            result.append(s1.charAt(i-1));
            i--; j--;
        } else if (dp[i-1][j] > dp[i][j-1]) {
            i--;
        } else {
            j--;
        }
    }
    return result.reverse().toString();
}
```

## 3. LCS Variants

### Shortest Common Supersequence (SCS)
```
SCS = m + n - LCS
```

### Minimum Insertions/Deletions
```
Min Insertions = n - LCS
Min Deletions = m - LCS
```

### Longest Common Substring
```java
// dp[i][j] stores length of LCS ending at i,j
if (text1.charAt(i-1) == text2.charAt(j-1)) {
    dp[i][j] = dp[i-1][j-1] + 1;
    maxLen = Math.max(maxLen, dp[i][j]);
}
```

## 4. Space Optimization

```java
public int longestCommonSubsequenceOptimized(String text1, String text2) {
    int m = text1.length();
    int[] prev = new int[text2.length() + 1];
    int[] curr = new int[text2.length() + 1];
    
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (text1.charAt(i-1) == text2.charAt(j-1)) {
                curr[j] = prev[j-1] + 1;
            } else {
                curr[j] = Math.max(prev[j], curr[j-1]);
            }
        }
        prev = curr.clone();
    }
    return prev[n];
}
```

## 🎯 Practice
- LeetCode 1143. Longest Common Subsequence
- LeetCode 583. Delete Operation for Two Strings
- Codeforces 1200F

