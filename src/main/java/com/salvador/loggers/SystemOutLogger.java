package com.salvador.loggers;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 14/02/13
 * Time: 17:28
 * To change this template use File | Settings | File Templates.
 */
public class SystemOutLogger implements LogReader {

    @Override
    public void addLog(String log) {
        System.out.println(log);
    }

    @Override
    public String getLog() {
        return "";
    }
}
