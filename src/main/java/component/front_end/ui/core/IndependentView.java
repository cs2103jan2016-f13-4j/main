package component.front_end.ui.core;

import component.front_end.ui.core.View;

/**
 * Created by maianhvu on 8/3/16.
 */
public abstract class IndependentView extends View<Object> {
    protected IndependentView() {
        super(null);
    }

    @Override
    public void buildContent() {}
}
