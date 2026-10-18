package mst;

import dsu.DSU;
import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.List;

public class Kruskal {
    public static MSTResult run(Graph g) {
        List<Edge> sorted = new ArrayList<>(g.allEdges());
        sorted.sort((a, b) -> Integer.compare(a.weight, b.weight));

        DSU dsu = new DSU(g.size());
        List<Edge> result = new ArrayList<>();
        long total = 0;
        long examined = 0;

        for (Edge e : sorted) {
            examined++;
            if (dsu.union(e.from, e.to)) {
                result.add(e);
                total += e.weight;
            }
        }

        boolean connected = dsu.components() == 1;
        return new MSTResult(result, total, connected, examined,
                dsu.findCalls + dsu.unionCalls);
    }
}