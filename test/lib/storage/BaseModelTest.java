package lib.storage;

import org.junit.Test;
import storage.BaseInstance;
import storage.BaseModel;
import storage.Field;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by maianhvu on 25/2/16.
 */
public class BaseModelTest {

    public static class TestInstance extends BaseInstance {

        public TestInstance(BaseModel parent) {
            super(parent);
        }
    }

    public static class TestModel extends BaseModel {

        public TestModel(Class<? extends BaseInstance> instanceClass) {
            super(instanceClass);
        }

        @Override
        public String serialize() {
            return null;
        }
    }

    @Test
    public void Model_objects_can_define_properties() {
        TestModel model = new TestModel(TestInstance.class);
        model.defineField("id", Field.Type.INTEGER);
        model.defineField("name", Field.Type.STRING);
        assertThat(model.getFields().size(), is(2));
    }

    @Test
    public void Model_returns_correct_field_when_queried_through_name() {
        TestModel model = new TestModel(TestInstance.class);
        model.defineField("age", Field.Type.INTEGER);

        Field field = model.getFieldByName("age");
        assertThat(field, not(is(nullValue())));
    }

    @Test
    public void Model_instances_can_hold_values() throws Exception {
        TestModel model = new TestModel(TestInstance.class);
        model.defineField("id", Field.Type.INTEGER);
        model.defineField("name", Field.Type.STRING);

        TestInstance instance = model.create();
        instance.setValue("id", 5);

        Integer id = instance.getValue("id");
        assertThat(id, is(5)) ;
    }
}
