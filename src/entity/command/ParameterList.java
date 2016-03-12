package entity.command;

import java.util.HashMap;

/**
 * When a user enters parameters, they occur in a (name, value) pair.
 * 
 * For instance, the issued command `add name:"chiong V0.1 start:1000 end:1400`
 * should produce the parameter list { (name, "chiong V0.1"), (start, 1000), (end, 1400) }.
 * 
 * The ParameterList class encapsulates this list, storing it internally as a map
 * of (ParameterName, ParameterValue) tuples. The respective documentation for
 * ParameterName and ParameterValue can be found at {@link ParameterName} and {@link ParameterValue}.
 * 
 * Created by maianhvu on 6/3/16.
 */
public class ParameterList {
    
    public static final String STRING_PARAMETER_DELIMITER = "\\s+";
    public static final String STRING_COMPONENT_DELIMITER = ":";

    private final HashMap<ParameterName, ParameterValue> parameterMap_;

    public ParameterList() {
        this.parameterMap_ = new HashMap<>();
    }

    public void addParameter(ParameterName parameterName, ParameterValue parameterValue) {
        this.parameterMap_.put(parameterName, parameterValue);
    }

    public boolean hasParameterNamed(ParameterName parameterName) {
        return this.parameterMap_.containsKey(parameterName);
    }

    public boolean hasValueForParameter(ParameterName parameterName) {
        return this.parameterMap_.get(parameterName) != null;
    }

    public ParameterValue getParameter(ParameterName parameterName) {
        assert this.hasParameterNamed(parameterName);
        return this.parameterMap_.get(parameterName);
    }
    
    
    public boolean isEmpty() {
        return this.parameterMap_.isEmpty();
    }

}
