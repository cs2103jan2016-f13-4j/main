package front_end.ui.utility;

import front_end.ui.core.IndependentUI;

/**
 * Created by maianhvu on 5/3/16.
 */
public class CommandPromptUI extends IndependentUI {

    private static final String STRING_COMMAND_PROMPT = "command: ";

    @Override
    public void render() {
        super.render();
        this.display(STRING_COMMAND_PROMPT);
    }
}
