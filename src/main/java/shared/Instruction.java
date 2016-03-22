package shared;

public class Instruction {

    private static final String STRING_QUANTIFIER_ALL = "all";

    /**
     * Types
     */
    public enum Type {
        // TYPE_NAME ( keyword,   isAllQuantifiable, doesRequireQuantifier )
        ADD          ( "add",     false,             false),
        EDIT         ( "edit",    false,             true),
        DISPLAY      ( "display", true,              false),
        DELETE       ( "delete" , true,              true),
        SEARCH       ( "search",  false,             false),
        EXIT         ( "exit",    false,             false),

        // Special types
        INVALID     (null, false, false),
        UNRECOGNISED(null, false, false);

        // Type properties
        final boolean isAllQuantifiable;
        final boolean doesRequireQuantifier;
        public final String keyword;

        // Type constructor
        Type(String key, boolean uniQuantifiable, boolean requireQuantifier) {
            keyword = key;
            isAllQuantifiable = uniQuantifiable;
            doesRequireQuantifier = requireQuantifier;
        }

    }

    /**
     * Properties
     */
    private Type type_;
    private boolean hasAllQuantifier_;
    private Integer index_;

    /**
     * Constructs an instruction and classify it to have
     * a universal quantifier.
     *
     * @param type
     */
    public Instruction(Type type) {
        this.type_ = type;

        this.inferQuantifierFromString(null);
    }

    /**
     * Constructs an instruction and points it to the
     * visual index.
     *
     * @param type
     * @param index
     */
    public Instruction(Type type, int index) {
        this.type_ = type;
        this.hasAllQuantifier_ = false;
        this.index_ = index;
    }

    public Instruction(String instruction, String quantifier) {
        this.type_ = this.inferInstructionTypeFromString(instruction);
        this.inferQuantifierFromString(quantifier);
    }

    private void inferQuantifierFromString(String quantifier) {
        // Assume that the instruction is not universally quantified
        this.hasAllQuantifier_ = false;

        if (quantifier == null) {
            // If there are no quantifier, and that quantifier is required,
            // then declare command invalid
            if (this.type_.doesRequireQuantifier) {
                this.type_ = Type.INVALID;
                return;
            }

            // If there are no quantifier, and that the type is
            // universally quantifiable, then let the quantifier
            // be universal
            if (this.type_.isAllQuantifiable) {
                this.hasAllQuantifier_ = true;
            }

            // If there are no quantifier, and the type is not
            // universally quantifiable, then there is nothing to worry about
            // Just remember to set the hasAllQuantifier to false
        } else {

            // If quantifier is not null and corresponds to the pre-defined
            // universal quantifier, then let the quantifier be universal
            if (this.type_.isAllQuantifiable && quantifier.equals(STRING_QUANTIFIER_ALL)) {
                this.hasAllQuantifier_ = true;
            }

            // If quantifier is not null, and does not correspond to the
            // pre-defined universal quantifier, then attempts to convert it
            // into an integer value
            else {
                try {
                    this.index_ = Integer.parseInt(quantifier);
                } catch (NumberFormatException e) {
                    // Number cannot be verified, tag the type as invalid
                    this.type_ = Type.INVALID;
                }
            }
        }

    }

    private Type inferInstructionTypeFromString(String instruction) {
        // Instruction should be trimmed and lower case
        instruction = instruction.trim().toLowerCase();

        // Search through the instruction in the definitions
        for (Type type : Type.values()) {
            if (type.keyword.equals(instruction)) {
                return type;
            }
        }

        return Type.UNRECOGNISED;
    }

    public boolean hasAllQuantifier() {
        return this.hasAllQuantifier_;
    }

    public Type getType() {
        return this.type_;
    }

    public Integer getIndex() {
        return this.index_;
    }

    public void setIndex(Integer index) {
        assert(index != null);

        this.index_ = index;
    }
}
