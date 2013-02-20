package com.salvador.scanners;

import com.salvador.annotations.Step;
import com.salvador.annotations.Steps;
import com.salvador.scenarios.Scenario;
import com.salvador.scenarios.ScenarioStep;
import com.salvador.scenarios.ScenarioStepRunInformation;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 14/02/13
 * Time: 17:35
 */
public class DefaultStepScanner implements StepScanner {

    static final Logger log = LoggerFactory.getLogger(DefaultStepScanner.class);

    Map<String, ScenarioStepRunInformation> stepMethods;

    @Override
    public List<Class> getStepClasses() {
        Reflections reflections = new Reflections("test.steps");
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Steps.class);

        return new ArrayList(classes);
    }

    @Override
    public void getScenarioSteps(Scenario scenario) {

        if (stepMethods == null) {
            getAllMethods();
        }

        for (ScenarioStep step : scenario.getSteps()) {
            log.debug("Scanning for step '{}'", step.getText());
            if (stepMethods.containsKey(step.getText())) {
                log.debug("Found in {}", stepMethods.get(step.getText()));
                step.setRunInformation(stepMethods.get(step.getText()));
            }
        }
    }

    private void getAllMethods() {
        final List<Class> stepClasses = getStepClasses();
        stepMethods = new LinkedHashMap<String, ScenarioStepRunInformation>();

        for (Class stepClass : stepClasses) {
            final Method[] methods = stepClass.getMethods();
            for (Method method : methods) {
                final Step annotation = method.getAnnotation(Step.class);
                if (annotation != null) {
                    log.debug("Found step '{}' in method {} in class {}",
                            new Object[]{annotation.value(), method.getName(), stepClass});
                    ScenarioStepRunInformation information = new ScenarioStepRunInformation();
                    information.setClassToRun(stepClass);
                    information.setMethodToRun(method);
                    stepMethods.put(annotation.value(), information);
                }
            }
        }
    }
}
