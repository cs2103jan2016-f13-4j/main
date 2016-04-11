package storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import exception.ExceptionHandler;
import shared.ApplicationContext;

/**
 * Manages user's preferences which is stored in JSON format.
 *
 * @@author A0127357B
 *
 */
public class UserPreferences {

    /**
     * Constants
     */
    private static final String DEFAULT_TO_DO_PATH = "data/ToDoData.csv";
    private static final String PREFERENCES_FILE_NAME = "data/user/UserPreferences.json";

    /**
     * Singleton implementation
     */
    private static UserPreferences instance = new UserPreferences();
    public static UserPreferences getInstance() {
        return instance;
    }

    /**
     * Properties
     */
    private String todoDataPath;

    /**
     * Constructs a new UserPreference instance.
     */
    private UserPreferences() {
        createOrReadPreferencesFile();
    }

    /**
     * Reads user preferences file stored on disk and set the To-Do data path as
     * specified in the file. If user preferences file does not already exist,
     * assign the default To-Do data path and create a new user preferences file
     * on disk.
     */
    public void createOrReadPreferencesFile() {
        boolean preferencesFileExists = checkPreferencesFileExists();
        if (!preferencesFileExists) {
            this.setTodoDataPath(this.DEFAULT_TO_DO_PATH); // set path to default
                                                        // one
            try {
                this.writeUserPreferencesToDisk(); // create preferences file on
                                                   // disk
            } catch (IOException e) {
                ExceptionHandler.handle(e);
            }
        } else {
            try {
                readUserPreferencesFromDisk(); // read existing preferences file
            } catch (IOException e) {
                ExceptionHandler.handle(e);
            }
        }
    }

    /**
     * Checks if json file containing user preferences data already exist,
     * creates a new file if it does not
     * 
     * @return true if the file already exists, false otherwise
     */
    public boolean checkPreferencesFileExists() {
        File file = new File(this.PREFERENCES_FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                ExceptionHandler.handle(e);
            }
            return false; // return false if file was newly created
        }
        return true; // return true if file already existed prior to executing
                     // this method
    }

    /**
     * Resets UserPreferences attributes to default values
     */
    public void resetUserPreferences() {
        File file = new File(this.PREFERENCES_FILE_NAME);
        file.delete();
        this.todoDataPath = this.DEFAULT_TO_DO_PATH;
    }

    // ----------------------------------------------------------------------------------------
    //
    // I. Gson Serializing Methods
    //
    // ----------------------------------------------------------------------------------------

    public class UserPreferencesSerializer implements JsonSerializer<UserPreferences> {

        /**
         * Gson invokes this call-back method during serialization when it encounters
         * a field of the specified type.
         *
         * @param userPreferences
         * @param typeOfSrc
         * @param context
         * @return
         */
        @Override public JsonElement serialize(UserPreferences userPreferences, Type typeOfSrc,
                JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("todoDataPath", userPreferences.getTodoDataPath());
            return jsonObject;
        }
    }

    /**
     * Write user preferences data to disk in json format.
     * 
     * @throws IOException
     */
    public void writeUserPreferencesToDisk() throws IOException {
        String json = prepareJson();

        File file = new File(this.PREFERENCES_FILE_NAME);
        this.createDirectory(this.PREFERENCES_FILE_NAME);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(json);
        fileWriter.close();
    }

    /**
     * Prepare json to be written to disk.
     * 
     * @return
     */
    public String prepareJson() {
        // Configure GSON
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(UserPreferences.class, new UserPreferencesSerializer());
        Gson gson = gsonBuilder.create();

        // Format to JSON
        String json = gson.toJson(this);

        return json;
    }

    /**
     * Creates directory for writing user preferences file
     *
     * @param fileName
     */
    public void createDirectory(String fileName) {
        // Try to create directory
        File folder = new File(fileName).getParentFile();
        folder.mkdirs();
    }

    // ----------------------------------------------------------------------------------------
    //
    // II. Gson Deserializing Methods
    //
    // ----------------------------------------------------------------------------------------

    public class UserPreferencesDeserializer implements JsonDeserializer<UserPreferences> {

        /**
         * Gson invokes this call-back method during deserialization when it encounters
         * a field of the specified type.
         *
         * @param json
         * @param typeOfT
         * @param context
         * @return
         * @throws JsonParseException
         */
        @Override public UserPreferences deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();
            final JsonElement jsonDataFileName = jsonObject.get("todoDataPath");
            final String toDoDataPath = jsonDataFileName.getAsString();
            UserPreferences userPreferences = UserPreferences.this;
            userPreferences.setTodoDataPath(toDoDataPath);
            return userPreferences;
        }
    }

    /**
     * Read user preferences data from disk in json format.
     * 
     * @throws IOException
     */
    public void readUserPreferencesFromDisk() throws IOException {
        this.handleJson(this.readLineFromDisk());
    }

    /**
     * Process json to obtain Java object.
     * 
     * @param jsonString
     */
    public void handleJson(String jsonString) {
        // Configure Gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(UserPreferences.class, new UserPreferencesDeserializer());
        Gson gson = gsonBuilder.create();
        UserPreferences userPreferences = gson.fromJson(jsonString, UserPreferences.class);
        this.setTodoDataPath(userPreferences.getTodoDataPath());
    }

    /**
     * Reads a line from user preferences file stored on disk
     *
     * @return
     */
    public String readLineFromDisk() {
        String jsonString = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.PREFERENCES_FILE_NAME));
            jsonString = reader.readLine();
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }
        return jsonString;
    }

    /**
     * Getters
     */
    public String getTodoDataPath() {
        if (ApplicationContext.mainContext().isTestingMode()) {
            return "tmp/ToDoData.csv";
        }
        return this.todoDataPath;
    }

    /**
     * Setters
     */
    public void setTodoDataPath(String pathName) {
        this.todoDataPath = pathName;
    }
}