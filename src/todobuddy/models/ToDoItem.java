package todobuddy.models;

import java.io.Serializable;

/**
 * Represent a task added to the ToDoBuddy.
 * @author Huiyie
 *
 */

public class ToDoItem implements Comparable<ToDoItem>, Serializable {

    private static final long serialVersionUID = 1L;
    
    private String name;
    private int priority;

    /**
     * constructor
     * @param name
     */
    public ToDoItem(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    @Override
    public int compareTo(ToDoItem o) {
        if (o.getPriority() == this.getPriority()) {
            return 0;
        } else if (o.getPriority() > this.getPriority()) {
            return 1;
        } else {
            return -1;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof ToDoItem) {
            return ((ToDoItem) o).getName().equals(this.getName());
        } else {
            return false;
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}