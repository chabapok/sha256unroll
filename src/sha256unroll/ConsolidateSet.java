package sha256unroll;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author chabapok
 */
public class ConsolidateSet extends HashSet<String>{

    void consolidate(Collection<String> arr) {
        if (isEmpty() && !arr.isEmpty()){
            for(String s: arr){
                add(s);
                break;
            }
        }
        
        metka:
        for(String addV: arr){
            for(String presentV: this){
                if (pokriv(presentV, addV)) continue metka;
                if (pokriv(addV, presentV)){
                    remove(presentV);
                    add(addV);
                    continue metka;
                }
            }
            add(addV);
        }
    }

    //проверка - то что еcть покрывает то, что хотим добавить
    private boolean pokriv(String presentV, String addV) {
        
        for(int i=0; i<presentV.length(); i++){
            char p = presentV.charAt(i);
            char a = addV.charAt(i);
            if ( (p=='*') || (p==a) ) continue;
            return false;
        }
        return true;
    }
    
}
