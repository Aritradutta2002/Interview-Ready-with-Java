package GRAPHS.level3_mst_and_advanced;

import GRAPHS.Basics.UnionFind;

import java.util.Comparator;
import java.util.List;

public class KruskalMST {
    public static int mst(int n, List<Edge> edges) {
        edges.sort(Comparator.comparingInt(e -> e.w));
        UnionFind uf = new UnionFind(n);
        int total = 0;
        int used = 0;
        for (Edge e : edges) {
            if (uf.union(e.u, e.v)) {
                total += e.w;
                used++;
                if (used == n - 1) break;
            }
        }
        return used == n - 1 ? total : Integer.MAX_VALUE; // disconnected
    }

    public static class Edge {
        public int u, v, w;

        public Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }
}
