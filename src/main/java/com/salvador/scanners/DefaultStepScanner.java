package com.salvador.scanners;

import com.salvador.annotations.Steps;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 14/02/13
 * Time: 17:35
 *
 */
public class DefaultStepScanner implements StepScanner {

    @Override
    public List<Class> scan() {
        Reflections reflections = new Reflections("test.steps");
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Steps.class);

        return new ArrayList(classes);
    }
}
