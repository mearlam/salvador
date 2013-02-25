package com.salvador.scenarios;

import com.salvador.pages.Page;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/02/13
 * Time: 09:45
 */
public class ScenarioManager {

    public ScenarioStep getScenarioStep(Page page, String id) {

        for(Scenario scenario : page.getItems(Scenario.class)) {
            for(ScenarioStep step : scenario.getSteps()) {
                if(step.getId().equals(id)) {
                    return step;
                }
            }
        }

        return null;
    }

}
