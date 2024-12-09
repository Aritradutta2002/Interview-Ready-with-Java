package STRINGS;
import java.util.*;
import java.lang.*;
public class String_03 {
    public static void main(String[] args) {
        System.out.println("Enter first String : ");
        Scanner sc = new Scanner(System.in);
        String str1 = sc.next();
        System.out.println("Enter second String : ");
        Scanner s = new Scanner(System.in);
        String str2 = s.next();
       String str3 = str1.toLowerCase();
       String str4 = str2.toLowerCase();

        if(str3.equals(str4))
        System.out.println(str1+" and "+str2+" are eqauls");
        else
            System.out.println(str1+" and "+str2+" are not eqauls");
    }
}
