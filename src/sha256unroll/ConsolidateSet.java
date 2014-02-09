package sha256unroll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author chabapok
 */
public class ConsolidateSet extends ArrayList<String> {

    
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

    //проверка - то что еcть покрывает то, что хотим добавить
    private boolean pokriv(String presentV, String addV) {

        for (int i = 0; i < presentV.length(); i++) {
            char p = presentV.charAt(i);
            char a = addV.charAt(i);
            if ((p == '*') || (p == a)) {
                continue;
            }
            return false;
        }
        return true;
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

        StringBuilder newSetBuilderA = new StringBuilder();
        StringBuilder newSetBuilderB = new StringBuilder();

        for (int i = 0; i < aStr.length(); i++) {
            char a = aStr.charAt(i);
            char b = bStr.charAt(i);

            //проверка, что а - все еще надмножество (и значит б-подмножесто)
            if (newSetBuilderA != null && b == '*' && a != '*') {
                newSetBuilderA = null;
            }

            //проверка, что b - все еще надмножество (и значит a-подмножество)
            if (newSetBuilderB != null && a == '*' && b != '*') {
                newSetBuilderB = null;
            }

            if (newSetBuilderA != null) {
                newSetBuilderA.append((a == b) ? a : '*');
            }

            if (newSetBuilderB != null) {
                newSetBuilderB.append((a == b) ? a : '*');
            }
        }

        if (newSetBuilderA != null) {
            return newSetBuilderA.toString();
        }
        if (newSetBuilderB != null) {
            return newSetBuilderB.toString();
        }
        return null;
    }

}
