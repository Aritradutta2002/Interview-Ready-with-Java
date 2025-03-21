package BACKTRACKING;
/*
*   Author  : Aritra Dutta
*   Created : Friday, 14.02.2025  12:08 am
*/
import java.util.*;
public class Permutations_Leetcode46 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int size = sc.nextInt();
        int[] arr = new int[size];
        for(int i = 0; i < size; i++){
            arr[i] = sc.nextInt();
        }
        System.out.println(permute(arr));;
    }

    /* APPROACH # 01 - With the help of Recursion and Swapping less Space Complexity Code */
    // Time Complexity  -> O(N!)
    // Space Complexity -> O(N)
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        permuteRecursion(0, nums, ans);
        return ans;
    }

    private static void permuteRecursion(int index, int[] nums, List<List<Integer>> ans) {
        if(index == nums.length){
            List<Integer> list = new ArrayList<>();
            for(int i = 0; i < nums.length; i++){
                list.add(nums[i]);
            }
            ans.add(new ArrayList<>(list));
            return;
        }

        for(int i = index; i < nums.length; i++){
            swap(i, index, nums);
            permuteRecursion(index + 1, nums, ans);
            swap(i, index, nums);
        }
    }

    private static void swap(int i, int j, int[] nums) {
        int t = nums[i];
        nums[i] = nums[j];
        nums[j] = t;
    }

    /* APPROACH # 02 - BackTracking and Mapping Approach with Recursion overhead */
    // Time Complexity  - O (N!)
    // Space Complexity - O(N)
    public static List<List<Integer>> permuteBackTrack(int[] nums) {
        List<List<Integer>> res = new ArrayList<>(); // to store returning the result
        List<Integer> permutation = new ArrayList<>(); // to store one of the permutation after each recursive call
        Set<Integer> visited = new HashSet<>(); // to track if the element is already added to the permutation list
        helper(nums, permutation, res, visited, 0); // permuteBackTrack helper function
        return res;
    }

    public static void helper(int[] nums, List<Integer> permutation, List<List<Integer>> res, Set<Integer> visited, int currentIndex) {
        if (visited.size() == nums.length) {
            res.add(new ArrayList<>(permutation));
            return;
        }
        for (int i = currentIndex; i < nums.length; i++) {
            if(!visited.contains(nums[i])) {
                permutation.add(nums[i]);
                visited.add(nums[i]);
                helper(nums, permutation, res, visited, currentIndex);
                permutation.removeLast();
                visited.remove(nums[i]);
            }
        }
    }
}

