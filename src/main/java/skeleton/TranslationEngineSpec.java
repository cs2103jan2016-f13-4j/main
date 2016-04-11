package skeleton;

import shared.Command;
import shared.ExecutionResult;

import java.util.function.Function;

/**
 *
 * @@author A0127046L
 */
public interface TranslationEngineSpec {
    UserInterfaceSpec getUserInterface();

    CommandParserSpec getCommandParser();

    void setCommandExecutionHandler(Function<Command, ExecutionResult> handler);

    void initialise();

    void displayResult(ExecutionResult result);

    void shutdown();
}
