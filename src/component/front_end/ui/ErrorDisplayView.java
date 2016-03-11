package component.front_end.ui;

import component.front_end.ui.core.View;
import utility.BoxBuilder;

/**
 * Created by maianhvu on 8/3/16.
 */
public class ErrorDisplayView extends View<String> {

    public static final int SIZE_WIDTH_BOX = 30;

    public ErrorDisplayView(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public void buildContent() {
        this.addLine(BoxBuilder.borderTop(SIZE_WIDTH_BOX));
        this.addLine(BoxBuilder.wrapString("[!] Error", SIZE_WIDTH_BOX));
        this.addLine(BoxBuilder.borderMiddle(SIZE_WIDTH_BOX));
        this.addLine(BoxBuilder.wrapLongText((String) this.getViewData(), SIZE_WIDTH_BOX));
        this.addLine(BoxBuilder.borderBottom(SIZE_WIDTH_BOX));
    }
}
