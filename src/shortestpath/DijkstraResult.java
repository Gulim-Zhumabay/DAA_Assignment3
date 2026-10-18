package shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DijkstraResult {
    public static final long INF = Long.MAX_VALUE;
    public final long[] dist;
    public final int[] prev;
    public final long relaxations;
    public final long pqOps;

    public DijkstraResult(long[] dist, int[] prev, long relaxations, long pqOps) {
        this.dist = dist;
        this.prev = prev;
        this.relaxations = relaxations;
        this.pqOps = pqOps;
    }

    public List<Integer> pathTo(int target) {
        List<Integer> path = new ArrayList<>();
        if (dist[target] == INF) return path;
        for (int v = target; v != -1; v = prev[v]) path.add(v);
        Collections.reverse(path);
        return path;
    }
}