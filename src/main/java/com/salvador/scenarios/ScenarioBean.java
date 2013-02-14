package com.salvador.scenarios;

import com.salvador.pages.Page;
import com.salvador.pages.PageManager;
import com.salvador.utils.FacesUtils;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectonemenu.SelectOneMenu;

import javax.annotation.PostConstruct;
import javax.el.MethodExpression;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UISelectItem;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 12/02/13
 * Time: 20:10
 */
@Named
@ConversationScoped
public class ScenarioBean implements Serializable {

    @Inject
    ScenarioContentBean scenarioContentBean;

    @Inject
    Conversation conversation;

    @SuppressWarnings("CdiUnproxyableBeanTypesInspection")
    @Inject
    transient PageManager pageManager;

    private String name;
    private String pagePath;
    private Scenario scenario;
    private List<String> parameters;

    @PostConstruct
    public void init() {
        conversation.begin();
    }

    public void createScenario() throws IOException {

        Page page = pageManager.getPage(pagePath);

        if (page != null) {
            page.getScenarios().add(scenario);
            pageManager.save(page);
            conversation.end();

            FacesUtils.redirect("/" + PageManager.TEST_FOLDER + "/" + page.getPath() + page.getName());
        }
    }

    public String addScenarioStep() {

        if (scenario == null) {
            pagePath = FacesUtils.getParam("page");
            scenario = new Scenario();
            parameters = new ArrayList<String>();
        }
        scenario.setName(name);

        ScenarioStep step = new ScenarioStep();
        step.setType(scenarioContentBean.getStepType());
        step.setText(scenarioContentBean.getStepText());
        scenario.getSteps().add(step);

        findParameters();

        scenarioContentBean.setCreated(true);
        return "";
    }

    public String addParameterSet() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        Map<String, String> testParams = new LinkedHashMap<String, String>();
        for (String param : parameters) {
            String paramValue = (String) externalContext.getRequestParameterMap().get(param);
            testParams.put(param, paramValue);
        }

        scenario.getTestRows().add(testParams);

        return "";
    }

    private void findParameters() {

        String[] parts = scenarioContentBean.getStepText().split(" ");

        for (String part : parts) {
            if (part.startsWith("@")) {
                parameters.add(part);
            }
        }
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
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
