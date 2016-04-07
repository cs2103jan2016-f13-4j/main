package logic.parser;

import com.google.gson.JsonObject;

import java.time.DayOfWeek;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @@author Mai Anh Vu
 */
public class TimeNoun {
    /**
     * Constants
     */
    private static final String KEY_JSON_MEANING = "meaning";
    private static final String KEY_JSON_KEYWORDS = "keywords";
    private static final String KEY_JSON_PREPOSITIONS = "prepositions";
    private static final String KEY_JSON_CGWP = "canGoWithoutPrepositions";

    /**
     * Types
     */
    public enum Relative {
        NOW, TODAY, TOMORROW, SAME_DAY
    }

    /**
     * Properties
     */
    private boolean _isRelative;
    private Relative _relativeMeaning;
    private DayOfWeek _absoluteMeaning;
    private Set<String> _keywords;
    private Set<TimePreposition.Meaning> _prepositions;
    private boolean _canGoWithoutPrepositions;

    /**
     * Constructs a time noun from the JSON data.
     * @param nounObject a JSON object containing the data for the time noun
     */
    public TimeNoun(JsonObject nounObject) {
        assert nounObject.has(KEY_JSON_MEANING);
        assert nounObject.has(KEY_JSON_KEYWORDS);
        assert nounObject.has(KEY_JSON_PREPOSITIONS);
        assert nounObject.has(KEY_JSON_CGWP);

        // Parse meaning
        String meaning = nounObject.get(KEY_JSON_MEANING).getAsString();
        // Look for the meaning inside the relative nouns list
        this._relativeMeaning = JsonUtils.findEnumValue(meaning, Relative.class);
        // Check whether it's null, and if it is we continue to look for this value
        // inside the absolute days of week list
        if (!(this._isRelative = (this._relativeMeaning != null))) {
            this._absoluteMeaning = JsonUtils.findEnumValue(meaning, DayOfWeek.class);
        }

        // Parse keywords
        this._keywords = JsonUtils.toStringSet(nounObject.get(KEY_JSON_KEYWORDS).getAsJsonArray());

        // Parse set of preposition meanings
        this._prepositions = JsonUtils.toStringSet(nounObject.get(KEY_JSON_PREPOSITIONS).getAsJsonArray())
                .stream()
                .map(prep -> JsonUtils.findEnumValue(prep, TimePreposition.Meaning.class))
                .collect(Collectors.toSet());

        this._canGoWithoutPrepositions = nounObject.get(KEY_JSON_CGWP).getAsBoolean();
    }

    public boolean isRelative() {
        return this._isRelative;
    }

    public Relative getRelativeMeaning() {
        assert this._isRelative;
        return this._relativeMeaning;
    }

    public DayOfWeek getAbsoluteMeaning() {
        assert !this._isRelative;
        return this._absoluteMeaning;
    }

    public Set<String> getKeywords() {
        return this._keywords;
    }

    public Set<TimePreposition.Meaning> getPrepositions() {
        return this._prepositions;
    }

    public boolean canGoWithoutPrepositions() {
        return this._canGoWithoutPrepositions;
    }
}
