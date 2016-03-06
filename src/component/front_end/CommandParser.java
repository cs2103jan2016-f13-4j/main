package component.front_end;

import entity.command.Command;
import entity.command.Instruction;
import entity.command.ParameterList;
import sun.awt.image.ImageWatched;

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

            // Else, we treat the key and value as oridinary parameters
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

                commandComponents.put(firstPart, secondPart);
            }
        }

        return commandComponents;
    }
}
