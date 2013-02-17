package com.salvador.executors;

import com.salvador.scenarios.Scenario;
import com.salvador.scenarios.ScenarioStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 17/02/13
 * Time: 19:33
 */
public class DefaultExecutionLogger implements ExecutionLogger {

    static final Logger log = LoggerFactory.getLogger(DefaultExecutionLogger.class);

    PrintWriter out;

    public DefaultExecutionLogger(String home) {
        try {
            final String outputFile = home + File.separator + "test-output.log";
            out = new PrintWriter(outputFile);
            log.debug("Writing output to {}", outputFile);
        } catch (FileNotFoundException e) {
            log.error("Could not create log file", e);
        }
    }

    @Override
    public void startScenario(Scenario scenario) {
        out.println("start:" + scenario.getName());
    }

    @Override
    public void logStep(ScenarioStep step) {
        out.println(step.getText());
    }

    @Override
    public void finishedScenario(Scenario scenario) {
        out.println("stop:" + scenario.getName());
    }

    @Override
    public void complete() {
        out.println("complete");
        out.flush();
        out.close();
    }
}
