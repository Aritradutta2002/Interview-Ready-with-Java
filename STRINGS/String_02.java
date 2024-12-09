package STRINGS;
import java.lang.*;

public class String_02 {
    public static void main(String[] args) {

        String str1 = new String("GeeksForGeeks");
        String str2 = new String("For");

        System.out.println(str1.compareTo(str2));
        System.out.println(str1.indexOf(str2));
        System.out.println(str1.equals(str2));
        System.out.println(str1.contains(str2));
        System.out.println(str1.concat(str2));
    }
}
