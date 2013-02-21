package com.salvador.loggers;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.
 * User: mearlam
 * Date: 21/02/13
 * Time: 14:19
 */
public class SystemOutLoggerTest {

    SystemOutLogger systemOutLogger;

    @Before
    public void setUp() {
        systemOutLogger = new SystemOutLogger();
    }

    @Test
    public void testAddLog() {
        systemOutLogger.addLog("this is a log statement");
        assertThat(systemOutLogger.getLog(),is(""));
    }
}
