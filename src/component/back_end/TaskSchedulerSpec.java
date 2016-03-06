package component.back_end;

import entity.Task;

import java.time.LocalDateTime;
import java.util.List;


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
public interface TaskSchedulerSpec {
    
    /**
     * @param existingTasks
     * @param durationOfNewTask
     * @return a date/time to slot in the new task
     */
    LocalDateTime findSlotForNewTask(List<Task> existingTasks, long durationOfNewTask);
}
