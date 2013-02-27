package com.salvador.runners;

import com.salvador.loggers.LogReader;
import com.salvador.pages.Page;
import com.salvador.pages.PageItem;
import com.salvador.runners.progress.Progress;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.Serializable;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 25/02/13
 * Time: 17:20
 */
public class TestSuite implements Serializable {

    private String id;
    private boolean running;
    private int testsToRun;
    private Map<String,Page> pages;
    private List<Progress> progress;

    // can be used to print info back to the screen
    private transient LogReader logReader;

    public TestSuite() {
        id = UUID.randomUUID().toString();
        progress = new ArrayList<Progress>();
        pages = new LinkedHashMap<String, Page>();
    }

    public LogReader getLogReader() {
        return logReader;
    }

    public void setLogReader(LogReader logReader) {
        this.logReader = logReader;
    }

    public int getTestsToRun() {
        return testsToRun;
    }

    public void setTestsToRun(int testsToRun) {
        this.testsToRun = testsToRun;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Page getPage(final String id) {
        return pages.get(id);
    }

    public void setPage(Page page) {
        flattenPages(page);
    }

    private void flattenPages(Page page) {
        pages.put(page.getId(),page);
        for(Page child : page.getChildren()) {
            flattenPages(child);
        }
    }

    public List<Page> getPages() {
        return new ArrayList<Page>(pages.values());
    }

    public List<Progress> getProgress() {
        return progress;
    }

    public Progress getProgress(String id) {
        for(Progress tempProgress : progress) {
            if(tempProgress.getItem() != null
                    && tempProgress.getItem().getId().equals(id)) {
                return tempProgress;
            }
        }

        return null;
    }

    public void setProgress(List<Progress> progress) {
        this.progress = progress;
    }
}
