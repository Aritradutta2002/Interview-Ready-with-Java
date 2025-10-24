package GRAPHS.level3_mst_and_advanced;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class PrimMST {
    public static int prim(int n, List<List<int[]>> adjW) {
        boolean[] vis = new boolean[n];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{0, 0});
        int total = 0, cnt = 0;
        while (!pq.isEmpty() && cnt < n) {
            int[] cur = pq.poll();
            int u = cur[0], w = cur[1];
            if (vis[u]) continue;
            vis[u] = true;
            total += w;
            cnt++;
            for (int[] e : adjW.get(u)) if (!vis[e[0]]) pq.add(new int[]{e[0], e[1]});
        }
        return cnt == n ? total : Integer.MAX_VALUE;
    }
}
