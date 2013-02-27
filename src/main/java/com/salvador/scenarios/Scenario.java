package com.salvador.scenarios;

import com.salvador.common.annotations.AutoTest;
import com.salvador.pages.PageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 12/02/13
 * Time: 20:10
 *
 */
@AutoTest
public class Scenario extends PageItem {


    private static final long serialVersionUID = -5550557777493406291L;
    private String notes;
    private List<ScenarioStep> steps;
    private List<ScenarioTest> tests;

    public Scenario() {
        steps = new ArrayList<ScenarioStep>();
        tests = new ArrayList<ScenarioTest>();
        setCanBeDisabled(true);
    }

    public List<ScenarioStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ScenarioStep> steps) {
        this.steps = steps;
    }

    public List<ScenarioTest> getTests() {
        return tests;
    }

    public void setTests(List<ScenarioTest> tests) {
        this.tests = tests;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ScenarioStep getStep(String id) {
        for(ScenarioStep step : steps) {
            if(step.getId() != null && step.getId().equals(id)) {
                return step;
            }
        }

        return null;
    }

    public ScenarioTest getTest(String id) {
        for(ScenarioTest test : tests) {
            if(test.getId() != null && test.getId().equals(id)) {
                return test;
            }
        }

        return null;
    }
}
