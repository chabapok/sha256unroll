package sha256unroll;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import static sha256unroll.Utils.*;

public class Sha256Unroll {

    public static String hash256(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data.getBytes());
        return bytesToHex(md.digest());
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes) {
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    
    
    
    public static void main(String[] args) throws NoSuchAlgorithmException {
        
        String pass ="The quick brown fox jumps over the lazy dog";
        int symCount = pass.length();
        
        int n=1;
        VariableManager vm = VariableManager.create(n*8);
        
        System.out.println("Nodes eCount="+Node.eCount);
        //Bits8 [] b = Utils.fromString(pass);
        Bits8 [] b0 = Utils.createXVars(n);
        Bits8[] b1 = Utils.fromString(pass.substring(b0.length));
        Bits8[] b = new Bits8[b0.length+b1.length];
        System.arraycopy(b0, 0, b, 0, b0.length);
        System.arraycopy(b1, 0, b, b0.length, b1.length);
        
        System.out.println("Nodes eCount="+Node.eCount);
        
        Sha256 sha256 = new Sha256();
        sha256.update(b);
        
        Bits8[] digest = sha256.digest();
        System.out.println("digest calculated");
        System.out.println("Nodes allCount="+Node.allCount);
        System.out.println("Nodes andCount="+Node.andCount);
        System.out.println("Nodes orCount="+Node.orCount);
        System.out.println("Nodes xorCount="+Node.xorCount);
        System.out.println("Nodes notCount="+Node.notCount);
        System.out.println("Nodes eCount="+Node.eCount);
        System.out.println("Nodes cCount="+Node.cCount);
        
        System.out.println();
        System.out.println(hash256(pass));
        
        
        String hash = "d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592";
        byte[] bhash = Utils.fromHexString(hash);
        byte[] bits = Utils.getBitset(bhash);
        
        long t1 =System.currentTimeMillis();
        Collection<byte[]> result = Utils.probeVal(bits, digest);
        long diff = System.currentTimeMillis()-t1;
        System.out.println("diff="+diff+"ms");
        
        
        for(byte[] bb: result){
            String variant = new String(bb);
            System.out.println( variant );
            int c =parse(variant) ;
            System.out.println(c);
            System.out.println((char)c);
            
        }
        
        
        /*
        int i=0;
        for(Bits8 r: digest){
            r.name = "x"+i;
            i++;
            System.out.print(r.hexStr());
        }
        */
        
        
        
    }
    
    
    public static void main2(String[] args) throws NoSuchAlgorithmException {
        VariableManager vm = VariableManager.create(8);
                
        Bits8 [] b = Utils.fromString("The quick brown fox jumps over the lazy dog");
        //Bits8 [] b = Utils.fromString("1");
        
        String s = Utils.foromBits8(b);
        System.out.println("s="+s);
        
    }
    
    
    
    
    public static void main1(String[] args) throws NoSuchAlgorithmException {
        
                
        VariableManager vars = VariableManager.create(4);

        EndNode x1 = vars.node(0);
        EndNode x2 = vars.node(1);
        EndNode x3 = vars.node(2);
        EndNode x4 = vars.node(3);

        Node m1 = and(x1, x2, x3, x4);
        m1.name = "m1";

        Node m2 = and(x2, x3);
        m2.name = "m2";
        Node m3 = not(m2);
        m3.name = "m3";
        Node m4 = and(x1, m3);
        m4.name = "m4";

        Node m5 = or(x2, x1);
        m5.name = "m5";
        Node m6 = and(x4, m5);
        m6.name = "m6";

        Node m7 = not(x3);
        m7.name = "m7";
        Node m8 = and(m7, x1, x4);
        m8.name = "m8";

        Node m9 = not(x1);
        m9.name = "m9";
        Node m10 = not(x2);
        m10.name = "m10";
        Node m11 = not(x3);
        m11.name = "m11";
        Node m12 = not(x4);
        m12.name = "m12";
        Node m13 = and(m9, m10, m11, m12);
        m13.name = "m13";

        //Node result = or(m1, m4, m6, m8);result.name = "result";
        Node result = or(m1, m4, m6, m8);
        result.name = "result";

        ///printTablIst(vars, result, 4);
        
        //vars.init(3, '1');
        printVariants(result);
        //v0=[**00, 0**0, 011*]
        //v1=[**01, *0*1, 1**1, 1*1*]
        
        //v0=[**00, 0**0, 011*]
        //v1=[*0*1, **01, 1**1, 1*1*]
        
        /*
        vars = VariableManager.create(32);

        Bits32 v1 = Bits32.create(20);
        Bits32 v2 = Bits32.createVar();

        Bits32 r = add(v1, v2);

        Collection<String> res = r.probeVal(1235);
        for (String variant : res) {
            String[] toks = Bits32.split(variant);
            for (String tok : toks) {
                System.out.print(Integer.parseInt(tok, 2) + " ");
            }
            System.out.println("");
        }

        Bits32 xx = Bits32.create(-1);
        Bits32 zz = Bits32.create(0xC0);
        Bits32 xored = xx.xor(zz);

        System.out.println(xored.calcBinary());
        Bits32 yy = xx.shr(1);
        System.out.println(yy.calcBinary());
        yy = xx.shr(2);
        System.out.println(yy.calcBinary());
*/
        
        
        /*
         vars = VariableManager.create(2);
         x1 = vars.createNext();
         x2 = vars.createNext();
         result = xor(x1, x2);
         x1.init('1');
         Collection<String> res = result.probeVal('0');
         System.out.println(res);
         */
        /*
         System.out.println("");
        
         result = xor(x1, x2, x3);result.name = "result";
         printTablIst(vars, result, 3);
         printVariants(result);
         */
    }

    static void printVariants(Node result) {
        long t1 = System.nanoTime();
        Collection<byte[]> variants0 = result.probeVal((byte)'0');
        long t2 = System.nanoTime();
        System.out.println("v0=" + variants0);
        long t3 = System.nanoTime();
        Collection<byte[]> variants1 = result.probeVal((byte)'1');
        long t4 = System.nanoTime();
        System.out.println("v1=" + variants1);
        
        System.out.println("time="+( (t2-t1)+(t4-t3) ) );
    }

    
    public static void main8(String[] args) throws NoSuchAlgorithmException {
        
        VariableManager vars = VariableManager.create(3);

        EndNode x1 = vars.node(0);
        EndNode x2 = vars.node(1);
        EndNode x3 = vars.node(2);
        
        //Node m1 = sum(x1, x2, x3);
        Node m1 = carry(x1, x2, x3);
        printVariants(m1);
    }
    
    
    public static void main3(String [] arg){
        VariableManager vars = VariableManager.create(32);

        int p = 0x3ff;
        int p1 =0x5fff;
        Bits32 v1 = Bits32.create(p);
        Bits32 v2 = Bits32.createVar();

        Bits32 r = add(v1, v2);
        
        Collection<byte[]> res = r.probeVal(p1);
        
        for (byte[] b : res) {
            String variant = new String(b);
            String[] toks = Bits32.split(variant);
            for (String tok : toks) {
                System.out.print(parse(tok));
            }
            System.out.println("");
        }
        System.out.println("r="+(p1-p));
        
    }
    
    
    static void printTablIst(VariableManager vm, Node result, int bitCount) {

        for (int i = 0; i < Math.pow(2, bitCount); i++) {
            String representation = Integer.toBinaryString(i);
            while (representation.length() < bitCount) {
                representation = "0" + representation;
            }
            vm.init(representation);
            byte r = result.calc();
            System.out.println(representation + "|" + r);
        }
        vm.reset();
    }

    
    static int parse(String v){
        int r = 0;
        for(char c:v.toCharArray()){
            if (c=='1') r = (r<<1)|1;
            else r = r<<1;
        }
        return r;
    }
    
}
