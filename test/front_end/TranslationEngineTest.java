package front_end;

import back_end.storage.relations.Task;
import factories.TaskList;
import front_end.ui.DisplayOneTaskUI;
import front_end.ui.DisplayTaskListUI;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by maianhvu on 5/3/16.
 */
public class TranslationEngineTest {

    private TranslationEngine engine_;
    private View<List<Task>> taskListView_;

    @Before
    public void setUp() {
        this.taskListView_ = new View<>(DisplayTaskListUI.class, TaskList.buildRandom());
        this.engine_ = new TranslationEngine();
    }

    @Test
    public void Translation_engine_does_not_have_currentView_by_default() {
        assertThat(this.engine_.getCurrentView(), is(nullValue()));
    }

    @Test
    public void Translation_engine_does_not_have_taskIdMap_by_default() {
        assertThat(this.engine_.getTaskIdMap(), is(nullValue()));
    }

    @Test
    public void Translation_engine_has_currentView_upon_display() {
        this.engine_.display(taskListView_);
        assertThat(this.engine_.getCurrentView(), is(taskListView_));
    }

    @Test
    public void Translation_engine_does_not_have_taskIdMap_if_data_is_not_task_list() {
        View<Task> singleTaskView = new View<>(DisplayOneTaskUI.class, new Task("Task 1"));
        this.engine_.display(singleTaskView);

        assertThat(this.engine_.getTaskIdMap(), is(nullValue()));
    }

    @Test
    public void Translation_engine_has_taskIdMap_if_data_is_task_list() {
        this.engine_.display(taskListView_);
        assertThat(this.engine_.getTaskIdMap(), is(not(nullValue())));
    }

}
