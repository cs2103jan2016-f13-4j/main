package component.front_end;

import component.back_end.storage.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by maianhvu on 6/3/16.
 */
public class VisualIndexMapperTest {

    private List<Task> relationList_;
    private VisualIndexMapper mapper_;

    @Before
    public void setUp() {
        this.relationList_ = new ArrayList<>();
        this.relationList_.add(new Task(54, "Task 54", null, null, null));
        this.relationList_.add(new Task(55, "Task 55", null, null, null));
        this.relationList_.add(new Task(56, "Task 56", null, null, null));

        this.mapper_ = new VisualIndexMapper(this.relationList_);
    }

    @Test
    public void Visual_index_mapper_translates_from_raw_to_visual_correctly() {
        for (int i = 1; i <= 3; i++) {
            Integer relationKey = (53+i);
            assertThat(this.mapper_.translateRawToVisual(relationKey), is(i));
        }
    }

    @Test
    public void Visual_index_mapper_translates_from_visual_to_raw_correctly() {
        for (int i = 1; i <= 3; i++) {
            Integer relationKey = this.mapper_.translateVisualToRaw(i);
            assertThat(relationKey, is(equalTo(53 + i)));
        }
    }

    @Test
    public void Visual_index_mapper_gives_correct_visual_id() {
        List<VisualTuple<? extends RelationInterface>> visualTuples = this.mapper_.getVisualTupleList();
        for (int i = 1, j = 0; i <= 3; i++, j++) {
            assertThat(visualTuples.get(j).getIndex(), is(i));
        }
    }
}
