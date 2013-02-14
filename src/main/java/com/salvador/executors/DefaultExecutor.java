package com.salvador.executors;

import com.salvador.scanners.DefaultStepScanner;
import com.salvador.scanners.StepScanner;
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

    StepScanner stepScanner = new DefaultStepScanner();

    @Override
    public void execute() {
        log.info("Executing test");
    }

    public static void main(String[] args) {
        DefaultExecutor executor = new DefaultExecutor();
        executor.execute();
    }
}
