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

        Task task1 = this.decisionEngine.createTask(fullAdd);
        task1.setId(specialTestID);
        Task expected1 = new Task(specialTestID, name1, null, testDate1, testDate2);
        Task task2 = this.decisionEngine.createTask(partialAdd);
        task2.setId(specialTestID);
        Task expected2 = new Task(specialTestID, name2, null, testDate2, testDate3);

        assertEquals(task1, expected1);
        assertEquals(task2, expected2);
    }

}
