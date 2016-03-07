package entity.command;

/**
 * Created by maianhvu on 6/3/16.
 */
public class Instruction {

    public static final String STRING_QUANTIFIER_UNIVERSAL = "all";

    /**
     * Types
     */
    public enum Type {
        // TYPE_NAME ( isUniversallyQuantifiable, doesRequireQuantifier )
        ADD          ( false,                     false),
        EDIT         ( false,                     true),
        DISPLAY      ( true,                      false),
        DELETE       ( true,                      true),
        EXIT         ( false,                     true),

        // Special types
        INVALID     (false, false),
        UNRECOGNISED(false, false);

        // Type properties
        final boolean isUniversallyQuantifiable;
        final boolean doesRequireQuantifier;

        // Type constructor
        Type(boolean uniQuantifiable, boolean requireQuantifier) {
            isUniversallyQuantifiable = uniQuantifiable;
            doesRequireQuantifier = requireQuantifier;
        }

    }

    /**
     * Properties
     */
    private Type type_;
    private boolean isUniversallyQuantified_;
    private Integer indexQuantifier_;

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
     * @param indexQuantifier
     */
    public Instruction(Type type, int indexQuantifier) {
        this.type_ = type;
        this.isUniversallyQuantified_ = false;
        this.indexQuantifier_ = indexQuantifier;
    }

    public Instruction(String instruction, String quantifier) {
        this.type_ = this.inferInstructionTypeFromString(instruction);
        this.inferQuantifierFromString(quantifier);
    }

    private void inferQuantifierFromString(String quantifier) {
        // Assume that the instruction is not universally quantified
        this.isUniversallyQuantified_ = false;

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
            if (this.type_.isUniversallyQuantifiable) {
                this.isUniversallyQuantified_ = true;
            }

            // If there are no quantifier, and the type is not
            // universally quantifiable, then there is nothing to worry about
            // Just remember to set the isUniversallyQuantified to false
        } else {

            // If quantifier is not null and corresponds to the pre-defined
            // universal quantifier, then let the quantifier be universal
            if (this.type_.isUniversallyQuantifiable && quantifier.equals(STRING_QUANTIFIER_UNIVERSAL)) {
                this.isUniversallyQuantified_ = true;
            }

            // If quantifier is not null, and does not correspond to the
            // pre-defined universal quantifier, then attempts to convert it
            // into an integer value
            else {
                try {
                    this.indexQuantifier_ = Integer.parseInt(quantifier);
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
        switch (instruction) {
            case "add":
                return Type.ADD;
            case "edit":
                return Type.EDIT;
            case "display":
                return Type.DISPLAY;
            case "delete":
                return Type.DELETE;
            case "exit":
                return Type.EXIT;
            default:
                return Type.UNRECOGNISED;
        }
    }

    public boolean isUniversallyQuantified() {
        return this.isUniversallyQuantified_;
    }

    public Type getType() {
        return this.type_;
    }

    public Integer getIndexQuantifier() {
        return this.indexQuantifier_;
    }
}
