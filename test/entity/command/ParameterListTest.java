package entity.command;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by maianhvu on 6/3/16.
 */
public class ParameterListTest {

    private ParameterList parameters_;

    /*
    @Before
    public void setUp() {
        this.parameters_ = new ParameterList();
    }

    @Test
    public void Unpopulated_parameters_are_null() {
        assertThat(this.parameters_.getParameter("randomParam"), is(nullValue()));
    }

    @Test
    public void Parameters_can_have_null_value() {
        this.parameters_.addParameter("param", null);
        assertThat(this.parameters_.hasParameterNamed("param"), is(true));
    }

    @Test
    public void Integer_parameters_are_correctly_parsed() {
        this.parameters_.addParameter("age", "21");
        assertThat(this.parameters_.getIntParameter("age"), is(21));
    }

    @Test
    public void Parameters_inconvertible_to_integer_are_returned_as_null() {
        this.parameters_.addParameter("value", "totally not integer");
        assertThat(this.parameters_.getIntParameter("value"), is(nullValue()));
    }

    @Test
    public void Double_parameters_are_correctly_parsed() {
        this.parameters_.addParameter("price", "1.10");
        assertThat(this.parameters_.getDoubleParameter("price"), is(equalTo(1.1)));
    }

    @Test
    public void Parameters_inconvertible_to_double_are_returned_as_null() {
        this.parameters_.addParameter("value", "totally not double");
        assertThat(this.parameters_.getDoubleParameter("value"), is(nullValue()));
    }
    */



}
