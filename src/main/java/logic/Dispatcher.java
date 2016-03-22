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

            // Handle shutdown
            if (result.isShutdownSignal()) {
                // Shutdown both engines
                this.getDecisionEngine().shutdown();
                this.getTranslationEngine().shutdown();
                // Demand application to close
                ApplicationContext.getPrimaryStage().close();
                return result;
            }

            // If not, gracefully falls through to displaying result
            getTranslationEngine().displayResult(result);
            return result;
        };
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
        // Build initial command
        Instruction initialInstruction = new Instruction(Instruction.Type.DISPLAY);
        ParameterList initialParams = ParameterList.emptyList();
        Command initialCommand = new Command(initialInstruction, initialParams);

        this._commandExecutor.andThen(result -> {
            this.getTranslationEngine().displayResult(result);
            return null;
        }).apply(initialCommand);
    }

    @Override public TranslationEngineSpec getTranslationEngine() {
        return TranslationEngine.getInstance();
    }

    @Override public DecisionEngineSpec getDecisionEngine() {
        return DecisionEngine.getInstance();
    }

}
