package logic;

import shared.*;
import skeleton.CollectionSpec;
import skeleton.DecisionEngineSpec;
import skeleton.SchedulerSpec;
import storage.Task;
import storage.Storage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DecisionEngine implements DecisionEngineSpec {
    /**
     * Singleton instance
     */
    private static DecisionEngine instance;

    private DecisionEngine() {
    }

    public static DecisionEngine getInstance() {
        if (instance == null) {
            instance = new DecisionEngine();
        }

        return instance;
    }

    @Override  public void initialise() {
        // TODO: stub
    }


    @Override public ExecutionResult performCommand(Command cmd) {

        // this sort of nonsense should have been handled in the front end
        assert (cmd.getInstruction().getType() != Instruction.Type.UNRECOGNISED);

        // handle exit command here, without creating a task unnecessarily
        if (cmd.getInstruction().getType() == Instruction.Type.EXIT) {
            ApplicationContext.getPrimaryStage().close();
            return null;
        }

        // Prepare final execution result to be returned
        ExecutionResult result = null;

        // all the standard commands
        switch (cmd.getInstruction().getType()) {
            case ADD:
                result = this.handleAdd(cmd);
                break;
            case EDIT:
                result = this.handleEdit(cmd);
                break;
            case DISPLAY:
                result = this.handleDisplay(cmd);
                break;
            case DELETE:
                result = this.handleDelete(cmd);
                break;
            case SEARCH:
                result = this.handleSearch(cmd);
                break;
            default:
                // if we reach this point, LTA Command Parser has failed in his duty
                // and awaits court martial
                assert false;
        }

        return result;
    }


    /**
     * checks whether the supplied command is completely defined (name, start time, end time, etc)
     * this information may then be used to decide if the Scheduler should be called
     *
     * @param cmd
     * @return
     */
    boolean isCommandComplete(Command cmd) {
        ParameterList params = cmd.getParameters();

        boolean hasName = params.hasParameterNamed(ParameterName.NAME);
        boolean hasStart = params.hasParameterNamed(ParameterName.DATE_FROM);
        boolean hasEnd = params.hasParameterNamed(ParameterName.DATE_TO);

        boolean isComplete = hasName && hasStart && hasEnd;
        return isComplete;
    }

    boolean isCommmandQuery(Command cmd) {
        ParameterList params = cmd.getParameters();

        return params.hasParameterNamed(ParameterName.QUERY);
    }


    /**
     * creates a Task from a specified command object when it makes sense
     * we should blow up when creating a Task doesn't really make sense
     * @param cmd
     * @return
     */
    protected Task createTask(Command cmd) {
        ParameterList params = cmd.getParameters();

        // initialisation
        String name = null;
        LocalDateTime from = null;
        LocalDateTime to = null;

        // for each command parameter, check if it was supplied
        // if so, extract the value and set the appropriate reference above to point to the extracted value
        if (params.hasParameterNamed(ParameterName.NAME)) {
            name = (String) params.getParameter(ParameterName.NAME).getValue();
        }
        if (params.hasParameterNamed(ParameterName.DATE_FROM)) {
            from = (LocalDateTime) params.getParameter(ParameterName.DATE_FROM).getValue();
        }
        if (params.hasParameterNamed(ParameterName.DATE_TO)) {
            to = (LocalDateTime) params.getParameter(ParameterName.DATE_TO).getValue();
        }

        // we now build the Task object for adding into the store
        return new Task(null, name, "", from, to);
    }

    protected ExecutionResult displayAllTasks(Command cmd) {
        assert cmd.getInstruction().getType() == Instruction.Type.DISPLAY;

        List<Task> listToDisplay = this.getTaskCollection().getAll();
        return new ExecutionResult(ViewType.TASK_LIST, listToDisplay);
    }


    protected ExecutionResult handleAdd(Command cmd) {
        assert cmd.getInstruction().getType() == Instruction.Type.ADD;

        Task taskToAdd = this.createTask(cmd);
        this.getTaskCollection().add(taskToAdd);

        return this.displayAllTasks(cmd);
    }

    protected ExecutionResult handleEdit(Command cmd) {
        assert cmd.getInstruction().getType() == Instruction.Type.EDIT;

<<<<<<< a3a5537ee34cd142e520083396e5c7b69e8590af
        ParameterList params = cmd.getParameters();

        int id = cmd.getInstruction().getIndex();
        Task task = this.getTaskCollection().get(id);

        // check which parameters have changed
        if (params.hasParameterNamed(ParameterName.NAME)) {
            task.setTaskName((String) params.getParameter(ParameterName.NAME).getValue());
        }
        if (params.hasParameterNamed(ParameterName.DATE_FROM)) {
            task.setStartTime((LocalDateTime) params.getParameter(ParameterName.DATE_FROM).getValue());
        }
        if (params.hasParameterNamed(ParameterName.DATE_TO)) {
            task.setEndTime((LocalDateTime) params.getParameter(ParameterName.DATE_TO).getValue());
        }

        return this.displayAllTasks(cmd);
    }

    protected ExecutionResult handleDisplay(Command cmd) {
        assert cmd.getInstruction().getType() == Instruction.Type.EDIT;
=======
        // PowerSearching!
        Pattern pattern = buildPowerSearchPattern(command);

        List<Task> foundTask = this.getTaskCollection().getAll()
                .stream()
                .filter(item -> {
                    // Match with task name first
                    Matcher m = pattern.matcher(item.getTaskName());
                    if (m.find()) return true;

                    // If doesn't match with task name, try to match
                    // with description ONLY IF it's not null
                    if (item.getDescription() == null) return false;
                    m = pattern.matcher(item.getDescription());

                    return m.find();
                })
                .collect(Collectors.toList());
>>>>>>> move powersearch to a separate method

        return this.displayAllTasks(cmd);
    }



    protected ExecutionResult handleDelete(Command cmd) {
        assert cmd.getInstruction().getType() == Instruction.Type.DELETE;

        int id = cmd.getInstruction().getIndex();
        this.getTaskCollection().remove(id);

        return this.displayAllTasks(cmd);
    }

    protected ExecutionResult handleSearch(Command cmd) {
        assert cmd.getInstruction().getType() == Instruction.Type.SEARCH;

        final String query = (String) cmd.getParameters().getParameter(ParameterName.QUERY).getValue();
        List<Task> foundTask = this.getTaskCollection().getAll()
                .stream()
                .filter(item ->
                        item.getTaskName().contains(query)
                        && item.getDescription().contains(query))
                .collect(Collectors.toList());

        return new ExecutionResult(ViewType.TASK_LIST, foundTask);
    }


    @Override
    public SchedulerSpec getTaskScheduler() {
        return Scheduler.getInstance();
    }

    @Override
    public void shutdown() {
        // TODO: stub
    }

    @Override
    public CollectionSpec<Task> getTaskCollection() {
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
        return Pattern.compile(patternBuilder.toString());
    }

}
