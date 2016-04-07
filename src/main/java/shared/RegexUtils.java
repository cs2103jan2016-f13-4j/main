package shared;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @@author Mai Anh Vu
 */
public class RegexUtils {

    private static final String DELIMITER_CHOICES = "|";

    /**
     * Constructs a matcher that matches the test string case-insensitively
     * with the RegExp pattern given.
     * @param pattern a regex string
     * @param testString a string to be tested for matches
     * @return a matcher that matches the string with the regex
     */
    public static Matcher caseInsensitiveMatch(String pattern, String testString) {
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        return p.matcher(testString);
    }

    /**
     * Constructs a RegExp that matches any string in the choices given.
     * @param choices a vararg array of different strings that can be matched
     * @return a RegExp string that matches any choice given
     */
    public static String choice(String... choices) {
        return String.format("(?:%s)", String.join(DELIMITER_CHOICES, choices));
    }

    /**
     * Constructs a RegExp that matches any choice given, which will be captured
     * under the named group denoted by the <code>name</code> string.
     * @param name the name of the capturing group
     * @param choices a vararg array of different strings that can be matched
     * @return a RegExp string that captures any choice given under a named group
     */
    public static String namedChoice(String name, String... choices) {
        assert name != null && !name.trim().isEmpty();
        return String.format("(?<%s>%s)", name,
                String.join(DELIMITER_CHOICES, choices));
    }


}
