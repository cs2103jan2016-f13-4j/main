package entity;

/**
 * Created by maianhvu on 5/3/16.
 */
public class Message {

    public enum Type {
        ERROR,
        WARNING,
        INFO
    }

    private final Type type_;
    private final String message_;

    public Message(Type type, String message) {
        this.type_ = type;
        this.message_ = message;
    }

    public Type getType() {
        return this.type_;
    }

    public String getMessage() {
        return this.message_;
    }
}
