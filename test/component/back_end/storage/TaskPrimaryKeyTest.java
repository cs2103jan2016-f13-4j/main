package component.back_end.storage;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Huiyie
 *
 */

public class TaskPrimaryKeyTest {

    private String taskName_;
    private LocalDateTime startDate_;
    
    @Before
    public void setUp() {
        taskName_ = "task name";
        startDate_ = LocalDateTime.of(2016, 3, 6, 14, 30);
    }
    
    @Test
    public void Identical_task_primary_keys_are_equal() {
        TaskNameAndStartTime pKey1 = new TaskNameAndStartTime(this.taskName_, this.startDate_);
        TaskNameAndStartTime pKey2 = new TaskNameAndStartTime(this.taskName_, this.startDate_);
        assertEquals(0, pKey1.compareTo(pKey2));
    }

    @Test
    public void Tasks_with_different_names_are_not_equal() {
        String name = "different";
        TaskNameAndStartTime pKey3 = new TaskNameAndStartTime(this.taskName_, this.startDate_);
        TaskNameAndStartTime pKey4 = new TaskNameAndStartTime(name, this.startDate_);
        assertNotEquals(0, pKey3.compareTo(pKey4));
    }
    
    @Test
    public void Tasks_with_different_start_times_are_not_equal() {
        LocalDateTime start = LocalDateTime.of(2016, 3, 6, 14, 0);
        TaskNameAndStartTime pKey5 = new TaskNameAndStartTime(this.taskName_, this.startDate_);
        TaskNameAndStartTime pKey6 = new TaskNameAndStartTime(this.taskName_, start);
        assertNotEquals(0, pKey5.compareTo(pKey6));
    }
}
