package sha256unroll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author chabapok
 */
public class Utils {
    
    static char not(char v){
        if (v=='*') return '*';
        if (v=='1') return '0';
        return '1';
    }
    
    
    static Collection<String> combineNotConflicted(Collection<String> aArr, Collection<String> bArr){
        HashSet<String> result = new HashSet();
        
        for(String aVariant:aArr ){
            for(String bVariant:bArr ){
                String combined = combine(aVariant, bVariant);
                if (combined!=null) result.add(combined);
            }    
        }
        
        return result;
    }

    /**
     * 
     * Уточнение.
     *  0*, *0, 00 -> 0
     *  1*, *1, 11 -> 1
     *  ** - >*
     * 01,10 - запрещено
     * 
     * @param aVariant
     * @param bVariant
     * @return 
     */
    static String combine(String aVariant, String bVariant) {
        if (aVariant.length()!=bVariant.length()) throw new RuntimeException("Length not same! "+aVariant+":"+bVariant);
        
        StringBuilder sb = new StringBuilder(aVariant.length());
        
        for(int i=0; i<aVariant.length(); i++){
            char a = aVariant.charAt(i);
            char b = bVariant.charAt(i);
            
            if (a=='*') {sb.append(b); continue;}
            if (b=='*') {sb.append(a); continue;}
            if (a==b) {sb.append(a); continue;}
            return null; //01 or 10
        }
        return sb.toString();
    }
    
    //тут неверно... надо как-то обьединять, даже с пустой...
    static Collection<String> removeDupes(Collection<String> aArr, Collection<String> bArr){
        ConsolidateSet result = new ConsolidateSet();
        result.consolidate(aArr);
        result.consolidate(bArr);        
        return result;
    }

    
    /**
     * Разуточнение.
     * 
     * *1, *0, **, 1*, 0* ->*
     * 11 ->1
     * 00 ->0
     * 
     * 01,10 ->запрещено
     * 
     */    
    static String decombine(String aVariant, String bVariant) {
        if (aVariant.length()!=bVariant.length()) throw new RuntimeException("Length not same! "+aVariant+":"+bVariant);
        
        StringBuilder sb = new StringBuilder(aVariant.length());
        
        for(int i=0; i<aVariant.length(); i++){
            char a = aVariant.charAt(i);
            char b = bVariant.charAt(i);
            
            if (a=='*' || b=='*') {sb.append('*'); continue;}
            if (a==b) {sb.append(a); continue;}
            return null; //01 or 10
        }
        return sb.toString();
        
    }
    
    
    
    
}
