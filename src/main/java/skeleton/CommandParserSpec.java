package skeleton;

import shared.Command;

/**
 * @@author Thenaesh Elango
 */
public interface CommandParserSpec {

    Command parse(String commandString);
}
