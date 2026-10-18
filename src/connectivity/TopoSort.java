package connectivity;

import graph.Edge;
import graph.Graph;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TopoSort {
    public static TopoResult run(Graph g) {
        int n = g.size();
        int[] indeg = new int[n];
        long ops = 0;

        for (Edge e : g.allEdges()) {
            indeg[e.to]++;
            ops++;
        }

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) q.add(i);
        }

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (Edge e : g.neighbors(u)) {
                ops++;
                indeg[e.to]--;
                if (indeg[e.to] == 0) q.add(e.to);
            }
        }

        return new TopoResult(order, order.size() != n, ops);
    }
}