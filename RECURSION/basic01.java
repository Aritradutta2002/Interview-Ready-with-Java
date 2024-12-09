package RECURSION;
import java.util.Scanner;
public class basic01 {

    static void print(int n){

        if(n == 50) {
            return;
        }
        System.out.println("Hello World" +"  "+n);
        print(n + 1);
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        print(1);
    }
}
