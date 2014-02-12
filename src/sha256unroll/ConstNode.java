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
    byte needVal;
    
    ConstNode(byte v){
        super('c', null);
        needVal = v;
    }


    @Override
    public Collection<byte[]> probeVal(byte v) {
        return v==needVal ? varManager.getInitial() : Collections.EMPTY_LIST;
    }
    
    @Override
    boolean isConst(){return true;}
    
    
    @Override
    byte calc(){
        //System.out.println("calc in node "+num);
        /*
        try{
            throw new Exception("asd");
        }catch(Exception e){
            String v = "Stack trace size="+e.getStackTrace().length;
            System.out.println(v);
        }*/
        return needVal;
    }

    
    
}

