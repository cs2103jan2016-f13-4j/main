package storage;

import java.io.File;
import java.io.IOException;

/**
 * Created by maianhvu on 25/2/16.
 */
public class Database {
    private static final String STRING_PATH_BASE_DIR = "data";
    private static final String STRING_PATH_DB_SCHEMA = "schema.yaml";

    public Database() throws IOException {
        this.initializeFileSystem();
        this.loadSchema();
    }

    private void loadSchema() {
    }

    private void initializeFileSystem() throws IOException {
        // Ensure base directory exists
        File baseDir = new File(STRING_PATH_BASE_DIR);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }

        // Ensure components exists
        File schema = new File(joinPath(STRING_PATH_BASE_DIR, STRING_PATH_DB_SCHEMA));
        if (!schema.exists()) {
            schema.createNewFile();
        }
    }

    private static String joinPath(String... path) {
        StringBuilder sb = new StringBuilder();
        for (String p : path) {
            if (sb.length() != 0) {
                sb.append("/");
            }
            sb.append(p);
        }
        return sb.toString();
    }
}
