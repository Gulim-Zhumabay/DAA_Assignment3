package tests;

import connectivity.SCC;
import connectivity.SCCResult;
import connectivity.TopoResult;
import connectivity.TopoSort;
import graph.Edge;
import graph.Graph;
import graph.GraphGenerator;
import mst.Kruskal;
import mst.Prim;
import shortestpath.Dijkstra;
import shortestpath.DijkstraResult;
import java.util.Arrays;
import java.util.List;

public class Tests {
    static int passed = 0;
    static int failed = 0;

    static void check(String name, boolean ok) {
        if (ok) passed++;
        else failed++;
        System.out.println((ok ? "pass: " : "fail: ") + name);
    }

    public static void main(String[] args) {
        singleVertex();
        disconnected();
        multipleMst();
        cycles();
        unreachable();
        negativeWeight();
        randomChecks();
        System.out.println(passed + " passed, " + failed + " failed");
    }

    static void singleVertex() {
        Graph g = new Graph(1, false);
        check("single vertex kruskal", Kruskal.run(g).totalWeight == 0 && Kruskal.run(g).connected);
        check("single vertex prim", Prim.run(g).totalWeight == 0 && Prim.run(g).connected);

        Graph d = new Graph(1, true);
        TopoResult t = TopoSort.run(d);
        check("single vertex topo", t.order.size() == 1 && !t.hasCycle);
        check("single vertex scc", SCC.run(d).components.size() == 1);
        check("single vertex dijkstra", Dijkstra.run(d, 0).dist[0] == 0);
    }

    static void disconnected() {
        Graph g = new Graph(4, false);
        g.addEdge(0, 1, 1);
        g.addEdge(2, 3, 2);
        check("disconnected kruskal", !Kruskal.run(g).connected && Kruskal.run(g).totalWeight == 3);
        check("disconnected prim", !Prim.run(g).connected);
    }

    static void multipleMst() {
        Graph g = new Graph(4, false);
        g.addEdge(0, 1, 5);
        g.addEdge(1, 2, 5);
        g.addEdge(2, 3, 5);
        g.addEdge(3, 0, 5);
        check("equal weights kruskal", Kruskal.run(g).totalWeight == 15);
        check("equal weights prim", Prim.run(g).totalWeight == 15);
    }

    static void cycles() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        g.addEdge(2, 3, 1);
        check("cycle detected by topo", TopoSort.run(g).hasCycle);
        check("cycle scc count", SCC.run(g).components.size() == 2);
    }

    static void unreachable() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 3);
        DijkstraResult r = Dijkstra.run(g, 0);
        check("unreachable distance", r.dist[2] == DijkstraResult.INF);
        check("unreachable path empty", r.pathTo(2).isEmpty());
        check("reachable distance", r.dist[1] == 3);
    }

    static void negativeWeight() {
        Graph g = new Graph(2, true);
        g.addEdge(0, 1, -5);
        boolean thrown = false;
        try {
            Dijkstra.run(g, 0);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check("dijkstra rejects negative weight", thrown);
    }

    static void randomChecks() {
        boolean mstOk = true;
        boolean topoOk = true;
        boolean sccOk = true;
        boolean dijkstraOk = true;

        for (long seed = 1; seed <= 50; seed++) {
            Graph u = GraphGenerator.connectedUndirected(30, 100, seed);
            if (Kruskal.run(u).totalWeight != Prim.run(u).totalWeight) mstOk = false;

            Graph dag = GraphGenerator.dag(30, 100, seed);
            if (!validTopo(dag)) topoOk = false;

            Graph dir = GraphGenerator.directedRandom(30, 100, seed);
            if (!eachVertexOnce(dir)) sccOk = false;

            Graph w = GraphGenerator.connectedUndirected(20, 60, seed);
            if (!matchesFloyd(w)) dijkstraOk = false;
        }

        check("kruskal equals prim on 50 random graphs", mstOk);
        check("topological order valid on 50 random dags", topoOk);
        check("every vertex in exactly one scc", sccOk);
        check("dijkstra matches floyd-warshall", dijkstraOk);
    }

    static boolean validTopo(Graph g) {
        TopoResult r = TopoSort.run(g);
        if (r.hasCycle || r.order.size() != g.size()) return false;
        int[] pos = new int[g.size()];
        for (int i = 0; i < r.order.size(); i++) pos[r.order.get(i)] = i;
        for (Edge e : g.allEdges()) {
            if (pos[e.from] >= pos[e.to]) return false;
        }
        return true;
    }

    static boolean eachVertexOnce(Graph g) {
        SCCResult r = SCC.run(g);
        int[] seen = new int[g.size()];
        for (List<Integer> comp : r.components) {
            for (int v : comp) seen[v]++;
        }
        for (int s : seen) {
            if (s != 1) return false;
        }
        return true;
    }

    static boolean matchesFloyd(Graph g) {
        int n = g.size();
        long big = 1_000_000_000_000L;
        long[][] d = new long[n][n];
        for (long[] row : d) Arrays.fill(row, big);
        for (int i = 0; i < n; i++) d[i][i] = 0;
        for (Edge e : g.allEdges()) {
            d[e.from][e.to] = Math.min(d[e.from][e.to], e.weight);
            d[e.to][e.from] = Math.min(d[e.to][e.from], e.weight);
        }
        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    d[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);

        for (int s = 0; s < n; s++) {
            long[] dist = Dijkstra.run(g, s).dist;
            for (int t = 0; t < n; t++) {
                if (dist[t] != d[s][t]) return false;
            }
        }
        return true;
    }
}