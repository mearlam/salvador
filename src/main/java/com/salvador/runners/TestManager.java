package com.salvador.runners;

import com.salvador.pages.Page;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 25/02/13
 * Time: 17:19
 */
@ApplicationScoped
public class TestManager implements Serializable {

    private Map<String,TestSuite> suites;

    @PostConstruct
    public void init() {
        suites = new LinkedHashMap<String, TestSuite>();
    }

    public TestSuite newSuite(Page page) {
        TestSuite suite = new TestSuite();
        suite.setPage(page);
        suites.put(suite.getId(),suite);

        return suite;
    }

    public Map<String, TestSuite> getSuites() {
        return suites;
    }

    public void setSuites(Map<String, TestSuite> suites) {
        this.suites = suites;
    }
}
