package logic;

import java.time.LocalTime;
import java.util.*;
import java.util.function.*;

import shared.*;
import skeleton.*;

/**
 * Represents an write operation on Storage by the Decision Engine, in response to a Command
 * Encapsulates both the operation and its inverse
 * Contains sufficient information to enable undo/redo of the operation
 *
 * There is a boolean field to denote whether an operation has been executed.
 * Undo/redo of an operation should not be carried out if the operation has not been executed.
 * This is enforced in undo/redo lambdas whenever the original operation may fail, and is reflected in their return values.
 *
 * WARNING: These objects should NOT persist between sessions. Undefined behaviour may result.
 *
 * @@author A0124772E
 */
public class StorageWriteOperation {

    private static final String ERROR_RANGE_EMPTY_FOR_DELETE = Message.DELETE_FAIL.toString();
    private static final String ERROR_RANGE_EMPTY_FOR_MARK = Message.MARK_FAIL.toString();
    private static final String WARNING_COLLIDING_TASK = "Task collides with another already existing task!";

    private Function<?, String> _initialOperation; // returns the error string for the operation, to be placed in an ExecutionResult
    private Function<?, Boolean> _undoOperation; // returns false if nothing was done due to original operation not being run
    private Function<?, Boolean> _redoOperation; // returns false if nothing was done due to original operation not being run

    private Command _command; // command that gave rise to this execution unit
    private Integer _id = null; // id of the task handled by this op, if used
    private int[] _idRange = null; // set of ids handled by this op, if used
    private Task _taskPreModification = null; // snapshot of the task before it is modified
    private Task _taskPostModification = null; // snapshot of the task after it is modified
    private boolean _wasExecuted = false; // success code for the operation

    private StorageSpec<Task> _storage; // the Storage instance used for the operation

    public StorageWriteOperation(Command command, StorageSpec storage) {
        this._command = command;
        this._storage = storage;

        switch (this._command.getInstruction()) {
            case ADD:
                this.createAsAddUnit();
                break;
            case DELETE:
                this.createAsDeleteUnit();
                break;
            case EDIT:
                this.createAsEditUnit();
                break;
            case MARK:
                this.createAsMarkUnit();
                break;
            default:
                assert false;
        }
    }


    // getters
    public Function<?, String> getInitialOperation() {
        return this._initialOperation;
    }

    public Function<?, Boolean> getUndoOperation() {
        return this._undoOperation;
    }

    public Function<?, Boolean> getRedoOperation() {
        return this._redoOperation;
    }

    public boolean isOperationExecuted() {
        return this._wasExecuted;
    }


    private void createAsAddUnit() {

        this._initialOperation = v -> {
            // initialise temp vars to store the new task properties
            String name = null;
            CustomTime from = null;
            CustomTime to = null;

            /*
             * for each command parameter, check if it was supplied
             * if so, extract the value and set the appropriate reference above to point to the extracted value
             */

            // extract name if it exists
            if (this._command.hasParameter(Command.ParamName.TASK_NAME)) {
                name = this._command.getParameter(Command.ParamName.TASK_NAME);
            }
            // extract start time if it exists
            if (this._command.hasParameter(Command.ParamName.TASK_START)) {
                from = this._command.getParameter(Command.ParamName.TASK_START);

                // if the start time is not defined, default to 0000
                if (!from.hasTime()) {
                    from = new CustomTime(from.getDate(), LocalTime.of(7, 0));
                }
            }
            // extract end time if it exists
            if (this._command.hasParameter(Command.ParamName.TASK_END)) {
                to = this._command.getParameter(Command.ParamName.TASK_END);

                // if the end time is not defined, default to 2359
                if (!to.hasTime()) {
                    to = new CustomTime(to.getDate(), LocalTime.of(23, 00));
                }
            }

            // ensure the task has a name, at the very least
            assert name != null;

            // we now build the Task object for adding into the store
            Task taskToAdd = new Task(null, name, "", from, to);

            // extract priority if it exists
            if (this._command.hasParameter(Command.ParamName.PRIORITY_VALUE)) {
                Task.Priority priority = this._command.getParameter(Command.ParamName.PRIORITY_VALUE);
                if (priority != null) {
                    taskToAdd.setPriority(priority);
                }
            }

            // check for collisions
            boolean taskCollides = Scheduler.getInstance().isColliding(taskToAdd);

            this._id = this._storage.save(taskToAdd);

            this._wasExecuted = true; // adding a task never fails

            if (taskCollides) {
                System.err.println(WARNING_COLLIDING_TASK);
                return WARNING_COLLIDING_TASK;
            } else {
                return null;
            }
        };


        this._undoOperation = v -> {
            assert this._id != null;
            this._storage.remove(this._id);
            return true;
        };

        this._redoOperation = v -> {
            assert this._id != null;
            this._storage.undelete(this._id);
            return true;
        };

    }

    private void createAsDeleteUnit() {

        this._initialOperation = v -> {
            // get the set of IDs whose corresponding tasks are to be deleted
            if (this._command.hasTrueValue(Command.ParamName.TASK_UNIVERSALLY_QUANTIFIED)) {
                this._idRange = this._storage.getNonDeletedTasks()
                        .stream() // get all tasks (that have not been deleted)
                        .mapToInt(Integer::intValue).toArray();
            } else {
                List<Range> ranges = this._command.getParameter(Command.ParamName.TASK_INDEX_RANGES);
                this._idRange = Arrays.stream(Range.enumerateRanges(ranges))
                        .filter(id -> !this._storage.get(id).isDeleted())
                        .toArray();
            }

            assert this._idRange != null;

            if (this._idRange.length == 0) {
                this._wasExecuted = false; // we didn't delete anything
                return ERROR_RANGE_EMPTY_FOR_DELETE;
            }

            Arrays.stream(this._idRange)
                    .forEach(this._storage::remove);

            this._wasExecuted = true; // we actually deleted some tasks
            return null;
        };


        this._undoOperation = v -> {
            if (!this._wasExecuted) {
                return false;
            }

            assert this._idRange != null;
            Arrays.stream(this._idRange)
                    .forEach(this._storage::undelete);
            return true;
        };

        this._redoOperation = v -> {
            if (!this._wasExecuted) {
                return false;
            }

            assert this._idRange != null;
            Arrays.stream(this._idRange)
                    .forEach(this._storage::remove);
            return true;
        };

    }

    private void createAsEditUnit() {

        this._initialOperation = v -> {
            this._id = this._command.getParameter(Command.ParamName.TASK_INDEX);
            assert this._id != null;
            Task task = this._storage.get(this._id);

             this._taskPreModification = task.clone(); // take snapshot of task before modifying it, for undo operation

            // check which parameters have changed
            if (this._command.hasParameter(Command.ParamName.TASK_NAME)) {
                task.setTaskName(this._command.getParameter(Command.ParamName.TASK_NAME));
            }
            if (this._command.hasParameter(Command.ParamName.TASK_START)) {
                task.setStartTime(this._command.getParameter(Command.ParamName.TASK_START));
            }
            if (this._command.hasParameter(Command.ParamName.TASK_END)) {
                task.setEndTime(this._command.getParameter(Command.ParamName.TASK_END));
            }

            // Check for priority
            if (this._command.hasParameter(Command.ParamName.PRIORITY_VALUE)) {
                Task.Priority priority = this._command.getParameter(Command.ParamName.PRIORITY_VALUE);
                if (priority != null) {
                    task.setPriority(priority);
                }
            }

            this._taskPostModification = task.clone(); // take snapshot of task after modifying it, for redo operation

            this._wasExecuted = true; // editing a task never fails
            return null;
        };


        this._undoOperation = v -> {
            assert this._taskPreModification != null;
            this._storage.save(this._taskPreModification);
            return true;
        };

        this._redoOperation = v -> {
            assert this._taskPostModification != null;
            this._storage.save(this._taskPostModification);
            return true;
        };

    }

    private void createAsMarkUnit() {

        this._initialOperation = v -> {
            List<Range> ranges = this._command.getParameter(Command.ParamName.TASK_INDEX_RANGES);
            this._idRange = Arrays.stream(Range.enumerateRanges(ranges))
                    .filter(id -> {
                        Task task = this._storage.get(id);
                        return !(task.isDeleted() || task.isCompleted());
                    })
                    .toArray();

            assert this._idRange != null;

            if (this._idRange.length == 0) {
                this._wasExecuted = false;
                return ERROR_RANGE_EMPTY_FOR_MARK;
            }

            Arrays.stream(this._idRange)
                    .mapToObj(this._storage::get)
                    .forEach(task -> task.setCompleted(true));

            this._wasExecuted = true;
            return null;
        };


        this._undoOperation = v -> {
            if (!this._wasExecuted) {
                return false;
            }

            assert this._idRange != null;

            Arrays.stream(this._idRange)
                    .mapToObj(this._storage::get)
                    .forEach(task -> task.setCompleted(false));
            return true;
        };

        this._redoOperation = v -> {
            if (!this._wasExecuted) {
                return false;
            }

            assert this._idRange != null;

            Arrays.stream(this._idRange)
                    .mapToObj(this._storage::get)
                    .forEach(task -> task.setCompleted(true));
            return true;
        };

    }
}
