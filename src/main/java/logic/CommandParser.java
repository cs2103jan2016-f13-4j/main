package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.parser.*;
import shared.*;
import skeleton.CommandParserSpec;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;

/**
 * @@author A0127046L
 */
public class CommandParser implements CommandParserSpec {

    private static final String FILE_PARSER_DATA = "CommandParserData.json";

    public static final String MATCHER_GROUP_INSTRUCTION = "INST";
    private static final String MATCHER_GROUP_PREPOSITION_1 = "PREP1";
    private static final String MATCHER_GROUP_PREPOSITION_2 = "PREP2";
    public static final String MATCHER_GROUP_RELATIVE_TIME = "RELTIME";
    public static final String MATCHER_GROUP_DAY_OF_WEEK = "DOW";
    public static final String MATCHER_GROUP_DATE = "DATE";
    public static final String MATCHER_GROUP_TIME_OF_DAY = "TIME";
    public static final String MATCHER_GROUP_PRIORITY = "PRIO";
    private static final String MATCHER_GROUP_INDEX = "INDEX";
    private static final String MATCHER_GROUP_RANGE_START = "RSTART";
    private static final String MATCHER_GROUP_RANGE_END = "REND";
    private static final String MATCHER_GROUP_UNIVERSAL_QUANTIFIER = "UQ";
    private static final String MATCHER_GROUP_HOUR = "HOUR";
    private static final String MATCHER_GROUP_MINUTES = "MIN";
    private static final String MATCHER_GROUP_HOUR_DOT = "HDOT";

    private static final String STRING_INVALID_NAME_MISSING = "Task name is missing";
    private static final String STRING_INVALID_START_WITHOUT_END = "Task cannot have a start without an end";
    private static final String STRING_INVALID_EDIT_ID_MISSING = "You must tell me which task to edit";
    private static final String STRING_INVALID_RANGE_MISSING = "You must indicate an index or a range of tasks";
    private static final String STRING_INVALID_QUERY_MISSING = "You must specify a search query";
    private static final String STRING_INVALID_RANGE_FORMAT = "Invalid range";
    private static final String STRING_INVALID_SCHEDULE_ID_MISSING = "You must tell me which task to schedule";
    public static final String STRING_INVALID_DURATION_MISSING = "You must tell me how long you want your task to last!";

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
        this._instructionPattern = RegexUtils.startOfString(RegexUtils.wordBoundary(RegexUtils.namedChoice(
                MATCHER_GROUP_INSTRUCTION,
                this._definitions.getInstructionKeywords()
        )));
    }

    private void constructTimeRegExp() {
        this._startTimePattern = constructTimeRegexUsing(prep ->
                prep.getMeaning() != TimePreposition.Meaning.ENDING, false);
        this._endTimePattern = constructTimeRegexUsing(prep ->
                prep.getMeaning() != TimePreposition.Meaning.STARTING, true);
    }

    private String constructTimeRegexUsing(Predicate<? super TimePreposition> selectPredicate,
                                           boolean isPrepositionOptional) {
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
                // %1$s: Unchainable prepositions, optional based on
                // the condition specified in parameters
                isPrepositionOptional ? RegexUtils.optionalWord(RegexUtils.namedChoice(
                        MATCHER_GROUP_PREPOSITION_1,
                        unchainablePrepositions
                )) : RegexUtils.word(RegexUtils.namedChoice(
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
                command = this.parseAddCommand(command, partialCommand);
                break;
            case EDIT:
                command = this.parseEditCommand(command, partialCommand);
                break;
            case DELETE:
            case MARK:
                command = this.parseRangeCommand(command, partialCommand);
                break;
            case SEARCH:
                command = this.parseSearchCommand(command, partialCommand);
                break;
            case SCHEDULE:
                command = this.parseScheduleCommand(command, partialCommand);
                break;
        }

        return command;
    }

    private Command parseScheduleCommand(Command command, String partialCommand) {
        // Find index first
        Matcher indexMatcher = RegexUtils.caseInsensitiveMatch(
                this.getIndexRegex(),
                partialCommand
        );
        // No index found, return invalid command
        if (!indexMatcher.find()) {
            return Command.invalidCommand(STRING_INVALID_SCHEDULE_ID_MISSING);
        }

        // Set index first
        int taskId = Integer.parseInt(indexMatcher.group(MATCHER_GROUP_INDEX));
        command.setParameter(Command.ParamName.TASK_INDEX, taskId);

        // Truncate partial command string, with filler words too
        partialCommand = partialCommand.substring(indexMatcher.end()).trim();

        // Cut away filler words
        Matcher fillerMatcher = RegexUtils.caseInsensitiveMatch(
                RegexUtils.optionalWord("using"),
                partialCommand
        );
        if (fillerMatcher.find()) {
            partialCommand = partialCommand.substring(fillerMatcher.end()).trim();
        }

        // Get duration
        Matcher durationMatcher = RegexUtils.caseInsensitiveMatch(
                this.getDurationRegex(),
                partialCommand);

        int hour = 0;
        int minutes = 0;
        boolean matchFound = false;
        boolean hasDot = false;

        while (durationMatcher.find()) {
            matchFound = true;
            // Parse hour
            if (durationMatcher.group(MATCHER_GROUP_HOUR) != null) {
                hour = Integer.parseInt(durationMatcher.group(MATCHER_GROUP_HOUR));
                hasDot = false;
            }

            if (durationMatcher.group(MATCHER_GROUP_HOUR_DOT) != null) {
                hasDot = true;
            }

            // Parse minutes
            if (durationMatcher.group(MATCHER_GROUP_MINUTES) != null) {
                minutes = Integer.parseInt(durationMatcher.group(MATCHER_GROUP_MINUTES));

                // If the dot exists, we need to be able to find the minutes
                // in terms of decimal places
                if (minutes != 0 && hasDot) {
                    minutes = minutes * 6;
                }
            }
        }

        // If there is no duration, return error
        if (!matchFound) {
            return Command.invalidCommand(STRING_INVALID_DURATION_MISSING);
        }


        // Find the final duration and set it to the command
        int duration = hour * 60 + minutes;
        command.setParameter(Command.ParamName.TASK_DURATION, duration);

        return command;
    }

    /**
     * Attempts to parse the partial command string into a valid <code>ADD</code>
     * command.
     * @param command a command object
     * @param partialCommand a string containing parameters
     * @return the processed command
     */
    private Command parseAddCommand(Command command, String partialCommand) {
        // Start by finding the parameters, and keep track of the lowest
        // index found using the regex. Truncating from this index onwards will
        // give us the true task name
        int lowestFoundIndex = Math.min(
                this.parseTimeParameters(command, partialCommand),
                this.parsePriorityParameters(command, partialCommand)
        );

        String taskName = partialCommand.substring(0, lowestFoundIndex).trim();
        if (StringUtils.isSurroundedByQuotes(taskName)) {
            taskName = StringUtils.stripEndCharacters(taskName);
        }

        // Verify that task name is not null or empty
        if (taskName.trim().isEmpty()) {
            return Command.invalidCommand(STRING_INVALID_NAME_MISSING);
        }
        command.setParameter(Command.ParamName.TASK_NAME, taskName);

        // Verify that it must have an end if it has a start
        if (command.hasParameter(Command.ParamName.TASK_START) &&
                !command.hasParameter(Command.ParamName.TASK_END)) {
            return Command.invalidCommand(STRING_INVALID_START_WITHOUT_END);
        }
        return command;
    }

    private Command parseEditCommand(Command command, String partialCommand) {
        // Get the index first
        Matcher indexMatcher = RegexUtils.caseInsensitiveMatch(
                this.getIndexRegex(),
                partialCommand
        );
        // Verify index's existence
        if (!indexMatcher.find()) {
            return Command.invalidCommand(STRING_INVALID_EDIT_ID_MISSING);
        }

        // Set ID parameter first, then truncate task
        int taskId = Integer.parseInt(indexMatcher.group(MATCHER_GROUP_INDEX));
        command.setParameter(Command.ParamName.TASK_INDEX, taskId);
        partialCommand = partialCommand.substring(indexMatcher.end());

        // Parse time parameters next, keeping track of lowest index
        int lowestFoundIndex = Math.min(
                this.parseTimeParameters(command, partialCommand),
                this.parsePriorityParameters(command, partialCommand)
        );

        String taskName = partialCommand.substring(0, lowestFoundIndex).trim();
        if (StringUtils.isSurroundedByQuotes(taskName)) {
            taskName = StringUtils.stripEndCharacters(taskName);
        }

        // Keep the task name if it's not empty
        if (!taskName.trim().isEmpty()) {
            command.setParameter(Command.ParamName.TASK_NAME, taskName);
        }

        return command;
    }

    private Command parseRangeCommand(Command command, String partialCommand) {
        Matcher rangeMatcher = RegexUtils.caseInsensitiveMatch(
                this.getRangeRegex(),
                partialCommand
        );

        List<Range> rangeList = new ArrayList<>();

        while (rangeMatcher.find()) {
            if (rangeMatcher.group(MATCHER_GROUP_RANGE_START) != null) {
                // Create new range
                Range newRange = new Range(
                        Integer.parseInt(rangeMatcher.group(MATCHER_GROUP_RANGE_START))
                );
                rangeList.add(newRange);
            }

            if (rangeMatcher.group(MATCHER_GROUP_RANGE_END) != null) {
                // Hypothetically, the case when an end exists while there were no
                // previous range or the previous range already has an end is not
                // likely to happen, but we will catch it anyway
                if (rangeList.isEmpty() || rangeList.get(rangeList.size() - 1).hasEnd()) {
                    return Command.invalidCommand(STRING_INVALID_RANGE_FORMAT);
                }

                Range lastRange = rangeList.get(rangeList.size() - 1);
                lastRange.setEnd(
                        Integer.parseInt(rangeMatcher.group(MATCHER_GROUP_RANGE_END))
                );
            }
        }

        // If no match found, we attempt to find the universal quantifier instead
        if (rangeList.isEmpty()) {
            Matcher universalQuantifierMatcher = RegexUtils.caseInsensitiveMatch(
                    this.getUniversalQuantifierRegex(),
                    partialCommand
            );

            if (!universalQuantifierMatcher.find()) {
                // No universal quantifier found either, command is invalid!
                return Command.invalidCommand(STRING_INVALID_RANGE_MISSING);
            }

            // Set as universally quantified and exit straight away
            command.setParameter(Command.ParamName.TASK_UNIVERSALLY_QUANTIFIED, true);
            return command;
        }

        // Straighten the range first
        Range.straightenRanges(rangeList);

        // Set command parameters
        command.setParameter(Command.ParamName.TASK_INDEX_RANGES, rangeList);

        return command;
    }

    private Command parseSearchCommand(Command command, String partialCommand) {
        String fillerPattern = RegexUtils.optionalWord(
                RegexUtils.noSurroundingQuotes("for")
        );
        Matcher fillerMatcher = RegexUtils.caseInsensitiveMatch(
                fillerPattern,
                partialCommand
        );
        // Truncate up to filler
        if (fillerMatcher.find()) {
            partialCommand = partialCommand.substring(fillerMatcher.end()).trim();
        }
        // Remove quotes if required
        String searchQuery = partialCommand;
        if (StringUtils.isSurroundedByQuotes(searchQuery)) {
            searchQuery = StringUtils.stripEndCharacters(searchQuery);
        }

        if (searchQuery.trim().isEmpty()) {
            return Command.invalidCommand(STRING_INVALID_QUERY_MISSING);
        }

        // Set parameter and finish
        command.setParameter(Command.ParamName.SEARCH_QUERY, searchQuery);
        return command;
    }


    /**
     * TODO: Write JavaDoc
     * @param command
     * @param partialCommand
     * @return the lowest index that matches any time string
     */
    private int parseTimeParameters(Command command, String partialCommand) {
        // Keep track of the lowest found index
        int lowestFoundIndex = partialCommand.length();
        // Also keep track of the highest found index so that end time
        // can be differentiated from start time
        int highestFoundIndex = 0;

        // Prepare time variables
        CustomTime startTime = null;
        CustomTime endTime = null;

        // Match start time
        Matcher startTimeMatcher = RegexUtils.caseInsensitiveMatch(
                this._startTimePattern,
                partialCommand
        );
        if (startTimeMatcher.find()) {
            startTime = new CustomTime(
                    this.parseDate(startTimeMatcher),
                    this.parseTime(startTimeMatcher)
            );

            // Save the highest and lowest found index
            if (startTimeMatcher.end() > highestFoundIndex) {
                highestFoundIndex = startTimeMatcher.end();
            }
            if (startTimeMatcher.start() < lowestFoundIndex) {
                lowestFoundIndex = startTimeMatcher.start();
            }
        }

        // Match end time
        Matcher endTimeMatcher = RegexUtils.caseInsensitiveMatch(
                this._endTimePattern,
                partialCommand
        );
        if (endTimeMatcher.find(highestFoundIndex)) {
            endTime = new CustomTime(
                    this.parseDate(endTimeMatcher),
                    this.parseTime(endTimeMatcher)
            );
            if (endTimeMatcher.start() < lowestFoundIndex) {
                lowestFoundIndex = endTimeMatcher.start();
            }
        }

        // Finally register the time to the command object
        if (startTime != null && !startTime.isNullDate()) {
            command.setParameter(Command.ParamName.TASK_START, startTime);
        }
        if (endTime != null && !endTime.isNullDate()) {
            command.setParameter(Command.ParamName.TASK_END, endTime);
        }

        // return the lowest found index
        return lowestFoundIndex;
    }

    private int parsePriorityParameters(Command command, String partialCommand) {
        Matcher matcher = RegexUtils.caseInsensitiveMatch(
                this._priorityPattern,
                partialCommand
        );
        // If cannot find, return lowest found to be string length
        if (!matcher.find()) {
            return partialCommand.length();
        }

        Task.Priority priority = this._definitions.queryPriority(
                matcher.group(MATCHER_GROUP_PRIORITY).toLowerCase()
        );
        command.setParameter(Command.ParamName.PRIORITY_VALUE, priority);

        return matcher.start();
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

    private String getIndexRegex() {
        return String.format("^(?:task\\s+)?(?:number(?:ed)?\\s+)?(?<%s>\\d+)",
                MATCHER_GROUP_INDEX);
    }

    private String getRangeRegex() {
        return String.format("(?:all\\s+)?(?:task(?:s)?\\s+)?(?:number(?:ed)?\\s+)?" +
                        "(?:(?<%s>\\d+)(?:\\s*(?:to|-)\\s*(?<%s>\\d+))?)",
                MATCHER_GROUP_RANGE_START,
                MATCHER_GROUP_RANGE_END);
    }

    private String getUniversalQuantifierRegex() {
        return RegexUtils.startOfString(RegexUtils.namedChoice(
                MATCHER_GROUP_UNIVERSAL_QUANTIFIER,
                "all", "everything"
        ));
    }

    private String getDurationRegex() {
        // This will be used for capturing the dot
        String timeDelimiterPattern1 = RegexUtils.unbracketedChoice(
                "h", "\\s+hour(?:s)?\\s+", RegexUtils.namedGroup(
                        MATCHER_GROUP_HOUR_DOT, "\\."
                )
        );
        // This will not, just for negative lookahead
        String timeDelimiterPattern2 = RegexUtils.unbracketedChoice(
                "h", "\\s+hour(?:s)?\\s+", "\\."
        );

        // Construct the patterns
        String hourPattern = RegexUtils.namedGroup(
                MATCHER_GROUP_HOUR,
                RegexUtils.positiveLookahead(
                        "\\d+",
                        timeDelimiterPattern1
                )
        );
        String minutesPattern = RegexUtils.namedGroup(
                MATCHER_GROUP_MINUTES,
                RegexUtils.negativeLookahead(
                        "\\d+",
                        timeDelimiterPattern2
                )
        );
        return RegexUtils.unbracketedChoice(
                hourPattern, minutesPattern
        );
    }

    //-------------------------------------------------------------------------------------------------
    //
    // GETTERS for constructed regexp
    //
    //-------------------------------------------------------------------------------------------------
    public String getInstructionPattern() {
        return this._instructionPattern;
    }

    public String getStartTimePattern() {
        return this._startTimePattern;
    }

    public String getEndTimePattern() {
        return this._endTimePattern;
    }

    public String getPriorityPattern() {
        return this._priorityPattern;
    }
}
