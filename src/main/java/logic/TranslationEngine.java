package logic;

import javafx.util.Pair;
import shared.Command;
import shared.ExecutionResult;
import skeleton.CommandParserSpec;
import skeleton.TranslationEngineSpec;
import skeleton.UserInterfaceSpec;
import storage.Task;
import ui.UserInterface;
import ui.view.TextListView;
import ui.view.View;

import java.util.List;
import java.util.function.Function;

public class TranslationEngine implements TranslationEngineSpec {

    /**
     * Singleton instance
     */
    private static TranslationEngine instance;

    /**
     * Properties
     */
    private Function<Command, ExecutionResult> _commandExecutionHandler;
    private VisualIndexMapper _indexMapper;

    /**
     * Private constructor
     */
    private TranslationEngine() {
    }

    /**
     * Singleton getter TODO: Write a more descriptive JavaDoc
     *
     * @return
     */
    public static TranslationEngine getInstance() {
        if (instance == null) {
            instance = new TranslationEngine();
        }
        return instance;
    }

    @Override public void setCommandExecutionHandler(Function<Command, ExecutionResult> handler) {
        assert (handler != null);
        this._commandExecutionHandler = handler;
    }

    @Override public void initialise() {
        assert (this._commandExecutionHandler != null);

        // Create input handler
        Function<String, Void> commandInputHandler = commandString -> {
            // Translate the raw command string given
            instance.translateCommand(commandString);
            return null;
        };

        // Attach input handler to user interface
        UserInterfaceSpec ui = this.getUserInterface();
        ui.initialize();
        ui.setOnCommandInputHandler(commandInputHandler);
        ui.show();
    }

    /**
     * TODO: Write JavaDoc
     *
     * @param result
     */
    @Override public void displayResult(ExecutionResult result) {
        if (result.isShutdownSignal()) {
            // Do not display shutdown signal
            return;
        }

        switch (result.getViewType()) {
            case TASK_LIST:
                // Convert list
                List<Pair<Integer, Task>> visualTaskList =
                        VisualIndexMapper.getInstance().translateRawToVisual(result.getData());
                View view = new TextListView(visualTaskList);
                this.getUserInterface().render(view);
                break;
        }
    }

    @Override
    public void shutdown() {
        this.getUserInterface().cleanUp();
    }

    private void translateCommand(String commandString) {
        assert commandString != null;
        assert this._commandExecutionHandler != null;

        Command command = this.getCommandParser().parse(commandString);

        if (command.getInstruction().getIndex() != null) {
            assert this._indexMapper != null;
            this._indexMapper.translateVisualToRaw(command);
        }

        // Schedule for displaying
        this._commandExecutionHandler.andThen(result -> {
            this.displayResult(result);
            return null;
        }).apply(command);
    }

    @Override public UserInterfaceSpec getUserInterface() {
        return UserInterface.getInstance();
    }

    @Override public CommandParserSpec getCommandParser() {
        return CommandParser.getInstance();
    }

}
