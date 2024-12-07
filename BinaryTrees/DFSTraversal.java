package BinaryTrees;
import java.util.*;

public class DFSTraversal {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        List<Integer>[] edges = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            edges[i] = new ArrayList<>();
        }

        for (int i = 0; i < n - 1; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            edges[a - 1].add(b - 1);
            edges[b - 1].add(a - 1);
        }
        dfs(0, edges, -1);
        System.out.println();
        dfs(2, edges, -1);
    }

    private static void dfs(int root, List<Integer>[] edges, int parent) {
        System.out.print((root + 1) + " ");
        for (int neighbor : edges[root]) {
            if (neighbor != parent) {
                dfs(neighbor, edges, root);
            }
        }
    }
}
