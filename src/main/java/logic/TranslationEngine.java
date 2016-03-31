package logic;

import javafx.util.Pair;
import shared.Command;
import shared.ExecutionResult;
import shared.Task;
import skeleton.CommandParserSpec;
import skeleton.TranslationEngineSpec;
import skeleton.UserInterfaceSpec;
import ui.UserInterface;
import ui.view.TaskListView;
import ui.view.View;

import java.util.List;
import java.util.function.Function;

/**
 * @@author Mai Anh Vu
 */
public class TranslationEngine implements TranslationEngineSpec {
    /**
     * Singleton instance
     */
    private static TranslationEngine instance;

    /**
     * Properties
     */
    private Function<Command, ExecutionResult> _commandExecutionHandler;
    private Command _lastCommand;

    /**
     * Private constructor
     */
    private TranslationEngine() {
        this._lastCommand = null;
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

    @Override
    public void setCommandExecutionHandler(Function<Command, ExecutionResult> handler) {
        assert (handler != null);
        this._commandExecutionHandler = handler;
    }

    @Override
    public void initialise() {
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
    @Override
    public void displayResult(ExecutionResult result) {
        if (result.isShutdownSignal()) {
            // Do not display shutdown signal
            return;
        }

        switch (result.getViewType()) {
            case TASK_LIST:
                // Convert list to one with visual IDs only
                List<Pair<Integer, Task>> visualTaskList =
                        getVisualIndexMapper().translateRawToVisual(result.getData());

                // Update mapper with list
                VisualIndexMapper.getInstance().updateList(result.getData());
                View view = new TaskListView(visualTaskList);
                this.getUserInterface().render(view);

                // Set title
                String title = "Here are all the things you should do today!";
                if (visualTaskList.isEmpty()) {
                    title = "You have got nothing left to do! Have something in mind?" +
                            " Add a new to-do by typing add name:\"<thing to do>\" and press Enter!";
                }

                // Account for search queries
                if (this._lastCommand != null &&
                        this._lastCommand.hasInstruction(Command.Instruction.SEARCH)) {
                    if (visualTaskList.isEmpty()) {
                        title = "No to-do with the query \"" +
                                this._lastCommand.getParameter(Command.ParamName.SEARCH_QUERY) +
                                "\" was found!";
                    } else {
                        title = String.format("Found %d items matching search query \"%s\"!",
                                visualTaskList.size(),
                                this._lastCommand.getParameter(Command.ParamName.SEARCH_QUERY));
                    }
                }

                this.getUserInterface().setHeader(title);

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

        if (command.getIndex() != null) {
            VisualIndexMapper.getInstance().translateVisualToRaw(command);
        }

        // Set last command to this command
        this._lastCommand = command;

        // Schedule for displaying
        this._commandExecutionHandler.andThen(result -> {
            this.displayResult(result);
            return null;
        }).apply(command);
    }

    @Override
    public UserInterfaceSpec getUserInterface() {
        return UserInterface.getInstance();
    }

    @Override
    public CommandParserSpec getCommandParser() {
        return CommandParser.getInstance();
    }

    private static VisualIndexMapper getVisualIndexMapper() {
        return VisualIndexMapper.getInstance();
    }

}
