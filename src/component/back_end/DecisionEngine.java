package component.back_end;

import component.back_end.storage.*;
import entity.*;
import entity.command.Command;


public class DecisionEngine extends DecisionEngineSpec { 
    protected TaskCollection taskData_;
    protected TaskSchedulerSpec taskScheduler_;
    
    public DecisionEngine() {
        this.taskData_ = new TaskCollection();
    }

    @Override
    public Task createTask(Command command) {
        
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
            case EXIT:
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
