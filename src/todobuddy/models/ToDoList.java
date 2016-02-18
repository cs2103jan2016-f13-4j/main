package todobuddy.models;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.PriorityQueue;

/**
 * A collection of ToDoItems.
 * @author Huiyie
 *
 */

public class ToDoList implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String MESSAGE_ERROR_TODOLIST_CLASS_NOT_FOUND = "ToDoList class not found.";
    
    private String listName;
    private PriorityQueue<ToDoItem> listQueue;

    /**
     * constructor
     * @param name
     */
    public ToDoList(String name) {
        this.listName = name;
        this.listQueue = new PriorityQueue<ToDoItem>();
    }
    
    public ToDoList() {
        this.listName = "";
        this.listQueue = new PriorityQueue<ToDoItem>();
    }
    
    public boolean addItemToList(ToDoItem item) {
        return this.listQueue.add(item);
    }
    
    /**
     * write ToDoList object to file
     */
    public void writeToFile() {
        try {
            FileOutputStream fos = new FileOutputStream("todobuddyData.csv");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    
    /**
     * read ToDoList object from file
     */
    public void readFromFile() {
        try {
            FileInputStream fis = new FileInputStream("todobuddyData.csv");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ToDoList tdList = (ToDoList) ois.readObject();
            fis.close();
            ois.close();
            overwriteListContent(tdList);
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println(MESSAGE_ERROR_TODOLIST_CLASS_NOT_FOUND);
            c.printStackTrace();
        }
    }

    private void overwriteListContent(ToDoList tdList) {
        ToDoItem item;
        while (!tdList.getListQueue().isEmpty()) {
            item = tdList.getListQueue().poll();
            this.listQueue.add(item);
        }
        this.setListName(tdList.getListName());
    }

    public String getListName() {
        return this.listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
    
    public PriorityQueue<ToDoItem> getListQueue() {
        return listQueue;
    }

    public void setListQueue(PriorityQueue<ToDoItem> listQueue) {
        this.listQueue = listQueue;
    }
    
}
