package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.parser.*;
import shared.Command;
import shared.CustomTime;
import shared.Resources;
import skeleton.CommandParserSpec;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;

/**
 * Created by maianhvu on 06/04/2016.
 */
public class CommandParser implements CommandParserSpec {

    private static final String FILE_PARSER_DATA = "CommandParserData.json";

    private static final String MATCHER_GROUP_INSTRUCTION = "INST";
    private static final String MATCHER_GROUP_PREPOSITION_1 = "PREP1";
    private static final String MATCHER_GROUP_PREPOSITION_2 = "PREP2";
    private static final String MATCHER_GROUP_RELATIVE_TIME = "RELTIME";
    private static final String MATCHER_GROUP_DAY_OF_WEEK = "DOW";
    private static final String MATCHER_GROUP_DATE = "DATE";
    private static final String MATCHER_GROUP_TIME_OF_DAY = "TIME";
    private static final String MATCHER_GROUP_PRIORITY = "PRIO";

    private static final String STRING_INVALID_HOUR = "Invalid hour";

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
    private String _startTimePattern;
    private String _endTimePattern;
    private String _priorityPattern;

    private CommandParser() {

    }

    @Override
    public void initialise() {
        this.readDataFromJson();
        this.constructRegularExpressions();
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
    private void constructRegularExpressions() {
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
        this._startTimePattern = constructTimeRegexUsing(prep ->
                prep.getMeaning() != TimePreposition.Meaning.ENDING);
        this._endTimePattern = constructTimeRegexUsing(prep ->
                prep.getMeaning() != TimePreposition.Meaning.STARTING);
    }

    private String constructTimeRegexUsing(Predicate<? super TimePreposition> selectPredicate) {
        //=====================================================================
        // UNCHAINABLE PREPOSITIONS
        //=====================================================================
        // Unchainable preposition is ALWAYS needed in the regular expression.
        // That leaves us with two alternatives: those with only one unchainable
        // preposition, and those with chainable prepositions. We consider the
        // former first
        String[] unchainablePrepositions = this._definitions.getTimePrepositions()
                .stream()
                .filter(selectPredicate)
                .filter(prep -> !prep.isChainable())
                .map(TimePreposition::getKeywords)
                .flatMap(Set::stream)
                .toArray(String[]::new);

        //=====================================================================
        // RELATIVE TIME
        //=====================================================================
        String[] relativeTimeNouns = this._definitions.getTimeNouns().stream()
                .filter(TimeNoun::isRelative)
                .map(TimeNoun::getKeywords)
                .flatMap(Set::stream)
                .toArray(String[]::new);

        //=====================================================================
        // ABSOLUTE DATE
        //=====================================================================
        // We bring in absolute date first because they cannot have a
        // chainable preposition in front of them, just like relative time
        String absoluteDates = RegexUtils.dateRegex();

        //=====================================================================
        // DAY OF WEEK
        //=====================================================================
        // Day of week type may opt to have a chainable preposition
        // in front of them
        String[] chainablePrepositions = this._definitions.getTimePrepositions()
                .stream()
                .filter(TimePreposition::isChainable)
                .filter(selectPredicate)
                .map(TimePreposition::getKeywords)
                .flatMap(Set::stream)
                .toArray(String[]::new);
        // Find out the absolute time nouns
        String[] dayOfWeeks = this._definitions.getTimeNouns().stream()
                .filter(noun -> !noun.isRelative())
                .map(TimeNoun::getKeywords)
                .flatMap(Set::stream)
                .toArray(String[]::new);
        //=====================================================================
        // TIME OF DAY
        //=====================================================================
        String timeOfDayPattern = "\\s+" + RegexUtils.namedGroup(
                MATCHER_GROUP_TIME_OF_DAY,
                RegexUtils.timeRegex()
        );

        String aggregatePattern = String.format("%1$s%2$s%3$s%4$s",
                // %1$s: Unchainable prepositions (required)
                RegexUtils.word(RegexUtils.namedChoice(
                        MATCHER_GROUP_PREPOSITION_1,
                        unchainablePrepositions
                )),
                // %2$s: Either relative time, exact date, or
                // a combination of a chainable preposition
                // (optional), and a day of week matcher
                RegexUtils.choice(
                        RegexUtils.namedChoice(
                                MATCHER_GROUP_RELATIVE_TIME,
                                relativeTimeNouns
                        ),
                        RegexUtils.namedChoice(
                                MATCHER_GROUP_DATE,
                                absoluteDates
                        ),
                        RegexUtils.optionalWord(RegexUtils.namedChoice(
                                MATCHER_GROUP_PREPOSITION_2,
                                chainablePrepositions
                        )).concat(RegexUtils.namedChoice(
                                MATCHER_GROUP_DAY_OF_WEEK,
                                dayOfWeeks
                        ))
                ),
                // %3$s: Optional, the English possessive suffix 's
                RegexUtils.optional("'s"),
                // %4$s: Optional, the time of the day
                RegexUtils.optional(timeOfDayPattern));

        // We don't want to parse this if it's wrapped within quotes
        aggregatePattern = RegexUtils.noSurroundingQuotes(aggregatePattern);

        return aggregatePattern;
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
        // Construct the instruction first from the command string
        Matcher instructionMatcher = RegexUtils.caseInsensitiveMatch(
                this._instructionPattern,
                commandString);

        // Cannot find instruction, return unrecognised
        if (!instructionMatcher.find()) {
            return Command.unrecognisedCommand();
        }

        // Find out the instruction type and start parsing
        Command.Instruction instruction = this._definitions.queryInstruction(
                instructionMatcher.group(MATCHER_GROUP_INSTRUCTION).toLowerCase()
        );
        // Prepare a command object to start pushing parameters inside
        Command command = new Command(instruction);
        // Cut off the instruction to prepare for command parsing
        String partialCommand = commandString.substring(instructionMatcher.end()).trim();

        switch (instruction) {
            case ADD:
                // Start by finding the start time
                this.parseTimeParameters(partialCommand, command);
                break;
            case EDIT:
                break;
            case DELETE:
            case MARK:
                break;
            default:
                break;
        }

        return command;
    }

    private void parseTimeParameters(String partialCommand, Command command) {
        // Match start time
        Matcher startTimeMatcher = RegexUtils.caseInsensitiveMatch(
                this._startTimePattern,
                partialCommand
        );
        CustomTime startTime = startTimeMatcher.find() ? new CustomTime(
                this.parseDate(startTimeMatcher),
                this.parseTime(startTimeMatcher)
        ) : null;

        // Match end time
        Matcher endTimeMatcher = RegexUtils.caseInsensitiveMatch(
                this._endTimePattern,
                partialCommand
        );
        CustomTime endTime = endTimeMatcher.find() ? new CustomTime(
                this.parseDate(endTimeMatcher),
                this.parseTime(endTimeMatcher)
        ) : null;

        // Finally register the time to the command object
        if (startTime != null && !startTime.isNullDate()) {
            command.setParameter(Command.ParamName.TASK_START, startTime);
        }
        if (endTime != null && !endTime.isNullDate()) {
            command.setParameter(Command.ParamName.TASK_END, endTime);
        }
    }

    private LocalDate parseDate(Matcher matcher) {
        // There are 3 cases:
        //     I. There is a relative time (now, today, same day, etc)
        //    II. There is a day of week (mon, tue, etc)
        //   III. There is an absolute date (Mar 21 2016 etc)

        //=====================================================================
        // I. RELATIVE TIME
        //=====================================================================
        if (matcher.group(MATCHER_GROUP_RELATIVE_TIME) != null) {
            String relativeTimeString = matcher.group(MATCHER_GROUP_RELATIVE_TIME)
                    .trim().toLowerCase();
            TimeNoun.Relative meaning = this._definitions.queryRelativeDate(relativeTimeString);
            switch (meaning) {
                case NOW:
                case TODAY:
                    return LocalDate.now();
                case TOMORROW:
                    return LocalDate.now().plusDays(1);
                default:
                    return null;
            }
        }

        //=====================================================================
        // II. DAY OF WEEK
        //=====================================================================
        if (matcher.group(MATCHER_GROUP_DAY_OF_WEEK) != null) {
            // Find out if the prepositions tell us that we should look for the
            // next week's instant instead
            boolean isNextWeek = false;

            isNextWeek = this.isImplyingNextWeek(matcher.group(MATCHER_GROUP_PREPOSITION_1));
            isNextWeek = this.isImplyingNextWeek(matcher.group(MATCHER_GROUP_PREPOSITION_2));

            // Get the day of week first
            DayOfWeek dayToConsider = this._definitions.queryDayOfWeek(
                    matcher.group(MATCHER_GROUP_DAY_OF_WEEK).trim().toLowerCase()
            );

            // In some cases, next week's might mean this week! For example, if today is
            // Sunday, then next Monday will mean tomorrow (the currently upcoming Monday!).
            // If this week's instance of the day is already past, then next actually
            // means current
            DayOfWeek today = LocalDate.now().getDayOfWeek();

            if (isNextWeek && dayToConsider.getValue() <= today.getValue()) {
                isNextWeek = false;
            }

            // Now that we are certain what we want, let's find the date!
            CustomTime temporary = CustomTime.now();
            if (!isNextWeek) {
                // Current week
                return temporary.current(dayToConsider).getDate();
            } else {
                // Next week
                return temporary.next(dayToConsider).getDate();
            }
        }

        //=====================================================================
        // III. ABSOLUTE DATE
        //=====================================================================
        if (matcher.group(MATCHER_GROUP_DATE) != null) {
            String day = matcher.group(RegexUtils.MATCHER_GROUP_DATE_DAY);
            String month = matcher.group(RegexUtils.MATCHER_GROUP_DATE_MONTH);
            String year = matcher.group(RegexUtils.MATCHER_GROUP_DATE_YEAR);

            // TODO: Catch this error
            // We don't want the date and the month to be null
            if (day == null || month == null) {
                return null;
            }

            int dayValue = Integer.parseInt(day);
            // Linear search through the months to find the correct month
            // We only want the first 3 letters for comparison (because
            // at the minimum the month will be recognised by its first
            // 3 characters)
            final String shortMonth = month.substring(0, 3);
            Month monthValue = Arrays.stream(Month.values())
                    .filter(m -> m.name().substring(0, 3).equalsIgnoreCase(shortMonth))
                    .findFirst()
                    .orElse(null); // This should not happen

            int yearValue = year != null ? Integer.parseInt(year) :
                    LocalDate.now().getYear();

            return LocalDate.of(yearValue, monthValue, dayValue);
        }

        // Nothing found, return null
        return null;
    }

    private LocalTime parseTime(Matcher matcher) {
        // Cannot find time of day
        if (matcher.group(MATCHER_GROUP_TIME_OF_DAY) == null) {
            return null;
        }
        // Lower the string and remove any colon
        String time = matcher.group(MATCHER_GROUP_TIME_OF_DAY)
                .toLowerCase().replace(":", "");
        boolean isPM = time.contains("pm");
        // Now that we have saved whether the string contains the word PM
        // or not, we can proceed on to discard it and parse the time
        int integerTime = Integer.parseInt(time.replaceAll("[^0-9]", ""));

        // Try to get the time. We will regard anything greater than 100
        // as following the HH:mm format, while the rest should be treated
        // as just hours and without minutes
        int hour = integerTime >= 100 ? integerTime / 100 : integerTime;
        if (hour < 12 && isPM) {
            hour += 12;
        }
        // TODO: Catch this error
        // Invalid hour, we return null
        if (hour >= 24) {
            return null;
        }

        ChronoUnit precision = ChronoUnit.HOURS;
        int minute = 0;
        if (integerTime >= 100) {
            minute = integerTime % 100;
            precision = ChronoUnit.MINUTES;
        }

        return LocalTime.of(hour, minute);
    }

    private boolean isImplyingNextWeek(String preposition) {
        // Non-existent, cannot imply
        if (preposition == null) {
            return false;
        }
        preposition = preposition.trim().toLowerCase();
        TimePreposition.Meaning meaning = this._definitions.queryTimePreposition(preposition);
        return meaning == TimePreposition.Meaning.NEXT;
    }

}