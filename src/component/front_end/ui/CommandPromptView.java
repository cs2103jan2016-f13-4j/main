package component.front_end.ui;

import component.front_end.ui.core.IndependentView;

/**
 * @@author Mai Anh Vu
 */
public class CommandPromptView extends IndependentView {

    /**
     * Constants
     */
    private static final String STRING_COMMAND_PROMPT = "command: ";

    @Override public void buildContent() {
        this.addLine(); // Buffer a new line to separate command prompt
        this.addText(STRING_COMMAND_PROMPT);
    }
}
