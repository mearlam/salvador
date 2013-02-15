package com.salvador.configuration;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 15/02/13
 * Time: 11:52
 */
@Named
@RequestScoped
public class ConfigurationBean {

    @Inject
    Configuration configuration;

    public String update() {
        // todo Configuration save to disk
        return "";
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
