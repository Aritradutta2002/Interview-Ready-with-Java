package StringsHandling;
import java.util.*;
public class FractionAdditionSubtraction {
    public static String fractionAddition(String expression) {
        int len = expression.length();
        int numerator = 0;
        int denominator = 1;
        int  i = 0;
        while (i < len) {
            int currNumerator = 0;
            int currDenominator = 0;
            boolean sign = false;
            if(expression.charAt(i) == '+' || expression.charAt(i) == '-') {
                i++;
                if(expression.charAt(i) == '-') {
                    sign = true;
                }
            }
            while(i < len && Character.isDigit(expression.charAt(i))) {
                int val  = expression.charAt(i) - '0';
                currNumerator = currNumerator * 10 + val;
                i++;
            }
            if(sign ==  true){          // If our current Numerator is negative then make it negative
                currNumerator *= -1;
            }
            i++;
            while(i < len && Character.isDigit(expression.charAt(i))) {
                int val  = expression.charAt(i) - '0';
                currDenominator = currDenominator * 10 + val;
                i++;
            }
            numerator = currNumerator * denominator + currDenominator * numerator;
            denominator = currDenominator * denominator;
        }
        int gcd = findGCD(numerator, denominator);
        numerator /= gcd;
        denominator /= gcd;
        return numerator + "/" + denominator;
    }

    private static int findGCD(int a, int b){
        if(a == 0){
            return b;
        }
        return findGCD(b % a, a);
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.next();
        System.out.println(fractionAddition(str));
    }
}
