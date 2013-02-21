package com.salvador.loggers;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.
 * User: mearlam
 * Date: 21/02/13
 * Time: 14:16
 */
public class HtmlStringLoggerTest {

    HtmlStringLogger htmlStringLogger;

    @Before
    public void setUp() {
        htmlStringLogger = new HtmlStringLogger();
    }

    @Test
    public void testAddLog() {
        htmlStringLogger.addLog("this is a log statement");
        assertThat(htmlStringLogger.getLog(),is("this is a log statement<br/>"));
    }
}
