package utility;

/**
 * Created by maianhvu on 8/3/16.
 */
public class StringUtil {

    /**
     * Constants
     */

    public static String rightPad(String line, int lineLength, String pad) {
        StringBuilder sb = new StringBuilder();
        sb.append(line);
        while (sb.length() < lineLength) {
            sb.append(pad);
        }

        return sb.toString();
    }
}
