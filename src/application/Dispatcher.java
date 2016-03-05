package application;

import back_end.*;
import front_end.*;


/**
 * The Dispatcher is the master controller that drives the (independent) front-end and back-end.
 * Its function is analogous to that of the human heart with respect to pulmonary and systemic circulation.
 * 
 * created by thenaesh on 5/3/16
 *
 */
public class Dispatcher {
    private DecisionEngineSpec decisionEngine = null;
    private TranslationEngineSpec translationEngine = null;
    
    /*
     * 1) pulls a command in from the front-end
     * 2) pushes it to the back-end for processing
     * 3) receives the resulting task list and other instructions from the back-end
     * 4) pushes the instructions to the front end
     */
    public void heartbeat() {
    }
}
