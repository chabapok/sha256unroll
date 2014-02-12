package sha256unroll;

import java.util.ArrayList;
import java.util.Collection;
import static sha256unroll.Utils.*;


public class Bits32{

    private final Node[] nodes = new Node[32];
    
    
    ArrayList<Bits8> toBits8Arr(){
        ArrayList<Bits8> r = new ArrayList();
        Bits8 v = new Bits8();
        v.copyFrom(nodes, 24, 8);
        r.add(v);
        
        v = new Bits8();
        v.copyFrom(nodes, 16, 8);
        r.add(v);
        
        v = new Bits8();
        v.copyFrom(nodes, 8, 8);
        r.add(v);
        
        v = new Bits8();
        v.copyFrom(nodes, 0, 8);
        r.add(v);
        
        return r;
    }
    
    
    static Bits32 fromBits8Arr(Bits8 v24, Bits8 v16, Bits8 v8, Bits8 v0){
        Bits32 result = new Bits32();
        
        System.arraycopy(v24.nodes, 0, result.nodes, 24, 8);
        System.arraycopy(v16.nodes, 0, result.nodes, 16, 8);
        System.arraycopy(v8.nodes,  0, result.nodes, 8, 8);
        System.arraycopy(v0.nodes,  0, result.nodes, 0, 8);
        
        return result;
    }
    
    
            
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
    
    
    Bits32 add( Bits32 v2 ){
        Bits32 b = new Bits32();
        Node carry=null;
        for(int i=0; i<32; i++){
            if (carry==null)
                b.nodes[i] = Utils.xor(nodes[i], v2.nodes[i]);
            else
                b.nodes[i] = Utils.xor(nodes[i], v2.nodes[i], carry);
            
            Node ab =Utils.and(nodes[i], v2.nodes[i]);
            if (carry==null){
                carry = ab;
            }else{
                Node apb = Utils.or(nodes[i], v2.nodes[i]);
                Node capb = Utils.and(carry, apb);
                carry = Utils.or(ab, capb);
            }
        }
        return b;
    }
    
    
    
    Bits32 add1( Bits32 v2 ){
        Bits32 b = new Bits32();
        Node carry=null;
        for(int i=0; i<32; i++){
            if (carry==null)
                b.nodes[i] = Utils.xor(nodes[i], v2.nodes[i]);
            else
                b.nodes[i] = Utils.sum(nodes[i], v2.nodes[i], carry);
            
            
            if (carry==null && i!=31){
                carry = Utils.and(nodes[i], v2.nodes[i]);
            }else{
                carry = Utils.carry(nodes[i], v2.nodes[i], carry);
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
    
    Bits32 shr(int count){
        Bits32 b = new Bits32();
        ConstNode ZERO = VariableManager.lastInstance.constNode('0');
        
        for(int i = 0; i<32; i++){
            int target = i-count;
            if (target<0){
                target+=32;
                b.nodes[target] = ZERO;
            }else{
                b.nodes[target] = nodes[i];
            }
        }
        return b;
    }
    
    
    Bits32 xor(Bits32 other){
        Bits32 b = new Bits32();
        for(int i = 0; i<32; i++){
            b.nodes[i] = Utils.xor( nodes[i], other.nodes[i] );
        }
        return b;
    }
    
    Bits32 and(Bits32 other){
        Bits32 b = new Bits32();
        for(int i = 0; i<32; i++){
            b.nodes[i] = Utils.and( nodes[i], other.nodes[i] );
        }
        return b;
    }

    Bits32 or(Bits32 other){
        Bits32 b = new Bits32();
        for(int i = 0; i<32; i++){
            b.nodes[i] = Utils.or( nodes[i], other.nodes[i] );
        }
        return b;
    }
    
    Bits32 not(){
        Bits32 b = new Bits32();
        for(int i = 0; i<32; i++){
            b.nodes[i] = Utils.not( nodes[i] );
        }
        return b;
    }
    
}
