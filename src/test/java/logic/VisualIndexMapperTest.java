package logic;

import org.junit.Test;
import shared.Command;
import shared.CustomTime;
import shared.Range;
import shared.Task;
import ui.view.VisualTask;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @@author A0127046L
 */
public class VisualIndexMapperTest {

    private static VisualIndexMapper getMapper() {
        return VisualIndexMapper.getInstance();
    }

    @Test
    public void Mapper_transforms_task_list_into_visual_list() {
        List<VisualTask> visualList = getMapper().translateRawToVisual(buildTaskList());
        // Index should start from 1
        int currentIndex = 1;
        for (VisualTask visualItem : visualList) {
            assertThat(visualItem.getVisualIndex(), is(equalTo(currentIndex++)));
        }
    }

    @Test
    public void Mapper_transforms_visual_command_into_raw_command() {
        getMapper().translateRawToVisual(buildTaskList());
        Command deleteCommand = new Command(Command.Instruction.DELETE);
        deleteCommand.setParameter(Command.ParamName.TASK_INDEX, 1);
        getMapper().translateVisualToRaw(deleteCommand);
        assertThat(deleteCommand.getParameter(Command.ParamName.TASK_INDEX), is(equalTo(51)));
    }

    @Test
    public void Mapper_transform_complex_task_list_correctly() {
        getMapper().translateRawToVisual(stubTasks(128, 47, 12, 13, 15, 67, 90, 20, 14, 68));
        Command deleteCommand = new Command(Command.Instruction.DELETE);
        deleteCommand.setParameter(Command.ParamName.TASK_INDEX_RANGES, Arrays.asList(
                new Range(2, 4),
                new Range(6),
                new Range(8, 10)
        ));

        getMapper().translateVisualToRaw(deleteCommand);
        List<Range> rawIdRanges = deleteCommand.getParameter(Command.ParamName.TASK_INDEX_RANGES);
        // [12,13,14] [67,68] [20]
        assertThat(rawIdRanges, hasSize(4));
        assertThat(rawIdRanges, hasItems(
                new Range(12, 14),
                new Range(20),
                new Range(47),
                new Range(67, 68)
        ));

    }

    private static List<Task> buildTaskList() {
        return IntStream.range(1, 5)
                .mapToObj(index -> new Task(index + 50, "Task " + index, "Lorem ipsum",
                        LocalDateTime.now(), LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    private static Task stubTask(int index) {
        return new Task(index, "Task " + index, null, CustomTime.todayAt(null), CustomTime.tomorrowAt(null));
    }

    private static List<Task> stubTasks(int... taskId) {
        return IntStream.of(taskId).mapToObj(VisualIndexMapperTest::stubTask)
                .collect(Collectors.toList());
    }

}
