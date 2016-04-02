package skeleton;

import shared.Command;

import java.util.Set;

/**
 * @@author Thenaesh Elango
 */
public interface CommandParserSpec {

    void initialise();

    Command parse(String commandString);
}
