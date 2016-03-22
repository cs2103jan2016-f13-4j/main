package shared;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;

public class Parameter {

    /**
     * Properties
     */
    private final Name _name;
    private final String _value;

    /**
     * TODO: Write JavaDoc
     *
     * @param name
     * @param value
     */
    private Parameter(Name name, String value) {
        this._name = name;
        this._value = value;
    }

    public Object getValue() {

        try {
            switch (this._name.valueType) {
                case STRING:
                    return this._value;
                case INTEGER:
                    return Integer.parseInt(this._value);
                case DOUBLE:
                    return Double.parseDouble(this._value);
                case DATE:
                    return LocalDateTime.parse(this._value);
                default:
                    return null;
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @author maianhvu
     */
    private static enum ValueType {
        STRING, INTEGER, DATE, DOUBLE;
    }

    /**
     * TODO: Write JavaDoc
     *
     * @author maianhvu
     */
    public static enum Name {

        // Add, Edit
        TASK_NAME(ValueType.STRING), DATE_FROM(ValueType.DATE), DATE_TO(ValueType.DATE),

        // Search
        SEARCH_QUERY(ValueType.STRING);

        final ValueType valueType;

        Name(ValueType type) {
            valueType = type;
        }
    }

    /**
     * TODO: Write JavaDoc
     *
     * @author maianhvu
     */
    public static class List {
        private final LinkedHashMap<Name, String> _paramList;

        public List() {
            this._paramList = new LinkedHashMap<>();
        }

        public void add(Name name, String value) {
            this._paramList.put(name, value);
        }

        public boolean hasParameterNamed(Name name) {
            return this._paramList.containsKey(name);
        }

        public Parameter get(Name name) {
            return new Parameter(name, this._paramList.get(name));
        }
    }

}
