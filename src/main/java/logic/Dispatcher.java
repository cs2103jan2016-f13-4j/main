package logic;

import shared.*;
import skeleton.DecisionEngineSpec;
import skeleton.DispatcherSpec;
import skeleton.TranslationEngineSpec;

import java.util.function.Function;

public class Dispatcher implements DispatcherSpec {

    /**
     * Singleton instance
     */
    private static Dispatcher instance;

    /**
     * Properties
     */
    private final Function<Command, ExecutionResult> _commandExecutor;

    /**
     * TODO: Write JavaDoc
     */
    private Dispatcher() {
        this._commandExecutor = command -> {
            ExecutionResult result = getDecisionEngine().performCommand(command);
            assert result != null;

            // Handle shutdown
            if (result.isShutdownSignal()) {

                // Shutdown both engines
                this.shutdown();

                // Demand application to close
                ApplicationContext.mainContext().getPrimaryStage().close();

                return result;
            }

            // If not, gracefully falls through to displaying result
            getTranslationEngine().displayResult(result);
            return result;
        };
    }

    @Override
    public void shutdown() {
        this.getDecisionEngine().shutdown();
        this.getTranslationEngine().shutdown();
    }

    /**
     * TODO: Write JavaDoc
     *
     * @return
     */
    public static Dispatcher getInstance() {
        if (instance == null) {
            instance = new Dispatcher();
        }
        return instance;
    }

    @Override public void initialise() {
        // Initialize DecisionEngine and trigger its storage
        this.getDecisionEngine().initialise();

        // Initialize TranslationEngine and trigger its view
        this.getTranslationEngine().initialise();

        // Add command handler to translation engine
        this.getTranslationEngine().setCommandExecutionHandler(this._commandExecutor);
    }

    @Override public void start() {
        // Execute and display the result of the initial command
        this._commandExecutor.andThen(result -> {
            this.getTranslationEngine().displayResult(result);
            return null;
        }).apply(constructInitialCommand());
    }

    @Override public TranslationEngineSpec getTranslationEngine() {
        return TranslationEngine.getInstance();
    }

    @Override public DecisionEngineSpec getDecisionEngine() {
        return DecisionEngine.getInstance();
    }

    private static Command constructInitialCommand() {
        Command.Instruction instruction = Command.Instruction.DISPLAY;
        Integer index = null;
        boolean isUniversallyQuantified = true;

        return new Command(instruction, index, isUniversallyQuantified);
    }

}
