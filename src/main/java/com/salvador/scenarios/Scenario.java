package com.salvador.scenarios;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 12/02/13
 * Time: 20:10
 * To change this template use File | Settings | File Templates.
 */
public class Scenario {

    private String name;
    private List<ScenarioStep> steps;

    public Scenario() {
        steps = new ArrayList<ScenarioStep>();
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
}
