package RECURSION.Permutations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Generates all unique permutations of an array with duplicates using recursion.
 *
 * @author Aritra Dutta
 */
public class PermuteArrayUnique {

    /**
     * Generates all unique permutations of an array.
     *
     * @param arr the input array (may contain duplicates)
     * @param index the current index
     */
    public static void permuteUnique(int[] arr, int index) {
        if (index == arr.length - 1) {
            System.out.println(Arrays.toString(arr));
        } else {
            Set<Integer> used = new HashSet<>();
            for (int i = index; i < arr.length; i++) {
                if (!used.contains(arr[i])) {
                    used.add(arr[i]);
                    swap(arr, index, i);
                    permuteUnique(arr, index + 1);
                    swap(arr, index, i); // backtrack
                }
            }
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int[] arr = {1, 1, 2};
        System.out.println("Unique permutations of array " + Arrays.toString(arr) + ":");
        permuteUnique(arr, 0);
    }
}