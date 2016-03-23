package logic;

import shared.Command;
import shared.ExecutionResult;
import shared.ViewType;
import skeleton.CollectionSpec;
import skeleton.DecisionEngineSpec;
import skeleton.TaskSchedulerSpec;
import storage.Task;
import storage.TaskCollection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    /**
     * creates a Task from a specified command object when it makes sense
     * we should blow up when creating a Task doesn't really make sense
     * @param cmd
     * @return
     */
    protected Task createTask(Command command) {
        String taskName = command.getParameter(Command.ParamName.TASK_NAME);
        assert taskName != null; // Command validity should have already been handled by CommandParser

        // Description is optional
        String taskDescription = command.getParameter(Command.ParamName.TASK_DESCRIPTION);

        // Same for the dates
        LocalDateTime taskStart = command.getParameter(Command.ParamName.TASK_START);
        LocalDateTime taskEnd   = command.getParameter(Command.ParamName.TASK_END);

        return new Task(null, taskName, taskDescription, taskStart, taskEnd);
    }

    protected ExecutionResult handleAdd(Command command) {
        assert command.hasInstruction(Command.Instruction.ADD);

        Task taskToAdd = this.createTask(command);
        this.getTaskCollection().add(taskToAdd);

        return this.handleDisplay(command);
    }

    protected ExecutionResult handleEdit(Command command) {
        assert command.hasInstruction(Command.Instruction.EDIT);

        Integer id = command.getIndex();
        assert id != null; // This should already be handled at Parser

        Task updatedTask = this.createTask(command);
        updatedTask.setId(id);
        this.getTaskCollection().edit(id, updatedTask);

        return this.handleDisplay(command);
    }

    protected ExecutionResult handleDisplay(Command command) {
        assert command.hasInstruction(Command.Instruction.DISPLAY);

        List<Task> listToDisplay = this.getTaskCollection().getAll();
        return new ExecutionResult(ViewType.TASK_LIST, listToDisplay);
    }

    protected ExecutionResult handleDelete(Command command) {
        assert command.hasInstruction(Command.Instruction.DELETE);

        Integer id = command.getIndex();
        assert id != null;

        this.getTaskCollection().remove(id);
        return this.handleDisplay(command);
    }

    protected ExecutionResult handleSearch(Command command) {
        assert command.hasInstruction(Command.Instruction.SEARCH);

        // PowerSearching!
        String query = command.getParameter(Command.ParamName.SEARCH_QUERY);
        String[] words = query.split("\\s+");
        StringBuilder patternBuilder = new StringBuilder();
        patternBuilder.append("\\b(?:");

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

        patternBuilder.append(")\\b");
        Pattern pattern = Pattern.compile(patternBuilder.toString());

        List<Task> foundTask = this.getTaskCollection().getAll()
                .stream()
                .filter(item -> {
                    Matcher m = pattern.matcher(item.getTaskName());
                    if (m.find()) return true;

                    if (item.getDescription() == null) return false;
                    m = pattern.matcher(item.getDescription());
                    return m.find();
                })
                .collect(Collectors.toList());

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
                result = this.handleAdd(command);
                break;
            case EDIT:
                result = this.handleEdit(command);
                break;
            case DISPLAY:
                result = this.handleDisplay(command);
                break;
            case DELETE:
                result = this.handleDelete(command);
                break;
            case SEARCH:
                result = this.handleSearch(command);
                break;
            default:
                // if we reach this point, LTA Command Parser has failed in his duty
                // and awaits court martial
                assert false;
        }

        return result;
    }


    @Override
    public TaskSchedulerSpec getTaskScheduler() {
        return TaskScheduler.getInstance();
    }

    @Override
    public void shutdown() {
        // TODO: stub
    }

    @Override
    public CollectionSpec<Task> getTaskCollection() {
        return TaskCollection.getInstance();
    }

}
