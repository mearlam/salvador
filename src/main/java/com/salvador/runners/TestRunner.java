package com.salvador.runners;

import com.salvador.loggers.LogReader;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/02/13
 * Time: 14:53
 */
public interface TestRunner {

    void run(TestRunnerParameters parameters,LogReader logReader) throws TestRunnerException;
}
