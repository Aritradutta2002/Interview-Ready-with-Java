package RECURSION;

import java.util.Scanner;

public class Product_nTo1 {
    public static int product (int num){
       if(num == 1){
           return 1;
       }
       return num * product(num-1);
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        System.out.println(product(n));
    }
}
