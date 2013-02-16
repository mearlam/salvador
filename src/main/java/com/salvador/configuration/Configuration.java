package com.salvador.configuration;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/02/13
 * Time: 11:13
 */
@ApplicationScoped
public class Configuration implements Serializable {

    private String home;
    private String javaHome;
    private String testRunnerClass;
    private String testExecutorClass;

    @PostConstruct
    public void init() {
        home = "C:\\mine\\salvador\\home\\";
        javaHome = System.getenv().get("JAVA_HOME");
        testRunnerClass = "com.salvador.runners.JavaTestRunner";
        testExecutorClass = "com.salvador.executors.DefaultExecutor";
    }

    public String getHome() {
        return home;
    }

    public String getTestRunnerClass() {
        return testRunnerClass;
    }

    public void setTestRunnerClass(String testRunnerClass) {
        this.testRunnerClass = testRunnerClass;
    }

    public String getJavaHome() {
        return javaHome;
    }

    public void setJavaHome(String javaHome) {
        this.javaHome = javaHome;
    }

    public String getTestExecutorClass() {
        return testExecutorClass;
    }

    public void setTestExecutorClass(String testExecutorClass) {
        this.testExecutorClass = testExecutorClass;
    }
}
