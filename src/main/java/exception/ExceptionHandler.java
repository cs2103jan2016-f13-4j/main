package exception;

/**
 * @@author Mai Anh Vu
 */
public class ExceptionHandler {
    private final static ExceptionHandler instance = new ExceptionHandler();

    public static ExceptionHandler getInstance() {
        return instance;
    }

    private ExceptionHandler() {
    }

    public static void handle(Exception e) {
        // TODO: stub
    }
}
