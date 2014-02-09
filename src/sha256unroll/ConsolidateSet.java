package sha256unroll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author chabapok
 */
public class ConsolidateSet extends ArrayList<String> {

    ConsolidateSet(){
        super();
    }
    

    ConsolidateSet(int capacity){
        super(capacity);
    }

    
    void consolidate(String... args){
        ArrayList<String> al = new ArrayList(Arrays.asList(args));
        consolidate(al);
    }
    
    void consolidate(String v) {
        ArrayList al = new ArrayList(1);
        al.add(v);
        consolidate(al);
    }

    void consolidate(Collection<String> arr) {
        if (isEmpty() && !arr.isEmpty()) {
            for (String s : arr) {
                add(s);
                arr.remove(s);
                break;
            }
        }

        ArrayList<String> arrToAdd = new ArrayList(arr.size() * 2);
        arrToAdd.addAll(arr);

        metka:
        for (int j = 0; j < arrToAdd.size(); j++) {
            String addV = arrToAdd.get(j);

            for (int i = 0; i < size(); i++) {
                String presentV = get(i);
                String sf = calcSetOf(presentV, addV);
                if (sf != null) {
                    remove(i);
                    arrToAdd.add(sf);
                    continue metka;
                }
            }
            add(addV);
        }
    }

    /**
     * Обрабатывает 2 ситуации:
     *
     * 1. Если одно значение является надмножеством другого, то оно возвращается
     * (значит его надо оставить, а другое ненужно) например: *1*1 01*1
     *
     * в этом примере второе ненужно, т.к. попадает в множетсво, которое
     * описывается первым
     *
     *
     * 2. Если два значения разные, но их можно описать одним множеством,
     * например: *1*1 *0*1 можно заменить оба на: ***1
     *
     *
     *
     * Если нет, то null.
     *
     * @param a
     * @param b
     * @return
     */
    private String calcSetOf(String aStr, String bStr) {
        if (aStr.length() != bStr.length()) {
            throw new RuntimeException("length not same a=" + aStr + " b=" + bStr);
        }
        
        boolean aSetofB = true;
        boolean bSetofA = true;
       
        int maskReplacePos = -1;
        boolean aMod=true;
        
        for (int i = 0; i < aStr.length(); i++) {
            char a = aStr.charAt(i);
            char b = bStr.charAt(i);

            //проверка, что а - все еще надмножество (и значит б-подмножесто)
            if ( !mayConverted(a, b) ){
                bSetofA = false;
            }

            if ( !mayConverted(b, a)){
                aSetofB = false;
            }
            
            if ( maskReplacePos != -2 ){
                if (a!=b) {
                    if (aMod){
                        aMod = false;
                        maskReplacePos=i;
                    }else{
                        maskReplacePos=-2;
                    }
                }
            }
                        
        }
        
        if (maskReplacePos>=0){
            char[] chars = aStr.toCharArray();
            chars[maskReplacePos] = '*';
            return String.valueOf(chars);
        }
        if (aSetofB) return bStr;
        if (bSetofA) return aStr;
        return null;
    }

    //"a" может превращаться в "b"
    private boolean mayConverted(char a, char b) {
        return a==b || (a=='*');
    }

}
