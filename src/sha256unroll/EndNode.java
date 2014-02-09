package sha256unroll;

import java.util.ArrayList;
import java.util.Collection;


public class EndNode extends Node{
    
    VariableManager varManager;
    
    
    int index;
    
    public EndNode(int index){
        super('e', null, null);
        this.index = index;
        this.name = "x"+index;
    }

    @Override
    public Collection<String> probeVal(char v) {
        char[] varStarts = varManager.getConditionsForVar(index, v);
        
        if (varStarts!=null){
            ArrayList<String> result = new ArrayList(1);
            result.add(String.valueOf(varStarts) );
            return result;
        }else{
            return new ArrayList(0);
        }
        
    }
    
    
    @Override
    char calc(){
        return varManager.get(index);
    }
    
    @Override
    boolean isConst(){
        char c = varManager.get(index);
        return c!= '*';
    }
    
}
