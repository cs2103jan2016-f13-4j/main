package logic;

import org.junit.Before;
import org.junit.Test;
import shared.Command;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @@author Mai Anh Vu
 */
public class FlexiCommandParserTest {

    private FlexiCommandParser _parser;

    @Before public void setUp() {
        this._parser = FlexiCommandParser.getInstance();
        this._parser.initialize();
    }

    @Test public void FlexiCommandParser_creates_correct_instruction_pattern() {
        final Pattern instructionPattern = Pattern.compile(this._parser.getInstructionPattern(), Pattern.CASE_INSENSITIVE);
        String command = "   ADD a new thing";
        Matcher m = instructionPattern.matcher(command);
        assertTrue(m.find());
        assertThat(m.group("INST").trim().toLowerCase(), is(equalTo("add")));
    }

    @Test public void FlexiCommandParser_creates_correct_time_pattern() {
        final Pattern timePattern = Pattern.compile(this._parser.getTimePattern(), Pattern.CASE_INSENSITIVE);
        String command = "from today's 5pm to next Thursday 2000";
        Matcher m = timePattern.matcher(command);

        assertTrue(m.find());
        assertThat(m.group("DATE"), is(equalTo("from today")));
        assertThat(m.group("TIME"), is(equalTo("5pm")));

        assertTrue(m.find());
        assertThat(m.group("DATE"), is(equalTo("to next Thursday")));
        assertThat(m.group("TIME"), is(equalTo("2000")));
    }

    @Test public void FlexiCommandParser_creates_correct_priority_pattern() {
        final Pattern priorityPattern = Pattern.compile(this._parser.getPriorityPattern(), Pattern.CASE_INSENSITIVE);
        String command = "as very important";
        Matcher m = priorityPattern.matcher(command);

        assertTrue(m.find());
        assertThat(m.group("PRIORITY"), is(equalTo("very important")));
    }

    @Test public void FlexiCommandParser_regards_undefined_keywords_as_unrecognised_commands() {
        Command command = this._parser.parse("random command");
        assertThat(command.getInstruction(), is(equalTo(Command.Instruction.UNRECOGNISED)));
    }

    @Test public void FlexiCommandParser_regards_empty_commands_as_invalid() {
        Command command = this._parser.parse("      ");
        assertThat(command.getInstruction(), is(equalTo(Command.Instruction.INVALID)));
    }

    @Test public void FlexiCommandParser_parses_exit_command_correctly() {
        Arrays.asList("exit", "quit").stream().map(this._parser::parse)
                .map(Command::getInstruction)
                .forEach(instruction -> assertThat(instruction, is(equalTo(Command.Instruction.EXIT))));
    }

    @Test public void FlexiCommandParser_parses_add_command_correctly() {
        String commandString = "add go to the gym starting on Tuesday 5pm with high priority";
        Command command = this._parser.parse(commandString);
        assertThat(command.getInstruction(), is(Command.Instruction.ADD));
        assertThat(command.getParameter(Command.ParamName.TASK_NAME),
                is(equalTo("go to the gym")));

        LocalDateTime tuesday = LocalDateTime.now();
        while (tuesday.getDayOfWeek() != DayOfWeek.TUESDAY) {
            tuesday = tuesday.plusDays(1);
        }
        tuesday = LocalDateTime.of(tuesday.getYear(), tuesday.getMonth(), tuesday.getDayOfMonth(),
                17, 0);
        assertThat(command.getParameter(Command.ParamName.TASK_START), is(equalTo(
                tuesday
        )));

    }

}
