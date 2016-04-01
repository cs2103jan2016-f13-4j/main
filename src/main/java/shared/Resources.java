package shared;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.util.Pair;

/**
 * @@author Mai Anh Vu
 */
public class Resources {
    /**
     * Constants
     */
    private static final String STRING_EXTENSION_TEMPLATES = ".fxml";
    private static final String STRING_EXTENSION_STYLESHEETS = ".css";
    private static final String STRING_EXTENSION_FONTS = ".ttf";

    private static final String STRING_PATH_TEMPLATES = "templates/";
    private static final String STRING_PATH_FONTS = "fonts/";
    private static final String STRING_PATH_STYLESHEETS = "stylesheets/";

    /**
     * Properties
     */
    private final ClassLoader _classLoader;

    /**
     * Singleton class implementation
     */
    private static Resources instance = new Resources();

    public static Resources getInstance() {
        return instance;
    }

    private Resources() {
        this._classLoader = this.getClass().getClassLoader();
    }

    /**
     * Builds a JavaFX view from the specified FXML template.
     *
     * @param template
     *            the template name (without extension)
     *
     * @return null if template not found
     */
    public <T> T getComponent(String template) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getTemplateUrl(template));
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T, C> Pair<T, C> getComponentAndController(String template) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getTemplateUrl(template));

            T component = loader.load();
            C controller = loader.getController();

            return new Pair<>(component, controller);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private URL getTemplateUrl(String template) {
        String fullPath = String.format("%s%s%s", STRING_PATH_TEMPLATES, template, STRING_EXTENSION_TEMPLATES);
        URL resourceUrl = this._classLoader.getResource(fullPath);
        return resourceUrl;
    }

    public String getFontUrl(String font) {
        String fontPath = String.format("%s%s%s", STRING_PATH_FONTS, font, STRING_EXTENSION_FONTS);
        return this._classLoader.getResource(fontPath).toExternalForm();
    }

    public String getStylesheet(String stylesheet) {
        String cssPath = String.format("%s%s%s", STRING_PATH_STYLESHEETS, stylesheet, STRING_EXTENSION_STYLESHEETS);
        return this._classLoader.getResource(cssPath).toExternalForm();
    }

    public Image getImage(String imageName) {
        String imagePath = "images/" + imageName;
        String imageFullPath = this._classLoader.getResource(imagePath).toExternalForm();
        return new Image(imageFullPath);
    }
}
