package shared;

import logic.parser.RegexUtils;
import org.junit.Test;

import java.util.regex.Matcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by maianhvu on 06/04/2016.
 */
public class RegexUtilsTest {

    @Test
    public void RegexUtils_constructs_correct_case_insensitive_matcher() {
        String pattern = "hello\\s+(?<MATCH>world)";
        Matcher matcher = RegexUtils.caseInsensitiveMatch(pattern, "hello WoRlD");
        assertTrue(matcher.find());
    }

    @Test
    public void RegexUtils_constructs_correct_choice_regex() {
        Matcher matcher = RegexUtils.caseInsensitiveMatch(
                RegexUtils.choice("one", "two", "three"),
                "I want two eggs"
        );
        assertTrue(matcher.find());
    }

    @Test
    public void RegexUtils_constructs_correct_named_choice_regex() {
        Matcher matcher = RegexUtils.caseInsensitiveMatch(
                RegexUtils.namedChoice("NUMBER", "one", "two", "three"),
                "Three bears"
        );
        assertTrue(matcher.find());
        assertThat(matcher.group("NUMBER").toLowerCase(), is(equalTo("three")));
    }
}
