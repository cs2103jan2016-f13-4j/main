package skeleton;

import shared.Command;

import java.util.Set;

/**
 * @@author A0124772E
 */
public interface CommandParserSpec {

    void initialise();

    Command parse(String commandString);
}
