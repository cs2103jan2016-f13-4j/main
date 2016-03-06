package component.front_end.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import entity.Task;
/**
 * This view class generate the view data when list of tasks object are to be rendered
 * @author Tio
 *
 */
public class TaskListView extends View<List<Task>> {
    private static final String DATE_FORMAT = "dd/MM|HH:mm"; 
    private static final String DATE_CONNECTOR = " - ";
    private static final String STRING_SEPARATOR = " | ";
    private static final String NUMBER_FORMAT = "%d.";
    private DateTimeFormatter df;
    
    public TaskListView(List<Task> list){
        super(list);
        df = DateTimeFormatter.ofPattern(DATE_FORMAT);
    }

    @Override
    public void buildContent() {
        List<Task> tasks = (List<Task>) this.getViewData();
        int order = 1; 
        for (Task task : tasks) {
            this.addText(addOrder(order++));
            this.constructTimeString(task);
            this.addText(STRING_SEPARATOR);
            this.addLine(task.getTask());
        }
    }
    
    protected String constructTimeString(Task current){
        LocalDateTime start = current.getStartingTime();
        LocalDateTime end = current.getEndingTime();
        String startDisplay = start.format(df);
        String endDisplay = end.format(df);
        this.addText(startDisplay);
        this.addText(DATE_CONNECTOR);
        this.addText(endDisplay);
        return this.getContent();
    }
    
    protected String addOrder(int i){
        return String.format(NUMBER_FORMAT,(i));
    }
    

}