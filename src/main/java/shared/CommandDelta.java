package shared;

/**
 * Created by thenaesh on 3/25/16.
 */
public class CommandDelta {
    private Task oldTask;
    private Task newTask;
    private Command command;

    public CommandDelta(Command command) {
        this.computeDelta(command);
    }

    private void computeDelta(Command command) {
    }
}
