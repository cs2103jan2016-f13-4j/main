package utility;

/**
 * Created by maianhvu on 8/3/16.
 */
public class BoxBuildingUtil {

    /**
     * Constant
     */
    private static final String STRING_LEFT_VERTICAL_BORDER = "│ ";
    private static final String STRING_RIGHT_VERTICAL_BORDER = " │";

    private static final String STRING_WHITE_SPACE = " ";
    private static final String STRING_FORMAT_SPACE = "\\s+";
    private static final String STRING_NEW_LINE = "\n";

    private static final char CHAR_BORDER_TOP = '─';

    public static String borderTop(int... widths) {
        return border('┌','┬','┐', widths);
    }

    public static String borderMiddle(int... widths) {
        return border('├','┼','┤', widths);
    }

    public static String borderBottom(int... widths) {
        return border('└','┴','┘', widths);
    }
    /**
     *
     * @param widths
     * @return
     */
    private static String border(char leftChar, char middleChar, char rightChar, int... widths) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < widths.length; i++) {
            char firstChar = leftChar;
            if (i != 0) {
                firstChar = middleChar;
            }

            builder.append(firstChar);
            builder.append(CHAR_BORDER_TOP);

            for (int j = 0; j < widths[i]; j++) {
                builder.append(CHAR_BORDER_TOP);
            }
        }

        builder.append(CHAR_BORDER_TOP);
        builder.append(rightChar);

        return builder.toString();
    }

    public static String wrapString(String line, int lineLength) {
        return String.format("%s%s%s",
                STRING_LEFT_VERTICAL_BORDER,
                StringUtil.rightPad(line, lineLength, STRING_WHITE_SPACE),
                STRING_RIGHT_VERTICAL_BORDER
        );
    }

    /**
     *
     * @param longText
     * @param lineLength
     * @return
     */
    public static String wrapLongText(String longText, int lineLength) {
        String[] words = longText.split(STRING_FORMAT_SPACE);
        int currentLineLength = 0;
        StringBuilder builder = new StringBuilder();
        builder.append(STRING_LEFT_VERTICAL_BORDER);

        for (String word : words) {

            int newLineLength = currentLineLength + word.length();
            if (currentLineLength != 0) {
                newLineLength += 1; // TODO: Extract magic constant
            }

            // Current line overflows to the next
            if (newLineLength > lineLength) {
                // Right pad current line
                while (currentLineLength < lineLength) {
                    builder.append(STRING_WHITE_SPACE);
                    currentLineLength++;
                }
                builder.append(STRING_RIGHT_VERTICAL_BORDER);

                // Go to next line
                newLineLength = 0;
                builder.append(STRING_NEW_LINE);
                builder.append(STRING_LEFT_VERTICAL_BORDER);
            }

            if (currentLineLength != 0 && newLineLength != 0) {
                builder.append(STRING_WHITE_SPACE);
            }

            builder.append(word);
            if (newLineLength == 0) {
                builder.append(STRING_WHITE_SPACE);
                newLineLength += word.length() + 1;
            }

            currentLineLength = newLineLength;
        }

        // Account for last right border
        while (currentLineLength < lineLength) {
            builder.append(STRING_WHITE_SPACE);
            currentLineLength++;
        }
        builder.append(STRING_RIGHT_VERTICAL_BORDER);


        return builder.toString();
    }

}
