package shared;

import java.util.Set;
import java.util.function.*;
import java.time.LocalDateTime;
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
    public static Function<?, String> createErrorFunction(String str) {
        return v -> str;
    }

    public static final String ERROR_ALREADY_DELETED_ALL_TASKS = "All tasks have already been deleted!";
    public static final String ERROR_ALREADY_MARKED_TASK = "Already marked task!";


    private Function<?, String> _initialOperation; // returns the error string for the operation, to be placed in an ExecutionResult
    private Function<?, Boolean> _undoOperation; // returns false if nothing was done due to original operation not being run
    private Function<?, Boolean> _redoOperation; // returns false if nothing was done due to original operation not being run

    private Command _command; // command that gave rise to this execution unit
    private Integer _id = null; // _id of the task handled by this execution unit
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
            Storage.getInstance().remove(this._id);
            return true;
        };

        this._redoOperation = v -> {
            Storage.getInstance().undelete(this._id);
            return true;
        };

    }

    private void createAsDeleteUnit() {

        if (_command.hasTrueValue(Command.ParamName.TASK_UNIVERSALLY_QUANTIFIED)) {
            /*
             * DELETE ALL TASKS
             */
            Set<Integer> tasksToDelete = Storage.getInstance().getNonDeletedTasks();

            this._initialOperation = v -> {

                // failure case where all tasks are already deleted
                if (tasksToDelete.isEmpty()) {
                    this._wasExecuted = false;
                    return ERROR_ALREADY_DELETED_ALL_TASKS;
                }

                tasksToDelete
                        .stream()
                        .forEach(id -> Storage.getInstance().remove(id));

                this._wasExecuted = true; // something actually got deleted
                return null;
            };


            this._undoOperation = v -> {
                if (!this._wasExecuted) {
                    return false;
                }

                tasksToDelete
                        .stream()
                        .forEach(id -> Storage.getInstance().undelete(id));

                return true;
            };

            this._redoOperation = v -> {
                if (!this._wasExecuted) {
                    return false;
                }

                tasksToDelete
                        .stream()
                        .forEach(id -> Storage.getInstance().remove(id));

                return true;
            };

        } else {
            /*
             * DELETE A SINGLE TASK
             */

            this._initialOperation = v -> {
                this._id = this._command.getParameter(Command.ParamName.TASK_INDEX);
                assert this._id != null;

                Storage.getInstance().remove(this._id);

                this._wasExecuted = true; // deleting a single task never fails
                return null;
            };

            this._undoOperation = v -> {
                Storage.getInstance().undelete(this._id);
                return true;
            };

            this._redoOperation = v -> {
                Storage.getInstance().remove(this._id);
                return true;
            };
        }
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
            this._id = _command.getParameter(Command.ParamName.TASK_INDEX);
            Task taskToMark = Storage.getInstance().get(this._id);

            if (taskToMark.isCompleted()) {
                this._wasExecuted = false; // task already marked
                return ERROR_ALREADY_MARKED_TASK;
            }

            taskToMark.setCompleted(true);

            this._wasExecuted = true;
            return null;
        };

        this._undoOperation = v -> {
            if (!this._wasExecuted) {
                return false;
            }
            Storage.getInstance().get(this._id).setCompleted(false);
            return true;
        };

        this._redoOperation = v -> {
            if (!this._wasExecuted) {
                return false;
            }
            Storage.getInstance().get(this._id).setCompleted(true);
            return true;
        };
    }
}
