import back_end.DecisionEngine;
import front_end.TranslationEngine;

/**
 * Created by maianhvu on 5/3/16.
 */
public class Dispatcher {

    private TranslationEngine translationEngine_;
    private DecisionEngine decisionEngine_;

    public Dispatcher() {
        this.translationEngine_ = new TranslationEngine();
        this.decisionEngine_ = new DecisionEngine();
    }
}
