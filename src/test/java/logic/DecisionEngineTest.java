package logic;

import static org.junit.Assert.*;
import org.junit.*;

import shared.*;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @@author Thenaesh Elango
 */
public class DecisionEngineTest {
    private DecisionEngine decisionEngine;

    private static final int specialTestID = -1;

    // times for test use
    private static final String testDate1String = "23032016 1600";
    private static final String testDate2String = "23032016 2100";
    private static final String testDate3String = "24032016 0800";
    private static final String testDate4String = "25032016 1200";
    private static final LocalDateTime testDate1 = StringParser.asDateTime(testDate1String);
    private static final LocalDateTime testDate2 = StringParser.asDateTime(testDate2String);
    private static final LocalDateTime testDate3 = StringParser.asDateTime(testDate3String);
    private static final LocalDateTime testDate4 = StringParser.asDateTime(testDate4String);

    // task names for test use
    private static final String name1 = "magazine of 30 rounds, load";
    private static final String name2 = "100 metre snap target";
    private static final String name3 = "watch your front, watch your front";
    private static final String name4 = "firers, check clear";

    /**
     * creates a Task from a specified command object when it makes sense we
     * should blow up when creating a Task doesn't really make sense
     *
     * @param cmd
     * @return
     */
    private static Task createTask(Command cmd) {
        // initialisation
        String name = null;
        CustomTime from = null;
        CustomTime to = null;

        // for each command parameter, check if it was supplied
        // if so, extract the value and set the appropriate reference above to
        // point to the extracted value
        if (cmd.hasParameter(Command.ParamName.TASK_NAME)) {
            name = cmd.getParameter(Command.ParamName.TASK_NAME);
        }
        if (cmd.hasParameter(Command.ParamName.TASK_START)) {
            from = cmd.getParameter(Command.ParamName.TASK_START);
        }
        if (cmd.hasParameter(Command.ParamName.TASK_END)) {
            to = cmd.getParameter(Command.ParamName.TASK_END);
        }

        // we now build the Task object for adding into the store
        return new Task(null, name, "", from, to);
    }


    @Before
    public void setUp() throws IOException {
        this.decisionEngine = DecisionEngine.getInstance();
    }

    @Test
    public void testCreateTask() {
        Command fullAdd = new Command(Command.Instruction.ADD, null, true);
        fullAdd.setParameter(Command.ParamName.TASK_NAME, name1);
        fullAdd.setParameter(Command.ParamName.TASK_START, testDate1);
        fullAdd.setParameter(Command.ParamName.TASK_END, testDate2);

        Command partialAdd = new Command(Command.Instruction.ADD, null, true);
        partialAdd.setParameter(Command.ParamName.TASK_NAME, name2);

        Task task1 = createTask(fullAdd);
        task1.setId(specialTestID);
        Task expected1 = new Task(specialTestID, name1, null, testDate1, testDate2);
        Task task2 = createTask(partialAdd);
        task2.setId(specialTestID);
        Task expected2 = new Task(specialTestID, name2, null, testDate2, testDate3);

        assertEquals(task1, expected1);
        assertEquals(task2, expected2);
    }

}
