package logic;

import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
        NOW, TODAY, TOMORROW,
        MONDAY(DayOfWeek.MONDAY),
        TUESDAY(DayOfWeek.TUESDAY),
        WEDNESDAY(DayOfWeek.WEDNESDAY),
        THURSDAY(DayOfWeek.THURSDAY),
        FRIDAY(DayOfWeek.FRIDAY),
        SATURDAY(DayOfWeek.SATURDAY),
        SUNDAY(DayOfWeek.SUNDAY);

        final DayOfWeek dayOfWeek;

        TimeNounMeaning(DayOfWeek dow) {
            dayOfWeek = dow;
        }

        TimeNounMeaning() {
            dayOfWeek = null;
        }

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
        this.prepareEnumTranslators();
        this.readDefinitions();
        this.destroyEnumTraslators();

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

    private void destroyEnumTraslators() {
        this._instructionEnumTranslator = null;
        this._timePrepositionMeaningEnumTranslator = null;
        this._timeNounMeaningEnumTranslator = null;
        this._priorityNounEnumTranslator = null;
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
            for (Map.Entry<TimeNounMeaning, Set<TimePrepositionMeaning>> entry : this._adjacencyList.entrySet()) {
                // Keep the noun here to refer to it later easily
                TimeNounMeaning noun = entry.getKey();

                // Enumerate all possible prepositions

                // Add SINGLE prepositions first
                Set<List<TimePreposition>> prepositionSet = new HashSet<>(entry.getValue().stream()
                        .map(this._timePrepositionMap::get)
                        .map(Arrays::asList)
                        .collect(Collectors.toSet()));
                // Add CHAINED prepositions
                entry.getValue().stream().map(this._timePrepositionMap::get)
                        .filter(TimePreposition::isChainable) // For checking is chainable condition
                        .flatMap(secondPrep -> entry.getValue().stream()
                                .map(this._timePrepositionMap::get)
                                .filter(firstPrep -> !firstPrep.isChainable() && !firstPrep.equals(secondPrep))
                                .map(firstPrep -> Arrays.asList(firstPrep, secondPrep))
                        )
                        .forEach(prepositionSet::add);

                // From each of these prepositions, we will point it to the correct clause
                prepositionSet.stream().map(prepositionList -> new TimeClause(prepositionList, noun))
                        .forEach(timeClause -> {
                            // At this point, we have the clause at the ready
                            // We will need to enumerate the keywords (a 3-level deep loop at least)
                            List<TimePreposition> prepositions = timeClause.getPrepositions();

                            List<String> stringClauses = new ArrayList<>();
                            for (final TimePreposition preposition : prepositions) {
                                if (stringClauses.isEmpty()) {
                                    stringClauses.addAll(preposition.getKeywords());
                                    continue;
                                }
                                stringClauses.addAll(stringClauses.stream().flatMap(oldClause ->
                                        preposition.getKeywords().stream()
                                                .map(newWord -> oldClause + " " + newWord)
                                ).collect(Collectors.toList()));
                            }

                            // Try to generate a flat map of prepositions
                            stringClauses.stream()
                                    .flatMap(stringClause -> this._timeNounMap.get(noun).stream().map(
                                            nounKeyword -> stringClause + " " + nounKeyword
                                    ))
                                    .forEach(stringClause -> timeClauseMap.put(stringClause, timeClause));
                        });
            }

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

        public TimeClause getTimeClause(String date) {
            return this.getTimeClauses().get(date);
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

        @Override public boolean equals(Object o) {
            if (o == null) return false;
            if (this == o) return true;
            if (!(o instanceof TimePreposition)) return false;

            TimePreposition prep = (TimePreposition) o;
            return this._meaning.equals(prep._meaning);
        }
    }

    private class TimeClause {
        private List<TimePreposition> _prepositions;
        private TimeNounMeaning _noun;

        TimeClause(List<TimePreposition> preposition, TimeNounMeaning noun) {
            this._prepositions = preposition;
            this._noun = noun;
        }

        List<TimePreposition> getPrepositions() {
            return this._prepositions;
        }

        Set<TimePrepositionMeaning> getPrepositionMeanings() {
            return new CopyOnWriteArraySet<>(this._prepositions.stream()
                    .map(TimePreposition::getMeaning).collect(Collectors.toList()));
        }

        TimeNounMeaning getNoun() {
            return this._noun;
        }

        @Override public String toString() {
            StringBuilder sb = new StringBuilder();
            this._prepositions.stream().map(TimePreposition::getMeaning).map(Object::toString).forEach(preposition -> {
                sb.append(preposition);
                sb.append(" ");
            });
            sb.append(this._noun);
            return sb.toString();
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
        Command command = new Command(instruction, null, false);

        // Based on the INSTRUCTION, we will now proceed to decoding the parameters.
        switch (instruction) {
            case ADD:
                // Try to find time and priority first
                Pattern timePattern = Pattern.compile(this.getTimePattern(), Pattern.CASE_INSENSITIVE);
                Matcher matcher = timePattern.matcher(commandString);
                int lowestFoundIndex = commandString.length();

                while (matcher.find()) {
                    // For finding task name
                    if (lowestFoundIndex > matcher.start()) {
                        lowestFoundIndex = matcher.start();
                    }
                    // TODO: Handle overlapping time
                    // TODO: Handle null results
                    Pair<Command.ParamName, LocalDateTime> parsedTime = parseDateTime(matcher);
                    command.setParameter(parsedTime.getKey(), parsedTime.getValue());
                }

                String taskName = commandString.substring(commandString.indexOf(' '), lowestFoundIndex).trim();
                command.setParameter(Command.ParamName.TASK_NAME, taskName);
                break;
        }

        return command;
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

    private Pair<Command.ParamName, LocalDateTime> parseDateTime(Matcher matcher) {
        String date = matcher.group("DATE");
        String time = matcher.group("TIME");

        // TODO: Handle case when date is null
        date = date.toLowerCase();
        TimeClause clause = this._commandDefinitions.getTimeClause(date);
        Command.ParamName dateType = null;
        if (clause.getPrepositionMeanings().contains(TimePrepositionMeaning.STARTING)) {
            dateType = Command.ParamName.TASK_START;
        } else if (clause.getPrepositionMeanings().contains(TimePrepositionMeaning.ENDING)) {
            dateType = Command.ParamName.TASK_END;
        }

        LocalDateTime dateTime = null;
        switch (clause.getNoun()) {
            case TODAY:
                dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
                break;
            case TOMORROW:
                dateTime = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.DAYS);
                break;
            case NOW:
                dateTime = LocalDateTime.now();
                break;
            default:
                assert clause.getNoun().dayOfWeek != null;
                int todayDoW = LocalDate.now().getDayOfWeek().getValue();
                int dayOfWeek = clause.getNoun().dayOfWeek.getValue();

                int dayOffset = dayOfWeek - todayDoW;
                if (dayOffset < 0 || clause.getPrepositionMeanings().contains(TimePrepositionMeaning.NEXT)) {
                    dayOffset += 7;
                }

                dateTime = LocalDateTime.now().plusDays(dayOffset).truncatedTo(ChronoUnit.DAYS);
                break;
        }

        // Return result straight away if time is null
        if (time == null) {
            return new Pair<>(dateType, dateTime);
        }

        time = time.toLowerCase().replace(":",""); // Disregard colon
        boolean isAm = !time.contains("pm"); // Remember if there was pm
        time = time.replaceAll("[^0-9]",""); // Keep only numbers
        Integer intTime;
        try {
            intTime = Integer.parseInt(time);
        } catch (NumberFormatException e) {
            // Invalid date
            return null;
        }

        Integer hour, minute = null;
        if (intTime < 100) {
            hour = intTime;
        } else {
            hour = intTime / 100;
            minute = intTime % 100;
            if (minute > 59) {
                return null;
            }
        }

        if (hour >= 24 || // Hour cannot be more than 24
                hour > 12 || isAm) { // Specified AM but hour is more than 24
            return null; // Hour cannot be more than 24
        }
        if (hour != 12 && !isAm) {
            hour = (hour + 12) % 24;
        }

        dateTime = dateTime.withHour(hour);
        if (minute != null) {
            dateTime = dateTime.withMinute(minute);
        }

        return new Pair<>(dateType, dateTime);
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
