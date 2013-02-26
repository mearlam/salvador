package com.salvador.runners;

import com.salvador.pages.Page;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 25/02/13
 * Time: 17:20
 */
public class TestSuite implements Serializable {

    private String id;
    private boolean running;
    private Page page;

    public TestSuite() {
        id = UUID.randomUUID().toString();
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

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
