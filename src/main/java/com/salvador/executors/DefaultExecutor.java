package com.salvador.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/02/13
 * Time: 15:36
 */
public class DefaultExecutor implements TestExecutor {

    final Logger log = LoggerFactory.getLogger(DefaultExecutor.class);

    @Override
    public void execute() {
        log.info("Executing tests");
    }

    public static void main(String[] args) {
        DefaultExecutor executor = new DefaultExecutor();
        executor.execute();
    }
}
