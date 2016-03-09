package component.back_end;

import component.back_end.storage.*;
import component.front_end.*;
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
        Task taskToAdd = new Task(null, name, "", from, to);
        
        return taskToAdd;
    }
    
    
    @Override
    public ExecutionResult<?> performCommand(Command cmd) {
        
        // this sort of nonsense should have been handled in the front end
        assert (cmd.getInstruction().getType() != Instruction.Type.UNRECOGNISED);
        
        // handle exit command here, without creating a task unnecessarily
        if (cmd.getInstruction().getType() == Instruction.Type.EXIT) {
            return ExecutionResult.getNullResult();
        }
        
        
        // all the standard commands
        // TODO: IMPLEMENT THESE!
        switch (cmd.getInstruction().getType()) {
            case ADD:
                // TODO
                break;
            case EDIT:
                // TODO
                break;
            case DISPLAY:
                // TODO
                break;
            case DELETE:
                // TODO
                break;
            default:
                // if we reach this point, LTA Command Parser has failed in his duty
                assert false;
        }
        
        return null;
    }
    

    @Override
    protected TaskSchedulerSpec getTaskScheduler() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected TaskCollection getTaskData() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
