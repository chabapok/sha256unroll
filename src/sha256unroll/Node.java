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
    static int allCount = 0;
    static int andCount = 0;
    static int orCount = 0;
    static int xorCount = 0;
    static int notCount =0;
    static int eCount=0;
    static int cCount=0;
    static int sumCount=0;
    static int carryCount=0;
    
    static String currLevel;
    
    String name;
    
    private Node a;
    private Node b;
    private Node c;
    
    /**
     * 
     * Символьное обозначение операции, которую представляет эта нода.
     * 
     * +*^c
     * 
     */
    private char operation;
    
    int nodeNum = allCount;
    String level = currLevel;
    
    {
        allCount++;
    }
    
    
    //sum or carry
    public Node(char op, Node aa, Node bb, Node cc){
        if (op!='C' && op!='S') throw new RuntimeException("Wrong operation");
        a = aa;
        b = bb;
        c = cc;
        operation = op;
        switch(op){
            case 'S': sumCount++;break;
            case 'C': carryCount++;break;
        }
    }
    
    // or and xor
    public Node(char op, Node p1, Node p2){
        a=p1;
        b=p2;
        operation = op;
        switch(op){
            case '+': orCount++;break;
            case '*': andCount++; break;
            case '^': xorCount++;break;
            case 'e': eCount++;break;
        }
    }
    
    //not
    public Node(char op, Node p1){
        a=p1;
        operation = op;
        if (op=='!')
            notCount++;
        else{
            cCount++;
        }
    }
    
    Collection<byte[]> if0=null;
    Collection<byte[]> if1=null;
    
    static long lastTrace=0;
    
    public Collection<byte[]> probeVal(byte v){
        if (v=='*') throw new RuntimeException("Зачем тогда звать? неважно же! "+name);
        
        if (v=='0'){ 
            if (if0==null) {
                if0 = probeValImpl(v);
                lastTrace++;
                if (lastTrace%1000==0)
                    System.out.println("probe 0 for node "+nodeNum+"ok. Level="+level+" if0 size= "+if0.size());
            }
            return if0;
        }
        if (if1==null){
            if1 = probeValImpl(v);
            
            lastTrace++;
            if (lastTrace%1000==0)
                System.out.println("probe 1 for node "+nodeNum+"ok.  level="+level +" if1 size= "+if1.size());
        }
        return if1;
    }
    
    
    private Collection<byte[]> probeValImpl(byte v){
        
        
        Collection<byte[]> c;
        switch(operation){
            case '!': c = not(v); break;
            case '+': c = or(v); break;
            case '*': c = and(v); break;
            case '^': c = xor(v); break;
            case 'C': c = carry(v); break;
            case 'S': c = sum(v); break;    
            default: throw new RuntimeException("Wrong operation "+operation);
        }
        
        //System.out.printf("%s (%s->%s): %s\n", name, v, operation,c);
        return c;
    }

    private Collection<byte[]> not(byte v) {
        Collection<byte[]> fromUp = a.probeVal( Utils.not(v) );
        return fromUp;
    }
    
    private Collection<byte[]> or(byte v) {
        Collection<byte[]> aArr = a.probeVal(v);
        Collection<byte[]> bArr = b.probeVal(v);

        if (v=='0'){
            Collection<byte[]> result = combineNotConflicted(aArr, bArr);
            return result;
            
        }else{
            
            Collection<byte[]> result = removeDupes(aArr, bArr);
            return result;
        }
    }

    private Collection<byte[]> and(byte v) {
        Collection<byte[]> aArr = a.probeVal(v);
        Collection<byte[]> bArr = b.probeVal(v);

        if (v=='1'){
            Collection<byte[]> result = combineNotConflicted(aArr, bArr);
            return result;
        }else{
            Collection<byte[]> result = removeDupes(aArr, bArr);
            return result;
        }
    }

    private Collection<byte[]> xor(byte v) {
        
        Collection<byte[]> aArr0 = a.probeVal((byte)'0');
        Collection<byte[]> bArr0 = b.probeVal((byte)'0');
        Collection<byte[]> aArr1 = a.probeVal((byte)'1');
        Collection<byte[]> bArr1 = b.probeVal((byte)'1');
        
        if (v=='0'){            
            Collection<byte[]> result0 = combineNotConflicted(aArr0, bArr0);
            Collection<byte[]> result1 = combineNotConflicted(aArr1, bArr1);

            Collection<byte[]> result = removeDupes(result0, result1);
            return result;
            
        }else{
            
            Collection<byte[]> result0 = combineNotConflicted(aArr0, bArr1);
            Collection<byte[]> result1 = combineNotConflicted(aArr1, bArr0);

            Collection<byte[]> result = removeDupes(result0, result1);
            return result;
        }
    }
    
    
    boolean isConst(){return false;}
    
    
    byte v='x';
    byte calc(){
        if (v=='x') v = calcImpl();
        return v;
    }
    
    private byte calcImpl(){
        //System.out.println("calc in node "+num);
        byte aResult = a.calc();
        if (operation=='!') return Utils.not(aResult);
        byte bResult = b.calc();
        
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
                
            case 'C':
                byte cResult = c.calc();
                if (
                        (aResult=='1' && bResult=='1') ||
                        (cResult=='1' && bResult=='1') ||
                        (cResult=='1' && aResult=='1')
                        ) return '1';
                return '0';
                
            case 'S':
                cResult = c.calc();
                String s = ""+aResult+bResult+cResult;
                if ("001".equals(s) || "010".equals(s) || "100".equals(s) || "111".equals(s)) 
                    return '1';
                return '0';
        }
        throw new RuntimeException("Unknow operation "+operation);
    }
    
    
    
    
    private Collection<byte[]> carry(byte v) {
        /*
        Collection<byte[]> a0 = a.probeVal((byte)'0');
        Collection<byte[]> a1 = a.probeVal((byte)'1');
        
        Collection<byte[]> b0 = b.probeVal((byte)'0');
        Collection<byte[]> b1 = b.probeVal((byte)'1');

        Collection<byte[]> c0 = c.probeVal((byte)'0');
        Collection<byte[]> c1 = c.probeVal((byte)'1');
        
        if (v=='0'){
            Collection<byte[]> r0 = combineNotConflicted(a0, b0, c0);
            Collection<byte[]> r1 = combineNotConflicted(a0, b1, c0);
            Collection<byte[]> r2 = combineNotConflicted(a1, b0, c0);
            Collection<byte[]> r3 = combineNotConflicted(a0, b0, c1);
            
            Collection<byte[]> r4 = removeDupes(r0, r1);
            Collection<byte[]> r5 = removeDupes(r2, r3);
            Collection<byte[]> r6 = removeDupes(r4, r5);
            return r6;            
        }else{
            Collection<byte[]> r0 = combineNotConflicted(a1, b1, c0);
            Collection<byte[]> r1 = combineNotConflicted(a0, b1, c1);
            Collection<byte[]> r2 = combineNotConflicted(a1, b0, c1);
            Collection<byte[]> r3 = combineNotConflicted(a1, b1, c1);
            
            Collection<byte[]> r4 = removeDupes(r0, r1);
            Collection<byte[]> r5 = removeDupes(r2, r3);
            Collection<byte[]> r6 = removeDupes(r4, r5);
            return r6;            
        }*/
        return null;
    }
    
    
    private Collection<byte[]> sum(byte v) {
/*
        Collection<byte[]> a0 = a.probeVal('0');
        Collection<byte[]> a1 = a.probeVal('1');
        
        Collection<byte[]> b0 = b.probeVal('0');
        Collection<byte[]> b1 = b.probeVal('1');

        Collection<byte[]> c0 = c.probeVal('0');
        Collection<byte[]> c1 = c.probeVal('1');
        
        if (v=='0'){
            Collection<byte[]> r0 = combineNotConflicted(a0, b0, c0);
            Collection<byte[]> r1 = combineNotConflicted(a1, b1, c0);
            Collection<byte[]> r2 = combineNotConflicted(a0, b1, c1);
            Collection<byte[]> r3 = combineNotConflicted(a1, b0, c1);
            
            Collection<byte[]> r4 = removeDupes(r0, r1);
            Collection<byte[]> r5 = removeDupes(r2, r3);
            Collection<byte[]> r6 = removeDupes(r4, r5);
            return r6;
        }else{
            Collection<byte[]> r0 = combineNotConflicted(a0, b1, c0);
            Collection<byte[]> r1 = combineNotConflicted(a1, b0, c0);
            Collection<byte[]> r2 = combineNotConflicted(a0, b0, c1);
            Collection<byte[]> r3 = combineNotConflicted(a1, b1, c1);
            
            Collection<byte[]> r4 = removeDupes(r0, r1);
            Collection<byte[]> r5 = removeDupes(r2, r3);
            Collection<byte[]> r6 = removeDupes(r4, r5);
            return r6;
        }
        */
        return null;
    }
}
