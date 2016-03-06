package component.front_end;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
//        List<String> commandWords = this.parser_.splitCommand("display:all task:\"Hello World!\"");
//        for (String word : commandWords) {
//            System.out.println(word);
//        }
    }
}
