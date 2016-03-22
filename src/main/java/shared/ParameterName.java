package shared;

import exception.InvalidParameterException;

/**
 * Created by maianhvu on 20/03/2016.
 */
public enum ParameterName {
    DATE_FROM       ("from"),
    DATE_TO         ("to"),
    NAME            ("name"),
    QUERY           ("query");

    final String paramString;

    ParameterName(String paramStr) {
        this.paramString = paramStr;
    }

    public String toString() {
        return this.paramString;
    }

    /**
     * @param str string to parse
     * @return parameter name, as one of the enum values
     * @throws InvalidParameterException if the string is rubbish
     */
    public static ParameterName parseParamName(String str) throws InvalidParameterException {
        assert str != null;

        for (ParameterName paramName : ParameterName.values()) {
            if (str.trim().toLowerCase().equals(paramName.paramString)) {
                return paramName;
            }
        }

        return null;
    }
}

