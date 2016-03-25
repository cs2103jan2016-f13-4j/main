package storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.time.LocalDateTime;

import org.junit.Test;
import shared.Task;

/**
 * 
 * @@author Chng Hui Yie
 *
 */

public class TaskTest {

    private final Integer TASK_ID = 1;
    private final String TASK_NAME = "homework";
    private final String TASK_DESCRIPTION = "cs2103t";
    private final LocalDateTime TASK_START = LocalDateTime.of(2016, 3, 6, 14, 30);
    private final LocalDateTime TASK_END = LocalDateTime.of(2016, 3, 8, 14, 30);
    private Task task_;

    @Test public void Task_is_encoded_correctly() {
        this.task_ = new Task(this.TASK_ID, this.TASK_NAME, this.TASK_DESCRIPTION, this.TASK_START, this.TASK_END);
        String taskString = this.task_.encodeTaskToString();
        String[] taskStringArr = taskString.split(", ");
        assertEquals("\"" + this.TASK_ID.toString() + "\"", taskStringArr[0]);
        assertEquals("\"" + this.TASK_NAME + "\"", taskStringArr[1]);
        assertEquals("\"" + this.TASK_DESCRIPTION + "\"", taskStringArr[2]);
        assertEquals("\"" + this.TASK_START.toString() + "\"", taskStringArr[3]);
        assertEquals("\"" + this.TASK_END.toString() + "\"", taskStringArr[4]);
    }

    @Test public void Task_with_special_characters_still_encode_correctly() {
        String specialTaskName = "A task with comma, and \"quotes\", and \"comma, within quotes\"";
        specialTaskName += ", and backslash before quote\\\"";
        Task specialTask = new Task(123, specialTaskName, "Random description", TASK_START, TASK_END);
        String specialTaskString = specialTask.encodeTaskToString();

        String expected = "" + "\"123\", "
                + "\"A task with comma, and \"quotes\", and \"comma, within quotes\", and backslash before quote\\\"\", "
                + "\"Random description\", " + "\"2016-03-06T14:30\", \"2016-03-08T14:30\"";

        assertEquals(expected, specialTaskString);
    }

    @Test public void Task_with_special_characters_still_decode_correctly() {
        String specialString = "A task with comma, and \"quotes\", and \"comma, within quotes\", and backslash before quote\\\"";
        String taskString = "" + "\"123\", " + "\"" + specialString + "\", " + "\"Random description\", " + "\""
                + this.TASK_START.toString() + "\", \"" + this.TASK_END.toString() + "\"";

        Task decodedTask = new Task(null, null, null, null, null);
        // decode the task string and check if the task attributes are equal to
        // what we expect
        decodedTask.decodeTaskFromString(taskString);

        assertSame(123, decodedTask.getId());
        assertEquals(specialString, decodedTask.getTaskName());
        assertEquals("Random description", decodedTask.getDescription());
        assertEquals(this.TASK_START, decodedTask.getStartTime());
        assertEquals(this.TASK_END, decodedTask.getEndTime());
    }

    @Test public void Decoded_Task_has_correct_attributes_assigned() {
        this.task_ = new Task(null, null, null, null, null);
        // String to parse into Task object
        String taskString = "\"88\", \"marketing pitch\", \"to microsoft\", \"2016-03-09t14:30:00\", \"2016-03-09t15:30:00\"";
        this.task_.decodeTaskFromString(taskString);

        assertSame(88, this.task_.getId());
        assertEquals("marketing pitch", this.task_.getTaskName());
        assertEquals("to microsoft", this.task_.getDescription());
        assertEquals(LocalDateTime.parse("2016-03-09t14:30:00"), this.task_.getStartTime());
        assertEquals(LocalDateTime.parse("2016-03-09t15:30:00"), this.task_.getEndTime());
    }

    @Test public void SetId_method_successfully_assign_ID_to_Task() {
        // create Task with null ID
        this.task_ = new Task(null, this.TASK_NAME, this.TASK_DESCRIPTION, this.TASK_START, this.TASK_END);

        // assign an integer ID
        this.task_.setId(this.TASK_ID);

        assertNotNull(this.task_.getId()); // check that ID is no longer null
        assertEquals(this.TASK_ID, this.task_.getId()); // check that ID equals
                                                        // the new assigned
                                                        // value

    }

}