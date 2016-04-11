package logic;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import javafx.util.Pair;
import shared.Command;
import shared.ExecutionResult;
import shared.Message;
import shared.Task;
import skeleton.CommandParserSpec;
import skeleton.TranslationEngineSpec;
import skeleton.UserInterfaceSpec;
import ui.UserInterface;
import ui.view.TaskListView;
import ui.view.View;
import ui.view.VisualTask;

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
            this.translateCommand(commandString);
            return null;
        };

        // Attach input handler to user interface
        UserInterfaceSpec ui = this.getUserInterface();
        ui.setOnCommandInputHandler(commandInputHandler);
        ui.initialize();
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
            List<VisualTask> visualTaskList = getVisualIndexMapper()
                    .translateRawToVisual(result.getData());
            View view = new TaskListView(visualTaskList, this._lastCommand);
            this.getUserInterface().render(view);

            // Set header title
            if (this._lastCommand != null) {
                if (this._lastCommand.getInstruction() == Command.Instruction.SEARCH) {
                    String searchQuery = this._lastCommand.getParameter(Command.ParamName.SEARCH_QUERY);
                    this.getUserInterface().setHeaderTitle(String.format(
                            "Search results for \"%s\"",
                            searchQuery
                    ));
                } else {
                    this.getUserInterface().setHeaderTitle(
                            "All tasks"
                    );
                }
            }

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

        if (command.hasParameter(Command.ParamName.TASK_INDEX) ||
                command.hasParameter(Command.ParamName.TASK_INDEX_RANGES)) {
            VisualIndexMapper.getInstance().translateVisualToRaw(command);
        }

        // Catch unrecognised or invalid command
        if (command.getInstruction() == Command.Instruction.INVALID) {
            this.getUserInterface().showNotification(
                    command.getInvalidationMessage());
            return;
        } else if (command.getInstruction() == Command.Instruction.UNRECOGNISED) {
            this.getUserInterface().showNotification(
                    Message.UNRECOGNISED.toString());
            return;
        }

        // Set last command to this command
        this._lastCommand = command;

        // Schedule for displaying
        this._commandExecutionHandler.apply(command);
    }

    @Override public UserInterfaceSpec getUserInterface() {
        return UserInterface.getInstance();
    }

    @Override public CommandParserSpec getCommandParser() {
        return CommandParser.getInstance();
    }

    private static VisualIndexMapper getVisualIndexMapper() {
        return VisualIndexMapper.getInstance();
    }

    private void displayNotification(ExecutionResult result) {
        String message = Message.WELCOME.toString();

        if (this._lastCommand != null) {
            switch (this._lastCommand.getInstruction()) {
                case DISPLAY:
                    int taskCount = ((List<?>) result.getData()).size();
                    if (taskCount == 0) {
                        message = Message.DISPLAY_EMPTY.toString();
                    } else {
                        message = String.format(
                                Message.DISPLAY_NORMAL.toString(),
                                taskCount);
                    }
                    break;
                case ADD:
                    String taskName = this._lastCommand.getParameter(Command.ParamName.TASK_NAME);
                    message = String.format(Message.ADD_SUCCESS.toString(), taskName);
                    break;
                case EDIT:
                    message = Message.EDIT_SUCCESS.toString();
                    break;
                case DELETE:
                    if (result.hasErrorMessage()) {
                        message = result.getErrorMessage();
                    } else if (this._lastCommand.hasTrueValue(
                            Command.ParamName.TASK_UNIVERSALLY_QUANTIFIED)) {
                        message = Message.DELETE_ALL_SUCCESS.toString();
                    } else {
                        message = Message.DELETE_SUCCESS.toString();
                    }
                    break;
                case SEARCH:
                    int searchFound = ((List<?>) result.getData()).size();
                    String searchQuery = this._lastCommand.getParameter(Command.ParamName.SEARCH_QUERY);
                    if (searchFound == 0) {
                        message = String.format(Message.SEARCH_FAIL.toString(), searchQuery);
                    } else {
                        message = String.format(Message.SEARCH_SUCCESS.toString(),
                                searchFound,
                                searchQuery);
                    }
                    break;
                case UNDO:
                    if (result.hasErrorMessage()) {
                        message = result.getErrorMessage();
                    } else {
                        message = Message.UNDO_SUCCESS.toString();
                    }
                    break;
                case REDO:
                    if (result.hasErrorMessage()) {
                        message = result.getErrorMessage();
                    } else {
                        message = Message.REDO_SUCCESS.toString();
                    }
                    break;
                case MARK:
                    if (result.hasErrorMessage()) {
                        message = result.getErrorMessage();
                    } else if (this._lastCommand.hasTrueValue(
                            Command.ParamName.TASK_UNIVERSALLY_QUANTIFIED)) {
                        message = Message.MARK_ALL_SUCCESS.toString();
                    } else {
                        message = Message.MARK_SUCCESS.toString();
                    }
            }
        }

        this.getUserInterface().showNotification(message);
    }
}
