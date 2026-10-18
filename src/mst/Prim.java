package mst;

import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Prim {
    public static MSTResult run(Graph g) {
        int n = g.size();
        boolean[] visited = new boolean[n];
        PriorityQueue<Edge> pq = new PriorityQueue<>((a, b) -> Integer.compare(a.weight, b.weight));
        List<Edge> result = new ArrayList<>();
        long total = 0;
        long examined = 0;
        long pqOps = 0;

        visited[0] = true;
        int count = 1;
        for (Edge e : g.neighbors(0)) {
            pq.add(e);
            pqOps++;
        }

        while (!pq.isEmpty()) {
            Edge e = pq.poll();
            pqOps++;
            examined++;
            if (visited[e.to]) continue;
            visited[e.to] = true;
            count++;
            result.add(e);
            total += e.weight;
            for (Edge next : g.neighbors(e.to)) {
                if (!visited[next.to]) {
                    pq.add(next);
                    pqOps++;
                }
            }
        }

        return new MSTResult(result, total, count == n, examined, pqOps);
    }
}