package front_end.ui;

import back_end.storage.relations.Task;
import front_end.VisualTuple;
import front_end.ui.base.UserInterface;
import front_end.ui.base.VisualIndexUI;

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

    }

}
