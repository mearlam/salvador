package com.salvador.scenarios;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.html.HtmlPanelGrid;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 12/02/13
 * Time: 20:14
 * To change this template use File | Settings | File Templates.
 */
@Named
@RequestScoped
public class ScenarioContentBean implements Serializable {

    private String name;
    private String stepType;
    private String stepText;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public String getStepText() {
        return stepText;
    }

    public void setStepText(String stepText) {
        this.stepText = stepText;
    }
}
