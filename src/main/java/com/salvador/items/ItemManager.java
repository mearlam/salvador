package com.salvador.items;

import com.salvador.pages.Page;
import com.salvador.pages.PageItem;
import com.salvador.scenarios.Scenario;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 21/02/13
 * Time: 04:54
 */
public class ItemManager {

    public void moveItemUp(Page page, String name) {
        int index = 0;
        PageItem item = null;

        for(PageItem tempItem: page.getItems()) {
            if(tempItem.getName().equals(name)) {
                item = tempItem;
                break;
            }
            index++;
        }

        if(item != null && index > 0) {
            page.getItems().remove(index);
            page.getItems().add(index-1,item);
        }
    }

    public void moveItemDown(Page page, String name) {
        int index = 0;
        PageItem item = null;

        for(PageItem tempItem: page.getItems()) {
            if(tempItem.getName().equals(name)) {
                item = tempItem;
                break;
            }
            index++;
        }

        // remove the scenario from the current position
        if(item != null && index < page.getItems().size() -1) {
            page.getItems().remove(index);
            page.getItems().add(index+1,item);
        }
    }
}
