package skeleton.front_end;

import component.front_end.ui.core.UserInterfaceSpec;
import entity.*;
import entity.command.Command;


/**
 * The Translation Engine has the duty of
 *      1) Getting user commands from the UI upon the order of the Dispatcher,
 *              properly formatted as a Command object
 *      2) Instructing the UI to render the results (of executing a command) sent to it,
 *              in an appropriate manner
 * 
 * It has knowledge of the Command Parser and delegates to it to parse the user input string.
 * 
 * Rank: Captain (reports to MAJ Dispatcher)
 * 
 * created by thenaesh on Mar 6, 2016
 *
 */
public abstract class TranslationEngineSpec {
    protected abstract CommandParserSpec getCommandParser();
    protected abstract UserInterfaceSpec getUserInterface();
    
    /**
     * decides what to to with an execution result sent by the Dispatcher
     * instructs the UI to carry out that decision
     * 
     * @param result
     */
    public abstract void display(ExecutionResult<?> result);
    
    /**
     * 1) instructs the UI to get a user command string (blocking if necessary)
     * 2) sends the user command string to the Command Parser, getting back a Command object
     * 
     * @return the Command object
     */
    public abstract Command getNextCommand();
}