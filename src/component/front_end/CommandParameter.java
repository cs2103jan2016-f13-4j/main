/**
 * 
 */
package component.front_end;


/**
 * CommandParameters is a bunch of static command constants to identify
 * the actual type of the parameter (stored as a string in the parameter list).
 * 
 * TODO:        Make this enum redundant by not using parameter strings in the value.
 *              This may entail modifying Command, as well as the Command Parser.
 * 
 * created by thenaesh on Mar 8, 2016
 *
 */
public enum CommandParameter {
    DATE_FROM   ("from"),
    DATE_TO     ("to"),
    NAME        ("name");
    
    final String paramStr_;
    
    CommandParameter(String paramStr) {
        this.paramStr_ = paramStr;
    }
    
    public static CommandParameter convertStringToCommandParameter(String str) {
        CommandParameter paramType = null;
        
        switch (str) {
            case "from":
                paramType = CommandParameter.DATE_FROM;
                break;
            case "to":
                paramType = CommandParameter.DATE_TO;
                break;
            case "name":
                paramType = CommandParameter.NAME;
                break;
            default:
                assert false; // shouldn't reach this point
                
        }
        
        return paramType;
    }
}
