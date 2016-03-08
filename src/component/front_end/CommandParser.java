package component.front_end;

import entity.command.Command;
import entity.command.Instruction;
import entity.command.ParameterList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by maianhvu on 6/3/16.
 */
public class CommandParser extends CommandParserSpec {

    private static final Pattern PATTERN_COMMAND_PARSER = Pattern.compile(
            "(\\w+)(?::(\"[^\"]+\"|[^\"\\s]+))?\\s?"
            );
    public static final char CHARACTER_QUOTATION_MARK = '"';
    public static final String STRING_EMPTY = "";

    @Override
    public Command parseCommand(String rawCommandString) {
        // Prepare the instruction and the parameter list
        Instruction instruction = null;
        ParameterList paramList = new ParameterList();

        // Split the command into discrete chunks
        LinkedHashMap<String, String> commandComponents = splitCommand(rawCommandString);

        // Iterate through and populate the command components
        for (String key : commandComponents.keySet()) {
            String value = commandComponents.get(key);

            // If instruction has not been initialized, then we will use the
            // current key and value to determine the instruction
            if (instruction == null) {
                instruction = new Instruction(key, value);
                continue;
            }

            // Else, we treat the key and value as ordinary parameters
            paramList.addParameter(key, value);
        }

        // Ensure that the instruction at least has a value
        if (instruction == null) {
            instruction = new Instruction(Instruction.Type.INVALID);
        }

        return new Command(instruction, paramList);
    }

    LinkedHashMap<String, String> splitCommand(String rawCommandString) {
        // Prepare linked hash map for storing command words
        LinkedHashMap<String, String> commandComponents = new LinkedHashMap<>();

        // Start matching the whole string against words of the command string
        Matcher matcher = PATTERN_COMMAND_PARSER.matcher(rawCommandString.trim());
        ArrayList<String> commandWords = new ArrayList<>();

        // Outer loop to find all the command words
        while (matcher.find()) {

            // Match with pattern again to find parameters
            Matcher innerMatcher = PATTERN_COMMAND_PARSER.matcher(matcher.group());

            // Inner loop to split command components
            while (innerMatcher.find()) {
                String firstPart = innerMatcher.group(1);
                String secondPart = innerMatcher.group(2);
                assert(firstPart != null);

                // Attempt to strip quotes from second part if there are
                if (secondPart != null && isSurroundedByQuotes(secondPart)) {
                    secondPart = stripExtremeCharacters(secondPart);
                }

                commandComponents.put(firstPart, secondPart);
            }
        }

        return commandComponents;
    }

    /**
     * Helper methods
     */
    private static boolean isSurroundedByQuotes(String string) {
        assert(string != null);

        if (string.length() < 2 ||
            string.charAt(0) != CHARACTER_QUOTATION_MARK ||
            string.charAt(string.length() - 1) != CHARACTER_QUOTATION_MARK) {
            return false;
        }

        return true;
    }

    private static String stripExtremeCharacters(String string) {
        assert(string != null);

        if (string.length() < 2) {
            return STRING_EMPTY;
        }

        return string.substring(1, string.length() - 1);
    }
}
