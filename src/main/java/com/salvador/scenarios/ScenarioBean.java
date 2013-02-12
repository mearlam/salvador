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
 * To change this template use File | Settings | File Templates.
 */
@Named
@SessionScoped
public class ScenarioBean implements Serializable {

    @Inject
    ScenarioContentBean scenarioContentBean;

    private HtmlPanelGrid grid;

    private List<Scenario> scenarios = new ArrayList<Scenario>();
    private int rowCount;

    public String createScenario() {
        return "";
    }

    public String addNewRow() {

        SelectOneMenu menu = new SelectOneMenu();
        menu.setId("menu-" + rowCount);
        UISelectItem item1 = new UISelectItem();
        item1.setId("menu-item-1-" + rowCount);
        item1.setItemLabel("Given");
        menu.getChildren().add(item1);

        grid.getChildren().add(menu);

//        InputText inputText = new InputText();
//        inputText.setId("step-text-" + rowCount);
//        inputText.setSize(100);
//        grid.getChildren().add(inputText);
//
//        CommandButton commandButton = new CommandButton();
//        commandButton.setId("add-button-" + rowCount);
//        commandButton.setIcon("ui-icon-plus");
//        grid.getChildren().add(commandButton);

        rowCount++;
        return "";
    }

    public HtmlPanelGrid getGrid() {
        return grid;
    }

    public void setGrid(HtmlPanelGrid grid) {
        this.grid = grid;
    }

    public String addScenarioStep() {

        boolean found = false;
        Scenario scenario = new Scenario();

        for(Scenario checkScenario : scenarios) {
            if(scenarioContentBean.getName().equals(checkScenario.getName())) {
                scenario = checkScenario;
                found = true;
                break;
            }
        }

        if(!found) {
            scenarios.add(scenario);
        }

        ScenarioStep step = new ScenarioStep();
        step.setType(scenarioContentBean.getStepType());
        step.setText(scenarioContentBean.getStepText());

        scenario.setName(scenarioContentBean.getName());
        scenario.getSteps().add(step);

        return "";
    }


    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }
}
