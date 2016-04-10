package logic;

import org.junit.Before;
import org.junit.Test;
import shared.Command;
import shared.CustomTime;
import shared.Task;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

/**
 * Created by maianhvu on 06/04/2016.
 */
public class CommandParserTest {

    private CommandParser _parser;

    @Before
    public void setUp() {
        this._parser = CommandParser.getInstance();
        this._parser.initialise();
    }

    @Test
    public void Command_parser_parses_simple_add_correctly() {
        Command command = this._parser.parse("add new task");
        assertThat(command.getInstruction(), is(Command.Instruction.ADD));
    }

    @Test
    public void Command_parser_parses_add_with_date_correctly() {
        Command command = this._parser.parse("add new task starting today 5pm to next Monday 1800");
        assertThat(command.getParameter(Command.ParamName.TASK_START),
                is(equalTo(CustomTime.todayAt(LocalTime.of(17, 0)))));
        assertThat(command.getParameter(Command.ParamName.TASK_END),
                is(equalTo(CustomTime.todayAt(LocalTime.of(18, 0)).current(DayOfWeek.MONDAY))));
    }

    @Test
    public void Command_parser_parses_add_with_exact_date_correctly() {
        Command command = this._parser.parse("add new task from 7th March's 0800 to Apr 27 2016");
        assertThat(command.getParameter(Command.ParamName.TASK_START),
                is(equalTo(new CustomTime(
                        LocalDate.of(2016, Month.MARCH, 7),
                        LocalTime.of(8, 0)
                ))));
        assertThat(command.getParameter(Command.ParamName.TASK_END),
                is(equalTo(new CustomTime(
                        LocalDate.of(2016, Month.APRIL, 27),
                        null
                ))));
    }

    @Test
    public void Command_parser_has_the_correct_task_name_without_quotes() {
        Command command = this._parser.parse("add new task starting today 5pm to next Monday 1800");
        assertThat(command.getParameter(Command.ParamName.TASK_NAME),
                is(equalTo("new task")));
    }

    @Test
    public void Command_parser_has_the_correct_task_name_with_quotes() {
        Command command = this._parser.parse("add \"new task starting today 5pm to next Monday 1800\"");
        assertThat(command.getParameter(Command.ParamName.TASK_NAME),
                is(equalTo("new task starting today 5pm to next Monday 1800")));
    }

    @Test
    public void Command_parser_parses_correctly_even_without_prepositions() {
        Command command = this._parser.parse("add new task today 7pm");
        assertThat(command.getParameter(Command.ParamName.TASK_END),
                is(equalTo(CustomTime.todayAt(LocalTime.of(19,0)))));
    }

    @Test
    public void Command_parser_regards_empty_add_phrases_as_invalid() {
        Command command = this._parser.parse("add");
        assertThat(command.getInstruction(), is(Command.Instruction.INVALID));
    }

    @Test
    public void Command_parser_regards_items_with_start_time_but_no_end_as_invalid() {
        Command command = this._parser.parse("add hello world starting tomorrow 7pm");
        assertThat(command.getInstruction(), is(Command.Instruction.INVALID));
    }

    @Test
    public void Command_parser_interprets_priority_correctly() {
        Command command = this._parser.parse("add hello world with high priority");
        assertThat(command.getParameter(Command.ParamName.PRIORITY_VALUE),
                is(equalTo(Task.Priority.HIGH)));
    }

    @Test
    public void Command_parser_parses_simple_edit_command_correctly() {
        Command command = this._parser.parse("edit task number 5 whatever from today until tomorrow");
        assertThat(command.getInstruction(), is(Command.Instruction.EDIT));
        assertThat(command.getParameter(Command.ParamName.TASK_NAME), is(equalTo("whatever")));
        assertThat(command.getParameter(Command.ParamName.TASK_START), is(equalTo(CustomTime.todayAt(null))));
        assertThat(command.getParameter(Command.ParamName.TASK_END), is(equalTo(CustomTime.tomorrowAt(null))));
    }

    @Test
    public void Command_parser_regards_edit_without_task_name_correctly() {
        Command command = this._parser.parse("edit 3 starting today");
        assertThat(command.getParameter(Command.ParamName.TASK_START), is(equalTo(CustomTime.todayAt(null))));
        assertFalse(command.hasParameter(Command.ParamName.TASK_NAME));
    }
}
