package component.front_end.ui;

import component.front_end.ui.core.IndependentView;

/**
 * Created by maianhvu on 8/3/16.
 */
public class CommandPromptView extends IndependentView {

    @Override
    public void buildContent() {
        this.addText("command: ");
    }
}
