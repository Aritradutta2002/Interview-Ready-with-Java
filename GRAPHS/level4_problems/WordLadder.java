package GRAPHS.level4_problems;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class WordLadder {
    public static int ladderLength(String beginWord, String endWord, java.util.List<String> wordList) {
        Set<String> dict = new HashSet<>(wordList);
        if (!dict.contains(endWord)) return 0;
        Queue<String> q = new ArrayDeque<>();
        q.add(beginWord);
        int steps = 1;
        while (!q.isEmpty()) {
            int sz = q.size();
            while (sz-- > 0) {
                String w = q.poll();
                if (w.equals(endWord)) return steps;
                char[] a = w.toCharArray();
                for (int i = 0; i < a.length; i++) {
                    char orig = a[i];
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == orig) continue;
                        a[i] = c;
                        String nw = new String(a);
                        if (dict.remove(nw)) q.add(nw);
                    }
                    a[i] = orig;
                }
            }
            steps++;
        }
        return 0;
    }
}
