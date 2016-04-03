package shared;

import java.util.function.*;
import java.time.LocalDateTime;
import storage.*;

/**
 * Represents an write operation on Storage by the Decision Engine, in response to a Command
 * Encapsulates both the operation and its inverse
 * Contains sufficient information to enable undo/redo of the operation
 *
 * WARNING: These objects should NOT persist between sessions. Undefined behaviour may result.
 *
 * @@author Thenaesh Elango
 */
public class StorageWriteOperation {
    private Function<?, ?> _initialOperation;
    private Function<?, ?> _undoOperation;
    private Function<?, ?> _redoOperation;

    private Command _command; // command that gave rise to this execution unit
    private Integer _id = null; // _id of the task created/modified/deleted by this execution unit
    private Task _taskPreModification = null; // snapshot of the task before it is modified
    private Task _taskPostModification = null; // snapshot of the task after it is modified

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
            default:
                assert false;
        }
    }


    // getters
    public Function<?, ?> getInitialOperation() {
        return this._initialOperation;
    }

    public Function<?, ?> getUndoOperation() {
        return this._undoOperation;
    }

    public Function<?, ?> getRedoOperation() {
        return this._redoOperation;
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

            this._id = Storage.getInstance().save(taskToAdd);
            return (Void) null;
        };

        this._undoOperation = v -> {
            Storage.getInstance().remove(this._id);
            return (Void) null;
        };

        this._redoOperation = v -> {
            Storage.getInstance().undelete(this._id);
            return (Void) null;
        };
    }

    private void createAsDeleteUnit() {

        this._initialOperation = v -> {
            this._id = this._command.getIndex();
            assert this._id != null;

            Storage.getInstance().remove(this._id);

            return (Void) null;
        };

        this._undoOperation = v -> {
            Storage.getInstance().undelete(this._id);

            return (Void) null;
        };

        this._redoOperation = v -> {
            Storage.getInstance().remove(this._id);

            return (Void) null;
        };
    }

    private void createAsEditUnit() {

        this._initialOperation = v -> {
            this._id = this._command.getIndex();
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

            this._taskPostModification = task.clone(); // take snapshot of task after modifying it, for redo operation

            return (Void) null;
        };

        this._undoOperation = v -> {
            assert this._taskPreModification != null;

            Storage.getInstance().save(this._taskPreModification);

            return (Void) null;
        };

        this._redoOperation = v -> {
            assert this._taskPostModification != null;

            Storage.getInstance().save(this._taskPostModification);

            return (Void) null;
        };
    }
}
