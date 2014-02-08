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
        
        Set<String> r = new HashSet();
        for(String s: arr){
            for(String ss: this){
                String combined = Utils.decombine(ss, s);
                if (combined!=null) r.add(combined);
            }
        }
        this.clear();
        this.addAll(r);
    }
    
}
