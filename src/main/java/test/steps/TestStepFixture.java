package test.steps;

import com.salvador.annotations.Step;
import com.salvador.annotations.Steps;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 26/02/13
 * Time: 19:18
 */
@Steps
public class TestStepFixture {

    @Step("step 1 goes here")
    public void testSomething() {

    }

    @Step("@employee does something")
    public void testSomethingElse(String employee) {

    }
}
