package com.salvador.scanners;

import com.salvador.scenarios.Scenario;
import com.salvador.scenarios.ScenarioStep;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 14/02/13
 * Time: 17:36
 *
 */
public class DefaultStepScannerTest {

    DefaultStepScanner scanner;

    @Before
    public void setUp() throws Exception {
        scanner = new DefaultStepScanner();
    }

    @Test
    public void testGetStepClasses() throws Exception {
        final List<Class> classes = scanner.getStepClasses();
        assertThat(classes.get(0).getName(),is("test.steps.TestStep"));
    }

    @Test
    public void testGetScenarioSteps() throws Exception {
        Scenario scenario = new Scenario();
        ScenarioStep step = new ScenarioStep();
        step.setType("Given");
        step.setText("this is a step with no param");
        scenario.getSteps().add(step);

        scanner.getScenarioSteps(scenario);
    }
}
