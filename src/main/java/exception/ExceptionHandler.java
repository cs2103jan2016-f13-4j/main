package exception;

/**
 * Created by maianhvu on 20/03/2016.
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
