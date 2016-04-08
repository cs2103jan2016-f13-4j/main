package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.parser.*;
import shared.Command;
import shared.Resources;
import skeleton.CommandParserSpec;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by maianhvu on 06/04/2016.
 */
public class CommandParser implements CommandParserSpec {

    private static final String FILE_PARSER_DATA = "CommandParserData.json";

    private static final String MATCHER_GROUP_INSTRUCTION = "INST";
    private static final String MATCHER_GROUP_PREPOSITION_RELATIVE = "RELPREP";
    private static final String MATCHER_GROUP_PREPOSITION_ABSOLUTE = "ABSPREP";
    private static final String MATCHER_GROUP_PREPOSITION_DATE = "DATEPREP";
    private static final String MATCHER_GROUP_RELATIVE_DATE = "RELDATE";
    private static final String MATCHER_GROUP_ABSOLUTE_DATE = "ABSDATE";
    private static final String MATCHER_GROUP_TIME = "TIME";
    private static final String MATCHER_GROUP_PRIORITY = "PRIO";

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
    private String _instructionPattern;
    private String _timePattern;
    private String _priorityPattern;

    private CommandParser() {

    }

    @Override
    public void initialise() {
        this.readDataFromJson();
        this.constructRegularExpression();
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

    //-------------------------------------------------------------------------------------------------
    //
    // REGULAR EXPRESSIONS construction
    //
    //-------------------------------------------------------------------------------------------------
    private void constructRegularExpression() {
        this.constructInstructionRegExp();
        this.constructTimeRegExp();
        this.constructPriorityRegExp();
    }

    private void constructInstructionRegExp() {
        assert this._definitions != null && this._definitions.getInstructionKeywords() != null;
        this._instructionPattern = RegexUtils.startOfString(RegexUtils.namedChoice(
                MATCHER_GROUP_INSTRUCTION,
                this._definitions.getInstructionKeywords()
        ));
    }

    private void constructTimeRegExp() {
        //=====================================================================
        // RELATIVE TIME
        //=====================================================================
        // Consider relative nouns first, they do not require prepositions
        // so we can consider the prepositions as optionals.
        // In addition, they have no chainable prepositions so we don't have
        // to worry about those things.
        Set<TimeNoun> relativeDates = this._definitions.getTimeNouns().stream()
                .filter(TimeNoun::isRelative)
                .collect(Collectors.toSet());
        String[] relativeDateKeywords = relativeDates.stream()
                .map(TimeNoun::getKeywords)
                .flatMap(Set::stream)
                .toArray(String[]::new);

        // Find out the kinds of prepositions allowed to precede a relative noun
        Set<TimePreposition.Meaning> allowedRelativePrepositions = relativeDates.stream()
                .map(TimeNoun::getPrepositions)
                .flatMap(Set::stream)
                .filter(meaning -> meaning != TimePreposition.Meaning.ENDING) // Cannot be ending
                .collect(Collectors.toSet());

        // Now we will chain them up and construct regular expressions for them
        String[] prepositionKeywords = this._definitions.getTimePrepositions().stream()
                .filter(prep -> allowedRelativePrepositions.contains(prep.getMeaning()))
                .map(TimePreposition::getKeywords)
                .flatMap(Set::stream)
                .toArray(String[]::new);

        String relativeStartTimeRegex = RegexUtils.optionalWord(RegexUtils.namedChoice(
                MATCHER_GROUP_PREPOSITION_RELATIVE,
                prepositionKeywords
        )).concat(RegexUtils.namedChoice(
                MATCHER_GROUP_RELATIVE_DATE,
                relativeDateKeywords
        ));

        //=====================================================================
        // ABSOLUTE TIME I (keyword absolute time)
        //=====================================================================
        // This time it's different because there are chainable prepositions,
        // and we want to catch these things too
        Set<TimeNoun> absoluteDates = this._definitions.getTimeNouns().stream()
                .filter(noun -> !noun.isRelative())
                .collect(Collectors.toSet());
        String[] absoluteDateKeywords = absoluteDates.stream()
                .map(TimeNoun::getKeywords)
                .flatMap(Set::stream)
                .toArray(String[]::new);

        Set<TimePreposition.Meaning> allowedAbsolutePrepositions = absoluteDates.stream()
                .map(TimeNoun::getPrepositions)
                .flatMap(Set::stream)
                .filter(meaning -> meaning != TimePreposition.Meaning.ENDING) // Do not want ending
                .collect(Collectors.toSet());

        String[] firstPrepKeywords = this._definitions.getTimePrepositions().stream()
                .filter(prep -> allowedAbsolutePrepositions.contains(prep.getMeaning()))
                .filter(prep -> !prep.isChainable())
                .map(TimePreposition::getKeywords)
                .flatMap(Set::stream)
                .toArray(String[]::new);

        String[] secondPrepKeywords = this._definitions.getTimePrepositions().stream()
                .filter(prep -> allowedAbsolutePrepositions.contains(prep.getMeaning()))
                .filter(TimePreposition::isChainable)
                .map(TimePreposition::getKeywords)
                .flatMap(Set::stream)
                .toArray(String[]::new);

        String absoluteStartTimeRegex1 = RegexUtils.namedGroup(
                MATCHER_GROUP_PREPOSITION_ABSOLUTE,
                // First Prepositions
                RegexUtils.word(RegexUtils.choice(firstPrepKeywords)).concat(
                        // Second Prepositions
                        RegexUtils.optionalWord(RegexUtils.choice(secondPrepKeywords))
                )
        ).concat(RegexUtils.namedChoice(
                MATCHER_GROUP_ABSOLUTE_DATE,
                absoluteDateKeywords
        ));

        //=====================================================================
        // ABSOLUTE TIME II (date absolute time)
        //=====================================================================
        String absoluteStartTimeRegex2 = RegexUtils.word(RegexUtils.namedChoice(
                MATCHER_GROUP_PREPOSITION_DATE,
                // Only unchainable prepositions
                firstPrepKeywords
        )).concat(RegexUtils.dateRegex());

        //=====================================================================
        // TIME OF DAY REGEX
        //=====================================================================
        String timeRegex = "\\s+" + RegexUtils.namedGroup(
                MATCHER_GROUP_TIME,
                RegexUtils.timeRegex()
        );

        // Combine them all!
        this._timePattern = String.format("%s%s%s",
                RegexUtils.choice(
                        relativeStartTimeRegex,
                        absoluteStartTimeRegex1,
                        absoluteStartTimeRegex2
                ),
                RegexUtils.optional("'s"),
                RegexUtils.optional(timeRegex)
        );
    }

    private void constructPriorityRegExp() {
        String[] priorityKeywords = this._definitions.getPriorities().stream()
                .map(Priority::getKeywords)
                .flatMap(Set::stream)
                .toArray(String[]::new);

        this._priorityPattern = RegexUtils.word(
                RegexUtils.choice(this._definitions.getPriorityPrepositionKeywords())
        ).concat(RegexUtils.namedChoice(
                MATCHER_GROUP_PRIORITY,
                priorityKeywords
        ));
    }

    @Override
    public Command parse(String commandString) {
        return null;
    }
}
