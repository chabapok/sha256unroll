package sha256unroll;

import static sha256unroll.Val.ANY;
import static sha256unroll.Val.NANY;
import static sha256unroll.Val.NULL;
import static sha256unroll.Val.ONE;

public class ActOr implements IAct{

    public static final ActOr instance = new ActOr();
    
    @Override
    public String name() {
        return "+";
    }

    @Override
    public void unroll(Node node, Val v) {
        switch(v){
            case ANY:
            case NANY:
            case NULL:
                for(Node n: node.parentNodes){
                    n.unroll(v);
                }
                return;
                
            case ONE:
                
                for(int i=0; i<node.parentNodes.size(); i++){
                    Hypotez h =  HypotezManager.openHypotez(node);
                    h.values[i] = Val.ONE;
                    node.unroll(h);
                    h.close();
                }
                return;
        }
        throw new RuntimeException("v is null");

    }
    
}
