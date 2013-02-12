package com.salvador.scenarios;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 12/02/13
 * Time: 20:11
 * To change this template use File | Settings | File Templates.
 */
public class ScenarioStep {

    private ScenarioStepType type;
    private String text;

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
}
