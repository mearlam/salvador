package com.salvador.scenarios;

import com.salvador.pages.Page;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/02/13
 * Time: 09:45
 */
public class ScenarioManager {

    public Scenario getScenario(Page page, String name) {
        for(Scenario scenario : page.getScenarios()) {
            if(scenario.getName().equals(name)) {
                return scenario;
            }
        }

        return null;
    }

    public void moveScenarioUp(Page page, String name) {
        int index = 0;
        Scenario scenario = null;

        for(Scenario tempScenario : page.getScenarios()) {
            if(tempScenario.getName().equals(name)) {
                scenario = tempScenario;
                break;
            }
            index++;
        }

        // remove the scenario from the current position
        if(scenario != null && index > 0) {
            page.getScenarios().remove(index);
            page.getScenarios().add(index-1,scenario);
        }
    }

    public void moveScenarioDown(Page page, String name) {
        int index = 0;
        Scenario scenario = null;

        for(Scenario tempScenario : page.getScenarios()) {
            if(tempScenario.getName().equals(name)) {
                scenario = tempScenario;
                break;
            }
            index++;
        }

        // remove the scenario from the current position
        if(scenario != null && index < page.getScenarios().size() -1) {
            page.getScenarios().remove(index);
            page.getScenarios().add(index+1,scenario);
        }
    }
}
