package HashMap;
import java.util.*;

public class hashmap {
    public static void main(String[] args) {
        HashMap<Integer,Integer> map=new HashMap<>();
        map.put(1,9);
        map.put(0,6);
        map.put(2,8);
        map.put(3,7);

        System.out.println(map.get(3));
    }
}
