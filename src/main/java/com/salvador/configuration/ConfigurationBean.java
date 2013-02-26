package com.salvador.configuration;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 15/02/13
 * Time: 11:52
 */
@Named
@RequestScoped
public class ConfigurationBean {

    Configuration configuration;

    @Inject
    ConfigurationManager configurationManager;

    public String update() throws IOException {
        configurationManager.save(configuration);
        return "";
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
