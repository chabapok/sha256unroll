package sha256unroll;

import java.util.Arrays;

/**
 *
 * @author chabapok
 */
public class RefCountManager {

    
    private static int[] counts = new int[100];
    
    
    static void incRefcount(int nn){
        if (counts.length<nn+1){
            counts = Arrays.copyOf(counts, nn+1);
            
        }
        counts[nn]++;
    }
    
    
    static double calcAvg(){
        double x=0;
        for(int t:counts){x+=t;}
        return x/counts.length;
    }
    
    
    static int calcMax(){
        int m=0;
        for(int t:counts){
            if (m<t) m=t;
        }
        return m;
    }
    
    
    static int[] calcCountByCounts(){
        int [] cc = new int[calcMax()+1];
        for(int x:counts){
            cc[x]++;
        }
        return cc;
    }

    static void printCalcCountByCounts() {
        int[] v= calcCountByCounts();
        StringBuilder sb = new StringBuilder();
        for(int x:v){
            sb.append(String.valueOf(x)).append(", ");
        }
        System.out.println("["+sb.toString()+"]");
    }
    
    
    
}
