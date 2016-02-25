package logic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Huiyie on 24/2/16.
 */

public class CommandTest {

    private static final String[] STRINGS_COMMAND_VALID = new String[]{
            ":add", ":a", ":show", ":s", ":edit", ":e", ":delete", ":x"
    };


    @Test
    public void Valid_commands_get_recognised_correctly() {
        for (String commandString : STRINGS_COMMAND_VALID) {
            Command.Type commandType = Command.inferCommandTypeFromInstruction(commandString);
            assertNotEquals(Command.Type.UNRECOGNISED, commandType);
        }
    }

    @Test
    public void Invalid_commands_are_flagged_as_unrecognised() {
        final String[] invalidCommands = new String[]{
                "add", "a", "show", "s", "edit", "e", "delete", "x",
                "insert", "display", "change", "remove", "clear", "quit", ""
        };
        for (String commandString : invalidCommands) {
            Command.Type commandType = Command.inferCommandTypeFromInstruction(commandString);
            assertEquals(Command.Type.UNRECOGNISED, commandType);
        }
    }

    @Test
    public void Add_commands_get_recognised_correctly() {
        Command.Type commandType = Command.inferCommandTypeFromInstruction(":a");
        assertEquals(Command.Type.ADD, commandType);

        commandType = Command.inferCommandTypeFromInstruction(":add");
        assertEquals(Command.Type.ADD, commandType);
    }

    @Test
    public void Show_commands_get_recognised_correctly() {
        Command.Type commandType = Command.inferCommandTypeFromInstruction(":s");
        assertEquals(Command.Type.SHOW, commandType);

        commandType = Command.inferCommandTypeFromInstruction(":show");
        assertEquals(Command.Type.SHOW, commandType);
    }

    @Test
    public void Edit_commands_get_recognised_correctly() {
        Command.Type commandType = Command.inferCommandTypeFromInstruction(":e");
        assertEquals(Command.Type.EDIT, commandType);

        commandType = Command.inferCommandTypeFromInstruction(":edit");
        assertEquals(Command.Type.EDIT, commandType);
    }

    @Test
    public void Delete_commands_get_recognised_correctly() {
        Command.Type commandType = Command.inferCommandTypeFromInstruction(":x");
        assertEquals(Command.Type.DELETE, commandType);

        commandType = Command.inferCommandTypeFromInstruction(":delete");
        assertEquals(Command.Type.DELETE, commandType);
    }

    @Test
    public void Valid_commands_in_different_casing_still_get_recognised_correctly() {
        for (String commandString : STRINGS_COMMAND_VALID) {
            commandString = commandString.toUpperCase();
            Command.Type commandType = Command.inferCommandTypeFromInstruction(commandString);
            assertNotEquals(Command.Type.UNRECOGNISED, commandType);
        }
    }

}