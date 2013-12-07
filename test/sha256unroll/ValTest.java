/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sha256unroll;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pelepeichenko A.V.
 */
public class ValTest {
    
    public ValTest() {
    }
    
    @Test
    public void testValues() {
        Val v1[]={Val.NULL, Val.ONE, Val.ANY};
        
        for(Val x: v1){
            StringBuilder sb = new StringBuilder();
            for(Val y: v1){
                Val r = x.compatible(y);
                sb.append(String.valueOf(r)).append(" ");
            }
            System.out.println(sb.toString() );
        }
    }

}