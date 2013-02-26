package com.salvador.scenarios;

import com.salvador.configuration.Configuration;
import com.salvador.pages.Page;
import com.salvador.pages.PageContent;
import com.salvador.pages.PageItem;
import com.salvador.pages.PageManager;
import com.salvador.utils.FacesUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
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

    @SuppressWarnings("CdiUnproxyableBeanTypesInspection")
    @Inject
    transient ScenarioManager scenarioManager;

    private String name;
    private String notes;
    private Scenario scenario;
    private List<String> parameters;
    private List<ScenarioStep> commonSteps;
    private boolean updating;

    @Inject
    PageContent pageContent;

    @Inject
    Configuration configuration;

    @PostConstruct
    public void init() {
        conversation.begin();
        scenario = new Scenario();
        parameters = new ArrayList<String>();
    }

    public String handleEditScenario(final String itemId) throws IOException {
        final PageItem item = pageContent.getCurrentPage().getItem(itemId);
        if (item != null) {
            scenario = (Scenario) item;
            name = scenario.getName();
            notes = scenario.getNotes();
            updating = true;

            for(ScenarioStep step : scenario.getSteps()) {
                findParameters(step.getText());
            }
        }

        return "/pages/add-scenario";
    }

    public List<ScenarioStep> getCommonSteps() throws IOException {
        if (commonSteps == null) {
            commonSteps = new ArrayList<ScenarioStep>();
            final Page rootPage = pageManager.getPage(configuration.getHome(), "");
            addCommonSteps(rootPage);

        }

        return commonSteps;
    }

    private void addCommonSteps(Page page) {
        for (Scenario tempScenario : page.getItems(Scenario.class)) {
            for (ScenarioStep step : tempScenario.getSteps()) {
                if (step.isCommon()) {
                    commonSteps.add(step);
                }
            }
        }

        for (Page child : page.getChildren()) {
            addCommonSteps(child);
        }
    }

    public void save() throws IOException {

        if (pageContent.getCurrentPage() != null) {
            if (validates()) {
                if (!updating) {
                    pageContent.getCurrentPage().getItems().add(scenario);
                }

                pageManager.save(configuration.getHome(), pageContent.getCurrentPage());
                conversation.end();

                FacesUtils.redirect(pageContent.getCurrentPage());
            }
        }
    }

    private boolean validates() {
        boolean valid = true;

        if (scenario == null || scenario.getSteps().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation error",
                            "You must have at least one step in a scenario"));
            valid = false;
        }

        return valid;
    }

    public String addScenarioStep() throws IOException {

        ScenarioStep step = new ScenarioStep();
        step.setType(scenarioContentBean.getStepType());
        step.setText(scenarioContentBean.getStepText());
        scenario.getSteps().add(step);

        findParameters(scenarioContentBean.getStepText());

        scenarioContentBean.setCreated(true);
        return "";
    }

    public String addParameterSet() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        Map<String, String> testParams = new LinkedHashMap<String, String>();
        for (String param : parameters) {
            String paramValue = externalContext.getRequestParameterMap().get(param);
            testParams.put(param, paramValue);
        }

        scenario.getTestRows().add(testParams);

        return "";
    }

    private void findParameters(final String text) {

        String[] parts = text.split(" ");

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
