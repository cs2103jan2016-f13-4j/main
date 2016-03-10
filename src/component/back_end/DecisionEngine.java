package component.back_end;

import component.back_end.storage.*;
import component.front_end.*;
import component.front_end.ui.TaskListView;
import entity.*;
import entity.command.*;

import java.time.LocalDateTime;
import java.util.*;


/**
 * 
 * created by thenaesh on Mar 8, 2016
 *
 */
public class DecisionEngine extends DecisionEngineSpec { 
    protected TaskCollection taskData_;
    protected TaskSchedulerSpec taskScheduler_;
    
    public DecisionEngine() {
        this.taskData_ = new TaskCollection();
    }

    
    protected List<Task> fullMatcher(String name, LocalDateTime start, LocalDateTime end) {
        TaskDescriptor desc = new SingleMatchDescriptor(name, start, end);
        return this.getTaskCollection().getAll(desc);
    }
    
    /**
     * creates a Task from a specified command object when it makes sense
     * we should blow up when creating a Task doesn't really make sense
     * @param cmd
     * @return
     */
    protected Task createTask(Command cmd) {
        ParameterList params = cmd.getParameters();
        
        
        // extract all the essential information out of the command
        // the asserts ensure that we blow up if any error was made
        // during the creation of the Command object in the Command Parser
        ParameterValue nameRaw = params.getParameter(ParameterName.NAME);
        assert (nameRaw.getValue() instanceof String);
        String name = (String) nameRaw.getValue();
        
        ParameterValue fromRaw = params.getParameter(ParameterName.DATE_FROM);
        assert (fromRaw.getValue() instanceof LocalDateTime);
        LocalDateTime from = (LocalDateTime) fromRaw.getValue();
        
        ParameterValue toRaw = params.getParameter(ParameterName.DATE_TO);
        assert (toRaw.getValue() instanceof LocalDateTime);
        LocalDateTime to = (LocalDateTime) toRaw.getValue();
        
        
        // we now build the Task object for adding into the store
        Task task = new Task(null, name, "", from, to);
        
        return task;
    }
    
    protected ExecutionResult<?> handleAdd(Command cmd) {
        Task taskToAdd = this.createTask(cmd);
        this.getTaskCollection().save(taskToAdd);
        
        return this.handleDisplay(cmd);
    }
    
    protected ExecutionResult<?> handleEdit(Command cmd) {
        int id = cmd.getInstruction().getIndex();
        Task updatedTask = this.createTask(cmd);
        updatedTask.setId(id);
        this.getTaskCollection().save(updatedTask);
        
        return this.handleDisplay(cmd);
    }
    
    protected ExecutionResult<?> handleDisplay(Command cmd) {
        List<Task> listToDisplay = this.getTaskCollection().getAll(UniversalDescriptor.get());
        return new ExecutionResult<>(TaskListView.class, listToDisplay);
    }
    
    protected ExecutionResult<?> handleDelete(Command cmd) {
        int id = cmd.getInstruction().getIndex();
        this.getTaskCollection().remove(id);
        
        return this.handleDisplay(cmd);
    }
    
    
    @Override
    public ExecutionResult<?> performCommand(Command cmd) {
        
        // this sort of nonsense should have been handled in the front end
        assert (cmd.getInstruction().getType() != Instruction.Type.UNRECOGNISED);
        
        // handle exit command here, without creating a task unnecessarily
        if (cmd.getInstruction().getType() == Instruction.Type.EXIT) {
            return ExecutionResult.getNullResult();
        }
        
        
        ExecutionResult<?> result = null;
        
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
            default:
                // if we reach this point, LTA Command Parser has failed in his duty
                // and awaits court martial
                assert false;
        }
        
        return result;
    }
    

    @Override
    protected TaskSchedulerSpec getTaskScheduler() {
        return this.taskScheduler_;
    }

    @Override
    protected TaskCollection getTaskCollection() {
        return this.taskData_;
    }
    
}
