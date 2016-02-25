package storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Created by maianhvu on 25/2/16.
 */
public class YamlParser {

    public static final String STRING_YAML_FILE_START_DEFAULT = "---";
    public static final String STRING_YAML_FILE_END_DEFAULT = "...";

    public static final char CHAR_YAML_COMMENT = '#';

    public static final Error ERROR_YAML_FILE_IMPROPER_START = new Error(
            "Schema file did not begin with " + STRING_YAML_FILE_START_DEFAULT
    );
    public static final Error ERROR_YAML_FILE_IMPROPER_END = new Error(
            "Schema file did not end with " + STRING_YAML_FILE_END_DEFAULT
    );

    public static LinkedHashMap<String, BaseModel> parseSchema(File schemaFile) throws IOException, Error {
        // Begin storing schema in a LinkedHashMap
        LinkedHashMap<String, BaseModel> schema = new LinkedHashMap<>();

        // Initialize IO operations
        BufferedReader reader = new BufferedReader(new FileReader(schemaFile));
        String line = reader.readLine();

        // Detect whether YAML file started properly
        if (!isStartOfYamlFile(line))  {
            throw ERROR_YAML_FILE_IMPROPER_START;
        }

        // Learn and adapt to indentation
        boolean isIndentationLearnt = false;
        String indentation = null;

        // Begin scanning through file
        while ((line = reader.readLine()) != null) {
            // Ignore comments
            if (isYamlComment(line)) continue;
            // End the file
            if (isEndOfYamlFile(line)) break;

        }

        // Try to read more
        if ((line = reader.readLine()) != null || !line.trim().isEmpty()) {
            throw ERROR_YAML_FILE_IMPROPER_END;
        }

        // Return full schema in the end
        return schema;
    }


    private static boolean isStartOfYamlFile(String line) {
        if (line == null) return false;
        return line.trim().equals(STRING_YAML_FILE_START_DEFAULT);
    }

    private static boolean isEndOfYamlFile(String line) {
        if (line == null) return false;
        return line.trim().equals(STRING_YAML_FILE_END_DEFAULT);
    }

    private static boolean isYamlComment(String line) {
        if (line == null) return false;
        return line.trim().charAt(0) == CHAR_YAML_COMMENT;
    }

    private static int countIndents(String line) throws Error {
        int spaceCharCounts = 0;
        while (line.charAt(spaceCharCounts) == ' ') {
            spaceCharCounts++;
        }

        float indents = spaceCharCounts / 4;
        int indentLevel = (int) Math.floor(indents);

        if (indents != indentLevel) {
            throw new Error("Inconsistent indentation level");
        }

        return indentLevel;
    }
}
