package com.salvador.runners;

import com.salvador.configuration.Configuration;
import com.salvador.loggers.LogReader;
import com.salvador.loggers.SystemOutLogger;
import com.salvador.pages.Page;
import com.salvador.pages.PageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

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

    private StringBuffer logBuffer = new StringBuffer();

    public String runTests() throws TestRunnerException {
        try {
            conversation.begin();
            final Page page = pageManager.getPageFromPageRequestParameter();

            TestRunnerParameters parameters = new TestRunnerParameters();
            parameters.setJavaHome(configuration.getJavaHome());
            parameters.setExecutorClass(configuration.getTestExecutorClass());
            log.info("Running tests with runner '{}'", configuration.getTestRunnerClass());
            log.info("Running tests with executor '{}'", configuration.getTestExecutorClass());

            Class testRunnerClass = Class.forName(configuration.getTestRunnerClass());
            TestRunner testRunner = (TestRunner) testRunnerClass.newInstance();
            testRunner.run(parameters, new LogReader() {
                @Override
                public void addLog(String log) {
                    logBuffer.append(log).append(System.getProperty("line.separator"));
                }
            });

        } catch (Exception e) {
            log.error("Could not start tests", e);
        }

        return "/pages/runner.xhtml";
    }

    public String getLog() {
        return logBuffer.toString();
    }

//    public void setLog(String buffer) {
//    }
}
