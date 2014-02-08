package sha256unroll;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import static sha256unroll.Utils.*;

public class Sha256Unroll {

    
    public static void main(String[] args) {
        EndNode x1 = new EndNode(1);
        EndNode x2 = new EndNode(2);
        EndNode x3 = new EndNode(3);
        EndNode x4 = new EndNode(4);
        
        char or  = '+';
        char and = '*';
        char not = '!';
        
        //Node m1 = new Node(and, x1, x2,x3,x4);m1.name = "m1";
        
        Node m2 = new Node(and, x2,x3); m2.name = "m2";
        Node m3 = new Node(not, m2); m3.name = "m3";
        Node m4 = new Node(and, x1, m3);m4.name = "m4";
        
        Node m5 = new Node(or, x2, x1);m5.name = "m5";
        Node m6 = new Node(and, x4, m5);m6.name = "m6";
        
        /*
        Node m7 = new Node(not, x3);        m7.name = "m7";
        Node m8 = new Node(and, m7, x1, x4);m8.name = "m8";
        
        
        Node m9 = new Node(not, x1); m9.name="m9";
        Node m10 = new Node(not, x2); m10.name="m10";
        Node m11 = new Node(not, x3); m11.name="m11";
        Node m12 = new Node(not, x4); m12.name="m12";
        Node m13 = new Node(and, m9, m10, m11, m12); m13.name="m13";
        */
        
        Node result = new Node(or, m4, m6);result.name = "result";
        
        Collection<String> variants0= result.probeVal('0');
        System.out.println("v0="+variants0);

        Collection<String> variants1= result.probeVal('1');
        System.out.println("v1="+variants1);

    }
    
    
    
}
