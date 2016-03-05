package front_end;

import java.util.List;
import objects.*;


/**
 * The Translation Engine has the duty of
 *      1) Getting user commands from the UI upon the order of the Dispatcher,
 *              properly formatted as a Command object
 *      2) Instructing the UI to display the results (of executing a command) sent to it,
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
    void display(List<Task> taskList) {
    }
    
    Command getCommand() {
        return null;
    }
}
