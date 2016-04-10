package logic;

import org.junit.Before;
import org.junit.Test;
import shared.Command;
import shared.CustomTime;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
}
