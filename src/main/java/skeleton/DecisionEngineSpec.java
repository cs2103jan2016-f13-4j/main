package skeleton;

import shared.Command;
import shared.ExecutionResult;
import storage.Task;

public interface DecisionEngineSpec {

    void initialise();

    CollectionSpec<Task> getTaskCollection();

    ExecutionResult performCommand(Command cmd);

    TaskSchedulerSpec getTaskScheduler();

    void shutdown();

}
