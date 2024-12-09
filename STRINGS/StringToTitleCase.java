package STRINGS;
import java.util.*;
public class StringToTitleCase {
    public static void main(String[] args) {
        Scanner sc = new Scanner (System.in);
        int t = sc.nextInt();
        while(t -- > 0){
            String str = sc.next();
            char [] ch = str.toCharArray();
            StringBuilder sb = new StringBuilder();
            sb.append(Character.toUpperCase(ch[0]));
            for (int i = 1; i < ch.length; i++) {
                if(ch[i] == ' ' && ch[i+1] != Character.toUpperCase(ch[i + 1])){
                    sb.append(' ' ).append(Character.toUpperCase(ch[i + 1]));
                }
                if(Character.isUpperCase(ch[i]) == true && Character.isUpperCase(ch[i + 1]) == true){
                    sb.append(ch[i]);
                }
                sb.append(ch[i]);
            }
            System.out.println(sb.toString());
        }
    }
}
