package test.steps;

import com.salvador.annotations.Step;
import com.salvador.annotations.Steps;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 14/02/13
 * Time: 17:45
 *
 */
@Steps
public class TestStep {

    @Step("this is a step with no param")
    public void runTest() {

    }

    @Step
    public void noParams() {

    }
}
