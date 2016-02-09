package todobuddy;

import java.time.*;

/**
 * This task represents an abstraction of a task.
 * The controller works primarily with this abstraction.
 * The model and view must be capable of using this abstraction for communication with the controller (regarding tasks).
 * 
 * @author thenaesh
 *
 */
public class Task {
	protected String name; // this will be used as a search index, to prevent numbers and their associated complications
	protected Integer priority; // lower is better

	/*
	 * We use a boxed type because these values may not necessarily exist.
	 * For example, when a user enters task without a start time or end time,
	 *  a Task object is passed from view to controller without these values defined.
	 * We thus need a nullable type, and the boxed Integer type will do nicely.
	 */
	protected Instant startTime = null;
	protected Instant endTime = null;
	protected Duration duration = null;
	
	/*
	 * Below are handful of overloaded constructors that allow flexibility in specifying data used to construct the Task.
	 */
	
	/**
	 * This constructor leaves start and end times, as well as duration, undefined.
	 * The two required fields (name and priority) are defined as passed in the arguments.
	 * @param name
	 * @param priority
	 */
	public Task(String name, Integer priority) {
		this.name = name;
		this.priority = priority;
	}
	/**
	 * This constructor is called when start and end times are specified.
	 * Duration is computed from the start and end times.
	 * The two required fields (name and priority) are defined as passed in the arguments.
	 * @param name
	 * @param priority
	 * @param startTime
	 * @param endTime
	 */
	public Task(String name, Integer priority, Instant startTime, Instant endTime) {
		this(name, priority);
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = Duration.between(startTime, endTime);
	}
	/**
	 * This constructor is called when the start time and duration are specified.
	 * End time is computed from the start time and duration.
	 * The two required fields (name and priority) are defined as passed in the arguments.
	 * @param name
	 * @param priority
	 * @param startTime
	 * @param duration
	 */
	public Task(String name, Integer priority, Instant startTime, Duration duration) {
		this(name, priority);
		this.startTime = startTime;
		this.endTime = startTime.plus(duration);
		this.duration = duration;
	}
	/**
	 * This constructor is called when the duration and end time are specified.
	 * Start time is computed from the end time and duration.
	 * This constructor is provided for completeness' sake, and may be awkward to use.
	 * Do not use unless with compelling reason.
	 * The two required fields (name and priority) are defined as passed in the arguments.
	 * @param name
	 * @param priority
	 * @param duration
	 * @param endTime
	 */
	public Task(String name, Integer priority, Duration duration, Instant endTime) {
		this(name, priority);
		this.startTime = endTime.minus(duration);
		this.endTime = endTime;
		this.duration = duration;
	}
}