package sha256unroll;

import java.util.Map;

/**
 *
 * @author Pelepeichenko A.V.
 */
public interface IAct {
    String name();
    void unroll(Node node, Val v);
    
    Val calc(Node node, Map<String, Val> v);
}
