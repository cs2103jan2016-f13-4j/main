package shared;

import java.util.HashMap;

/**
 * Created by maianhvu on 20/03/2016.
 */
public class ParameterList {

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

    public static ParameterList emptyList() {
        return new ParameterList();
    }
}
