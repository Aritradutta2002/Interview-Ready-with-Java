package RECURSION.Permutations;

import java.util.Arrays;

/**
 * Generates all permutations of an array using recursion.
 *
 * @author Aritra Dutta
 */
public class PermuteArray {

    /**
     * Generates all permutations of an array.
     *
     * @param arr the input array
     * @param index the current index to fix
     */
    public static void permute(int[] arr, int index) {
        if (index == arr.length - 1) {
            System.out.println(Arrays.toString(arr));
        } else {
            for (int i = index; i < arr.length; i++) {
                swap(arr, index, i);
                permute(arr, index + 1);
                swap(arr, index, i); // backtrack
            }
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3};
        System.out.println("Permutations of array " + Arrays.toString(arr) + ":");
        permute(arr, 0);
    }
}