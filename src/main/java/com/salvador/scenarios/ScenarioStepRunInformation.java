package com.salvador.scenarios;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 17/02/13
 * Time: 18:54
 */
public class ScenarioStepRunInformation {

    private Class classToRun;
    private Method methodToRun;


    public Class getClassToRun() {
        return classToRun;
    }

    public void setClassToRun(Class classToRun) {
        this.classToRun = classToRun;
    }

    public Method getMethodToRun() {
        return methodToRun;
    }

    public void setMethodToRun(Method methodToRun) {
        this.methodToRun = methodToRun;
    }

    @Override
    public String toString() {
        return "ScenarioStepRunInformation{" +
                "classToRun=" + classToRun +
                ", methodToRun=" + methodToRun +
                '}';
    }
}
