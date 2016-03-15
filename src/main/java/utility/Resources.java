package utility;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;

/**
 * Created by maianhvu on 15/03/2016.
 */
public class Resources {

    private static final ClassLoader CLASS_LOADER_DEFAULT = Resources.class.getClassLoader();

    private static final String PATH_RESOURCES_TEMPLATES = "templates/";
    private static final String EXTENSION_RESOURCES_TEMPLATES = ".fxml";

    /**
     * Creates a template from the given
     * @param template
     * @param <T>
     * @return
     */
    public static <T> T getTemplate(String template) {
        // Get the URL to the template
        String resourcePath = String.format("%s%s%s", PATH_RESOURCES_TEMPLATES, template, EXTENSION_RESOURCES_TEMPLATES);
        URL resourceUrl = CLASS_LOADER_DEFAULT.getResource(resourcePath);

        try {
            return FXMLLoader.load(resourceUrl);
        } catch (IOException e) {
            // Cannot find file
            return null;
        }
    }
}
