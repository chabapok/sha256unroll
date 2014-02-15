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
    
    void consolidate(byte[] bStr) {
        if (isEmpty()){
            add(bStr);
            return; 
        }
        byte z = '*';
        
        
        metka:
        for(int i=0; i<size(); i++){
            byte[] aStr = get(i);
            byte[] sf = null;
            
            if (aStr.length != bStr.length) throw new RuntimeException("length not same!");

            boolean aSetofB = true;
            boolean bSetofA = true;

            int bitChange = -1;


            for (int j = 0; j < aStr.length; j++) {
                byte a = aStr[j]; byte b = bStr[j];
                if (a != b) {
                    if (a != z) bSetofA = false;
                    if (b != z) aSetofB = false;

                    if (a !=z && b!=z){
                        if (bitChange==-1){
                            bitChange = j;
                        }else{
                            bitChange = -2;
                        }
                    }else{bitChange = -2;}

                    if (!bSetofA && !aSetofB && bitChange==-2) continue metka;
                }                
            }
            if (bitChange>=0){
                sf = aStr.clone();
                sf[bitChange] = z;
            }else{
                sf = aSetofB ? bStr : aStr;
            }

            if (sf!=null){
                set(i, sf);
                return;
            }
        }
        add(bStr);
    }

  
    static int counter = 0;

}
