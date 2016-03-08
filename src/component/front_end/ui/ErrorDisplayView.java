package component.front_end.ui;

import component.front_end.ui.core.View;
import utility.BoxBuildingUtil;

import javax.swing.*;

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
        this.addLine(BoxBuildingUtil.borderTop(SIZE_WIDTH_BOX));
        this.addLine(BoxBuildingUtil.wrapString("[!] Error", SIZE_WIDTH_BOX));
        this.addLine(BoxBuildingUtil.borderMiddle(SIZE_WIDTH_BOX));
        this.addLine(BoxBuildingUtil.wrapLongText((String) this.getViewData(), SIZE_WIDTH_BOX));
        this.addLine(BoxBuildingUtil.borderBottom(SIZE_WIDTH_BOX));
    }
}
