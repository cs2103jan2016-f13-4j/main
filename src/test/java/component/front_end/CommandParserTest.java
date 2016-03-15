package component.front_end;

import component.front_end.CommandParser;
import entity.command.Command;
import entity.command.Instruction;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by maianhvu on 6/3/16.
 */
public class CommandParserTest {

    private CommandParser parser_;

    @Before
    public void setUp() {
        this.parser_ = new CommandParser();
    }

    @Test
    public void Command_parser_splits_raw_command_string_into_correct_elements() {
        LinkedHashMap<String, String> commandWords = this.parser_.splitCommand("display:all task:\"Hello World!\" date");
        assertThat(commandWords.get("display"), is(equalTo("all")));
        assertThat(commandWords.get("task"), is(equalTo("Hello World!")));
        assertThat(commandWords.containsKey("date"), is(true));
        assertThat(commandWords.get("date"), is(nullValue()));
    }

    @Test
    public void Command_parser_correctly_recognizes_add_command() {
        assertThat(this.parser_.parseCommand("add").getInstruction().getType(), is(Instruction.Type.ADD));
    }

    @Test
    public void Command_parser_correctly_recognizes_display_all_command() {
        String[] displayAllCommands = new String[] {
                "display", "display:", "display:all"
        };

        for (String commandString : displayAllCommands) {
            Command displayAll = this.parser_.parseCommand(commandString);
            assertThat(displayAll.getInstruction().getType(), is(Instruction.Type.DISPLAY));
            assertThat(displayAll.getInstruction().hasAllQuantifier(), is(true));
        }
    }

    @Test
    public void Command_parser_correctly_recognizes_display_one_command() {
        Command displayOne = this.parser_.parseCommand("display:6");
        assertThat(displayOne.getInstruction().getType(), is(Instruction.Type.DISPLAY));
        assertThat(displayOne.getInstruction().hasAllQuantifier(), is(false));
        assertThat(displayOne.getInstruction().getIndex(), is(6));
    }

    @Test
    public void Command_parser_correctly_recognizes_edit_command() {
        Command edit = this.parser_.parseCommand("edit:2");
        assertThat(edit.getInstruction().getType(), is(Instruction.Type.EDIT));
        assertThat(edit.getInstruction().hasAllQuantifier(), is(false));
        assertThat(edit.getInstruction().getIndex(), is(2));
    }

    @Test
    public void Command_parser_correctly_recognizes_delete_command() {
        Command delete = this.parser_.parseCommand("delete:7");
        assertThat(delete.getInstruction().getType(), is(Instruction.Type.DELETE));
        assertThat(delete.getInstruction().hasAllQuantifier(), is(false));
        assertThat(delete.getInstruction().getIndex(), is(7));
    }
}
