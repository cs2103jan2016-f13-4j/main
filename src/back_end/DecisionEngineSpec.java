package back_end;

import objects.*;


/**
 * The Decision Engine has the duty of receiving a command from the Dispatcher and deciding how to act upon it.
 * It has knowledge of the Data Store and gives it instructions on what to do with its data.
 * 
 * Should the Decision Engine decide that a command cannot be directly fulfilled (e.g. adding a task
 * into an already occupied slot), it will do one of two things:
 *      1) Report an error (by raising an exception) back to the Dispatcher
 *      2) Perform other actions (conservatively) to allow the command to be eventually fulfilled
 * 
 * Rank: Captain (reports to MAJ Dispatcher)
 * 
 * created by thenaesh on Mar 5, 2016
 *
 */
public abstract class DecisionEngineSpec {
    public ExecutionResult performCommand(Command cmd) {
        return null;
    }
}
