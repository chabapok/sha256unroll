package sha256unroll;

/**
 *
 * @author Pelepeichenko A.V.
 */
public class ActAnd implements IAct{

    public static final ActAnd instance = new ActAnd();
    
    
    @Override
    public String name() {
        return "&";
    }

    @Override
    public void unroll(Node node, Val v) {
        switch(v){
            case ANY:
            case NANY:
            case ONE:
                for(Node n: node.parentNodes){
                    n.unroll(v);
                }
                return;
                
            case NULL:
                
                for(int i=0; i<node.parentNodes.size(); i++){
                    Hypotez h =  HypotezManager.openHypotez(node);
                    h.values[i] = Val.NULL;
                    node.unroll(h);
                    h.close();
                }
                
                return;
        }
        throw new RuntimeException("v is null");
    }
    
}
