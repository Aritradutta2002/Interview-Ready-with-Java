package DYNAMIC_PROGRAMMING.ClassicDP;
import java.util.*;
/*
 *   Author  : Aritra Dutta
 *   Created : Saturday, 01.03.2025  12:30 am
 */
public class LongestCommonSubsequence_Leetcode1143 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String text1 = sc.nextLine();
        String text2 = sc.nextLine();
        sc.close();

        System.out.println(longestCommonSubsequence(text1, text2));
        System.out.println(longestCommonSubsequenceRecursive(text1, text2));
    }

    /*
        Let Optimise the Solution using DP - Adding Memoization
        Time Complexity =>  O(M x N)
        Space Complexity => O(N)
     */

    public static int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();

        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    curr[j] = prev[j - 1] + 1;
                } else {
                    curr[j] = Math.max(prev[j], curr[j - 1]);
                }
            }
            prev = curr.clone();
        }
        return prev[n];
    }

    /*
        Recursive Approach - This Solution is correct but very time-consuming. If two string don't match anything
        Time Complexity => O(2^N) (approx)
     */

    public static int longestCommonSubsequenceRecursive(String text1, String text2) {
        int len1 = text1.length();
        int len2 = text2.length();

        return findLCSLengthRecursive(text1, 0, len1, text2, 0, len2);
    }

    public static int findLCSLengthRecursive(String text1, int i, int m, String text2, int j, int n) {
        if (i == m || j == n){
            return 0;
        }

        if (text1.charAt(i) == text2.charAt(j)) {
            return 1 + findLCSLengthRecursive(text1, i + 1, m, text2, j + 1, n);
        } else {
            return Math.max(findLCSLengthRecursive(text1, i + 1, m, text2, j, n),
                    findLCSLengthRecursive(text1, i, m, text2, j + 1, n));
        }
    }
}
