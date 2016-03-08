package exception.back_end;

/**
 * 
 * @author Huiyie
 *
 */

public class PrimaryKeyNotFoundException extends Exception {
    
    private int primaryKey_;
    private final String MESSAGE = "Primary key not found: ";

    public PrimaryKeyNotFoundException(int pKey) {
        this.primaryKey_ = pKey;
    }
    
    @Override
    public String getMessage() {
        return this.MESSAGE + this.primaryKey_;
    }
    
}
