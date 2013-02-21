package com.salvador.notes;

import com.salvador.common.annotations.AutoTest;
import com.salvador.pages.PageItem;

/**
 * Created with IntelliJ IDEA.
 * User: mark
 * Date: 21/02/13
 * Time: 04:32
 */
@AutoTest
public class PageNote extends PageItem {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
