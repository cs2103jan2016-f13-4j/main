package logic;

import javafx.util.Pair;
import org.junit.Test;
import shared.Command;
import storage.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by maianhvu on 23/03/2016.
 */
public class VisualIndexMapperTest {

    private static VisualIndexMapper getMapper() {
        return VisualIndexMapper.getInstance();
    }

    @Test
    public void Mapper_transforms_task_list_into_visual_list() {
        List<Pair<Integer, Task>> visualList = getMapper().translateRawToVisual(buildTaskList());
        // Index should start from 1
        int currentIndex = 1;
        for (Pair<Integer, Task> visualItem : visualList) {
            assertThat(visualItem.getKey(), is(equalTo(currentIndex++)));
        }
    }

    @Test
    public void Mapper_transforms_visual_command_into_raw_command() {
        getMapper().updateList(buildTaskList());
        Command deleteCommand = new Command(
                new Instruction(Instruction.Type.DELETE, 1),
                ParameterList.emptyList()
        );
        getMapper().translateVisualToRaw(deleteCommand);
        assertThat(deleteCommand.getInstruction().getIndex(), is(equalTo(51)));
    }

    private static List<Task> buildTaskList() {
        return IntStream.range(1, 5)
                .mapToObj(index -> new Task(index + 50, "Task " + index, "Lorem ipsum",
                        LocalDateTime.now(), LocalDateTime.now()))
                .collect(Collectors.toList());
    }
}
