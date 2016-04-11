package skeleton;

import shared.Command;
import shared.ExecutionResult;
import shared.Task;

/**
 * The Decision Engine receives a Command object from the Dispatcher. It then executes it and
 * returns the execution result back to the Dispatcher.
 * @@author Thenaesh Elango
 */
public interface DecisionEngineSpec {

    void initialise();

    ExecutionResult performCommand(Command cmd);

    void shutdown();

}
