package STRINGS;
import java.lang.*;

public class StringReverse_Index {
    static public void reverseStringAt(String str, int position) {
        char [] ch = str.toCharArray();
        int start = 0;
        int end = ch.length - 1;
        while (start < position) {
            char temp = ch[start];
            ch[start] = ch[position -1];
            ch[position -1 ] = temp;
            start ++;
            position --;
        }
        String string = new String(ch);
        System.out.println(string);
    }

    public static void main(String[] args) {
        String s = "AritraDutta";
        reverseStringAt(s, 6);
    }
}
