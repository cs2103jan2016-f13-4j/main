package skeleton;

/**
 * @@author Thenaesh Elango
 */
public interface DispatcherSpec {

    TranslationEngineSpec getTranslationEngine();

    DecisionEngineSpec getDecisionEngine();

    void initialise();

    void start();

    void shutdown();
}
