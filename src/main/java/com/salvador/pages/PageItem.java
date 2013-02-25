package com.salvador.pages;

import com.salvador.common.annotations.AutoTest;
import com.salvador.common.annotations.SkipAutoTest;

import java.io.Serializable;
import java.lang.String;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 21/02/13
 * Time: 04:07
 */
@AutoTest
public class PageItem implements Serializable {

    private String id;
    private String name;
    private boolean enabled;
    private boolean canBeDisabled;

    public PageItem() {
        id = UUID.randomUUID().toString();
        enabled = true;
        canBeDisabled = true;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SkipAutoTest
    public String getId() {
        return id;
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

    public boolean isCanBeDisabled() {
        return canBeDisabled;
    }

    public void setCanBeDisabled(boolean canBeDisabled) {
        this.canBeDisabled = canBeDisabled;
    }
}
