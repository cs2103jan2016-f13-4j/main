package component.front_end.ui;

import component.back_end.storage.Task;
import component.front_end.ui.core.VisualIndexView;
import component.front_end.ui.core.VisualTuple;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * This child class of View generate the view data when list of tasks object are to be rendered. 
 * The rendering format is as follow: [display ID]. [Time Span] | [Task Name]
 * @author Tio
 *
 */
public class TaskListView extends VisualIndexView<Task> {
    private static final String DATE_FORMAT = "dd/MM|HH:mm";
    private static final String STRING_DATE_DISPLAY_FORMAT = "%s - %s";
    private static final String STRING_SEPARATOR = " | ";
    private static final String NUMBER_FORMAT = "%d. ";

    private DateTimeFormatter df;
    
    public TaskListView(List<VisualTuple<Task>> list){
        super(list);
        df = DateTimeFormatter.ofPattern(DATE_FORMAT);
    }

    @Override
    public void buildContent() {
        List<VisualTuple<Task>> visualTasks = this.getVisualTupleList();

        for (VisualTuple<Task> visualTask : visualTasks) {
            this.addText(this.constructDisplayID(visualTask.getIndex()));

            Task task = visualTask.getOriginal();
            this.addText(this.constructTimeString(task));
            this.addText(STRING_SEPARATOR);
            this.addLine(task.getTaskName());
        }
    }
    /**
     * this is a helper function that will format the start time and the end time of the task to 
     * the following form: dd/MM|HH:mm - dd/MM|HH:mm 
     * @param task - current task that is going to be formatted
     * @return String containing the start date and end date of the task
     */
    protected String constructTimeString(Task task){
        LocalDateTime start = task.getStartTime();
        LocalDateTime end = task.getEndTime();
        
        String startDisplay = start.format(df);
        String endDisplay = end.format(df);
        
        return String.format(STRING_DATE_DISPLAY_FORMAT, startDisplay, endDisplay);
    }
    // this is a helper function that deals with the formatting of the display ID of the task
    protected String constructDisplayID(int i){
        return String.format(NUMBER_FORMAT,(i));
    }
    

}