package shared;

public enum ViewType {
    TASK_LIST("TaskListView"),
    SINGLE_TASK("SingleTaskView");

    public final String template;

    ViewType(String temp) {
        template = temp;
    }
}
