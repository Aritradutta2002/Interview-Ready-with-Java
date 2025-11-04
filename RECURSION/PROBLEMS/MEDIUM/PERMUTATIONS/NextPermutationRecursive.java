package RECURSION.PROBLEMS.MEDIUM.PERMUTATIONS;

import java.util.Arrays;

/**
 * Generates the next permutation in lexicographical order using recursion.
 * Note: This is a recursive approach; typically iterative for efficiency.
 *
 * @author Aritra Dutta
 */
public class NextPermutationRecursive {

    /**
     * Generates the next permutation of the array.
     *
     * @param arr the array to modify
     * @return true if next permutation exists, false if it's the last
     */
    public static boolean nextPermutation(int[] arr) {
        return nextPermutationHelper(arr, arr.length - 1);
    }

    private static boolean nextPermutationHelper(int[] arr, int n) {
        if (n == 0) {
            return false;
        }
        if (nextPermutationHelper(arr, n - 1)) {
            return true;
        }
        // Find the rightmost increase
        for (int i = n - 1; i >= 0; i--) {
            if (arr[i] < arr[n]) {
                swap(arr, i, n);
                reverse(arr, i + 1, n);
                return true;
            }
        }
        return false;
    }

    private static void reverse(int[] arr, int start, int end) {
        while (start < end) {
            swap(arr, start, end);
            start++;
            end--;
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3};
        System.out.println("Original array: " + Arrays.toString(arr));
        if (nextPermutation(arr)) {
            System.out.println("Next permutation: " + Arrays.toString(arr));
        } else {
            System.out.println("No next permutation exists");
        }
    }
}