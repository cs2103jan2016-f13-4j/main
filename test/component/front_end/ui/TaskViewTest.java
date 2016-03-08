package component.front_end.ui;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import component.back_end.storage.Task;
import component.front_end.ui.core.VisualTuple;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TaskViewTest {
    private static final String DATE_FORMAT = "dd/MM|HH:mm";
    
    private LocalDateTime ldtStart;
    private LocalDateTime ldtEnd;
    private TaskView tv; 
    private VisualTuple<Task> vtt; 
    private Task task; 
    
    @Before 
    public void init(){
        ldtStart = LocalDateTime.of(2016,3,6,22,0);
        ldtEnd = LocalDateTime.of(2016,3,7,0,0);
        task = new Task(1,"Finish ST2334","2 more chapters left", ldtStart, ldtEnd);
        vtt = new VisualTuple<Task>(1,task);
        tv = new TaskView(vtt);
       
    }
    
    @Test
    public void dateViewTest() {
    String expectedStart = "Sunday, 06 March 2016 | 22:00";    
    String expectedEnd = "Monday, 07 March 2016 | 00:00";
    assertThat(tv.constructTime(ldtStart),is(expectedStart));
    assertThat(tv.constructTime(ldtEnd),is(expectedEnd));
      
    }

}
