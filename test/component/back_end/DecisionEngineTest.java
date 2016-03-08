package component.back_end;

import static org.junit.Assert.*;

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
    
    @Test
    public void testCreateTask() {
        Instruction.Type instrType = Instruction.Type.ADD;
        
        ParameterList params =  new ParameterList();
        params.addParameter("param1", "value1");
        params.addParameter("param2", "value2");
    }
    
}
