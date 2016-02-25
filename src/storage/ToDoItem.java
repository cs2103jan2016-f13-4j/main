package storage;

import java.io.Serializable;

/**
 * Represent a task added to the ToDoBuddy.
 *
 * @author Huiyie
 */

public class ToDoItem implements Comparable<ToDoItem>, Serializable {

    /**
     * Constants
     */
    private static final long serialVersionUID = 1L;

    /**
     * Properties
     */
    private String name_;
    private int priority_;

    /**
     * Constructs a to-do item with the specified name and priority.
     *
     * @param name a string
     * @param priority a number
     */
    public ToDoItem(String name, int priority) {
        this.name_ = name;
        this.priority_ = priority;
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
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof ToDoItem)) return false;

        ToDoItem another = (ToDoItem) o;
        if (!this.name_.equals(another.name_)) return false;
        return this.priority_ == another.priority_;
    }

    public String getName() {
        return this.name_;
    }

    public void setName(String name) {
        this.name_ = name;
    }

    public int getPriority() {
        return this.priority_;
    }

    public void setPriority(int priority) {
        this.priority_ = priority;
    }
}