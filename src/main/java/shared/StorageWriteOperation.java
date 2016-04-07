package shared;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import storage.*;

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
 * @@author Thenaesh Elango
 */
public class StorageWriteOperation {

    public static final String ERROR_RANGE_EMPTY_FOR_DELETE = "No valid tasks in range to delete!";
    public static final String ERROR_RANGE_EMPTY_FOR_MARK = "No valid tasks in range to mark as complete!";

    private Function<?, String> _initialOperation; // returns the error string for the operation, to be placed in an ExecutionResult
    private Function<?, Boolean> _undoOperation; // returns false if nothing was done due to original operation not being run
    private Function<?, Boolean> _redoOperation; // returns false if nothing was done due to original operation not being run

    private Command _command; // command that gave rise to this execution unit
    private Integer _id = null; // id of the task handled by this op, if used
    private int[] _idRange = null; // set of ids handled by this op, if used
    private Task _taskPreModification = null; // snapshot of the task before it is modified
    private Task _taskPostModification = null; // snapshot of the task after it is modified
    private boolean _wasExecuted = false; // success code for the operation

    public StorageWriteOperation(Command command) {
        this._command = command;

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

            // for each command parameter, check if it was supplied
            // if so, extract the value and set the appropriate reference above to point to the extracted value
            if (this._command.hasParameter(Command.ParamName.TASK_NAME)) {
                name = this._command.getParameter(Command.ParamName.TASK_NAME);
            }
            if (this._command.hasParameter(Command.ParamName.TASK_START)) {
                from = this._command.getParameter(Command.ParamName.TASK_START);
            }
            if (this._command.hasParameter(Command.ParamName.TASK_END)) {
                to = this._command.getParameter(Command.ParamName.TASK_END);
            }
            // we now build the Task object for adding into the store
            Task taskToAdd = new Task(null, name, "", from, to);

            // Check for priority
            if (this._command.hasParameter(Command.ParamName.PRIORITY_VALUE)) {
                Task.Priority priority = this._command.getParameter(Command.ParamName.PRIORITY_VALUE);
                if (priority != null) {
                    taskToAdd.setPriority(priority);
                }
            }

            this._id = Storage.getInstance().save(taskToAdd);

            this._wasExecuted = true; // adding a task never fails
            return null;
        };


        this._undoOperation = v -> {
            assert this._id != null;
            Storage.getInstance().remove(this._id);
            return true;
        };

        this._redoOperation = v -> {
            assert this._id != null;
            Storage.getInstance().undelete(this._id);
            return true;
        };

    }

    private void createAsDeleteUnit() {

        this._initialOperation = v -> {
            // get the set of IDs whose corresponding tasks are to be deleted
            if (this._command.hasTrueValue(Command.ParamName.TASK_UNIVERSALLY_QUANTIFIED)) {
                this._idRange = Storage.getInstance().getNonDeletedTasks()
                        .stream() // get all tasks (that have not been deleted)
                        .mapToInt(Integer::intValue).toArray();
            } else {
                List<Range> ranges = this._command.getParameter(Command.ParamName.TASK_INDEX_RANGES);
                this._idRange = Arrays.stream(Range.enumerateRanges(ranges))
                        .filter(id -> !Storage.getInstance().get(id).isDeleted())
                        .toArray();
            }

            assert this._idRange != null;

            if (this._idRange.length == 0) {
                this._wasExecuted = false; // we didn't delete anything
                return ERROR_RANGE_EMPTY_FOR_DELETE;
            }

            Arrays.stream(this._idRange)
                    .forEach(Storage.getInstance()::remove);

            this._wasExecuted = true; // we actually deleted some tasks
            return null;
        };


        this._undoOperation = v -> {
            if (!this._wasExecuted) {
                return false;
            }

            assert this._idRange != null;
            Arrays.stream(this._idRange)
                    .forEach(Storage.getInstance()::undelete);
            return true;
        };

        this._redoOperation = v -> {
            if (!this._wasExecuted) {
                return false;
            }

            assert this._idRange != null;
            Arrays.stream(this._idRange)
                    .forEach(Storage.getInstance()::remove);
            return true;
        };

    }

    private void createAsEditUnit() {

        this._initialOperation = v -> {
            this._id = this._command.getParameter(Command.ParamName.TASK_INDEX);
            assert this._id != null;
            Task task = Storage.getInstance().get(this._id);

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
            Storage.getInstance().save(this._taskPreModification);
            return true;
        };

        this._redoOperation = v -> {
            assert this._taskPostModification != null;
            Storage.getInstance().save(this._taskPostModification);
            return true;
        };

    }

    private void createAsMarkUnit() {

        this._initialOperation = v -> {
            List<Range> ranges = this._command.getParameter(Command.ParamName.TASK_INDEX_RANGES);
            this._idRange = Arrays.stream(Range.enumerateRanges(ranges))
                    .filter(id -> {
                        Task task = Storage.getInstance().get(id);
                        return !(task.isDeleted() || task.isCompleted());
                    })
                    .toArray();

            assert this._idRange != null;

            if (this._idRange.length == 0) {
                this._wasExecuted = false;
                return ERROR_RANGE_EMPTY_FOR_MARK;
            }

            Arrays.stream(this._idRange)
                    .mapToObj(Storage.getInstance()::get)
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
                    .mapToObj(Storage.getInstance()::get)
                    .forEach(task -> task.setCompleted(false));
            return true;
        };

        this._redoOperation = v -> {
            if (!this._wasExecuted) {
                return false;
            }

            assert this._idRange != null;
            Arrays.stream(this._idRange)
                    .mapToObj(Storage.getInstance()::get)
                    .forEach(task -> task.setCompleted(true));
            return true;
        };

    }
}
