package front_end;

import back_end.storage.relations.Task;

import java.util.List;

/**
 * Created by maianhvu on 5/3/16.
 */
public class TranslationEngine {

    private CommandParser commandParser_;
    private TaskIdMap taskIdMap_;
    private View currentView_;

    public TranslationEngine() {
        this.commandParser_ = new CommandParser();
    }

    public void display(View view) {
        this.currentView_ = view;

        // If data is a list, we provide it with the
        // task ID mapping
        if (this.currentView_.getUI().hasTaskList()) {
            assert(this.currentView_.getData() instanceof List);
            this.taskIdMap_ = new TaskIdMap((List<Task>) this.currentView_.getData());
        }
    }

    // Package level methods, exposed only for testing purposes
    View getCurrentView() {
        return this.currentView_;
    }

    TaskIdMap getTaskIdMap() {
        return this.taskIdMap_;
    }

}
