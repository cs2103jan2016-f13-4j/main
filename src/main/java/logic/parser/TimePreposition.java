package logic.parser;

import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.Set;

/**
 * @@author A0127046L
 */
public class TimePreposition {
    private static final String KEY_JSON_MEANING = "meaning";
    private static final String KEY_JSON_KEYWORDS = "keywords";
    private static final String KEY_JSON_CHAINABLE = "chainable";

    public enum Meaning {
        CURRENT, NEXT, STARTING, ENDING
    }

    private Meaning _meaning;
    private Set<String> _keywords;
    private boolean _isChainable;

    public TimePreposition(JsonObject prepObject) {
        assert prepObject.has(KEY_JSON_MEANING);
        assert prepObject.has(KEY_JSON_KEYWORDS);
        assert prepObject.has(KEY_JSON_CHAINABLE);

        // Parse meaning
        this._meaning = JsonUtils.findEnumValue(
                prepObject.get(KEY_JSON_MEANING).getAsString(),
                Meaning.class
        );
        assert this._meaning != null;

        // Parse keywords
        this._keywords = JsonUtils.toStringSet(prepObject.get(KEY_JSON_KEYWORDS).getAsJsonArray());

        // Parse chainability
        this._isChainable = prepObject.get(KEY_JSON_CHAINABLE).getAsBoolean();
    }

    // Getters
    public Meaning getMeaning() {
        return this._meaning;
    }

    public Set<String> getKeywords() {
        return this._keywords;
    }

    public boolean isChainable() {
        return this._isChainable;
    }
}
