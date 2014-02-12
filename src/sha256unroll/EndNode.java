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
    public Collection<byte[]> probeVal(byte v) {
        byte[] varStarts = varManager.getConditionsForVar(index, v);
        
        if (varStarts!=null){
            ArrayList<byte[]> result = new ArrayList(1);
            result.add(varStarts);
            return result;
        }else{
            return new ArrayList(0);
        }
        
    }
    
    public void init(byte v){
        varManager.init(index, v);
    }
    
    public void init(char v){
        varManager.init(index, (byte)(v&0xff));
    }
    
    
    @Override
    byte calc(){
        return varManager.get(index);
    }
    
    @Override
    boolean isConst(){
        byte c = varManager.get(index);
        return c!= '*';
    }
    
}
