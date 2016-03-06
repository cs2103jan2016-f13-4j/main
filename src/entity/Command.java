package entity;

/**
 * Created by maianhvu on 5/3/16.
 */
public class Command {

    /**
     * Constants
     */
    private static final Type TYPE_COMMAND_INITIAL = Type.DISPLAY_ALL;
    private static final Object[] PARAMETERS_COMMAND_INITIAL = null;

    /**
     * Types
     */
    public static enum Type {
        ADD,
        EDIT,
        DISPLAY_ALL,
        DISPLAY_ONE,
        DELETE,
        EXIT,
        UNRECOGNISED
    }

    /**
     * Properties
     */
    private final Type type_;
    private final Object[] parameters_;

    /**
     * Constructs a command based on the supplied type and parameters
     * @param type
     * @param parameters
     */
    public Command(Type type, Object[] parameters) {
        this.type_ = type;
        this.parameters_ = parameters;
    }

    /**
     * Constructs the first command to be executed by the application stack
     * @return the required initial command
     */
    public static Command getInitialCommand() {
        return new Command(TYPE_COMMAND_INITIAL, PARAMETERS_COMMAND_INITIAL);
    }

    public Type getType() {
        return this.type_;
    }

    public Object[] getParameters() {
        return this.parameters_;
    }
}
