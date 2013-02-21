package com.salvador.scenarios;

import com.salvador.common.annotations.AutoTest;
import com.salvador.pages.PageItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private List<Map<String,String>> testRows;

    public Scenario() {
        steps = new ArrayList<ScenarioStep>();
        testRows = new ArrayList<Map<String, String>>();
    }

    public List<ScenarioStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ScenarioStep> steps) {
        this.steps = steps;
    }

    public List<Map<String, String>> getTestRows() {
        return testRows;
    }

    public void setTestRows(List<Map<String, String>> testRows) {
        this.testRows = testRows;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
