package com.salvador.pages;

import com.salvador.scenarios.Scenario;

import javax.enterprise.context.RequestScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Page implements Serializable {

    private String name;
    private String path;
    private String fullPath;
    private Date created;
    private boolean enabled;
    private List<Scenario> scenarios;
    private List<Page> children;

    public Page() {
        created = new Date();
        scenarios = new ArrayList<Scenario>();
        children = new ArrayList<Page>();
        enabled = true;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Page> getChildren() {
        return children;
    }

    public void setChildren(List<Page> children) {
        this.children = children;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }
}
