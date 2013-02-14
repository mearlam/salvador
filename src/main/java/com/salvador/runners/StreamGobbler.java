package com.salvador.runners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 20/09/11
 * Time: 15:53
 *
 * Used for grabbing output from another process
 */
public class StreamGobbler extends Thread {

    final Logger log = LoggerFactory.getLogger(StreamGobbler.class);

    InputStream is;
    LogReader logReader;

    StreamGobbler(InputStream is, LogReader logReader) {
        this.is = is;
        this.logReader = logReader;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is,"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                logReader.addLog(line);
            }
        } catch (IOException ioe) {
            log.error("Could not read from stream",ioe);
        }
    }
}
