package sha256unroll;

import java.util.Arrays;

/**
 *
 * @author chabapok
 */
public class VariableManager {

    private char[] initialConditions;
    
    VariableManager(int varCount){
        initialConditions = new char[varCount];
        Arrays.fill(initialConditions, '*');
    }
    
    
    char[] getConditionsForVar(int varnum, char value){
        char[] cond = Arrays.copyOf(initialConditions, initialConditions.length);
        if(Utils.mayConverted(initialConditions[varIndex(varnum)], value)){
            cond[varIndex(varnum)] = value;
            return cond;
        }else{
            return null;
        }
    }
    
    
    EndNode node(int index){
        EndNode n = new EndNode(index);
        n.varManager = this;
        return n;
    }

    void init(int varnum, char v) {
        initialConditions[varIndex(varnum)] = v;
    }

    
    private int varIndex(int varNum){
        return initialConditions.length-1-varNum;
    }

    @Override
    public String toString() {
        return String.valueOf(initialConditions);
    }
    
    
    
}
