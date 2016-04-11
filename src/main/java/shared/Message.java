package shared;

/**
 * @@author A0127046L
 */
public enum Message {

    WELCOME("Welcome to Your MOM!"),

    UNRECOGNISED("Oops! I don't really understand what you are saying"),

    DISPLAY_EMPTY("Add a new task by entering \"add <task name>\""),
    DISPLAY_NORMAL("Found %d tasks!"),

    ADD_SUCCESS("Added: %s"),
    EDIT_SUCCESS("Edited task to have new details! (undo-able)"),
    DELETE_SUCCESS("Deleted task! (undo-able)"),
    DELETE_FAIL("No valid tasks in range to delete!"),

    SEARCH_FAIL("Found no task with the search query \"%s\""),
    SEARCH_SUCCESS("Found %d matches with the query \"%s\""),

    UNDO_SUCCESS("Reverted last command!"),
    UNDO_FAIL("No tasks to undo!"),
    REDO_SUCCESS("Redone last command!"),
    REDO_FAIL("No tasks to redo!"),

    MARK_SUCCESS("Marked task as completed! (undo-able)"),
    MARK_FAIL("No valid tasks in range to mark as complete!");

    private final String messageString;
    Message(String msg) {
        messageString = msg;
    }

    @Override public String toString() {
        return messageString;
    }
}
