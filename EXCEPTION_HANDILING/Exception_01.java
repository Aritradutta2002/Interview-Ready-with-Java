package EXCEPTION_HANDILING;
import java.io.*;
import java.util.Scanner;

public class Exception_01 {
    public static void main(String[] args){
        try{
            Scanner sc = new Scanner(new File("test.in"));
        }

        catch(FileNotFoundException e){
            System.out.println("File not Found !");
        }
        try{
            int result = 10/0;
            System.out.println(result);
        }
        catch(ArithmeticException e){
            System.out.println("Arithmetic Exception");
        }
        finally {
            System.out.println("Always executed");
        }

        System.out.println("I am Aritra");
    }
}
