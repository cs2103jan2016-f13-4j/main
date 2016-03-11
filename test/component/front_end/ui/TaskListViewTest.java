package component.front_end.ui;

import component.back_end.storage.Task;
import component.front_end.ui.core.VisualTuple;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class TaskListViewTest {
      
    private Task task1;
    private Task task2; 
    private ArrayList<VisualTuple<Task>> taskList;
    private DateTimeFormatter df;
    private LocalDateTime ldtStart1;
    private LocalDateTime ldtStart2;
    private LocalDateTime ldtEnd1;
    private LocalDateTime ldtEnd2;
    private TaskListView listView;
    
    @Before
    public void setUp(){
       //TO-DO: set up some dummyTask. Set up TaskListView
        ldtStart1 = LocalDateTime.of(2015,10,5,1,0); 
        ldtEnd1 = LocalDateTime.of(2015,10,6,5,0); 
        ldtStart2 = LocalDateTime.of(2015,10,6,12,0); 
        ldtEnd2 = LocalDateTime.of(2015,10,6,17,0); 
   
        task1 = new Task(100, "Stay Alive", "Do not die",ldtStart1,ldtEnd1);
        task2 = new Task(101, "Cry", "Me a river",ldtStart2,ldtEnd2);
        
        taskList = new ArrayList<VisualTuple<Task>>();
        taskList.add(new VisualTuple<>(1, task1));
        taskList.add(new VisualTuple<>(2, task2));
        
        listView = new TaskListView(taskList);
    }
    
    @Test
    public void constructTimeTest() {
        
        assertThat(listView.constructTimeString(task1),is(equalTo("05/10|01:00 - 06/10|05:00")));
        assertThat(listView.constructTimeString(task2),is(equalTo("06/10|12:00 - 06/10|17:00")));
    }
    
    @Test
    public void buildContentTest() {
        StringBuilder expectedContent = new StringBuilder();
        expectedContent.append("Found 2 task(s):\n");
        expectedContent.append("1. 05/10|01:00 - 06/10|05:00 | Stay Alive\n");
        expectedContent.append("2. 06/10|12:00 - 06/10|17:00 | Cry\n");

        assertThat(listView.getContent(), is(equalTo(expectedContent.toString())));
    }
    

}
