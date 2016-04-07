package logic;

import org.junit.Before;
import org.junit.Test;

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
    public void Command_parser_reads_JSON_definitions_correctly() {

    }
}
