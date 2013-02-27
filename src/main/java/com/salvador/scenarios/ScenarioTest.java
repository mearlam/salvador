package com.salvador.scenarios;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: mearlam
 * Date: 27/02/13
 * Time: 10:38
 */
public class ScenarioTest implements Serializable {

    private String id;
    private String text;
    private Map<String,String> data;

    public ScenarioTest() {
        id = UUID.randomUUID().toString();
        data = new LinkedHashMap<String, String>();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
