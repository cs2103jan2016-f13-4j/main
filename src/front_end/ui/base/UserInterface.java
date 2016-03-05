package front_end.ui.base;

import java.io.PrintStream;

/**
 * Created by maianhvu on 5/3/16.
 */
public abstract class UserInterface<T> {

    private static final PrintStream STREAM_OUTPUT_DEFAULT = System.out;

    private final PrintStream outputStream_;
    private final T data_;

    public UserInterface(PrintStream outputStream, T data) {
        this.outputStream_ = outputStream;
        this.data_ = data;
    }

    public UserInterface(T data) {
        this(STREAM_OUTPUT_DEFAULT, data);
    }

    public T getData() {
        return this.data_;
    }

    public abstract void render();

    protected void display(String format, Object... arguments) {
        this.outputStream_.printf(format, arguments);
    }

    protected void displayLine(String format, Object... arguments) {
        this.display(format + "\n", arguments);
    }
}
