package com.salvador.scanners;

import com.salvador.scenarios.Scenario;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 14/02/13
 * Time: 17:33
 *
 */
public interface StepScanner {

    List<Class> getStepClasses();
    void getScenarioSteps(Scenario scenario);
}
