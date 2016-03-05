package stubs;

import back_end.DecisionEngine;
import back_end.storage.Storage;
import back_end.storage.relations.Task;
import front_end.ui.DisplayTaskListUI;
import objects.Command;
import objects.ExecutionResult;
import objects.Message;

import java.util.List;

/**
 * Created by maianhvu on 5/3/16.
 */
public class StubbedDecisionEngine extends DecisionEngine {

    public StubbedDecisionEngine() {
        super();
        try {
            this.dataStorage_.save(new Task("Task 1"));
            this.dataStorage_.save(new Task("Task 2"));
            this.dataStorage_.save(new Task("Task 3"));
            this.dataStorage_.save(new Task("Task 4"));
        } catch (Storage.RelationNotFoundException e) {
            e.printStackTrace();
        } catch (Storage.PrimaryKeyMissingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ExecutionResult performCommand(Command command) {

        switch (command.getType()) {
            case DISPLAY_ALL:
                try {
                    List<Task> tasks = this.dataStorage_.getAll(Task.class);
                    ExecutionResult<List<Task>> result = new ExecutionResult<>(DisplayTaskListUI.class, tasks);
                    result.addMessage(new Message(
                            Message.Type.INFO,
                            String.format("Found %d tasks", tasks.size())
                    ));

                    return result;
                } catch (Storage.RelationNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case EXIT:
                return ExecutionResult.nullResult();
        }
        return null;
    }
}
