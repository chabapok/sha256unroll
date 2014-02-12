package sha256unroll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static sha256unroll.Utils.mayConverted;

/**
 *
 * @author chabapok
 */
public class ConsolidateSet extends ArrayList<String> {

    ConsolidateSet() {
        super();
    }

    ConsolidateSet(int capacity) {
        super(capacity);
    }

    void consolidate(String... args) {
        ArrayList<String> al = new ArrayList(Arrays.asList(args));
        consolidate(al);
    }

    void consolidate(String v) {
        ArrayList al = new ArrayList(1);
        al.add(v);
        consolidate(al);
    }

    void consolidate(Collection<String> arr) {
        // System.out.println("Consolidate size="+arr.size()+":"+this.size() );
        ArrayList<String> arrToAdd = new ArrayList(arr.size() + size());
        arrToAdd.addAll(arr);

        if (isEmpty() && !arrToAdd.isEmpty()) {
            String s = arrToAdd.remove(arrToAdd.size() - 1);
            add(s);
        }

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

    static int counter = 0;

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
        if (aStr.length() == bStr.length()) {

            //counter++;
            //if ((counter%100000)==0)
            //   System.out.println(aStr);
            boolean aSetofB = true;
            boolean bSetofA = true;

            for (int i = 0; i < aStr.length(); i++) {
                char a = aStr.charAt(i);
                char b = bStr.charAt(i);
                //проверка, что а - все еще надмножество (и значит б-подмножесто)
                if (a != b) {
                    if (a != '*') {
                        bSetofA = false;
                    }

                    if (b != '*') {
                        aSetofB = false;
                    }

                    if (!(bSetofA || aSetofB)) {
                        return null;
                    }
                }
            }

            return aSetofB ? bStr : aStr;

        } else {
            throw new RuntimeException("length not same a=" + aStr + " b=" + bStr);
        }
    }

}
