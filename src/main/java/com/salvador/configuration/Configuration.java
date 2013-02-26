package com.salvador.configuration;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/02/13
 * Time: 11:13
 */
@ApplicationScoped
public class Configuration implements Serializable {

    private transient String home;
    private String javaHome;
    private String javaClasspath;
    private Map<String, String> variables;
    private String testRunnerClass;
    private String testExecutorClass;
    private transient boolean newConfiguration;

    @Inject
    ConfigurationManager configurationManager;

    public Configuration() {
        newConfiguration = true;
        variables = new LinkedHashMap<String, String>();
    }

    @PostConstruct
    public void init() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        home = ctx.getExternalContext().getInitParameter("com.salvador.home");

        javaHome = System.getenv().get("JAVA_HOME");
        testRunnerClass = "com.salvador.runners.JavaTestRunner";
        testExecutorClass = "com.salvador.executors.DefaultExecutor";

        Configuration loadedConfiguration = configurationManager.load(home);
        if (!loadedConfiguration.isNewConfiguration()) {
            deepCopy(loadedConfiguration, this);
        }
    }

    public String getJavaClasspath() {
        return javaClasspath;
    }

    public void setJavaClasspath(String javaClasspath) {
        this.javaClasspath = javaClasspath;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

    public boolean isNewConfiguration() {
        return newConfiguration;
    }

    public void setNewConfiguration(boolean newConfiguration) {
        this.newConfiguration = newConfiguration;
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

    public void deepCopy(Configuration from, Configuration to) {
        to.setJavaHome(from.getJavaHome());
        to.setTestExecutorClass(from.getTestExecutorClass());
        to.setTestRunnerClass(from.getTestRunnerClass());

        if (from.getVariables() != null) {
            if(to.getVariables() != null) {
                to.getVariables().clear();
            }
            for (String key : from.getVariables().keySet()) {
                to.getVariables().put(key, from.getVariables().get(key));
            }
        }
    }
}
