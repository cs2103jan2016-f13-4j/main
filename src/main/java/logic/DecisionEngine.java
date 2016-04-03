package logic;

import javafx.util.Pair;
import shared.*;
import skeleton.CollectionSpec;
import skeleton.DecisionEngineSpec;
import skeleton.SchedulerSpec;
import storage.Storage;
import storage.TaskPriorityComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @@author Thenaesh Elango
 */
public class DecisionEngine implements DecisionEngineSpec {
    public static final double THRESHOLD_POWERSEARCH_WEIGHTED = 0.0;
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

    /**
     * creates a Task from a specified command object when it makes sense we
     * should blow up when creating a Task doesn't really make sense
     * 
     * @param cmd
     * @return
     */
    protected Task createTask(Command cmd) {
        // initialisation
        String name = null;
        CustomTime from = null;
        CustomTime to = null;

        // for each command parameter, check if it was supplied
        // if so, extract the value and set the appropriate reference above to
        // point to the extracted value
        if (cmd.hasParameter(Command.ParamName.TASK_NAME)) {
            name = cmd.getParameter(Command.ParamName.TASK_NAME);
        }
        if (cmd.hasParameter(Command.ParamName.TASK_START)) {
            from = cmd.getParameter(Command.ParamName.TASK_START);
        }
        if (cmd.hasParameter(Command.ParamName.TASK_END)) {
            to = cmd.getParameter(Command.ParamName.TASK_END);
        }

        // we now build the Task object for adding into the store
        return new Task(null, name, "", from, to);
    }

    protected ExecutionResult displayAllTasks() {
        List<Task> listToDisplay = this.getTaskCollection().getAll().stream()
                .sorted(TaskPriorityComparator.getInstance()).collect(Collectors.toList());

        return new ExecutionResult(ViewType.TASK_LIST, listToDisplay,null);
    }

    protected ExecutionResult handleAdd(Command command) {
        assert command.hasInstruction(Command.Instruction.ADD);

        Task taskToAdd = this.createTask(command);

        final int id = this.getTaskCollection().save(taskToAdd);

        // wipe out redo stack
        this.redoOperations.clear();

        // add the corresponding undo operation
        this.undoOperations.push(v -> {
            this.getTaskCollection().remove(id);

            // add corresponding redo operation
            this.redoOperations.push(w -> {
                this.handleAdd(command);
                return (Void) null;
            });

            return (Void) null;
        });

        return this.displayAllTasks();
    }

    protected ExecutionResult handleEdit(Command command) {
        assert command.hasInstruction(Command.Instruction.EDIT);

        Integer index = command.getIndex();
        assert index != null;
        Task task = this.getTaskCollection().get(index);

        final Task originalTaskCopy = task.clone();

        // check which parameters have changed
        if (command.hasParameter(Command.ParamName.TASK_NAME)) {
            task.setTaskName(command.getParameter(Command.ParamName.TASK_NAME));
        }
        if (command.hasParameter(Command.ParamName.TASK_START)) {
            task.setStartTime(command.getParameter(Command.ParamName.TASK_START));
        }
        if (command.hasParameter(Command.ParamName.TASK_END)) {
            task.setEndTime(command.getParameter(Command.ParamName.TASK_END));
        }

        // wipe out redo stack
        this.redoOperations.clear();

        // add corresponding undo operation
        this.undoOperations.push(v -> {

            this.getTaskCollection().save(originalTaskCopy);

            this.undoOperations.push(w -> {
                this.handleEdit(command);
                return (Void) null;
            });
            return (Void) null;
        });

        return this.displayAllTasks();
    }

    protected ExecutionResult handleDisplay(Command command) {
        assert command.hasInstruction(Command.Instruction.DISPLAY);
        return this.displayAllTasks();
    }

    protected ExecutionResult handleDelete(Command command) {
        assert command.hasInstruction(Command.Instruction.DELETE);

        // // Handle case where delete is aggregate
        // // TODO: Make this undo-able
        // if (command.isUniversallyQuantified()) {
        // // For undoing
        // this.getTaskCollection().getAll().stream()
        // .map(Task::clone)
        // .forEach(task -> task.setDeletedStatus(true));
        // // Temporary
        // this.getTaskCollection().getAll().stream()
        // .mapToInt(Task::getId)
        // .forEach(this.getTaskCollection()::remove);
        // return this.displayAllTasks();
        // }

        Integer id = command.getIndex();
        assert id != null;

        Task deletedTask = this.getTaskCollection().get(id).clone();
        deletedTask.setDeletedStatus(true);

        // wipe out the redo stack
        this.redoOperations.clear();

        // add the corresponding undo operation
        this.undoOperations.push(v -> {
            this.getTaskCollection().undelete(id);

            this.redoOperations.push(w -> {
                this.handleDelete(command);
                return (Void) null;
            });

            return (Void) null;
        });

        this.getTaskCollection().remove(id);
        return this.displayAllTasks();
    }

    protected ExecutionResult handleSearch(Command command) {
        assert command.hasInstruction(Command.Instruction.SEARCH);

        // PowerSearching!
        Pattern pattern = buildPowerSearchPattern(command);
        double averageQueryLength = getAverageQueryLength(command);

        List<Task> foundTask = this.getTaskCollection().getAll().stream().map(item -> {
            // Match with task name first
            Matcher m = pattern.matcher(item.getTaskName());
            int matches = 0;
            List<Double> similarityIndex = new ArrayList<>();

            while (m.find()) {
                matches++;
                double similarity = averageQueryLength / m.group("MATCH").length();
                if (similarity > 1.0) {
                    similarity = 1.0;
                }
                similarityIndex.add(similarity);
            }

            double weightedMatch = matches * similarityIndex.stream()
                    .mapToDouble(Double::doubleValue).average().orElse(0.0);
            return new Pair<>(weightedMatch, item);
        }).filter(pair -> pair.getKey() > THRESHOLD_POWERSEARCH_WEIGHTED)
                .sorted((pair1, pair2) -> pair2.getKey().compareTo(pair1.getKey()))
                .map(Pair::getValue)
                .collect(Collectors.toList());

        return new ExecutionResult(ViewType.TASK_LIST, foundTask , null);
    }

    protected ExecutionResult handleUndo(Command command) {
        assert command.hasInstruction(Command.Instruction.UNDO);

        // attempt an undo only if the undo stack is not empty
        if (!this.undoOperations.isEmpty()) {
            this.undoOperations.pop().apply(null);
        }

        return this.displayAllTasks();
    }

    protected ExecutionResult handleRedo(Command command) {
        assert command.hasInstruction(Command.Instruction.UNDO);

        if (!this.redoOperations.isEmpty()) {
            this.redoOperations.pop().apply(null);
        }

        return this.displayAllTasks();
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
        patternBuilder.append("\\b(?<MATCH>");

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

    private static double getAverageQueryLength(Command command) {
        String query = command.getParameter(Command.ParamName.SEARCH_QUERY);
        return Arrays.asList(query.split("\\s+")).stream()
                .mapToDouble(String::length)
                .average().orElse(0.0);
    }
}
