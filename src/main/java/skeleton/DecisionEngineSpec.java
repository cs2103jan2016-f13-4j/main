package skeleton;

import logic.ExecutionResult;
import shared.Command;
import storage.Task;

public interface DecisionEngineSpec {

    void initialise();

    CollectionSpec<Task> getTaskCollection();

    ExecutionResult performCommand(Command cmd);

    SchedulerSpec getTaskScheduler();

}
