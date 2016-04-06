package skeleton;

import shared.Command;
import shared.ExecutionResult;
import shared.Task;

/**
 * @@author Thenaesh Elango
 */
public interface DecisionEngineSpec {

    void initialise();

    StorageSpec<Task> getStorage();

    ExecutionResult performCommand(Command cmd);

    SchedulerSpec getTaskScheduler();

    void shutdown();

}
