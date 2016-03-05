package front_end.ui;

import back_end.storage.relations.Task;
import front_end.ui.core.VisualIndexUI;
import front_end.ui.base.VisualTuple;

import java.util.List;

/**
 * Created by maianhvu on 5/3/16.
 */
public class DisplayTaskListUI extends VisualIndexUI<Task> {

    public DisplayTaskListUI(List<Task> taskList) {
        super(taskList);
    }

    @Override
    public void render() {
        super.render();
        for (VisualTuple<Task> visualTask : this.getVisualTuples()) {
            Task task = visualTask.getOriginal();
            this.displayLine("%d. %s", visualTask.getIndex(), task.getTask());
        }
    }

}
