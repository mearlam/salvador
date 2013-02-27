package com.salvador.services;

/**
 * Created by IntelliJ IDEA.
 * User: mearlam
 * Date: 27/02/13
 * Time: 08:07
 */
public enum ServiceUpdate {

    PASSED("passed"),
    MISSING_STEP("missing"),
    SCENARIO_COMPLETE("scenario-complete"),
    PAGE_START("page-start");

    private String service;

    private ServiceUpdate(String service) {
        this.service = service;
    }

    public String getService() {
        return service;
    }
}
