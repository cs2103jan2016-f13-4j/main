package logic;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import shared.*;
import skeleton.CollectionSpec;
import skeleton.DecisionEngineSpec;
import skeleton.SchedulerSpec;
import storage.Storage;
import storage.TaskPriorityComparator;

/**
 * @@author Thenaesh Elango
 */
public class DecisionEngine implements DecisionEngineSpec {
    /**
     * Singleton instance and constructor
     */
    private static DecisionEngine instance;

    private DecisionEngine() {
    }

    /**
     * instance fields
     */
    private Stack<Function<Void, Void>> undoOperations = new Stack<>(); // used for undoing
    private Stack<Function<Void, Void>> redoOperations = new Stack<>();

    public static DecisionEngine getInstance() {
        if (instance == null) {
            instance = new DecisionEngine();
        }

        return instance;
    }

    @Override public void initialise() {
        Storage.getInstance().readFromDisk();
    }

    /**
     * checks whether the supplied command is completely defined (name, start
     * time, end time, etc) this information may then be used to decide if the
     * Scheduler should be called
     *
     * @param cmd
     * @return
     */
    boolean isCommandComplete(Command cmd) {
        boolean hasName = cmd.hasParameter(Command.ParamName.TASK_NAME);
        boolean hasStart = cmd.hasParameter(Command.ParamName.TASK_START);
        boolean hasEnd = cmd.hasParameter(Command.ParamName.TASK_END);

        boolean isComplete = hasName && hasStart && hasEnd;
        return isComplete;
    }

    boolean isCommmandQuery(Command cmd) {
        return cmd.hasParameter(Command.ParamName.SEARCH_QUERY);
    }



    protected ExecutionResult displayAllTasks() {
        List<Task> listToDisplay = this.getTaskCollection().getAll().stream()
                .sorted(TaskPriorityComparator.getInstance()).collect(Collectors.toList());

        return new ExecutionResult(ViewType.TASK_LIST, listToDisplay);
    }

    protected ExecutionResult handleDisplay(Command command) {
        assert command.hasInstruction(Command.Instruction.DISPLAY);
        return this.displayAllTasks();
    }


    protected ExecutionResult handleSearch(Command command) {
        assert command.hasInstruction(Command.Instruction.SEARCH);

        // PowerSearching!
        Pattern pattern = buildPowerSearchPattern(command);

        List<Task> foundTask = this.getTaskCollection().getAll().stream().filter(item -> {
            // Match with task name first
            Matcher m = pattern.matcher(item.getTaskName());
            if (m.find())
                return true;

            // If doesn't match with task name, try to match
            // with description ONLY IF it's not null
            if (item.getDescription() == null)
                return false;
            m = pattern.matcher(item.getDescription());

            return m.find();
        }).collect(Collectors.toList());

        return new ExecutionResult(ViewType.TASK_LIST, foundTask);
    }



    @Override public ExecutionResult performCommand(Command command) {

        // this sort of nonsense should have been handled in the front end
        assert !command.hasInstruction(Command.Instruction.INVALID);
        assert !command.hasInstruction(Command.Instruction.UNRECOGNISED);

        // handle exit command here, without creating a task unnecessarily
        if (command.hasInstruction(Command.Instruction.EXIT)) {
            return ExecutionResult.shutdownSignal();
        }

        // Prepare final execution result to be returned
        ExecutionResult result = null;

        // all the standard commands
        switch (command.getInstruction()) {
            case ADD:
            case DELETE:
            case EDIT:
                StorageWriteOperation op = new StorageWriteOperation(command);
                op.getInitialOperation().apply(null);
                StorageWriteOperationHistory.getInstance().addToHistory(op);
                result = this.displayAllTasks();
            case DISPLAY:
                result = this.handleDisplay(command);
                break;
            case SEARCH:
                result = this.handleSearch(command);
                break;
            case UNDO:
                StorageWriteOperationHistory.getInstance().undo();
                result = this.displayAllTasks();
                break;
            case REDO:
                StorageWriteOperationHistory.getInstance().redo();
                result = this.displayAllTasks();
                break;
            default:
                // if we reach this point, LTA Command Parser has failed in his duty
                // and awaits court martial
                assert false;
        }

        return result;
    }

    @Override public SchedulerSpec getTaskScheduler() {
        return Scheduler.getInstance();
    }

    @Override public void shutdown() {
        Storage.getInstance().writeToDisk();
    }

    @Override public CollectionSpec<Task> getTaskCollection() {
        return Storage.getInstance();
    }

    private static Pattern buildPowerSearchPattern(Command command) {
        String query = command.getParameter(Command.ParamName.SEARCH_QUERY);
        // Split the query into words
        String[] words = query.split("\\s+");

        // Begin building pattern by signalling that we are looking for
        // a word that contains the characters
        StringBuilder patternBuilder = new StringBuilder();
        patternBuilder.append("\\b(?:");

        // In that particular order. We achieve this by inserting
        // greedy word (\w*) pattern, slotted between the characters of
        // each of the query word
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i != 0) {
                patternBuilder.append("|");
            }

            for (int j = 0; j < word.length(); j++) {
                patternBuilder.append("\\w*");
                patternBuilder.append(word.charAt(j));
            }
            patternBuilder.append("\\w*"); // At the end too
        }

        // Conclude the pattern
        patternBuilder.append(")\\b");
        return Pattern.compile(patternBuilder.toString(), Pattern.CASE_INSENSITIVE);
    }

}
