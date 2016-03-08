package component.back_end;

import component.back_end.storage.*;
import entity.*;
import entity.command.Command;
import entity.command.Instruction;


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

    @Override
    public Task createRawTask(Command command) {
        
        switch (command.getInstruction().getType()) {
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
            case UNRECOGNISED:
                // TODO
                break;
            default:
                // if we reach this point, LTA Command Parser has failed in his duty
                assert false;
        }
        return null;
        
    }
    
    @Override
    public ExecutionResult<?> performCommand(Command cmd) {
        
        assert (cmd.getInstruction().getType() != Instruction.Type.UNRECOGNISED);
        // handle exit command here, without creating a task unnecessarily
        if (cmd.getInstruction().getType() == Instruction.Type.EXIT) {
            return ExecutionResult.getNullResult();
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
