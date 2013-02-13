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
 */
@Named
@RequestScoped
public class ScenarioContentBean implements Serializable {


    private String stepType;
    private String stepText;

    private boolean created;

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

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }
}
