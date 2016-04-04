package logic;

import org.junit.Before;
import org.junit.Test;
import shared.Command;
import shared.CustomTime;
import shared.Task;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @@author Mai Anh Vu
 */
public class FlexiCommandParserTest {

    private FlexiCommandParser _parser;

    @Before public void setUp() {
        this._parser = FlexiCommandParser.getInstance();
        this._parser.initialise();
    }

    @Test public void FlexiCommandParser_creates_correct_instruction_pattern() {
        final Pattern instructionPattern = Pattern.compile(this._parser.getInstructionPattern(), Pattern.CASE_INSENSITIVE);
        String command = "   ADD a new thing";
        Matcher m = instructionPattern.matcher(command.trim());
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
        tuesday = tuesday.withHour(17).truncatedTo(ChronoUnit.HOURS);
        assertThat(command.getParameter(Command.ParamName.TASK_START), is(equalTo(
                tuesday
        )));
    }

    @Test public void FlexiCommandParser_parses_complex_add_commands_correctly() {
        String commandString = "add Eat Da Poo Poo from today 1030 until next Fri's 1am";
        Command command = this._parser.parse(commandString);
        assertThat(command.getInstruction(), is(Command.Instruction.ADD));
        assertThat(command.getParameter(Command.ParamName.TASK_NAME),
                is(equalTo("Eat Da Poo Poo")));
        assertThat(command.getParameter(Command.ParamName.TASK_START),
                is(equalTo(LocalDateTime.now().withHour(10).withMinute(30).truncatedTo(ChronoUnit.MINUTES)))
        );
    }

    @Test public void FlexiCommandParser_parses_time_nouns_without_prepositions_correctly() {
        String commandString = "add talk cock sing song tomorrow";
        Command command = this._parser.parse(commandString);

        assertThat(command.getInstruction(), is(Command.Instruction.ADD));
        assertThat(command.getParameter(Command.ParamName.TASK_NAME),
                is(equalTo("talk cock sing song")));
        assertThat(command.getParameter(Command.ParamName.TASK_END),
                is(equalTo(CustomTime.tomorrowAt(null))));
    }

    @Test public void FlexiCommandParser_parses_PM_time_correctly() {
        String commandString = "add talk cock sing song from tomorrow to next week's Thursday 1300";
        Command command = this._parser.parse(commandString);

        assertThat(command.getInstruction(), is(Command.Instruction.ADD));
        assertThat(command.getParameter(Command.ParamName.TASK_NAME),
                is(equalTo("talk cock sing song")));
        LocalDateTime thursday = LocalDateTime.now();
        int passes = 1;
        if (thursday.getDayOfWeek().getValue() >= DayOfWeek.THURSDAY.getValue()) {
            passes = 0;
        }
        while (!thursday.getDayOfWeek().equals(DayOfWeek.THURSDAY) || passes > 0) {
            if (thursday.getDayOfWeek().equals(DayOfWeek.THURSDAY)) passes--;
            thursday = thursday.plusDays(1);
        }
        thursday = thursday.withHour(13).withMinute(0).truncatedTo(ChronoUnit.MINUTES);
        assertThat(command.getParameter(Command.ParamName.TASK_END),
                is(equalTo(thursday)));

    }

    @Test public void FlexiCommandParser_parses_edit_task_correctly() {
        String commandString = "edit task number 32 get down on it from today 2359 to tomorrow 1pm";
        Command command = this._parser.parse(commandString);

        assertThat(command.getInstruction(), is(Command.Instruction.EDIT));
        assertThat(command.getParameter(Command.ParamName.TASK_INDEX), is(equalTo(32)));
        assertThat(command.getParameter(Command.ParamName.TASK_NAME), is(equalTo("get down on it")));
        assertThat(command.getParameter(Command.ParamName.TASK_START), is(equalTo(
                LocalDateTime.now().withHour(23).withMinute(59).truncatedTo(ChronoUnit.MINUTES)
        )));
        assertThat(command.getParameter(Command.ParamName.TASK_END), is(equalTo(
                LocalDateTime.now().plusDays(1).withHour(13).truncatedTo(ChronoUnit.HOURS)
        )));
    }

    @Test public void FlexiCommandParser_parses_edit_with_fillers_correctly() {
        String commandString = "edit task number 32 change to get down on it from today 2359 to tomorrow 1pm";
        Command command = this._parser.parse(commandString);
        assertThat(command.getParameter(Command.ParamName.TASK_NAME), is(equalTo("get down on it")));
    }

    @Test public void FlexiCommandParser_parses_edit_with_fake_fillers_correctly() {
        String commandString = "edit task number 32 \"change to get down on it\" from today 2359 to tomorrow 1pm";
        Command command = this._parser.parse(commandString);
        assertThat(command.getParameter(Command.ParamName.TASK_NAME), is(equalTo("change to get down on it")));
    }

    @Test public void FlexiCommandParser_parses_edit_without_task_name_correctly() {
        String commandString = "edit task number 10 to starting same day 10pm";
        Command command = this._parser.parse(commandString);
        assertThat(command.getParameter(Command.ParamName.TASK_NAME), is(nullValue()));
        CustomTime newTime = new CustomTime(null, LocalTime.of(22,0));
        assertThat(command.getParameter(Command.ParamName.TASK_START), is(equalTo(newTime)));
    }

    @Test public void FlexiCommandParser_parses_universally_delete_command_correctly() {
        String commandString = "delete all tasks";
        Command command = this._parser.parse(commandString);
        assertThat(command.getInstruction(), is(Command.Instruction.DELETE));
        assertThat(command.hasTrueValue(Command.ParamName.TASK_UNIVERSALLY_QUANTIFIED), is(true));
        assertThat(command.getParameter(Command.ParamName.TASK_INDEX), is(nullValue()));
    }

    @Test public void FlexiCommandParser_parses_specific_date_correctly() {
        String commandString = "add take out the trash from 4 mar to next week's Thursday";
        Command command = this._parser.parse(commandString);
        assertThat(command.getInstruction(), is(Command.Instruction.ADD));

        CustomTime fourthMarch = new CustomTime(LocalDate.of(
                LocalDate.now().getYear(),
                Month.MARCH,
                4
        ), null);
        assertThat(command.getParameter(Command.ParamName.TASK_START),
                is(equalTo(fourthMarch)));
        assertThat(command.getParameter(Command.ParamName.TASK_END),
                is(equalTo(CustomTime.todayAt(null).next(DayOfWeek.THURSDAY))));
    }

    @Test public void FlexiCommandParser_parses_specific_date_with_time_correctly() {
        String commandString = "add go to the gym from Mar 25th 2016's 7pm to 8 apr 330";
        Command command = this._parser.parse(commandString);

        CustomTime startTime = new CustomTime(
                LocalDate.of(2016, Month.MARCH, 25),
                LocalTime.of(19, 0),
                ChronoUnit.HOURS
        );

        CustomTime endTime = new CustomTime(
                LocalDate.of(LocalDate.now().getYear(), Month.APRIL, 8),
                LocalTime.of(3,30),
                ChronoUnit.MINUTES
        );

        assertThat(command.getParameter(Command.ParamName.TASK_START),
                is(equalTo(startTime)));
        assertThat(command.getParameter(Command.ParamName.TASK_END),
                is(equalTo(endTime)));
    }

    @Test public void FlexiCommandParser_parses_simple_priority_correctly() {
        String commandString = "add go to the gym today with high priority";
        Command command = this._parser.parse(commandString);
        assertThat(command.getParameter(Command.ParamName.PRIORITY_VALUE),
                is(Task.Priority.HIGH));
    }

    @Test public void FlexiCommandParser_parses_complex_priority_correctly() {
        String commandString = "add go to the mall from tomorrow's 5pm to next week's Fri 1000 as unimportant";
        Command command = this._parser.parse(commandString);
        assertThat(command.getParameter(Command.ParamName.PRIORITY_VALUE),
                is(Task.Priority.LOW));
    }

    @Test public void FlexiCommandParser_parses_display_with_parameters_correctly() {
        String commandString = "display all tasks from today 5pm until 25th May 2016";
        Command command = this._parser.parse(commandString);

        assertThat(command.getInstruction(), is(Command.Instruction.DISPLAY));

        CustomTime start = CustomTime.todayAt(LocalTime.of(17, 0));
        CustomTime end = new CustomTime(LocalDate.of(2016, Month.MAY, 25), null);
        assertThat(command.getParameter(Command.ParamName.TASK_START),
                is(equalTo(start)));
        assertThat(command.getParameter(Command.ParamName.TASK_END),
                is(equalTo(end)));
        assertThat(command.hasTrueValue(Command.ParamName.TASK_UNIVERSALLY_QUANTIFIED), is(true));
    }

}
