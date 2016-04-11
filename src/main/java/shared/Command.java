package shared;

import java.security.InvalidParameterException;
import java.util.LinkedHashMap;
import java.util.List;

import exception.ExceptionHandler;

/**
 * @@author Mai Anh Vu
 */
public class Command {

    /**
     * Types
     */
    public enum Instruction {
        ADD, DISPLAY, MARK, EDIT, SEARCH, UNDO, REDO, DELETE, TUTORIAL, EXIT, UNRECOGNISED, INVALID;
    }

    public enum ParamType {
        STRING(String.class),
        DATE(CustomTime.class),
        PRIORITY(Task.Priority.class),
        BOOLEAN(Boolean.class),
        INTEGER(Integer.class),
        LIST(List.class);

        final Class<?> typeClass;

        ParamType(Class<?> tClass) {
            typeClass = tClass;
        }
    }

    public enum ParamName {
        TASK_NAME(ParamType.STRING),
        TASK_START(ParamType.DATE),
        TASK_END(ParamType.DATE),
        PRIORITY_VALUE(ParamType.PRIORITY),

        SEARCH_QUERY(ParamType.STRING),

        TASK_INDEX(ParamType.INTEGER),
        TASK_INDEX_RANGES(ParamType.LIST),
        TASK_UNIVERSALLY_QUANTIFIED(ParamType.BOOLEAN),

        TASK_DURATION(ParamType.INTEGER);

        public final ParamType type;

        ParamName(ParamType t) {
            type = t;
        }
    }

    /**
     * Properties
     */
    private Instruction _instruction;
    private LinkedHashMap<ParamName, Object> _parameters;

    /**
     * Constructs a command with the given instruction, index OR quantifier.
     * 
     * @param instruction
     */
    public Command(Instruction instruction) {
        this._instruction = instruction;
        this._parameters = new LinkedHashMap<>();
    }

    public Instruction getInstruction() {
        return this._instruction;
    }

    public void setParameter(ParamName name, Object value) throws InvalidParameterException {
        if (!name.type.typeClass.isAssignableFrom(value.getClass())) {
            throw new InvalidParameterException(
                    String.format("A value of type %s cannot be assigned to parameter %s (%s)",
                            value.getClass().getCanonicalName(), name.name(),
                            name.type.typeClass.getCanonicalName()));
        }
        this._parameters.put(name, value);
    }

    @SuppressWarnings("unchecked") public <T> T getParameter(ParamName name) {
        try {
            return (T) this._parameters.get(name);
        } catch (ClassCastException e) {
            ExceptionHandler.handle(e);
            assert false; // We don't want this to happen
            return null;
        }
    }

    public boolean hasInstruction(Instruction instruction) {
        return this._instruction == instruction;
    }

    public boolean hasParameter(ParamName name) {
        return this._parameters.containsKey(name);
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._instruction);
        this._parameters.entrySet().forEach(entry -> {
            sb.append(" ").append(entry.getKey())
                    .append("=").append(entry.getValue());
        });
        return sb.toString();
    }

    public void removeParameter(ParamName taskName) {
        if (this._parameters.containsKey(taskName)) {
            this._parameters.remove(taskName);
        }
    }

    public int getParametersCount() {
        return this._parameters.keySet().size();
    }

    public static Command initialCommand() {
        Command command = new Command(Instruction.DISPLAY);
        command.setParameter(ParamName.TASK_UNIVERSALLY_QUANTIFIED, true);
        return command;
    }

    public boolean hasTrueValue(ParamName name) {
        try {
            return this.hasParameter(name) && (boolean) this.getParameter(name);
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Special types of commands
     */
    public static Command invalidCommand(String message) {
        Command invalidCommand =  new Command(Instruction.INVALID);
        invalidCommand._invalidationMessage = message;
        return invalidCommand;
    }
    public static Command unrecognisedCommand() {
        return new Command(Instruction.UNRECOGNISED);
    }

    /**
     * Invalid commands handling
     */
    private String _invalidationMessage;
    public void setAsInvalid(String message) {
        this._instruction = Instruction.INVALID;
        this._invalidationMessage = message;
    }

    public String getInvalidationMessage() {
        assert this._instruction == Instruction.INVALID && this._invalidationMessage != null;
        return this._invalidationMessage;
    }
}
