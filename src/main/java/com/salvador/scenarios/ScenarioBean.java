package com.salvador.scenarios;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectonemenu.SelectOneMenu;

import javax.el.MethodExpression;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UISelectItem;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 12/02/13
 * Time: 20:10
 */
@Named
@SessionScoped
public class ScenarioBean implements Serializable {

    @Inject
    ScenarioContentBean scenarioContentBean;

    private String name;
    private Scenario scenario;

    public String createScenario() {
        return "";
    }

    public String addScenarioStep() {

        if(scenario == null) {
            scenario = new Scenario();
        }
        scenario.setName(name);

        ScenarioStep step = new ScenarioStep();
        step.setType(scenarioContentBean.getStepType());
        step.setText(scenarioContentBean.getStepText());
        scenario.getSteps().add(step);

        scenarioContentBean.setCreated(true);
        return "";
    }


    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
