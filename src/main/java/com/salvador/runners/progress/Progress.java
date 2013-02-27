package com.salvador.runners.progress;

import com.salvador.pages.Page;
import com.salvador.pages.PageItem;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: mearlam
 * Date: 27/02/13
 * Time: 08:00
 */
public abstract class Progress implements Serializable {

    private Page page;
    private PageItem item;
    private boolean complete;
    private boolean passed;
    private boolean missing;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public PageItem getItem() {
        return item;
    }

    public void setItem(PageItem item) {
        this.item = item;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public boolean isMissing() {
        return missing;
    }

    public void setMissing(boolean missing) {
        this.missing = missing;
    }
}
