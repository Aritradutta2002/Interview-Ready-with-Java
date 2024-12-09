package STRINGS;
import java.util.Scanner;

public class CamelCase {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a String: ");
        String s = sc.nextLine();

        for (int i = 0; i < s.length(); i++) {
            char currentChar = s.charAt(i);
            if (Character.isUpperCase(currentChar)){
                System.out.println();
                currentChar = Character.toLowerCase(currentChar);
            }
            else if (Character.isLowerCase(currentChar)){
                currentChar = Character.toUpperCase(currentChar);
            }
            System.out.print(currentChar);
        }
        sc.close();
    }
}
