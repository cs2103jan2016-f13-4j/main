package logic;

import static org.junit.Assert.*;

import org.junit.*;

import java.io.IOException;

/**
 * Created by thenaesh on 3/22/16.
 */
public class DecisionEngineTest {
    private DecisionEngine decisionEngine;

    @Before
    public void setUp() throws IOException {
        this.decisionEngine = DecisionEngine.getInstance();
    }

}
