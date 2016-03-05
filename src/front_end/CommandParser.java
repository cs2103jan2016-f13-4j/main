package front_end;

import objects.Command;

/**
 * Created by maianhvu on 5/3/16.
 */
class CommandParser {

    public Command parseCommand(String rawCommandString) {
        Command.Type commandType = null;
        Object[] parameters = null;

        // Determine type
        switch (rawCommandString) {
            case "add":
                commandType = Command.Type.ADD;
                break;
            case "display":
                commandType = Command.Type.DISPLAY_ALL;
                break;
            case "exit":
                commandType = Command.Type.EXIT;
                break;
            default:
                commandType = Command.Type.UNRECOGNISED;
                break;
        }

        // Determine parameters if command is recognised
        if (commandType != Command.Type.UNRECOGNISED) {
            // TODO: Parse command parameters
        }

        return new Command(commandType, parameters);
    }

}
