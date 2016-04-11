package skeleton;

/**
 * @@author A0124772E
 */
public interface DispatcherSpec {

    TranslationEngineSpec getTranslationEngine();

    DecisionEngineSpec getDecisionEngine();

    void initialise();

    void start();

    void shutdown();
}
