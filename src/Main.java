import graph.Graph;
import mst.Kruskal;
import mst.MSTResult;
import mst.Prim;

public class Main {
    public static void main(String[] args) {
        Graph g = new Graph(4, false);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 2);
        g.addEdge(0, 2, 3);
        g.addEdge(2, 3, 4);

        MSTResult r = Kruskal.run(g);
        System.out.println("weight: " + r.totalWeight);
        System.out.println("connected: " + r.connected);

        MSTResult p = Prim.run(g);
        System.out.println("prim weight: " + p.totalWeight);
        System.out.println("prim connected: " + p.connected);
    }

}
