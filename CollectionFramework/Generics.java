package CollectionFramework;
import java.util.*;
class pair<T,S>{
    T a;
    S b;
}
public class Generics {
    public static void main(String[] args) {
        pair<String,String> p = new pair<>();
        p.a = "12";
        p.b = "Aritra Dutta";
        String str = (String)p.a;
    }
}
