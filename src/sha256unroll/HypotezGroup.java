package sha256unroll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Pelepeichenko A.V.
 */
public class HypotezGroup {
    
    HypotezGroup prev;
    
    ArrayList<Hypotez> group = new ArrayList();

    Hypotez register(Hypotez h) {
        for(int i=0; i<group.size();i++){
            Hypotez other = group.get(i);
            if (equal(h, other)) return other;
        }
        group.add(h);
        return h;
    }

    private boolean equal(Hypotez h, Hypotez other) {
        
        if (h.values.length == other.values.length){
            for(int i=0; i<h.values.length; i++){
                if (h.values[i] != other.values[i]) return false;
            }
            return true;
        }else
            return false;
    }
    
    
    public void findHypotezes( Map<String, Val> composite){
        for(Hypotez h: group){
            Map<String, Val> r = (h.deduction==null) ? composite : contradict(composite, h);
            if (r==null) continue;
            if (prev==null){
                //System.out.println("FINDED! "+r);
                ResolveManager.addResolve(r);
            }else{
                prev.findHypotezes(r);
            }
        }
    }

    private Map<String, Val> contradict(Map<String, Val> composite, Hypotez h) {
        Map<String, Val> result = new TreeMap();
        for(Map.Entry<String, Val> entry: h.deduction.entrySet()){
            Val compVal = composite.get( entry.getKey() );
            Val v = entry.getValue();
            
            if (compVal==null){
                result.put(entry.getKey(), v);
                continue;
            }
            if ( (compVal==Val.NULL && v==Val.ONE) || (compVal==Val.ONE && v==Val.NULL) ){
                return null;
            }
            
            if (compVal==Val.ONE || compVal==Val.NULL){
                result.put(entry.getKey(), compVal);
            }else{
                result.put(entry.getKey(), v);
            }
        }
        for(Map.Entry<String, Val> entry: composite.entrySet()){
            if (result.get(entry.getKey())==null){
                result.put(entry.getKey(), entry.getValue() );
            }
        }
        return result;
    }

    void unregister(Hypotez h) {
        for(int i=0; i<group.size();i++){
            Hypotez other = group.get(i);
            if (equal(h, other)) {
                group.remove(i);
                return;
            }
        }
    }
    
    
}
