package BACKTRACKING;
/*
*   Author  : Aritra Dutta
*   Created : Friday, 14.02.2025  12:08 am
*/
import java.util.*;
public class Permutations_Leetcode {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int size = sc.nextInt();
        int[] arr = new int[size];
        for(int i = 0; i < size; i++){
            arr[i] = sc.nextInt();
        }
        System.out.println(permute(arr));;
    }


    static public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();




        return res;
    }
}
