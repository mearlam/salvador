package com.salvador.runners;

import com.salvador.configuration.Configuration;
import com.salvador.loggers.HtmlStringLogger;
import com.salvador.loggers.LogReader;
import com.salvador.loggers.SystemOutLogger;
import com.salvador.pages.Page;
import com.salvador.pages.PageManager;
import com.salvador.scenarios.ScenarioStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 15/02/13
 * Time: 09:16
 */
@Named
@ConversationScoped
public class TestRunnerBean implements Serializable {

    final Logger log = LoggerFactory.getLogger(TestRunnerBean.class);

    @Inject
    Conversation conversation;

    @SuppressWarnings("CdiUnproxyableBeanTypesInspection")
    @Inject
    transient PageManager pageManager;

    @Inject
    transient Configuration configuration;

    transient private LogReader logReader;
    private boolean running;
    TestRunner testRunner;

    private ProcessListener processListener;
    private List<ScenarioStep> stepsRun;

    public String runTests() throws TestRunnerException {
        try {
            conversation.begin();

            processListener = new ProcessListener() {
                @Override
                public void processFinished(Process process) {
                    if (running) {
                        log.debug("tests finished");
                        running = false;
                    }
                }
            };

            final Page page = pageManager.getPageFromPageRequestParameter();

            final TestRunnerParameters parameters = new TestRunnerParameters();
            parameters.setJavaHome(configuration.getJavaHome());
            parameters.setExecutorClass(configuration.getTestExecutorClass());
            parameters.setHome(configuration.getHome());
            parameters.setPage(page.getName());
            log.info("Running tests with runner '{}'", configuration.getTestRunnerClass());
            log.info("Running tests with executor '{}'", configuration.getTestExecutorClass());

            final Class testRunnerClass = Class.forName(configuration.getTestRunnerClass());
            testRunner = (TestRunner) testRunnerClass.newInstance();

            logReader = new HtmlStringLogger();

            Thread thread = new Thread() {
                public void run() {
                    log.debug("Starting test runner");
                    testRunner.run(parameters, logReader, processListener);
                    running = true;
                }
            };

            thread.start();


        } catch (Exception e) {
            log.error("Could not start tests", e);
        }

        return "/pages/runner.xhtml";
    }

    public String getLog() {
        return logReader.getLog();
    }

    public boolean isRunning() {
        return running;
    }

    public List<ScenarioStep> getStepsRun() {
        return stepsRun;
    }

    public void setStepsRun(List<ScenarioStep> stepsRun) {
        this.stepsRun = stepsRun;
    }
}
