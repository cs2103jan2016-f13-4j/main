package logic;

import shared.*;
import skeleton.DecisionEngineSpec;
import skeleton.DispatcherSpec;
import skeleton.TranslationEngineSpec;

public class Dispatcher implements DispatcherSpec {

    /**
     * Singleton instance
     */
    private static Dispatcher instance;

    /**
     * TODO: Write JavaDoc
     */
    private Dispatcher() {
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
        this.getTranslationEngine().setCommandExecutionHandler(command -> {
            ExecutionResult result = getDecisionEngine().performCommand(command);
            getTranslationEngine().displayResult(result);
            return null;
        });
    }

    @Override public void start() {
        // Build initial command
        Instruction initialInstruction = new Instruction(Instruction.Type.DISPLAY);
        ParameterList initialParams = ParameterList.emptyList();
        Command initialCommand = new Command(initialInstruction, initialParams);

        ExecutionResult result = getDecisionEngine().performCommand(initialCommand);
        getTranslationEngine().displayResult(result);
    }

    @Override public TranslationEngineSpec getTranslationEngine() {
        return TranslationEngine.getInstance();
    }

    @Override public DecisionEngineSpec getDecisionEngine() {
        return DecisionEngine.getInstance();
    }

}
