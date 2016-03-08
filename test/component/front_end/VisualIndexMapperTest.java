package component.front_end;

import component.back_end.storage.PrimaryKey;
import component.back_end.storage.PrimaryKeyInterface;
import component.back_end.storage.RelationInterface;
import component.front_end.ui.core.VisualTuple;
import factories.TestPrimaryKey;
import factories.TestRelation;
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

    private List<RelationInterface> relationList_;
    private VisualIndexMapper mapper_;

    @Before
    public void setUp() {
        this.relationList_ = new ArrayList<>();
        this.relationList_.add(new TestRelation("relation 54"));
        this.relationList_.add(new TestRelation("relation 55"));
        this.relationList_.add(new TestRelation("relation 56"));

        this.mapper_ = new VisualIndexMapper(this.relationList_);
    }

    @Test
    public void Visual_index_mapper_translates_from_raw_to_visual_correctly() {
        for (int i = 1; i <= 3; i++) {
            PrimaryKeyInterface<String> relationKey = new PrimaryKey<>("relation " + (53 + i));
            assertThat(this.mapper_.translateRawToVisual(relationKey), is(i));
        }
    }

    @Test
    public void Visual_index_mapper_translates_from_visual_to_raw_correctly() {
        for (int i = 1; i <= 3; i++) {
            PrimaryKeyInterface<String> relationKey = this.mapper_.translateVisualToRaw(i);
            assertThat(relationKey.getValue(), is(equalTo("relation " + (53 + i))));
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
