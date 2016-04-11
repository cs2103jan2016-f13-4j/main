package logic.parser;

import com.google.gson.JsonObject;
import shared.Command;

import java.util.Set;

/**
 * @@author A0127046L
 */
public class Instruction {

    private static final String KEY_JSON_NAME = "name";
    private static final String KEY_JSON_KEYWORDS = "keywords";

    private Command.Instruction _name;
    private Set<String> _keywords;

    public Instruction(JsonObject instructionObject) {
        assert instructionObject.has(KEY_JSON_NAME);
        assert instructionObject.has(KEY_JSON_KEYWORDS);

        // Parse name
        this._name = JsonUtils.findEnumValue(
                instructionObject.get(KEY_JSON_NAME).getAsString(),
                Command.Instruction.class
        );

        // Parse keywords
        this._keywords = JsonUtils.toStringSet(
                instructionObject.get(KEY_JSON_KEYWORDS).getAsJsonArray()
        );
    }

    public Command.Instruction getName() {
        return this._name;
    }

    public Set<String> getKeywords() {
        return this._keywords;
    }
}
