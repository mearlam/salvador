package com.salvador.runners.progress;

import com.salvador.scenarios.ScenarioTest;

/**
 * Created by IntelliJ IDEA.
 * User: mearlam
 * Date: 27/02/13
 * Time: 08:59
 */
public class ScenarioTestProgress extends Progress {

    private ScenarioTest test;
    private String text;

    public ScenarioTest getTest() {
        return test;
    }

    public void setTest(ScenarioTest test) {
        this.test = test;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
