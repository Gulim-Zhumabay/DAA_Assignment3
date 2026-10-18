package graph;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GraphGenerator {
    private static final int MAX_WEIGHT = 100;

    public static Graph connectedUndirected(int n, int m, long seed) {
        if (m < n - 1 || m > n * (n - 1) / 2) throw new IllegalArgumentException("bad edge count");
        Random rnd = new Random(seed);
        Graph g = new Graph(n, false);
        Set<Long> used = new HashSet<>();

        for (int v = 1; v < n; v++) {
            add(g, used, rnd.nextInt(v), v, rnd);
        }
        int count = n - 1;
        while (count < m) {
            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);
            if (u != v && add(g, used, u, v, rnd)) count++;
        }
        return g;
    }

    public static Graph dag(int n, int m, long seed) {
        if (m > n * (n - 1) / 2) throw new IllegalArgumentException("bad edge count");
        Random rnd = new Random(seed);
        Graph g = new Graph(n, true);
        Set<Long> used = new HashSet<>();

        int[] order = new int[n];
        for (int i = 0; i < n; i++) order[i] = i;
        for (int i = n - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int t = order[i];
            order[i] = order[j];
            order[j] = t;
        }

        int count = 0;
        while (count < m) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            if (a == b) continue;
            if (a > b) { int t = a; a = b; b = t; }
            if (add(g, used, order[a], order[b], rnd)) count++;
        }
        return g;
    }

    public static Graph directedRandom(int n, int m, long seed) {
        if (m > n * (n - 1)) throw new IllegalArgumentException("bad edge count");
        Random rnd = new Random(seed);
        Graph g = new Graph(n, true);
        Set<Long> used = new HashSet<>();

        int count = 0;
        while (count < m) {
            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);
            if (u != v && add(g, used, u, v, rnd)) count++;
        }
        return g;
    }

    private static boolean add(Graph g, Set<Long> used, int u, int v, Random rnd) {
        long n = g.size();
        long key = g.isDirected() ? u * n + v : Math.min(u, v) * n + Math.max(u, v);
        if (!used.add(key)) return false;
        g.addEdge(u, v, 1 + rnd.nextInt(MAX_WEIGHT));
        return true;
    }
}