package storage;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test the serializing and deserializing function of ToDoList.
 *
 * @author Huiyie
 */

public class ToDoListTest {

    @Test
    public void ToDoList_should_have_name() {
        ToDoList tdl1 = new ToDoList("tdl1");
        assertNotNull(tdl1.getListName());
    }

    @Test
    public void ToDoList_object_from_read_and_write_should_have_the_same_name() {
        ToDoList tdl2 = new ToDoList("tdl2");
        tdl2.writeToFile();
        ToDoList tdl3 = new ToDoList();
        tdl3.readFromFile();
        assertEquals(tdl2.getListName(), tdl3.getListName());
    }

    @Test
    public void ToDoList_object_from_read_and_write_should_have_same_priority_queue_entries() {
        ToDoList tdl4 = new ToDoList("tdl4");
        tdl4.addItemToList(new ToDoItem("item1", 1));
        tdl4.addItemToList(new ToDoItem("item2", 2));
        tdl4.addItemToList(new ToDoItem("item3", 3));
        tdl4.writeToFile();
        ToDoList tdl5 = new ToDoList();
        tdl5.readFromFile();

        ToDoItem originalItem, copiedItem;
        while (!tdl4.getListQueue().isEmpty() && !tdl5.getListQueue().isEmpty()) {
            originalItem = tdl4.getListQueue().poll();
            copiedItem = tdl5.getListQueue().poll();
            assertEquals(originalItem, copiedItem);
        }
    }
}