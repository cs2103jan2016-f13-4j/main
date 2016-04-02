package logic;

import org.junit.Before;
import org.junit.Test;
import shared.Command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by maianhvu on 01/04/2016.
 */
public class FlexiCommandParserTest {

    private FlexiCommandParser _parser;

    @Before public void setUp() {
        this._parser = FlexiCommandParser.getInstance();
        this._parser.initialize();
    }

    @Test public void FlexiCommandParser_reads_JSON_data_correctly() {
        System.out.println(this._parser.getInstructions());
        System.out.println(this._parser.getTimeClauses());
    }

    @Test public void FlexiCommandParser_parses_add_command_correctly() {
        String commandString = "add go to the gym on Tuesday with high priority";
        Command command = this._parser.parse(commandString);
        assertThat(command.getInstruction(), is(Command.Instruction.ADD));
        assertThat(command.getParameter(Command.ParamName.TASK_NAME),
                is(equalTo("go to the gym")));
    }

}
