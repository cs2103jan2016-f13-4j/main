package logic;

import exception.InvalidParameterException;
import shared.*;
import skeleton.CommandParserSpec;

import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser implements CommandParserSpec {

    private static final Pattern PATTERN_COMMAND_PARSER = Pattern.compile(
            "(\\w+)(?::(\"[^\"]+\"|[^\"\\s]+))?\\s?"
    );
    private static final char CHARACTER_QUOTATION_MARK = '"';
    private static final String STRING_EMPTY = "";

    /**
     * Singleton instance
     */
    private static CommandParser instance;

    /**
     * TODO: Write JavaDoc
     */
    private CommandParser() {}

    /**
     * TODO: Write JavaDoc
     *
     * @return
     */
    public static CommandParser getInstance() {
        if (instance == null) {
            return instance = new CommandParser();
        }
        return instance;
    }

    /**
     * TODO: Write JavaDoc
     *
     * @param commandString
     *
     * @return
     */
    @Override public Command parse(String commandString) {
        // Prepare the instruction and the parameter list
        Instruction instruction = null;
        ParameterList paramList = new ParameterList();

        // Split the command into discrete chunks
        LinkedHashMap<String, String> commandComponents = splitCommand(commandString);

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
            // But first, we need to check that the key is a legit parameter type
            ParameterName paramName = null;
            ParameterValue paramValue = null;
            try {
                paramName = ParameterName.parseParamName(key);
                paramValue = ParameterValue.parseParamValue(value, paramName);
            } catch (InvalidParameterException e) {
                // TODO: Add a proper exception handler for this; an assertion is not the way to go
                assert false;
            }
            paramList.addParameter(paramName, paramValue);
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

        return !(string.length() < 2 ||
                string.charAt(0) != CHARACTER_QUOTATION_MARK ||
                string.charAt(string.length() - 1) != CHARACTER_QUOTATION_MARK);

    }

    private static String stripExtremeCharacters(String string) {
        assert(string != null);

        if (string.length() < 2) {
            return STRING_EMPTY;
        }

        return string.substring(1, string.length() - 1);
    }

}
