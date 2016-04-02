package logic;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import com.google.gson.internal.LinkedTreeMap;
import javafx.util.Pair;
import shared.Command;
import shared.Resources;
import shared.Task;
import skeleton.CommandParserSpec;

/**
 * @@author Mai Anh Vu
 */
public class FlexiCommandParser implements CommandParserSpec {

    private static final String NAME_FILE_DATA = "CommandParserData.json";
    private static final String PATTERN_TIME = "\\d{1,2}:?(?:\\d{2})?(?:\\s*(?:am|pm))?";

    /**
     * Types
     */
    private enum TimePrepositionMeaning {
        CURRENT, NEXT, STARTING, ENDING
    }
    private enum TimeNounMeaning {
        NOW, TODAY, TOMORROW, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
    /**
     * Singleton implementation
     */
    private static FlexiCommandParser instance = new FlexiCommandParser();

    public static FlexiCommandParser getInstance() {
        return instance;
    }

    /**
     * Properties
     */
    private HashMap<String, Command.Instruction> _instructionEnumTranslator;
    private HashMap<String, TimePrepositionMeaning> _timePrepositionMeaningEnumTranslator;
    private HashMap<String, TimeNounMeaning> _timeNounMeaningEnumTranslator;
    private HashMap<String, Task.Priority> _priorityNounEnumTranslator;

    private Definition _commandDefinitions;
    private String _instructionPattern;
    private String _timePattern;
    private String _priorityPattern;

    private FlexiCommandParser() {}

    public void initialize() {
        // Prepare enum translation map
        this.prepareEnumTranslators();

        this.readDefinitions();

        this._instructionPattern = constructInstructionRegex();
        this._timePattern = constructNotSurroundedByQuotesRegex(constructTimeRegex());
        this._priorityPattern = constructNotSurroundedByQuotesRegex(constructPriorityRegex());
    }

    /*=================================================================================================
     |                                                                                                |
     | ENUM CLASS INVERSION TABLE PREPARATIONS                                                        |
     |                                                                                                |
     +-------------------------------------------------------------------------------------------------
     |                                                                                                |
     | This section constructs hash maps that points a constant string name back to                   |
     | the original enum constant with the corresponding name. This is usually used                   |
     | in JSON deserializations.                                                                      |
     |                                                                                                |
     *===============================================================================================*/
    private void prepareEnumTranslators() {
        this._instructionEnumTranslator = constructEnumTranslator(Command.Instruction.class);
        this._timePrepositionMeaningEnumTranslator = constructEnumTranslator(TimePrepositionMeaning.class);
        this._timeNounMeaningEnumTranslator = constructEnumTranslator(TimeNounMeaning.class);
        this._priorityNounEnumTranslator = constructEnumTranslator(Task.Priority.class);
    }

    private static <T extends Enum> HashMap<String, T> constructEnumTranslator(Class<T> enumClass) {
        HashMap<String, T> map = new HashMap<>();
        Arrays.stream(enumClass.getEnumConstants())
                .forEach(meaning -> map.put(
                        meaning.name(),
                        meaning
                ));
        return map;
    }

    /*=================================================================================================
     |                                                                                                |
     | READING JSON DEFINITIONS                                                                       |
     |                                                                                                |
     +-------------------------------------------------------------------------------------------------
     |                                                                                                |
     | This section reads the JSON files from the `Resource` folder and parse the                     |
     | correct data into the `Definition` private class (nested under this class).                    |
     | The parsed `Definition` class will then assist in both parsing the command,                    |
     | and highlighting the command keywords inside the command input field.                          |
     |                                                                                                |
     *===============================================================================================*/
    private void readDefinitions() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Definition.class, new DefinitionDeserializer());
        Gson gson = builder.create();

        // Deserialize
        String definitionData = Resources.getInstance().getDataFrom(NAME_FILE_DATA);
        Definition definition = gson.fromJson(definitionData, Definition.class);

        this._commandDefinitions = definition;
    }

    /*=================================================================================================
     |                                                                                                |
     | PUBLIC GET API FOR KEYWORDS                                                                    |
     |                                                                                                |
     +-------------------------------------------------------------------------------------------------
     |                                                                                                |
     | This allows other classes to access the data deserialized from the JSON file.                  |
     |                                                                                                |
     *===============================================================================================*/
    public String getInstructionPattern() {
        assert this._instructionPattern != null;
        return this._instructionPattern;
    }

    public String getTimePattern() {
        assert this._timePattern != null;
        return this._timePattern;
    }

    public String getPriorityPattern() {
        assert this._priorityPattern != null;
        return this._priorityPattern;
    }
    /*=================================================================================================
     |                                                                                                |
     | CONSTRUCTION OF REGEX                                                                          |
     |                                                                                                |
     +-------------------------------------------------------------------------------------------------
     |                                                                                                |
     | The regular expressions to parse the commands, as well as to highlight them                    |
     | inside the command input box, will all be constructed in the methods within                    |
     | this section.                                                                                  |
     |                                                                                                |
     *===============================================================================================*/
    private String constructInstructionRegex() {
        StringBuilder sb = new StringBuilder("(?<INST>^\\s*");
        Set<String> instructionKeywords = this._commandDefinitions.getInstructionKeywords();
        sb.append(constructChoiceRegex(instructionKeywords));
        sb.append(")");
        return sb.toString();
    }

    private String constructTimeRegex() {
        final HashMap<Set<TimePrepositionMeaning>, Set<TimeNounMeaning>> compressedGraph =
                new LinkedHashMap<>();

        // Compress the bipartite graph between prepositions and time nouns
        this._commandDefinitions.getTimeClauseGraph().entrySet().forEach(entry -> {
            TimeNounMeaning noun = entry.getKey();
            Set<TimePrepositionMeaning> prepositions = entry.getValue();
            Set<TimeNounMeaning> nouns = compressedGraph.get(prepositions);
            if (nouns == null) {
                nouns = new TreeSet<>();
                compressedGraph.put(prepositions, nouns);
            }
            nouns.add(noun);
        });

        // Enumerate the possible combinations according to the compressed graph
        Set<String> compressedTimeClauses = compressedGraph.entrySet().stream().map(entry -> {
            // Group all the prepositions inside a map first
            String prepositionsRegex = constructChoiceRegex(
                    entry.getKey().stream().map(this._commandDefinitions::getTimePreposition)
                            .map(TimePreposition::getKeywords)
                            .flatMap(Collection::stream).collect(Collectors.toSet())
            );

            // Add some chaining of prepositions behind
            String chainablePrepositionsRegex = constructChoiceRegex(
                    entry.getKey().stream().map(this._commandDefinitions::getTimePreposition)
                            .filter(TimePreposition::isChainable)
                            .map(TimePreposition::getKeywords)
                            .flatMap(Collection::stream).collect(Collectors.toSet())
            );
            if (!chainablePrepositionsRegex.isEmpty()) { // Account for empty chainable
                chainablePrepositionsRegex = String.format("(?:%s?\\s+)?", chainablePrepositionsRegex);
            }

            String nounsRegex = constructChoiceRegex(
                    entry.getValue().stream().map(this._commandDefinitions::getTimeNounKeywords)
                            .flatMap(Collection::stream)
                            .collect(Collectors.toSet())
            );

            // Allow chaining of prepositions
            return String.format("%s\\s+%s%s",
                    prepositionsRegex,
                    chainablePrepositionsRegex,
                    nounsRegex);

        }).collect(Collectors.toSet());

        // Construct time regex
        return String.format("(?<DATE>%s)(?:'s)?\\s*(?<TIME>%s)?",
                constructChoiceRegex(compressedTimeClauses),
                PATTERN_TIME);
    }

    private String constructPriorityRegex() {
        assert this._commandDefinitions != null;

        String prepositions = constructChoiceRegex(
                this._commandDefinitions.getPriorityPrepositionKeywords()
        );
        String nouns = constructChoiceRegex(
                this._commandDefinitions.getPriorityNounKeywords()
        );

        return String.format("%s\\s+(?<PRIORITY>%s)",
                prepositions, nouns);
    }

    /*=================================================================================================
     |                                                                                                |
     | JSON DEFINITION CLASS                                                                          |
     |                                                                                                |
     +-------------------------------------------------------------------------------------------------
     |                                                                                                |
     | This class represents the definitions data that have been abstracted after                     |
     | deserialization from the raw JSON file. This class also contains some helper                   |
     | algorithms to help create some of the regular expressions above.                               |
     |                                                                                                |
     *===============================================================================================*/
    private class Definition {
        /**
         * Properties
         */
        private HashMap<Command.Instruction, Set<String>> _commandKeywordMap;
        private HashMap<TimePrepositionMeaning, TimePreposition> _timePrepositionMap;
        private HashMap<TimeNounMeaning, Set<String>> _timeNounMap;
        private Set<String> _priorityPrepositions;
        private HashMap<Task.Priority, Set<String>>_prioritiesKeywordMap;

        private Map<TimeNounMeaning, Set<TimePrepositionMeaning>> _adjacencyList;
        private HashMap<String, Command.Instruction> _commandKeywordInversionMap;


        // Caching
        private Set<String> _cachedInstructionKeywords;
        private Map<String, TimeClause> _cachedTimeClauseMap;
        private Set<String> _cachedPriorityNounKeywords;

        /**
         * TODO: Write JavaDoc
         */
        Definition() {
            this._commandKeywordMap = new LinkedHashMap<>();
            this._timePrepositionMap = new LinkedHashMap<>();
            this._timeNounMap = new LinkedHashMap<>();
            this._prioritiesKeywordMap = new LinkedHashMap<>();
            this._adjacencyList = new LinkedTreeMap<>();

            this._commandKeywordInversionMap = new HashMap<>();
        }

        /**
         * TODO: Write JavaDoc
         * @param name
         * @param keywords
         */
        void addInstruction(String name, String[] keywords) {
            final Command.Instruction instruction = _instructionEnumTranslator.get(name);
            final Set<String> keywordSet = new CopyOnWriteArraySet<>(Arrays.asList(keywords));
            this._commandKeywordMap.put(instruction, keywordSet);
            keywordSet.stream().forEach(keyword -> this._commandKeywordInversionMap.put(keyword, instruction));
        }

        /**
         * TODO: Write JavaDoc
         * @param meaning
         * @param keywords
         * @param isChainable
         */
        void addTimePreposition(String meaning, String[] keywords, boolean isChainable) {
            final TimePrepositionMeaning realMeaning = _timePrepositionMeaningEnumTranslator.get(meaning);
            final Set<String> keywordSet = new CopyOnWriteArraySet<>(Arrays.asList(keywords));
            this._timePrepositionMap.put(realMeaning, new TimePreposition(
                    realMeaning,
                    keywordSet,
                    isChainable
            ));
        }

        /**
         * TODO: Write JavaDoc
         * @param meaning
         * @param keywords
         * @param prepositions
         */
        void addTimeNoun(String meaning, String[] keywords, String[] prepositions) {
            final TimeNounMeaning realMeaning = _timeNounMeaningEnumTranslator.get(meaning);
            final Set<String> keywordSet = new CopyOnWriteArraySet<>(Arrays.asList(keywords));
            this._timeNounMap.put(realMeaning, keywordSet);

            // Add time nouns to adjacency matrix
            final Set<TimePrepositionMeaning> prepositionsList = Arrays.stream(prepositions)
                    .map(_timePrepositionMeaningEnumTranslator::get)
                    .collect(Collectors.toSet());
            this._adjacencyList.put(realMeaning, prepositionsList);
        }

        /**
         * TODO: Write JavaDoc
         * @param keywords
         */
        void setPriorityPrepositions(String[] keywords) {
            this._priorityPrepositions = new CopyOnWriteArraySet<>(Arrays.asList(keywords));
        }

        /**
         * TODO: Write JavaDoc
         * @param meaning
         * @param keywords
         */
        void addPriorityNoun(String meaning, String[] keywords) {
            this._prioritiesKeywordMap.put(
                    _priorityNounEnumTranslator.get(meaning),
                    new CopyOnWriteArraySet<>(Arrays.asList(keywords))
            );
        }

        /**
         * TODO: Write JavaDoc
         * @return
         */
        Map<String, TimeClause> getTimeClauses() {
            if (this._cachedTimeClauseMap != null) {
                return this._cachedTimeClauseMap;
            }

            // Initialize the map
            Map<String, TimeClause> timeClauseMap = new LinkedHashMap<>();

            // Run through the adjacency list to construct the map
            this._adjacencyList.entrySet().forEach(entry -> {
                // Get all nouns
                Set<String> timeNouns = this._timeNounMap.get(entry.getKey());
                // Use all the prepositions to construct the time clause map
                entry.getValue().stream()
                        .map(preposition -> new Pair<>(
                                new TimeClause(preposition, entry.getKey()),
                                this._timePrepositionMap.get(preposition)
                        ))
                        .forEach(pair -> {
                            TimeClause clause = pair.getKey();
                            Set<String> prepositions = pair.getValue().getKeywords();

                            prepositions.stream().flatMap(preposition ->
                                    timeNouns.stream().map(noun -> String.format("%s %s", preposition, noun))
                            ).forEach(
                                    stringClause -> timeClauseMap.put(stringClause, clause)
                            );
                        });
            });

            // Cache
            this._cachedTimeClauseMap = timeClauseMap;

            return timeClauseMap;
        }

        /**
         * TODO: Write JavaDoc
         * @return
         */
        Map<TimeNounMeaning, Set<TimePrepositionMeaning>> getTimeClauseGraph() {
            return this._adjacencyList;
        }

        /**
         * TODO: Write JavaDoc
         * @return
         */
        Set<String> getInstructionKeywords() {
            // Return cache right away
            if (this._cachedInstructionKeywords != null) {
                return this._cachedInstructionKeywords;
            }

            // Construct instructions set
            this._cachedInstructionKeywords = this._commandKeywordMap.values().stream()
                    .map(ArrayList::new)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());

            return this._cachedInstructionKeywords;
        }

        Command.Instruction getInstruction(String keyword) {
            return this._commandKeywordInversionMap.get(keyword);
        }

        /**
         * TODO: Write JavaDoc
         * @param preposition
         * @return
         */
        TimePreposition getTimePreposition(TimePrepositionMeaning preposition) {
            return this._timePrepositionMap.get(preposition);
        }

        /**
         * TODO: Write JavaDoc
         * @param noun
         * @return
         */
        Set<String> getTimeNounKeywords(TimeNounMeaning noun) {
            return this._timeNounMap.get(noun);
        }

        Set<String> getPriorityPrepositionKeywords() {
            return this._priorityPrepositions;
        }

        Set<String> getPriorityNounKeywords() {
            if (this._cachedPriorityNounKeywords != null) {
                return this._cachedPriorityNounKeywords;
            }

            this._cachedPriorityNounKeywords = this._prioritiesKeywordMap.values().stream()
                    .map(ArrayList::new)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());

            return this._cachedPriorityNounKeywords;
        }
    }

    /**
     * This class supports the JSON deserialization of {@link Definition} class.
     */
    private class DefinitionDeserializer implements JsonDeserializer<Definition> {

        @Override public Definition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            // Get the definition as a JSON object
            final JsonObject definitionObject = json.getAsJsonObject();
            final Definition definition = new Definition();

            // Begin deserialization

            // INSTRUCTIONS ARRAY
            // This array contains the information about command instructions and their alternative forms
            final JsonArray instructions = definitionObject.get("instructions").getAsJsonArray();
            instructions.forEach(instructionElement -> {
                final JsonObject instructionObject = instructionElement.getAsJsonObject();

                final String instructionName = instructionObject.get("name").getAsString();
                final String[] instructionKeywords = decodeJSONStringArray(
                        instructionObject.get("keywords").getAsJsonArray()
                );

                definition.addInstruction(instructionName, instructionKeywords);
            });

            // TIME PREPOSITIONS ARRAY
            // This array dictates the behaviour of the prepositions which modify the time word following
            // these prepositions, inside a command string
            // Example: from next
            final JsonArray timePrepositions = definitionObject.get("timePrepositions").getAsJsonArray();
            timePrepositions.forEach(timePrepositionElement -> {
                // Map to an actual JSON object
                final JsonObject timePrepositionObject = timePrepositionElement.getAsJsonObject();

                final String meaning = timePrepositionObject.get("meaning").getAsString();
                final String[] timePrepositionMeaningKeywords = decodeJSONStringArray(
                        timePrepositionObject.get("keywords").getAsJsonArray()
                );
                final boolean isChainable = timePrepositionObject.get("chainable").getAsBoolean();

                definition.addTimePreposition(meaning, timePrepositionMeaningKeywords, isChainable);
            });

            // TIME NOUNS ARRAY
            // This array defines the different types of days that are allowed
            final JsonArray timeNouns = definitionObject.get("timeNouns").getAsJsonArray();
            timeNouns.forEach(timeElement -> {
                // Map to an actual JSON object
                final JsonObject timeObject = timeElement.getAsJsonObject();

                final String meaning = timeObject.get("meaning").getAsString();
                final String[] keywords = decodeJSONStringArray(
                        timeObject.get("keywords").getAsJsonArray()
                );
                final String[] prepositions = decodeJSONStringArray(
                        timeObject.get("prepositions").getAsJsonArray()
                );

                definition.addTimeNoun(meaning, keywords, prepositions);
            });

            // PRIORITY PREPOSITIONS ARRAY
            // This array contains all the prepositions used to indicate a priority
            definition.setPriorityPrepositions(decodeJSONStringArray(
                    definitionObject.get("priorityPrepositions").getAsJsonArray()
            ));

            // PRIORITY NOUNS ARRAY
            // This array contains the different meaning and the keywords of priorities.
            final JsonArray priorityNouns = definitionObject.get("priorities").getAsJsonArray();
            priorityNouns.forEach(noun -> {
                // Map to an actual JSON object
                final JsonObject nounDefinition = noun.getAsJsonObject();

                final String meaning = nounDefinition.get("meaning").getAsString();
                final String[] keywords = decodeJSONStringArray(
                        nounDefinition.get("keywords").getAsJsonArray()
                );

                definition.addPriorityNoun(meaning, keywords);
            });

            return definition;
        }
    }

    private class TimePreposition {
        private TimePrepositionMeaning _meaning;
        private Set<String> _keywords;
        private boolean _isChainable;

        TimePreposition(TimePrepositionMeaning meaning, Set<String> keywords, boolean isChainable) {
            this._meaning = meaning;
            this._keywords = keywords;
            this._isChainable = isChainable;
        }

        TimePrepositionMeaning getMeaning() {
            return this._meaning;
        }

        Set<String> getKeywords() {
            return this._keywords;
        }

        boolean isChainable() {
            return this._isChainable;
        }
    }

    private class TimeClause {
        private TimePrepositionMeaning _preposition;
        private TimeNounMeaning _noun;

        TimeClause(TimePrepositionMeaning preposition, TimeNounMeaning noun) {
            this._preposition = preposition;
            this._noun = noun;
        }

        TimePrepositionMeaning getPreposition() {
            return this._preposition;
        }

        TimeNounMeaning getNoun() {
            return this._noun;
        }

        @Override public String toString() {
            return String.format("%s %s", this._preposition.name(), this._noun.name());
        }
    }

    /*=================================================================================================
     |                                                                                                |
     | MAIN PARSE METHOD                                                                              |
     |                                                                                                |
     +-------------------------------------------------------------------------------------------------
     |                                                                                                |
     | Core method that pieces everything together to construct a Command from a raw query.           |
     |                                                                                                |
     *===============================================================================================*/
    /**
     * TODO: Write JavaDoc
     * @param commandString
     * @return
     */
    @Override public Command parse(String commandString) {
        // Ensure command string is ready
        assert commandString != null;
        commandString = commandString.trim();

        // INSTRUCTION parsing
        // We will first use the instruction RegExp to determine the type of instruction.
        // This instruction type will then dictate how parameters are created
        Command.Instruction instruction = this.parseInstruction(commandString);

        // Based on the INSTRUCTION, we will now proceed to decoding the parameters.
        switch (instruction) {
            case ADD:
                break;
        }

        return new Command(instruction, null, false);
    }

    private Command.Instruction parseInstruction(String commandString) {
        // Empty commands are invalid
        if (commandString.isEmpty()) {
            return Command.Instruction.INVALID;
        }

        // Prepare RegExp
        Pattern instructionPattern = Pattern.compile(this.getInstructionPattern(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = instructionPattern.matcher(commandString);
        // No instructions found, return unrecognised
        if (!matcher.find()) return Command.Instruction.UNRECOGNISED;

        String instructionString = matcher.group("INST");
        // Couldn't find the correct string
        if (instructionString == null) {
            return Command.Instruction.UNRECOGNISED;
        }

        Command.Instruction instruction = this._commandDefinitions.getInstruction(instructionString);
        if (instruction == null) return Command.Instruction.UNRECOGNISED;

        return instruction;
    }

    /*=================================================================================================
     |                                                                                                |
     | HELPER METHODS                                                                                 |
     |                                                                                                |
     +-------------------------------------------------------------------------------------------------
     |                                                                                                |
     | Supporting methods that make other functions maintain brevity and efficiency.                  |
     |                                                                                                |
     *===============================================================================================*/
    private static String[] decodeJSONStringArray(JsonArray stringArray) {
        final String[] array = new String[stringArray.size()];
        IntStream.range(0, array.length).forEachOrdered(index -> {
            array[index] = stringArray.get(index).getAsString();
        });
        return array;
    }

    private static String constructChoiceRegex(Set<String> choices) {
        // Borderline cases
        if (choices.size() == 0) {
            return "";
        }
        if (choices.size() == 1) {
            return choices.iterator().next();
        }

        StringBuilder sb = new StringBuilder();
        ArrayList<String> choiceList = new ArrayList<>(choices);
        Collections.sort(choiceList, (noun1, noun2) -> noun2.length() - noun1.length());
        for (String choice : choiceList) {
            if (sb.length() != 0) {
                sb.append("|");
            }
            sb.append(choice);
        }
        sb.insert(0, "(?:");
        sb.append(")");
        return sb.toString();
    }

    private static String constructNotSurroundedByQuotesRegex(String currentRegex) {
        return currentRegex + "(?=(?:(?:(?:[^\"\\\\]++|\\\\.)*+\"){2})*+(?:[^\"\\\\]++|\\\\.)*+$)";
    }
}
