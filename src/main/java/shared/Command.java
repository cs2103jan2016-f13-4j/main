package shared;

import javafx.util.Pair;

public class Command {

    /**
     * Constants
     */
    private static final Instruction INSTRUCTION_COMMAND_INITIAL = new Instruction(Instruction.Type.DISPLAY);
    private static final ParameterList PARAMETERS_COMMAND_INITIAL = null;

    /**
     * Properties
     */
    private final Instruction instruction_;
    private final ParameterList parameters_;

    /**
     * Constructs a command based on the supplied type and parameters
     * @param instruction
     * @param parameters
     */
    public Command(Instruction instruction, ParameterList parameters) {
        this.instruction_ = instruction;
        this.parameters_ = parameters;
    }

    /**
     * Constructs the first command to be executed by the application stack
     * @return the required initial command
     */
    public static Command getInitialCommand() {
        return new Command(INSTRUCTION_COMMAND_INITIAL, PARAMETERS_COMMAND_INITIAL);
    }

    public Instruction getInstruction() {
        return this.instruction_;
    }

    public ParameterList getParameters() {
        return this.parameters_;
    }
}
