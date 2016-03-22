package skeleton;

import logic.ExecutionResult;
import shared.Command;

import java.util.function.Function;

public interface TranslationEngineSpec {
    UserInterfaceSpec getUserInterface();

    CommandParserSpec getCommandParser();

    void setCommandExecutionHandler(Function<Command, Void> handler);

    void initialise();

    void displayResult(ExecutionResult result);
}
