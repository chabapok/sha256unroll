package sha256unroll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author chabapok
 */
public class Utils {
    
    static byte not(byte v){
        if (v=='*') return '*';
        if (v=='1') return '0';
        return '1';
    }
    
    //"a" может превращаться в "b"
    static boolean mayConverted(byte a, byte b) {
        return a==b || (a=='*');
    }
    
/*    
    static Collection<String> combineNotConflicted(Collection<String> aArr, Collection<String> bArr, Collection<String> cArr){
        Collection<String> rr = combineNotConflicted(aArr, bArr);
        Collection<String> r = combineNotConflicted(rr, cArr);
        return r;
    }
  */  
    static int counter=0;
    
    static Collection<byte[]> combineNotConflicted(Collection<byte[]> aArr, Collection<byte[]> bArr){
        /*
        counter++;
        if ((counter%1000)==0)
            System.out.println("Combine "+aArr.size()+":"+bArr.size());
        */
        ConsolidateSet result = new ConsolidateSet( aArr.size() + bArr.size() );
        
        int i=0,j=0;
        
        for(byte[] aVariant:aArr ){
            //System.out.printf("\rCombine  "+i+":"+j+":"+result.size());
            i++;
            j=0;
            for(byte[] bVariant:bArr ){
                byte[] combined = combine(aVariant, bVariant);
                if (combined!=null) result.consolidate(combined);
                j++;
            }    
        }
        //System.out.println("-->"+result.size());
        return result;
    }

    /**
     * 
     * Уточнение.
     *  0*, *0, 00 -> 0
     *  1*, *1, 11 -> 1
     *  ** - >*
     * 01,10 - запрещено
     * 
     * @param aVariant
     * @param bVariant
     * @return 
     */
    static byte[] combine(byte[] aVariant, byte[] bVariant) {
        if (aVariant.length!=bVariant.length) throw new RuntimeException("Length not same! "+aVariant+":"+bVariant);
        
        byte[] sb = new byte[aVariant.length];
        
        for(int i=0; i<aVariant.length; i++){
            byte a = aVariant[i];
            byte b = bVariant[i];
            
            if (a=='*') {sb[i]=b; continue;}
            if (b=='*' || a==b) {sb[i]=a; continue;}
            return null; //01 or 10
        }
        return sb;
    }
    
    /*
    static Collection<String> removeDupes(Collection<String> aArr, Collection<String> bArr, Collection<String> cArr){
        Collection<String> rr = removeDupes(aArr, bArr);
        Collection<String> r = removeDupes(rr, cArr);
        return r;
    }
    */
    
    static Collection<byte[]> removeDupes(Collection<byte[]> aArr, Collection<byte[]> bArr){
        int tSize=aArr.size()+bArr.size();
        //System.out.print("rd "+aArr.size()+":"+bArr.size());
        ConsolidateSet result = new ConsolidateSet( aArr.size()+bArr.size() );
        result.consolidateArr(aArr);
        //result.addAll(aArr);
        result.consolidateArr(bArr);
        
        //if (result.size()<tSize) System.out.println("Real RemoveDupes! -> "+result.size() );
        //else System.out.println("-->"+result.size());
        return result;
    }

    
    static Node op(char op, Node ... args){
        
        Node n2 = new Node(op, args[0], args[1]);
        switch(args.length){
            case 2: return n2;
            case 3: return new Node(op, n2, args[2]);
            default: {
                    Node a[] = Arrays.copyOfRange(args, 2, args.length);
                    Node otherNodes = op(op, a);
                    Node r = new Node(op, n2, otherNodes);
                    return r;
                }
        }
    }
    
    static Node or(Node ... args){  return op('+', args); }
    static Node and(Node ... args){ return op('*', args); }
    static Node xor(Node ... args){ return op('^', args); }
    
    /*
    //так дольше
    static Node xor(Node a, Node b){ 
        Node v1=and(a, not(b));
        Node v2=and(b, not(a));
        return or(v1, v2 );
    }
    
    static Node xor(Node a, Node b, Node c){ 
        Node v1=xor(a, b);
        return xor(v1, c);
    }
    */
    
    static Node not(Node arg){      return new Node('!', arg); }
    
    static Node sum(Node a, Node b, Node c){
        return new Node('S', a,b,c);
    }
    
    static Node carry(Node a, Node b, Node c){
        return new Node('C', a,b,c);
    }
    
 /*
    static Bits32 and(Bits32 a, Bits32 b){return a.and(b);}
    static Bits32 or(Bits32 a, Bits32 b){return a.or(b);}
    static Bits32 xor(Bits32 a, Bits32 b){return a.xor(b);}
    static Bits32 not(Bits32 a){return a.not();}

    
    static Bits32 add(Bits32 v1, Bits32 v2){ return v1.add(v2);}
*/
    
    
    private static Node xNodes[] = new Node[1024];
    static Node x(int i){
        if (xNodes[i]==null){
            xNodes[i] =new EndNode(i);
        }
        return xNodes[i];
    }
    
    
    
    static Node[] consolidate(Bits8[] arr){
        Node[] result = new Node[arr.length*8];
        for(int i=0; i<arr.length; i++){
            System.arraycopy(arr[i].nodes, 0, result, i*8, 8);
        }
        return result;
    }
    
    
    static byte[] fromHexString(String s){
        if ( (s.length()&1) != 0) throw new RuntimeException("wrong string length");
        byte[] result = new byte[s.length()/2];
        for(int i=0, j=0; i<s.length(); i+=2, j++){
            String v = s.substring(i, i+2);
            int b = Integer.parseInt(v, 16);
            result[j] = (byte) b;
        }
        return result;
    }
    
    static byte[] getBitset(byte[] arr){
        
        byte[] result = new byte[arr.length*8];
        
        for(int i=0; i<arr.length; i++){
            byte a = arr[i];
            for(int j=0; j<8; j++){
                
                byte bit = ((a&1) == 0)? (byte)'0' : (byte)'1';
                result[i*8+j] = bit;
                
                a = (byte) (a>>>1);
            }
        }
        return result;
    }
    
    static Collection<byte[]> probeVal(byte[] needValues, Bits8[] arr){
        Node[] nodes = consolidate(arr);
        if (needValues.length != nodes.length) 
            throw new RuntimeException("Wrong lengths. NeedValues.len="+needValues.length+" and arr.size="+nodes.length+"bits");
        
        
        Collection<byte[]> results = null;
        for(int i=0; i<nodes.length; i++){
            
            System.out.printf("Probe node [%d/%d]\n", i, nodes.length );
            if (needValues[i]=='*') continue;
            Collection<byte[]> variants = nodes[i].probeVal( needValues[i] );
            
            if (results==null){
                results = variants;
            }else{
                results = combineNotConflicted(results, variants);
            }
        }
        return results;
    }

    
    
    
    static Bits8[] createXVars(int symCount){
        Bits8[] r = new Bits8[symCount];
        
        for(int i=0; i<r.length; i++){
            r[i] = Bits8.createX();
        }
        return r;
    }
    
    
    
    static Bits8[] fromString(String v){
        byte[] symbols = v.getBytes();
        Bits8[] r = new Bits8[symbols.length];
        
        for(int i=0; i<r.length; i++){
            r[i] = Bits8.create(symbols[i]);
        }
        return r;
    }
    
    static String foromBits8(Bits8[] b){
        byte[] r = new byte[b.length];
        
        for(int i=0; i<b.length; i++){
            r[i] = b[i].toByte();
        }
        String result = new String(r);
        return result;
    }
    
    
}
