package com.salvador.pages;

import com.salvador.common.annotations.AutoTest;
import com.salvador.common.annotations.SkipAutoTest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AutoTest
public class Page implements Serializable {

    private String name;
    private String path;
    private String fullPath;
    private Date created;
    private boolean enabled;
    private boolean isRootPage;
    private List<PageItem> items;
    private transient List<Page> children;

    public Page() {
        created = new Date();
        name = "";
        path = "";
        items = new ArrayList<PageItem>();
        children = new ArrayList<Page>();
        enabled = true;
    }

    public List<PageItem> getItems() {
        return items;
    }

    public void setItems(List<PageItem> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SkipAutoTest
    public Date getCreated() {
        return created;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Page> getChildren() {
        return children;
    }

    public void setChildren(List<Page> children) {
        this.children = children;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public boolean isRootPage() {
        return isRootPage;
    }

    public void setRootPage(boolean rootPage) {
        isRootPage = rootPage;
    }

    public <T> List<T> getItems(Class<T> clazz) {
        List<T> tempItems = new ArrayList<T>();
        for(PageItem item : items) {
            if(item.getClass().equals(clazz)) {
                tempItems.add((T)item);
            }
        }

        return tempItems;
    }

    public PageItem getItem(String name) {
        for(PageItem item : items) {
            if(item.getName() != null && item.getName().equals(name)) {
                return item;
            }
        }

        return null;
    }
}
