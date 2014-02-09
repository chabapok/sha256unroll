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
    
    char get(int varnum) {
        return initialConditions[varIndex(varnum)];
    }

    
    EndNode node(int index){
        EndNode n = new EndNode(index);
        n.varManager = this;
        return n;
    }

    
    EndNode node(int index, String name){
        EndNode n = new EndNode(index);
        n.varManager = this;
        n.name = name;
        return n;
    }

    
    void init(int varnum, char v) {
        initialConditions[varIndex(varnum)] = v;
    }

    void init(String vars){
        initialConditions = vars.toCharArray();
    }
    
    void reset(){
        Arrays.fill(initialConditions, '*');
    }
    
    private int varIndex(int varNum){
        return initialConditions.length-1-varNum;
    }

    @Override
    public String toString() {
        return String.valueOf(initialConditions);
    }

    
    
    
}
