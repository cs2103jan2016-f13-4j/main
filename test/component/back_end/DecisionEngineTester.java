package component.back_end;

import component.back_end.storage.TaskCollection;
import entity.command.Instruction;
import entity.command.ParameterList;
import org.junit.Before;
import org.junit.Test;


class DecisionEngineTester extends DecisionEngine {
    public TaskCollection getTaskCollectionForTest() {
        return this.taskData_;
    }
    private DecisionEngineTester dEngine_;
    
    @Before
    public void setUp() {
        this.dEngine_ = new DecisionEngineTester();
        TaskCollection tc = this.dEngine_.getTaskCollectionForTest();
    }
    
    @Test
    public void testCreateRawAddTask() {
        Instruction instruction = new Instruction(Instruction.Type.ADD);
        
        ParameterList params =  new ParameterList();
        params.addParameter("name", "chiong V0.1");
        params.addParameter("start", "1000");
        params.addParameter("end", "1800");
        
        Command cmd = new Command(instruction, params);
        
        
        Task task = this.dEngine_.createRawTask(cmd);
        
        assertEquals(task.getTaskName(), "chiong V0.1");
        assertEquals(task.getStartTime().getHour(), 10);
        assertEquals(task.getEndTime().getHour(), 18);
        
    }
    
}
