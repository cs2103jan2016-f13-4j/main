package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.parser.ParserDefinitions;
import logic.parser.ParserDefinitionsDeserializer;
import shared.Command;
import shared.Resources;
import skeleton.CommandParserSpec;

/**
 * Created by maianhvu on 06/04/2016.
 */
public class CommandParser implements CommandParserSpec {

    private static final String FILE_PARSER_DATA = "CommandParserData.json";

    /**
     * Singleton implementation
     */
    private static CommandParser instance = new CommandParser();
    public static CommandParser getInstance() {
        return instance;
    }

    /**
     * Properties
     */
    private ParserDefinitions _definitions;

    private CommandParser() {

    }

    @Override
    public void initialise() {
        this.readDataFromJson();

    }

    private void readDataFromJson() {
        // Create a JSON builder from the deserializer class
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(
                ParserDefinitions.class,
                ParserDefinitionsDeserializer.getInstance());

        Gson gson = builder.create();

        // Read data from the file and parse it
        String definitionData = Resources.sharedResources().getDataFrom(FILE_PARSER_DATA);
        this._definitions = gson.fromJson(definitionData, ParserDefinitions.class);
    }

    @Override
    public Command parse(String commandString) {
        return null;
    }
}
