package com.salvador.scenarios;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 12/02/13
 * Time: 20:11
 *
 */
public class ScenarioStep implements Serializable {

    private ScenarioStepType type;
    private String text;
    private ScenarioStepRunInformation runInformation;

    public ScenarioStepType getType() {
        return type;
    }

    public void setType(String type) {
        this.type = Enum.valueOf(ScenarioStepType.class,type);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ScenarioStepRunInformation getRunInformation() {
        return runInformation;
    }

    public void setRunInformation(ScenarioStepRunInformation runInformation) {
        this.runInformation = runInformation;
    }
}
