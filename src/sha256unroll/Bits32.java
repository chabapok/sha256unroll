package sha256unroll;

import java.util.ArrayList;
import java.util.Collection;
import static sha256unroll.Utils.*;


public class Bits32{

    private Node[] nodes = new Node[32];
    
    
    
    void setTo(int v){
        for(int i=0; i<32; i++){
            ((EndNode)nodes[i]).init( ((v&1) == 1)? '1' : '0' );
            v = v>>>1;
        }
    }
    
    void setBit(int bit, char v){
         ((EndNode)nodes[bit]).init(v);
    }
    
    
    static Bits32 create(int init){
        Bits32 b = new Bits32();

        for(int i=0; i<32; i++){            
            b.nodes[i] = VariableManager.lastInstance.constNode( ((init&1) == 1)? '1' : '0' );
            init = init>>>1;
        }
        return b;
    }
    
    static Bits32 createVar(){
        Bits32 b = new Bits32();

        for(int i=0; i<32; i++){
            b.nodes[i] = VariableManager.lastInstance.createNext();            
        }
        return b;
    }
    
    
    static Bits32 add(Bits32 v1, Bits32 v2){
        Bits32 b = new Bits32();
        Node carry=null;
        for(int i=0; i<32; i++){
            if (carry==null)
                b.nodes[i] = xor(v1.nodes[i], v2.nodes[i]);
            else
                b.nodes[i] = xor(v1.nodes[i], v2.nodes[i], carry);
            
            Node ab =and(v1.nodes[i], v2.nodes[i]);
            if (carry==null){
                carry = ab;
            }else{
                Node apb = or(v1.nodes[i], v2.nodes[i]);
                Node capb = and(carry, apb);
                carry = or(ab, capb);
            }
        }
        return b;
    }
    
    
    Collection<String> probeVal(int v){
        
        char[] ch = new char[32];
        for(int i=0; i<32; i++){
            ch[i] =  ((v&1) == 1)? '1' : '0';
            v = v>>>1;
        }
        Collection<String> results = null;
        for(int i=0; i<32; i++){
            Collection<String> variants = nodes[i].probeVal( ch[i] );
            
            if (results==null){
                results = variants;
            }else{
                results = combineNotConflicted(results, variants);
            }
        }
        return results;
    }
    
    
    int calc(){
       char[] values = calcBinary();
       String result = String.valueOf(values);
       int r = Integer.parseInt(result, 2);
       return r;
    }
    
    char[] calcBinary(){
       char[] values = new char[32];
       for(int i=0; i<32; i++){
           values[31-i] = nodes[i].calc();
       }
       return values;
    }
    
    
    
    static String[] split(String v){
        ArrayList<String> al = new ArrayList();
        
        while(!v.isEmpty()){
            String s;
            if (v.length()>32){
                s = v.substring(0, 32);
                v= v.substring(32);
            }
            else{
                s = v;
                v="";
            }
            al.add(s);
        }
        return al.toArray(new String[al.size()]);
    }
    
    
    Bits32 rr(int count){
        Bits32 b = new Bits32();
        for(int i = 0; i<32; i++){
            int target = i-count;
            if (target<0) target+=32;
            b.nodes[target] = nodes[i];
        }
        return b;
    }
    
}
