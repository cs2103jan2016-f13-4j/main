package shared;

/**
 * @@author Mai Anh Vu
 */
public enum ViewType {
    TASK_LIST("TaskListView"),
    SINGLE_TASK("SingleTaskView");

    public final String template;

    ViewType(String temp) {
        template = temp;
    }
}
