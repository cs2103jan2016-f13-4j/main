package entity.command;

/**
 * Created by maianhvu on 6/3/16.
 */
public class Instruction {

    /**
     * Types
     */
    public enum Type {
        ADD(false),
        EDIT(true),
        DISPLAY(true),
        DELETE(true),
        EXIT(false),
        UNRECOGNISED(false);

        final boolean isUniversallyQuantifiable;
        Type(boolean uniQuantifiable) {
            isUniversallyQuantifiable = uniQuantifiable;
        }

    }

    private Type type_;
    private boolean hasUniversalQuantifier_;
    private Integer indexQuantifier_;

    /**
     * Constructs an instruction and classify it to have
     * a universal quantifier.
     *
     * @param type
     */
    public Instruction(Type type) {
        assert(type.isUniversallyQuantifiable);
        this.type_ = type;
        this.hasUniversalQuantifier_ = true;
    }

    /**
     * Constructs an instruction and points it to the
     * visual index.
     *
     * @param type
     * @param indexQuantifier
     */
    public Instruction(Type type, int indexQuantifier) {
        this.type_ = type;
        this.hasUniversalQuantifier_ = false;
    }

    public boolean hasUniversalQuantifier() {
        return this.hasUniversalQuantifier_;
    }

    public Type getType() {
        return this.type_;
    }

    public Integer getIndexQuantifier() {
        return this.indexQuantifier_;
    }
}
