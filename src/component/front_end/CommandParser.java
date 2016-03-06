package component.front_end;

import entity.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by maianhvu on 6/3/16.
 */
public class CommandParser implements CommandParserSpec {

    private static final Pattern PATTERN_COMMAND_PARSER = Pattern.compile(
            "(\\w+)(?::(\"[^\"]+\"|[^\"\\s]+))?\\s?"
            );

    @Override
    public Command parseCommand(String rawCommandString) {
        return null;
    }

    List<String> splitCommand(String rawCommandString) {
        Matcher matcher = PATTERN_COMMAND_PARSER.matcher(rawCommandString.trim());
        ArrayList<String> commandWords = new ArrayList<>();
        while (matcher.find()) {
            Matcher innerMatcher = PATTERN_COMMAND_PARSER.matcher(matcher.group());

            while (innerMatcher.find()) {
                commandWords.add(innerMatcher.group(1));
                commandWords.add(innerMatcher.group(2));
            }
        }
        return commandWords;
    }
}
