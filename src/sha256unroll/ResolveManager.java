package sha256unroll;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ResolveManager {

    static ArrayList< Map<String, Val> > resolves = new ArrayList();
    
    public static void addResolve(Map<String, Val> resolve){
        for(Map<String, Val> m: resolves){
            if (eq(m, resolve)) return;
        }
        resolves.add(resolve);
    }

    private static boolean eq(Map<String, Val> m1, Map<String, Val> m2) {
        if (m1.size()!=m2.size()) return false;
        Iterator< Entry<String, Val> > i1 = m1.entrySet().iterator();
        Iterator< Entry<String, Val> > i2 = m2.entrySet().iterator();
        
        while(i1.hasNext()){
            Entry<String, Val> e1 = i1.next();
            Entry<String, Val> e2 = i2.next();
            if ( !e1.getKey().equals(e2.getKey()) || e1.getValue()!=e2.getValue()) return false;
        }
        return true;
    }
    
    
    static void show(){
        for(Map<String, Val> m: resolves){
            System.out.println("FINDED: "+m); 
        }
    }
    
}
