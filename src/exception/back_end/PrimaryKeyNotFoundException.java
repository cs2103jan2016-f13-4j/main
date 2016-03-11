package exception.back_end;

/**
 * 
 * @author Huiyie
 *
 */

public class PrimaryKeyNotFoundException extends Exception {

    /**
     * Properties
     */
    private int primaryKey_;
    private final String STRING_MESSAGE_ERROR = "Primary key not found: ";

    /**
     * Constructs a new PrimaryKeyNotFoundException with the provided integer
     * primary key
     * @param pKey an integer key
     */
    public PrimaryKeyNotFoundException(int pKey) {
        this.primaryKey_ = pKey;
    }
    
    @Override public String getMessage() {
        return this.STRING_MESSAGE_ERROR + this.primaryKey_;
    }
    
}
