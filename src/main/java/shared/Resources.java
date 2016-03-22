package shared;

import javafx.fxml.FXMLLoader;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;

public class Resources {

    private static final ClassLoader CLASS_LOADER_DEFAULT = Resources.class.getClassLoader();
    private static final String STRING_EXTENSION_TEMPLATES = ".fxml";
    private static final String STRING_PATH_TEMPLATES = "templates/";
    private static final String STRING_PATH_FONTS = "fonts/";

    /**
     * Builds a JavaFX view from the specified FXML template.
     *
     * @param template the template name (without extension)
     *
     * @return null if template not found
     */
    public static <T> T getComponent(String template) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getTemplateUrl(template));
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T, C> Pair<T, C> getComponentAndController(String template) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getTemplateUrl(template));
            return new Pair<>(loader.load(), loader.getController());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static URL getTemplateUrl(String template) {
        String fullPath = String.format("%s%s%s", STRING_PATH_TEMPLATES, template, STRING_EXTENSION_TEMPLATES);
        URL resourceUrl = CLASS_LOADER_DEFAULT.getResource(fullPath);
        return resourceUrl;
    }

    public static String getFontUrl(String font) {
        return CLASS_LOADER_DEFAULT.getResource(STRING_PATH_FONTS.concat(font)).toExternalForm();
    }
}
