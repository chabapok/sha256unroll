package sha256unroll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author chabapok
 */
public class VariableManager {
    static VariableManager lastInstance;

    static VariableManager create(int varCount){
        VariableManager vm = new VariableManager(varCount);
        lastInstance = vm;
        return vm;
    }
    
    
    private char[] initialConditions;
    
    private VariableManager(int varCount){
        initialConditions = new char[varCount];
        Arrays.fill(initialConditions, '*');
    }
    
    
    
    Collection<String> getInitial(){
        String v=String.valueOf(initialConditions);
        ArrayList<String> initialCond = new ArrayList();
        initialCond.add(v);
        return initialCond;
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

    
    int cn =0;
    EndNode createNext(){
        EndNode en = node(cn++);
        return en;
    }
    
    EndNode createNext(String name){
        EndNode en = node(cn++, name);
        return en;
    }
    
    
    EndNode node(int index){
        return node(index, "x");
    }

    
    EndNode node(int index, String name){
        EndNode n = new EndNode(index);
        n.varManager = this;
        n.name = name+index;
        return n;
    }

    
    ConstNode constNode(char v){
        ConstNode n = new ConstNode(v);
        n.varManager = this;
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
