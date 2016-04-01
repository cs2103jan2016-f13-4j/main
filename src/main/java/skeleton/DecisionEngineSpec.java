package skeleton;

import shared.Command;
import shared.ExecutionResult;
import shared.Task;

/**
 * @@author Thenaesh Elango
 */
public interface DecisionEngineSpec {

    void initialise();

    CollectionSpec<Task> getTaskCollection();

    ExecutionResult performCommand(Command cmd);

    SchedulerSpec getTaskScheduler();

    void shutdown();

}
