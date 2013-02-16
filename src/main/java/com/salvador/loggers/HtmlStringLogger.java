package com.salvador.loggers;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 16/02/13
 * Time: 08:15
 */
public class HtmlStringLogger implements LogReader {

    private StringBuffer logBuffer = new StringBuffer();

    @Override
    public void addLog(String log) {
        logBuffer.append(log).append("<br/>");
    }

    public String getLog() {
        return logBuffer.toString();
    }

}
