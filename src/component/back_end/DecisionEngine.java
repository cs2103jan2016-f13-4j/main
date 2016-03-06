package component.back_end;

import entity.*;


public class DecisionEngine implements DecisionEngineSpec {

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
    
    public void addTask(Task task) {
    }
    
    public void editTask(Task task) {
    }
    
    public void displayTasks(){
    }

}
