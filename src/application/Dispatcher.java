package application;

import back_end.DecisionEngine;
import front_end.TranslationEngine;
import objects.Command;
import objects.ExecutionResult;
import stubs.StubbedDecisionEngine;

/**
 * Created by maianhvu on 5/3/16.
 */
class Dispatcher {

    private static final boolean NO_SKIP_INPUT = false;

    private final TranslationEngine translationEngine_;
    private final DecisionEngine decisionEngine_;

    public Dispatcher() {
        this.translationEngine_ = new TranslationEngine(NO_SKIP_INPUT); // Does not skip input
        this.decisionEngine_ = new StubbedDecisionEngine();
    }

    private static boolean isTerminateCommand(Command command) {
        return command.getType() == Command.Type.EXIT;
    }

    public void start() {
        Command nextCommand = Command.getInitialCommand();

        while (!isTerminateCommand(nextCommand)) {
            ExecutionResult result = this.decisionEngine_.performCommand(nextCommand);
            nextCommand = this.translationEngine_.displayAndParseCommand(result);
        }
    }
}
