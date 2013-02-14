package com.salvador.runners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
    public void run(TestRunnerParameters parameters, LogReader logReader) throws TestRunnerException{

        Runtime rt = Runtime.getRuntime();

        try {

            String command = parameters.getJavaHome() + File.separator + "bin" +
                                                        File.separator + "java ";

            if(parameters.getJavaOptions() != null) {
                command += parameters.getJavaOptions();
            }

            if(parameters.getClassPath() != null) {
                command +=  " -classpath " + parameters.getClassPath();
            }

            command += " " + parameters.getExecutorClass();

            log.info("Running command '{}'", command);

            process = rt.exec(command);
        } catch (IOException e) {
            log.error("Could not execute process",e);
            throw new TestRunnerException("Could not execute process",e);
        }

        StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), logReader);
        StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), logReader);

        errorGobbler.start();
        outputGobbler.start();
    }

    public static void main(String[] args) {

        TestRunnerParameters parameters = new TestRunnerParameters();
        parameters.setClassPath("C:\\mine\\salvador\\target\\classes;C:\\.m2\\repository\\mysql\\mysql-connector-java\\5.1.9\\mysql-connector-java-5.1.9.jar;C:\\.m2\\repository\\org\\hsqldb\\hsqldb\\2.2.6\\hsqldb-2.2.6.jar;C:\\.m2\\repository\\org\\hibernate\\hibernate-core\\4.0.0.Final\\hibernate-core-4.0.0.Final.jar;C:\\.m2\\repository\\commons-collections\\commons-collections\\3.2.1\\commons-collections-3.2.1.jar;C:\\.m2\\repository\\antlr\\antlr\\2.7.7\\antlr-2.7.7.jar;C:\\.m2\\repository\\org\\jboss\\spec\\javax\\transaction\\jboss-transaction-api_1.1_spec\\1.0.0.Final\\jboss-transaction-api_1.1_spec-1.0.0.Final.jar;C:\\.m2\\repository\\dom4j\\dom4j\\1.6.1\\dom4j-1.6.1.jar;C:\\.m2\\repository\\xml-apis\\xml-apis\\1.0.b2\\xml-apis-1.0.b2.jar;C:\\.m2\\repository\\org\\jboss\\logging\\jboss-logging\\3.1.0.CR2\\jboss-logging-3.1.0.CR2.jar;C:\\.m2\\repository\\com\\fasterxml\\classmate\\0.5.4\\classmate-0.5.4.jar;C:\\.m2\\repository\\org\\jboss\\jandex\\1.0.3.Final\\jandex-1.0.3.Final.jar;C:\\.m2\\repository\\org\\hibernate\\common\\hibernate-commons-annotations\\4.0.1.Final\\hibernate-commons-annotations-4.0.1.Final.jar;C:\\.m2\\repository\\javassist\\javassist\\3.12.1.GA\\javassist-3.12.1.GA.jar;C:\\.m2\\repository\\org\\hibernate\\hibernate-entitymanager\\4.0.0.Final\\hibernate-entitymanager-4.0.0.Final.jar;C:\\.m2\\repository\\org\\hibernate\\java-persistence\\jpa-api\\2.0-cr-1\\jpa-api-2.0-cr-1.jar;C:\\.m2\\repository\\commons-logging\\commons-logging\\1.1.1\\commons-logging-1.1.1.jar;C:\\.m2\\repository\\cglib\\cglib\\2.2\\cglib-2.2.jar;C:\\.m2\\repository\\asm\\asm\\3.1\\asm-3.1.jar;C:\\.m2\\repository\\org\\slf4j\\slf4j-api\\1.6.4\\slf4j-api-1.6.4.jar;C:\\.m2\\repository\\log4j\\log4j\\1.2.14\\log4j-1.2.14.jar;C:\\.m2\\repository\\org\\slf4j\\slf4j-log4j12\\1.6.4\\slf4j-log4j12-1.6.4.jar;C:\\.m2\\repository\\com\\sun\\faces\\jsf-api\\2.1.0-b03\\jsf-api-2.1.0-b03.jar;C:\\.m2\\repository\\com\\sun\\faces\\jsf-impl\\2.1.0-b03\\jsf-impl-2.1.0-b03.jar;C:\\.m2\\repository\\org\\primefaces\\primefaces\\3.3\\primefaces-3.3.jar;C:\\.m2\\repository\\javax\\servlet\\jstl\\1.2\\jstl-1.2.jar;C:\\.m2\\repository\\javax\\inject\\javax.inject\\1\\javax.inject-1.jar;C:\\.m2\\repository\\org\\jboss\\weld\\servlet\\weld-servlet\\1.1.10.Final\\weld-servlet-1.1.10.Final.jar;C:\\.m2\\repository\\com\\thoughtworks\\xstream\\xstream\\1.2.2\\xstream-1.2.2.jar;C:\\.m2\\repository\\xpp3\\xpp3_min\\1.1.3.4.O\\xpp3_min-1.1.3.4.O.jar;C:\\.m2\\repository\\commons-io\\commons-io\\2.0.1\\commons-io-2.0.1.jar;");
        parameters.setJavaHome(System.getenv().get("JAVA_HOME"));
        parameters.setExecutorClass("com.salvador.executors.DefaultExecutor");

        LogReader logReader = new LogReader() {
            @Override
            public void addLog(String log) {
                System.out.println(log);
            }
        };

        JavaTestRunner runner = new JavaTestRunner();
        runner.run(parameters,logReader);
    }
}
