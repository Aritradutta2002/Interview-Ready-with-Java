package GRAPHS.level4_problems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CloneGraph {
    public static Node cloneGraph(Node node) {
        if (node == null) return null;
        Map<Node, Node> mp = new HashMap<>();
        return dfs(node, mp);
    }

    private static Node dfs(Node u, Map<Node, Node> mp) {
        if (mp.containsKey(u)) return mp.get(u);
        Node c = new Node(u.val);
        mp.put(u, c);
        for (Node v : u.neighbors) c.neighbors.add(dfs(v, mp));
        return c;
    }

    public static class Node {
        public int val;
        public java.util.List<Node> neighbors;

        public Node() {
            val = 0;
            neighbors = new ArrayList<>();
        }

        public Node(int v) {
            val = v;
            neighbors = new ArrayList<>();
        }
    }
}
