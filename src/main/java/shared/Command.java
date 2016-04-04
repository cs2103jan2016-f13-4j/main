package shared;

import java.util.LinkedHashMap;

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
        STRING, DATE, PRIORITY
    }

    public enum ParamName {
        TASK_NAME(ParamType.STRING), TASK_DESCRIPTION(ParamType.STRING), TASK_START(ParamType.DATE), TASK_END(
                ParamType.DATE), SEARCH_QUERY(ParamType.STRING), PRIORITY_VALUE(ParamType.PRIORITY);

        public final ParamType type;

        ParamName(ParamType t) {
            type = t;
        }
    }

    /**
     * Properties
     */
    private Instruction _instruction;
    private Integer _index;
    private boolean _isUniversallyQuantified;
    private LinkedHashMap<ParamName, Object> _parameters;

    /**
     * Constructs a command with the given instruction, index OR quantifier.
     * 
     * @param instruction
     * @param index
     * @param isUniversallyQuantified
     */
    public Command(Instruction instruction, Integer index, boolean isUniversallyQuantified) {
        assert index == null || !isUniversallyQuantified; // isUniversallyQuantified
                                                          // => (index == null)

        this._instruction = instruction;
        this._index = index;
        this._isUniversallyQuantified = isUniversallyQuantified;
        this._parameters = new LinkedHashMap<>();
    }

    public void setIndex(int index) {
        this._index = index;
    }

    public Integer getIndex() {
        return this._index;
    }

    public boolean isUniversallyQuantified() {
        return this._isUniversallyQuantified;
    }

    public Instruction getInstruction() {
        return this._instruction;
    }

    public void setParameter(ParamName name, Object value) {
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
        if (this._index != null) {
            sb.append("[").append(this._index).append("]");
        } else if (this._isUniversallyQuantified) {
            sb.append("[all]");
        }
        this._parameters.entrySet().forEach(entry -> {
            sb.append(" ").append(entry.getKey()).append("=").append(entry.getValue());
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

    public void setUniversallyQuantified() {
        this._isUniversallyQuantified = true;
    }

    /**
     * Special types of commands
     */
    public static Command invalidCommand() {
        return new Command(Instruction.INVALID, null, false);
    }
    public static Command unrecognisedCommand() {
        return new Command(Instruction.UNRECOGNISED, null, false);
    }

    public static Command initialCommand() {
        return new Command(Instruction.DISPLAY, null, true);
    }
}
