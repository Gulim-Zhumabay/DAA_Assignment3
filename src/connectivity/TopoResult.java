package connectivity;

import java.util.List;

public class TopoResult {
    public final List<Integer> order;
    public final boolean hasCycle;
    public final long ops;

    public TopoResult(List<Integer> order, boolean hasCycle, long ops) {
        this.order = order;
        this.hasCycle = hasCycle;
        this.ops = ops;
    }
}