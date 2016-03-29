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

    @Test public void Task_is_encoded_correctly() {
        Task task1 = new Task(1, "proposal", "client ABC", LocalDateTime.of(2016, 3, 6, 14, 30),
                LocalDateTime.of(2016, 3, 8, 14, 30));
        String taskString = task1.encodeTaskToString();
        String[] taskStringArr = taskString.split(", ");
        assertEquals("\"" + "1" + "\"", taskStringArr[0]);
        assertEquals("\"" + "proposal" + "\"", taskStringArr[1]);
        assertEquals("\"" + "client ABC" + "\"", taskStringArr[2]);
        assertEquals("\"" + LocalDateTime.of(2016, 3, 6, 14, 30).toString() + "\"", taskStringArr[3]);
        assertEquals("\"" + LocalDateTime.of(2016, 3, 8, 14, 30).toString() + "\"", taskStringArr[4]);
    }

    @Test public void Task_with_special_characters_still_encode_correctly() {
        String specialTaskName = "A task with comma, and \"quotes\", and \"comma, within quotes\"";
        specialTaskName += ", and backslash before quote\\\"";
        Task specialTask = new Task(123, specialTaskName, "Random description", LocalDateTime.of(2016, 3, 9, 23, 59),
                LocalDateTime.of(2016, 3, 11, 12, 00));
        String specialTaskString = specialTask.encodeTaskToString();

        String expected = "" + "\"123\", "
                + "\"A task with comma, and \"quotes\", and \"comma, within quotes\", and backslash before quote\\\"\", "
                + "\"Random description\", " + "\"2016-03-09T23:59\", \"2016-03-11T12:00\"";

        assertEquals(expected, specialTaskString);
    }

    @Test public void Task_with_special_characters_still_decode_correctly() {
        String specialString = "A task with comma, and \"quotes\", and \"comma, within quotes\", and backslash before quote\\\"";
        String taskString = "" + "\"123\", " + "\"" + specialString + "\", " + "\"Random description\", " + "\""
                + LocalDateTime.of(2016, 3, 10, 12, 00).toString() + "\", \""
                + LocalDateTime.of(2016, 3, 11, 22, 30).toString() + "\"";

        Task decodedTask = new Task(null, null, null, null, null);
        // decode the task string and check if the task attributes are equal to
        // what we expect
        decodedTask.decodeTaskFromString(taskString);

        assertSame(123, decodedTask.getId());
        assertEquals(specialString, decodedTask.getTaskName());
        assertEquals("Random description", decodedTask.getDescription());
        assertEquals(LocalDateTime.of(2016, 3, 10, 12, 00), decodedTask.getStartTime());
        assertEquals(LocalDateTime.of(2016, 3, 11, 22, 30), decodedTask.getEndTime());
    }

    @Test public void Decoded_Task_has_correct_attributes_assigned() {
        Task task4 = new Task(null, null, null, null, null);
        // String to parse into Task object
        String taskString = "\"88\", \"marketing pitch\", \"to microsoft\", \"2016-03-09t14:30:00\", \"2016-03-09t15:30:00\"";
        task4.decodeTaskFromString(taskString);

        assertSame(88, task4.getId());
        assertEquals("marketing pitch", task4.getTaskName());
        assertEquals("to microsoft", task4.getDescription());
        assertEquals(LocalDateTime.parse("2016-03-09t14:30:00"), task4.getStartTime());
        assertEquals(LocalDateTime.parse("2016-03-09t15:30:00"), task4.getEndTime());
    }

    @Test public void SetId_method_successfully_assign_ID_to_Task() {
        // create Task with null ID
        Task task5 = new Task(null, "proposal", "client XYZ", LocalDateTime.of(2016, 3, 1, 23, 59),
                LocalDateTime.of(2016, 3, 2, 1, 00));

        // assign an integer ID
        task5.setId(5);

        assertNotNull(task5.getId()); // check that ID is no longer null
        assertSame(5, task5.getId()); // check that ID equals
                                      // the new assigned
                                      // value

    }

}