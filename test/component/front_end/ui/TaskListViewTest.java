package component.front_end.ui;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import entity.Task;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class TaskListViewTest {
    
    private final static String DATE_CONNECTOR = " - ";
    private final static String DATE_FORMAT = "dd/MM|HH:mm";
    private final static String STRING_TITLE_1 = "Stay Alive";
    private final static String STRING_TITLE_2 =  "Cry";
    private final static String STRING_DETAIL_1 =  "do not die";
    private final static String STRING_DETAIL_2 = "me a river";
    private final static String STRING_NEW_LINE = "\n";
    
    private Task task1; 
    private Task task2; 
    private ArrayList<Task> taskList;
    private DateTimeFormatter df;
    private LocalDateTime ldtStart1;
    private LocalDateTime ldtStart2;
    private LocalDateTime ldtEnd1;
    private LocalDateTime ldtEnd2;
    private TaskListView listView;
    
    @Before
    public void init(){
       //TO-DO: set up some dummyTask. Set up TaskListView
        LocalDateTime ldtStart1 = LocalDateTime.of(2015,10,5,1,0); 
        LocalDateTime ldtEnd1 = LocalDateTime.of(2015,10,6,5,0); 
        LocalDateTime ldtStart2 = LocalDateTime.of(2015,10,6,12,0); 
        LocalDateTime ldtEnd2 = LocalDateTime.of(2015,10,6,17,0); 
        
        df = DateTimeFormatter.ofPattern(DATE_FORMAT);
        
        task1 = new Task(STRING_TITLE_1, STRING_DETAIL_1,ldtStart1,ldtEnd1);
        task2 = new Task(STRING_TITLE_2, STRING_DETAIL_2,ldtStart2,ldtEnd2);
        
        taskList = new ArrayList<Task>(); 
        taskList.add(task1);
        taskList.add(task2);
        
        listView = new TaskListView(taskList);
    }
    
    @Test
    public void constructTimeTest() {
        String start1 = ldtStart1.format(df);
        String end1 = ldtEnd1.format(df);
        String start2 = ldtStart2.format(df);
        String end2 = ldtEnd2.format(df); 
        String time1 = start1 + DATE_CONNECTOR +  end1; 
        String time2 = start2 + DATE_CONNECTOR + end2;
        
        assertThat(listView.constructTimeString(task1),is(equalTo(time1)));
        assertThat(listView.constructTimeString(task2),is(equalTo(time2)));
    }
    
    @Test
    public void getContentTest() {
        String start1 = ldtStart1.format(df);
        String end1 = ldtEnd1.format(df);
        String start2 = ldtStart2.format(df);
        String end2 = ldtEnd2.format(df); 
        String time1 = start1 + DATE_CONNECTOR +  end1; 
        String time2 = start2 + DATE_CONNECTOR + end2;
        String outcome1 = "1. " + time1 + STRING_TITLE_1; 
        String outcome2 = "2. " + time2 + STRING_TITLE_2;
        String combinedOutcome = outcome1 + STRING_NEW_LINE + outcome2;
        
        assertThat(listView.getContent(),is(combinedOutcome));
    }


}
