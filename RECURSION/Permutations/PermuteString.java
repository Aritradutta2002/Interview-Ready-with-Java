package RECURSION.Permutations;

/**
 * Generates all permutations of a string using recursion and backtracking.
 *
 * @author Aritra Dutta
 */
public class PermuteString {

    /**
     * Generates all permutations of a string.
     *
     * @param str the input string
     * @param l the starting index for swapping
     * @param r the ending index
     */
    public static void permute(String str, int l, int r) {
        if (l == r) {
            System.out.println(str);
        } else {
            for (int i = l; i <= r; i++) {
                str = swap(str, l, i);
                permute(str, l + 1, r);
                str = swap(str, l, i); // backtrack
            }
        }
    }

    private static String swap(String str, int i, int j) {
        char[] charArray = str.toCharArray();
        char temp = charArray[i];
        charArray[i] = charArray[j];
        charArray[j] = temp;
        return String.valueOf(charArray);
    }

    public static void main(String[] args) {
        String str = "ABC";
        System.out.println("Permutations of '" + str + "':");
        permute(str, 0, str.length() - 1);
    }
}