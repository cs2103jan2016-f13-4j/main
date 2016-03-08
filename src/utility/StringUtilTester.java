package utility;

import component.front_end.ui.ErrorDisplayView;

import java.util.TreeMap;

/**
 * Created by maianhvu on 8/3/16.
 */
public class StringUtilTester {

    public static void main(String[] args) {
        ErrorDisplayView view = new ErrorDisplayView("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
        view.buildContent();
        System.out.println(view.getContent());
    }
}
