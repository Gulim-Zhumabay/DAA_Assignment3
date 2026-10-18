package shortestpath;

import graph.Edge;
import graph.Graph;
import java.util.Arrays;
import java.util.PriorityQueue;

public class Dijkstra {
    public static DijkstraResult run(Graph g, int source) {
        int n = g.size();
        for (Edge e : g.allEdges()) {
            if (e.weight < 0) throw new IllegalArgumentException("negative weight");
        }

        long[] dist = new long[n];
        int[] prev = new int[n];
        Arrays.fill(dist, DijkstraResult.INF);
        Arrays.fill(prev, -1);
        dist[source] = 0;

        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[1], b[1]));
        pq.add(new long[]{source, 0});
        long relaxations = 0;
        long pqOps = 1;

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            pqOps++;
            int u = (int) cur[0];
            if (cur[1] > dist[u]) continue;
            for (Edge e : g.neighbors(u)) {
                relaxations++;
                long nd = dist[u] + e.weight;
                if (nd < dist[e.to]) {
                    dist[e.to] = nd;
                    prev[e.to] = u;
                    pq.add(new long[]{e.to, nd});
                    pqOps++;
                }
            }
        }

        return new DijkstraResult(dist, prev, relaxations, pqOps);
    }
}