import graph.Graph;
import graph.GraphGenerator;
import connectivity.TopoSort;
import mst.Kruskal;
import mst.Prim;

public class Main {
    public static void main(String[] args) {
        Graph g1 = GraphGenerator.connectedUndirected(100, 500, 42);
        Graph g2 = GraphGenerator.connectedUndirected(100, 500, 42);
        System.out.println("connected: " + Kruskal.run(g1).connected);
        System.out.println("kruskal: " + Kruskal.run(g1).totalWeight);
        System.out.println("prim: " + Prim.run(g1).totalWeight);
        System.out.println("same seed: " + (Kruskal.run(g2).totalWeight == Kruskal.run(g1).totalWeight));

        Graph d = GraphGenerator.dag(100, 500, 42);
        System.out.println("dag has cycle: " + TopoSort.run(d).hasCycle);

        Graph r = GraphGenerator.directedRandom(100, 500, 42);
        System.out.println("random has cycle: " + TopoSort.run(r).hasCycle);
    }
}