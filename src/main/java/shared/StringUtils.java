package shared;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @@author Mai Anh Vu
 */
public class StringUtils {
    public static final char CHAR_QUOTES = '\"';

    // we don't want this class to be instantiated
    private StringUtils() {
    }

    public static boolean isSurroundedByQuotes(String string) {
        return string != null && string.length() >= 2 &&
                string.charAt(0) == CHAR_QUOTES && string.charAt(string.length() - 1) == CHAR_QUOTES;

    }

    public static String stripEndCharacters(String string) {
        if (string == null || string.length() < 2) {
            return "";
        }
        return string.substring(1, string.length() - 1);
    }
}
