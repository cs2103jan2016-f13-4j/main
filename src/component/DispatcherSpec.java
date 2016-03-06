package component;

import component.back_end.DecisionEngineSpec;
import component.front_end.TranslationEngineSpec;


/**
 * The Dispatcher is the master controller that drives the (otherwise independent) front-end and back-end.
 * Its function is analogous to that of the human heart with respect to pulmonary and systemic circulation.
 * 
 * Rank: Major (reports to LTC Launcher)
 * 
 * created by thenaesh on 5/3/16
 *
 */
public interface DispatcherSpec {
    DecisionEngineSpec getDecisionEngine();
    TranslationEngineSpec getTranslationEngine();

    /*
     * 1) pulls a command in from the front-end
     * 2) pushes it to the back-end for processing
     * 3) receives the resulting task list and other instructions from the back-end
     * 4) pushes the instructions to the front end
     */
    void pulse();
}
