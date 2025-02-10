package BINARY_TREES.LeetCode;
import java.util.*;

public class GraphTraversal {
    public static void dfs(int currentNode, List<List<Integer>> adj, int parent, List<Integer> ans, boolean[] vis) {
        ans.add(currentNode);
        vis[currentNode] = true;
        for (int neighbour : adj.get(currentNode)){
            if (neighbour != parent) {
                dfs(neighbour, adj, currentNode, ans, vis);
            }
        }
    }

    public static void solve() {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<List<Integer>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int i = 0; i < n - 1; i++) {
            int u = sc.nextInt() - 1;
            int v = sc.nextInt() - 1;
            adj.get(u).add(v);
            adj.get(v).add(u);
        }
        int root = 0;
        List<Integer> dfsTraversal = new ArrayList<>();
        boolean[] vis = new boolean[n];
        dfs(root, adj, -1, dfsTraversal, vis);
        sc.close();
    }

    public static void main(String[] args) {
        solve();
    }
}
