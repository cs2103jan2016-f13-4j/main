package storage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import exception.ExceptionHandler;
import exception.PrimaryKeyNotFoundException;
import shared.Command;
import shared.Task;
import skeleton.CollectionSpec;

/**
 * @@author Chng Hui Yie
 */
public class Storage implements CollectionSpec<Task> {

    /**
     * Constants
     */
    public static final int INDEX_TASK_INITIAL = 1;

    /**
     * Singleton Implementation
     */
    private static Storage instance = new Storage();
    public static Storage getInstance() {
        return instance;
    }

    /**
     * Properties
     */
    private TreeMap<Integer, Task> _taskData;

    /**
     * Constructs a new Storage instance.
     */
    private Storage() {
        // Instantiates storage
        this._taskData = new TreeMap<>();

        // this.readFromDisk();
    }

    // ----------------------------------------------------------------------------------------
    //
    // I. Save Method
    //
    // ----------------------------------------------------------------------------------------

    /**
     * Saves a Task object to TreeMap
     * 
     * @param task
     *            the Task to put into TreeMap
     * @return the ID of the added Task
     */
    @Override public int save(Task task) {
        // TODO: Check for potential time clashes
        boolean isNewTask = (task.getId() == null);
        boolean isDeleted = task.isDeleted();

        // Find a new ID for tasks that does not exist inside the storage
        if (isNewTask) {
            int newIndex = INDEX_TASK_INITIAL;

            if (!this._taskData.isEmpty()) {
                // Use last index increment
                newIndex = this._taskData.lastKey() + 1;
            }

            task.setId(newIndex);
        }

        // Put the task
        this._taskData.put(task.getId(), task);

        return task.getId();
    }

    public void writeToDisk() {
        List<Task> allTask = this.getAll();

        // Keep internal index serial
        List<String> tasksToWrite = IntStream.range(0, allTask.size()).mapToObj(index -> {
            Task task = allTask.get(index);
            task.setId(index + 1);
            return task;
        }).map(Task::encodeTaskToString).collect(Collectors.toList());

        this.getDiskIO().write(tasksToWrite);
    }

    // ----------------------------------------------------------------------------------------
    //
    // II. Get Method
    //
    // ----------------------------------------------------------------------------------------

    /**
     * Returns the Task to which the specified index is mapped
     * 
     * @param index
     *            the index whose associated Task is to be returned
     * @return the Task to which the specified index is mapped
     */
    @Override public Task get(int index) {
        // check if TreeMap contains the key that is queried
        // check if the Task entry has already been deleted
        if (!this._taskData.containsKey(index) || this._taskData.get(index).isDeleted()) {
            try {
                throw new PrimaryKeyNotFoundException(index);
            } catch (PrimaryKeyNotFoundException e) {
                ExceptionHandler.handle(e);
            }
            return null;
        }

        // key exists, retrieve Task corresponding to key
        return this._taskData.get(index);
    }

    // ----------------------------------------------------------------------------------------
    //
    // III. GetAll Method
    //
    // ----------------------------------------------------------------------------------------

    /**
     * Returns a filtered list of Tasks that match the specified TaskDescriptor
     * Returns the full (unfiltered) list of Tasks when no TaskDescriptor is
     * specified
     * 
     * @return results which is a list of filtered Tasks that matches
     *         TaskDescriptor if one is specified, else results is the full list
     *         of Tasks stored in TreeMap
     */
    @Override public List<Task> getAll() {
        return this._taskData.values().stream()
                .filter(task -> !task.isDeleted())
                .collect(Collectors.toList());
    }

    // ----------------------------------------------------------------------------------------
    //
    // IV. Remove Method
    //
    // ----------------------------------------------------------------------------------------
    /**
     * Removes the mapping for this index from TreeMap if present
     *
     * @param id
     *            index for which mapping should be removed
     * @return the previous Task associated with id, or null if there was no
     *         mapping for id
     */
    @Override public Task remove(int id) {
        if (!this._taskData.containsKey(id)) {
            try {
                throw new PrimaryKeyNotFoundException(id);
            } catch (PrimaryKeyNotFoundException e) {
                // TODO Auto-generated catch block
                ExceptionHandler.handle(e);
            }
            return null;
        }
        this._taskData.get(id).setDeletedStatus(true);
        return this._taskData.get(id);
    }

    /**
     * Clears all the Tasks from storage.
     */
    public void removeAll() {
        this._taskData.clear();
    }

    // ----------------------------------------------------------------------------------------
    //
    // V. Search by Time Range Methods
    //
    // ----------------------------------------------------------------------------------------

    /**
     * Search for all Tasks that has startTime before or at the same time as
     * dateTime
     * 
     *            high endpoint (inclusive) of the Tasks in the returned list
     * @return a list of Tasks that starts before or at the same time as
     *         dateTime
     */
    public List<Task> searchByDate(LocalDateTime start, LocalDateTime end) {
        // Search for all
        return this.getAll().stream()
                .filter(task -> {
                    // When time is null, we assume it's unbounded
                    boolean startSatisfies = start == null || task.getStartTime().compareTo(start) >= 0;
                    boolean endSatisfies = end == null || task.getEndTime().compareTo(end) <= 0;
                    return startSatisfies && endSatisfies;
                })
                .collect(Collectors.toList());
    }

    public DiskIO getDiskIO() {
        return DiskIO.getInstance();
    }

    // ----------------------------------------------------------------------------------------
    //
    // VI. Read From Disk Method
    //
    // ----------------------------------------------------------------------------------------

    public void readFromDisk() {
        readFromDisk(this.getDiskIO().read());
    }

    public void readFromDisk(List<String> taskStrings) {
        taskStrings.stream()
                .map(Task::decodeTaskFromString)
                .forEach(this::save);
    }
}
