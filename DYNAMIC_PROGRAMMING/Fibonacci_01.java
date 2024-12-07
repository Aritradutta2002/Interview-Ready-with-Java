package DYNAMIC_PROGRAMMING;

import java.util.Scanner;

public class Fibonacci_01 {

   static public int fib(int n) {
        int [] arr = new int [n];
        arr[0] = 0;
        arr[1] = 1;

        for(int i = 2; i<n; i++){
            arr[i] = arr[i-1] + arr[i-2];
        }
      return arr[n-1] + arr[n-2];
    }
    public static void main(String[] args) {
       Scanner sc = new Scanner(System.in);
       int n = sc.nextInt();
        int result  = fib(n);
        System.out.println(result);
    }
}
