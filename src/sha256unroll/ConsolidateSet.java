package sha256unroll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static sha256unroll.Utils.mayConverted;

/**
 *
 * @author chabapok
 */
public class ConsolidateSet extends ArrayList<byte[]> {

    ConsolidateSet() {
        super();
    }

    ConsolidateSet(int capacity) {
        super(capacity);
    }

    void consolidate(String... args) {
        ArrayList<byte[]> al = new ArrayList(Arrays.asList(args));
        consolidateArr(al);
    }

    
    void consolidateArr(Collection<byte[]> arr) {
        for(byte[] s:arr){
            consolidate(s);
        }
    }
    
    void consolidate(byte[] v) {
        if (isEmpty()){
            add(v);
            return; 
        }
        
        for(int i=0; i<size(); i++){
            byte[] s = get(i);
            
            byte[] sf = calcSetOf(v, s);
            if (sf!=null){
                set(i, sf);
                return;
            }
        }
        add(v);
        return;
    }

  
    static int counter = 0;

    /**
     * Обрабатывает 2 ситуации:
     *
     * 1. Если одно значение является надмножеством другого, то оно возвращается
     * (значит его надо оставить, а другое ненужно) например: *1*1 01*1
     *
     * в этом примере второе ненужно, т.к. попадает в множетсво, которое
     * описывается первым
     *
     *
     * 2. Если два значения разные, но их можно описать одним множеством,
     * например: *1*1 *0*1 можно заменить оба на: ***1
     *
     *
     *
     * Если нет, то null.
     *
     * @param a
     * @param b
     * @return
     */
    private byte[] calcSetOf(byte[] aStr, byte[] bStr) {
        if (aStr.length == bStr.length) {

            //counter++;
            //if ((counter%100000)==0)
            //   System.out.println(aStr);
            boolean aSetofB = true;
            boolean bSetofA = true;

            int bitChange = -1;
            
            for (int i = 0; i < aStr.length; i++) {
                byte a = aStr[i]; byte b = bStr[i];
                if (a != b) {
                    if (a != '*') bSetofA = false;
                    if (b != '*') aSetofB = false;
                    
                    if (a !='*' && b!='*'){
                        if (bitChange==-1){
                            bitChange = i;
                        }else{
                            bitChange = -2;
                        }
                    }else{bitChange = -2;}
                    
                    if (!bSetofA && !aSetofB && bitChange==-2) return null;
                }                
            }
            if (bitChange>=0){
                byte[] b = aStr.clone();
                b[bitChange] = (byte)'*';
                return b;
            }
            return aSetofB ? bStr : aStr;

        } else {
            throw new RuntimeException("length not same a=" + aStr + " b=" + bStr);
        }
    }

}
