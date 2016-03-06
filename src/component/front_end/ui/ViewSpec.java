package component.front_end.ui;

/**
 * Created by maianhvu on 6/3/16.
 */
public interface ViewSpec {
    /**
     * Returns the content to be displayed
     */
    String getContent();

    /**
     * Returns the data attached with the view
     */
    Object getViewData();
}
