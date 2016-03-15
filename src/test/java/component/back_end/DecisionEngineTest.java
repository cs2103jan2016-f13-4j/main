package component.back_end;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.Month;

import component.back_end.DecisionEngine;
import org.junit.*;
import component.back_end.storage.Task;
import component.back_end.storage.TaskCollection;
import entity.command.*;


public class DecisionEngineTest {
    private DecisionEngine dEngine_;
    private TaskCollection tc_;

    private Command CMD_ADD;
    private Command CMD_EDIT;
    private Command CMD_DISPLAY;
    private Command CMD_DELETE;


    private void initSampleCommands() {
        this.initSampleAddCommand();
    }

    private void initSampleAddCommand() {
        Instruction instruction = new Instruction(Instruction.Type.ADD);

        ParameterList params =  new ParameterList();
        params.addParameter(ParameterName.NAME, ParameterValue.parseParamValue("chiong V0.1", ParameterName.NAME));
        params.addParameter(ParameterName.DATE_FROM, ParameterValue.parseParamValue("20022016 1000", ParameterName.DATE_FROM));
        params.addParameter(ParameterName.DATE_TO, ParameterValue.parseParamValue("11032017 1800", ParameterName.DATE_TO));

        this.CMD_ADD = new Command(instruction, params);
    }

    private void initSampleEditCommand(int index) {
        Instruction instruction = new Instruction(Instruction.Type.EDIT, index);
    }


    @Before
    public void setUp() throws IOException {
        this.tc_ = new TaskCollection();
        this.dEngine_ = new DecisionEngine(this.tc_, null);
        this.initSampleCommands();
    }

    @Test
    public void testCreateTask() {
        Task task = this.dEngine_.createTask(this.CMD_ADD);

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


    @Test
    public void testHandleAdd() {

    }

    @Test
    public void testHandleEdit() {

    }

    @Test
    public void testHandleDisplay() {

    }

    @Test
    public void testHandleDelete() {

    }
}
