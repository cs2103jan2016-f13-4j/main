package free_objects;

/**
 * Created by maianhvu on 5/3/16.
 */
public class Command {

    public enum Type {
        ADD,
        EDIT,
        DISPLAY_ALL,
        DISPLAY_ONE,
        DELETE,
        EXIT,
        UNRECOGNISED
    }

    private final Type type_;
    private final Object[] parameters_;

    public Command(Type type, Object[] parameters) {
        this.type_ = type;
        this.parameters_ = parameters;
    }

    public static Command getInitialCommand() {
        return new Command(Type.DISPLAY_ALL, null);
    }

    public Type getType() {
        return this.type_;
    }

    public Object[] getParameters() {
        return this.parameters_;
    }
}
