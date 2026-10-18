package dsu;

public class DSU {
    private final int[] parent;
    private final int[] rank;
    private int components;
    public long findCalls = 0;
    public long unionCalls = 0;

    public DSU(int n) {
        parent = new int[n];
        rank = new int[n];
        components = n;
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    public int find(int x) {
        findCalls++;
        int root = x;
        while (parent[root] != root) root = parent[root];
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    public boolean union(int a, int b) {
        unionCalls++;
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) return false;
        if (rank[ra] < rank[rb]) { int t = ra; ra = rb; rb = t; }
        parent[rb] = ra;
        if (rank[ra] == rank[rb]) rank[ra]++;
        components--;
        return true;
    }

    public int components() { return components; }
}