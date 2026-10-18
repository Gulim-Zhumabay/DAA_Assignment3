import graph.Graph;
import mst.Kruskal;
import mst.MSTResult;
import mst.Prim;
import connectivity.SCC;
import connectivity.SCCResult;
import connectivity.TopoResult;
import connectivity.TopoSort;

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

        Graph dag = new Graph(3, true);
        dag.addEdge(0, 1, 1);
        dag.addEdge(1, 2, 1);
        dag.addEdge(0, 2, 1);
        TopoResult t1 = TopoSort.run(dag);
        System.out.println("dag order: " + t1.order + " cycle: " + t1.hasCycle);

        Graph cyc = new Graph(3, true);
        cyc.addEdge(0, 1, 1);
        cyc.addEdge(1, 2, 1);
        cyc.addEdge(2, 0, 1);
        TopoResult t2 = TopoSort.run(cyc);
        System.out.println("cycle graph cycle: " + t2.hasCycle);

        Graph sg = new Graph(6, true);
        sg.addEdge(0, 1, 1);
        sg.addEdge(1, 2, 1);
        sg.addEdge(2, 0, 1);
        sg.addEdge(2, 3, 1);
        sg.addEdge(3, 4, 1);
        sg.addEdge(4, 5, 1);
        sg.addEdge(5, 3, 1);
        System.out.println("scc: " + SCC.run(sg).components);
    }

}
