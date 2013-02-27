package com.salvador.runners.progress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mearlam
 * Date: 27/02/13
 * Time: 08:59
 */
public class ScenarioProgress extends Progress {

    private int stepsComplete;
    private List<ScenarioTestProgress> tests;

    public ScenarioProgress() {
        tests = new ArrayList<ScenarioTestProgress>();
    }

    public int getStepsComplete() {
        return stepsComplete;
    }

    public void setStepsComplete(int stepsComplete) {
        this.stepsComplete = stepsComplete;
    }

    public List<ScenarioTestProgress> getTests() {
        return tests;
    }

    public void setTests(List<ScenarioTestProgress> tests) {
        this.tests = tests;
    }

    public void incrementStepsComplete() {
        stepsComplete++;
    }
}
