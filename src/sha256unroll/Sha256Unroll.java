package sha256unroll;


import java.util.Map;
import java.util.TreeMap;
import static sha256unroll.Utils.*;

public class Sha256Unroll {

    
    public static void main(String[] args) {
        EndNode x1 = new EndNode("x1");
        EndNode x2 = new EndNode("x2");
        EndNode x3 = new EndNode("x3");
        EndNode x4 = new EndNode("x4");
        
        IAct or  = ActOr.instance;
        IAct and = ActAnd.instance;
        IAct not = ActNot.instance;
        
        Node m1 = new Node(and, x1, x2,x3,x4);m1.name = "m1";
        
        Node m2 = new Node(and, x2,x3); m2.name = "m2";
        Node m3 = new Node(not, m2);m3.name = "m3";
        Node m4 = new Node(and, x1, m3);m4.name = "m4";
        
        Node m5 = new Node(or, x2, x1);m5.name = "m5";
        Node m6 = new Node(and, x4, m5);m6.name = "m6";
        
        Node m7 = new Node(not, x3);        m7.name = "m7";
        Node m8 = new Node(and, m7, x1, x4);m8.name = "m8";
        
        Node result = new Node(or, m1, m4, m6, m8);result.name = "result";
        
        Node notResult = new Node(not, result); notResult.name = "notResult";
        Hypotez h = HypotezManager.openHypotez(notResult);
        h.values[0] = Val.NULL;
        notResult.unroll( h );
        h.close();
        
        println("hypotez count="+HypotezManager.hypNum);
        println("root hyp count="+HypotezManager.rootHypotez.size());
        
        
        for(int x=0; x<16; x++){
            Map<String, Val> m = createFor(x);
            Val r = result.calc(m);
            System.out.println("x="+x+" r="+r);
        }
    }
    
    
    
    static Map<String, Val> createFor(int v){
        Map<String, Val> m = new TreeMap();
        for(int i=1; i<32; i++){
            m.put("x"+i, ((v&1)==1) ? Val.ONE : Val.NULL );
            v = v>>1;
        }
        return m;
    }
    
    
}
