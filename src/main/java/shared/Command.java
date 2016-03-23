package shared;

import exception.ExceptionHandler;

import java.util.LinkedHashMap;

public class Command {

    /**
     * Types
     */
    public enum Instruction {
        ADD, DISPLAY, MARK, EDIT, SEARCH, UNDO, DELETE, EXIT,
        UNRECOGNISED, INVALID;
    }

    public enum ParamType {
        STRING, INTEGER, DATE, DOUBLE
    }

    public enum ParamName {
        TASK_NAME(ParamType.STRING),
        TASK_DESCRIPTION(ParamType.STRING),
        TASK_START(ParamType.DATE),
        TASK_END(ParamType.DATE),
        SEARCH_QUERY(ParamType.STRING);

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
     * @param instruction
     * @param index
     * @param isUniversallyQuantified
     */
    public Command(Instruction instruction, Integer index, boolean isUniversallyQuantified) {
        assert index == null || !isUniversallyQuantified; // isUniversallyQuantified => (index == null)

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

    public <T> T getParameter(ParamName name) {
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
}
