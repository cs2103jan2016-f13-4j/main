package component.back_end;

import static org.junit.Assert.*;

import java.time.Month;

import org.junit.*;
import component.back_end.storage.Task;
import component.back_end.storage.TaskCollection;
import entity.command.*;


class DecisionEngineTester extends DecisionEngine {
    public TaskCollection getTaskCollectionForTest() {
        return this.taskData_;
    }
}

public class DecisionEngineTest {
    private DecisionEngineTester dEngine_;
    
    @Before
    public void setUp() {
        this.dEngine_ = new DecisionEngineTester();
        TaskCollection tc = this.dEngine_.getTaskCollectionForTest();
    }
    
    /*
     * WARNING: INCOMPLETE/BROKEN TEST! DON'T ASSUME ANYTHING FROM ITS RESULTS!
     * TODO: fix it after the Command Parser has been fixed
     */
    @Test
    public void testCreateTask() {
        Instruction instruction = new Instruction(Instruction.Type.ADD);
        
        ParameterList params =  new ParameterList();
            params.addParameter(ParameterName.NAME,
                    ParameterValue.parseParamValue("chiong V0.1", ParameterName.NAME));
            params.addParameter(ParameterName.DATE_FROM,
                    ParameterValue.parseParamValue("20022016 1000", ParameterName.DATE_FROM));
            params.addParameter(ParameterName.DATE_TO,
                    ParameterValue.parseParamValue("11032017 1800", ParameterName.DATE_TO));
        
        Command cmd = new Command(instruction, params);
        
        
        Task task = this.dEngine_.createTask(cmd);
        
        assertEquals(task.getTaskName(), "chiong V0.1");
        
        assertEquals(task.getStartTime().getYear(), 2016);
        assertEquals(task.getStartTime().getMonth(), Month.FEBRUARY);
        assertEquals(task.getStartTime().getDayOfMonth(), 20);
        assertEquals(task.getStartTime().getHour(), 10);
        assertEquals(task.getStartTime().getMinute(), 0);
        
        assertEquals(task.getEndTime().getYear(), 2017);
        assertEquals(task.getEndTime().getMonth(), Month.MARCH);
        assertEquals(task.getEndTime().getDayOfMonth(), 11);
        assertEquals(task.getEndTime().getHour(), 18);
        assertEquals(task.getEndTime().getMinute(), 0);
    }
}
