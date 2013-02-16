package com.salvador.loggers;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/02/13
 * Time: 15:28
 */
public interface LogReader {

    void addLog(String log);
    String getLog();
}
