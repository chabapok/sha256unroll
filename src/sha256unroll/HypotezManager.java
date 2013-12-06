package sha256unroll;

import java.util.ArrayList;
import java.util.HashMap;


public class HypotezManager {
    static int hypNum =0;
    
    static ArrayList<Hypotez> rootHypotez=new ArrayList();
    
    static Hypotez lastHypotez;
    
    static Hypotez openHypotez(Node node, int index, Val v){
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
        if (index>=0)
            h.values[index] =v;
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
    
    public interface HypotezInitializator{
        void init(Hypotez h);
    }
    
}
