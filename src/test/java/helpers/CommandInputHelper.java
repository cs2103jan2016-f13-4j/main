package helpers;

/**
 * Created by maianhvu on 01/04/2016.
 */
public class CommandInputHelper {

    public static String constructAddCommand(String taskName) {
        return String.format("add name:\"%s\"", taskName);
    }

    public static String constructEditCommand(int visualId, String taskName) {
        return String.format("edit:%d name:\"%s\"", visualId, taskName);
    }
}
