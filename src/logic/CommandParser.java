package logic;

/**
 * Created by Huiyie on 24/2/16.
 */

public class CommandParser {

    private static final String STRING_DELIMITER_COMMAND = "\\s+";
    private static final int COUNT_PARTS_COMMAND = 2;
    private static final int ID_PART_COMMAND_INSTRUCTION = 0;
    private static final int ID_PART_COMMAND_PARAMETER = 1;

    /**
     * Parses the raw command string, determines its instruction type and parameter
     *
     * @param rawCommand a command string
     * @return the Command object interpreted from the raw string
     */
    public static Command parse(String rawCommand) {
        String[] commandParts = splitRawCommandIntoParts(rawCommand);

        // Initialise command instruction type to null first
        Command.Type commandType = null;

        // If the command is empty, set it to unrecognised
        if (commandParts.length == 0) {
            return new Command(Command.Type.UNRECOGNISED, null);
        }

        // Determine the command instruction type
        commandType = Command.inferCommandTypeFromInstruction(commandParts[ID_PART_COMMAND_INSTRUCTION]);

        // Determine parameter
        String parameter = null;
        if (commandParts.length > ID_PART_COMMAND_PARAMETER) {
            parameter = commandParts[ID_PART_COMMAND_PARAMETER];
        }

        return new Command(commandType, parameter);
    }

    public static String[] splitRawCommandIntoParts(String rawCommand) {
        return rawCommand.trim().split(STRING_DELIMITER_COMMAND, COUNT_PARTS_COMMAND);
    }

}
