package sha256unroll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author chabapok
 */
public class ConstNode extends Node{  
    
    VariableManager varManager;
    char needVal;
    
    ConstNode(char v){
        super('c', null);
        needVal = v;
    }


    @Override
    public Collection<String> probeVal(char v) {
        return v==needVal ? varManager.getInitial() : Collections.EMPTY_LIST;
    }
    
    @Override
    boolean isConst(){return true;}
    
    
    @Override
    char calc(){
        //System.out.println("calc in node "+num);
        return needVal;
    }

    
    
}

