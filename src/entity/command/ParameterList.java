package entity.command;

import java.time.LocalDateTime;
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

    private final HashMap<ParameterName, String> parameterMap_;

    public ParameterList() {
        this.parameterMap_ = new HashMap<>();
    }

    public void addParameter(ParameterName parameterName, String parameterValue) {
        this.parameterMap_.put(parameterName, parameterValue);
    }

    public boolean hasParameterNamed(ParameterName parameterName) {
        return this.parameterMap_.containsKey(parameterName);
    }

    public boolean hasValueForParameter(ParameterName parameterName) {
        return this.parameterMap_.get(parameterName) != null;
    }

    public String getParameter(ParameterName parameterName) {
        return this.parameterMap_.get(parameterName);
    }
    
    
    /**
     * Returns the parameter with the name specified, converted into an
     * integer value. Returns null if the value cannot be converted into integer.
     * @param parameterName a string
     * @return the integer value of the parameter associated with the name
     */
    public Integer getIntParameter(ParameterName parameterName) {
        try {
            return Integer.parseInt(this.getParameter(parameterName).toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Returns the parameter with the name specified, converted into an
     * double value. Returns null if the value cannot be converted into double.
     * @param parameterName a string
     * @return the double value of the parameter associated with the name
     */
    public Double getDoubleParameter(ParameterName parameterName) {
        try {
            return Double.parseDouble(this.getParameter(parameterName).toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public LocalDateTime getDateParameter(ParameterName parameterName) {
        return null; // TODO: un-implemented method stub
    }

    public boolean isEmpty() {
        return this.parameterMap_.isEmpty();
    }

}
