package sha256unroll;

/**
 *
 * @author Pelepeichenko A.V.
 */
public enum Val {

    NULL, ONE, ANY;

    public Val not() {
        switch (this) {
            case NULL:
                return ONE;
            case ONE:
                return NULL;
            case ANY:
                return ANY;
        }
        throw new RuntimeException("Val==null");
    }

    public Val compatible(Val other) {
        if (other == null) {
            throw new RuntimeException("Val==null");
        }

        if (this == other) {
            return this;
        }
        if (this == ANY && other != ANY) {
            return other;
        }
        if (other == ANY && this != ANY){
            return this;
        }
        
        return null;
    }

    @Override
    public String toString() {
        switch (this) {
            case NULL:
                return "0";
            case ONE:
                return "1";
            case ANY:
                return "*";
        }
        return "u";
    }
}
