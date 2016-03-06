package component;

import component.back_end.*;
import component.front_end.*;


/**
 * The Dispatcher is the master controller that drives the (otherwise independent) front-end and back-end.
 * Its function is analogous to that of the human heart with respect to pulmonary and systemic circulation.
 * 
 * Rank: Major (reports to LTC Launcher)
 * 
 * created by thenaesh on 5/3/16
 *
 */
public class DispatcherSpec {
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
