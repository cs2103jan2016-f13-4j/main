package entity.command;

import java.time.LocalDateTime;

import utility.StringParser;

/**
 * This is the counterpart to the ParameterName enum.
 * Its job is to represent the parameter value (second entry in the ParameterList internal tuples).
 * 
 * Every value is stored as an Object reference. Upcasting may be done elsewhere, based on the
 * associated ParameterName.
 * 
 * created by thenaesh on Mar 9, 2016
 *
 */
public class ParameterValue {
    private Object value_ = null;
    
    // to make this class symmetric to the ParameterName enum, we disallow direct
    // construction and require a factory class method (called parseParamValue)
    private ParameterValue(Object value) {
        this.value_ = value;
    }
    
    public Object getValue() {
        return this.value_;
    }
    
    // factory method to create a ParameterValue object
    public static ParameterValue parseParamValue(String strToParse, ParameterName associatedName) {
        assert strToParse != null;
        assert associatedName != null;
        
        ParameterValue pv = null;
        
        // we return at each case so there's minimal fuss w.r.t. types
        switch (associatedName) {
            case DATE_FROM:
                LocalDateTime from = StringParser.asDateTime(strToParse);
                pv = new ParameterValue(from);
               break;
            case DATE_TO:
                LocalDateTime to = StringParser.asDateTime(strToParse);
                pv = new ParameterValue(to);
                break;
            case NAME:
                String name = strToParse;
                pv = new ParameterValue(name);
                break;
            default:
                // if we reach this point, it means associatedName is not well-defined
                // but that would mean that an exception was thrown in ParameterName.parseParamName()
                // that would mean that we'd never have had a proper associatedName to pass into this method
                // that would mean that we'd never reach this point
                // this is a contradiction, so blow up here before the universe is destroyed
                assert false;
        }
        
        assert pv != null;
        return pv;
    }
}