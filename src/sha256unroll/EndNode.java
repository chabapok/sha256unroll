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
        
        //System.out.println(" "+name+" node path: ");
        
    }

    
    @Override
    void unroll(Hypotez h) {
        if (h.values.length!=1) throw new RuntimeException("h.values.length!=1");
        unroll(h.values[0]);
    }


    
    @Override
    Val calc(Map<String, Val> v){
        Val r=v.get(name);
        if (r==null){
            System.out.println("WARN: can't find "+name+" in value map");
        }
        return r;
    }

    
}
