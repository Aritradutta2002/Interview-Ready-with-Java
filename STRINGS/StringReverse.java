package STRINGS;
import java.lang.*;
public class StringReverse {
    static public char[] strReverse(char [] s) {
        int start  = 0;
        int end   = s.length-1;
        int mid = (start + end)/2;
        for (int i = 0; i < mid+1; i++) {
            char temp = s[i];
            s[i] = s[end];
            s[end] = temp;
            end--;
        }
        return s;
    }
    public static void main(String[] args) {
        char [] ch = {'h','e','l','l','o'};
        System.out.println(strReverse(ch));
    }
}
