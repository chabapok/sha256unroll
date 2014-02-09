package sha256unroll;


import java.util.Collection;
import static sha256unroll.Utils.*;

public class Sha256Unroll {

    
    public static void main(String[] args) {
        VariableManager vars = new VariableManager(4);
        
        EndNode x1 = vars.node(0);
        EndNode x2 = vars.node(1);
        EndNode x3 = vars.node(2);
        EndNode x4 = vars.node(3);
        
        
        Node m1 = and(x1, x2,x3,x4);m1.name = "m1";
        
        Node m2 = and(x2, x3); m2.name = "m2";
        Node m3 = not(m2); m3.name = "m3";
        Node m4 = and(x1, m3);m4.name = "m4";
       
        Node m5 = or(x2, x1);m5.name = "m5";
        Node m6 = and(x4, m5);m6.name = "m6";
        
        
        Node m7 = not(x3);        m7.name = "m7";
        Node m8 = and(m7, x1, x4);m8.name = "m8";
        
        
        Node m9  = not(x1); m9.name="m9";
        Node m10 = not(x2); m10.name="m10";
        Node m11 = not(x3); m11.name="m11";
        Node m12 = not(x4); m12.name="m12";
        Node m13 = and( m9, m10, m11, m12); m13.name="m13";

        
        //Node result = or(m1, m4, m6, m8);result.name = "result";
        Node result = or(m1, m4, m6, m8, m13);result.name = "result";
        
        printTablIst(vars, result, 4);
        
        vars.init( 3, '1');
        printVariants(result);
        
        System.out.println("");
        
        result = xor(x1, x2, x3);result.name = "result";
        printTablIst(vars, result, 3);
        printVariants(result);
        
    }
    
    static void printVariants(Node result){
        Collection<String> variants0= result.probeVal('0');
        System.out.println("v0="+variants0);

        Collection<String> variants1= result.probeVal('1');
        System.out.println("v1="+variants1);
    }
    
    
    static void printTablIst(VariableManager vm, Node result, int bitCount){
        
        for(int i=0; i<Math.pow(2, bitCount); i++){
            String representation = Integer.toBinaryString(i);
            while(representation.length() < bitCount){
                representation = "0"+representation;
            }
            vm.init(representation);
            char r = result.calc();
            System.out.println(representation+"|"+r);
        }
        vm.reset();
    }
    
    
    
}
