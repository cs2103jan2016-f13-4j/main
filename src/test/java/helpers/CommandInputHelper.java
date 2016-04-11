package helpers;

/**
 * @@author A0127046L
 */
public class CommandInputHelper {

    public static String constructAddCommand(String taskName) {
        return String.format("add %s", taskName);
    }

    public static String constructEditCommand(int visualId, String taskName) {
        return String.format("edit task number %d \"%s\"", visualId, taskName);
    }
}
