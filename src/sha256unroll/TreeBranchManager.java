package sha256unroll;

import java.util.ArrayList;

/**
 *
 * @author Pelepeichenko A.V.
 */
public class TreeBranchManager {

    static ArrayList<Node> path = new ArrayList<Node>();
    
    static void atEnter(Node n){
        path.add(n);
    }
    
    static void atExit(){
        path.remove(path.size()-1);
    }
    
    
    static String current(){
        StringBuilder sb = new StringBuilder();
        Node prevNode=null;
        String delim ="";
        for(Node n:path){
            if (n!=prevNode){
                sb.append(delim).append(n.name);
                delim="->";
            }
            prevNode = n;
        }
        return sb.toString();
    }
    
}

class NodeHolder{
    Node node;
    
}
