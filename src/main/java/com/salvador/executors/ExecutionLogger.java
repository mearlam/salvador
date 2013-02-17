package com.salvador.executors;

import com.salvador.scenarios.Scenario;
import com.salvador.scenarios.ScenarioStep;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 17/02/13
 * Time: 19:31
 */
public interface ExecutionLogger {

    void startScenario(Scenario scenario);
    void logStep(ScenarioStep step);
    void finishedScenario(Scenario scenario);
    void complete();
}
