package exception;

/**
 * @@author A0127046L
 */
public class PrimaryKeyNotFoundException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -8577319752977443484L;
    /**
     * Properties
     */
    private final int _primaryKey;
    private final String STRING_MESSAGE_ERROR = "Primary key not found: ";

    /**
     * Constructs a new PrimaryKeyNotFoundException with the provided integer
     * primary key
     *
     * @param pKey
     *            an integer key
     */
    public PrimaryKeyNotFoundException(int pKey) {
        this._primaryKey = pKey;
    }

    @Override public String getMessage() {
        return this.STRING_MESSAGE_ERROR + this._primaryKey;
    }

}
