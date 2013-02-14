package com.salvador.configuration;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.File;
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

    @PostConstruct
    public void init() {
        home = "C:\\mine\\salvador\\home\\";
    }

    public String getHome() {
        return home;
    }

    public String getPagesHome() {
        return home + "pages" + File.separator;
    }
}
