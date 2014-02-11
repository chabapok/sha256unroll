package sha256unroll;

public class Bits8 {
    String name;
    
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

    byte toByte() {
        int v =0;
        for(int i=0; i<8; i++){
            //System.out.println("calc "+name+"."+i);
            char r = nodes[i].calc();
            v = v>>>1;
            if(r=='1') v = v | 128;
            else if (r!='0') System.out.println("Warn "+r);
        }
        return (byte)v;
    }

    
    
    String hexStr() {
        byte b = toByte();
        String s = Integer.toHexString( b & 0xff );
        if (s.length()==1) s= '0'+s;
        return s;
    }

}
