package sha256unroll;

import java.util.Map;

public class ActNot implements IAct{

    public static final ActNot instance = new ActNot();
    
    @Override
    public String name() {
        return "!";
    }

    @Override
    public void unroll(Node node, Val v) {
        Val notV= v.not();
        node.parentNodes.get(0).unroll(notV);
    }

    @Override
    public Val calc(Node node, Map<String, Val> v) {
        Val r = node.parentNodes.get(0).calc(v);
        return r.not();
    }


    
}
