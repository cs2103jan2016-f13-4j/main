package storage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Test;

import shared.CustomTime;
import shared.Task;

import static org.junit.Assert.*;

/**
 * 
 * @@author Chng Hui Yie
 *
 */

public class TaskTest {

    // ----------------------------------------------------------------------------------------
    //
    // I. Encoding To String Tests
    //
    // ----------------------------------------------------------------------------------------

    @Test public void Task_is_encoded_correctly() {
        CustomTime start = new CustomTime(LocalDateTime.of(2016, 3, 6, 14, 30));
        CustomTime end = new CustomTime(LocalDateTime.of(2016, 3, 8, 14, 30));
        Task task1 = new Task(1, "proposal", "client ABC", start, end);
        task1.setCompleted(true);
        task1.setPriority(Task.Priority.MEDIUM);
        String taskString = task1.encodeTaskToString();
        String[] taskStringArr = taskString.split(",");
        assertEquals(8, taskStringArr.length);
        assertEquals("1", taskStringArr[0]);
        assertEquals("proposal", taskStringArr[1]);
        assertEquals("client ABC", taskStringArr[2]);
        assertEquals(task1.getCreationTime().toString(), taskStringArr[3]);
        assertEquals(start.toString(), taskStringArr[4]);
        assertEquals(end.toString(), taskStringArr[5]);
        assertEquals("true", taskStringArr[6]); // isCompleted has been set as true
        assertEquals("2", taskStringArr[7]); // task priority medium has a value of 2
    }

    @Test public void Task_with_special_characters_still_encode_correctly() {
        CustomTime start = new CustomTime(LocalDateTime.of(2016, 3, 9, 23, 59));
        CustomTime end = new CustomTime(LocalDateTime.of(2016, 3, 11, 12, 00));
        String specialTaskName = "A task with comma, and \"quotes\", and \"comma, within quotes\"";
        specialTaskName += ", and backslash before quote\\\"";
        Task specialTask = new Task(123, specialTaskName, "Random description", start, end); // priority is set to default
        String creationTime = specialTask.getCreationTime().toString(); // get creationTime to add to expected String for checking
        String specialTaskString = specialTask.encodeTaskToString();

        String expected = "123,"
                + "\"A task with comma, and \\\"quotes\\\", and \\\"comma, within quotes\\\", and backslash before quote\\\\\\\"\",Random description," + creationTime + "," + start.toString() + "," + end.toString() + ",false,1";

        assertEquals(expected, specialTaskString);
    }

    // ----------------------------------------------------------------------------------------
    //
    // II. Decoding From String Tests
    //
    // ----------------------------------------------------------------------------------------

    @Test public void Task_with_special_characters_still_decode_correctly() {
        String specialString = "A task with comma, and \"quotes\", and \"comma, within quotes\", and backslash before quote\\\"";

        // Convert all backslashes to double backslashes
        // Convert all quotes to backslash quote
        // Convert all commas to backslash comma
        String encodedSpecialString = "A task with comma, and \\\"quotes\\\", and \\\"comma, within quotes\\\", and backslash before quote\\\\\\\"";

        CustomTime start = new CustomTime(LocalDateTime.of(2016, 3, 10, 12, 00));
        CustomTime end = new CustomTime(LocalDateTime.of(2016, 3, 11, 22, 30));

        String taskString = "123,\"" + encodedSpecialString + "\",Random description,"
                + LocalDateTime.of(2016, 3, 1, 23, 59).toString() + ","
                + start.toString() + ","
                + end.toString() + ",false,2";

        // decode the task string and check if the task attributes are equal to
        // what we expect
        Task task3 = Task.decodeTaskFromString(taskString);

        assertSame(123, task3.getId());
        assertEquals(specialString, task3.getTaskName());
        assertEquals("Random description", task3.getDescription());
        assertEquals(LocalDateTime.of(2016, 3, 1, 23, 59), task3.getCreationTime());
        assertEquals(start, task3.getStartTime());
        assertEquals(end, task3.getEndTime());
        assertFalse(task3.isCompleted());
        assertEquals(Task.Priority.MEDIUM, task3.getPriority());
    }

    @Test public void Decoded_Task_has_correct_attributes_assigned() {
        String taskString = "88,marketing pitch,to microsoft,2016-03-02T23:59:01,2016-03-09T14:30,2016-03-09T15:30,true,2";
        Task task4 = Task.decodeTaskFromString(taskString);

        assertSame(88, task4.getId());
        assertEquals("marketing pitch", task4.getTaskName());
        assertEquals("to microsoft", task4.getDescription());
        assertEquals(LocalDateTime.parse("2016-03-02T23:59:01"), task4.getCreationTime());
        assertEquals(CustomTime.fromString("2016-03-09T14:30"), task4.getStartTime());
        assertEquals(CustomTime.fromString("2016-03-09T15:30"), task4.getEndTime());
        assertTrue(task4.isCompleted());
        assertEquals(Task.Priority.MEDIUM, task4.getPriority());
    }

    // ----------------------------------------------------------------------------------------
    //
    // III. Set ID Test
    //
    // ----------------------------------------------------------------------------------------

    @Test public void SetId_method_successfully_assign_ID_to_Task() {
        // create Task with null ID
        Task task5 = new Task(null, "proposal", "client XYZ", new CustomTime(LocalDateTime.of(2016, 3, 1, 23, 59)),
                new CustomTime(LocalDateTime.of(2016, 3, 2, 1, 00)));

        // assign an integer ID
        task5.setId(5);

        assertNotNull(task5.getId()); // check that ID is no longer null
        assertSame(5, task5.getId()); // check that ID equals the new assigned
                                      // value

    }

}