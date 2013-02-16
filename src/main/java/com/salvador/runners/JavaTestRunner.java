package com.salvador.runners;

import com.salvador.loggers.LogReader;
import com.salvador.loggers.SystemOutLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: MEarlam
 * Date: 14/02/13
 * Time: 15:18
 */
public class JavaTestRunner implements TestRunner {

    final Logger log = LoggerFactory.getLogger(JavaTestRunner.class);

    Process process;

    @Override
    public void run(TestRunnerParameters parameters, LogReader logReader, ProcessListener processListener) throws TestRunnerException {

        Runtime rt = Runtime.getRuntime();

        try {

            // todo change to a builder
            String command = parameters.getJavaHome() + File.separator + "bin" +
                    File.separator + "java ";

            if (parameters.getJavaOptions() != null) {
                command += parameters.getJavaOptions();
            }

            if (parameters.getClassPath() != null) {
                command += " -classpath " + parameters.getClassPath();
            } else {
                command += " -classpath ";
            }

            command += getLibsFromHome(parameters);
            command += " " + parameters.getExecutorClass();
            command += " " + parameters.getHome();
            if (parameters.getPage() != null) {
                command += " " + parameters.getPage();
            }

            log.info("Running command '{}'", command);

            process = rt.exec(command);

            // listens to see when the tests finish
            ProcessExitDetector processExitDetector = new ProcessExitDetector(process);
            processExitDetector.addProcessListener(processListener);
            processExitDetector.start();

        } catch (IOException e) {
            log.error("Could not execute process", e);
            throw new TestRunnerException("Could not execute process", e);
        }

        StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), logReader);
        StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), logReader);

        errorGobbler.start();
        outputGobbler.start();
    }

    private String getLibsFromHome(TestRunnerParameters parameters) {
        File libDirectory = new File(parameters.getHome() + File.separator + "libs");
        StringBuilder builder = new StringBuilder();
        String delim = "";
        if (libDirectory.exists()) {
            for (String file : libDirectory.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith("jar");
                }
            })) {
                builder.append(delim).
                        append(parameters.getHome()).
                        append(File.separator).
                        append("libs").
                        append(File.separator).
                        append(file);
                delim = ";";
            }
        }

        log.debug("Adding classpath from libs '{}'", builder.toString());
        return builder.toString();
    }


    public static void main(String[] args) {

        TestRunnerParameters parameters = new TestRunnerParameters();
        parameters.setJavaHome(System.getenv().get("JAVA_HOME"));
        parameters.setHome("C:\\mine\\salvador\\home");
        parameters.setPage("Page1");
        parameters.setExecutorClass("com.salvador.executors.DefaultExecutor");

        LogReader logReader = new SystemOutLogger();

        JavaTestRunner runner = new JavaTestRunner();
        runner.run(parameters, logReader, new ProcessListener() {
            @Override
            public void processFinished(Process process) {
                System.out.println("tests finished");
            }
        });
    }
}
