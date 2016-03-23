package skeleton;

import shared.Command;
import shared.ExecutionResult;

import java.util.function.Function;

/**
 * @@author Mai Anh Vu
 */
public interface TranslationEngineSpec {
    UserInterfaceSpec getUserInterface();

    CommandParserSpec getCommandParser();

    void setCommandExecutionHandler(Function<Command, ExecutionResult> handler);

    void initialise();

    void displayResult(ExecutionResult result);

    void shutdown();
}
