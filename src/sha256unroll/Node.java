package sha256unroll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


public class Node {
    
    ArrayList<Node> parentNodes;
    IAct action;
    
    String name;
    
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
        action.unroll(this, v);
    }

    void unroll(Hypotez h) {
        int i=0;
        for(Node n : parentNodes){
            n.unroll(h.values[i]);
            i++;
        }
    }
    
    
    
    Val calc(Map<String, Val> v){
        return action.calc(this, v);
    }
    
}
