package logic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Huiyie on 24/2/16.
 */


public class CommandParserTest {

    private static final int COMMANDS_WITHOUT_PARAMETER_EXPECTED_COMMAND_PARTS_LENGTH = 1;
    private static final int COMMANDS_WITH_PARAMETER_EXPECTED_COMMAND_PARTS_LENGTH = 2;

    @Test
    public void Empty_command_returns_unrecognised_command_type() {
        Command command = CommandParser.parse("");
        assertEquals(Command.Type.UNRECOGNISED, command.getType());
        assertNull(command.getParameter());
    }

    @Test
    public void Commands_get_split_into_instruction_and_parameter_correctly() {
        Command command = CommandParser.parse(":a cs2103 project");
        assertEquals(Command.Type.ADD, command.getType());
        assertEquals("cs2103 project", command.getParameter());
    }

    @Test
    public void Commands_with_no_parameter_have_instruction_and_no_parameter_after_parsing() {
        Command command = CommandParser.parse(":s");
        assertEquals(Command.Type.SHOW, command.getType());
        assertNull(command.getParameter());
    }

    @Test
    public void Commands_with_parameter_have_both_instruction_and_parameter_after_parsing() {
        Command command = CommandParser.parse(":e 2 duration 2h 15m");
        assertEquals(Command.Type.EDIT, command.getType());
        assertEquals("2 duration 2h 15m", command.getParameter());
    }

    @Test
    public void Commands_get_split_correctly_with_extra_spaces() {
        Command command = CommandParser.parse("   :s    from Jan 2016  ");
        assertEquals(Command.Type.SHOW, command.getType());
        assertEquals("from Jan 2016", command.getParameter());
    }

    @Test
    public void Commands_with_parameter_are_split_into_correct_number_of_segments() {
        String[] commandParts = CommandParser.splitRawCommandIntoParts(":x cs2101 oral presentation");
        assertEquals(COMMANDS_WITH_PARAMETER_EXPECTED_COMMAND_PARTS_LENGTH, commandParts.length);
    }

    @Test
    public void Commands_without_parameters_are_split_into_correct_number_of_segments() {
        String[] commandParts = CommandParser.splitRawCommandIntoParts(":s");
        assertEquals(COMMANDS_WITHOUT_PARAMETER_EXPECTED_COMMAND_PARTS_LENGTH, commandParts.length);
    }
}
