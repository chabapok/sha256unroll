package sha256unroll;

import java.util.Map;


public class EndNode extends Node{
    Val v = Val.ANY;
    
    
    public EndNode(String name){
        super(null, null);
        this.name = name;    
    }
    
    
    
    @Override
    void unroll(Val v){
        TreeBranchManager.atEnter(this);
        HypotezManager.addDeduction(name, v);
        
        System.out.println(" "+name+" node path: "+TreeBranchManager.current() );
        TreeBranchManager.atExit();
        
        
    }

    
    @Override
    void unroll(Hypotez h) {
        if (h.values.length!=1) throw new RuntimeException("h.values.length!=1");
        unroll(h.values[0]);
    }


    
    @Override
    Val calc(Map<String, Val> vv){
        Val r=vv.get(name);
        if (r==null){
            System.out.println("WARN: can't find "+name+" in value map");
        }
        return r;
    }

    
}
