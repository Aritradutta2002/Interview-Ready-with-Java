package StringsHandling;

public class ReverseStringII_leetcode541 {
    static public String reverseStr(String s, int k) {
        char[] ch = s.toCharArray();
        int start = 0;
        int end = (s.length() - 1);
        while(start < end) {
            char temp = ch[start];
            ch[start] = ch[end];
            ch[end] = temp;
            start ++;
            end--;
        }






        String str = new String(ch);
        return str;
    }

    public static void main(String[] args) {
        String s = "abcdefg";
        System.out.println(reverseStr(s, 2));
    }
}
