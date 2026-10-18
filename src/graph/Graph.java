package graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int n;
    private final boolean directed;
    private final List<List<Edge>> adj = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int u, int v, int w) {
        edges.add(new Edge(u, v, w));
        adj.get(u).add(new Edge(u, v, w));
        if (!directed) adj.get(v).add(new Edge(v, u, w));
    }

    public int size() { return n; }
    public boolean isDirected() { return directed; }
    public List<Edge> neighbors(int u) { return adj.get(u); }
    public List<Edge> allEdges() { return edges; }
}