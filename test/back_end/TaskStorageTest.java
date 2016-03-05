package back_end;

import back_end.storage.base.PrimaryKey;
import back_end.storage.Storage;
import back_end.storage.relations.Task;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by maianhvu on 5/3/16.
 */
public class TaskStorageTest {

    private Storage dataStorage_;

    @Before
    public void setUp() {
        this.dataStorage_ = new Storage();
        this.dataStorage_.initializeStorageFor(Task.class);
    }

    @Test
    public void Data_storage_automatically_assigns_index_to_task() throws Exception {
        Task task = new Task("Test");
        assertThat(task.getPrimaryKey(), is(nullValue()));
        this.dataStorage_.save(task);
        assertThat(task.getPrimaryKey(), is(not(nullValue())));
    }

    @Test
    public void Data_storage_returns_records_primary_key_when_saved() throws Exception {
        Task task = new Task("Test");
        PrimaryKey key = this.dataStorage_.save(task);

        Task createdTask = this.dataStorage_.get(Task.class, key);
        assertThat(createdTask, is(equalTo(task)));
    }
}
