package component.back_end;

import java.time.LocalDateTime;
import java.util.List;

import entity.*;


/**
 * The Task Scheduler has one job: assist the Decision Engine with automatically finding a timeslot
 * to place a task of specified duration.
 * 
 * This is typically done when the user specifies a task without sufficient information to explicitly determine
 * a start and end time; in such situations, the task is considered floating and the Task Scheduler is called
 * upon to find a slot.
 * 
 * Rank: Lieutenant (reports to CPT Decision Engine)
 * 
 * created by thenaesh on Mar 5, 2016
 *
 */
public abstract class TaskSchedulerSpec {
    
    /**
     * @param existingTasks
     * @param durationOfNewTask
     * @return a date/time to slot in the new task
     */
    public abstract LocalDateTime findSlotForNewTask(List<Task> existingTasks, long durationOfNewTask);
}
