package front_end;

import back_end.storage.relations.Task;
import factories.TaskList;
import front_end.ui.DisplayOneTaskUI;
import front_end.ui.DisplayTaskListUI;
import objects.ExecutionResult;
import front_end.ui.utility.VisualTuple;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by maianhvu on 5/3/16.
 */
public class TranslationEngineTest {

    private TranslationEngine engine_;
    private ExecutionResult<List<Task>> taskListExecutionResult_;

    @Before
    public void setUp() {
        this.taskListExecutionResult_ = new ExecutionResult<>(DisplayTaskListUI.class, TaskList.buildRandom());
        this.engine_ = new TranslationEngine();
    }

    @Test
    public void Translation_engine_does_not_have_currentView_by_default() {
        assertThat(this.engine_.getCurrentExecutionResult(), is(nullValue()));
    }

    @Test
    public void Translation_engine_does_not_have_idTranslationMap_by_default() {
        assertThat(this.engine_.getCurrentIdTranslator(), is(nullValue()));
    }

    @Test
    public void Translation_engine_has_currentView_upon_display() {
        this.engine_.displayAndParseCommand(taskListExecutionResult_);
        assertThat(this.engine_.getCurrentExecutionResult(), is(taskListExecutionResult_));
    }

    @Test
    public void Translation_engine_does_not_have_idTranslationMap_if_data_is_not_task_list() {
        ExecutionResult<Task> singleTaskExecutionResult = new ExecutionResult<>(DisplayOneTaskUI.class, new Task("Task 1"));
        this.engine_.displayAndParseCommand(singleTaskExecutionResult);

        assertThat(this.engine_.getCurrentIdTranslator(), is(nullValue()));
    }

    @Test
    public void Translation_engine_has_idTranslationMap_if_data_is_task_list() {
        this.engine_.displayAndParseCommand(taskListExecutionResult_);
        assertThat(this.engine_.getCurrentIdTranslator(), is(not(nullValue())));
    }

    @Test
    public void Translation_engine_displays_correct_content() {
        this.engine_.initializeUI(taskListExecutionResult_);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        // Inject output stream
        this.engine_.getCurrentUI().setOutputStream(printStream);
        this.engine_.getCurrentUI().render();

        // Check output
        String output = outputStream.toString();

        // Construct expected output
        StringBuilder expectedOutputBuilder = new StringBuilder();
        for (Object tuple : this.engine_.getCurrentIdTranslator().getVisualTupleList()) {
            VisualTuple<Task> visualTaskTuple = (VisualTuple<Task>) tuple;
            expectedOutputBuilder.append(String.format("%d. %s\n",
                    visualTaskTuple.getIndex(),
                    visualTaskTuple.getOriginal().getTask()));
        }

        assertThat(output, is(equalTo(expectedOutputBuilder.toString())));
    }
}
