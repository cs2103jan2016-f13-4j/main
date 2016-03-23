package logic;

import shared.*;
import skeleton.CollectionSpec;
import skeleton.DecisionEngineSpec;
import skeleton.SchedulerSpec;
import storage.Task;
import storage.Storage;

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

    protected ExecutionResult handleAdd(Command cmd) {
        assert cmd.getInstruction().getType() == Instruction.Type.ADD;

        Task taskToAdd = this.createTask(cmd);
        this.getTaskCollection().add(taskToAdd);

        return this.handleDisplay(cmd);
    }

    protected ExecutionResult handleEdit(Command cmd) {
        assert cmd.getInstruction().getType() == Instruction.Type.EDIT;

        int id = cmd.getInstruction().getIndex();
        Task updatedTask = this.createTask(cmd);
        updatedTask.setId(id);
        this.getTaskCollection().edit(id, updatedTask);

        return this.handleDisplay(cmd);
    }

    protected ExecutionResult handleDisplay(Command cmd) {
        assert cmd.getInstruction().getType() == Instruction.Type.DISPLAY;

        List<Task> listToDisplay = this.getTaskCollection().getAll();
        return new ExecutionResult(ViewType.TASK_LIST, listToDisplay);
    }

    protected ExecutionResult handleDelete(Command cmd) {
        assert cmd.getInstruction().getType() == Instruction.Type.DELETE;

        int id = cmd.getInstruction().getIndex();
        this.getTaskCollection().remove(id);

        return this.handleDisplay(cmd);
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

}
