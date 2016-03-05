package front_end.ui.base;

import java.io.PrintStream;

/**
 * Created by maianhvu on 5/3/16.
 */
public abstract class UserInterface<T> {

    private static final PrintStream STREAM_OUTPUT_DEFAULT = System.out;
    private final T data_;
    private PrintStream outputStream_;

    public UserInterface(T data) {
        this.outputStream_ = STREAM_OUTPUT_DEFAULT;
        this.data_ = data;
    }

    public T getData() {
        return this.data_;
    }

    public abstract void render();

    public void setOutputStream(PrintStream outputStream) {
        this.outputStream_ = outputStream;
    }

    protected void display(String format, Object... arguments) {
        this.outputStream_.printf(format, arguments);
    }

    protected void displayLine(String format, Object... arguments) {
        this.display(format + "\n", arguments);
    }
}
