package sha256unroll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static sha256unroll.Utils.*;

public class Hypotez {
    Hypotez parent;
    ArrayList<Hypotez> next = new ArrayList();
    
    Node node;
    Val[] values;
    int num;
    
    Map<String, Val> deduction;
    
    boolean closed = false;
    boolean broken = false;
    
    
    public boolean addDeduction(String variable, Val value){
        if (value==Val.ANY){ return broken;}
        if (closed)
            println("Warn: add Deduction to closed hypotez");
        
        println("add to hypoteze "+num+": "+variable+"="+value);
        
        if (deduction==null)
            deduction = new TreeMap();
        Val old = deduction.put(variable, value);
        
        if ( (old==Val.NULL && value==Val.ONE) || (old==Val.ONE && value==Val.NULL) ){
            broken = true;
            node.unregisterGypotez(this);
        }
        return broken;
    }
    
    public void close(){
        HypotezManager.closeHypotez(this);
        closed = true;
        
        if (deduction==null){
            println("close hypotez " + num + " is null");
        }else{
        
            println("close hypotez " + num +" for hyp path "+ branchPath() );
            for(Map.Entry<String, Val> e: deduction.entrySet()){
                println("  "+e.getKey()+" = "+e.getValue() );
            }
        }
        
    }
    
    
    String branchPath(){
        StringBuilder hypotezBranchPath = new StringBuilder();
        Hypotez h = this;
        do{
            hypotezBranchPath.append(h.node.name).append("<-");
            h = h.parent;
        }while(h!=null);
        return hypotezBranchPath.toString();
    }
}
