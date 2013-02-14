package com.salvador.scenarios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 12/02/13
 * Time: 20:10
 * To change this template use File | Settings | File Templates.
 */
public class Scenario implements Serializable {


    private static final long serialVersionUID = -5550557777493406291L;
    private String name;
    private List<ScenarioStep> steps;
    private List<Map<String,String>> testRows;

    public Scenario() {
        steps = new ArrayList<ScenarioStep>();
        testRows = new ArrayList<Map<String, String>>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
