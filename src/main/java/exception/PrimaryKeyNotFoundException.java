package exception;

/**
 * Created by maianhvu on 20/03/2016.
 */
public class PrimaryKeyNotFoundException extends Exception {
    /**
     * Properties
     */
    private final int _primaryKey;
    private final String STRING_MESSAGE_ERROR = "Primary key not found: ";

    /**
     * Constructs a new PrimaryKeyNotFoundException with the provided integer
     * primary key
     * @param pKey an integer key
     */
    public PrimaryKeyNotFoundException(int pKey) {
        this._primaryKey = pKey;
    }

    @Override public String getMessage() {
        return this.STRING_MESSAGE_ERROR + this._primaryKey;
    }

}
