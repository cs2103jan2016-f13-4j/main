package logic;

import exception.InvalidParameterException;
import javafx.util.Pair;
import shared.*;
import skeleton.CommandParserSpec;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser implements CommandParserSpec {

    private static final Pattern PATTERN_COMMAND_PARSER = Pattern.compile(
            "(\\w+)(?::(\"[^\"]+\"|[^\"\\s]+))?\\s?"
    );
    private static final char CHARACTER_QUOTATION_MARK = '"';
    private static final String STRING_EMPTY = "";
    private static final String KEYWORD_UNIVERSAL_QUANTIFIER = "all";

    private enum InstructionKeywords {
        KEYWORDS_ADD    (Command.Instruction.ADD,     "add"),
        KEYWORDS_DISPLAY(Command.Instruction.DISPLAY, "display" ),
        KEYWORDS_EDIT   (Command.Instruction.EDIT,    "edit", "update", "modify", "change" ),
        KEYWORDS_SEARCH (Command.Instruction.SEARCH,  "search", "find" ),
        KEYWORDS_DELETE (Command.Instruction.DELETE,  "delete", "remove" ),
        KEYWORDS_MARK   (Command.Instruction.MARK,    "mark", "complete", "done" ),
        KEYWORDS_EXIT   (Command.Instruction.EXIT,    "exit" );

        final Command.Instruction instruction;
        final String[] keywords;

        InstructionKeywords(Command.Instruction inst, String... words) {
            instruction = inst;
            keywords = words;
        }
    }

    /**
     * Singleton instance
     */
    private static CommandParser instance;

    /**
     * Properties
     */
    private final HashMap<String, Command.Instruction> _instructionMap;

    /**
     * TODO: Write JavaDoc
     */
    private CommandParser() {
        this._instructionMap = constructInstructionMap();
    }

    private HashMap<String, Command.Instruction> constructInstructionMap() {
        HashMap<String, Command.Instruction> instructionMap = new HashMap<>();
        Arrays.asList(InstructionKeywords.values()).stream()
                .forEach(definition -> {
                    Arrays.asList(definition.keywords).stream()
                            .forEach(keyword -> {
                                instructionMap.put(keyword, definition.instruction);
                            });
                });
        return instructionMap;
    }

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
        Command.Instruction instruction = null;
        Integer index = null;
        boolean isUniversallyQuantified = true;
        List<Pair<Command.ParamName, Object>> parameters = new ArrayList<>();

        // Split the command into discrete chunks
        LinkedHashMap<String, String> commandComponents = splitCommand(commandString);

        // Iterate through and populate the command components
        for (String key : commandComponents.keySet()) {
            String value = commandComponents.get(key);

            // If instruction has not been initialized, then we will use the
            // current key and value to determine the instruction
            if (instruction == null) {
                instruction = this.parseInstruction(key);

                // Halt processing once the command has been confirmed to be invalid
                if (instruction == Command.Instruction.UNRECOGNISED) {
                    break;
                }

                // Try to parse quantifier
                if (value == null) continue;

                if (value.trim().toLowerCase().equals(KEYWORD_UNIVERSAL_QUANTIFIER)) {
                    isUniversallyQuantified = true;
                } else try {
                    index = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    // Do nothing
                }
                continue;
            }

            // Else, we treat the key and value as ordinary parameters
            // But first, we need to check that the key is a legit parameter type
            Command.ParamName paramName = this.parseParamName(key);
            if (paramName == null) continue; // Ignore invalid param name
            Object paramValue = null;
            switch (paramName.type) {
                case STRING:
                    paramValue = value;
                    break;
                case DATE:
                    paramValue = StringParser.asDateTime(value);
                    break;
                case INTEGER:
                    try {
                        paramValue = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        instruction = Command.Instruction.INVALID;
                    }
                    break;
            }

            // Account for the case where we had trouble parsing the param value
            if (instruction == Command.Instruction.INVALID) {
                break;
            }

            // Add the parameter to the list
            parameters.add(new Pair<>(paramName, paramValue));
        }

        // Ensure that the instruction at least has a value
        if (instruction == null) {
            instruction = Command.Instruction.UNRECOGNISED;
        }

        // Create the command and return it
        Command command = new Command(instruction, index, isUniversallyQuantified);
        parameters.stream().forEach(param -> command.setParameter(param.getKey(), param.getValue()));
        return command;
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

    private Command.Instruction parseInstruction(String string) {
        string = string.trim().toLowerCase();
        if (!this._instructionMap.containsKey(string)) {
            return Command.Instruction.UNRECOGNISED;
        }
        return this._instructionMap.get(string);
    }

    private Command.ParamName parseParamName(String string) {
        string = string.trim().toLowerCase();
        switch (string) {
            case "name":
                return Command.ParamName.TASK_NAME;
            case "description":
                return Command.ParamName.TASK_DESCRIPTION;
            case "from":
                return Command.ParamName.TASK_START;
            case "to":
                return Command.ParamName.TASK_END;
            case "query":
                return Command.ParamName.SEARCH_QUERY;
            default:
                return null;
        }
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
