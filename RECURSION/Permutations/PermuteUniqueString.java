package RECURSION.Permutations;

import java.util.HashSet;
import java.util.Set;

/**
 * Generates all unique permutations of a string with possible duplicates using recursion.
 *
 * @author Aritra Dutta
 */
public class PermuteUniqueString {

    /**
     * Generates all unique permutations of a string.
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

    private static String swap(String str, int i, int j) {
        char[] charArray = str.toCharArray();
        char temp = charArray[i];
        charArray[i] = charArray[j];
        charArray[j] = temp;
        return String.valueOf(charArray);
    }

    public static void main(String[] args) {
        String str = "AAB";
        System.out.println("Unique permutations of '" + str + "':");
        permuteUnique(str, 0, str.length() - 1);
    }
}