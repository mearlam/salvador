package com.salvador.executors;

import com.salvador.pages.Page;
import com.salvador.pages.PageManager;
import com.salvador.scanners.DefaultStepScanner;
import com.salvador.scanners.StepScanner;
import com.salvador.scenarios.Scenario;
import com.salvador.scenarios.ScenarioStep;
import com.salvador.scenarios.ScenarioTest;
import com.salvador.services.ServiceUpdate;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
    Client client;

    @Override
    public void execute(String home, String testId, String pagePath) {
        log.info("Executing test {}", testId);
        executionLogger = new DefaultExecutionLogger(home);

        try {
            final Page page = pageManager.getPage(home, pagePath);
            log.info("Test page: '{}'", page.getFullPath());
            log.info("Child pages: '{}'", page.getChildren().size());

            stepScanner = new DefaultStepScanner();

            client = Client.create();
            runPage(testId, page);

        } catch (IOException e) {
            log.error("Could not run tests", e);
        } finally {
            executionLogger.complete();
        }
    }

    private void runPage(final String testId, Page page) {
        log.debug("running page: {}", page.getName());
        sendStatusUpdate(ServiceUpdate.PAGE_START, testId, page.getId());
        for (Scenario scenario : page.getItems(Scenario.class)) {
            log.debug(">{}", scenario.getName());
            executionLogger.startScenario(scenario);
            runScenario(testId, page, scenario);
        }
        for (Page child : page.getChildren()) {
            runPage(testId, child);
        }
    }

    private void runScenario(String testId, Page page, Scenario scenario) {
        stepScanner.getScenarioSteps(scenario);

        if (scenario.getTests().isEmpty()) {
            runScenarioWithNoData(testId, page,  scenario);
        } else {
            runScenarioWithTestData(testId, page,  scenario);
        }

        sendStatusUpdate(ServiceUpdate.SCENARIO_COMPLETE, testId, page.getId(),scenario.getId());
        executionLogger.finishedScenario(scenario);
    }

    private void runScenarioWithTestData(String testId, Page page, Scenario scenario) {
        // run the scenario once for each data row
        for (ScenarioTest test : scenario.getTests()) {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignore) {}

            for (ScenarioStep step : scenario.getSteps()) {
                log.debug("\t>step '{} {}'", step.getType().name(), step.getText());
                if (step.getRunInformation() != null) {
                    String stepText = step.getType().name() + " " + step.getText();
                    for (String key : test.getData().keySet()) {
                        stepText = stepText.replace(key, test.getData().get(key));
                    }
                    log.debug("\t\t>run '{}'", stepText);
                    sendStatusUpdate(ServiceUpdate.PASSED,
                            testId,page.getId(), scenario.getId(), step.getId(),test.getId());
                } else {
                    sendStatusUpdate(ServiceUpdate.MISSING_STEP, testId, page.getId(),scenario.getId(),
                            step.getId(),test.getId());
                    log.error("Could not find step run information!");
                }

                executionLogger.logStep(step);
            }
        }
    }

    private void runScenarioWithNoData(String testId, Page page, Scenario scenario) {
        for (ScenarioStep step : scenario.getSteps()) {
            log.debug("\t>step '{} {}'", step.getType().name(), step.getText());
            if (step.getRunInformation() != null) {
                log.debug("\t\t>run '{} {}'", step.getType().name(), step.getText());

            } else {
                sendStatusUpdate(ServiceUpdate.MISSING_STEP, testId, page.getId(),scenario.getId(), step.getId(),
                        "");
                log.error("Could not find step run information!");
            }
            sendStatusUpdate(ServiceUpdate.PASSED, testId, page.getId(),scenario.getId(), step.getId(),
                    "");
            executionLogger.logStep(step);
        }
    }


    private void sendStatusUpdate(ServiceUpdate update, String testId, String pageId,
                                  String scenarioId, String stepId,
                                  String scenarioTestId) {

        log.debug("sending {} status update", update.name());
        // todo work out running application URL, probably need to pass it in
        final String restUrl = "http://localhost:8080/rest/runner/" + update.getService() + "/"
                + testId + "/" + pageId + "/" + scenarioId + "/" + stepId + "/" + scenarioTestId;
        log.debug("url: {}", restUrl);
        WebResource webResource = client.resource(restUrl);

       webResource.accept("application/json").get(ClientResponse.class);
    }

    private void sendStatusUpdate(ServiceUpdate update, String testId, String pageId,String scenarioId) {

        log.debug("sending {} status update", update.name());
        // todo work out running application URL, probably need to pass it in
        final String restUrl = "http://localhost:8080/rest/runner/" + update.getService() + "/"
                + testId + "/" + pageId + "/" + scenarioId;
        log.debug("url: {}", restUrl);
        WebResource webResource = client.resource(restUrl);

        webResource.accept("application/json").get(ClientResponse.class);
    }

    private void sendStatusUpdate(ServiceUpdate update, String testId, String pageId) {

        log.debug("sending {} status update", update.name());
        // todo work out running application URL, probably need to pass it in
        final String restUrl = "http://localhost:8080/rest/runner/" + update.getService() + "/"
                + testId + "/" + pageId;
        log.debug("url: {}", restUrl);
        WebResource webResource = client.resource(restUrl);

        webResource.accept("application/json").get(ClientResponse.class);
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