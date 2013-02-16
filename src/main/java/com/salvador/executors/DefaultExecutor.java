package com.salvador.executors;

import com.salvador.pages.Page;
import com.salvador.pages.PageManager;
import com.salvador.scenarios.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/02/13
 * Time: 15:36
 */
public class DefaultExecutor implements TestExecutor {

    static final Logger log = LoggerFactory.getLogger(DefaultExecutor.class);

    PageManager pageManager = new PageManager();

    List<Scenario> scenarios = new ArrayList<Scenario>();

    @Override
    public void execute(String home, String pagePath) {
        log.info("Executing test");
        try {
            final Page page = pageManager.getPage(home, pagePath);
            log.info("Test page: '{}'", page.getFullPath());
            log.info("Scenarios: '{}'", page.getScenarios().size());
            log.info("Child pages: '{}'", page.getChildren().size());
            getScenarios(page);
            log.info("Scenarios to run {}", scenarios.size());

            for(Scenario scenario :scenarios) {
                log.debug(">{}", scenario.getName());
            }

        } catch (IOException e) {
            log.error("Could not run tests", e);
        }
    }

    private void getScenarios(Page page) {

        scenarios.addAll(page.getScenarios());

        for(Page childPage: page.getChildren()) {
            getScenarios(childPage);
        }
    }


    public static void main(String[] args) {

        if (args.length == 1) {
            DefaultExecutor executor = new DefaultExecutor();
            executor.execute(args[0], null);
        } else if (args.length == 2) {
            DefaultExecutor executor = new DefaultExecutor();
            executor.execute(args[0], args[1]);
        } else {
            log.error("You must supply home");
        }
    }
}
