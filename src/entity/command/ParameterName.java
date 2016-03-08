package entity.command;

/**
 * This enumerates the name of parameter (e.g. name, start date, end date, ...).
 * Its job is to represent the parameter name (first entry in the ParameterList internal tuples).
 * 
 * We don't want to use a String (unlike for the parameter value), since there
 * are only a few clearly defined parameter names after command parsing is compete.
 * 
 * We handle all the invalid parameter names in the Command Parser, using the
 * parseParamType class method in this enum. This way, the Decision Engine can simply
 * assume the entire Command object is perfectly valid and stripped of all user stupidity.
 * 
 * 
 * created by thenaesh on Mar 8, 2016
 *
 */
public enum ParameterName {
    DATE_FROM       ("from"),
    DATE_TO         ("to"),
    NAME            ("name");
    
    final String paramStr_;
    
    private ParameterName(String paramStr) {
        this.paramStr_ = paramStr;
    }
    
    public String toString() {
        return this.paramStr_;
    }
    
    /**
     * @param str string to parse
     * @return parameter name, as one of the enum values
     * @throws InvalidParameterException if the string is rubbish
     */
    public static ParameterName parseParamName(String str) throws InvalidParameterException {
        assert str != null;
        
        ParameterName paramType = null;
        
        switch (str) {
            case "name":
                paramType = NAME;
                break;
            case "from":
                paramType = DATE_FROM;
                break;
            case "to":
                paramType = DATE_TO;
                break;
            default:
                throw new InvalidParameterException();
        }
        
        return paramType;
    }
}