package sha256unroll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static sha256unroll.Utils.*;

/**
 *  У ноды паренты есть полюбому
 * 
 * @author chabapok
 */
public class Node {
    
    String name;
    
    Node a;
    Node b;
    
    /**
     * 
     * Символьное обозначение операции, которую представляет эта нода.
     * 
     * +*^c
     * 
     */
    char operation;
    
    // or and xor
    public Node(char op, Node p1, Node p2){
        a=p1;
        b=p2;
        operation = op;
    }
    
    //not
    public Node(char op, Node p1){
        a=p1;
        operation = op;
        assert(op=='!');
    }
    
       
    public Collection<String> probeVal(char v){
        if (v=='*') throw new RuntimeException("Зачем тогда звать? неважно же! "+name);
        Collection<String> c;
        switch(operation){
            case '!': c = not(v); break;
            case '+': c = or(v); break;
            case '*': c = and(v); break;
            case '^': c = xor(v); break;
            default: throw new RuntimeException("Wrong operation "+operation);
        }
        
        //System.out.printf("%s (%s->%s): %s\n", name, v, operation,c);
        return c;
    }

    private Collection<String> not(char v) {
        Collection<String> fromUp = a.probeVal( Utils.not(v) );
        return fromUp;
    }
    
    private Collection<String> or(char v) {
        if (v=='0'){
            Collection<String> aArr = a.probeVal('0');
            Collection<String> bArr = b.probeVal('0');
            
            Collection<String> result = combineNotConflicted(aArr, bArr);
            return result;
            
        }else{
            Collection<String> aArr = a.probeVal('1');
            Collection<String> bArr = b.probeVal('1');
            
            Collection<String> result = removeDupes(aArr, bArr);
            return result;
        }
    }

    private Collection<String> and(char v) {
        if (v=='1'){
            Collection<String> aArr = a.probeVal('1');
            Collection<String> bArr = b.probeVal('1');
            
            Collection<String> result = combineNotConflicted(aArr, bArr);
            return result;
        }else{
            Collection<String> aArr = a.probeVal('0');
            Collection<String> bArr = b.probeVal('0');
            
            Collection<String> result = removeDupes(aArr, bArr);
            return result;
        }
    }

    private Collection<String> xor(char v) {
        if (v=='0'){
            Collection<String> aArr = a.probeVal('0');
            Collection<String> bArr = b.probeVal('0');
            
            Collection<String> result0 = combineNotConflicted(aArr, bArr);

            
            aArr = a.probeVal('1');
            bArr = b.probeVal('1');
            
            Collection<String> result1 = combineNotConflicted(aArr, bArr);

            Collection<String> result = removeDupes(result0, result1);
            return result;
            
        }else{
            Collection<String> aArr = a.probeVal('0');
            Collection<String> bArr = b.probeVal('1');
            
            Collection<String> result0 = combineNotConflicted(aArr, bArr);

            aArr = a.probeVal('1');
            bArr = b.probeVal('0');
            
            Collection<String> result1 = combineNotConflicted(aArr, bArr);

            Collection<String> result = removeDupes(result0, result1);
            return result;
        }        
    }
    
    
    
    
    
}
