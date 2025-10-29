package RECURSION.Permutations;

/**
 * Collection of permutation problems solved using recursion.
 * Includes generating all permutations, handling duplicates, and more.
 *
 * @author Aritra Dutta
 * @created Friday, 21.03.2025 09:37 am
 */
import java.util.*;

public class PermutationsWithRecursion {

    /**
     * Generates all permutations of a string using recursion and backtracking.
     *
     * @param str the input string
     * @param l the starting index for swapping
     * @param r the ending index
     */
    public static void permuteString(String str, int l, int r) {
        if (l == r) {
            System.out.println(str);
        } else {
            for (int i = l; i <= r; i++) {
                str = swap(str, l, i);
                permuteString(str, l + 1, r);
                str = swap(str, l, i); // backtrack
            }
        }
    }

    /**
     * Generates all unique permutations of a string with possible duplicates using recursion.
     *
     * @param str the input string (may contain duplicates)
     * @param l the starting index
     * @param r the ending index
     */
    public static void permuteUnique(String str, int l, int r) {
        if (l == r) {
            System.out.println(str);
        } else {
            Set<Character> used = new HashSet<>();
            for (int i = l; i <= r; i++) {
                if (!used.contains(str.charAt(i))) {
                    used.add(str.charAt(i));
                    str = swap(str, l, i);
                    permuteUnique(str, l + 1, r);
                    str = swap(str, l, i); // backtrack
                }
            }
        }
    }

    /**
     * Generates all permutations of an array using recursion.
     *
     * @param arr the input array
     * @param index the current index to fix
     */
    public static void permuteArray(int[] arr, int index) {
        if (index == arr.length - 1) {
            System.out.println(Arrays.toString(arr));
        } else {
            for (int i = index; i < arr.length; i++) {
                swap(arr, index, i);
                permuteArray(arr, index + 1);
                swap(arr, index, i); // backtrack
            }
        }
    }

    /**
     * Generates all unique permutations of an array with duplicates using recursion.
     *
     * @param arr the input array (may contain duplicates)
     * @param index the current index
     */
    public static void permuteArrayUnique(int[] arr, int index) {
        if (index == arr.length - 1) {
            System.out.println(Arrays.toString(arr));
        } else {
            Set<Integer> used = new HashSet<>();
            for (int i = index; i < arr.length; i++) {
                if (!used.contains(arr[i])) {
                    used.add(arr[i]);
                    swap(arr, index, i);
                    permuteArrayUnique(arr, index + 1);
                    swap(arr, index, i); // backtrack
                }
            }
        }
    }

    /**
     * Generates the next permutation in lexicographical order using recursion.
     * Note: This is a recursive approach; typically iterative for efficiency.
     *
     * @param arr the array to modify
     * @return true if next permutation exists, false if it's the last
     */
    public static boolean nextPermutationRecursive(int[] arr) {
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

    // Helper methods
    private static String swap(String str, int i, int j) {
        char[] charArray = str.toCharArray();
        char temp = charArray[i];
        charArray[i] = charArray[j];
        charArray[j] = temp;
        return String.valueOf(charArray);
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        System.out.println("Permutations of 'ABC':");
        permuteString("ABC", 0, 2);

        System.out.println("\nUnique permutations of 'AAB':");
        permuteUnique("AAB", 0, 2);

        System.out.println("\nPermutations of array [1,2,3]:");
        int[] arr1 = {1, 2, 3};
        permuteArray(arr1, 0);

        System.out.println("\nUnique permutations of array [1,1,2]:");
        int[] arr2 = {1, 1, 2};
        permuteArrayUnique(arr2, 0);

        System.out.println("\nNext permutation of [1,2,3]:");
        int[] arr3 = {1, 2, 3};
        if (nextPermutationRecursive(arr3)) {
            System.out.println(Arrays.toString(arr3));
        } else {
            System.out.println("No next permutation");
        }
    }
}