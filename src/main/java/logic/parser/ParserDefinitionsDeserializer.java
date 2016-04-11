package logic.parser;

import com.google.gson.*;
import exception.ExceptionHandler;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by maianhvu on 06/04/2016.
 */
public class ParserDefinitionsDeserializer implements JsonDeserializer<ParserDefinitions> {
    /**
     * Constants
     */
    private static final String KEY_JSON_INSTRUCTIONS = "instructions";
    private static final String KEY_JSON_TIME_PREPOSITIONS = "timePrepositions";
    private static final String KEY_JSON_TIME_NOUNS = "timeNouns";
    private static final String KEY_JSON_PRIORITY_PREPOSITIONS = "priorityPrepositions";
    private static final String KEY_JSON_PRIORITY_NOUNS = "priorities";

    /**
     * Singleton implementation
     */
    private static ParserDefinitionsDeserializer instance = new ParserDefinitionsDeserializer();
    public static ParserDefinitionsDeserializer getInstance() {
        return instance;
    }
    private ParserDefinitionsDeserializer() {
    }

    @Override
    public ParserDefinitions deserialize(JsonElement json,
                                         Type typeOfT,
                                         JsonDeserializationContext context) throws JsonParseException {
        // Convert the entire element into a JSON object
        JsonObject definitionObject = json.getAsJsonObject();

        // Parse individual arrays of objects
        List<Instruction> instructions = this.getObjectsFromKey(
                KEY_JSON_INSTRUCTIONS, Instruction.class, definitionObject);
        List<TimePreposition> timePrepositions = this.getObjectsFromKey(
                KEY_JSON_TIME_PREPOSITIONS, TimePreposition.class, definitionObject);
        List<TimeNoun> timeNouns = getObjectsFromKey(
                KEY_JSON_TIME_NOUNS, TimeNoun.class, definitionObject);
        Set<String> priorityPrepositions = JsonUtils.toStringSet(
                definitionObject.get(KEY_JSON_PRIORITY_PREPOSITIONS).getAsJsonArray()
        );
        List<Priority> priorities = this.getObjectsFromKey(
                KEY_JSON_PRIORITY_NOUNS, Priority.class, definitionObject);

        // Finally, populate the definition object with the necessary definitions
        ParserDefinitions definitions = new ParserDefinitions();
        definitions.setInstructions(instructions);
        definitions.setTimePrepositions(timePrepositions);
        definitions.setTimeNouns(timeNouns);
        definitions.setPriorityPrepositions(priorityPrepositions);
        definitions.setPriorities(priorities);

        return definitions;
    }

    private <T> List<T> getObjectsFromKey(String key, Class<T> objectClass, JsonObject object) {
        return this.parseJsonArray(
                object.get(key).getAsJsonArray(),
                objectClass
        );
    }

    private <T> List<T> parseJsonArray(JsonArray array, Class<T> objectClass) {
        return IntStream.range(0, array.size())
                .mapToObj(array::get)
                .map(JsonElement::getAsJsonObject)
                .map(object -> this.constructFromJsonObject(object, objectClass))
                .filter(object -> object != null)
                .collect(Collectors.toList());
    }

    private <T> T constructFromJsonObject(JsonObject object, Class<T> objectClass) {
        try {
            return (T) objectClass.getConstructor(JsonObject.class).newInstance(object);
        } catch (Exception e) {
            ExceptionHandler.handle(e);
            return null;
        }
    }
}
