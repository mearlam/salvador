package com.salvador.runners;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/02/13
 * Time: 15:20
 */
public class TestRunnerException extends RuntimeException {

    public TestRunnerException(String message) {
        super(message);
    }

    public TestRunnerException(String message, Throwable cause) {
        super(message, cause);
    }
}
