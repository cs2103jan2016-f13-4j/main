package front_end.ui;

import back_end.storage.relations.Task;
import front_end.ui.base.UserInterface;

import java.util.List;

/**
 * Created by maianhvu on 5/3/16.
 */
public class DisplayTaskListUI extends UserInterface<List<Task>> {

    public DisplayTaskListUI(List<Task> taskList) {
        super(taskList);
    }

    @Override
    public void render() {

    }

    @Override
    public boolean hasTaskList() {
        return true;
    }

}
