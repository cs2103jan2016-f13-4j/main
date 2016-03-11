package skeleton;

import skeleton.back_end.DecisionEngineSpec;
import skeleton.front_end.TranslationEngineSpec;


/**
 * The Dispatcher is the master controller that drives the (otherwise independent) front-end and back-end.
 * Its function is analogous to that of the human heart with respect to pulmonary and systemic circulation.
 * 
 * Rank: Major (reports to LTC Launcher)
 * 
 * created by thenaesh on 5/3/16
 *
 */
public abstract class DispatcherSpec {
    protected abstract DecisionEngineSpec getDecisionEngine();
    protected abstract TranslationEngineSpec getTranslationEngine();

    /*
     * 1. Starts a the application with an initial command
     * 2. Enter programme loop until command is exit
     *    2.1 Pass the command to back end
     *    2.2 Decision engine returns execution result
     *    2.3 Pass the execution result to translation engine
     *    2.4 Translation engine returns the next command to be executed
     */
    public abstract void pulse();
}
