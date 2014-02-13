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
    
    
    Bits32 add1( Bits32 v2 ){
        Bits32 r = new Bits32();
        Node carry=null;
        for(int i=0; i<32; i++){
            Node a =nodes[i];
            Node b =v2.nodes[i];
            Node c = carry;
            
            if (carry==null){
                
                Node n1 = Utils.not(nodes[i]);
                Node n2 = Utils.not(v2.nodes[i]);
                
                r.nodes[i] = Utils.or( Utils.and(nodes[i], n2), Utils.and(n1, v2.nodes[i]) );
                carry = Utils.and(nodes[i], v2.nodes[i]);
                
            }else{
            
                
                Node na = Utils.not(nodes[i]);
                Node nb = Utils.not(v2.nodes[i]);
                Node nc = Utils.not(c);
                
                Node x4 = Utils.and(na, b);
                Node x3 = Utils.and(a, nb);
                Node x2 = Utils.and(a, b);
                Node x1 = Utils.and(x2, c);
                
                Node xx1= Utils.and(na,nb);
                Node xx = Utils.and(c, xx1);
                Node gg = Utils.and(nc, x3);
                Node pp = Utils.and(nc, x4);
                
                
                Node vv =Utils.or(x4, x3);
                Node aa = Utils.and(nc, vv);
                r.nodes[i] = Utils.or(aa, xx, x1);
                
                Node cpp = Utils.and(nc, x2);
                Node cgg = Utils.and(c, x4);
                Node cxx = Utils.and(c, x3);
                carry =Utils.or( Utils.and( c, vv), x2);
            }
 
            
           
        }
        return r;
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
    
    
    
    Bits32 add2( Bits32 v2 ){
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
    
    Collection<byte[]> probeVal(int v){
        
        byte[] ch = new byte[32];
        for(int i=0; i<32; i++){
            ch[i] =  ((v&1) == 1)? (byte)'1' : (byte)'0';
            v = v>>>1;
        }
        Collection<byte[]> results = null;
        for(int i=0; i<32; i++){
            Collection<byte[]> variants = nodes[i].probeVal( ch[i] );
            
            if (results==null){
                results = variants;
            }else{
                results = combineNotConflicted(results, variants);
            }
        }
        return results;
    }
    
    
    int calc(){
       byte[] values = calcBinary();
       String result = String.valueOf(values);
       int r = Integer.parseInt(result, 2);
       return r;
    }
    
    byte[] calcBinary(){
       byte[] values = new byte[32];
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
