package component;

import java.io.IOException;

import component.back_end.DecisionEngine;
import component.front_end.TranslationEngine;
import entity.ExecutionResult;
import entity.command.Command;
import entity.command.Instruction;
import javafx.stage.Stage;
import skeleton.DispatcherSpec;

/**
 * Created by maianhvu on 8/3/16.
 */
public class Dispatcher extends DispatcherSpec {
    private final TranslationEngine translationEngine_;
    private final DecisionEngine decisionEngine_;

    /**
     * Constructs a default dispatcher.
     * @throws IOException 
     */
    public Dispatcher(Stage primaryStage) throws IOException {
        this.translationEngine_ = new TranslationEngine(primaryStage);
        this.decisionEngine_ = new DecisionEngine();
    }

    /**
     * Returns the main translation engine used by this dispatcher.
     * @return the main translation engine
     */
    @Override public TranslationEngine getTranslationEngine() {
        return this.translationEngine_;
    }

    /**
     * Returns the main decision engine used by this dispatcher.
     * @return the main decision engine
     */
    @Override public DecisionEngine getDecisionEngine() {
        return this.decisionEngine_;
    }

    /**
     * Starts the dispatcher using an initial command.
     * After that enter programme's loop until command has a terminate instruction
     */
    @Override public void pulse() {
        Command nextCommand = Command.getInitialCommand();

        // Let decision engine decide what to do with the command first
        ExecutionResult<?> result = this.decisionEngine_.performCommand(nextCommand);

        // Show the result
        this.translationEngine_.display(result);

        // Read the next command
        nextCommand = this.translationEngine_.getNextCommand();

        // TODO: Terminate the program

    }

    private static boolean isTerminateCommand(Command command) {
        return command.getInstruction().getType() == Instruction.Type.EXIT;
    }
}
