package logic.parser;

import shared.Command;
import shared.Task;

import java.time.DayOfWeek;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @@author Mai Anh Vu
 */
public class ParserDefinitions {

    /**
     * Properties
     */

    // Instructions
    private List<Instruction> _instructions;
    private LinkedHashMap<String, Command.Instruction> _instructionTranslator;

    // Time Prepositions
    private List<TimePreposition> _timePrepositions;
    private LinkedHashMap<String, TimePreposition.Meaning> _timePrepositionTranslator;

    // Time Nouns
    private List<TimeNoun> _timeNouns;
    private LinkedHashMap<String, TimeNoun.Relative> _relativeTimeTranslator;
    private LinkedHashMap<String, DayOfWeek> _absoluteTimeTranslator;

    // Priority (plus Prepositions)
    private Set<String> _priorityPrepositions;
    private List<Priority> _priorities;
    private LinkedHashMap<String, Task.Priority> _priorityTranslator;

    // Caching behaviours
    private String[] _cachedInstructionKeywords;
    private String[] _cachedPriorityPrepositionKeywords;

    public ParserDefinitions() {
        this._instructionTranslator = new LinkedHashMap<>();
        this._timePrepositionTranslator = new LinkedHashMap<>();
        this._relativeTimeTranslator = new LinkedHashMap<>();
        this._absoluteTimeTranslator = new LinkedHashMap<>();
        this._priorityTranslator = new LinkedHashMap<>();
    }

    //-------------------------------------------------------------------------------------------------
    //
    // SETTER METHODS
    //
    //-------------------------------------------------------------------------------------------------
    /**
     * Registers a set of instructions to be used under this definition.
     * @param instructions a set of {@link Instruction}
     */
    public void setInstructions(List<Instruction> instructions) {
        this._instructions = instructions;

        instructions.stream().forEach(instruction -> {
            instruction.getKeywords().stream().forEach(keyword -> {
                this._instructionTranslator.put(
                        keyword,
                        instruction.getName());
            });
        });
    }

    /**
     * Registers a set of time prepositions to be used under this definition.
     * @param prepositions a set of {@link TimePreposition}
     */
    public void setTimePrepositions(List<TimePreposition> prepositions) {
        this._timePrepositions = prepositions;

        // Populate time prepositions translation
        prepositions.stream().forEach(preposition -> {
            preposition.getKeywords().forEach(keyword -> {
                this._timePrepositionTranslator.put(
                        keyword,
                        preposition.getMeaning());
            });
        });
    }

    /**
     * Registers a set of time nouns to be used under this definition. This method
     * populates both relative and absolute time nouns.
     * @param nouns a set of {@link TimeNoun}
     */
    public void setTimeNouns(List<TimeNoun> nouns) {
        this._timeNouns = nouns;

        // Populate relative time nouns translation
        nouns.stream().filter(TimeNoun::isRelative).forEach(noun -> {
            noun.getKeywords().forEach(keyword -> {
                this._relativeTimeTranslator.put(
                        keyword,
                        noun.getRelativeMeaning()
                );
            });
        });

        // Populate absolute time nouns translation
        nouns.stream().filter(noun -> !noun.isRelative()).forEach(noun -> {
            noun.getKeywords().forEach(keyword -> {
                this._absoluteTimeTranslator.put(
                        keyword,
                        noun.getAbsoluteMeaning()
                );
            });
        });
    }

    /**
     * Registers a set of priority preposition keywords to be used under this
     * definition.
     * @param prepositions a set of preposition keywords
     */
    public void setPriorityPrepositions(Set<String> prepositions) {
        this._priorityPrepositions = prepositions;
    }

    /**
     * Registers a set of priority nouns to be used under this definition.
     * @param priorities a set of {@link Priority}
     */
    public void setPriorities(List<Priority> priorities) {
        this._priorities = priorities;

        // Populate priorities translation
        priorities.stream().forEach(priority -> {
            priority.getKeywords().forEach(keyword -> {
                this._priorityTranslator.put(
                        keyword,
                        priority.getMeaning());
            });
        });
    }

    //-------------------------------------------------------------------------------------------------
    //
    // GETTER METHODS
    //
    //-------------------------------------------------------------------------------------------------
    public String[] getInstructionKeywords() {
        if (this._cachedInstructionKeywords == null) {
            this._cachedInstructionKeywords = this._instructions.stream()
                    .map(Instruction::getKeywords)
                    .flatMap(Set::stream)
                    .toArray(String[]::new);
        }
        return this._cachedInstructionKeywords;
    }

    public List<TimePreposition> getTimePrepositions() {
        return this._timePrepositions;
    }

    public List<TimeNoun> getTimeNouns() {
        return this._timeNouns;
    }

    public Set<TimeNoun> getRelativeDates() {
        return this.getTimeNouns().stream()
                .filter(TimeNoun::isRelative)
                .collect(Collectors.toSet());
    }

    public String[] getPriorityPrepositionKeywords() {
        if (this._cachedPriorityPrepositionKeywords == null) {
            this._cachedPriorityPrepositionKeywords = this._priorityPrepositions
                    .stream().toArray(String[]::new);
        }
        return this._cachedPriorityPrepositionKeywords;
    }

    public List<Priority> getPriorities() {
        return this._priorities;
    }

    public Command.Instruction queryInstruction(String instruction) {
        return this._instructionTranslator.get(instruction);
    }

    public TimeNoun.Relative queryRelativeDate(String date) {
        return this._relativeTimeTranslator.get(date);
    }

    public TimePreposition.Meaning queryTimePreposition(String preposition) {
        return this._timePrepositionTranslator.get(preposition);
    }

    public DayOfWeek queryDayOfWeek(String dayOfWeekString) {
        return this._absoluteTimeTranslator.get(dayOfWeekString);
    }
}
