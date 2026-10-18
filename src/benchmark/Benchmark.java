package benchmark;

import connectivity.SCC;
import connectivity.TopoSort;
import graph.Graph;
import graph.GraphGenerator;
import mst.Kruskal;
import mst.MSTResult;
import mst.Prim;
import shortestpath.Dijkstra;
import shortestpath.DijkstraResult;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Benchmark {
    private static final int RUNS = 5;
    private static final long SEED = 42;
    private static final int[][] SIZES = {{10, 30}, {100, 500}, {1000, 5000}};
    private static final String[] NAMES = {"small", "medium", "large"};

    interface Task {
        long[] run();
    }

    public static void main(String[] args) throws IOException {
        List<String> rows = new ArrayList<>();

        for (int i = 0; i < SIZES.length; i++) {
            int n = SIZES[i][0];
            int m = SIZES[i][1];
            String name = NAMES[i];

            Graph und = GraphGenerator.connectedUndirected(n, m, SEED);
            Graph dag = GraphGenerator.dag(n, m, SEED);
            Graph cyc = GraphGenerator.directedRandom(n, m, SEED);

            rows.add(measure("kruskal", name, n, m, () -> {
                MSTResult r = Kruskal.run(und);
                return new long[]{r.edgesExamined, r.otherOps};
            }));
            rows.add(measure("prim", name, n, m, () -> {
                MSTResult r = Prim.run(und);
                return new long[]{r.edgesExamined, r.otherOps};
            }));
            rows.add(measure("topo_dag", name, n, m, () ->
                    new long[]{TopoSort.run(dag).ops, 0}));
            rows.add(measure("topo_cyclic", name, n, m, () ->
                    new long[]{TopoSort.run(cyc).ops, 0}));
            rows.add(measure("scc", name, n, m, () ->
                    new long[]{SCC.run(cyc).ops, 0}));
            rows.add(measure("dijkstra", name, n, m, () -> {
                DijkstraResult r = Dijkstra.run(und, 0);
                return new long[]{r.relaxations, r.pqOps};
            }));
        }

        Path dir = Path.of("results", "tables");
        Files.createDirectories(dir);
        try (PrintWriter w = new PrintWriter(Files.newBufferedWriter(dir.resolve("results.csv")))) {
            w.println("algorithm,graph,v,e,avg_time_ns,ops_1,ops_2");
            for (String row : rows) w.println(row);
        }

        System.out.println("algorithm,graph,v,e,avg_time_ns,ops_1,ops_2");
        for (String row : rows) System.out.println(row);
    }

    private static String measure(String algo, String graphName, int v, int e, Task task) {
        for (int i = 0; i < 50; i++) task.run();
        long total = 0;
        long[] ops = null;
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            ops = task.run();
            total += System.nanoTime() - start;
        }
        return algo + "," + graphName + "," + v + "," + e + ","
                + (total / RUNS) + "," + ops[0] + "," + ops[1];
    }
}