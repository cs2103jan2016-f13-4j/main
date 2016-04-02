package skeleton;

import shared.Command;

import java.util.Set;

/**
 * @@author Thenaesh Elango
 */
public interface CommandParserSpec {

    Command parse(String commandString);
}
