package logic;

import shared.Command;
import skeleton.CommandParserSpec;

/**
 * Created by maianhvu on 31/03/2016.
 */
public class FlexiCommandParser implements CommandParserSpec {

    /**
     * Singleton implementation
     */
    private static final FlexiCommandParser instance = new FlexiCommandParser();
    public static FlexiCommandParser getInstance() { return instance; }

    /**
     * Properties
     */
    private FlexiCommandParser() {

    }

    @Override
    public Command parse(String commandString) {
        return null;
    }
}
