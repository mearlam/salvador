package com.salvador.scenarios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: mearlam
 * Date: 27/02/13
 * Time: 11:06
 */
public class ScenarioTextFormatter {

    static final Logger log = LoggerFactory.getLogger(ScenarioTextFormatter.class);

    public String getFormattedText(ScenarioStep step, ScenarioTest test) {
        String stepText = step.getType().name() + " " + step.getText();
        for (String key : test.getData().keySet()) {
            stepText = stepText.replace(key, test.getData().get(key));
        }
        log.debug("step text '{}'", stepText);

        return stepText;
    }
}
