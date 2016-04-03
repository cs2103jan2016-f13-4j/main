package ui;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.ListViewMatchers.hasItems;
import static org.testfx.matcher.control.ListViewMatchers.hasListCell;

import java.io.File;

import org.junit.AfterClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import helpers.CommandInputHelper;
import helpers.IntegerationTestHelper;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Pair;
import shared.ApplicationContext;
import shared.Task;
import storage.Storage;

public class CommandInputTest extends ApplicationTest {

    @Override public void start(Stage stage) {
        IntegerationTestHelper.startTestApplication(stage);
    }

    @Test public void Application_closes_on_exit_command() {
        // given:
        clickOn("#command-input");

        // when:
        write("exit").push(KeyCode.ENTER);

        // then:
        assertThat(ApplicationContext.mainContext().getPrimaryStage().isShowing(), is(false));
    }

    @Test public void New_item_appears_on_valid_add_command() {
        // given:
        clickOn("#command-input");
        ListView<?> listView = IntegerationTestHelper.findComponent("#component--main");
        int itemsCount = listView.getItems().size();

        // when:
        write(CommandInputHelper.constructAddCommand("Buy some groceries")).push(KeyCode.ENTER);

        // then:
        verifyThat("#component--main", hasItems(itemsCount + 1));
        write("exit").push(KeyCode.ENTER);
    }

    @Test public void Existing_item_gets_modified_using_edit_command() {
        // given:
        clickOn("#command-input");
        write(CommandInputHelper.constructAddCommand("To be edited")).push(KeyCode.ENTER);
        int newItemIndex = ((ListView<?>) IntegerationTestHelper.findComponent("#component--main")).getItems().size();
        Task recentItem = Storage.getInstance().getAll().stream()
                .filter(task -> task.getTaskName().equals("To be edited")).findFirst().get();

        // when:
        write(CommandInputHelper.constructEditCommand(newItemIndex, "Already edited")).push(KeyCode.ENTER);

        // then:
        assertThat(recentItem.getTaskName(), is(equalTo("Already edited")));
        verifyThat("#component--main", hasListCell(new Pair<>(newItemIndex, recentItem)));
        write("exit").push(KeyCode.ENTER);
    }

    @AfterClass public static void tearDownAfterClass() throws Exception {
        IntegerationTestHelper.shutdownTestApplication();
        new File("tmp/ToDoData.csv").delete();
    }
}
