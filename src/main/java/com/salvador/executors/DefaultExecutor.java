package com.salvador.executors;

import com.salvador.annotations.Step;
import com.salvador.annotations.Steps;
import com.salvador.pages.Page;
import com.salvador.pages.PageManager;
import com.salvador.scanners.DefaultStepScanner;
import com.salvador.scanners.StepScanner;
import com.salvador.scenarios.Scenario;
import com.salvador.scenarios.ScenarioStep;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javassist.bytecode.stackmap.TypeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/02/13
 * Time: 15:36
 * <p/>
 * Runs inside its own JVM
 */
public class DefaultExecutor implements TestExecutor {

    static final Logger log = LoggerFactory.getLogger(DefaultExecutor.class);

    ExecutionLogger executionLogger;
    PageManager pageManager = new PageManager();

    private StepScanner stepScanner;

    @Override
    public void execute(String home, String testId, String pagePath) {
        log.info("Executing test {}", testId);
        executionLogger = new DefaultExecutionLogger(home);

        try {
            final Page page = pageManager.getPage(home, pagePath);
            log.info("Test page: '{}'", page.getFullPath());
            log.info("Child pages: '{}'", page.getChildren().size());

            stepScanner = new DefaultStepScanner();

            runPage(testId, page);

            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(5000);
                    log.debug("Sleeping to test");
                } catch (InterruptedException e) {
                    log.error("Could not sleep");
                }
            }

        } catch (IOException e) {
            log.error("Could not run tests", e);
        } finally {
            executionLogger.complete();
        }
    }

    private void runPage(final String testId, Page page) {
        log.debug("running page: {}", page.getName());
        for (Scenario scenario : page.getItems(Scenario.class)) {
            log.debug(">{}", scenario.getName());
            executionLogger.startScenario(scenario);
            runScenario(testId, scenario);
        }
        for (Page child : page.getChildren()) {
            runPage(testId, child);
        }
    }

    private void runScenario(String testId, Scenario scenario) {
        stepScanner.getScenarioSteps(scenario);

        if (scenario.getTestRows().isEmpty()) {
            runScenarioWithNoData(testId, scenario);
        } else {
            runScenarioWithTestData(testId, scenario);
        }


        executionLogger.finishedScenario(scenario);
    }

    private void runScenarioWithTestData(String testId, Scenario scenario) {
        // run the scenario once for each data row
        for (Map<String, String> data : scenario.getTestRows()) {
            for (ScenarioStep step : scenario.getSteps()) {
                log.debug("\t>step '{} {}'", step.getType().name(), step.getText());
                if (step.getRunInformation() != null) {
                    String stepText = step.getType().name() + " " + step.getText();
                    for (String key : data.keySet()) {
                        stepText = stepText.replace(key, data.get(key));
                    }
                    log.debug("\t\t>run '{}'", stepText);

                } else {
                    log.error("Could not find step run information!");
                }
                sendStatusUpdate(testId, scenario.getId(), step.getId());
                executionLogger.logStep(step);
            }
        }
    }

    private void runScenarioWithNoData(String testId, Scenario scenario) {
        for (ScenarioStep step : scenario.getSteps()) {
            log.debug("\t>step '{} {}'", step.getType().name(), step.getText());
            if (step.getRunInformation() != null) {
                log.debug("\t\t>run '{} {}'", step.getType().name(), step.getText());

            } else {
                log.error("Could not find step run information!");
            }
            sendStatusUpdate(testId, scenario.getId(), step.getId());
            executionLogger.logStep(step);
        }
    }


    private void sendStatusUpdate(String testId, String scenarioId, String stepId) {
        Client client = Client.create();

        // todo work out running application URL, probably need to pass it in
        WebResource webResource = client
                .resource("http://localhost:8080/rest/runner/passed/" + testId + "/" + scenarioId + "/" +
                        stepId);

        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);
    }

    public static void main(String[] args) {

        if (args.length == 2) {
            DefaultExecutor executor = new DefaultExecutor();
            executor.execute(args[0], args[1], null);
        } else if (args.length == 3) {
            DefaultExecutor executor = new DefaultExecutor();
            executor.execute(args[0], args[1], args[2]);
        } else {
            log.error("You must supply home");
        }
    }

}