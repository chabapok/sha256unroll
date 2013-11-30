package sha256unroll;

import java.util.Map;


public class EndNode extends Node{
    Val v = Val.ANY;
    
    String name;
    
    public EndNode(String name){
        super(null, null);
        this.name = name;
        
    }
    
    
    
    @Override
    void unroll(Val v){
        HypotezManager.addDeduction(name, v);
    }

    
    @Override
    void unroll(Hypotez h) {
        if (h.values.length!=1) throw new RuntimeException("h.values.length!=1");
        unroll(h.values[0]);
    }


    
    Val calc(Map<String, Val> v){
        return v.get(name);
    }

    
}
