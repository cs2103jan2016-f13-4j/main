package logic;

import javafx.util.Pair;
import shared.*;
import skeleton.StorageSpec;
import skeleton.DecisionEngineSpec;
import skeleton.SchedulerSpec;
import skeleton.WriteHistorySpec;
import storage.Storage;
import storage.TaskPriorityComparator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @@author A0124772E
 */
public class DecisionEngine implements DecisionEngineSpec {

    // singleton instance, cosntructor and accessor
    private static DecisionEngine instance = new DecisionEngine();
    public static DecisionEngine getInstance() {
        if (instance == null) {
            instance = new DecisionEngine();
        }
        return instance;
    }
    private DecisionEngine() {
    }


    /**
     * the most important method in this class
     * this is the method that gets called by the Dispatcher
     * it takes in a command, executes it, and returns the execution result back to the caller
     * @param command
     * @return
     */
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
            case MARK:
                result = this.handleWriteOperation(command);
                break;
            case DISPLAY:
                result = this.handleDisplay(command);
                break;
            case SEARCH:
                result = this.handleSearch(command);
                break;
            case SCHEDULE:
                result = this.handleSchedule(command);
                break;
            case UNDO:
                boolean undoActuallyHappened = this.getWriteHistory().undo();
                result = this.displayAllTasks();
                if (!undoActuallyHappened) {
                    result.setErrorMessage(Message.UNDO_FAIL.toString());
                }
                break;
            case REDO:
                boolean redoActuallyHappened = this.getWriteHistory().redo();
                result = this.displayAllTasks();
                if (!redoActuallyHappened) {
                    result.setErrorMessage(Message.REDO_FAIL.toString());
                }
                break;
            default:
                // if we reach this point, LTA Command Parser has failed in his duty
                // and awaits court martial
                assert false;
        }

        return result;
    }


    @Override public void initialise() {
        StorageSpec<?> storage = this.getStorage();
        storage.initialise();
    }

    @Override public void shutdown() {
        StorageSpec<?> storage = this.getStorage();
        storage.shutdown();
    }


    ///////////////////////////////
    // START OF COMMAND HANDLERS //
    ///////////////////////////////

    /**
     * handles any one of ADD/DELETE/EDIT/MARK commands
     * essentially handles any commands that involve changing the state of one or more tasks in the Storage handler
     * another way to look at this method is that it handles any undo-able commands
     * @param command
     * @return
     */
    protected ExecutionResult handleWriteOperation(Command command) {
        assert command.hasInstruction(Command.Instruction.ADD)
                || command.hasInstruction(Command.Instruction.DELETE)
                || command.hasInstruction(Command.Instruction.EDIT)
                || command.hasInstruction(Command.Instruction.MARK);

        StorageWriteOperation op = new StorageWriteOperation(command, this.getStorage());
        String errorMsg = this.getWriteHistory().addToHistoryAfterExecuting(op);

        ExecutionResult result = this.displayAllTasks();
        result.setErrorMessage(errorMsg);

        return result;
    }

    /**
     * handles a DISPLAY command with bounds
     *
     * @param command
     * @return
     */
    protected ExecutionResult handleDisplay(Command command) {
        assert command.hasInstruction(Command.Instruction.DISPLAY);

        List<Task> listToDisplay = this.getStorage().getAll();

        // for each command parameter, filter the list of tasks
        if (command.hasParameter(Command.ParamName.TASK_NAME)) {
            String name = command.getParameter(Command.ParamName.TASK_NAME);
            listToDisplay = listToDisplay
                    .stream()
                    .filter(task -> task.getTaskName().equals(name))
                    .collect(Collectors.toList());
        }
        if (command.hasParameter(Command.ParamName.TASK_START)) {
            CustomTime from = command.getParameter(Command.ParamName.TASK_START);
            listToDisplay = listToDisplay
                    .stream()
                    .filter(task -> from.compareTo(task.getStartTime()) <= 0)
                    .collect(Collectors.toList());
        }
        if (command.hasParameter(Command.ParamName.TASK_END)) {
            CustomTime to = command.getParameter(Command.ParamName.TASK_END);
            listToDisplay = listToDisplay
                    .stream()
                    .filter(task -> to.compareTo(task.getEndTime()) >= 0)
                    .collect(Collectors.toList());
        }

        Collections.sort(listToDisplay, TaskPriorityComparator.getInstance());

        // at this point, we have a properly filtered list
        return new ExecutionResult(ViewType.TASK_LIST, listToDisplay);
    }


    /**
     * handles a SEARCH command, including PowerSearch functionality
     *
     * @param command
     * @return
     */
    protected ExecutionResult handleSearch(Command command) {
        assert command.hasInstruction(Command.Instruction.SEARCH);

        // PowerSearching!
        Pattern pattern = buildPowerSearchPattern(command);
        double averageQueryLength = getAverageQueryLength(command);

        List<Task> foundTask = this.getStorage().getAll().stream().map(item -> {
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

        return new ExecutionResult(ViewType.TASK_LIST, foundTask);
    }

    /**
     * handles the SCHEDULE command
     * @param command
     * @return
     */
    protected ExecutionResult handleSchedule(Command command) {
        assert command.hasInstruction(Command.Instruction.SCHEDULE);

        assert command.hasParameter(Command.ParamName.TASK_INDEX);
        Integer id = command.getParameter(Command.ParamName.TASK_INDEX);

        assert command.hasParameter(Command.ParamName.TASK_DURATION);
        Integer duration = command.getParameter(Command.ParamName.TASK_DURATION);
        assert duration != null;
        TemporalRange rangeToScheduleIn = this.getTaskScheduler().schedule(duration);

        // transform the SCHEDULE command into an EDIT command
        Command editCommand = new Command(Command.Instruction.EDIT);
        editCommand.setParameter(Command.ParamName.TASK_INDEX, id);
        editCommand.setParameter(Command.ParamName.TASK_START, rangeToScheduleIn.getStart());
        editCommand.setParameter(Command.ParamName.TASK_END, rangeToScheduleIn.getEnd());

        // just handle the newly generate EDIT command the usual way
        return this.handleWriteOperation(editCommand);
    }

    /////////////////////////////
    // END OF COMMAND HANDLERS //
    /////////////////////////////

    /**
     * a special display method that displays all tasks currently stored
     * @return
     */
    protected ExecutionResult displayAllTasks() {
        List<Task> listToDisplay = this.getStorage().getAll().stream()
                .sorted(TaskPriorityComparator.getInstance()).collect(Collectors.toList());

        return new ExecutionResult(ViewType.TASK_LIST, listToDisplay);
    }


    private SchedulerSpec getTaskScheduler() {
        return Scheduler.getInstance();
    }

    private StorageSpec<Task> getStorage() {
        return Storage.getInstance();
    }

    private WriteHistorySpec getWriteHistory() {
        return WriteHistory.getInstance();
    }


    // HELPER METHODS & FIELDS //

    private static final double THRESHOLD_POWERSEARCH_WEIGHTED = 0.0;

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
