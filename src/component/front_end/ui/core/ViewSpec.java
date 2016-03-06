package component.front_end.ui.core;

/**
 * Created by maianhvu on 6/3/16.
 */
public interface ViewSpec {
    /**
     * Returns the content to be displayed
     */
    String getContent();

    /**
     * Populates the content of the view
     */
    void buildContent();

    /**
     * Returns the data attached with the view
     */
    Object getViewData();
}
