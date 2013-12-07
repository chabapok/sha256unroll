package sha256unroll;

import java.util.Map;

/**
 *
 * @author Pelepeichenko A.V.
 */
public class ActXor implements IAct{

    @Override
    public String name() {
        return "^";
    }

    @Override
    public void unroll(Node node, Val v) {
        if (v == Val.ANY){
            for(Node n: node.parentNodes){
                n.unroll(v);
            }
            return;
        }
        
        if (node.parentNodes.size()==3){ unroll3(node, v); return;}
        if (node.parentNodes.size()!=2){ throw new RuntimeException("xor on more, than 2 values");}

        if (v == Val.NULL){
            
            Hypotez h = HypotezManager.openHypotez(node, Val.NULL, Val.NULL );
            node.unroll(h);
            h.close();
            
            h = HypotezManager.openHypotez(node, Val.ONE, Val.ONE );
            node.unroll(h);
            h.close();
            
        }else{
            
            Hypotez h = HypotezManager.openHypotez(node, Val.NULL, Val.ONE );
            node.unroll(h);
            h.close();
            
            h = HypotezManager.openHypotez(node, Val.ONE, Val.NULL );
            node.unroll(h);
            h.close();            
        }
    }

    private void unroll3(Node node, Val v){
        if (v==Val.NULL){
            
            Hypotez h;
            
            h = HypotezManager.openHypotez(node, Val.NULL, Val.NULL, Val.NULL );
            node.unroll(h);
            h.close();
            
            h = HypotezManager.openHypotez(node, Val.NULL, Val.ONE, Val.ONE );
            node.unroll(h);
            h.close();
            
            h = HypotezManager.openHypotez(node, Val.ONE, Val.NULL, Val.ONE );
            node.unroll(h);
            h.close();
            
            h = HypotezManager.openHypotez(node, Val.ONE, Val.ONE, Val.NULL );
            node.unroll(h);
            h.close();
            
        }else{
            
            Hypotez h;
            h = HypotezManager.openHypotez(node, Val.NULL, Val.NULL, Val.ONE );
            node.unroll(h);
            h.close();
            
            h = HypotezManager.openHypotez(node, Val.NULL, Val.ONE, Val.NULL );
            node.unroll(h);
            h.close();
            
            h = HypotezManager.openHypotez(node, Val.ONE, Val.NULL, Val.NULL );
            node.unroll(h);
            h.close();
            
            h = HypotezManager.openHypotez(node, Val.ONE, Val.ONE, Val.ONE );
            node.unroll(h);
            h.close();
                        
        }
    }
    
    
    @Override
    public Val calc(Node node, Map<String, Val> v) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
