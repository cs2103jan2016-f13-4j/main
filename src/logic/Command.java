package logic;

/**
 * Created by Huiyie on 24/2/16.
 */

public class Command {

    /**
     * Constants
     */
    private static final String[] STRINGS_COMMAND_ADD = new String[]{
            ":add", ":a"
    };
    private static final String[] STRINGS_COMMAND_SHOW = new String[]{
            ":show", ":s"
    };
    private static final String[] STRINGS_COMMAND_EDIT = new String[]{
            ":edit", ":e"
    };
    private static final String[] STRINGS_COMMAND_DELETE = new String[]{
            ":delete", ":x"
    };
    /**
     * Properties
     */
    private final Type commandType_;
    private final String parameter_;
    /**
     * Constructor
     *
     * @param commandType The type of the command
     * @param parameter   Associated data
     */
    public Command(Type commandType, String parameter) {
        this.commandType_ = commandType;
        this.parameter_ = parameter;
    }

    public static Type inferCommandTypeFromInstruction(String instruction) {
        // Case insensitive matching
        instruction = instruction.trim().toLowerCase();

        // Iterate through all command types and see if the instruction matches any of
        // the pre-registered types
        for (Type type : Type.values()) {
            if (type.instructions == null) {
                continue;
            }

            for (String inst : type.instructions) {
                if (instruction.equals(inst)) {
                    return type;
                }
            }
        }

        // Cannot find, return unrecognised
        return Type.UNRECOGNISED;
    }

    /**
     * Returns the instruction type of the command.
     *
     * @return instruction type
     */
    public Type getType() {
        return this.commandType_;
    }

    /**
     * Returns the parameter associated with the command.
     *
     * @return the parameter of the command
     */
    public String getParameter() {
        return this.parameter_;
    }

    /**
     * Returns whether the command instruction is unrecognised.
     *
     * @return if instruction is unrecognised
     */
    public boolean isUnrecognised() {
        return this.commandType_ == Type.UNRECOGNISED;
    }

    public enum Type {
        ADD(STRINGS_COMMAND_ADD),
        SHOW(STRINGS_COMMAND_SHOW),
        EDIT(STRINGS_COMMAND_EDIT),
        DELETE(STRINGS_COMMAND_DELETE),
        UNRECOGNISED(null);

        final String[] instructions;

        Type(String[] inst) {
            instructions = inst;
        }
    }
}
