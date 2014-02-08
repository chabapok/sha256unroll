package sha256unroll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


public class EndNode extends Node{
    
    final int varCount=4;
    
    int index;
    
    public EndNode(int index){
        super('e', null, null);
        this.index = index;
        this.name = "x"+index;
    }

    @Override
    public Collection<String> probeVal(char v) {
        StringBuilder sb = new StringBuilder();
        for(int i=varCount; i>0; i--){
            sb.append( (index==i)? v:'*' );
        }
        ArrayList<String> result = new ArrayList(1);
        result.add(sb.toString());
        return result;
    }
    
    
    
    
}
