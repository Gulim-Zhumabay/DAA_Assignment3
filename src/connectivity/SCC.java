package connectivity;

import graph.Edge;
import graph.Graph;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class SCC {
    private boolean[] visited;
    private final Deque<Integer> stack = new ArrayDeque<>();
    private List<List<Integer>> rev;
    private long ops = 0;

    public static SCCResult run(Graph g) {
        return new SCC().compute(g);
    }

    private SCCResult compute(Graph g) {
        int n = g.size();
        visited = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (!visited[i]) dfs1(g, i);
        }

        rev = new ArrayList<>();
        for (int i = 0; i < n; i++) rev.add(new ArrayList<>());
        for (Edge e : g.allEdges()) {
            rev.get(e.to).add(e.from);
            ops++;
        }

        visited = new boolean[n];
        List<List<Integer>> components = new ArrayList<>();
        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited[v]) {
                List<Integer> comp = new ArrayList<>();
                dfs2(v, comp);
                components.add(comp);
            }
        }

        return new SCCResult(components, ops);
    }

    private void dfs1(Graph g, int u) {
        visited[u] = true;
        for (Edge e : g.neighbors(u)) {
            ops++;
            if (!visited[e.to]) dfs1(g, e.to);
        }
        stack.push(u);
    }

    private void dfs2(int u, List<Integer> comp) {
        visited[u] = true;
        comp.add(u);
        for (int v : rev.get(u)) {
            ops++;
            if (!visited[v]) dfs2(v, comp);
        }
    }
}