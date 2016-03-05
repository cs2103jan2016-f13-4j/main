package front_end.ui.base;

import back_end.storage.relations.Task;
import factories.TaskList;
import front_end.ui.base.VisualIdTranslator;
import front_end.ui.base.VisualTuple;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by maianhvu on 5/3/16.
 */
public class VisualIdTranslatorTest {

    private List<Task> taskList_;
    private VisualIdTranslator taskIdMap_;

    @Before
    public void setUp() {
        this.taskList_ = TaskList.buildRandom(10);
        this.taskIdMap_ = new VisualIdTranslator(this.taskList_);
    }

    @Test
    public void TaskIdMap_populates_all_tasks() {
        assertThat(this.taskIdMap_.getIndexList().size(), is(this.taskList_.size()));
    }

    @Test
    public void TaskIdMap_correctly_converts_visual_into_raw() {
        for (int i = 1, j = 0; i <= taskList_.size(); i++, j++) {
            assertThat(this.taskIdMap_.translateVisualToRaw(i),
                    is(this.taskList_.get(j).getPrimaryKey().getValue()));
        }
    }

    @Test
    public void TaskIdMap_correctly_converts_raw_to_visual() {
        int currentVisualIndex = 1;
        for (Task task : this.taskList_) {
            assertThat(this.taskIdMap_.translateRawToVisual(task.getPrimaryKey()),
                    is(currentVisualIndex));
            currentVisualIndex++;
        }
    }

    @Test
    public void TaskIdMap_has_correct_idTranslationMap() {
        List<VisualTuple<Task>> idTranslationMap = this.taskIdMap_.getVisualTupleList();
        int currentVisualIndex = 1;
        for (VisualTuple<Task> tuple : idTranslationMap) {
            assertThat(tuple.getIndex(), is(currentVisualIndex));
            currentVisualIndex++;
        }
    }
}
