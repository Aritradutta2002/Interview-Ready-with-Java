package GRAPHS.level4_problems;

public class NumberOfProvinces {
    public static int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        boolean[] vis = new boolean[n];
        int ans = 0;
        for (int i = 0; i < n; i++)
            if (!vis[i]) {
                ans++;
                dfs(i, isConnected, vis);
            }
        return ans;
    }

    static void dfs(int u, int[][] g, boolean[] vis) {
        vis[u] = true;
        for (int v = 0; v < g.length; v++) if (g[u][v] == 1 && !vis[v]) dfs(v, g, vis);
    }
}
