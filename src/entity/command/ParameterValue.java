package entity.command;

import java.time.LocalDateTime;
import java.util.regex.*;

import util.*;

/**
 * This is the counterpart to the ParameterName enum.
 * Its job is to represent the parameter value (second entry in the ParameterList internal tuples).
 * 
 * We store both the type and actual value of the parameter type in this class.
 * Ideally, we'd be able to use an algebraic data type for this use case, but this is Java. :(
 * 
 * Internally, the value is pointed to by an Object reference. When an external reference is requested,
 * the get method does the necessary upcasting based on the typeOfValue field. This upcasting is safe
 * as the type is stored along with the value and no guesswork is made as to the actual type.
 * 
 * created by thenaesh on Mar 9, 2016
 *
 */
public class ParameterValue {
    private Class<?> typeOfValue_ = null;
    private Object value_ = null;
    
    // to make this class symmetric to the ParameterName enum, we disallow direct
    // construction and require a factory class method (called parseParamValue)
    private ParameterValue(Class<?> typeOfValue, Object value) {
        this.typeOfValue_ = typeOfValue;
        this.value_ = value;
    }
    
    // method is generic as it may return different types based on the associated parameter name
    // type inference should allow us to omit the type, making the method type-polymorphic (in the Haskell sense)
    public static ParameterValue parseParamValue(String strToParse, ParameterName associatedName) {
        assert strToParse != null;
        assert associatedName != null;
        
        ParameterValue pv = null;
        
        // we return at each case so there's minimal fuss w.r.t. types
        switch (associatedName) {
            case DATE_FROM:
                LocalDateTime from = StringParser.asDateTime(strToParse);
                pv = new ParameterValue(LocalDateTime.class, from);
               break;
            case DATE_TO:
                LocalDateTime to = StringParser.asDateTime(strToParse);
                pv = new ParameterValue(LocalDateTime.class, to);
                break;
            case NAME:
                String name = strToParse;
                pv = new ParameterValue(String.class, name);
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