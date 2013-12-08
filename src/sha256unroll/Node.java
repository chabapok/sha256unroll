package sha256unroll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


public class Node {
    
    ArrayList<Node> parentNodes;
    IAct action;
    
    String name;
    
    //что в ноду пришло снизу
    Val unrolled;
    
    public Node(IAct action, Node ... args){
        parentNodes = new ArrayList();
        parentNodes.addAll(Arrays.asList(args));
        this.action = action;
    }
    
    public Node(ArrayList<Node> parentNodes, IAct action){
        this.parentNodes = parentNodes;
        this.action = action;
    }
    
       
    void unroll(Val v){
        unrolled = v;
        //TreeBranchManager.atEnter(this);
        action.unroll(this, v);
       // TreeBranchManager.atExit();
    }

    void unroll(Hypotez h) {
       // TreeBranchManager.atEnter(this);
        int i=0;
        for(Node n : parentNodes){
            n.unroll(h.values[i]);
            i++;
        }
       // TreeBranchManager.atExit();
    }
    
    
    
    Val calc(Map<String, Val> v){
        return action.calc(this, v);
    }

    
    HypotezGroup hypotezGroup;
    Hypotez registerHypotez(Hypotez h) {
        if (hypotezGroup==null){
            hypotezGroup = HypotezManager.createGroup(this);
        }
        
        return hypotezGroup.register(h);
    }

    void unregisterGypotez(Hypotez h) {
        if (hypotezGroup==null) return;
        hypotezGroup.unregister(h);
    }
    
}
