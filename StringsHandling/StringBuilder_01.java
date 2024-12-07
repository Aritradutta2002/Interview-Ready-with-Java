package StringsHandling;
import java.util.*;
import java.lang.*;

public class StringBuilder_01 {
    public static void main(String[] args) {
        StringBuilder str1 = new StringBuilder("");
        StringBuilder str2 = new StringBuilder("Geeks");

        System.out.println(str1.capacity());

        str1 = str1.append("A Quick Brown Fox Jumps over a Lazy Dog");
        System.out.println(str1.capacity());

    }
}
