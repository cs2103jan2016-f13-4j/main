package logic.parser;

import com.google.gson.JsonObject;
import shared.Task;

import java.util.Set;

/**
 * @@author A0127046L
 */
public class Priority {
    private static final String KEY_JSON_MEANING = "meaning";
    private static final String KEY_JSON_KEYWORDS = "keywords";

    private Task.Priority _meaning;
    private Set<String> _keywords;

    public Priority(JsonObject priorityObject) {
        assert priorityObject.has(KEY_JSON_MEANING);
        assert priorityObject.has(KEY_JSON_KEYWORDS);

        // Parse meaning
        this._meaning = JsonUtils.findEnumValue(
                priorityObject.get(KEY_JSON_MEANING).getAsString(),
                Task.Priority.class
        );

        // Parse keywords
        this._keywords = JsonUtils.toStringSet(
                priorityObject.get(KEY_JSON_KEYWORDS).getAsJsonArray()
        );
    }

    // Getters
    public Task.Priority getMeaning() {
        return this._meaning;
    }

    public Set<String> getKeywords() {
        return this._keywords;
    }
}
