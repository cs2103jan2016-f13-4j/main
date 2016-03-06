package component.front_end.ui;

import entity.*;


/**
 * The User Interface has two jobs:
 *      1) render a view ({@link View})
 *      2) get a user command string
 * 
 * The building of the view is the job of the Translation Engine; this class should not care about it.
 * 
 * Rank: Staff Sergeant (reports to CPT Translation Engine)
 * 
 * created by thenaesh on Mar 6, 2016
 *
 */
public abstract class UserInterfaceSpec {
    
    /**
     * renders the supplied view
     * 
     * @param viewToRender
     */
    public abstract void render(View viewToRender);
    
    /**
     * gets an input string directly from the user
     * Note that this method does not do any parsing; that is the job of the Command Parser
     * 
     * @return user input string
     */
    public abstract String getUserInputString();
}
