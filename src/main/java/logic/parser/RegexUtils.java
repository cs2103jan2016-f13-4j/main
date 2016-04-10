package logic.parser;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @@author Mai Anh Vu
 */
public class RegexUtils {

    private static final String DELIMITER_CHOICES = "|";
    public static final String MATCHER_GROUP_DATE_YEAR = "YEAR";
    public static final String MATCHER_GROUP_DATE_DAY = "DAY";
    public static final String MATCHER_GROUP_DATE_MONTH = "MONTH";

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
        // We want the longer strings to be matched first
        Arrays.sort(choices, (string1, string2) -> string2.length() - string1.length());

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

        // We want the longer strings to be matched first
        Arrays.sort(choices, (string1, string2) -> string2.length() - string1.length());

        return String.format("(?<%s>%s)", name,
                String.join(DELIMITER_CHOICES, choices));
    }

    /**
     * Constructs a RegExp that matches a string only when it is the start
     * of the starting string (we allow spaces in front).
     * @param string the string to be considered as starting
     * @return a RegExp string that only matches if <code>string</code>
     * is at the start of the tested string
     */
    public static String startOfString(String string) {
        return "^\\s*" + string;
    }

    /**
     * Constructs a RegExp that matches even when the provided string does
     * not exist inside the candidate string.
     * @param string an optional string
     * @return a RegExp that matches regardless of whether the provided
     * string exists or not
     */
    public static String optional(String string) {
        return String.format("(?:%s)?", string);
    }

    /**
     * Constructs a RegExp that matches similarly to optional, but with
     * compulsory trailing spaces if the word actually exists.
     * @param word an optional word
     * @return a RegExp that matches regardless of whether the provided
     * word exists or not
     */
    public static String optionalWord(String word) {
        return optional(word(word));
    }

    /**
     * Constructs a RegExp that matches the given word string followed by
     * compulsory trailing spaces.
     * @param word
     * @return
     */
    public static String word(String word) {
        return word + "\\s+";
    }

    /**
     * Constructs a RegExp from an existing RegExp that makes the original
     * one not matchable if it's between quotes.
     * @param currentRegex the original regular expression
     * @return the modified RegExp string
     */
    public static String noSurroundingQuotes(String currentRegex) {
        return currentRegex.concat("(?=(?:(?:(?:[^\"\\\\]++|\\\\.)*+\"){2})*+(?:[^\"\\\\]++|\\\\.)*+$)");
    }

    /**
     * Wraps an existing RegExp in a named capturing group.
     * @param name a string denoting the name of the group
     * @param group an existing regular expression
     * @return a named capturing group
     */
    public static String namedGroup(String name, String group) {
        return String.format("(?<%s>%s)", name, group);
    }

    public static String dateRegex() {
        return String.format("(?:(?<%s>\\d{4})\\s*|(?<%s>\\b\\d{1,2})(?:st|nd|rd|th)?\\b" +
                "\\s*|(?<%s>january|february|march|april|may|june|july|august|september|" +
                "october|november|december|jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)\\s*){2,3}",
                MATCHER_GROUP_DATE_YEAR,
                MATCHER_GROUP_DATE_DAY,
                MATCHER_GROUP_DATE_MONTH);
    }

    public static String timeRegex() {
        return "(?:\\d|:(?=\\d(?<=\\d))){1,5}(?:\\s*(?:am|pm))?";
    }
}
