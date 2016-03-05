package front_end;

import back_end.storage.relations.Task;
import factories.TaskList;
import front_end.ui.DisplayTaskListUI;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by maianhvu on 5/3/16.
 */
public class ViewTest {

    private View<List<Task>> taskListView_;

    @Before
    public void setUp() {
        this.taskListView_ = new View(
                DisplayTaskListUI.class,
                TaskList.buildGeneric());
    }

    @Test
    public void View_gets_successfully_constructed_from_class() {
        assertThat(this.taskListView_.getUI(), is(not(nullValue())));
    }

    @Test
    public void View_stores_the_data_under_UI_instance() {
        List<Task> viewTaskList = taskListView_.getData();
        List<Task> uiTaskList = taskListView_.getUI().getData();
        assertThat(viewTaskList, is(uiTaskList));
    }
}
