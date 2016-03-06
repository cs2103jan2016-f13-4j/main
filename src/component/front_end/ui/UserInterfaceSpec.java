package component.front_end.ui;

import entity.View;


/**
 * The User Interface renders a ({@link View}) and then prompts for user input
 *
 * Rank: Staff Sergeant (reports to CPT Translation Engine)
 * 
 * created by thenaesh on Mar 6, 2016
 */
public interface UserInterfaceSpec {
    
    /**
     * Renders the content from the view
     * 
     * @param viewToRender
     */
     void render(View viewToRender);

    /**
     * First, renders a CommandPromptView
     *
     * Then, uses internal scanner
     * to get the nextLine() from user input
     * @return the string read from
     */
    String queryInput();
}
