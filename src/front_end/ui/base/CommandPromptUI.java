package front_end.ui.base;

import back_end.storage.base.Relation;
import front_end.ui.core.UserInterface;

/**
 * Created by maianhvu on 5/3/16.
 */
public class CommandPromptUI extends UserInterface<Relation> {


    public static final String STRING_COMMAND_PROMPT = "command: ";

    public CommandPromptUI() {
        super(null);
    }

    @Override
    public void render() {
        super.render();
        this.display(STRING_COMMAND_PROMPT);
    }
}
