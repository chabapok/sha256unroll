package sha256unroll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static sha256unroll.Utils.*;

/**
 *  У ноды паренты есть полюбому
 * 
 * @author chabapok
 */
public class Node {
    static AtomicInteger ai = new AtomicInteger();
    
    String name;
    
    private Node a;
    private Node b;
    
    /**
     * 
     * Символьное обозначение операции, которую представляет эта нода.
     * 
     * +*^c
     * 
     */
    private char operation;
    
    int num;
    
    // or and xor
    public Node(char op, Node p1, Node p2){
        a=p1;
        b=p2;
        operation = op;
        num = ai.incrementAndGet();
    }
    
    //not
    public Node(char op, Node p1){
        a=p1;
        operation = op;
        num = ai.incrementAndGet();
        assert(op=='!');
    }
    
    Collection<String> if0=null;
    Collection<String> if1=null;
    
    public Collection<String> probeVal(char v){
        if (v=='*') throw new RuntimeException("Зачем тогда звать? неважно же! "+name);
        
        if (v=='0'){ 
            if (if0==null) if0 = probeValImpl(v);
            return if0;
        }
        if (if1==null) if1 = probeValImpl(v);
        return if1;
    }
    
    
    private Collection<String> probeValImpl(char v){
        
        
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
        Collection<String> aArr = a.probeVal(v);
        Collection<String> bArr = b.probeVal(v);

        if (v=='0'){
            Collection<String> result = combineNotConflicted(aArr, bArr);
            return result;
            
        }else{
            
            Collection<String> result = removeDupes(aArr, bArr);
            return result;
        }
    }

    private Collection<String> and(char v) {
        Collection<String> aArr = a.probeVal(v);
        Collection<String> bArr = b.probeVal(v);

        if (v=='1'){
            Collection<String> result = combineNotConflicted(aArr, bArr);
            return result;
        }else{
            Collection<String> result = removeDupes(aArr, bArr);
            return result;
        }
    }

    private Collection<String> xor(char v) {
        
        Collection<String> aArr0 = a.probeVal('0');
        Collection<String> bArr0 = b.probeVal('0');
        Collection<String> aArr1 = a.probeVal('1');
        Collection<String> bArr1 = b.probeVal('1');
        
        if (v=='0'){            
            Collection<String> result0 = combineNotConflicted(aArr0, bArr0);
            Collection<String> result1 = combineNotConflicted(aArr1, bArr1);

            Collection<String> result = removeDupes(result0, result1);
            return result;
            
        }else{
            
            Collection<String> result0 = combineNotConflicted(aArr0, bArr1);
            Collection<String> result1 = combineNotConflicted(aArr1, bArr0);

            Collection<String> result = removeDupes(result0, result1);
            return result;
        }
    }
    
    
    boolean isConst(){return false;}
    
    
    char v='x';
    char calc(){
        if (v=='x') v = calcImpl();
        return v;
    }
    
    char calcImpl(){
        //System.out.println("calc in node "+num);
        char aResult = a.calc();
        if (operation=='!') return Utils.not(aResult);
        char bResult = b.calc();
        
        switch(operation){
            case '+': 
                if (aResult=='1' || bResult=='1') return '1';
                if (aResult=='0' && bResult=='0') return '0';
                return '?';
                
            case '*':
                if (aResult=='1' && bResult=='1') return '1';
                if (aResult=='0' && bResult=='1') return '0';
                if (aResult=='1' && bResult=='0') return '0';
                if (aResult=='0' && bResult=='0') return '0';
                return '?';
                
            case '^':
                if (aResult=='1' && bResult=='1') return '0';
                if (aResult=='0' && bResult=='0') return '0';
                if (aResult=='0' && bResult=='1') return '1';
                if (aResult=='1' && bResult=='0') return '1';
                return '?';
        }
        throw new RuntimeException("Unknow operation "+operation);
    }
    
}
