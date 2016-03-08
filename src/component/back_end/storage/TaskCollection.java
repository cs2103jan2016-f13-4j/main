package component.back_end.storage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeMap;

import exception.back_end.PrimaryKeyNotFoundException;

/**
 * Created by maianhvu on 7/3/16.
 */
public class TaskCollection {
    
    private final boolean SEARCH_MAP_BY_DATETIME_INCLUSIVE = true;

    private TreeMap<Integer, Task> taskData_;
    private TreeMap<LocalDateTime, List<Task>> startTimeTree_;
    private TreeMap<LocalDateTime, List<Task>> endTimeTree_;

    public TaskCollection() {
        this.taskData_ = new TreeMap<>();
        this.startTimeTree_ = new TreeMap<>();
        this.endTimeTree_ = new TreeMap<>();
    }

    public int save(Task task) {
        // TODO: Check for potential time clashes

        if (task.getId() == null) {
            // TODO: Extract magic constant
            int newIndex = 1;

            if (!this.taskData_.isEmpty()) {
                // TODO: Extract magic constant
                newIndex = this.taskData_.lastKey() + 1;
            }

            task.setId(newIndex);
        }

        // add Task entry to the tree that maps ID to Task
        this.taskData_.put(task.getId(), task);
        
        // add Task entry to the tree that maps start time to Task
        // do not add any Task with null start time to this tree
        if (task.getStartTime() != null) {
            if (!this.startTimeTree_.containsKey(task.getStartTime())) {
                // if key yet to exist, put it into the tree and create a list
                this.startTimeTree_.put(task.getStartTime(), new ArrayList<Task>());
            }
            // append Task to end of list
            this.startTimeTree_.get(task.getStartTime()).add(task);
        }
        
        // add Task entry to the tree that maps end time to Task
        // do not add any Task with null end time to this tree
        if (task.getEndTime() != null) {
            if (!this.endTimeTree_.containsKey(task.getEndTime())) {
                // if key yet to exist, put it into the tree and create a list
                this.endTimeTree_.put(task.getEndTime(), new ArrayList<Task>());
            }
            // append Task to end of list
            this.endTimeTree_.get(task.getEndTime()).add(task);
        }
        
        return task.getId();
    }

    public Task get(int index) throws PrimaryKeyNotFoundException {
        // check if TreeMap contains the key that is queried
        if (!this.taskData_.containsKey(index)) {
            throw new PrimaryKeyNotFoundException(index);
        }
        // key exists, retrieve Task corresponding to key
        return this.taskData_.get(index);
    }
    
    /**
     * Returns a filtered list of Tasks that match the specified TaskDescriptor
     * Returns the full (unfiltered) list of Tasks when no TaskDescriptor is specified 
     * 
     * @param taskDescriptor
     * 
     * @return results which is a list of filtered Tasks that matches TaskDescriptor if one is specified,
     * else results is the full lis of Tasks stored in TreeMap
     * 
     */
    public List<Task> getAll(TaskDescriptor taskDescriptor) {
        ArrayList<Task> results = new ArrayList<>(this.taskData_.values());
        
        // when no task descriptor is specified, taskDescriptor is null
        if (taskDescriptor == null) {
            return results;
        }
        
        Iterator<Task> it = results.iterator();
        while (it.hasNext()) {
            Task task = it.next();
            if (!taskDescriptor.matches(task)) {
                // remove Task that does not match TaskDescriptor
                it.remove();
            }
        }
        
        // return filtered results as a List
        return results;
    }

    public Task remove(int id) {
        // TODO: Check if ID does not exist

        return this.taskData_.remove(id);
    }
    
    public List<Task> startBefore(LocalDateTime dateTime) {
        ArrayList<Task> resultList = new ArrayList<Task>();
        NavigableMap<LocalDateTime, List<Task>> resultsMap = this.startTimeTree_.headMap(dateTime, SEARCH_MAP_BY_DATETIME_INCLUSIVE);
        NavigableSet<LocalDateTime> resultsKeySet = resultsMap.navigableKeySet();
        
        Iterator<LocalDateTime> it = resultsKeySet.iterator();
        while (it.hasNext()) {
            LocalDateTime key = it.next();
            List<Task> values = resultsMap.get(key);
            resultList.addAll(values);
        }
        
        return resultList;
    }
}
