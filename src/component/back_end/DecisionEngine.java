package component.back_end;

import component.back_end.storage.*;
import entity.*;


public class DecisionEngine extends DecisionEngineSpec { 
    protected DataStoreSpec dataStore_ = null;
    protected TaskSchedulerSpec taskScheduler_ = null;
    
    public DecisionEngine() {
        this.dataStore_ = new DataStore();
    }

    @Override
    public ExecutionResult<?> performCommand(Command cmd) {
        
        switch (cmd.getType()) {
            case ADD:
                // TODO
                break;
            case EDIT:
                // TODO
                break;
            case DISPLAY_ALL:
                // TODO
                break;
            case DISPLAY_ONE:
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
    protected TaskSchedulerSpec getTaskScheduler() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected DataStoreSpec getDataStore() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
