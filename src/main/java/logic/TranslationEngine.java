package logic;

import java.util.List;
import java.util.function.Function;

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

    @Override public void setCommandExecutionHandler(Function<Command, ExecutionResult> handler) {
        assert (handler != null);
        this._commandExecutionHandler = handler;
    }

    @Override public void initialise() {
        // Trigger initialisation of CommandParser
        this.getCommandParser().initialise();

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
            // Convert list to one with visual IDs only
            List<Pair<Integer, Task>> visualTaskList = getVisualIndexMapper().translateRawToVisual(result.getData());

            // Update mapper with list
            VisualIndexMapper.getInstance().updateList(result.getData());
            View view = new TaskListView(visualTaskList);
            this.getUserInterface().render(view);
            break;
        default:
            break;
        }

        // Display notification
        this.displayNotification(result);
    }

    @Override public void shutdown() {
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

    @Override public UserInterfaceSpec getUserInterface() {
        return UserInterface.getInstance();
    }

    @Override public CommandParserSpec getCommandParser() {
        return FlexiCommandParser.getInstance();
    }

    private static VisualIndexMapper getVisualIndexMapper() {
        return VisualIndexMapper.getInstance();
    }

    private void displayNotification(ExecutionResult result) {
        String message = "Welcome to Your MOM!";

        if (this._lastCommand != null) {
            switch (this._lastCommand.getInstruction()) {
                case DISPLAY:
                    int taskCount = ((List<?>) result.getData()).size();
                    if (taskCount == 0) {
                        message = "Add a new to-do by entering \"add <task>\"!";
                    } else {
                        message = String.format("Found %d to-dos!", taskCount);
                    }
                    break;
                case ADD:
                    // TODO: Take care of failed addition
                    String taskName = this._lastCommand.getParameter(Command.ParamName.TASK_NAME);
                    message = String.format("Added: %s", taskName);
                    break;
                case EDIT:
                    // TODO: Take care of failed edit
                    message = String.format("Edited task number %d with new details!",
                            this._lastCommand.getIndex());
                    break;
                case DELETE:
                    message = String.format("Deleted task number %d! You can undo this by entering \"undo\"",
                            this._lastCommand.getIndex());
                    break;
                case  SEARCH:
                    int searchFound = ((List<?>) result.getData()).size();
                    String searchQuery = this._lastCommand.getParameter(Command.ParamName.SEARCH_QUERY);
                    if (searchFound == 0) {
                        message = String.format("Found no to-do with the search query \"%s\"", searchQuery);
                    } else {
                        message = String.format("Found %d matches with the query \"%s\"",
                                searchFound,
                                searchQuery);
                    }
                    break;
            }
        }

        this.getUserInterface().showNotification(message);
    }
}
