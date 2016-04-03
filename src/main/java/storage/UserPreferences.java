package storage;

import com.google.gson.*;
import exception.ExceptionHandler;

import java.io.*;
import java.lang.reflect.Type;

/**
 * Created by Huiyie on 2/4/16.
 */

public class UserPreferences {
    /**
     * Properties
     */
    private static UserPreferences instance = new UserPreferences();

    public static UserPreferences getInstance() {
        return instance;
    }

    /**
     * Properties
     */
    private String todoDataPath;
    private static final String defaultToDoPath = "data/ToDoData.csv";
    private static final String preferencesFileName = "data/user/UserPreferences.json";

    /**
     * Constructor
     */
    private UserPreferences() {
        createOrReadPreferencesFile();
    }

    public void createOrReadPreferencesFile() {
        boolean preferencesFileExists = checkPreferencesFileExists();
        if (!preferencesFileExists) {
            this.setTodoDataPath(this.defaultToDoPath); // set path to default one
            try {
                this.writeUserPreferencesToDisk(); // create preferences file on disk
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
     * Checks if json file containing user preferences data already exist, creates a new file if it does not
     * @return  true if the file already exists, false otherwise
     */
    public boolean checkPreferencesFileExists() {
        File file = new File(this.preferencesFileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                ExceptionHandler.handle(e);
            }
            return false; // return false if file was newly created
        }
        return true; // return true if file already existed prior to executing this method
    }

    /**
     * Resets UserPreferences attributes to default values
     */
    public void resetUserPreferences() {
        File file = new File(this.preferencesFileName);
        file.delete();
        this.todoDataPath = this.defaultToDoPath;
    }

    // ----------------------------------------------------------------------------------------
    //
    // I. Gson Serializing Methods
    //
    // ----------------------------------------------------------------------------------------

    public class UserPreferencesSerializer implements JsonSerializer<UserPreferences> {

        @Override
        public JsonElement serialize(UserPreferences userPreferences, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("todoDataPath", userPreferences.getTodoDataPath());
            return jsonObject;
        }
    }

    public void writeUserPreferencesToDisk() throws IOException {
        String json = prepareJson();

        File file = new File(this.preferencesFileName);
        this.createDirectory(this.preferencesFileName);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(json);
        fileWriter.close();
    }

    public String prepareJson() {
        // Configure GSON
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(UserPreferences.class, new UserPreferencesSerializer());
        Gson gson = gsonBuilder.create();

        // Format to JSON
        String json = gson.toJson(this);

        return json;
    }

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

        @Override
        public UserPreferences deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();
            final JsonElement jsonDataFileName = jsonObject.get("todoDataPath");
            final String toDoDataPath = jsonDataFileName.getAsString();
            UserPreferences userPreferences = UserPreferences.this;
            userPreferences.setTodoDataPath(toDoDataPath);
            return userPreferences;
        }
    }

    public void readUserPreferencesFromDisk() throws IOException {
        this.handleJson(this.readLineFromDisk());
    }

    public void handleJson(String jsonString) {
        // Configure Gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(UserPreferences.class, new UserPreferencesDeserializer());
        Gson gson = gsonBuilder.create();
        UserPreferences userPreferences = gson.fromJson(jsonString, UserPreferences.class);
        this.setTodoDataPath(userPreferences.getTodoDataPath());
    }

    public String readLineFromDisk() {
        String jsonString = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.preferencesFileName));
            jsonString = reader.readLine();
        } catch (IOException e) {
            ExceptionHandler.handle(e);
        }
        return jsonString;
    }

    // ----------------------------------------------------------------------------------------
    //
    // III. Getter and Setter Methods
    //
    // ----------------------------------------------------------------------------------------

    public String getTodoDataPath() {
        return this.todoDataPath;
    }

    public void setTodoDataPath(String pathName) {
        this.todoDataPath = pathName;
    }
}