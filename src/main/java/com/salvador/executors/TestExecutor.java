package com.salvador.executors;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/02/13
 * Time: 15:35
 */
public interface TestExecutor {

    /**
     * Runs the java main class that kicks off the tests
     *
     * @param home   Where the pages are stored
     * @param testId The id of the tests to update status on
     * @param page   The root page we are running
     */
    void execute(String home, String testId, String page);
}
