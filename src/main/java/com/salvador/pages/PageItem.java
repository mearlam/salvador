package com.salvador.pages;

import java.io.Serializable;
import java.lang.String;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 21/02/13
 * Time: 04:07
 */
public class PageItem implements Serializable {

    private String name;
    private boolean enabled;

    public PageItem() {
        enabled = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
