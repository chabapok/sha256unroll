package sha256unroll;

public class Bits8 {
    
    Node[] nodes = new Node[8];
    
    
    static Bits8 create(int init){
        Bits8 b = new Bits8();

        for(int i=0; i<8; i++){            
            b.nodes[i] = VariableManager.lastInstance.constNode( ((init&1) == 1)? '1' : '0' );
            init = init>>>1;
        }
        return b;
    }
    
    
    void copyFrom(Node[] n, int index, int len){
        for(int i=0; i<len; i++){
            nodes[i] = n[i+index];
        }
    }

}
