/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sha256unroll;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chabapok
 */
public class ConsolidateSetTest {
    
    public ConsolidateSetTest() {
    }

    @Test
    public void testConsolidate() {
        ConsolidateSet set = new ConsolidateSet();
        
        set.consolidate("1*");
        set.consolidate("*1");
        System.out.println(set);
        
        set.consolidate("*1");
        System.out.println(set);
        
        set.clear();
        set.consolidate("*0");
        set.consolidate("00");
        System.out.println(set);
        
        set.clear();
        set.consolidate(
                "1*1*",
                "1**1");
        System.out.println(set);
        set.consolidate("1*00");
        System.out.println(set);
        
        set.clear();
        
        set.consolidate("0110", "0100");
        System.out.println(set);
    }
    
}
