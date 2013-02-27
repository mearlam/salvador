package com.salvador.scenarios;

import com.salvador.common.annotations.AutoTest;
import com.salvador.common.annotations.SkipAutoTest;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 12/02/13
 * Time: 20:11
 *
 */
@AutoTest
public class ScenarioStep implements Serializable {

    private String id;
    private ScenarioStepType type;
    private String text;
    private boolean common;
    private ScenarioStepRunInformation runInformation;

    public ScenarioStep() {
        id = UUID.randomUUID().toString();
    }

    @SkipAutoTest
    public String getId() {
        return id;
    }

    public ScenarioStepType getType() {
        return type;
    }

    @SkipAutoTest
    public void setType(String type) {
        this.type = Enum.valueOf(ScenarioStepType.class,type);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ScenarioStepRunInformation getRunInformation() {
        return runInformation;
    }

    public void setRunInformation(ScenarioStepRunInformation runInformation) {
        this.runInformation = runInformation;
    }

    public boolean isCommon() {
        return common;
    }

    public void setCommon(boolean common) {
        this.common = common;
    }
}
