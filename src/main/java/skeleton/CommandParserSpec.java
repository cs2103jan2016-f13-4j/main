package skeleton;

import shared.Command;

public interface CommandParserSpec {

    Command parse(String commandString);
}
