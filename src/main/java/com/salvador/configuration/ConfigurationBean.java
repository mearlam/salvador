package com.salvador.configuration;

import com.salvador.spi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 15/02/13
 * Time: 11:52
 */
@Named
@ViewScoped
public class ConfigurationBean implements Serializable {

    @Inject
    Configuration configuration;

    private String variableName;
    private String variableValue;


    @Inject
    transient ConfigurationManager configurationManager;

    public void update() throws IOException {
        // copy the config to a savable format or it will just be a weld object
        Configuration saveConfiguration = new Configuration();
        configuration.deepCopy(configuration,saveConfiguration);
        configurationManager.save(configuration.getHome(),saveConfiguration);
    }

    public void removeVariable(String key) throws IOException {
        if(configuration.getVariables().containsKey(key)) {
            configuration.getVariables().remove(key);
            update();
        }
    }

    public void addVariable() throws IOException {
        configuration.getVariables().put(variableName,variableValue);
        update();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getVariableValue() {
        return variableValue;
    }

    public void setVariableValue(String variableValue) {
        this.variableValue = variableValue;
    }
}
