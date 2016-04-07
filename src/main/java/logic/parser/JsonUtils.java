package logic.parser;

import com.google.gson.JsonArray;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by maianhvu on 06/04/2016.
 */
public class JsonUtils {

    public static Set<String> toStringSet(JsonArray array) {
        String[] stringArray = new String[array.size()];
        for (int i = 0; i < array.size(); i++) {
            stringArray[i] = array.get(i).getAsString();
        }
        return Arrays.stream(stringArray).collect(Collectors.toSet());
    }

    public static <E extends Enum> E findEnumValue(String enumName, Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(constant -> constant.name().equals(enumName))
                .findFirst().orElse(null);
    }
}
