package component.front_end;

import entity.Command;


/**
 * The Command Parser has one job: take a user input string and turn it into a Command object.
 * 
 * Rank: Lieutenant (reports to CPT Translation Engine)
 * 
 * created by thenaesh on Mar 6, 2016
 *
 */
public interface CommandParserSpec {
    
    /**
     * parses a given string into a Command object
     * this Command object may or may not be complete, depending on the string given
     * (see {@link Command} regarding completeness of Command objects)
     * 
     * @param cmdStr
     * @return the created (complete or incomplete) Command object
     */
    Command parseCommand(String cmdStr);
}
