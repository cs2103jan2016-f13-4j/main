package demo;

import helpers.IntegrationTestHelper;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.File;
import java.util.Arrays;

/**
 * Created by maianhvu on 04/04/2016.
 */
public class OP2Demo extends ApplicationTest {

    public static final int DELAY_SUBMIT = 3000;
    public static final int DELAY_POST_EXECUTION = 15000;

    @Override public void start(Stage stage) throws Exception {
        IntegrationTestHelper.startTestApplication(stage);
        new File("tmp/ToDoData.csv").deleteOnExit();
    }

    @Test
    public void Tio_add_commands() {
        clickOn("#command-input");

        executeCommands(
                "add meeting with new business partner from wednesday 10 am to wednesday 2 pm",
                "add meeting with boss from tomorrow 4 pm to 5th april 6 pm as high priority",
                "add tidy up cubicle as low priority",
                "create buy parents birthday present as high priority",
                "exit"
        );
    }

    private void executeCommands(String... command) {
        Arrays.asList(command).stream()
                .forEach(this::fillInCommand);
    }

    private void fillInCommand(String command) {
        clickOn("#command-input")
                .write(command)
                .sleep(DELAY_SUBMIT)
                .push(KeyCode.ENTER);

        if (!command.trim().toLowerCase().equals("exit")) {
            sleep(DELAY_POST_EXECUTION);
        }
    }
}
