package shared;

import java.time.LocalDateTime;

/**
 * Created by maianhvu on 20/03/2016.
 */
public class ParameterValue {
    private Object value_ = null;

    // CAUTION: no checks made for appropriate type of value
    public ParameterValue(Object value) {
        this.value_ = value;
    }

    public Object getValue() {
        return this.value_;
    }

    // factory method to create a ParameterValue object, ensures the name is correct
    public static ParameterValue parseParamValue(String strToParse, ParameterName associatedName) {
        assert strToParse != null;
        assert associatedName != null;

        ParameterValue paramValue = null;

        // we return at each case so there's minimal fuss w.r.t. types
        switch (associatedName) {

            // DATE TYPE PARAMS
            case DATE_FROM:
            case DATE_TO:
                LocalDateTime date = StringParser.asDateTime(strToParse);
                paramValue = new ParameterValue(date);
                break;

            // STRING TYPE PARAMS
            case NAME:
            case QUERY:
                paramValue = new ParameterValue(strToParse);
                break;

            default:
                // if we reach this point, it means associatedName is not well-defined
                // but that would mean that an exception was thrown in ParameterName.parseParamName()
                // that would mean that we'd never have had a proper associatedName to pass into this method
                // that would mean that we'd never reach this point
                // this is a contradiction, so blow up here before the universe is destroyed
                assert false;
        }

        assert paramValue != null;
        return paramValue;

    }
}