package component.front_end.ui;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import component.back_end.storage.Task;
import component.front_end.ui.core.View;
import component.front_end.ui.core.VisualTuple;


public class TaskView extends View<VisualTuple<Task>> {
    private final static String STRING_DATE_FORMAT = "EEEE, dd MMMM YYYY | HH:mm";
    private final static String STRING_START_TIME = "Start: ";
    private final static String STRING_EMPTY = ""; 
    private final static String STRING_END_TIME = "End  : ";
    private final static String STRING_DETAIL = "Detail";
    private DateTimeFormatter df_;  
    
    public TaskView(VisualTuple<Task> vtt){
            super(vtt);

    }
    
    @Override
    public void buildContent() {
        this.df_ = DateTimeFormatter.ofPattern(STRING_DATE_FORMAT);
        VisualTuple<Task> tuple = (VisualTuple<Task>) this.getViewData();
        Task tsk = tuple.getOriginal();
        this.addLine(tsk.getTaskName());
        this.addText(STRING_START_TIME);
        this.addLine(constructTime(tsk.getStartTime()));
        this.addText(STRING_END_TIME);
        this.addLine(constructTime(tsk.getEndTime()));
        this.addLine(STRING_EMPTY);
        this.addLine(STRING_DETAIL);
        this.addLine(tsk.getDescription());
        
    }
    
    protected String constructTime(LocalDateTime ldtTask){
        return ldtTask.format(this.df_);
    }
    
   
    
}

