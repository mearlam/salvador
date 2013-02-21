package com.salvador.executors;

import com.salvador.pages.Page;
import com.salvador.pages.PageManager;
import com.salvador.scanners.DefaultStepScanner;
import com.salvador.scanners.StepScanner;
import com.salvador.scenarios.Scenario;
import com.salvador.scenarios.ScenarioStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/02/13
 * Time: 15:36
 *
 * Runs inside its own JVM
 */
public class DefaultExecutor implements TestExecutor {

    static final Logger log = LoggerFactory.getLogger(DefaultExecutor.class);

    ExecutionLogger executionLogger;
    PageManager pageManager = new PageManager();

    private StepScanner stepScanner;

    List<Scenario> scenarios = new ArrayList<Scenario>();

    @Override
    public void execute(String home, String pagePath) {
        log.info("Executing test");
        executionLogger = new DefaultExecutionLogger(home);

        try {
            final Page page = pageManager.getPage(home, pagePath);
            log.info("Test page: '{}'", page.getFullPath());
            log.info("Scenarios: '{}'", page.getItems(Scenario.class).size());
            log.info("Child pages: '{}'", page.getChildren().size());
            getScenarios(page);
            log.info("Scenarios to run {}", scenarios.size());

            stepScanner = new DefaultStepScanner();
            for(Scenario scenario :scenarios) {
                log.debug(">{}", scenario.getName());
                executionLogger.startScenario(scenario);
                runScenario(scenario);
            }

        } catch (IOException e) {
            log.error("Could not run tests", e);
        }finally {
            executionLogger.complete();
        }
    }

    private void runScenario(Scenario scenario) {
        stepScanner.getScenarioSteps(scenario);

        for(ScenarioStep step : scenario.getSteps()) {
            log.debug("Running step '{} {}'",step.getType().name(), step.getText());
            if(step.getRunInformation() != null) {
                log.debug("Found step run information");
            }else {
                log.error("Could not find step run information!");
            }
            executionLogger.logStep(step);
        }

        executionLogger.finishedScenario(scenario);
    }

    private void getScenarios(Page page) {

        scenarios.addAll(page.getItems(Scenario.class));

        for(Page childPage: page.getChildren()) {
            getScenarios(childPage);
        }
    }


    public static void main(String[] args) {

        if (args.length == 1) {
            DefaultExecutor executor = new DefaultExecutor();
            executor.execute(args[0], null);
        } else if (args.length == 2) {
            DefaultExecutor executor = new DefaultExecutor();
            executor.execute(args[0], args[1]);
        } else {
            log.error("You must supply home");
        }
    }
}
