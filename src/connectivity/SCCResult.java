package connectivity;

import java.util.List;

public class SCCResult {
    public final List<List<Integer>> components;
    public final long ops;

    public SCCResult(List<List<Integer>> components, long ops) {
        this.components = components;
        this.ops = ops;
    }
}