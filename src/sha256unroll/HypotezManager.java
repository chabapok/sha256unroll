package sha256unroll;

import java.util.ArrayList;
import java.util.HashMap;


public class HypotezManager {
    static int hypNum =0;
    
    static ArrayList<Hypotez> rootHypotez=new ArrayList();
    
    static Hypotez lastHypotez;
    
    static Hypotez openHypotez(Node node, int index, Val v){
        Hypotez h = createEmpty(node);
        h.values[index] = v;
        h = node.registerHypotez(h);
        return h;
    }

    static Hypotez openHypotez(Node node, Val ... v){
        Hypotez h = createEmpty(node);
        if (h.values.length!=v.length) throw new RuntimeException("Length inconsistent");
        h.values = v;
        h = node.registerHypotez(h);
        return h;
    }

    
    
    static Hypotez createEmpty(Node node){
        Hypotez h = new Hypotez();
        h.values = new Val[ node.parentNodes.size() ];
        h.num = hypNum;
        h.node = node;
        
        for(int i=0; i<h.values.length; i++){
            h.values[i] = Val.ANY;
        }
        hypNum++;
        
        if (lastHypotez != null){
            h.parent = lastHypotez;
            lastHypotez.next.add(h);
        }else{
            rootHypotez.add(h);
        }
        lastHypotez = h;
        return h;
    }
    
    static void closeHypotez(Hypotez h){
        lastHypotez = lastHypotez.parent;
    }
    
    
    public static void addDeduction(String variable, Val value){
        lastHypotez.addDeduction(variable, value);
    }

    static String branchPath() {
        return lastHypotez.branchPath();
    }

    
    
    static HypotezGroup prev;
    
    static HypotezGroup createGroup(Node n) {
        HypotezGroup h = new HypotezGroup();
        h.prev = prev;
        prev = h;
        return h;
    }
    
}
